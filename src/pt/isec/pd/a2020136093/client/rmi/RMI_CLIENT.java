package pt.isec.pd.a2020136093.client.rmi;

import javafx.application.Platform;
import pt.isec.pd.a2020136093.client.ui.gui.ADMIN.MenuAdminUI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMI_CLIENT extends UnicastRemoteObject implements RMI_CLIENT_INTERFACE {
    MenuAdminUI menuAdminUI;

    public RMI_CLIENT(MenuAdminUI menuAdminUI) throws RemoteException {
        this.menuAdminUI = menuAdminUI;
    }

    @Override
    public void receiveNotificationAsync(String info) throws RemoteException {
        System.out.println("\n\n---------------Notificação assíncrona---------------");
        System.out.println(info);
        System.out.println("----------------------------------------------------");

        Platform.runLater(() -> {
            menuAdminUI.refresh_async();
        });
    }
}
