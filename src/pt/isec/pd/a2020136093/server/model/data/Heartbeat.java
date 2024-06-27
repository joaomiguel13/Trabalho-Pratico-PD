package pt.isec.pd.a2020136093.server.model.data;

import java.io.Serializable;

public class Heartbeat implements Serializable {
    private String serverIP;
    private int serverPort;
    private int serverDBVersion;
    private String RMI_NAME;
    private int RMI_PORT;

    public Heartbeat(String serverIP, int serverPort, int serverDBVersion, String RMI_NAME, int RMI_PORT) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.serverDBVersion = serverDBVersion;
        this.RMI_NAME = RMI_NAME;
        this.RMI_PORT = RMI_PORT;
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getServerDBVersion() {
        return serverDBVersion;
    }

    public String getRMI_NAME() {
        return RMI_NAME;
    }

    public int getRMI_PORT() {
        return RMI_PORT;
    }


    // FUNCTIONS FOR BACKUP SERVERS TO UPDATE THEIR DATA
    public void setRMI_NAME(String RMI_NAME) {
        this.RMI_NAME = RMI_NAME;
    }
    public void setRMI_PORT(int RMI_PORT) {
        this.RMI_PORT = RMI_PORT;
    }


    public void updateServerDBVersion(int version){
        serverDBVersion = version;
    }
}
