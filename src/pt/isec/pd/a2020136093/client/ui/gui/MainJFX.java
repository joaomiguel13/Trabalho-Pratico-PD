package pt.isec.pd.a2020136093.client.ui.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pd.a2020136093.client.ClientMain;
import pt.isec.pd.a2020136093.client.communication.ManageConnections;

public class MainJFX extends Application {

    ManageConnections mc;

    @Override
    public void init() throws Exception{
        super.init();
    }

    @Override
    public void start(Stage stage){
        initGUI(new Stage(), "Presence Register");
    }

    public void initGUI(Stage stage,String title){
        this.mc = ClientMain.mc;

        RootPane rootPane = new RootPane(mc);
        Scene scene = new Scene(rootPane,900,725);
        //stage.getIcons().add(ImageManager.getImage("pacman-icon.png"));
        stage.setScene(scene);
        stage.setTitle(title);

        stage.setWidth(1000);
        stage.setHeight(700);
        //stage.setFullScreen(true);

        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
