package pt.isec.pd.a2020136093.server.model.rmi;

import pt.isec.pd.a2020136093.client.rmi.RMI_CLIENT_INTERFACE;
import pt.isec.pd.a2020136093.server.rmi_backup.RMI_SERVER_BACKUP_INTERFACE;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI_SERVER_INTERFACE extends Remote {

    byte[] getDatabaseCopy_chunk(long offset) throws RemoteException;

    public void addObserver_backups(RMI_SERVER_BACKUP_INTERFACE observer) throws RemoteException;
    //public void removeObserver_backups(RMI_SERVER_BACKUP_INTERFACE observer) throws RemoteException;

    public void addObserver_clients(RMI_CLIENT_INTERFACE observer) throws RemoteException;
    //public void removeObserver_clients(RMI_CLIENT_INTERFACE observer) throws RemoteException;

}
