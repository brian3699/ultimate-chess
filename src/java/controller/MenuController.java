package controller;

import view.components.ChoiceView;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class responsible for creating the menu scene and communication user's choices to the backend.
 *
 * @author Young Jun
 */
public class MenuController {
    private static final String LANGUAGE_CHOICE_DIRECTORY = "view.resources.language.LanguageOptions";
    private static final String OPTION_TITLE_ENGLISH = "Please Select a Language ";
    private final String defaultLanguage;
    private List<String> languageList;
    private String gameLanguage;

    /**
     * Constructor for MenuController
     */
    public MenuController() {
        languageList = new ArrayList<>();
        populateLanguageOptions();
        defaultLanguage = languageList.get(0);
    }


    // read from LANGUAGE_CHOICE_DIRECTORY and add elements to arraylist languageList
    private void populateLanguageOptions() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(LANGUAGE_CHOICE_DIRECTORY);
        Enumeration<String> enumeration = resourceBundle.getKeys();
        while (enumeration.hasMoreElements()) {
            languageList.add(enumeration.nextElement());
        }
    }

    /**
     * Creates a popup in the frontend for the user to choose a language to play the game.
     *
     * @return user's language choice
     */
    public String getUserLanguage() {
        ChoiceView choiceView = new ChoiceView();
        // convert list to array
        String[] languageOptions = new String[languageList.size()];
        languageList.toArray(languageOptions);
        // create choice view and get user's language choice
        gameLanguage = choiceView.getUserLanguage(defaultLanguage, languageOptions, OPTION_TITLE_ENGLISH);
        return gameLanguage;
    }
}
