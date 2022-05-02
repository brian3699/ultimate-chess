package controller;

import view.components.ChoiceView;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

public class MainController {
    private static final String LANGUAGE_CHOICE_DIRECTORY = "view.resources.language.LanguageOptions";
    private static final String OPTION_TITLE_ENGLISH = "Please Select a Language ";
    private final String defaultLanguage;
    private List<String> languageList;
    private String gameLanguage;

    public MainController(){
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
