package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.web.HTMLEditor;

import java.sql.*;

public class DBLoad {
    public void loadNotesTitlesFromDatabase(ListView<String> previewList) {
        String selectQuery = "SELECT title FROM notes";
        try (Connection connection = DriverManager.getConnection(DBconnection.url, DBconnection.user, DBconnection.password);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            // Explicitly create a new ObservableList to update previewList
            ObservableList<String> notesTitles = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                notesTitles.add(title);
            }

            // Set the items of previewList
            previewList.setItems(notesTitles);
            //System.out.println(previewList.getItems());
        } catch (SQLException e) {
            e.printStackTrace();
            getAlert alert = new getAlert();
            alert.showAlert("Error", "Database error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    // Method to load the content of a note from the database and set it in the editor
    public static void loadNoteContentFromDatabase(String title, HTMLEditor editor) {
        String selectContentQuery = "SELECT content FROM notes WHERE title = ?";
        try (Connection connection = DriverManager.getConnection(DBconnection.url, DBconnection.user, DBconnection.password);
             PreparedStatement preparedStatement = connection.prepareStatement(selectContentQuery)) {

            preparedStatement.setString(1, title);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String content = resultSet.getString("content");
                    editor.setHtmlText(content);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            getAlert alert = new getAlert();
            alert.showAlert("Error", "Database error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    public static String returnNoteContentFromDB(String title, HTMLEditor editor) {
        String selectContentQuery = "SELECT content FROM notes WHERE title = ?";
        String content = null;
        try (Connection connection = DriverManager.getConnection(DBconnection.url, DBconnection.user, DBconnection.password);
             PreparedStatement preparedStatement = connection.prepareStatement(selectContentQuery)) {

            preparedStatement.setString(1, title);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    content = resultSet.getString("content");
                    editor.setHtmlText(content);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            getAlert.showAlert("Error", "Database error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
        return content;
    }
}
