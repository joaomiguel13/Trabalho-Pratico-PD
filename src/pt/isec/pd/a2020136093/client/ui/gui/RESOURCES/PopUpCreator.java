package pt.isec.pd.a2020136093.client.ui.gui.RESOURCES;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class PopUpCreator {

    public static String editName(String oldName) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Atualizar Nome");
        if(oldName != null) {
            dialog.setHeaderText("Nome: " + oldName);
        }
        else{
            dialog.setHeaderText("Editar nome do evento");
        }
        dialog.setContentText("Novo nome:");

        // Traditional way to get the response value.
        return dialog.showAndWait()
                .map(result -> {
                    System.out.println();

                    return result;
                })
                .orElse(null);
    }

    public static String editEmail(String oldEmail) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Atualizar Email");
        dialog.setHeaderText("Email: "+ oldEmail);
        dialog.setContentText("Novo email:");

        // Traditional way to get the response value.
        return dialog.showAndWait()
                .map(result -> {
                    System.out.println();

                    return result;
                })
                .orElse(null);
    }

    public static String editPassword(String oldPassword) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Atualizar Password");
        dialog.setHeaderText("Password: "+oldPassword);
        dialog.setContentText("Nova password:");

        // Traditional way to get the response value.
        return dialog.showAndWait()
                .map(result -> {
                    System.out.println();

                    return result;
                })
                .orElse(null);
    }

    public static String editIDNumber(String oldIdN) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Atualizar Numero de Identificacao");
        dialog.setHeaderText("Número Identificação: "+oldIdN);
        dialog.setContentText("Novo Número Identificacao:");

        // Traditional way to get the response value.
        return dialog.showAndWait()
                .map(result -> {

                    return result;
                })
                .orElse(null);
    }

    public static String sendPresenceCode() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Submeter código de presença");
        dialog.setHeaderText("Introduza o código de presença");
        dialog.setContentText("Código:");

        // Traditional way to get the response value.
        return dialog.showAndWait()
                .map(result -> {
                    System.out.println();

                    return result;
                })
                .orElse(null);
    }




    public static void exitAlert(Button btnExit) {

        btnExit.setOnAction(event -> {
            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    null,
                    ButtonType.YES, ButtonType.NO
            );

            alert.setTitle("Sair");
            alert.setHeaderText("Quer realmente sair?");

            //ImageView exitIcon = new ImageView(ImageManager.getImage("pacman-sad.png"));
            //exitIcon.setFitHeight(100);
            //exitIcon.setFitWidth(100);
            //alert.getDialogPane().setGraphic(exitIcon);

            alert.showAndWait().ifPresent(response -> {
                switch (response.getButtonData()){
                    case YES -> {
                        Platform.exit();
                    }
                    case NO -> {}
                }
            });
        });
    }

    public static String editLocal() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Atualizar Local");
        dialog.setHeaderText("Introduza o novo local");
        dialog.setContentText("Local:");

        // Traditional way to get the response value.
        return dialog.showAndWait()
                .map(result -> {
                    System.out.println("Novo local: " + result);

                    return result;
                })
                .orElse(null);
    }

    public static String editData() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Atualizar Data");
        dialog.setHeaderText("Introduza a nova data");
        dialog.setContentText("Data:");

        // Traditional way to get the response value.
        return dialog.showAndWait()
                .map(result -> {
                    System.out.println("Nova data: " + result);

                    return result;
                })
                .orElse(null);
    }

    public static String editHourStart() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Atualizar Hora de Inicio");
        dialog.setHeaderText("Introduza a nova hora de inicio");
        dialog.setContentText("Hora de Inicio:");

        // Traditional way to get the response value.
        return dialog.showAndWait()
                .map(result -> {
                    System.out.println("Nova hora de inicio: " + result);

                    return result;
                })
                .orElse(null);
    }

    public static String editHourEnd() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Atualizar Hora de Fim");
        dialog.setHeaderText("Introduza a nova hora de fim");
        dialog.setContentText("Hora de Fim:");

        // Traditional way to get the response value.
        return dialog.showAndWait()
                .map(result -> {
                    System.out.println("Nova hora de fim: " + result);

                    return result;
                })
                .orElse(null);
    }

    public static String deleteEventPopUp() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Apagar Evento");
        dialog.setHeaderText("Introduza o ID do evento a apagar");
        dialog.setContentText("ID:");

        return dialog.showAndWait()
                .map(result -> {
                    System.out.println();

                    return result;
                })
                .orElse(null);

    }

    public static void checkStudentPresencesPopUp_list(ArrayList<ArrayList<String>> listaPresencas){
        // Create a TableView
        TableView<ArrayList<String>> tableView = new TableView<>();

        // Create columns for the TableView
        String[] columnHeaders = {"ID", "Nome", "Local", "Data", "Hora de inicio", "Hora de fim"};

        for (int i = 0; i < columnHeaders.length; i++) {
            TableColumn<ArrayList<String>, String> column = new TableColumn<>(columnHeaders[i]);
            final int columnIndex = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            tableView.getColumns().add(column);
        }

        // Create an ObservableList to hold the data
        ObservableList<ArrayList<String>> presencesData = FXCollections.observableArrayList(listaPresencas);
        FilteredList<ArrayList<String>> filteredData = new FilteredList<>(presencesData, p -> true);

        // Set the items of the TableView
        tableView.setItems(filteredData);

        // Set the preferred width of the TableView (adjust as needed)
        tableView.setPrefWidth(900);

        // Create a TextField for filtering
        TextField filterTextField = new TextField();
        filterTextField.setPromptText("Critérios / Filtros");

        // Add a listener to the filter TextField to update the TableView based on user input
        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(event -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Convert event data to lowercase for case-insensitive filtering
                String filterText = newValue.toLowerCase();

                // Check if any column contains the filter text
                for (String value : event) {
                    if (value.toLowerCase().contains(filterText)) {
                        return true;
                    }
                }

                return false;
            });
        });

        // Create a VBox to hold the TextField and TableView
        VBox vBox = new VBox(filterTextField, tableView);
        vBox.setSpacing(25);

        tableView.setPadding(new Insets(10, 10, 10, 10));

        // Create an alert with information type
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registo de Presenças");
        alert.setHeaderText(null);

        // Set the content of the alert to the TableView
        alert.getDialogPane().setContent(vBox);

        // Show the alert
        alert.showAndWait();
    }

    public static void checkEventPresencesPopUp_list(ArrayList<ArrayList<String>> listaPresencas ) {
        // Create a TableView
        TableView<ArrayList<String>> tableView = new TableView<>();

        // Create columns for the TableView
        String[] columnHeaders = {"Nome", "Email", "Número Identificação"};


        for (int i = 0; i < columnHeaders.length; i++) {
            TableColumn<ArrayList<String>, String> column = new TableColumn<>(columnHeaders[i]);
            final int columnIndex = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            tableView.getColumns().add(column);
        }


// Create an ObservableList to hold the data
        ObservableList<ArrayList<String>> presencesData = FXCollections.observableArrayList(listaPresencas);
        FilteredList<ArrayList<String>> filteredData = new FilteredList<>(presencesData, p -> true);

        // Set the items of the TableView
        tableView.setItems(filteredData);

        // Set the preferred width of the TableView (adjust as needed)
        tableView.setPrefWidth(900);

        // Create a TextField for filtering
        TextField filterTextField = new TextField();
        filterTextField.setPromptText("Critérios / Filtros");

        // Add a listener to the filter TextField to update the TableView based on user input
        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(event -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Convert event data to lowercase for case-insensitive filtering
                String filterText = newValue.toLowerCase();

                // Check if any column contains the filter text
                for (String value : event) {
                    if (value.toLowerCase().contains(filterText)) {
                        return true;
                    }
                }

                return false;
            });
        });

        // Create a VBox to hold the TextField and TableView
        VBox vBox = new VBox(filterTextField, tableView);
        vBox.setSpacing(25);

        tableView.setPadding(new Insets(10, 10, 10, 10));

        // Create an alert with information type
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registo de Presenças");
        alert.setHeaderText(null);

        // Set the content of the alert to the TableView
        alert.getDialogPane().setContent(vBox);

        // Show the alert
        alert.showAndWait();
    }

    public static String generateCodePopUp() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Gerar Codigo");
        dialog.setHeaderText("Introduza o ID do evento");
        dialog.setContentText("ID:");

        return dialog.showAndWait()
                .map(result -> {
                    System.out.println();

                    return result;
                })
                .orElse(null);
    }

    public static String checkEventPresencesPopUp() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Consultar presenças do evento");
        dialog.setHeaderText("Introduza o ID do evento");
        dialog.setContentText("ID:");

        return dialog.showAndWait()
                .map(result -> {
                    System.out.println();

                    return result;
                })
                .orElse(null);
    }

    public static String generateCSV1PopUp() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Gerar CSV");
        dialog.setHeaderText("Introduza o ID do evento");
        dialog.setContentText("ID:");

        return dialog.showAndWait()
                .map(result -> {
                    System.out.println();

                    return result;
                })
                .orElse(null);
    }

    public static String checkStudentPresencesPopUp() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Consultar Presencas do Aluno");
        dialog.setHeaderText("Introduza o email");
        dialog.setContentText("Email:");

        return dialog.showAndWait()
                .map(result -> {
                    System.out.println();

                    return result;
                })
                .orElse(null);
    }


// STUDENT SIDE
    public static void checkPresencesPopUp(ArrayList<ArrayList<String>> listaPresencas) {
        // Create a TableView
        TableView<ArrayList<String>> tableView = new TableView<>();

        // Create columns for the TableView
        String[] columnHeaders = {"ID", "Nome", "Local", "Data", "Hora de inicio", "Hora de fim"};

        for (int i = 0; i < columnHeaders.length; i++) {
            TableColumn<ArrayList<String>, String> column = new TableColumn<>(columnHeaders[i]);
            final int columnIndex = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            tableView.getColumns().add(column);
        }

        // Create an ObservableList to hold the data
        ObservableList<ArrayList<String>> presencesData = FXCollections.observableArrayList(listaPresencas);
        FilteredList<ArrayList<String>> filteredData = new FilteredList<>(presencesData, p -> true);

        // Set the items of the TableView
        tableView.setItems(filteredData);

        // Set the preferred width of the TableView (adjust as needed)
        tableView.setPrefWidth(900);

        // Create a TextField for filtering
        TextField filterTextField = new TextField();
        filterTextField.setPromptText("Critérios / Filtros");

        // Add a listener to the filter TextField to update the TableView based on user input
        filterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(event -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Convert event data to lowercase for case-insensitive filtering
                String filterText = newValue.toLowerCase();

                // Check if any column contains the filter text
                for (String value : event) {
                    if (value.toLowerCase().contains(filterText)) {
                        return true;
                    }
                }

                return false;
            });
        });

        // Create a VBox to hold the TextField and TableView
        VBox vBox = new VBox(filterTextField, tableView);
        vBox.setSpacing(25);

        tableView.setPadding(new Insets(10, 10, 10, 10));

        // Create an alert with information type
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registo de Presenças");
        alert.setHeaderText(null);

        // Set the content of the alert to the TableView
        alert.getDialogPane().setContent(vBox);

        // Show the alert
        alert.showAndWait();
    }

    public static String generateSCV2PopUp() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Gerar CSV");
        dialog.setHeaderText("Introduza o email do aluno");
        dialog.setContentText("EMAIL:");

        return dialog.showAndWait()
                .map(result -> {
                    System.out.println();

                    return result;
                })
                .orElse(null);
    }





    public static String presencePopUp_idEvento() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Consultar presenças do evento");
        dialog.setHeaderText("Introduza o ID do evento");
        dialog.setContentText("ID:");

        return dialog.showAndWait()
                .map(result -> {
                    System.out.println();

                    return result;
                })
                .orElse(null);
    }
    public static String presencePopUp_email() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Introduza o email");
        dialog.setContentText("Email:");

        return dialog.showAndWait()
                .map(result -> {
                    System.out.println();

                    return result;
                })
                .orElse(null);
    }






    public static String editEventPopUp() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Editar Evento");
        dialog.setHeaderText("Introduza o ID do evento a editar");
        dialog.setContentText("ID:");

        return dialog.showAndWait()
                .map(result -> {
                    System.out.println();

                    return result;
                })
                .orElse(null);
    }

    public static String generateCodePopUp_tempo() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Gerar Codigo");
        dialog.setHeaderText("Introduza o tempo de validade do codigo");
        dialog.setContentText("Tempo:");

        return dialog.showAndWait()
                .map(result -> {
                    System.out.println();

                    return result;
                })
                .orElse(null);
    }
}