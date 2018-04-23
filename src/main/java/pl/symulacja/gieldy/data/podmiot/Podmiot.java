package pl.symulacja.gieldy.data.podmiot;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.aktywo.Waluta;
import pl.symulacja.gieldy.main.SymulacjaGieldy;
import pl.symulacja.gieldy.utils.DialogsUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Anstrakcyjna klasa imitująca podmiot kupujący na giełdzie, jej rozszerzeniem jest Inwetor i Fundusz Inwestycyjny
 * Zawiera imię i nazwisko Podmiotu, jego budżet oraz przypisaną mu walutę, posiada również listę wykupionych aktyw
 * @author Paweł
 */
public abstract class Podmiot implements Serializable, Runnable{
    transient private StringProperty nameProperty = new SimpleStringProperty(this,"nameProperty");
    transient private StringProperty surnameProperty = new SimpleStringProperty(this,"nameProperty");
    transient private DoubleProperty budgetProperty = new SimpleDoubleProperty(this,"nameProperty");
    transient private ObservableList<Aktywo> stockList = FXCollections.observableArrayList();
    private Waluta domyslnaWaluta;
    private boolean kill = false;

    // KONSTRUKTORY
    public Podmiot(){}

    /**
     * Zwraca imię podmiotu
     * @return Imię podmiotu (StringProperty)
     */
    public StringProperty getNameProperty() {
        return nameProperty;
    }

    /**
     * Zwraca imię podmiotu
     * @return Imię podmiotu
     */
    public String getNamePropertyValue() {
        return nameProperty.get();
    }

    /**
     * Ustawia imię podmiotu
     * @param nameProperty Imię podmiotu
     */
    public void setNamePropertyValue(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    /**
     * Zwraca nazwisko podmiotu
     * @return Nazwisko podmiotu
     */
    public String getSurnamePropertyValue() {
        return surnameProperty.get();
    }

    /**
     * Zwraca nazwisko podmiotu
     * @return Nazwisko podmiotu (StringProperty)
     */
    public StringProperty getSurnameProperty() {
        return surnameProperty;
    }

    /**
     * Ustawia nazwisko podmiotu
     * @param surnameProperty Nazwisko podmiotu
     */
    public void setSurnamePropertyValue(String surnameProperty) {
        this.surnameProperty.set(surnameProperty);
    }

    /**
     * Zwraca budżet podmiotu
     * @return Budżet
     */
    public double getBudgetPropertyValue() {
        return budgetProperty.get();
    }

    /**
     * Zwraca budżet podmiotu
     * @return Budżet (DoubleProperty)
     */
    public DoubleProperty getBudgetProperty() {
        return budgetProperty;
    }

    /**
     * Ustawia budżet podmiotu
     * @param budgetProperty Budżet
     */
    public void setBudgetPropertyValue(double budgetProperty) {
        this.budgetProperty.set(budgetProperty);
    }

    /**
     * Zwraca listę aktyw wykupionych przez podmiot
     * @return Lista aktyw (ObservableList)
     */
    public ObservableList<Aktywo> getStockList() {
        return stockList;
    }

    /**
     * Zwraca walutę którą posługuje się podmiot
     * @return Obiekt klasy Waluta
     */
    public Waluta getDomyslnaWaluta() {
        return domyslnaWaluta;
    }

    /**
     * Przypisuje walutę podmiotowi
     * @param domyslnaWaluta Obiekt klasy Waluta
     */
    public void setDomyslnaWaluta(Waluta domyslnaWaluta) {
        this.domyslnaWaluta = domyslnaWaluta;
    }

    /**
     * Sprawdza zezwolenie na działanie wątku
     * @return Zezwolenie działania
     */
    private boolean isKill() {
        return kill;
    }

    /**
     * Ustawia zezwolenie na dziłanie wątku
     * @param kill Zezwolenie działania
     */
    public void setKill(boolean kill) {
        this.kill = kill;
    }

    public abstract void zlecSprzedaz();
    public abstract void zlecKupno();

    /**
     * Sumulacja zachowania podmiotu wraz z zaimplementowanym usuwaniem aktyw spółek/funduszy już nie istniejących na giełdzie
     */
    @Override
    public void run(){
        Random random = new Random();
        int licznik = 0;
        double choose;
        while (!isKill()) {
            while (SymulacjaGieldy.symuluj) {
                choose = random.nextDouble();
                if (choose < 0.05) {
                    zwiekszBudget();
                }
                else if (choose < 0.65) {
                    zlecKupno();
                }
                else{
                    zlecSprzedaz();
                }
                if (licznik == 6){
                    licznik = 0;
                    ArrayList<Aktywo> doUsuniecia = new ArrayList<>();
                    for (Aktywo aktywo: this.getStockList()){
                        try {
                            if (aktywo.isUsuwany())
                                doUsuniecia.add(aktywo);
                        }catch (Exception ignored){}
                    }
                    if (!doUsuniecia.isEmpty())
                        doUsuniecia.forEach(aktywo -> getStockList().remove(aktywo));
                }
                licznik++;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                DialogsUtils.errorDialog(e.getMessage());
            }
        }
    }

    /**
     * Pozwala zwiększyć budget Inwestora i usypia go na kilka sekund
     */
    void zwiekszBudget() {
        Random random = new Random();
        int temp = random.nextInt(100);
        if (temp < 10)
            temp += 20;
        temp *= 10;
        setBudgetPropertyValue(getBudgetPropertyValue() + temp);
        try {
            Thread.sleep((random.nextInt(10) + 1) * 1000);
        } catch (InterruptedException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
    }

    /**
     * Pozwala na dodawanie aktywa do listy tak żeby łączył się jeśli już jakaś jego ilośc została kupiona wcześniej przez podmiot
     * @param aktywo Kupione aktywo
     */
    public void dodajAktywo(Aktywo aktywo) {
        for (Aktywo a: this.getStockList()){
            if (a.getNamePropertyValue().equals(aktywo.getNamePropertyValue())) {
                a.setQuantityPropertyValue(a.getQuantityPropertyValue() + aktywo.getQuantityPropertyValue());
                return;
            }
        }
        getStockList().add(aktywo);
    }

    /**
     * Wyprzedaje wszystkie aktywa przed usunięciem podmiotu
     */
    public void sellAll(){
        while (getStockList().size() > 0) {
            Aktywo aktywo = getStockList().get(0);
            aktywo.getStockExchange().zlecenieSprzedazy(this, aktywo, aktywo.getQuantityPropertyValue());
        }
    }


    // POZOSTALE
    @Override
    public String toString() {
        return getNamePropertyValue() + " " + getSurnamePropertyValue();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(nameProperty.get());
        oos.writeObject(surnameProperty.get());
        oos.writeObject(budgetProperty.get());
        oos.writeObject(new ArrayList<>(stockList));
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        nameProperty = new SimpleStringProperty((String) ois.readObject());
        surnameProperty = new SimpleStringProperty((String) ois.readObject());
        budgetProperty = new SimpleDoubleProperty((double) ois.readObject());
        stockList = FXCollections.observableArrayList((ArrayList<Aktywo>)ois.readObject());
    }
}
