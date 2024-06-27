package pt.isec.pd.a2020136093.server.model;

import pt.isec.pd.a2020136093.server.model.data.Heartbeat;
import pt.isec.pd.a2020136093.server.model.rmi.RMI_SERVER_INTERFACE;
import pt.isec.pd.a2020136093.server.rmi_backup.RMI_SERVER_BACKUP;
import pt.isec.pd.a2020136093.server.threads.Multicast_ReadHearbeat;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;

import static pt.isec.pd.a2020136093.server.model.data.CONSTANTS.*;
import static pt.isec.pd.a2020136093.server.model.data.CONSTANTS.NETWORK_INTERFACE_NAME;

public class ServerBackup {
    RMI_SERVER_BACKUP rmiBackup;
    private final String DB_BACKUP_PATH;
    static String DB_BACKUP_PATH_STATIC;
    Heartbeat serverData;

    public ServerBackup(String dbPath) {
        this.DB_BACKUP_PATH = dbPath;
        DB_BACKUP_PATH_STATIC = dbPath;
    }

    public void start() {
        File db_directory = new File(DB_BACKUP_PATH.trim());
        serverData = new Heartbeat(null, 0, 0, null, 0);


        if(!db_directory.isDirectory() || !db_directory.exists()){
            System.out.println("Diretório da base de dados não existe ou não é válido!");
            System.exit(-1);
        }

        File[] files = db_directory.listFiles();
        if(files != null){
            if(files.length != 0){
                System.out.println("Diretório não está vazio!");
                System.exit(-1);
            }
        }


        // ================================= MULTICAST =================================
        MulticastSocket multicastSocket;
        try {
            InetAddress group = InetAddress.getByName(MULTICAST_IP);
            int port = MULTICAST_PORT;
            NetworkInterface nif;
            try {
                nif = NetworkInterface.getByInetAddress(InetAddress.getByName(NETWORK_INTERFACE_NAME));
            } catch (SocketException | UnknownHostException e) {
                nif = NetworkInterface.getByName(NETWORK_INTERFACE_NAME);
            }

            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(new InetSocketAddress(group, port), nif);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new Multicast_ReadHearbeat(multicastSocket, serverData).start();



        // ================================= RMI =================================
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }while(serverData.getRMI_NAME() == null);


        try {
            rmiBackup = new RMI_SERVER_BACKUP(DB_BACKUP_PATH, serverData.getRMI_NAME());

            Registry r = LocateRegistry.getRegistry("localhost");

            RMI_SERVER_INTERFACE remoteRef = (RMI_SERVER_INTERFACE) r.lookup(r.list()[0]);
            remoteRef.addObserver_backups(rmiBackup);

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static int get_backup_dbVersion(){
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + DB_BACKUP_PATH_STATIC + "/PD-2023-24-TP_BACKUP.db");
             Statement statement = connection.createStatement()
        ) {

            String checkDB = "SELECT version FROM db_version";
            ResultSet resultSet = statement.executeQuery(checkDB);

            if(resultSet == null)
                return -1;

            System.out.println("Versão da base de dados server backup: " + resultSet.getInt("version"));

            return resultSet.getInt("version");
        } catch (SQLException e) {
            return -99; // bd nao criada ainda
        }
    }
}