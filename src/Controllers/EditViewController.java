package Controllers;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import Models.*;

public class EditViewController {

    // Layout Elements
    @FXML HBox priceHBox;

    // Interface Elements
    @FXML private Label titleLabel;
    @FXML private Label msgLabel;
    @FXML private TextField itemNameTextField;
    @FXML private TextField urlTextField;
    @FXML private TextField priceTextField;
    @FXML private ChoiceBox<String> groupDropdown = new ChoiceBox<>(new DropdownList<String>());
    @FXML private Button openBrowserButton;
    @FXML private Button submitButton;
    @FXML private Button cancelButton;
    private Button getPriceButton = new Button();

    private final String DEFAULT_ERROR_TEXT = "Error: Unable to Add Item";

    private boolean addMode = true; // Updates UI and functionality for new item entry, otherwise use item editing options
    private PriceFinder finder = new PriceFinder();
    private Item item = new Item("",0,""); // Main item being added or edited


    public void initialize() throws Exception {

        // Initialize optional 'Get Price' button
        ImageView btnImage = new ImageView("assets/img/baseline-desktop_mac-black-18/2x/baseline_desktop_mac_black_18dp.png");
        btnImage.setFitHeight(25);
        btnImage.setFitWidth(25);
        getPriceButton.setMaxWidth(80);
        //getPriceButton.setStyle("-fx-background-color: rgb(41,143,243); -fx-color: white;");
        getPriceButton.setTextFill(Color.WHITE);
        getPriceButton.setFocusTraversable(false);
        getPriceButton.setTooltip(new Tooltip("Search for and Update Item Price"));
        getPriceButton.setGraphic(btnImage);
        getPriceButton.setOnMouseClicked(e -> {

        });

        if (addMode) {
            priceHBox.getChildren().add(getPriceButton); // Add button to interface
        }
        else {
            ObservableList hBoxChildren = priceHBox.getChildren();
            if (hBoxChildren.size() > 1) // Remove optional button if it exists in the interface
                priceHBox.getChildren().remove(hBoxChildren.remove(hBoxChildren.size() - 1));
            submitButton.setText("Update Item");
        }

        // Initialize field values
        msgLabel.setText(DEFAULT_ERROR_TEXT);
        titleLabel.setText((addMode) ? "Add New Item" : String.format("Editing Item: %s", item.getItemName()));
        itemNameTextField.setText(item.getItemName());
        urlTextField.setText(item.getItemURL());
        priceTextField.setText(String.format("%.2f", item.getCurrentPrice()));
        //TODO: Set group dropdown value


        /*   UI Listeners   */
        openBrowserButton.setOnMouseClicked(e -> {
            String urlText = urlTextField.getText();
            if (urlText.isEmpty()) {
                displayError("Error: Invalid URL");
                return;
            }
            try {
                Utilities.openPageInBrowser(urlText);
            }
            catch (Exception ex) {
                displayError(ex.getMessage());
            }
        });

        submitButton.setOnMouseClicked(e -> {
            // Get user inputted values from interface
            String itemNameText = itemNameTextField.getText();
            String urlText = urlTextField.getText();
            String priceText = priceTextField.getText();
            String groupSelection = groupDropdown.getValue();

            //TODO: Handle errors
            //TODO: Add item to list
            if (addMode) { // Create new item
                item = new Item(itemNameText, Double.valueOf(priceText.substring(1,priceText.length())), urlText);
            }
            else { // Update existing item data
                item.setItemName(itemNameText);
                item.setItemURL(urlText);
                //TODO: update item price
            }

            dismissWindow();
        });

        cancelButton.setOnMouseClicked(e -> { dismissWindow(); });
    }

    private void displayError(String errorText) {
        msgLabel.setText(errorText);
        msgLabel.setVisible(true);
    }

    public void updateView() {  }

    private void dismissWindow() { System.out.println("Dismissing Edit Window"); }
}
