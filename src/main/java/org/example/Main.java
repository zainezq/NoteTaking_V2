package org.example;


import javafx.application.Application;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getClassLoader().getResource("main-window-view.fxml"));

            Scene scene = new Scene(root, 1200, 800);

            //File style = new File("Z:\\Prod.Work\\Projects2023\\NoteTaking_V4\\src\\main\\resources\\darkmode.css");
            //scene.getStylesheets().add(style.toURI().toURL().toExternalForm());
            stage.setTitle("Note Taking");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
