package org.example;

import javafx.scene.control.Alert;

public class getAlert {
    public static void showAlert(String title, String content, Alert.AlertType error) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

