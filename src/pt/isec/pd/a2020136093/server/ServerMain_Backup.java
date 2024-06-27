package pt.isec.pd.a2020136093.server;

import pt.isec.pd.a2020136093.server.model.ServerBackup;

public class ServerMain_Backup {
    public static void main(String[] args) {
        if(args.length != 1){
            System.err.println("Invalid number of arguments");
            System.exit(-1);
        }

        final String DB_BACKUP_PATH = args[0];

        ServerBackup sb = new ServerBackup(DB_BACKUP_PATH);
        sb.start();
    }
}
