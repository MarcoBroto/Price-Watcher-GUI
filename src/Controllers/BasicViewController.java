package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.*;
import java.net.URI;
import java.util.LinkedList;

import Models.*;

public class BasicViewController {

    @FXML private Button refreshButton;
    @FXML private Button openBrowserButton;
    @FXML private TextField itemNameTextField;
    @FXML private TextField itemURLTextField;
    @FXML private TextField itemPriceTextField;
    @FXML private TextField priceChangeTextField;
    @FXML private TextField itemsAddedTextField;

    private PriceFinder finder = new PriceFinder();
    private LinkedList<Item> addedItems = new LinkedList<>();
    private String addedItemsString = "";

    public void initialize() {
        refreshButton.setOnMouseClicked(e -> {
            String itemName = itemNameTextField.getText();
            if (itemName.isEmpty()) return;
            String itemURL = itemURLTextField.getText();
            for (Item i : addedItems) {
                if (i.getItemName().equals(itemName)) {
                    itemURLTextField.setText(i.getItemURL());
                    finder.simulatePriceChange(i);
                    itemPriceTextField.setText(String.format("$%.2f", i.getCurrentPrice()));
                    priceChangeTextField.setText(String.format("%s%.2f", (i.getPriceChange() < 0) ? "" : "+", i.getPriceChange()));
                    return;
                }
            }
            Item newItem = new Item(itemName, 0, itemURL);
            finder.setRandomPrice(newItem);
            addedItems.add(newItem);
            itemPriceTextField.setText(String.format("$%.2f", newItem.getCurrentPrice()));
            priceChangeTextField.setText(String.format("%s%.2f", (newItem.getPriceChange() < 0) ? "" : "+", newItem.getPriceChange()));
            addedItemsString += itemName + ", ";
            itemsAddedTextField.setText(addedItemsString);
        });

        openBrowserButton.setOnMouseClicked(e -> {
            openPageInBrowser(itemURLTextField.getText());
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
}
