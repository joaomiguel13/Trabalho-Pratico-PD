package pt.isec.pd.a2020136093.client.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pd.a2020136093.client.communication.ManageConnections;
import pt.isec.pd.a2020136093.client.ui.gui.RESOURCES.PopUpCreator;

public class MainMenuUI extends BorderPane {
    ManageConnections mc;

    Font titleFont, buttonsFont;
    Label lblTitle;
    Button btnLogin,btnRegister,btnExit;

    public MainMenuUI(ManageConnections mc) {
        this.mc = mc;

        //titleFont = FontManager.loadFont("PAC-FONT.TTF",69);
        //buttonsFont = FontManager.loadFont("PressStart2P-Regular.ttf",12);

        createViews();
        registerHandlers();
        update();
    }


    private void createViews() {
        this.setBackground(new Background(new BackgroundFill(Color.rgb(240, 240, 240), null, null)));

        lblTitle = new Label("PRESENCE REGISTER");
        lblTitle.setStyle("-fx-text-fill: #333; -fx-font-size: 36px; -fx-font-weight: bold;");

        btnLogin = new Button("LOGIN");
        btnLogin.setMinWidth(120);
        btnLogin.getStyleClass().add("button");

        btnRegister = new Button("REGISTAR");
        btnRegister.setMinWidth(120);
        btnRegister.getStyleClass().add("button");

        btnExit = new Button("SAIR");
        btnExit.setMinWidth(120);
        btnExit.getStyleClass().add("button");

        VBox vBox = new VBox(lblTitle, btnLogin, btnRegister, btnExit);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        VBox.setMargin(btnLogin, new Insets(25, 0, 0, 0)); // Set top margin for the button

        this.setCenter(vBox);
    }


    private void registerHandlers() {
        RootPane.addPropertyChangeListener("SHOWMENU", evt -> { update(); });
        RootPane.addPropertyChangeListener("SHOWLOGIN", evt -> { update(); });
        RootPane.addPropertyChangeListener("SHOWREGISTER", evt -> { update(); });

        btnLogin.setOnAction( event -> {
            RootPane.setShowMainMenu(false);
            RootPane.setShowLogin(true);
        });

        btnRegister.setOnAction( event -> {
            RootPane.setShowMainMenu(false);
            RootPane.setShowRegister(true);
        });

        btnExit.setOnAction( event -> {

        });


        PopUpCreator.exitAlert(btnExit);
    }


    private void update(){
        if(RootPane.showMainMenu){
            this.setVisible(true);
        }else{
            this.setVisible(false);
        }
    }


}
