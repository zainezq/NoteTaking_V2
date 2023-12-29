package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;

import java.io.File;

public class Controller {
    private File loadedFile;


    @FXML
    public SplitPane yourSplitPane;
    @FXML
    private HTMLEditor editor;
    @FXML
    private ListView<String> previewList;

    public void initialize() {
        // Bind the dividerPositions property to the Scene width property
        yourSplitPane.setDividerPositions(0.2); // Initial position

        // Add a listener to dynamically adjust the position on window resize
        yourSplitPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                yourSplitPane.setDividerPositions(0.2);
                newScene.widthProperty().addListener((obs2, oldWidth, newWidth) -> {
                    yourSplitPane.setDividerPositions(0.2);
                });
            }
        });
        System.out.println(previewList.getSelectionModel().getSelectedItem());
        // Set up a listener to handle note selection events in the previewList
        previewList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> handleNoteSelection(newValue, oldValue));

        // Load notes from the database and populate the previewList
        DBLoad noteLoad = new DBLoad();
        noteLoad.loadNotesTitlesFromDatabase(previewList);

        // Initialize the editor
        editor.setDisable(true);
        editor.setHtmlText("Please click 'New Note' or select an existing note to get started...");
    }

    public void handleNoteSelection(String newTitle, String oldTitle) {
        if (oldTitle != null) {
            // Check if the current editor content has changed
            String currentContent = editor.getHtmlText();

            if (saveNote.checkChange(editor, oldTitle)) {
                // Save the current editor content before switching to another note
                System.out.println(currentContent);
                saveNote.updateNoteWhenSwitching(oldTitle, currentContent);
            }
        }

        if (newTitle != null) {
            // Load the content of the selected note from the database
            editor.setDisable(false);
            DBLoad.loadNoteContentFromDatabase(newTitle, editor);
        }
    }




    @FXML
    private void handleNew(){
        newNote newNote = new newNote();
        newNote.handleNew(editor, previewList);
    }

    @FXML
    private void handleAddToNote() {
        newNote newNote = new newNote();
        newNote.handleAddToNote(editor, previewList);
    }

    @FXML
    public void handleDelete() {
        deleteNote deleteNote = new deleteNote();
        deleteNote.deleteN(editor, previewList);
    }


    @FXML
    public void handleLoad() {
        loadNote.Load(previewList, editor, loadedFile);
    }

    @FXML
    private void handleSave() {
        saveNote saveNote = new saveNote();
        saveNote.handleSave(editor, previewList);
    }
    @FXML
    public void handleSaveAs() {
            saveNote.saveAs(editor, previewList);
    }
    @FXML
    public void handleExportSelected(ActionEvent actionEvent) {
    }
    @FXML
    public void handleExportAll(ActionEvent actionEvent) {
    }
    @FXML
    public void handleTutorial(ActionEvent actionEvent) {
    }
    @FXML
    public void handleAbout() {
    }
    @FXML
    public void handleRenameNote() {
        renameNote renameNote = new renameNote();
        renameNote.handleRenameNote(previewList);
    }

    @FXML
    public void handleSetting() {

    }


}


