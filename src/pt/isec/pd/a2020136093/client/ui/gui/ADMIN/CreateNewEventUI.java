package pt.isec.pd.a2020136093.client.ui.gui.ADMIN;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import pt.isec.pd.a2020136093.client.communication.ManageConnections;
import pt.isec.pd.a2020136093.client.ui.gui.RESOURCES.CSSManager;

public class CreateNewEventUI extends BorderPane {
    ManageConnections mc;
    HBox hbox0;
    HBox hbox1, hbox2, hbox3, hbox4, hbox5;
    TextField EventNameField, EventLocalField, EventDateField, EventStartHourField, EventFinishHourField;

    Label lblTitle, lblNameEvent, lblLocalEvent, lblDateEvent, lblHourEventStart, lblHourEventEnd, lblResultado;
    Button btnCreateEvent,btnBack;

    public CreateNewEventUI(ManageConnections mc) {
        this.mc = mc;

        CSSManager.applyCSS(this, "style1.css");

        createViews();
        registerHandlers();
        update();
    }


    private void createViews() {
        this.setBackground(new Background(new BackgroundFill(Color.rgb(240, 240, 240), null, null)));

        lblTitle = new Label("Criar Evento");
        lblTitle.setStyle("-fx-text-fill: #333; -fx-font-size: 36px; -fx-font-weight: bold;");


        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        lblNameEvent = new Label("Nome do Evento");
        lblNameEvent.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        EventNameField = new TextField();
        EventNameField.setMinWidth(200);

        lblLocalEvent = new Label("Local do Evento");
        lblLocalEvent.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        EventLocalField = new TextField();
        EventLocalField.setMinWidth(200);

        lblDateEvent = new Label("Data do Evento [DD-MM-YYYY]");
        lblDateEvent.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        EventDateField = new TextField();
        EventDateField.setMinWidth(200);

        lblHourEventStart = new Label("Hora de inicio do Evento [HH:SS]");
        lblHourEventStart.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        EventStartHourField = new TextField();
        EventStartHourField.setMinWidth(200);

        lblHourEventEnd = new Label("Hora de fim do Evento [HH:SS]");
        lblHourEventEnd.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        EventFinishHourField = new TextField();
        EventFinishHourField.setMinWidth(200);

        gridPane.add(lblNameEvent, 0, 0);
        gridPane.add(EventNameField, 1, 0);
        gridPane.add(lblLocalEvent, 0, 1);
        gridPane.add(EventLocalField, 1, 1);
        gridPane.add(lblDateEvent, 0, 2);
        gridPane.add(EventDateField, 1, 2);
        gridPane.add(lblHourEventStart, 0, 3);
        gridPane.add(EventStartHourField, 1, 3);
        gridPane.add(lblHourEventEnd, 0, 4);
        gridPane.add(EventFinishHourField, 1, 4);


        lblResultado = new Label();
        lblResultado.setVisible(false);

        hbox0 = new HBox();
        btnCreateEvent = new Button("Criar Evento");
        btnCreateEvent.setMinWidth(120);
        btnCreateEvent.getStyleClass().add("button");

        btnBack = new Button("Voltar");
        btnBack.setMinWidth(120);
        btnBack.getStyleClass().add("button");
        hbox0.getChildren().addAll(btnCreateEvent, btnBack);
        hbox0.setSpacing(15);
        hbox0.setAlignment(Pos.CENTER);


        VBox vBox = new VBox(lblTitle, gridPane, lblResultado, hbox0);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        VBox.setMargin(gridPane, new Insets(25, 0, 0, 0)); // Set top margin for the button
        VBox.setMargin(btnCreateEvent, new Insets(30, 0, 0, 0)); // Set top margin for the button

        this.setCenter(vBox);
    }



    private void registerHandlers() {
        btnCreateEvent.setOnAction(event -> {
            if(mc.criarEvento(
                    EventNameField.getText(),
                    EventLocalField.getText(),
                    EventDateField.getText(),
                    EventStartHourField.getText(),
                    EventFinishHourField.getText()
            )){
                EventNameField.setText("");
                EventLocalField.setText("");
                EventDateField.setText("");
                EventStartHourField.setText("");
                EventFinishHourField.setText("");

                lblResultado.setText("Evento criado com sucesso!");
                lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 16px; -fx-font-weight: bold;");
                lblResultado.setVisible(true);

                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> {
                    lblResultado.setVisible(false);
                });
                pause.play();
            }
            else{
                EventNameField.setText("");
                EventLocalField.setText("");
                EventDateField.setText("");
                EventStartHourField.setText("");
                EventFinishHourField.setText("");

                lblResultado.setText("Houve um erro ao criar o evento!");
                lblResultado.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
                lblResultado.setVisible(true);

                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> {
                    lblResultado.setVisible(false);
                });
                pause.play();
            }
        });


        btnBack.setOnAction(event -> {
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.close();
        });
    }


    private void update(){

    }


}


