package view;

import javafx.scene.control.ChoiceDialog;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

public class LanguageChoiceView {
    private static final String LANGUAGE_RESOURCE_DIRECTORY = "view.resources.language.";
    private static final String LANGUAGE_CHOICE_DIRECTORY = "view.resources.language.LanguageOptions";
    private static final String OPTION_TITLE_ENGLISH = "Please Select a Language ";
    private final String defaultLanguage;
    private List<String> languageList;


    public LanguageChoiceView(){
        languageList = new ArrayList<String>();
        populateLanguageOptions();
        defaultLanguage = languageList.get(0);
    }

    /**
     * Generates a choice dialog for the user to choose a language
     * @return ResourceBundle of the language the user has chosen
     */
    public ResourceBundle getUserLanguageBundle(){
        String userLanguage = makeChoiceDialog(defaultLanguage, languageList, OPTION_TITLE_ENGLISH);
        return makeResourceBundle(userLanguage);
    }

    // read from LANGUAGE_CHOICE_DIRECTORY and add elements to languageList arraylist
    private void populateLanguageOptions() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(LANGUAGE_CHOICE_DIRECTORY);
        Enumeration<String> enumeration = resourceBundle.getKeys();
        while (enumeration.hasMoreElements()) {
            languageList.add(enumeration.nextElement());
        }
    }

    /**
     * Creates a choiceDialog and returns
     */
    private String makeChoiceDialog(String defaultOption, List<String> optionList,
                                                 String title) {
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(defaultOption);
        choiceDialog.setContentText(title);
        for (String option : optionList) {
            choiceDialog.getItems().add(option);
        }
        choiceDialog.showAndWait();
        return choiceDialog.getSelectedItem();
    }



    // make ResourceBundle and return
    private ResourceBundle makeResourceBundle(String lang) {
        try {
            return ResourceBundle.getBundle(LANGUAGE_RESOURCE_DIRECTORY + lang);
        } catch (Exception e) {
            return ResourceBundle.getBundle(LANGUAGE_RESOURCE_DIRECTORY + defaultLanguage);
        }
    }



}
