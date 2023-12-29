package org.example;

import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.*;
import java.util.Optional;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class loadNote {
    public static void Load(ListView<String> previewList, HTMLEditor editor, File loadedFile){

            // Open a file chooser dialog to select a file to load
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File selectedFile = fileChooser.showOpenDialog(editor.getScene().getWindow());
            int selectedIndex = previewList.getSelectionModel().getSelectedIndex();

            if (selectedFile != null) {
                // Read the content of the selected file and set it in the editor
                loadedFile = selectedFile;

                System.out.println("Reading and loading file...");

                try {
                    // Implement a method to read file content
                    String fileContent = readFile(selectedFile);

                    if (!fileContent.isEmpty()) {
                        String toHtml = readFile(selectedFile);
                        String HTMLText = convertTextToHtml(toHtml);

                        // Split the content into the first line (title) and the rest (content)
                        //String[] lines = fileContent.split("\n");
                        // Extract the first line as the title
                        //String title = lines[0];
                        TextInputDialog dialog = new TextInputDialog("Unnamed note");
                        dialog.setTitle("Name the Note");
                        dialog.setHeaderText("Enter a title for the note:");
                        dialog.setContentText("Note Title:");

                        Optional<String> result = dialog.showAndWait();
                        result.ifPresent(newTitle -> {
                            previewList.getItems().add(newTitle);
                            newNote.saveNoteToDatabase(newTitle, HTMLText, DBconnection.url, DBconnection.user, DBconnection.password);
                            // Select the newly added note in the list
                            previewList.getSelectionModel().select(newTitle);

                        });

                        // Add the title to the previewList
                        //previewList.getItems().add(title);

                        // Enable the editor and load the content
                        System.out.println(HTMLText);
                        editor.setDisable(false);
                        editor.setHtmlText(HTMLText);
                    }

                } catch (IOException e) {
                    getAlert showAlert = new getAlert();
                    showAlert.showAlert("Error", "Error reading the file", Alert.AlertType.ERROR);
                }
            }
            }

    private static String readFile(File file) throws IOException {
        // Implement file reading logic here
        String content = ""; // Initialize an empty string

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                content += scanner.nextLine() + "\n";
            }
        }

        return content;
    }
    private static String convertTextToHtml(String plainText) {
        // Replace newline characters with HTML line breaks
        String htmlContent = plainText.replace("\n", "<br>");

        // Wrap the text in HTML body and paragraph tags
        return "<html><body><p>" + htmlContent + "</p></body></html>";
    }
    public static String convertHtmlToPlainText(String html) {
        Document document = Jsoup.parse(html);
        return document.text();
    }


}

