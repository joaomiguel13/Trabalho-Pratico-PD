package pt.isec.pd.a2020136093.client.ui.gui.RESOURCES;

import javafx.scene.Parent;

public class CSSManager {
    private CSSManager(){}

    public static void applyCSS(Parent parent, String filename) {
        var url = CSSManager.class.getResource("styles/" + filename);
        if (url == null)
            return;

        String fileCSS = url.toExternalForm();
        parent.getStylesheets().add(fileCSS);
    }
}