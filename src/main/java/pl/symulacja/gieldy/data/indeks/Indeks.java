package pl.symulacja.gieldy.data.indeks;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.symulacja.gieldy.data.spolka.Spolka;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Klasa imitująca indeksy notujące spółki (które sprzedają akcje na giełdzie) - zawiera listę przypisanych spółek, nazwę i swój wynik
 * @author Paweł
 */
public class Indeks implements Serializable{
    transient private StringProperty nameProperty = new SimpleStringProperty(this,"nameProperty");
    transient private ObservableList<Spolka> companyList = FXCollections.observableArrayList();
    transient private IntegerProperty scoreProperty = new SimpleIntegerProperty(this,"scoreProperty");


    // KONSTRUKTORY
    public Indeks(){}


    // GETTERY I SETTERY
    /**
     * Zwraca nazwę indeksu
     * @return Nazwa indesku
     */
    public String getNamePropertyValue() {
        return nameProperty.get();
    }

    /**
     * Zwraca nazwę indeksu
     * @return Nazwa indesku (StringProperty)
     */
    public StringProperty getNameProperty() {
        return nameProperty;
    }

    /**
     * Ustawia nazwę indeksu
     * @param nameProperty nazwa indeksu
     */
    public void setNamePropertyValue(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    /**
     * Ustawia nazwę indeksu
     * @param nameProperty nazwa indeksu (StringProperty)
     */
    public void setNameProperty(StringProperty nameProperty) {
        this.nameProperty = nameProperty;
    }

    /**
     * Zwraca listę spółek przypisanych do indeksu
     * @return Lista spółek (ObservableList)
     */
    public ObservableList<Spolka> getCompanyList() {
        return companyList;
    }

    /**
     * Zwraca wynik indeksu
     * @return Wynik indeksu (IntegerProperty)
     */
    public IntegerProperty getScoreProperty() {
        return scoreProperty;
    }

    /**
     * Ustawia wynik indeksu
     * @param scoreProperty wynik
     */
    public void setScorePropertyValue(int scoreProperty) {
        this.scoreProperty.set(scoreProperty);
    }


    // POZOSTALE
    @Override
    public String toString() {
        return getNamePropertyValue();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(nameProperty.get());
        oos.writeObject(new ArrayList<>(companyList));
        oos.writeObject(scoreProperty.get());
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        nameProperty = new SimpleStringProperty((String) ois.readObject());
        companyList = FXCollections.observableArrayList((ArrayList<Spolka>)ois.readObject());
        scoreProperty = new SimpleIntegerProperty((int) ois.readObject());
    }
}
