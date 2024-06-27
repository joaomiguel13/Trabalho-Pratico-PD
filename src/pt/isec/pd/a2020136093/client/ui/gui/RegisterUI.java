package pt.isec.pd.a2020136093.client.ui.gui;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import pt.isec.pd.a2020136093.client.communication.ManageConnections;

public class RegisterUI extends BorderPane {
    ManageConnections mc;

    HBox hBox1;
    HBox hbox1, hbox2, hbox3, hbox4;
    TextField nameField, emailField, passwordField, nIdentificacaoField;

    Label lblRetorno, lblTitle, lblName, lblEmail, lblPassword, lblNIdentificacao;
    Button btnRegister,btnBack;

    public RegisterUI(ManageConnections mc) {

        this.mc = mc;
        //titleFont = FontManager.loadFont("PAC-FONT.TTF",69);
        //buttonsFont = FontManager.loadFont("PressStart2P-Regular.ttf",12);

        createViews();
        registerHandlers();
        update();
    }


    private void createViews() {
        this.setBackground(new Background(new BackgroundFill(Color.rgb(240, 240, 240), null, null)));

        lblTitle = new Label("REGISTAR NOVA CONTA");
        lblTitle.setStyle("-fx-text-fill: #333; -fx-font-size: 36px; -fx-font-weight: bold;");


        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        lblEmail = new Label("Email");
        lblEmail.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        emailField = new TextField();
        emailField.setMinWidth(200);

        gridPane.add(lblEmail, 0, 0);
        gridPane.add(emailField, 1, 0);


        lblPassword = new Label("Password");
        lblPassword.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        passwordField = new PasswordField();
        passwordField.setMinWidth(150);

        gridPane.add(lblPassword, 0, 1);
        gridPane.add(passwordField, 1, 1);


        lblName = new Label("Nome");
        lblName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        nameField = new TextField();
        nameField.setMinWidth(150);

        gridPane.add(lblName, 0, 2);
        gridPane.add(nameField, 1, 2);


        lblNIdentificacao = new Label("Número\nIdentificação");
        lblNIdentificacao.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        nIdentificacaoField = new TextField();
        nIdentificacaoField.setMinWidth(120);

        gridPane.add(lblNIdentificacao, 0, 3);
        gridPane.add(nIdentificacaoField, 1, 3);


        lblRetorno = new Label("");
        lblRetorno.setVisible(false);

        hBox1 = new HBox();
        btnRegister = new Button("REGISTAR");
        btnRegister.setMinWidth(120);
        btnRegister.getStyleClass().add("button");

        btnBack = new Button("VOLTAR");
        btnBack.setMinWidth(120);
        btnBack.getStyleClass().add("button");
        hBox1.getChildren().addAll(btnRegister,btnBack);
        hBox1.setSpacing(15);
        hBox1.setAlignment(Pos.CENTER);


        VBox vBox = new VBox(lblTitle, gridPane, lblRetorno, hBox1);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(35);
        VBox.setMargin(gridPane, new Insets(15, 0, 0, 0)); // Set top margin for the button

        this.setCenter(vBox);
    }


    private void registerHandlers() {
        RootPane.addPropertyChangeListener("SHOWMENU", evt -> { update(); });
        RootPane.addPropertyChangeListener("SHOWREGISTER", evt -> { update(); });


        btnRegister.setOnAction( event -> {

            if(mc.register(emailField.getText(), passwordField.getText(), nameField.getText(), nIdentificacaoField.getText())){
                lblRetorno.setVisible(true);
                lblRetorno.setText("Conta registada com sucesso!");
                lblRetorno.setStyle("-fx-text-fill: green; -fx-font-size: 16px; -fx-font-weight: bold;");

                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(e -> {
                    lblRetorno.setVisible(false);

                    RootPane.setShowRegister(false);
                    RootPane.setShowLogin(true);

                });
                pause.play();

                emailField.setText("");
                passwordField.setText("");
                nameField.setText("");
                nIdentificacaoField.setText("");

            }
            else{
                lblRetorno.setVisible(true);
                lblRetorno.setText("Email já registado!");
                lblRetorno.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");

                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(e -> {
                    lblRetorno.setVisible(false);

                    emailField.setText("");
                    passwordField.setText("");
                    nameField.setText("");
                    nIdentificacaoField.setText("");

                });
                pause.play();
            }
        });

        btnBack.setOnAction( event -> {
            RootPane.setShowRegister(false);
            RootPane.setShowMainMenu(true);
        });

    }


    private void update(){
        if(RootPane.showRegister){
            this.setVisible(true);
        }else{
            this.setVisible(false);
        }
    }


}
