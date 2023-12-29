package org.example;

import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Objects;

public class saveNote {
    public void handleSave(HTMLEditor editor, ListView<String> previewList) {
        String title = previewList.getSelectionModel().getSelectedItem(); // Implement a method to get the title of the selected note
        String content = editor.getHtmlText();  // Get the content from the editor

        if (title != null && !title.isEmpty()) {
            updateNoteInDatabase(title, content);
        } else {
            getAlert alert = new getAlert();
            alert.showAlert("Error", "Please select a note to save.", Alert.AlertType.ERROR);
        }
    }

    public static void updateNoteInDatabase(String title, String content) {
        String updateQuery = "UPDATE notes SET content = ? WHERE title = ?";

        try (Connection connection = DriverManager.getConnection(DBconnection.url, DBconnection.user, DBconnection.password);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, content);
            preparedStatement.setString(2, title);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                getAlert.showAlert("Success", "Note updated successfully.", Alert.AlertType.INFORMATION);
            } else {
                getAlert.showAlert("Error", "Failed to update the note.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            getAlert.showAlert("Error", "Database error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public static boolean checkChange(HTMLEditor editor, String title) {
        String currentContent = editor.getHtmlText();
        String storedContent = DBLoad.returnNoteContentFromDB(title, editor);
        // Compare the current content with the stored content
        return !Objects.equals(currentContent, storedContent);
    }
    public static void updateNoteWhenSwitching(String title, String content) {
        String updateQuery = "UPDATE notes SET content = ? WHERE title = ?";

        try (Connection connection = DriverManager.getConnection(DBconnection.url, DBconnection.user, DBconnection.password);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, content);
            preparedStatement.setString(2, title);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("success");
            } else {
                System.out.println("failure");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            getAlert.showAlert("Error", "Database error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public static void saveAs(HTMLEditor editor, ListView<String> previewList){
        String title = previewList.getSelectionModel().getSelectedItem();
        String content = DBLoad.returnNoteContentFromDB(title, editor);

        if (title != null && !title.isEmpty()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File saveFile = fileChooser.showSaveDialog(previewList.getScene().getWindow());

            if (saveFile != null) {
                try (FileWriter fileWriter = new FileWriter(saveFile)) {
                    String parsedContent = loadNote.convertHtmlToPlainText(content);
                    fileWriter.write(parsedContent);
                }  catch (IOException e) {
                    getAlert.showAlert("Error", "Error saving notes as TXT", Alert.AlertType.ERROR);
                }
            }
        }else {
            getAlert.showAlert("Error", "Please select a note to save.", Alert.AlertType.ERROR);
        }

    }

}
