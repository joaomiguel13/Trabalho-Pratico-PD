package pt.isec.pd.a2020136093.server.rmi_backup;

import pt.isec.pd.a2020136093.server.model.rmi.RMI_SERVER_INTERFACE;

import java.io.FileOutputStream;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMI_SERVER_BACKUP extends UnicastRemoteObject implements RMI_SERVER_BACKUP_INTERFACE {
    String serverRMI_Name;
    String path;

    public RMI_SERVER_BACKUP(String path, String serverRMI_Name) throws RemoteException {
        this.path = path;
        this.serverRMI_Name = serverRMI_Name;
    }

    @Override
    public void receiveDBUpdate() throws RemoteException {
        System.out.println("\n\n---------------SERVER PRINCIPAL ATUALIZOU BASE DE DADOS---------------");
        System.out.println();



        try {
            String registration = "rmi://" + "localhost" + "/" + serverRMI_Name;
            RMI_SERVER_INTERFACE rmiServerInterface = (RMI_SERVER_INTERFACE) Naming.lookup(registration);

            System.out.println("Registado com sucesso no serviço RMI do servidor principal!");

            try(FileOutputStream localFileOutputStream = new FileOutputStream(path + "/PD-2023-24-TP_BACKUP.db")) {
                byte[] b;
                long offset = 0;

                while ((b = rmiServerInterface.getDatabaseCopy_chunk(offset)) != null) {
                    localFileOutputStream.write(b);
                    offset += b.length;
                }
                System.out.println("Cópia da base de dados do servidor principal concluida.");
            }

        }catch (NotBoundException e) {
            System.out.println("No remoteTime service available!");
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("RMI Error - " + e);
        } catch (Exception e) {
            System.out.println("Error - " + e);
        }
    }
}
