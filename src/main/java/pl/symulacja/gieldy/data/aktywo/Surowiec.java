package pl.symulacja.gieldy.data.aktywo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

/**
 * Klasa imitująca surowiec (rozszerzająca aktywo), zawiera dodatkowo jednostkę handlową w jakiej jest sprzedawany
 * @author Paweł
 */
public class Surowiec extends Aktywo implements Serializable{
    transient private StringProperty jednostkaHandlowaProperty = new SimpleStringProperty(this,"jednostkaHandlowaProperty");

    //  KONSTRUKTORY    //
    public Surowiec(){}


    //  GETTERY I SETTERY   //
    /**
     * Zwraca jednostkę handlową w której sprzedawany jest surowiec
     * @return Jednostka handlowa (StringProperty)
     */
    public StringProperty getJednostkaHandlowaProperty() {
        return jednostkaHandlowaProperty;
    }

    /**
     * Zwraca jednostkę handlową w której sprzedawany jest surowiec
     * @return Jednostka handlowa
     */
    public String getJednostkaHandlowaPropertyValue() {
        return jednostkaHandlowaProperty.get();
    }

    /**
     * Ustawia jednostkę handlową w której sprzedawany jest surowiec
     * @param nameProperty Jednostka handlowa (StringProperty)
     */
    public void setJednostkaHandlowaProperty(StringProperty nameProperty){
        this.jednostkaHandlowaProperty = nameProperty;
    }

    /**
     * Ustawia jednostkę handlową w której sprzedawany jest surowiec
     * @param propertyName Jednostka handlowa
     */
    public void setJednostkaHandlowaPropertyValue(String propertyName) {
        this.jednostkaHandlowaProperty.set(propertyName);
    }

    /**
     * Ustala nową cenę surowca podczas jego tworzenia
     * @param price Cena
     */
    public void setNewPrice(double price){
        price *= 100;
        price = Math.round(price);
        price /= 100;
        setPricePropertyValue(price);
        setMaxPricePropertyValue(price);
        setMinPricePropertyValue(price);
        setQuantityPropertyValue(0);
        getChartData().add(price);
    }

    /**
     * Zwraca zaokrągloną cenę do 2 miejsc po przecinku
     * @param value Cena
     * @return Zaokrąglona cena
     */
    private static double zaokralij(double value) {
        value *= 100;
        value = Math.round(value);
        value /=100;
        return value;
    }

    /**
     * Symuluje zmianę ceny Surowca
     */
    synchronized public void zmianaCeny() {
        Random random = new Random();
        double temp = random.nextDouble();
        temp *= 10;
        temp = zaokralij(temp);
        if (random.nextDouble() < 0.5)
            temp *= -1;
        double cena = getPricePropertyValue();
        if (cena + temp < 0)
            cena -= temp;
        else
            cena += temp;
        cena = zaokralij(cena);
        if (cena > getMaxPricePropertyValue())
            setMaxPricePropertyValue(cena);
        else if (cena < getMinPricePropertyValue())
            setMinPricePropertyValue(cena);
        setPricePropertyValue(cena);
        getChartData().add(cena);
    }


    // POZOSTALE
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(jednostkaHandlowaProperty.get());
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        jednostkaHandlowaProperty = new SimpleStringProperty((String) ois.readObject());
    }
}
