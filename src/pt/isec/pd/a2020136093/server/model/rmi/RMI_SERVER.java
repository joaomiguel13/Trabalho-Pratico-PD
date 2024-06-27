package pt.isec.pd.a2020136093.server.model.rmi;

import pt.isec.pd.a2020136093.client.rmi.RMI_CLIENT_INTERFACE;
import pt.isec.pd.a2020136093.server.rmi_backup.RMI_SERVER_BACKUP_INTERFACE;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMI_SERVER extends UnicastRemoteObject implements RMI_SERVER_INTERFACE {
    List<RMI_CLIENT_INTERFACE> observers_clients;
    List<RMI_SERVER_BACKUP_INTERFACE> observers_backups;

    //Recebe lista de observers vazias
    public RMI_SERVER(List<RMI_CLIENT_INTERFACE> observers_clients, List<RMI_SERVER_BACKUP_INTERFACE> observers_backups) throws RemoteException {
        this.observers_clients = observers_clients;
        this.observers_backups = observers_backups;
    }


    // BACKUP SERVER
    @Override
    public byte[] getDatabaseCopy_chunk(long offset) throws RemoteException {
        int MAX_CHUNK_SIZE = 10000; //bytes

        //System.out.println("[BACKUP SERVER] Sending database copy chunk...");

        byte[] fileChunk = new byte[MAX_CHUNK_SIZE];
        int nbytes;
        String dbFileName = "PD-2023-24-TP.db";

        try (FileInputStream requestedFileInputStream = new FileInputStream(dbFileName)) {

            /*
             * Obtem um bloco de bytes do ficheiro colocando-os no array fileChunk e omitindo os primeiros offset bytes.
             */
            requestedFileInputStream.skip(offset);
            nbytes = requestedFileInputStream.read(fileChunk);

            if (nbytes == -1) { //EOF
                return null;
            }

            /*
             * Se fileChunk nao esta' totalmente preenchido (MAX_CHUNCK_SIZE), recorre-se
             * a um array auxiliar com tamanho correspondente ao numero de bytes efectivamente lidos.
             */
            if (nbytes < fileChunk.length) {
                /*
                 * Aloca aux
                 */
                byte[] aux = new byte[nbytes];


                /*
                 * Copia os bytes obtidos do ficheiro de fileChunck para aux
                 */
                System.arraycopy(fileChunk, 0, aux, 0, nbytes);


                return aux;
            }

            return fileChunk;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public /*synchronized*/ void addObserver_backups(RMI_SERVER_BACKUP_INTERFACE observer) throws RemoteException {
        synchronized (observers_backups){
            if(!observers_backups.contains(observer)) {
                observers_backups.add(observer);
                //System.out.println("Observer adicionado");
                observer.receiveDBUpdate();
            }
        }
    }
    /*@Override
    public /*synchronized*/ /*void removeObserver_backups(RMI_SERVER_BACKUP_INTERFACE observer) throws RemoteException {
        synchronized (observers_backups){
            if(observers_backups.remove(observer)) {
                //System.out.println("Observer removido");
            }
        }
    }*/




    // CLIENTS
    @Override
    public /*synchronized*/ void addObserver_clients(RMI_CLIENT_INTERFACE observer) throws RemoteException {
        synchronized (observers_clients){
            if(!observers_clients.contains(observer)) {
                observers_clients.add(observer);
                //System.out.println("Observer adicionado");
            }
        }
    }

    /*@Override
    public /*synchronized*/ /*void removeObserver_clients(RMI_CLIENT_INTERFACE observer) throws RemoteException {
        synchronized (observers_clients){
            if(observers_clients.remove(observer)) {
                //System.out.println("Observer removido");
            }
        }
    }*/

}
