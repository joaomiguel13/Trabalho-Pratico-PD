package pt.isec.pd.a2020136093.client.ui.gui.ADMIN;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import pt.isec.pd.a2020136093.client.communication.ManageConnections;
import pt.isec.pd.a2020136093.client.ui.gui.RESOURCES.PopUpCreator;
import pt.isec.pd.a2020136093.client.ui.gui.RootPane;

import java.util.ArrayList;

public class MenuAdminUI extends BorderPane {
    ManageConnections mc;
    Font titleFont, buttonsFont;

    Label lblTitle, lblResultado;
    Button btnCreateNewEvent, btnEditEvent, btnDeleteEvent, btnCheckEvents, btnGenerateCode, btnCheckEventPresences, btnGenerateCSV1, btnCheckStudentPresences, btnGenerateCSV2, btnDeletePresence, btnAddPresence, btnLogout;

    public MenuAdminUI(ManageConnections mc) {

        this.mc = mc;

        //titleFont = FontManager.loadFont("PAC-FONT.TTF",69);
        //buttonsFont = FontManager.loadFont("PressStart2P-Regular.ttf",12);

        createViews();
        registerHandlers();
        update();
    }


    public void createViews() {

        this.setBackground(new Background(new BackgroundFill(Color.rgb(240, 240, 240), null, null)));

        lblTitle = new Label("Bem-vindo admin");
        lblTitle.setStyle("-fx-text-fill: #333; -fx-font-size: 36px; -fx-font-weight: bold;");


        ArrayList<ArrayList<String>> listaEventos = mc.checkEvents();
        // TABLE DOS EVENTOS
        // Create a TableView
        TableView<ArrayList<String>> tableView = new TableView<>();

        // Create columns for the TableView
        String[] columnHeaders = {"ID", "Nome", "Código", "Local", "Data", "Hora de inicio", "Hora de fim", "Presenças"};

        for (int i = 0; i < columnHeaders.length; i++) {
            TableColumn<ArrayList<String>, String> column = new TableColumn<>(columnHeaders[i]);
            final int columnIndex = i;
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex)));
            tableView.getColumns().add(column);

            // Set preferred column width (adjust as needed)
            column.setPrefWidth(100);
        }

        // Create an ObservableList to hold the data
        ObservableList<ArrayList<String>> eventData = FXCollections.observableArrayList(listaEventos);
        FilteredList<ArrayList<String>> filteredData = new FilteredList<>(eventData, p -> true);

        // Set the items of the TableView
        tableView.setItems(filteredData);


        tableView.setMaxHeight(269);

        // Create a TextField for filtering
        TextField filterTextField = new TextField();
        filterTextField.setPromptText("Critérios / Filtros");
        filterTextField.setMaxWidth(500);

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

        filterTextField.setMinWidth(250);

        HBox hBox_top = new HBox();
        hBox_top.setAlignment(Pos.CENTER);
        hBox_top.getChildren().add(filterTextField);
        hBox_top.setSpacing(30);


        // Create a VBox to hold the TextField and TableView
        VBox vBox_filters_table = new VBox(hBox_top, tableView);
        vBox_filters_table.setSpacing(25);
        vBox_filters_table.setAlignment(Pos.CENTER);



        lblResultado = new Label("");
        lblResultado.setVisible(false);


        btnCreateNewEvent = new Button("Criar novo evento");
        btnCreateNewEvent.setMinWidth(120);
        btnCreateNewEvent.getStyleClass().add("button2");

        btnEditEvent = new Button("Editar evento");
        btnEditEvent.setMinWidth(120);
        btnEditEvent.getStyleClass().add("button2");

        btnDeleteEvent = new Button("Eliminar evento");
        btnDeleteEvent.setMinWidth(120);
        btnDeleteEvent.getStyleClass().add("button2");

        btnGenerateCode = new Button("Gerar código evento");
        btnGenerateCode.setMinWidth(120);
        btnGenerateCode.getStyleClass().add("button2");

        btnCheckEventPresences = new Button("Consultar presencas evento");
        btnCheckEventPresences.setMinWidth(120);
        btnCheckEventPresences.getStyleClass().add("button2");

        btnGenerateCSV1 = new Button("Gerar ficheiro CSV (presencas evento)");
        btnGenerateCSV1.setMinWidth(120);
        btnGenerateCSV1.getStyleClass().add("button2");

        btnCheckStudentPresences = new Button("Consultar presencas eventos (aluno)");
        btnCheckStudentPresences.setMinWidth(120);
        btnCheckStudentPresences.getStyleClass().add("button2");

        btnGenerateCSV2 = new Button("Gerar ficheiro CSV (presencas aluno)");
        btnGenerateCSV2.setMinWidth(120);
        btnGenerateCSV2.getStyleClass().add("button2");

        btnDeletePresence = new Button("Eliminar presencas evento");
        btnDeletePresence.setMinWidth(120);
        btnDeletePresence.getStyleClass().add("button2");

        btnAddPresence = new Button("Inserir presenca evento");
        btnAddPresence.setMinWidth(120);
        btnAddPresence.getStyleClass().add("button2");

        btnLogout = new Button("Logout");
        btnLogout.setMinWidth(120);
        btnLogout.getStyleClass().add("button2");

        HBox hBox1 = new HBox(btnCreateNewEvent, btnEditEvent, btnDeleteEvent, btnGenerateCode);
        HBox hBox2 = new HBox(btnCheckEventPresences, btnCheckStudentPresences,btnDeletePresence, btnAddPresence);
        HBox hbox3 = new HBox(btnGenerateCSV1, btnGenerateCSV2);

        hBox1.setAlignment(Pos.CENTER);
        hBox2.setAlignment(Pos.CENTER);
        hbox3.setAlignment(Pos.CENTER);

        hBox1.setSpacing(15);
        hBox2.setSpacing(15);
        hbox3.setSpacing(15);

        VBox vbox_buttons = new VBox(hBox1, hBox2, hbox3);
        vbox_buttons.setAlignment(Pos.CENTER);
        vbox_buttons.setSpacing(15);

        hBox_top.getChildren().add(btnLogout);


        VBox vBox = new VBox(lblTitle,vBox_filters_table, lblResultado, vbox_buttons);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);

        this.setCenter(vBox);
    }


    private void registerHandlers() {
        RootPane.addPropertyChangeListener("SHOWMENU", evt -> { update(); });
        RootPane.addPropertyChangeListener("SHOWADMINMENU", evt -> { update(); });

        btnCreateNewEvent.setOnAction(event -> {
            Stage stage = new Stage();
            Scene scene = new Scene(new CreateNewEventUI(mc), 900, 725);
            //stage.getIcons().add(ImageManager.getImage("pacman-icon.png"));
            stage.setScene(scene);
            stage.setTitle("Criar novo evento");

            stage.setWidth(1000);
            stage.setHeight(700);

            stage.show();
        });

        btnEditEvent.setOnAction(event -> {

            String id = PopUpCreator.editEventPopUp();
            if(id != null) {

                Stage stage = new Stage();
                Scene scene = new Scene(new EditDataEventUI(mc, id), 900, 725);
                //stage.getIcons().add(ImageManager.getImage("pacman-icon.png"));
                stage.setScene(scene);
                stage.setTitle("Editar dados do evento");

                stage.setWidth(1000);
                stage.setHeight(700);

                stage.show();
            }
        });

        btnDeleteEvent.setOnAction(event -> {
            String id = PopUpCreator.deleteEventPopUp();
            if(id != null) {

                if (mc.deleteEvent(id)) {
                    lblResultado.setText("Evento apagado com sucesso! Atualizando...");
                    lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 16px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                } else {
                    lblResultado.setText("Houve um erro ao apagar o evento!\n[Verifique se o evento tem presenças registadas]");
                    lblResultado.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                }
            }
        });


        btnGenerateCode.setOnAction(event->{
            String id = PopUpCreator.generateCodePopUp();
            String tempo = PopUpCreator.generateCodePopUp_tempo();

            if(id != null && tempo != null) {

                if (mc.generateEventCode(id,tempo)) {
                    lblResultado.setText("Codigo gerado com sucesso! Atualizando...");
                    lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 16px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                }
                else {
                    lblResultado.setText("Houve um erro ao gerar o código do evento!\n[Verique se o evento já se econtra ativo]");
                    lblResultado.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                }
            }
        });

        btnCheckEventPresences.setOnAction(event->{
            String id = PopUpCreator.checkEventPresencesPopUp();

            if(id != null) {

                ArrayList<ArrayList<String>> listaPresencas = mc.checkPresencesEvent(id);

                if(listaPresencas.size() == 0){
                    lblResultado.setText("Evento não existe ou sem presenças registadas!");
                    lblResultado.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                }
                else {

                    PopUpCreator.checkEventPresencesPopUp_list(listaPresencas);
                }
            }
        });

        btnGenerateCSV1.setOnAction(event->{
            String id = PopUpCreator.generateCSV1PopUp();
            if(id != null) {

                if (mc.generateCSV_event(id)) {
                    lblResultado.setText("Ficheiro CSV gerado com sucesso! Atualizando...");
                    lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 16px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });

                    pause.play();
                } else {
                    lblResultado.setText("Houve um erro ao gerar o ficheiro CSV!");
                    lblResultado.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                }
            }

        });


        btnCheckStudentPresences.setOnAction(event->{

            String email = PopUpCreator.checkStudentPresencesPopUp();

            ArrayList<ArrayList<String>> listaPresencas_doAluno = mc.checkPresences2(email);
            if(listaPresencas_doAluno.size() == 0){
                lblResultado.setText("Aluno não existe ou sem presenças registadas!");
                lblResultado.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
                lblResultado.setVisible(true);

                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(e -> {
                    lblResultado.setVisible(false);
                });
                pause.play();
            }
            else {

                PopUpCreator.checkStudentPresencesPopUp_list(listaPresencas_doAluno);
            }


        });
        btnGenerateCSV2.setOnAction(event->{
            String email = PopUpCreator.generateSCV2PopUp();
            if(email != null) {

                if (mc.generateCSV_student(email)) {
                    lblResultado.setText("Ficheiro CSV gerado com sucesso! Atualizando...");
                    lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 16px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });

                    pause.play();
                } else {
                    lblResultado.setText("Houve um erro ao gerar o ficheiro CSV!");
                    lblResultado.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                }
            }
        });

        btnDeletePresence.setOnAction(event->{
            String id = PopUpCreator.presencePopUp_idEvento();
            String email = PopUpCreator.presencePopUp_email();
            if(id != null && email != null) {

                if (mc.deletePresence(id, email)) {
                    lblResultado.setText("Presença apagada com sucesso! Atualizando...");
                    lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 16px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                } else {
                    lblResultado.setText("Houve um erro ao apagar a presença!");
                    lblResultado.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                }
            }
        });

       btnAddPresence.setOnAction(event->{
           String id= PopUpCreator.presencePopUp_idEvento();
           String email= PopUpCreator.presencePopUp_email();
           if(id != null && email != null) {

               if (mc.addPresence(id, email)) {
                   lblResultado.setText("Presença adicionada com sucesso! Atualizando...");
                   lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 16px; -fx-font-weight: bold;");
                   lblResultado.setVisible(true);

                   PauseTransition pause = new PauseTransition(Duration.seconds(2));
                   pause.setOnFinished(e -> {
                       lblResultado.setVisible(false);
                   });
                   pause.play();
               } else {
                   lblResultado.setText("Presença já registada ou parâmetros inválidos!");
                   lblResultado.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
                   lblResultado.setVisible(true);

                   PauseTransition pause = new PauseTransition(Duration.seconds(2));
                   pause.setOnFinished(e -> {
                       lblResultado.setVisible(false);
                   });
                   pause.play();
               }
           }
        });




       btnLogout.setOnAction(event -> {
           mc.logout();
           RootPane.setShowAdminMenu(false);
           RootPane.setShowLogin(true);
       });


    }


    private void update(){
        if(RootPane.showAdminMenu){
            this.setVisible(true);
            Stage stage = (Stage) this.getScene().getWindow();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    mc.logout();
                }
            });
        }else{
            this.setVisible(false);
        }
    }

    public void refresh_async(){
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            this.getChildren().clear();
            createViews();
            registerHandlers();
        });
        pause.play();
    }
}
