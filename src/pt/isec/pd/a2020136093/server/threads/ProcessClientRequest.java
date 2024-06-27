package pt.isec.pd.a2020136093.server.threads;

import pt.isec.pd.a2020136093.client.rmi.RMI_CLIENT_INTERFACE;
import pt.isec.pd.a2020136093.data.EventsData;
import pt.isec.pd.a2020136093.server.model.data.Heartbeat;
import pt.isec.pd.a2020136093.server.model.jdbc.ManageDB;
import pt.isec.pd.a2020136093.server.model.rmi.RMI_SERVER;
import pt.isec.pd.a2020136093.server.rmi_backup.RMI_SERVER_BACKUP_INTERFACE;
import pt.isec.pd.a2020136093.utils.REQUESTS;
import pt.isec.pd.a2020136093.utils.REQUEST_CLIENT_TO_SERVER;
import pt.isec.pd.a2020136093.utils.REQUEST_ADMIN_TO_SERVER;
import pt.isec.pd.a2020136093.utils.RESPONSE_SERVER_TO_CLIENT_OR_ADMIN;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static pt.isec.pd.a2020136093.server.model.data.CONSTANTS.*;

public class ProcessClientRequest extends Thread {
    private Socket nextClient;
    ManageDB manageDB;
    Heartbeat serverData;
    ArrayList<String> loggedIn;
    List<RMI_CLIENT_INTERFACE> observers_clients;
    List<RMI_SERVER_BACKUP_INTERFACE> observers_backups;
    RMI_SERVER rmiServer;

    public ProcessClientRequest(Socket nextClient, ManageDB manageDB, Heartbeat serverData, ArrayList<String> loggedIn, List<RMI_CLIENT_INTERFACE> observers_clients, List<RMI_SERVER_BACKUP_INTERFACE> observers_backups, RMI_SERVER rmiServer){
        this.nextClient = nextClient;
        this.manageDB = manageDB;
        this.serverData = serverData;
        this.loggedIn = loggedIn;
        this.observers_clients = observers_clients;
        this.observers_backups = observers_backups;
        this.rmiServer = rmiServer;

        try {
            nextClient.setSoTimeout(TIMEOUT_CLIENT_MILLISECONDS);   // TIMEOUT PARA DESCONECTAR CLIENTE INATIVO
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("Cliente -> " + nextClient.getInetAddress().getHostAddress() + ":" + nextClient.getPort() + " conectado");

        try (
                ObjectOutputStream oout = new ObjectOutputStream(nextClient.getOutputStream());
                ObjectInputStream oin = new ObjectInputStream(nextClient.getInputStream())
        ) {
            while (true) {

                REQUEST_CLIENT_TO_SERVER requestClientServer = null;
                REQUEST_ADMIN_TO_SERVER requestClientServerAdmin = null;

                Object receivedObject = oin.readObject();

                //Lê o objecto enviado pelo cliente e determina se é uma solicitação de admin ou de estudante
                if (receivedObject instanceof REQUEST_CLIENT_TO_SERVER) {
                    requestClientServer = (REQUEST_CLIENT_TO_SERVER) receivedObject;
                }
                else if (receivedObject instanceof REQUEST_ADMIN_TO_SERVER) {
                    requestClientServerAdmin = (REQUEST_ADMIN_TO_SERVER) receivedObject;
                }

                RESPONSE_SERVER_TO_CLIENT_OR_ADMIN response = new RESPONSE_SERVER_TO_CLIENT_OR_ADMIN();

                // ADMIN
                if (requestClientServerAdmin != null ) {
                    switch (requestClientServerAdmin.msgCode) {
                        case REQUESTS.ADMIN_REQUEST_CREATE_EVENT -> {
                            if (manageDB.addNewEvent(requestClientServerAdmin.name, requestClientServerAdmin.local, requestClientServerAdmin.date, requestClientServerAdmin.timeStart, requestClientServerAdmin.timeEnd)) {
                                manageDB.updateDB_version();
                                serverData.updateServerDBVersion(manageDB.getDB_version());
                                sendHearbeat_updatedDB();

                                response.response = "Evento criado com sucesso!";
                                response.resultado = true;
                                oout.writeObject(response);
                            } else {
                                response.response = "Evento não criado!";
                                response.resultado = false;
                                oout.writeObject(response);
                            }
                        }
                        case REQUESTS.ADMIN_REQUEST_EDIT_EVENT -> {
                            if (manageDB.editEvent(requestClientServerAdmin.id, requestClientServerAdmin.name, requestClientServerAdmin.local, requestClientServerAdmin.date, requestClientServerAdmin.timeStart, requestClientServerAdmin.timeEnd)) {
                                manageDB.updateDB_version();
                                serverData.updateServerDBVersion(manageDB.getDB_version());
                                sendHearbeat_updatedDB();

                                response.response = "Evento editado com sucesso!";
                                response.resultado = true;
                                oout.writeObject(response);
                            } else {
                                response.response = "Evento não editado!";
                                response.resultado = false;
                                oout.writeObject(response);
                            }
                        }
                        case REQUESTS.ADMIN_REQUEST_DELETE_EVENT -> {
                            if (manageDB.deleteEvent(requestClientServerAdmin.id)) {
                                manageDB.updateDB_version();
                                serverData.updateServerDBVersion(manageDB.getDB_version());
                                sendHearbeat_updatedDB();

                                response.response = "Evento eliminado com sucesso!";
                                response.resultado = true;
                                oout.writeObject(response);
                            } else {
                                response.response = "Evento não eliminado!";
                                response.resultado = false;
                                oout.writeObject(response);
                            }
                        }
                        case REQUESTS.ADMIN_REQUEST_CHECK_EVENTS -> {
                            ArrayList<ArrayList<String>> list = manageDB.checkEvents();
                            for (ArrayList<String> l : list) {
                                response.eventsList.addEvent(new EventsData(l.get(0), l.get(1), l.get(2), l.get(3), l.get(4), l.get(5), l.get(6), l.get(7)));
                            }

                            response.response = "LISTA DE EVENTOS";

                            oout.writeObject(response);
                        }
                        case REQUESTS.ADMIN_REQUEST_GENERATE_CODE -> {
                            if(manageDB.generateCode(requestClientServerAdmin.id, requestClientServerAdmin.eventCode, requestClientServerAdmin.eventTime)){
                                manageDB.updateDB_version();
                                serverData.updateServerDBVersion(manageDB.getDB_version());
                                sendHearbeat_updatedDB();

                                response.response = "Código gerado com sucesso!\n-> [" + requestClientServerAdmin.eventCode + "]";
                                response.resultado = true;
                                oout.writeObject(response);
                            }
                            else {
                                response.response = "O evento não está ativo!";
                                response.resultado = false;
                                oout.writeObject(response);
                            }
                        }
                        case REQUESTS.ADMIN_REQUEST_CHECK_PRESENCES_EVENT -> {
                            ArrayList<ArrayList<String>> presencesList = manageDB.checkPresencesEventID(requestClientServerAdmin.id);    // ID DO EVENTO

                            response.response = "LISTA DE PRESENÇAS NO EVENTO [ID]: "+ requestClientServerAdmin.id;
                            response.presencesADMIN = presencesList;


                            oout.writeObject(response);
                        }
                        case REQUESTS.ADMIN_REQUEST_DELETE_PRESENCE -> {
                            if(manageDB.deletePresence(requestClientServerAdmin.id, requestClientServerAdmin.emailToManagePresence)){
                                manageDB.updateDB_version();
                                serverData.updateServerDBVersion(manageDB.getDB_version());
                                sendHearbeat_updatedDB();

                                response.response = "Presença eliminada com sucesso!";
                                response.resultado = true;
                                oout.writeObject(response);
                            }
                            else {
                                response.response = "Presença não eliminada!";
                                response.resultado = false;
                                oout.writeObject(response);
                            }
                        }
                        case REQUESTS.ADMIN_REQUEST_ADD_PRESENCE -> {
                            if(manageDB.addPresence(requestClientServerAdmin.id, requestClientServerAdmin.emailToManagePresence)){
                                manageDB.updateDB_version();
                                serverData.updateServerDBVersion(manageDB.getDB_version());
                                sendHearbeat_updatedDB();

                                response.response = "Presença adicionada com sucesso!";
                                response.resultado = true;
                                oout.writeObject(response);
                            }
                            else {
                                response.response = "Presença já registada ou parâmetros inválidos!";
                                response.resultado = false;
                                oout.writeObject(response);
                            }
                        }
                        case REQUESTS.ADMIN_REQUEST_GENERATE_CSV_EVENT -> {
                            ArrayList<ArrayList<String>> presencesList = manageDB.checkPresencesEventID(requestClientServerAdmin.id);    // ID DO EVENTO
                            ArrayList<String> eventInfo = manageDB.checkEvent(requestClientServerAdmin.id);

                            String csv = generateCSV(presencesList, eventInfo);
                            if(csv != null) {
                                response.response = csv;
                                response.resultado = true;
                                oout.writeObject(response);
                            }

                            else{
                                response.response = "Houve um erro ao gerar o ficheiro CSV!";
                                response.resultado = false;
                                oout.writeObject(response);
                            }

                        }
                        case REQUESTS.ADMIN_REQUEST_GENERATE_CSV_STUDENT -> {
                            ArrayList<ArrayList<String>> presencesList = manageDB.checkPresences(requestClientServerAdmin.emailToManagePresence);    // ID DO EVENTO
                            ArrayList<String> studentInfo = manageDB.checkStudent(requestClientServerAdmin.emailToManagePresence);

                            String csv = generateCSV2(presencesList, studentInfo);
                            if(csv != null) {
                                response.response = csv;
                                response.resultado = true;
                                oout.writeObject(response);
                            }
                            else{
                                response.response = "Houve um erro ao gerar o ficheiro CSV!";
                                response.resultado = false;
                                oout.writeObject(response);
                            }
                        }
                    }
                }





                // ALUNO
                else if (requestClientServer != null) {

                    switch (requestClientServer.msgCode) {
                        case REQUESTS.CLIENT_REQUEST_LOGOUT -> {
                            response.response = "Utilizador deslogado com sucesso!";
                            response.resultado = true;

                            loggedIn.remove(requestClientServer.email);

                            oout.writeObject(response);

                            nextClient.setSoTimeout(TIMEOUT_CLIENT_MILLISECONDS);
                        }

                        case REQUESTS.CLIENT_REQUEST_REGISTER -> {
                            if (manageDB.addNewUser(requestClientServer.name, requestClientServer.email, requestClientServer.password, requestClientServer.nIdentificacao)) {

                                // DISABLE SOCKET TIMEOUT
                                nextClient.setSoTimeout(0);

                                manageDB.updateDB_version();
                                serverData.updateServerDBVersion(manageDB.getDB_version());
                                sendHearbeat_updatedDB();

                                response.response = "Utilizador registado com sucesso!";
                                response.resultado = true;
                                oout.writeObject(response);

                                nextClient.setSoTimeout(TIMEOUT_CLIENT_MILLISECONDS);
                            } else {
                                response.response = "Utilizador já registado!";
                                response.resultado = false;
                                oout.writeObject(response);
                            }
                        }

                        case REQUESTS.CLIENT_REQUEST_LOGIN -> {
                            if(loggedIn.contains(requestClientServer.email)){
                                response.response = "Utilizador já logado!";
                                response.resultado = false;
                                oout.writeObject(response);
                            }
                            else {
                                if (manageDB.login(requestClientServer.email, requestClientServer.password)) {

                                    // DISABLE SOCKET TIMEOUT
                                    nextClient.setSoTimeout(0);

                                    response.response = "Bem-vindo " + requestClientServer.email + "!";
                                    response.resultado = true;

                                    ArrayList<String> list = manageDB.getClientData(requestClientServer.email);

                                    response.clientData.addAll(list);

                                    loggedIn.add(requestClientServer.email);

                                    oout.writeObject(response);

                                } else {
                                    response.response = "Credenciais invalidas!";
                                    response.resultado = false;
                                    oout.writeObject(response);
                                }
                            }
                        }

                        case REQUESTS.CLIENT_REQUEST_EDIT_DATA -> {
                            if (manageDB.editData(requestClientServer.novoEmail, requestClientServer.email, requestClientServer.password, requestClientServer.name, requestClientServer.nIdentificacao) != null) {
                                manageDB.updateDB_version();
                                serverData.updateServerDBVersion(manageDB.getDB_version());
                                sendHearbeat_updatedDB();

                                response.response = "Dados alterados com sucesso!";
                                response.resultado = true;

                                ArrayList<String> list = manageDB.getClientData(requestClientServer.novoEmail);

                                response.clientData.addAll(list);

                                oout.writeObject(response);
                            } else {
                                response.response = "Dados não alterados!";
                                response.resultado = false;
                                oout.writeObject(response);
                            }
                        }
                        case REQUESTS.CLIENT_REQUEST_SUBMIT_CODE -> {
                            if (manageDB.submitCode(requestClientServer.email, requestClientServer.eventCode)) {
                                manageDB.updateDB_version();
                                serverData.updateServerDBVersion(manageDB.getDB_version());
                                sendHearbeat_updatedDB();

                                response.response = "Código submetido com sucesso!";
                                response.resultado = true;
                                oout.writeObject(response);
                            }
                            else {
                                response.response = "Presença já registada ou código inválido!";
                                response.resultado = false;
                                oout.writeObject(response);
                            }
                        }
                        case REQUESTS.CLIENT_REQUEST_CHECK_PRESENCES -> {
                            ArrayList<ArrayList<String>> list = manageDB.checkPresences(requestClientServer.email);

                            response.clientEventsData = list;

                            oout.writeObject(response);
                        }
                        case REQUESTS.CLIENT_REQUEST_GENERATE_CSV_STUDENT -> {
                            ArrayList<ArrayList<String>> presencesList = manageDB.checkPresences(requestClientServer.email);    // ID DO EVENTO
                            ArrayList<String> studentInfo = manageDB.checkStudent(requestClientServer.email);

                            String csv = generateCSV2(presencesList, studentInfo);
                            if(csv != null) {
                                response.response = csv;
                                response.resultado = true;
                                oout.writeObject(response);
                            }
                            else{
                                response.response = "Houve um erro ao gerar o ficheiro CSV!";
                                response.resultado = false;
                                oout.writeObject(response);
                            }
                        }
                    }
                }
            }
        }
        catch (SocketTimeoutException e) {  // FECHA O SOCKET AUTOMATICAMENTE
            System.out.println("Cliente -> " + nextClient.getInetAddress().getHostAddress() + ":" + nextClient.getPort() + " inativo por 10 segundos... A conexão será fechada!");
        }
        catch (ClassNotFoundException | IOException | SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }


    // FUNÇAO PARA ENVIAR HEARTBEAT SEMPRE QUE HA UM UPDATE NA DB
    public void sendHearbeat_updatedDB(){
        DatagramSocket datagramSocket;

        try {
            datagramSocket = new DatagramSocket();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(serverData);

            byte[] msgBytes = baos.toByteArray();

            InetAddress ipServer = InetAddress.getByName(MULTICAST_IP);

            DatagramPacket dpSend = new DatagramPacket(
                    msgBytes,
                    msgBytes.length,
                    ipServer,
                    MULTICAST_PORT
            );

            datagramSocket.send(dpSend);

            if(observers_backups.size() != 0) {
                List<RMI_SERVER_BACKUP_INTERFACE> observersToRemove = new ArrayList<>();

                for (RMI_SERVER_BACKUP_INTERFACE svBackup : observers_backups) {
                    try {
                        svBackup.receiveDBUpdate();
                    }
                    catch (RemoteException e) {
                        System.out.println("Observador server_backup inacessivel removido");
                        observersToRemove.add(svBackup);
                    }
                }

                observers_backups.removeAll(observersToRemove);
            }


            if(observers_clients.size() != 0) {
                List<RMI_CLIENT_INTERFACE> observersToRemove = new ArrayList<>();

                for (RMI_CLIENT_INTERFACE client : observers_clients) {
                    try {
                        client.receiveNotificationAsync("Nova atualização da base de dados");
                    }
                    catch (RemoteException e) {
                        System.out.println("Observador cliente inacessivel removido");
                        observersToRemove.add(client);
                    }
                }

                observers_clients.removeAll(observersToRemove);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public String generateCSV(ArrayList<ArrayList<String>> presencesList, ArrayList<String> eventInfo){
        try{
            StringBuilder sb = new StringBuilder();

            sb.append("\"Designação\";\"").append(eventInfo.get(0)).append("\"\n");
            sb.append("\"Local\";\"").append(eventInfo.get(1)).append("\"\n");
            sb.append("\"Data\";\"").append(eventInfo.get(2)).append("\"\n");
            sb.append("\"Hora início\";\"").append(eventInfo.get(3)).append("\"\n");
            sb.append("\"Hora fim\";\"").append(eventInfo.get(4)).append("\"\n");
            sb.append("\"Nome\";\"Número identificação\";\"Email\"\n");

            for (ArrayList<String> l : presencesList) {
                sb.append("\"").append(l.get(0)).append("\";\"").append(l.get(1)).append("\";\"").append(l.get(2)).append("\"\n");
            }
            
            return sb.toString();
        }
        catch (Exception e) {
            return null;
        }
    }

    public String generateCSV2(ArrayList<ArrayList<String>> presencesList, ArrayList<String> studentInfo){

        StringBuilder sb = new StringBuilder();

        try {
            sb.append("\"Nome\";\"Número identificação\";\"Email\"\n");

            sb.append("\"").append(studentInfo.get(0)).append("\";\"").append(studentInfo.get(2)).append("\";\"").append(studentInfo.get(1)).append("\"\n");


            sb.append("\"Designação\";\"Local\";\"Data\";\"Hora início\"\n");
            for (ArrayList<String> l : presencesList) {
                sb.append("\"").append(l.get(1)).append("\";\"").append(l.get(2)).append("\";\"").append(l.get(3)).append("\";\"").append(l.get(4)).append("\"\n");
            }


            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
