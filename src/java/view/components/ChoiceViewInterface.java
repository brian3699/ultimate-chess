package view.components;

public interface ChoiceViewInterface {

    /**
     * Generates a choice dialog for the user to choose from the list of options
     *
     * @return user's choice
     */
    public String makeChoiceDialog(String defaultChoice, String[] optionsArray, String title);
}
