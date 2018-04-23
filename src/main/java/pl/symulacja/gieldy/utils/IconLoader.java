package pl.symulacja.gieldy.utils;

import javafx.scene.image.Image;

/**
 * Klasa ładująca ikonę do programu
 * @author Paweł
 */
class IconLoader {
    /**
     * Wczytuje ikonę
     * @param fxmlPath Scieżka do pliku z obrazem
     * @return ikona
     */
    static Image Iconpath(String fxmlPath){
        Image icon = new Image(IconLoader.class.getResource(fxmlPath).toString());

        try {
            return icon;
        }
        catch (Exception e){
            DialogsUtils.errorDialog(e.getMessage());
        }
        return null;
    }
}
