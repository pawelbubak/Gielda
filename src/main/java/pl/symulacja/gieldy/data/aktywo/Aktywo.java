package pl.symulacja.gieldy.data.aktywo;

import javafx.beans.property.*;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.gielda.Gielda;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Klasa abstrakcyjna zawierająca podstawowe dane rozrzerzających ją klas: nazwa, cena, cena maksymalna, cena minimalna, ilość
 * @author Paweł
 */
public abstract class Aktywo implements Serializable{
    private static MainDataClass mainData;
    transient private StringProperty nameProperty = new SimpleStringProperty(this,"nameProperty");
    transient private DoubleProperty priceProperty = new SimpleDoubleProperty(this,"priceProperty");
    transient private DoubleProperty maxPriceProperty = new SimpleDoubleProperty(this,"maxPriceProperty");
    transient private DoubleProperty minPriceProperty = new SimpleDoubleProperty(this,"minPriceProperty");
    transient private IntegerProperty quantityProperty = new SimpleIntegerProperty(this,"quantityProperty");
    transient private BooleanProperty usuwany = new SimpleBooleanProperty(this, "usuwany", false);
    private Gielda stockExchange;
    private ArrayList<Double> chartDataY = new ArrayList<>();

    //  KONSTRUKTORY    //
    public Aktywo(){}


    //  GETTERY I SETTERY   //

    /**
     * Zwraca nazwę aktywa
     * @return Nazwa aktywa (StringProperty)
     */
    public StringProperty getNameProperty() {
        return nameProperty;
    }

    /**
     * Zwraca nazwę aktywa
     * @return Nazwa aktywa
     */
    public String getNamePropertyValue() {
        return nameProperty.get();
    }

    /**
     * Ustawia nazwę aktywa
     * @param nameProperty Nazwa aktywa (StringProperty)
     */
    public void setNameProperty(StringProperty nameProperty){
        this.nameProperty = nameProperty;
    }

    /**
     * Ustawia nazwę aktywa
     * @param propertyName Nazwa aktywa
     */
    public void setNamePropertyValue(String propertyName) {
        this.nameProperty.set(propertyName);
    }

    /**
     * Zwraca aktualną cenę akcji
     * @return Cena akcji (DoubleProperty)
     */
    synchronized public DoubleProperty getPriceProperty() {
        return priceProperty;
    }

    /**
     * Zwraca aktualną cenę akcji
     * @return Cena akcji
     */
    synchronized public double getPricePropertyValue() {
        return priceProperty.get();
    }

    /**
     * Ustawia aktualną cenę akcji
     * @param nameProperty Cena akcji (DoubleProperty)
     */
    public void setPriceProperty(DoubleProperty nameProperty){
        this.priceProperty = nameProperty;
    }

    /**
     * Ustawia aktualną cenę akcji
     * @param propertyName Cena akcji
     */
    synchronized public void setPricePropertyValue(double propertyName) {
        this.priceProperty.set(propertyName);
    }

    /**
     * Zwraca cenę maksymalną akcji
     * @return Cena maksymalna (DoubleProperty)
     */
    public DoubleProperty getMaxPriceProperty() {
        return maxPriceProperty;
    }

    /**
     * Zwraca cenę maksymalną akcji
     * @return Cena maksymalna
     */
    double getMaxPricePropertyValue() {
        return maxPriceProperty.get();
    }

    /**
     * Ustawia cenę maksymalną akcji
     * @param nameProperty Cena maksymalna (DoubleProperty)
     */
    public void setMaxPriceProperty(DoubleProperty nameProperty){
        this.maxPriceProperty = nameProperty;
    }

    /**
     * Ustawia cenę maksymalną akcji
     * @param propertyName Cena maksymalna
     */
    public void setMaxPricePropertyValue(double propertyName) {
        this.maxPriceProperty.set(propertyName);
    }

    /**
     * Zwraca cenę minimalną akcji
     * @return Cena minimalna (DoubleProperty)
     */
    public DoubleProperty getMinPriceProperty() {
        return minPriceProperty;
    }

    /**
     * Zwraca cenę minimalną akcji
     * @return Cena minimalna
     */
    double getMinPricePropertyValue() {
        return minPriceProperty.get();
    }

    /**
     * Ustawia cenę minimalną akcji
     * @param nameProperty Cena minimalna (DoubleProperty)
     */
    public void setMinPriceProperty(DoubleProperty nameProperty){
        this.minPriceProperty = nameProperty;
    }

    /**
     * Ustawia cenę minimalną akcji
     * @param propertyName Cena minimalna
     */
    public void setMinPricePropertyValue(double propertyName) {
        this.minPriceProperty.set(propertyName);
    }

    /**
     * Zwraca ilość akcji
     * @return Ilość (IntegerProperty)
     */
    public IntegerProperty getQuantityProperty() {
        return quantityProperty;
    }

    /**
     * Zwraca ilość akcji
     * @return Ilość
     */
    public int getQuantityPropertyValue() {
        return quantityProperty.get();
    }

    /**
     * Ustawia ilość akcji
     * @param nameProperty Ilość (IntegerProperty)
     */
    public void setQuantityProperty(IntegerProperty nameProperty){
        this.quantityProperty = nameProperty;
    }

    /**
     * Ustawia ilość akcji
     * @param propertyName Ilość
     */
    public void setQuantityPropertyValue(int propertyName) {
        this.quantityProperty.set(propertyName);
    }

    /**
     * Zwraca instancję z wszystkimi danymi programu
     * @param mainDataClass Dane Programu
     */
    public static void setMainDataClass(MainDataClass mainDataClass){
        mainData = mainDataClass;
    }

    /**
     * Ustawia instancje z wszystkimi danymi progamu
     * @return Dane Programu
     */
    synchronized public static MainDataClass getMainDataClass(){
        return mainData;
    }

    /**
     * Zwraca giełde na której aktywo jest wystawiane
     * @return Giełda
     */
    public Gielda getStockExchange() {
        return stockExchange;
    }

    /**
     * Ustawia giełde na której aktywo jest wystawiane
     * @param stockExchange Giełda
     */
    public void setStockExchange(Gielda stockExchange) {
        this.stockExchange = stockExchange;
    }

    /**
     * Zwraca dane do wykresu
     * @return Dane do wykresu
     */
    public ArrayList<Double> getChartData() {
        return chartDataY;
    }

    /**
     * Ustawia dane do wykresu
     * @param chartData Dane do wykresu
     */
    public void setChartData(ArrayList<Double> chartData) {
        this.chartDataY = chartData;
    }

    /**
     * Zwraca czy dana akcja ma być usunięta
     * @return Czy usunąć
     */
    synchronized public boolean isUsuwany() {
        return usuwany.get();
    }

    /**
     * Zwraca czy dana akcja ma być usunięta
     * @return Czy usunąć (BooleanProperty)
     */
    public BooleanProperty getUsuwanyProperty() {
        return usuwany;
    }

    /**
     * Ustawia czy dana akcja ma być usunięta
     * @param usuwany Czy usunąć
     */
    synchronized public void setUsuwanyValue(boolean usuwany) {
        this.usuwany.set(usuwany);
    }

    /**
     * Ustawia czy dana akcja ma być usunięta
     * @param usuwany Czy usunąć (BooleanProperty)
     */
    public void setUsuwanyProperty(BooleanProperty usuwany) {
        this.usuwany = usuwany;
    }

    //  POZOSTALE   //
    @Override
    public String toString() {
        return nameProperty.get();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(nameProperty.get());
        oos.writeObject(priceProperty.get());
        oos.writeObject(maxPriceProperty.get());
        oos.writeObject(minPriceProperty.get());
        oos.writeObject(quantityProperty.get());
        oos.writeObject(usuwany.get());
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        nameProperty = new SimpleStringProperty((String) ois.readObject());
        priceProperty = new SimpleDoubleProperty((double) ois.readObject());
        maxPriceProperty = new SimpleDoubleProperty((double) ois.readObject());
        minPriceProperty = new SimpleDoubleProperty((double) ois.readObject());
        quantityProperty = new SimpleIntegerProperty((int) ois.readObject());
        usuwany = new SimpleBooleanProperty((boolean) ois.readObject());
    }
}