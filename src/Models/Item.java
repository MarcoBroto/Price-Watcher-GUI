/**
 * Item.java
 * @author Marco Soto
 */

package Models;

import java.util.Date;

public class Item {
    private String name;
    private double currentPrice;
    private String url;
    private double priceChange;
    private Date lastUpdate;

    public Item(String itemName, double price, String urlString) {
        this.name = itemName;
        this.currentPrice = price;
        this.priceChange = 0;
        this.url = urlString;
        this.lastUpdate = new Date();
    }

    public String getItemName() { return this.name; }

    public double getCurrentPrice() { return this.currentPrice; }

    public double getPriceChange() { return this.priceChange; }

    public Date lastUpdate() { return this.lastUpdate; }

    public String getItemURL() { return this.url; }

    public void setItemURL(String url) { this.url = url; }

    /**
     *
     * @param newPrice
     */
    void updatePrice(double newPrice) {
        this.priceChange = newPrice - this.currentPrice;
        this.currentPrice = newPrice;
        this.lastUpdate = new Date();
    }

    /**
     * Overrides toString() method to form a plaintext reqpresentation of the Item object.
     * @return  String description of this Item.
     */
    @Override
    public String toString() {
        return String.format("Name: %s\n URL: %s\n Price: %.2f\n Change: %.2f%%\n Price Last Checked: %s\n",
                this.name, this.url, this.currentPrice, this.priceChange, this.lastUpdate);
    }
}
