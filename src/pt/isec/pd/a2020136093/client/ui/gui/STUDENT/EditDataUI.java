package pt.isec.pd.a2020136093.client.ui.gui.STUDENT;

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

public class EditDataUI extends BorderPane {
    ManageConnections mc;
    Font titleFont, buttonsFont;

    Label lblTitle, lblResultado;
    Button btnEditName, btnEditEmail, btnEditPassword, btnEditIDNumber, btnClose;

    public EditDataUI(ManageConnections mc) {
        this.mc = mc;

        CSSManager.applyCSS(this, "style1.css");
        //titleFont = FontManager.loadFont("PAC-FONT.TTF",69);
        //buttonsFont = FontManager.loadFont("PressStart2P-Regular.ttf",12);

        createViews();
        registerHandlers();
        update();
    }


    private void createViews() {
        this.setBackground(new Background(new BackgroundFill(Color.rgb(240, 240, 240), null, null)));

        lblTitle = new Label("Editar dados de registo");
        lblTitle.setStyle("-fx-text-fill: #333; -fx-font-size: 36px; -fx-font-weight: bold;");

        lblResultado = new Label("");
        lblResultado.setVisible(false);

        btnEditName = new Button("Editar nome");
        btnEditName.setMinWidth(120);
        btnEditName.getStyleClass().add("button");

        btnEditEmail = new Button("Editar email");
        btnEditEmail.setMinWidth(120);
        btnEditEmail.getStyleClass().add("button");

        btnEditPassword = new Button("Editar password");
        btnEditPassword.setMinWidth(120);
        btnEditPassword.getStyleClass().add("button");

        btnEditIDNumber = new Button("Editar numero de identificacao");
        btnEditIDNumber.setMinWidth(120);
        btnEditIDNumber.getStyleClass().add("button");

        btnClose = new Button("Fechar");
        btnClose.setMinWidth(120);
        btnClose.getStyleClass().add("button");


        VBox vBox = new VBox(lblTitle, btnEditName, btnEditEmail, btnEditPassword, btnEditIDNumber, lblResultado, btnClose);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        VBox.setMargin(btnEditName, new Insets(55, 0, 0, 0)); // Set top margin for the button
        VBox.setMargin(btnClose, new Insets(35, 0, 0, 0)); // Set top margin for the button

        this.setCenter(vBox);
    }


    private void registerHandlers() {

        btnEditName.setOnAction(event -> {
            String nome = PopUpCreator.editName(mc.getName());
            if(nome != null) {

                if (mc.editData(1, nome)) {
                    lblResultado.setText("Nome alterado com sucesso!");
                    lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 25px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                } else {
                    lblResultado.setText("Houve um erro ao alterar o nome!");
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

        btnEditEmail.setOnAction(event -> {
            String email = PopUpCreator.editEmail(mc.getEmail());
            if(email != null && !email.equals("")) {

                if (mc.editData(2, email)) {
                    lblResultado.setText("Email alterado com sucesso!");
                    lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 25px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                } else {
                    lblResultado.setText("Houve um erro ao alterar o email!");
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

        btnEditPassword.setOnAction(event -> {
            String password = PopUpCreator.editPassword(mc.getPassword());
            if(password != null && !password.equals("")) {

                if (mc.editData(3, password)) {
                    lblResultado.setText("Password alterada com sucesso!");
                    lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 25px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                } else {
                    lblResultado.setText("Houve um erro ao alterar a password!");
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

        btnEditIDNumber.setOnAction(event -> {
            String idNumber = PopUpCreator.editIDNumber(mc.getNIdentificacao());
            if(idNumber != null && !idNumber.equals("")) {

                if (mc.editData(4, idNumber)) {
                    lblResultado.setText("Número de Indentificação alterado com sucesso!");
                    lblResultado.setStyle("-fx-text-fill: green; -fx-font-size: 25px; -fx-font-weight: bold;");
                    lblResultado.setVisible(true);

                    PauseTransition pause = new PauseTransition(Duration.seconds(3));
                    pause.setOnFinished(e -> {
                        lblResultado.setVisible(false);
                    });
                    pause.play();
                } else {
                    lblResultado.setText("Houve um erro ao alterar o número de identificação!");
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


        btnClose.setOnAction(event -> {
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });
    }


    private void update(){
        /*if(RootPane.showMainMenu){
            this.setVisible(true);
        }else{
            this.setVisible(false);
        }*/
    }

}
