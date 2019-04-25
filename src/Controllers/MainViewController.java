/**
 * MainViewController.java
 * @author Marco Soto
 */

package Controllers;

import Models.DropdownList;
import Models.Item;
import Models.PriceFinder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

public class MainViewController {

    /* View Interface Elements */
    @FXML private Button refreshButton;
    @FXML private Button openBrowserButton;
    @FXML private TextField itemNameTextField;
    @FXML private TextField itemURLTextField;
    @FXML private Label itemPriceLabel;
    @FXML private Label priceChangeLabel;
    @FXML private Label errorMsgLabel;
    @FXML private ChoiceBox<String> prevItemsDropdown = new ChoiceBox<>(new DropdownList<String>());
    @FXML private RadioButton queryOptionRadio;
    @FXML private RadioButton simulateOptionRadio;

    /* Required View Element Structures */
    private final ToggleGroup radioToggleGroup = new ToggleGroup(); // Stores radio toggle options
    private boolean queryOption = true; // Query api for price if true, simulate price change if false

    /* Required Controller Structures */
    private HashMap<String, Item> itemList = new HashMap<>(); // Stores searched item data
    private PriceFinder finder = new PriceFinder(); // Price Finder that scrapes web and updates item prices
    private static MediaPlayer player; // Used to play sounds

    /*  */
    private Stage popupStage = new Stage();


    /**
     * Initialize view controller and its corresponding views.
     * Attaches event listeners to view controls and sets their initial values.
     */
    public void initialize() throws Exception {
        System.out.println("Initializing");

        popupStage.setResizable(false);
        Scene aboutPopupScene = new Scene(FXMLLoader.load(getClass().getResource("../Views/AboutView.fxml")));
        Scene editPopupScene = new Scene(FXMLLoader.load(getClass().getResource("../Views/EditView.fxml")));
        Scene searchPopupScene = new Scene(FXMLLoader.load(getClass().getResource("../Views/SearchView.fxml")));
        displayPopup(editPopupScene);

        /* Set Resource Locations */
        URL soundURL = getClass().getResource("/assets/SiliconValley_bitcoinAlert.wav");

        /* Set Initial View Element and Structure States */
        prevItemsDropdown.getItems().add(""); // Add blank row to dropdown list
        prevItemsDropdown.setValue("");
        queryOptionRadio.fire(); // Activate first radio option
        queryOptionRadio.setToggleGroup(radioToggleGroup);
        simulateOptionRadio.setToggleGroup(radioToggleGroup);
        queryOptionRadio.setUserData("query");
        simulateOptionRadio.setUserData("simulate");


        /* Radio Buttons Change Listener */
        radioToggleGroup.selectedToggleProperty().addListener(e -> {
            if (radioToggleGroup.getSelectedToggle() == null) return;
            if (radioToggleGroup.getSelectedToggle().getUserData() == "query") {
                queryOptionRadio.setSelected(true);
                simulateOptionRadio.setSelected(false);
                queryOption = true;
            }
            else {
                simulateOptionRadio.setSelected(true);
                queryOptionRadio.setSelected(false);
                queryOption = false;
            }
        });


        /* Open Browser Button Pressed Listener */
        openBrowserButton.setOnMouseClicked(e -> {
            errorMsgLabel.setVisible(false); // Hide Error Message

            String itemName = itemNameTextField.getText();
            Item item = itemList.getOrDefault(itemName, null);
            if (item != null && item.getItemURL() != null)
                openPageInBrowser(item.getItemURL());
            else {
                errorMsgLabel.setText("Invalid URL Error");
                errorMsgLabel.setVisible(true);
            }
        });


        /* Refresh Button Pressed Listener */
        refreshButton.setOnMouseClicked(e -> {
            /* Set initial values after click */
            errorMsgLabel.setVisible(false); // Hide Error Message
            prevItemsDropdown.setValue(""); // Clear dropdown selection

            /* Get list item object associated with item name */
            Item currentItem;
            String itemName = itemNameTextField.getText();
            if (itemName.isEmpty()) { // Invalid Item query, reset and try again
                errorMsgLabel.setText("Item Search Error");
                errorMsgLabel.setVisible(true);
                itemPriceLabel.setText("$0.00");
                priceChangeLabel.setText("$0.00");
                itemURLTextField.setText("");
                priceChangeLabel.setStyle("-fx-background-color: white; -fx-text-fill: black");
                prevItemsDropdown.setValue("");
                return;
            }
            else if (!itemList.containsKey(itemName)) {
                currentItem = new Item(itemNameTextField.getText(), 0, null);
                itemList.put(itemName, currentItem);
                prevItemsDropdown.getItems().add(itemName);
            }
            else currentItem = itemList.get(itemName);

            /* Update Item Price */
            if (currentItem.getCurrentPrice() == 0 || queryOption)
                try {
                    finder.updatePrice_walmartAPI(currentItem); // Get price from Walmart api
                }
                catch (Exception ex) { // API connection error
                    System.out.println(ex);
                    finder.setRandomPrice(currentItem);
                }
            else
                finder.simulatePriceChange(currentItem); // Simulate price

            /* Update Interface, Extract to function module */
            double priceChange = currentItem.getPriceChange();
            itemPriceLabel.setText(String.format("$%.2f", currentItem.getCurrentPrice()));
            if (priceChange < 0) // Format price change label with negative change
                priceChangeLabel.setText(String.format("-$%.2f", -priceChange));
            else // Format label with positive change
                priceChangeLabel.setText(String.format("+$%.2f", priceChange));
            itemURLTextField.setText((currentItem.getItemURL() == null) ? "(none)" : currentItem.getItemURL());
            /* Update Percent Change Box Style, Extract to function module */
            if (priceChange < 0) {
                final String dir = System.getProperty("user.dir");
                priceChangeLabel.setStyle("-fx-background-color: rgb(250,70,98); -fx-text-fill: white");
                // ****VOLUME WARNING!!!!**** Uncomment next line for sound.
                //playSound(soundURL.toString());
            }
            else if (priceChange > 0) priceChangeLabel.setStyle("-fx-background-color: rgb(47,213,102); -fx-text-fill: white;");
            else priceChangeLabel.setStyle("-fx-background-color: white; -fx-text-fill: black");
        });


        /* Dropdown Change Listener */
        prevItemsDropdown.valueProperty().addListener(e -> {
            String val = prevItemsDropdown.getValue();
            if (!val.isEmpty()) {
                itemNameTextField.setText(val);
                Item selectedItem = itemList.get(val);
                itemPriceLabel.setText(String.format("$%.2f", selectedItem.getCurrentPrice()));
                priceChangeLabel.setText("0.00%");
                itemURLTextField.setText(selectedItem.getItemURL());
                priceChangeLabel.setStyle("-fx-background-color: white; -fx-text-fill: black");
            }
        });
    }


    /**
     *
     * @param urlString
     */
    private static void openPageInBrowser(String urlString) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(urlString));
            }
        }
        catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }


    /**
     *
     * @param file_string
     */
    private static void playSound(String file_string) {
        try {
            Media media = new Media(file_string);
            player = new MediaPlayer(media);
            player.play();
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void displayPopup(Scene scene) {
        popupStage.setScene(scene);
        popupStage.show();
    }

    private void dismissPopup() { popupStage.hide(); }
}
