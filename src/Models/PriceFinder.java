/**
 * PriceFinder.java
 * @author Marco Soto
 */

package Models;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Random;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class PriceFinder {

    private static final String API_KEY_WALMART = "4pw9e246y8b2b8ewa3pq4w79";

    /**
     *
     * @param item
     */
    public void setRandomPrice(Item item) {
        java.util.Random r = new java.util.Random();
        item.updatePrice(Math.random() * Math.pow(10,r.nextInt(4)));
    }


    /**
     *
     * @param item
     */
    public void simulatePriceChange(Item item) {
        Random rand = new Random();
        double priceChange, newPrice;
        int sign = rand.nextInt(2);
        priceChange = (sign == 0) ? rand.nextDouble() : -rand.nextDouble();
        newPrice = item.getCurrentPrice() + priceChange;
        item.updatePrice((newPrice > 0) ? newPrice : 0);
    }


    /**
     *
     * @param item
     */
    public void updatePrice_walmartAPI(Item item) throws Exception {
        System.out.println("Querying: " + item);
        JSONObject jsonResponse = new JSONObject(queryWalmartAPI(item));
        //System.out.println(jsonResponse);
        if (jsonResponse.getInt("totalResults") < 1) return;
        JSONArray returnedItems = jsonResponse.getJSONArray("items");
        JSONObject firstItem =  (JSONObject) returnedItems.get(0);
        //System.out.println(firstItem);
        double newPrice = firstItem.getDouble("salePrice"); // Price taken from first item
        String newURL = firstItem.getString("productUrl");
        item.updatePrice(newPrice); // Update item price with new price found
        item.setItemURL(newURL);
    }


    /**
     *
     * @param item
     * @return
     * @throws Exception
     */
    private String queryWalmartAPI(Item item) throws Exception {
        String jsonResponse; // Stores the json string response from api request
        String stringURI = newURIString_walmartAPI(item.getItemName()); // Construct URI

        /* Query API */
        URL queryURL = new URL(stringURI);
        queryURL.openConnection();
        HttpURLConnection conn = (HttpURLConnection) queryURL.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder strBuff = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            strBuff.append(inputLine);
        in.close();
        conn.disconnect();
        jsonResponse = strBuff.toString();
        return jsonResponse;
    }


    /**
     *
     * @param itemName
     * @return
     */
    private static String newURIString_walmartAPI(String itemName) {
        // TODO: remove special characters from string
        String formattedItemName = itemName.replace(" ", "%20"); // Replace spaces to conform to URL format
        //System.out.println("Unformatted item: " + itemName);
        //System.out.println("Formatted item: " + formattedItemName);
        return String.format("http://api.walmartlabs.com/v1/search?query=%s&format=json&apiKey=%s",
                formattedItemName, API_KEY_WALMART);
    }
}
