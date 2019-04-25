package Models;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Utilities {

    /**
     *
     * @param urlString
     */
    public static boolean openPageInBrowser(String urlString) throws URISyntaxException, IOException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(urlString));
            return true;
        }
        else {
            System.out.println("Error: Desktop browse action not supported.");
            return false;
        }
    }
}
