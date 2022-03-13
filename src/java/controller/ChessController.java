package controller;

import java.util.ResourceBundle;

public class ChessController {
    private static final String LANGUAGE_RESOURCE_DIRECTORY = "view.resources.language.";
    private final ResourceBundle languageResource;


    public ChessController(String gameLanguage){
        languageResource = makeResourceBundle(LANGUAGE_RESOURCE_DIRECTORY + gameLanguage);
    }

    // make ResourceBundle and return
    private ResourceBundle makeResourceBundle(String path) {
        try {
            return ResourceBundle.getBundle(path);
        } catch (Exception e) {
            return null;
        }
    }
}
