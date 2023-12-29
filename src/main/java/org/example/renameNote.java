package org.example;

import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class renameNote {

    public void handleRenameNote(ListView<String> previewList) {
        String currentTitle = previewList.getSelectionModel().getSelectedItem();
        String updateQuery = "UPDATE notes SET title = ? WHERE title = ?";
        DBconnection conn = new DBconnection();
        getAlert alert = new getAlert();
        int selectedIndex = previewList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // Prompt the user for a new title
            TextInputDialog dialog = new TextInputDialog(previewList.getItems().get(selectedIndex));
            dialog.setTitle("Rename Note");
            dialog.setHeaderText("Enter a new title for the note:");
            dialog.setContentText("New Title:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newTitle -> {
                // Update the title in the list and the DB
                String oldTitle = previewList.getItems().get(selectedIndex);
                previewList.getItems().set(selectedIndex, newTitle);
                if (currentTitle != null && !currentTitle.isEmpty()){
                    try (Connection connection = DriverManager.getConnection(conn.url, conn.user, conn.password);
                         PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                        preparedStatement.setString(1, newTitle);
                        preparedStatement.setString(2, oldTitle);

                        int affectedRows = preparedStatement.executeUpdate();

                        if (affectedRows > 0) {
                            System.out.println("Note title updated successfully.");
                        } else {
                            System.out.println("Note title update failed. Note with title '" + currentTitle + "' not found.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        alert.showAlert("Error", "Database error: " + e.getMessage(), Alert.AlertType.ERROR);
                    }


                }
            });
        }}
}
