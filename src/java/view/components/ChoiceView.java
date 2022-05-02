package view.components;

import javafx.scene.control.ChoiceDialog;

public class ChoiceView {

    /**
     * Generates a choice dialog for the user to choose a language
     * @return language resource bundle's path
     */
    public String getUserLanguage(String defaultChoice, String[] optionsArray, String title){

        String userLanguage = makeChoiceDialog(defaultChoice, optionsArray, title);
        return userLanguage;
    }


    /**
     * Creates a choiceDialog and returns
     */
    private String makeChoiceDialog(String defaultOption, String[] optionList,
                                                 String title) {
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(defaultOption);
        choiceDialog.setContentText(title);
        for (String option : optionList) choiceDialog.getItems().add(option);
        choiceDialog.showAndWait();
        return choiceDialog.getSelectedItem();
    }
}
