package pl.symulacja.gieldy.data.kraj;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pl.symulacja.gieldy.data.aktywo.Waluta;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Klasa imitująca kraj, zawiera nazwę oraz domyślną walutę
 * @author Paweł
 */
public class Kraj implements Serializable {
    transient private StringProperty nameProperty = new SimpleStringProperty();
    private Waluta waluta;


    // KONSTRUKTORY
    public Kraj(){
        waluta = null;
    }


    // GETTERY I SETTERY
    /**
     * Zwraca StringProperty zawierający nazwę Kraju
     * @return Nazwa kraju(StringProperty)
     */
    public StringProperty getNameProperty() {
        return nameProperty;
    }

    /**
     * Zwraca nazwę Kraju
     * @return Nazwa kraju
     */
    public String getNamePropertyValue() {
        return nameProperty.get();
    }

    /**
     * Ustawia StringProperty zawierający nazwę Kraju
     * @param nameProperty Nazwa kraju (StringProperty)
     */
    public void setNameProperty(StringProperty nameProperty) {
        this.nameProperty = nameProperty;
    }

    /**
     * Ustawia nazwę kraju
     * @param nameProperty nazwa Kraju
     */
    public void setNamePropertyValue(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    /**
     * Zwraca domyślną walutę danego kraju
     * @return Obiekt klasy Waluta
     */
    public Waluta getWaluta() {
        return waluta;
    }

    /**
     * Ustawia domyślną walutę dla danego kraju
     * @param waluta Obiekt klasy Waluta
     */
    public void setWaluta(Waluta waluta) {
        this.waluta = waluta;
    }


    // POZOSTALE
    @Override
    public String toString() {
        return nameProperty.get();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(nameProperty.getValue());
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        nameProperty = new SimpleStringProperty((String) ois.readObject());
    }
}
