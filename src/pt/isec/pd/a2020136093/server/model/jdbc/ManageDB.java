package pt.isec.pd.a2020136093.server.model.jdbc;

import pt.isec.pd.a2020136093.client.rmi.RMI_CLIENT_INTERFACE;
import pt.isec.pd.a2020136093.server.model.ServerBackup;
import pt.isec.pd.a2020136093.server.model.data.CONSTANTS;
import pt.isec.pd.a2020136093.server.rmi_backup.RMI_SERVER_BACKUP_INTERFACE;
import pt.isec.pd.a2020136093.server.threads.threadCodigos;
import pt.isec.pd.a2020136093.utils.Codigo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class ManageDB {
    private final String dbPath;
    private List<RMI_SERVER_BACKUP_INTERFACE> observers_backups;
    private List<RMI_CLIENT_INTERFACE> observers_clients;

    public ManageDB(String dbPath, List<RMI_SERVER_BACKUP_INTERFACE> observers_backups, List<RMI_CLIENT_INTERFACE> observers_clients) {
        this.dbPath = dbPath;
        this.observers_backups = observers_backups;
        this.observers_clients = observers_clients;
    }

    public void removeCodigo(Codigo codigo) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String generateCode = "UPDATE events SET code = NULL WHERE id = '" + codigo.getCodigo() + "'";
            statement.executeUpdate(generateCode);

            send_to_RMI();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void send_to_RMI() {
        if (observers_backups.size() != 0) {
            List<RMI_SERVER_BACKUP_INTERFACE> observersToRemove = new ArrayList<>();

            for (RMI_SERVER_BACKUP_INTERFACE svBackup : observers_backups) {
                try {
                    if (ServerBackup.get_backup_dbVersion() != getDB_version()) {
                        observersToRemove.add(svBackup);
                    } else {
                        svBackup.receiveDBUpdate();
                    }
                } catch (RemoteException e) {
                    System.out.println("Observador server_backup inacessivel removido");
                    observersToRemove.add(svBackup);
                }
            }

            observers_backups.removeAll(observersToRemove);
        }


        if (observers_clients.size() != 0) {
            List<RMI_CLIENT_INTERFACE> observersToRemove = new ArrayList<>();

            for (RMI_CLIENT_INTERFACE client : observers_clients) {
                try {
                    client.receiveNotificationAsync("Nova atualização da base de dados");
                } catch (RemoteException e) {
                    System.out.println("Observador cliente inacessivel removido");
                    observersToRemove.add(client);
                }
            }

            observers_clients.removeAll(observersToRemove);
        }
    }


    public boolean connectDB() {
        //Tenta estabelecer ligação à base de dados
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            return true;
        } catch (SQLException e) {
            System.out.println("Database Connection Error: " + e.getMessage());
            return false;
        }
    }

    public int getDB_version() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()
        ) {

            String checkDB = "SELECT version FROM db_version";
            ResultSet resultSet = statement.executeQuery(checkDB);

            if (resultSet == null)
                return -1;

            return resultSet.getInt("version");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateDB_version() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()
        ) {

            String checkDB = "SELECT version FROM db_version";
            ResultSet resultSet = statement.executeQuery(checkDB);

            int currentDBVersion = resultSet.getInt("version");

            String updateDB = "UPDATE db_version SET version = " + (currentDBVersion + 1) + " WHERE version = " + currentDBVersion;
            statement.executeUpdate(updateDB);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createDB() {
        //Conexão feita com a classe DriverManager. Objecto statement para executar queries
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            List<String> sqlScript = Files.readAllLines(Paths.get(CONSTANTS.DATABASE_CREATE_SCRIPT_PATH));

            for (String script : sqlScript) {
                //Ignorar comentários no script sql
                if (script.startsWith("--"))
                    continue;

                //Tentar executar cada linha do script sql
                try {
                    statement.executeUpdate(script);
                } catch (SQLException e) {
                    System.err.println("SQL Error: " + e.getMessage());
                }
            }

            //Verifica se a tabela existe
            String checkDBVersion = "SELECT * FROM db_version";
            ResultSet resultSet = statement.executeQuery(checkDBVersion);
            //Se não houver resultados, adicionamos o valor de "version" = 0
            if (!resultSet.next()) {
                String insertDBVersion = "INSERT INTO db_version (version) VALUES (0)";
                statement.executeUpdate(insertDBVersion);
            }

        } catch (SQLException | IOException e) {
            System.err.println("Database Connection Error: " + e.getMessage());
        }

    }

    public boolean addNewUser(String name, String email, String password, String nIdentificacao) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String checkEmail = "SELECT * FROM accounts WHERE email = '" + email + "'";
            ResultSet resultSet = statement.executeQuery(checkEmail);

            if (resultSet.next())
                return false;

            boolean admin = false;

            String newUser = "INSERT INTO accounts (name, email, password, admin, nIdentificacao) VALUES ('" + name + "', '" + email + "', '" + password + "', '" + admin + "', '" + nIdentificacao + "')";
            statement.executeUpdate(newUser);

            return true;
        }
    }

    public boolean login(String email, String password) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String checkPassword = "SELECT * FROM accounts WHERE email = '" + email + "' AND password = '" + password + "'";
            ResultSet resultSet = statement.executeQuery(checkPassword);

            if (!resultSet.next())
                return false;

            return true;
        }
    }


    // ADMIN
    public boolean addNewEvent(String name, String local, String date, String timeStart, String timeEnd) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String newEvent = "INSERT INTO events (name, local, date, timeStart, timeEnd) VALUES ('" + name + "', '" + local + "', '" + date + "', '" + timeStart + "', '" + timeEnd + "')";
            statement.executeUpdate(newEvent);

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean editEvent(int idEvento, String name, String local, String date, String timeStart, String timeEnd) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String getEvent = "SELECT * FROM events WHERE id = '" + idEvento + "'";
            ResultSet resultSet = statement.executeQuery(getEvent);
            if (!resultSet.next())
                return false;

            String nPresences = resultSet.getString("nPresences");
            if (Integer.parseInt(nPresences) > 0 && (!resultSet.getString("timeStart").equals(timeStart) || !resultSet.getString("timeEnd").equals(timeEnd)))
                return false;

            String editEvent = "UPDATE events SET name = '" + name + "', local = '" + local + "', date = '" + date + "', timeStart = '" + timeStart + "', timeEnd = '" + timeEnd + "' WHERE id = '" + idEvento + "'";
            statement.executeUpdate(editEvent);

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteEvent(int idEvento) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String getEvent = "SELECT * FROM events WHERE id = '" + idEvento + "'";
            ResultSet resultSet = statement.executeQuery(getEvent);
            if (!resultSet.next())
                return false;

            String nPresences = resultSet.getString("nPresences");
            if (Integer.parseInt(nPresences) > 0)
                return false;

            String deleteEvent = "DELETE FROM events WHERE id = '" + idEvento + "'";
            statement.executeUpdate(deleteEvent);

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ArrayList<String>> checkEvents() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String checkEvents = "SELECT * FROM events";
            ResultSet resultSet = statement.executeQuery(checkEvents);

            ArrayList<ArrayList<String>> eventsList = new ArrayList<>();

            while (resultSet.next()) {
                ArrayList<String> event = new ArrayList<>();
                event.add(resultSet.getString("id"));
                event.add(resultSet.getString("name"));
                event.add(resultSet.getString("code"));
                event.add(resultSet.getString("local"));
                event.add(resultSet.getString("date"));
                event.add(resultSet.getString("timeStart"));
                event.add(resultSet.getString("timeEnd"));
                event.add(resultSet.getString("nPresences"));

                eventsList.add(event);
            }

            return eventsList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean generateCode(int eventId, int eventCode, int tempo) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String checkRepeatedCode = "SELECT * FROM events";
            ResultSet resultSet = statement.executeQuery(checkRepeatedCode);
            while (resultSet.next()) {
                if (resultSet.getInt("code") == eventCode)
                    return false;
            }


            String getEvent = "SELECT * FROM events WHERE id = '" + eventId + "'";
            resultSet = statement.executeQuery(getEvent);
            if (!resultSet.next())
                return false;

            String timeStart = resultSet.getString("timeStart");
            String timeEnd = resultSet.getString("timeEnd");

            // VERIFICAR DATA
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDateTime now = LocalDateTime.now();
            if (dtf.format(now).compareTo(resultSet.getString("date")) != 0)
                return false;

            // VERIFICAR HORA
            dtf = DateTimeFormatter.ofPattern("HH:mm");
            now = LocalDateTime.now();
            if (dtf.format(now).compareTo(timeStart) < 0 || dtf.format(now).compareTo(timeEnd) > 0)
                return false;


            String generateCode = "UPDATE events SET code = '" + eventCode + "' WHERE id = '" + eventId + "'";
            statement.executeUpdate(generateCode);

            new threadCodigos(this, new Codigo(eventId, tempo)).start();

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ArrayList<String>> checkPresencesEventID(int eventID) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String query = "SELECT a.name, a.email, a.nIdentificacao " +
                    "FROM accounts a " +
                    "INNER JOIN accounts_events ae ON a.id = ae.account_id " +
                    "WHERE ae.event_id = '" + eventID + "'";

            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<ArrayList<String>> accountsList = new ArrayList<>();

            while (resultSet.next()) {
                ArrayList<String> account = new ArrayList<>();

                account.add(resultSet.getString("name"));
                account.add(resultSet.getString("email"));
                account.add(resultSet.getString("nIdentificacao"));

                accountsList.add(account);
            }

            return accountsList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deletePresence(int eventID, String email) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String checkEmail = "SELECT * FROM accounts WHERE email = '" + email + "'";
            ResultSet resultSet = statement.executeQuery(checkEmail);
            if (!resultSet.next())
                return false;

            int idAccount = Integer.parseInt(resultSet.getString("id"));

            String checkPresence = "SELECT * FROM accounts_events WHERE account_id = '" + idAccount + "' AND event_id = '" + eventID + "'";
            resultSet = statement.executeQuery(checkPresence);
            if (!resultSet.next())
                return false;

            String deletePresence = "DELETE FROM accounts_events WHERE account_id = '" + idAccount + "' AND event_id = '" + eventID + "'";
            statement.executeUpdate(deletePresence);

            String getEvent = "SELECT * FROM events WHERE id = '" + eventID + "'";
            resultSet = statement.executeQuery(getEvent);
            if (!resultSet.next())
                return false;

            int nPresences = Integer.parseInt(resultSet.getString("nPresences"));

            String updateNPresences = "UPDATE events SET nPresences = '" + (nPresences - 1) + "' WHERE id = '" + eventID + "'";
            statement.executeUpdate(updateNPresences);

            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addPresence(int eventID, String email) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String checkEmail = "SELECT * FROM accounts WHERE email = '" + email + "'";
            ResultSet resultSet = statement.executeQuery(checkEmail);
            if (!resultSet.next())
                return false;

            int idAccount = Integer.parseInt(resultSet.getString("id"));

            String checkPresence = "SELECT * FROM accounts_events WHERE account_id = '" + idAccount + "' AND event_id = '" + eventID + "'";
            resultSet = statement.executeQuery(checkPresence);
            if (resultSet.next())
                return false;

            String addPresence = "INSERT INTO accounts_events (account_id, event_id) VALUES ('" + idAccount + "', '" + eventID + "')";
            statement.executeUpdate(addPresence);

            String getEvent = "SELECT * FROM events WHERE id = '" + eventID + "'";
            resultSet = statement.executeQuery(getEvent);
            if (!resultSet.next())
                return false;

            int nPresences = Integer.parseInt(resultSet.getString("nPresences"));

            String updateNPresences = "UPDATE events SET nPresences = '" + (nPresences + 1) + "' WHERE id = '" + eventID + "'";
            statement.executeUpdate(updateNPresences);

            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // ALUNO
    public ArrayList<String> getClientData(String email) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String checkPassword = "SELECT * FROM accounts WHERE email = '" + email + "'";
            ResultSet resultSet = statement.executeQuery(checkPassword);

            // NOT NECESSARY BECAUSE LOGIN RETURNED TRUE
            if (!resultSet.next())
                return null;


            ArrayList<String> clientData = new ArrayList<>();

            clientData.add(resultSet.getString("admin"));
            clientData.add(resultSet.getString("name"));
            clientData.add(resultSet.getString("email"));
            clientData.add(resultSet.getString("password"));
            clientData.add(resultSet.getString("nIdentificacao"));

            return clientData;
        }
    }

    public ArrayList<String> editData(String novoEmail, String email, String password, String name, String nIdentificacao) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String editData = "UPDATE accounts SET name = '" + name + "', email = '" + novoEmail + "', password = '" + password + "',  nIdentificacao = '" + nIdentificacao + "' WHERE email = '" + email + "'";
            statement.executeUpdate(editData);

            String checkPassword = "SELECT * FROM accounts WHERE email = '" + novoEmail + "'";
            ResultSet resultSet = statement.executeQuery(checkPassword);

            // NOT NECESSARY BECAUSE LOGIN RETURNED TRUE
            if (!resultSet.next())
                return null;

            ArrayList<String> clientData = new ArrayList<>();
            clientData.add(resultSet.getString("admin"));
            clientData.add(resultSet.getString("name"));
            clientData.add(resultSet.getString("email"));
            clientData.add(resultSet.getString("password"));
            clientData.add(resultSet.getString("nIdentificacao"));

            return clientData;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean submitCode(String email, String code) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String checkCode = "SELECT * FROM events WHERE code = '" + code + "'";
            ResultSet resultSet_code = statement.executeQuery(checkCode);
            if (!resultSet_code.next())
                return false;

            int idEvento = Integer.parseInt(resultSet_code.getString("id"));
            int nPresences = Integer.parseInt(resultSet_code.getString("nPresences"));


            String checkEmail = "SELECT * FROM accounts WHERE email = '" + email + "'";
            ResultSet resultSet_email = statement.executeQuery(checkEmail);

            int idAccount = Integer.parseInt(resultSet_email.getString("id"));


            // VERIFICAR SE JA TEM PRESENÇA
            String checkPresence = "SELECT * FROM accounts_events WHERE account_id = '" + idAccount + "' AND event_id = '" + idEvento + "'";
            if (statement.executeQuery(checkPresence).next())
                return false;

            String addRegisteredPresence = "INSERT INTO accounts_events (account_id, event_id) VALUES ('" + idAccount + "', '" + idEvento + "')";
            statement.executeUpdate(addRegisteredPresence);

            String updateNPresences = "UPDATE events SET nPresences = '" + (nPresences + 1) + "' WHERE id = '" + idEvento + "'";
            statement.executeUpdate(updateNPresences);

            return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<ArrayList<String>> checkPresences(String email) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String query = "SELECT e.id, e.name, e.local, e.date, e.timeStart, e.timeEnd " +
                    "FROM accounts a " +
                    "INNER JOIN accounts_events ae ON a.id = ae.account_id " +
                    "INNER JOIN events e ON ae.event_id = e.id " +
                    "WHERE a.email = '" + email + "'";

            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<ArrayList<String>> presencesList = new ArrayList<>();

            while (resultSet.next()) {
                ArrayList<String> presence = new ArrayList<>();
                presence.add(Integer.toString(resultSet.getInt("id")));
                presence.add(resultSet.getString("name"));
                presence.add(resultSet.getString("local"));
                presence.add(resultSet.getString("date"));
                presence.add(resultSet.getString("timeStart"));
                presence.add(resultSet.getString("timeEnd"));

                presencesList.add(presence);
            }

            return presencesList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // NOT A REQUEST, ONLY GETTING INFO FOR CSV
    public ArrayList<String> checkEvent(int idEvent) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String checkEvents = "SELECT * FROM events WHERE id = '" + idEvent + "'";
            ResultSet resultSet = statement.executeQuery(checkEvents);

            ArrayList<String> event = new ArrayList<>();

            while (resultSet.next()) {
                event.add(resultSet.getString("name"));
                event.add(resultSet.getString("local"));
                event.add(resultSet.getString("date"));
                event.add(resultSet.getString("timeStart"));
                event.add(resultSet.getString("timeEnd"));
            }

            return event;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> checkStudent(String email) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            String checkStudent = "SELECT * FROM accounts WHERE email = '" + email + "'";
            ResultSet resultSet = statement.executeQuery(checkStudent);

            ArrayList<String> student = new ArrayList<>();

            while (resultSet.next()) {
                student.add(resultSet.getString("name"));
                student.add(resultSet.getString("email"));
                student.add(resultSet.getString("nIdentificacao"));
            }

            return student;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

