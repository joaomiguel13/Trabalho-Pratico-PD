package pt.isec.pd.a2020136093.client.ui.gui;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import pt.isec.pd.a2020136093.client.communication.ManageConnections;

public class LoginUI extends BorderPane {
    ManageConnections mc;

    HBox hBox1;
    HBox hbox_email, hbox_pass;
    TextField emailField;
    PasswordField passwordField;

    Label lblRetorno, lblTitle, lblEmail, lblPassword;
    Button btnLogin,btnBack;

    public LoginUI(ManageConnections mc) {
        this.mc = mc;


        //titleFont = FontManager.loadFont("PAC-FONT.TTF",69);
        //buttonsFont = FontManager.loadFont("PressStart2P-Regular.ttf",12);

        createViews();
        registerHandlers();
        update();
    }


    private void createViews() {
        this.setBackground(new Background(new BackgroundFill(Color.rgb(240, 240, 240), null, null)));

        lblTitle = new Label("LOGIN");
        lblTitle.setStyle("-fx-text-fill: #333; -fx-font-size: 36px; -fx-font-weight: bold;");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        lblEmail = new Label("Email");
        lblEmail.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        emailField = new TextField();
        emailField.setMinWidth(200);

        lblPassword = new Label("Password");
        lblPassword.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        passwordField = new PasswordField();
        passwordField.setMinWidth(150);

        gridPane.add(lblEmail, 0, 0);
        gridPane.add(emailField, 1, 0);
        gridPane.add(lblPassword, 0, 1);
        gridPane.add(passwordField, 1, 1);

        lblRetorno = new Label("");
        lblRetorno.setVisible(false);


        hBox1 = new HBox();
        btnLogin = new Button("LOGIN");
        btnLogin.getStyleClass().add("button");

        btnBack = new Button("VOLTAR");
        btnBack.getStyleClass().add("button");

        hBox1.getChildren().addAll(btnLogin, btnBack);
        hBox1.setSpacing(15);
        hBox1.setAlignment(Pos.CENTER);



        VBox vBox = new VBox(lblTitle, gridPane, lblRetorno, hBox1);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(30);
        VBox.setMargin(gridPane, new Insets(5, 0, 0, 0)); // Set top margin for the button

        this.setCenter(vBox);
    }




    private void registerHandlers() {
        RootPane.addPropertyChangeListener("SHOWMENU", evt -> { update(); });
        RootPane.addPropertyChangeListener("SHOWLOGIN", evt -> { update(); });


        btnLogin.setOnAction( event -> {
            int retorno = mc.login(emailField.getText(), passwordField.getText());

            if (retorno == 0) {

                lblRetorno.setVisible(true);
                lblRetorno.setText("Login efetuado com sucesso! Entrando...");
                lblRetorno.setStyle("-fx-text-fill: green; -fx-font-size: 16px; -fx-font-weight: bold;");

                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(e -> {
                    lblRetorno.setVisible(false);
                    RootPane.setShowLogin(false);

                    if(mc.isAdmin())
                        RootPane.setShowAdminMenu(true);

                    else
                        RootPane.setShowStudentMenu(true);
                });
                pause.play();

                emailField.setText("");
                passwordField.setText("");


            }
            else {
                lblRetorno.setVisible(true);
                if(retorno == 1)
                    lblRetorno.setText("Utilizador já logado!");
                else if(retorno == 2)
                    lblRetorno.setText("Credenciais erradas ou Servidor fechou a conexão!");

                lblRetorno.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");

                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(e -> {
                    lblRetorno.setVisible(false);

                });
                pause.play();

                emailField.setText("");
                passwordField.setText("");
            }


        });

        btnBack.setOnAction( event -> {
            RootPane.setShowLogin(false);
            RootPane.setShowMainMenu(true);
        });

    }


    private void update(){
        if(RootPane.showLogin){
            this.setVisible(true);
        }else{
            this.setVisible(false);
        }
    }


}
