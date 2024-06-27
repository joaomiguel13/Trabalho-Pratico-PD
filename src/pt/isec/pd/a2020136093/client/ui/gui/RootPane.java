package pt.isec.pd.a2020136093.client.ui.gui;

import javafx.scene.layout.*;
import pt.isec.pd.a2020136093.client.communication.ManageConnections;
import pt.isec.pd.a2020136093.client.rmi.RMI_CLIENT;
import pt.isec.pd.a2020136093.client.ui.gui.ADMIN.MenuAdminUI;
import pt.isec.pd.a2020136093.client.ui.gui.RESOURCES.CSSManager;
import pt.isec.pd.a2020136093.client.ui.gui.STUDENT.MenuStudentUI;
import pt.isec.pd.a2020136093.server.model.rmi.RMI_SERVER_INTERFACE;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class RootPane extends BorderPane {
    ManageConnections mc;
    static PropertyChangeSupport pcs;
    public static boolean showMainMenu = true;
    public static boolean showLogin = false;
    public static boolean showRegister = false;
    public static boolean showStudentMenu = false;
    public static boolean showAdminMenu = false;


    public RootPane(ManageConnections manageConnections){
        this.mc = manageConnections;

        pcs = new PropertyChangeSupport(this);

        createViews();
        registerHandlers();
        update();
    }

    private void createViews(){
        CSSManager.applyCSS(this, "style1.css");

        MenuAdminUI menuAdminUI = new MenuAdminUI(mc);

        try {
            RMI_CLIENT rmiClient = new RMI_CLIENT(menuAdminUI);

            Registry r = LocateRegistry.getRegistry("localhost");

            RMI_SERVER_INTERFACE remoteRef = (RMI_SERVER_INTERFACE) r.lookup(r.list()[0]);
            remoteRef.addObserver_clients(rmiClient);

        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

        StackPane stackPane = new StackPane(
                new MainMenuUI(mc),
                new LoginUI(mc),
                new RegisterUI(mc),
                new MenuStudentUI(mc),
                menuAdminUI
        );

        this.setCenter(stackPane);
    }

    private void registerHandlers(){
    }

    private void update() {
    }


    public static void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }


    public static void setShowMainMenu(boolean b){
        showMainMenu = b;
        pcs.firePropertyChange("SHOWMENU", null, null);
    }
    public static void setShowLogin(boolean b){
        showLogin = b;
        pcs.firePropertyChange("SHOWLOGIN", null, null);
    }
    public static void setShowRegister(boolean b){
        showRegister = b;
        pcs.firePropertyChange("SHOWREGISTER", null, null);
    }
    public static void setShowStudentMenu(boolean b){
        showStudentMenu = b;
        pcs.firePropertyChange("SHOWSTUDENTMENU", null, null);
    }
    public static void setShowAdminMenu(boolean b){
        showAdminMenu = b;
        pcs.firePropertyChange("SHOWADMINMENU", null, null);
    }


}