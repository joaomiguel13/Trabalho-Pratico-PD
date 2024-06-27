package pt.isec.pd.a2020136093.client;

import javafx.application.Application;
import pt.isec.pd.a2020136093.client.communication.ManageConnections;
import pt.isec.pd.a2020136093.client.ui.gui.MainJFX;
import pt.isec.pd.a2020136093.client.ui.text.ClientUI;

public class ClientMain {
    public static ManageConnections mc;
    public static void main(String[] args) {
        boolean useGUI = true;


        if(args.length != 2){
            System.err.println("Invalid number of arguments");
            System.exit(-1);
        }

        final String SERVER_IP = args[0];
        final int SERVER_PORT = Integer.parseInt(args[1]);

        mc = new ManageConnections(SERVER_IP, SERVER_PORT);

        if(useGUI) {
            Application.launch(MainJFX.class, args);
        }
        else {
            ClientUI ui = new ClientUI(mc);
            ui.start();
        }

    }
}
