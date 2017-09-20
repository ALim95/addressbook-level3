package seedu.addressbook.ui;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seedu.addressbook.commands.AddCommand;
import seedu.addressbook.commands.CommandResult;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.*;
import seedu.addressbook.data.tag.Tag;
import seedu.addressbook.data.tag.UniqueTagList;
import seedu.addressbook.logic.Logic;

import java.util.ArrayList;

public class PersonAddDialogController {

    public static final String MESSAGE_USAGE = "Name, address and tag must be alphanumeric." + " Phone must be numerical" +
            " Email must be in the following format example@gmail.com";

    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField tagField;
    @FXML
    private CheckBox addressPrivate;
    @FXML
    private CheckBox phonePrivate;
    @FXML
    private CheckBox emailPrivate;

    private Person personToAdd;
    private Stage dialogStage;
    private boolean okClicked = false;
    private Logic logic;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param logic
     */
    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() throws Exception {
        try {
            personToAdd = getPerson();
            okClicked = true;
            AddCommand command = new AddCommand(personToAdd);
            CommandResult result = logic.execute(command);
            if (result.feedbackToUser.equals(AddCommand.MESSAGE_DUPLICATE_PERSON)) {
                duplicateAddAlert();
            } else {
                addSuccessAlert();
                dialogStage.close();
            }
        } catch (IllegalValueException e) {
            addFailedAlert();
        }
    }

    private void duplicateAddAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(dialogStage);
        alert.setTitle("Error adding");
        alert.setHeaderText("Duplicate person in address book");
        alert.showAndWait();
    }

    private void addFailedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(dialogStage);
        alert.setTitle("Error adding");
        alert.setHeaderText("Fields are not filled up / invalid input");
        alert.setContentText(MESSAGE_USAGE);
        alert.showAndWait();
    }

    private void addSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Add a person");
        alert.setHeaderText("Person has been successfully added");
        alert.setContentText(personToAdd.toString());
        alert.showAndWait();
    }

    private Person getPerson() throws IllegalValueException {
        Name newName = new Name(nameField.getText());
        Phone newPhone = new Phone(phoneField.getText(), phonePrivate.isSelected());
        Email newEmail = new Email(emailField.getText(), emailPrivate.isSelected());
        Address newAddress = new Address(addressField.getText(), addressPrivate.isSelected());
        UniqueTagList tagList = getTags();
        return new Person(newName, newPhone, newEmail, newAddress, tagList);
    }

    private UniqueTagList getTags() throws IllegalValueException {
        if (tagField.getText().isEmpty()) {
            return new UniqueTagList();
        }
        String[] stringTags = tagField.getText().split(",");
        ArrayList<Tag> newTagList1 = new ArrayList();
        for (String tags : stringTags) {
            newTagList1.add(new Tag(tags));
        }
        ArrayList<Tag> newTagList = newTagList1;
        return new UniqueTagList(newTagList);
    }


    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
