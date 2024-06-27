package pt.isec.pd.a2020136093.server.rmi_backup;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI_SERVER_BACKUP_INTERFACE extends Remote{
    void receiveDBUpdate() throws RemoteException;
}
