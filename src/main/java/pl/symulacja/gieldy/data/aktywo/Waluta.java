package pl.symulacja.gieldy.data.aktywo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.symulacja.gieldy.data.kraj.Kraj;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Klasa imitująca walutę - rozrzerza Aktywo o listę krajów które jej używają
 * @author Paweł
 */
public class Waluta extends Aktywo implements Serializable{
    transient private ObservableList<Kraj> countryList = FXCollections.observableArrayList();
    private static final double MAX_DOPUSZCZALNA_CENA = 100;

    // KONSTRUKTORY
    public Waluta(){
        Random rand = new Random();
        setNewPrice(rand.nextDouble()*100);
    }

    /**
     * Zwraca listę krajów używających tę waluty
     * @return Lista krajów
     */
    public ObservableList<Kraj> getCountryList() {
        return countryList;
    }

    /**
     * Ustawia listę krajów używających tę waluty
     * @param countryList Lista krajów
     */
    public void setCountryList(ObservableList<Kraj> countryList) {
        this.countryList = countryList;
    }

    /**
     * Ustala nową cenę waluty podczas jego tworzenia
     * @param price Cena
     */
    private void setNewPrice(double price){
        setPricePropertyValue(price);
        setMaxPricePropertyValue(price);
        setMinPricePropertyValue(price);
        setQuantityPropertyValue(0);
    }

    /**
     * Losuje wartośc waluty
     * @param w Waluta
     */
    public static void setValue(Waluta w){
        Random random = new Random();
        double price = random.nextDouble();
        price *= 20;
        if (price < 0.5)
            price += 0.5;
        price = zaokralij(price);
        w.setNewPrice(price);
    }

    /**
     * Zaokrągla odebraną wartość do 2 miejsc po przecinku
     * @param value cena
     * @return Zaokrąglona cena
     */
    private static double zaokralij(double value) {
        value *= 100;
        value = Math.round(value);
        value /=100;
        return value;
    }

    /**
     * Symuluje zmianę ceny waluty
     */
    synchronized public void zmianaCeny() {
        Random random = new Random();
        double temp = random.nextDouble();
        temp = zaokralij(temp);
        if (random.nextDouble() < 0.5)
            temp *= -1;
        double cena = getPricePropertyValue();
        if (cena + temp < 0 || cena + temp > MAX_DOPUSZCZALNA_CENA)
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
    @Override
    public String toString(){
        return getNamePropertyValue();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(new ArrayList<>(countryList));
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        countryList = FXCollections.observableArrayList((ArrayList<Kraj>)ois.readObject());
    }
}
