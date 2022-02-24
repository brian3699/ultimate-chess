package controller;

import view.LanguageChoiceView;

import java.util.ResourceBundle;

public class Controller {
    ResourceBundle languageResource;


    public Controller(){
        LanguageChoiceView languageChoiceView = new LanguageChoiceView();
        languageResource = languageChoiceView.getUserLanguageBundle();
    }

}
