package pt.isec.pd.a2020136093.server.threads;

import pt.isec.pd.a2020136093.server.model.ServerBackup;
import pt.isec.pd.a2020136093.server.model.data.Heartbeat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

import static pt.isec.pd.a2020136093.server.model.data.CONSTANTS.TIMETOUT_SERVER_BACKUP;

public class Multicast_ReadHearbeat extends Thread {
    public static int MAX_SIZE = 1000;
    protected MulticastSocket s;
    protected Heartbeat serverData_backup;

    boolean timeout = false;
    int timeout_current_seconds = 0;

    public Multicast_ReadHearbeat(MulticastSocket s, Heartbeat serverData_backup){
        this.s = s;
        this.serverData_backup = serverData_backup;

        Thread timerThread = new Thread(() -> {
            while (true) {
                try {
                    ++timeout_current_seconds;

                    if (timeout_current_seconds == TIMETOUT_SERVER_BACKUP) {
                        System.out.println("\nNão foi recebido nenhum 'HEARBEAT' do servidor principal em 30 segundos!");
                        System.exit(-1);
                    }

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timerThread.start();
    }

    @Override
    public void run() {
        Object obj;
        DatagramPacket pkt;
        Heartbeat serverData;

        if (s == null)
            return;

        try {
            while (!timeout) {

                pkt = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);
                //MulticastSocket recebe o pacote
                s.receive(pkt);

                //Abre o pacote
                try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(pkt.getData(), 0, pkt.getLength()))) {
                    //Lê o objeto
                    obj = in.readObject();

                    if (obj instanceof Heartbeat) {

                        serverData = (Heartbeat) obj;

                        System.out.print("\nRECEIVED HEARTBEAT FROM " + serverData.getServerIP() + ":" + serverData.getServerPort());
                        System.out.println("\n[INFO]\n-> DatabaseVersion: " + serverData.getServerDBVersion() + " RMI_NAME: " + serverData.getRMI_NAME() + " RMI_PORT: " + serverData.getRMI_PORT());

                        //Vai preencher o 'serverdata' do backup que estava vazio com info importante
                        serverData_backup.setRMI_NAME(serverData.getRMI_NAME());
                        serverData_backup.setRMI_PORT(serverData.getRMI_PORT());

                        timeout_current_seconds = 0;

                        if(serverData.getServerDBVersion() != ServerBackup.get_backup_dbVersion()){
                            if(ServerBackup.get_backup_dbVersion() == -99) //Se for a primeira vez que o servidor de backup recebe um heartbeat, ignora pois n tem a db criada
                                continue;
                            System.out.println("VERSÃO DA BASE DE DADOS DO SERVIDOR PRINCIPAL DIFERENTE DA VERSÃO DA BASE DE DADOS DO SERVIDOR DE BACKUP!");
                            System.exit(-1);
                        }
                        else{
                            System.out.println("VERSÕES IGUAIS!");
                        }
                    }


                } catch (ClassNotFoundException e) {
                    System.out.println();
                    System.out.println("Mensagem recebida de tipo inesperado! " + e);
                } catch (IOException e) {
                    System.out.println();
                    System.out.println("Impossibilidade de aceder ao conteudo da mensagem recebida! " + e);
                } catch (Exception e) {
                    System.out.println();
                    System.out.println("Excepcao: " + e);
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
