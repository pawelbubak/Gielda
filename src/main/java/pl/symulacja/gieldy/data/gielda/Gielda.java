package pl.symulacja.gieldy.data.gielda;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.podmiot.Podmiot;
import pl.symulacja.gieldy.utils.RandomModule;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Abstrakcyjna klasa z której dziedziczą bardziej specjalistyczne giełdy
 * @author Paweł
 */
public abstract class Gielda implements Serializable {
    transient private StringProperty nameProperty = new SimpleStringProperty(this, "nameProperty");
    transient private DoubleProperty marzaProperty = new SimpleDoubleProperty(this,"marzaProperty");

    // KONSTRUKTORY
    public Gielda(){}

    /**
     * Zwraca nazwę giełdy
     * @return Nazwa giełdy
     */
    public String getNamePropertyValue() {
        return nameProperty.get();
    }

    /**
     * Zwraca nazwę giełdy
     * @return Nazwa giełdy (StringProperty)
     */
    public StringProperty getNameProperty() {
        return nameProperty;
    }

    /**
     * Ustawia nazwę giełdy
     * @param nameProperty Nazwa giełdy
     */
    public void setNamePropertyValue(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    /**
     * Ustawia nazwę giełdy
     * @param nameProperty Nazwa giełdy (StringProperty)
     */
    public void setNameProperty(StringProperty nameProperty) {
        this.nameProperty = nameProperty;
    }

    /**
     * Zwraca marże
     * @return Marża
     */
    synchronized public double getMarzaPropertyValue() {
        return marzaProperty.get();
    }

    /***
     * Zwraca marże
     * @return Marża (DoubleProperty)
     */
    public DoubleProperty getMarzaProperty() {
        return marzaProperty;
    }

    /**
     * Ustawia marże
     * @param marzaProperty Marża
     */
    synchronized public void setMarzaPropertyValue(double marzaProperty) {
        this.marzaProperty.set(marzaProperty);
    }

    public abstract ObservableList<Aktywo> getList();

    public abstract void zlecenieKupna(Podmiot inwestor, Aktywo aktywo, int quantity);

    public abstract void zlecenieSprzedazy(Podmiot inwestor, Aktywo aktywo, int quantity);

    /**
     * Wyznacza cenę za podana ilość wybranego aktywa
     * @param podmiot Podmiot
     * @param aktywo Wybrane aktywo
     * @param quantity kupowana ilość
     * @return Cena
     */
    double przelicz(Podmiot podmiot, Aktywo aktywo, double quantity){
        return quantity * aktywo.getPricePropertyValue() / podmiot.getDomyslnaWaluta().getPricePropertyValue();
    }

    /**
     * Wyznacza marże dla wybranego aktywa
     * @param price Cena
     * @return Cena z marżą
     */
    double przliczMarze(double price){
        double temp = price + price * Aktywo.getMainDataClass().getRynekWalut().getMarzaPropertyValue();
        return RandomModule.round(temp + temp * getMarzaPropertyValue())/100;
    }

    /**
     * Wyznacza maksymalną kwotę za którą można kupić aktywo po odciągnięciu marży
     * @param budget Budżet kupującego
     * @return Cena
     */
    double przelicz(double budget){
        double marza, marza2;
        marza = Aktywo.getMainDataClass().getRynekWalut().getMarzaPropertyValue();
        marza2 = getMarzaPropertyValue();
        return RandomModule.round(budget/(1 + marza + marza2 + marza*marza2))/100;
    }


    // POZOSTALE
    @Override
    public String toString() {
        return getNamePropertyValue();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(nameProperty.get());
        oos.writeObject(marzaProperty.get());
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        nameProperty = new SimpleStringProperty((String) ois.readObject());
        marzaProperty = new SimpleDoubleProperty((double) ois.readObject());
    }
}
