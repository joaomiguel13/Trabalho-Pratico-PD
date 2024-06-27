package pt.isec.pd.a2020136093.client.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI_CLIENT_INTERFACE extends Remote{
    void receiveNotificationAsync(String info) throws RemoteException;
}
