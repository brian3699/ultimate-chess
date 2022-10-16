package view.components;

import javafx.scene.control.ChoiceDialog;

/**
 * A class that creates a popup message that displays options that a user can choose
 *
 * @author Young Jun
 */
public class ChoiceView implements ChoiceViewInterface{

    @Override
    //Generates a choice dialog for the user to choose from the list of options
    public String makeChoiceDialog(String defaultChoice, String[] optionsArray, String title) {
        // create a new choice dialog and set the default option
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(defaultChoice);
        // set the title of the dialog
        choiceDialog.setContentText(title);
        // add all options to the choice dialog
        for (String option : optionsArray) choiceDialog.getItems().add(option);
        // show and wait until the user makes a choice
        choiceDialog.showAndWait();
        // return user's choice
        return choiceDialog.getSelectedItem();
    }
}
