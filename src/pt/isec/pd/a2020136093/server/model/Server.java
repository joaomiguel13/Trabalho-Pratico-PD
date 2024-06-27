package pt.isec.pd.a2020136093.server.model;

import pt.isec.pd.a2020136093.client.rmi.RMI_CLIENT_INTERFACE;
import pt.isec.pd.a2020136093.server.model.data.Heartbeat;
import pt.isec.pd.a2020136093.server.model.rmi.RMI_SERVER;
import pt.isec.pd.a2020136093.server.rmi_backup.RMI_SERVER_BACKUP_INTERFACE;
import pt.isec.pd.a2020136093.server.threads.Multicast_SendHeartbeat;
import pt.isec.pd.a2020136093.server.threads.ProcessClientRequest;
import pt.isec.pd.a2020136093.server.model.jdbc.ManageDB;

import java.io.*;
import java.net.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

import static pt.isec.pd.a2020136093.server.model.data.CONSTANTS.*;

public class Server {
    ManageDB manageDB;
    private final int PORT;
    private final String DB_PATH;
    private final String RMI_NAME;
    private final int RMI_PORT;

    private ArrayList<String> loggedIn;

    Heartbeat serverData;

    RMI_SERVER rmiServer;

    List<RMI_CLIENT_INTERFACE> observers_clients;
    List<RMI_SERVER_BACKUP_INTERFACE> observers_backups;

    public Server(int port, String dbPath, String rmiName, int rmiPort) {
        this.PORT = port;
        this.DB_PATH = dbPath;
        this.RMI_NAME = rmiName;
        this.RMI_PORT = rmiPort;

        this.loggedIn = new ArrayList<>();

        this.observers_clients = new ArrayList<>();
        this.observers_backups = new ArrayList<>();

        manageDB = new ManageDB(DB_PATH, observers_backups, observers_clients);

        if (manageDB.connectDB()) {
            System.out.println("Conectado à base de dados");
            try {
                manageDB.createDB();
                serverData = new Heartbeat(InetAddress.getLocalHost().getHostAddress(), PORT, manageDB.getDB_version(), RMI_NAME, RMI_PORT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println("Não foi possível conectar à base de dados");
            System.exit(-1);
        }
    }

    public void start() {

        // ================================= MULTICAST =================================
        MulticastSocket multicastSocket;
        try {
            //Converte a string IP para um objeto InetAddress
            InetAddress group = InetAddress.getByName(MULTICAST_IP);
            int port = MULTICAST_PORT;
            //Cria um objeto NetworkInterface para definir a interface de rede
            NetworkInterface nif;
            //Converte a string NETWORK_INTERFACE_NAME para um objeto NetworkInterface
            try{
                nif = NetworkInterface.getByInetAddress(InetAddress.getByName(NETWORK_INTERFACE_NAME));
            } catch (SocketException | UnknownHostException e) {
                nif = NetworkInterface.getByName(NETWORK_INTERFACE_NAME);
            }

            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(new InetSocketAddress(group, port), nif);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new Multicast_SendHeartbeat(serverData).start();    // ENVIAR HEARBEAT PARA MULTICAST


        // ================================= RMI =================================
        /*
         * Se existirem varias interfaces de rede activas na maquina onde corre esta aplicacao,
         * convem definir de forma explicita o endereco que deve ser incluido na referencia remota do servico
         * RMI criado. Para o efeito, o endereco deve ser atribuido 'a propriedade java.rmi.server.hostname.
         *
         * Pode ser no codigo atraves do metodo System.setProperty():
         *      - System.setProperty("java.rmi.server.hostname", "10.65.129.232"); //O endereco usado e' apenas um exemplo
         *      - System.setProperty("java.rmi.server.hostname", args[3]); //Neste caso, assume-se que o endereco e' passado como quarto argumento na linha de comando
         *
         * Tambem pode ser como opcao passada 'a maquina virtual Java:
         *      - java -Djava.rmi.server.hostname=10.202.128.22 GetRemoteFileClient ... //O endereco usado e' apenas um exemplo
         *      - No Netbeans: Properties -> Run -> VM Options -> -Djava.rmi.server.hostname=10.202.128.22 //O endereco usado e' apenas um exemplo
         */


        try{
            try{
                //Cria um registry local onde vão ser registados os serviços RMI
                LocateRegistry.createRegistry(RMI_PORT);
                System.out.println("Registry lançado!");
            }
            catch (RemoteException e) {
                System.out.println("Registry já em execução!");
                System.exit(-1);
            }
            /*
             * Cria o servico.
             */
            rmiServer = new RMI_SERVER(observers_clients, observers_backups);
            System.out.println("Service RMI criado! [" + rmiServer.getRef().remoteToString() + "]");

            /*
             * Regista o servico no rmiregistry local para que possam localiza'-lo, ou seja,
             * obter a sua referencia remota (endereco IP, porto de escuta, etc.).
             */
            Naming.rebind("rmi://"+ "localhost" + "/" + RMI_NAME, rmiServer);
            System.out.println("Service RMI registado no registry!");

            /*
             * Para terminar um servico RMI do tipo UnicastRemoteObject:
             *
             *  UnicastRemoteObject.unexportObject(fileService, true).
             */
        }
        catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
            System.exit(-1);
        }
        catch(Exception e){
            System.out.println("Erro - " + e);
            System.exit(-1);
        }



        // ================================= CRIAR SERVER SOCKET E ACEITAR CLIENTES =================================
        try (ServerSocket socket = new ServerSocket(PORT)) {

            System.out.println("Server: " + InetAddress.getLocalHost().getHostAddress() + " iniciado na porta " + PORT);

            while (true) {
                //Referência para o socket do cliente que se conectou
                Socket nextClient = socket.accept();
                //Para cada cliente que se conecta, cria uma thread para o atender
                new ProcessClientRequest(nextClient, manageDB, serverData, loggedIn, observers_clients, observers_backups, rmiServer).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
