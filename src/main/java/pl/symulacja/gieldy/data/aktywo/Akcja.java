package pl.symulacja.gieldy.data.aktywo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import pl.symulacja.gieldy.data.spolka.Spolka;
import java.io.Serializable;

/**
 * Klasa imitująca akcje spółki (rozszerzająca aktywo), zawiera dodatkowo odniesienie do właściciela - spółki
 * @author Paweł
 */
public class Akcja extends Aktywo implements Serializable {
    private Spolka owner;

    // KONSTRUKTORY
    public Akcja(){}

    public Akcja(String name, DoubleProperty price, DoubleProperty maxPrice, DoubleProperty minPrice, IntegerProperty quantity, Spolka owner){
        this.setNamePropertyValue(name);
        this.setPriceProperty(price);
        this.setMaxPriceProperty(maxPrice);
        this.setMinPriceProperty(minPrice);
        this.setQuantityPropertyValue(quantity.get());
        this.setOwner(owner);
    }

    public Akcja(String name, DoubleProperty price, DoubleProperty maxPrice, DoubleProperty minPrice, int quantity, Spolka owner){
        this.setNamePropertyValue(name);
        this.setPriceProperty(price);
        this.setMaxPriceProperty(maxPrice);
        this.setMinPriceProperty(minPrice);
        this.setQuantityPropertyValue(quantity);
        this.setOwner(owner);
    }

    // GETTERY I SETTERY
    /**
     * Zwraca właściciela - Spółkę która wypuściła tą akcję
     * @return Właściciel
     */
    public Spolka getOwner() {
        return owner;
    }

    /**
     * Ustawia właściciela - Spółkę która wypuszcza tą akcję
     * @param owner Właściciel
     */
    public void setOwner(Spolka owner) {
        this.owner = owner;
    }
}
