package org.example;

import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.web.HTMLEditor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class deleteNote {
    public void deleteN(HTMLEditor editor, ListView<String> previewList) {
        getAlert alert = new getAlert();
        String title = previewList.getSelectionModel().getSelectedItem();

        if (title != null && !title.isEmpty()) {
            deleteNoteInDB(title);
            int selectedIndex = previewList.getSelectionModel().getSelectedIndex();
            previewList.getItems().remove(title);

            // Select the next item if available, or the previous one if not
            int itemCount = previewList.getItems().size();
            if (itemCount > 0) {
                int newIndex = (selectedIndex < itemCount) ? selectedIndex : itemCount - 1;
                previewList.getSelectionModel().select(newIndex);
            } else {
                // If no items are left, clear the editor
                editor.setHtmlText("<html><head></head><body contenteditable=\"true\"></body></html>");
                editor.setDisable(true);
            }

            alert.showAlert("Note Deleted", "The note selected was successfully deleted.", Alert.AlertType.INFORMATION);
        } else {
            alert.showAlert("Error", "Please select a note to delete.", Alert.AlertType.ERROR);
        }
    }


    private void deleteNoteInDB(String title) {
        String deleteQuery = "DELETE FROM notes WHERE title = ?";
        DBconnection conn = new DBconnection();
        getAlert alert = new getAlert();
        try (Connection connection = DriverManager.getConnection(conn.url, conn.user, conn.password);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, title);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // Deletion successful
                System.out.println("Note deleted successfully: " + title);


            } else {
                // Note with the given title not found
                System.out.println("Note not found: " + title);
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
            alert.showAlert("Error", "Database error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
}}
