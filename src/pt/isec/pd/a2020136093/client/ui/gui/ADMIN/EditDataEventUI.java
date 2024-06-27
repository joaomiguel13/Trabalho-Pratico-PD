package pt.isec.pd.a2020136093.client.ui.gui.ADMIN;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import pt.isec.pd.a2020136093.client.communication.ManageConnections;
import pt.isec.pd.a2020136093.client.ui.gui.RESOURCES.CSSManager;
import pt.isec.pd.a2020136093.client.ui.gui.RESOURCES.PopUpCreator;

public class EditDataEventUI extends BorderPane {
    ManageConnections mc;
    Font titleFont, buttonsFont;

    String id;

    Label lblTitle, lblResultado;
    Button btnEditName, btnEditLocal, btnEditData, btnEditHourStart, btnBack, btnEditHourEnd;

    public EditDataEventUI(ManageConnections mc, String id) {
        this.mc = mc;
        this.id = id;

        CSSManager.applyCSS(this, "style1.css");

        createViews();
        registerHandlers();
        update();
    }


    private void createViews() {
        this.setBackground(new Background(new BackgroundFill(Color.rgb(240, 240, 240), null, null)));

        lblTitle = new Label("Editar dados do Evento");
        lblTitle.setStyle("-fx-text-fill: #333; -fx-font-size: 36px; -fx-font-weight: bold;");

        lblResultado = new Label();
        lblResultado.setVisible(false);

        btnEditName = new Button("Editar nome");
        btnEditName.setMinWidth(120);
        btnEditName.getStyleClass().add("button");

        btnEditLocal = new Button("Editar local");
        btnEditLocal.setMinWidth(120);
        btnEditLocal.getStyleClass().add("button");

        btnEditData = new Button("Editar data");
        btnEditData.setMinWidth(120);
        btnEditData.getStyleClass().add("button");

        btnEditHourStart = new Button("Editar hora de inicio");
        btnEditHourStart.setMinWidth(120);
        btnEditHourStart.getStyleClass().add("button");

        btnEditHourEnd = new Button("Editar hora de fim");
        btnEditHourEnd.setMinWidth(120);
        btnEditHourEnd.getStyleClass().add("button");

        btnBack = new Button("Voltar");
        btnBack.setMinWidth(120);
        btnBack.getStyleClass().add("button");


        VBox vBox = new VBox(lblTitle, btnEditName, btnEditLocal, btnEditData, btnEditHourStart, btnEditHourEnd, lblResultado, btnBack);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        VBox.setMargin(btnEditName, new Insets(25, 0, 0, 0)); // Set top margin for the button
        VBox.setMargin(btnEditName, new Insets(35, 0, 0, 0)); // Set top margin for the button

        this.setCenter(vBox);
    }


    private void registerHandlers() {

        btnEditName.setOnAction(event -> {
            String newName = PopUpCreator.editName(null);
            if(newName != null && !newName.equals("")) {

                if (mc.editEvent(id, 1, newName)) {
                    lblResultado.setText("Nome alterado com sucesso!");
                    lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 25px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                } else {
                    lblResultado.setText("Houve um erro a editar o nome do evento!\nVerifique se o evento já tem presenças registadas!");
                    lblResultado.setStyle("-fx-text-fill: red; -fx-font-size: 25px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                }
            }
        });

        btnEditLocal.setOnAction(event -> {
            String local = PopUpCreator.editLocal();
            if(local != null && !local.equals("")){

                if (mc.editEvent(id, 2, local)) {
                    lblResultado.setText("Local alterado com sucesso!");
                    lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 25px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                } else {
                    lblResultado.setText("Houve um erro a editar o local do evento!\nVerifique se o evento já tem presenças registadas!");
                    lblResultado.setStyle("-fx-text-fill: red; -fx-font-size: 25px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                }
            }
        });

        btnEditData.setOnAction(event -> {
            String data = PopUpCreator.editData();
            if(data != null && !data.equals("")) {

                if (mc.editEvent(id, 3, data)) {
                    lblResultado.setText("Data alterada com sucesso!");
                    lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 25px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                } else {
                    lblResultado.setText("Houve um erro a editar a data do evento!\nVerifique se o evento já tem presenças registadas!");
                    lblResultado.setStyle("-fx-text-fill: red; -fx-font-size: 25px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                }
            }
        });

        btnEditHourStart.setOnAction(event -> {
            String hourStart = PopUpCreator.editHourStart();
            if(hourStart != null && !hourStart.equals("")) {

                if (mc.editEvent(id, 4, hourStart)) {
                    lblResultado.setText("Hora de inicio alterada com sucesso!");
                    lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 25px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                } else {
                    lblResultado.setText("Houve um erro a editar a hora de inicio do evento!\nVerifique se o evento já tem presenças registadas!");
                    lblResultado.setStyle("-fx-text-fill: red; -fx-font-size: 25px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                }
            }

        });

        btnEditHourEnd.setOnAction(event -> {
            String hourEnd = PopUpCreator.editHourEnd();
            if(hourEnd != null && !hourEnd.equals("")  ) {

                if (mc.editEvent(id, 5, hourEnd)) {
                    lblResultado.setText("Hora de fim alterada com sucesso!");
                    lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 25px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                } else {
                    lblResultado.setText("Houve um erro a editar a hora de fim do evento!\nVerifique se o evento já tem presenças registadas!");
                    lblResultado.setStyle("-fx-text-fill: red; -fx-font-size: 25px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                }
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