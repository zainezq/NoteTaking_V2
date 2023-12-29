package org.example;

import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class newNote {
    public void handleNew(HTMLEditor editor, ListView<String> previewList) {
        // Check if there's unsaved content in the editor
        if (!isContentUnsaved(editor)) {
            // If there's unsaved content, ask the user for confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unsaved Changes");
            alert.setHeaderText("You have unsaved changes. Do you want to save them?");
            alert.setContentText("Choose your option.");

            ButtonType saveButton = new ButtonType("Save");
            ButtonType discardButton = new ButtonType("Discard");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(saveButton, discardButton, cancelButton);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent()) {
                if (result.get() == saveButton) {
                    // Save the content
                    //handleSave();
                } else if (result.get() == discardButton) {
                    // Discard unsaved changes and clear the editor
                    editor.setHtmlText("<html><head></head><body contenteditable=\"true\"></body></html>");
                    previewList.getItems().clear();
                }
                // If the user chooses Cancel, do nothing
            }
        } else {
            handleAddToNote(editor, previewList);
        }
    }
    public boolean isContentUnsaved(HTMLEditor editor) {
        // Check if the editor contains any content
        String currentContent = editor.getHtmlText();
        return !currentContent.isEmpty();
    }

    public void handleAddToNote(HTMLEditor editor, ListView<String> previewList) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Note");
        dialog.setHeaderText("Enter a title for the new note:");
        dialog.setContentText("Title:");

        Optional<String> result = dialog.showAndWait();
        DBconnection connection = new DBconnection();
        result.ifPresent(title -> {
            if(!previewList.getItems().contains(title)){
                previewList.getItems().add(title);
                saveNoteToDatabase(title, "", connection.url, connection.user, connection.password);
                previewList.getSelectionModel().select(title);
                editor.setDisable(false);
                editor.setHtmlText("");
            }
            else {
                getAlert showAlert = new getAlert();
                showAlert.showAlert("Error", "A note with the same title already exists.", Alert.AlertType.ERROR);
            }
        });
    }

    public static void saveNoteToDatabase(String title, String content, String url, String user, String password) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO notes (title, content) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, content);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            getAlert showAlert = new getAlert();
            showAlert.showAlert("Error", "Failed to save the note to the database.", Alert.AlertType.ERROR);
        }
    }
}
