package pt.isec.pd.a2020136093.server;

import pt.isec.pd.a2020136093.server.model.Server;


public class ServerMain {
    public static void main(String[] args) {
        if(args.length != 4){
            System.err.println("Invalid number of arguments");
            System.exit(-1);
        }

        final int TCP_PORT = Integer.parseInt(args[0]);
        final String DB_PATH = args[1];
        final String RMI_NAME = args[2];
        final int RMI_PORT = Integer.parseInt(args[3]);

        Server s = new Server(TCP_PORT, DB_PATH, RMI_NAME, RMI_PORT);
        s.start();

    }
}
