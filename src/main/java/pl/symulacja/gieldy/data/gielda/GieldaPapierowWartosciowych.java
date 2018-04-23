package pl.symulacja.gieldy.data.gielda;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.symulacja.gieldy.data.aktywo.Akcja;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.aktywo.Waluta;
import pl.symulacja.gieldy.data.indeks.Indeks;
import pl.symulacja.gieldy.data.kraj.Kraj;
import pl.symulacja.gieldy.data.podmiot.FunduszInwestycyjny;
import pl.symulacja.gieldy.data.podmiot.Inwestor;
import pl.symulacja.gieldy.data.podmiot.Podmiot;
import pl.symulacja.gieldy.data.spolka.Spolka;
import pl.symulacja.gieldy.utils.RandomModule;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Klasa imitująca Giełdę Papierów Wartościowych - Rozszerzenie giełdy, dodatkowo zawiera listę Akcji, Kraj, Walutę, Miasto, Ulicę i liste indeksów notujących spółki
 * @author Paweł
 */
public class GieldaPapierowWartosciowych extends Gielda implements Serializable {
    private Kraj country = null;
    private Waluta currency = null;
    transient private StringProperty townProperty = new SimpleStringProperty(this, "townProperty");
    transient private StringProperty streetProperty = new SimpleStringProperty(this,"streetProperty");
    transient private ObservableList<Indeks> indexList = FXCollections.observableArrayList();
    transient private ObservableList<Akcja> stockList = FXCollections.observableArrayList();

    /**
     * Zwraca listę Akcji
     * @return lista Akcji
     */
    public ObservableList<Akcja> getStockList() {
        return stockList;
    }

    /**
     * Zwraca kraj pochodzenia giełdy
     * @return Obiekt klasy Kraj
     */
    public Kraj getCountry() {
        return country;
    }

    /**
     * Ustawia kraj pochodzenia giełdy
     * @param country Obiekt klasy Kraj
     */
    public void setCountry(Kraj country) {
        this.country = country;
    }

    /**
     * Zwraca walutę którą się posluguje giełda
     * @return Obiekt klasy Waluta
     */
    synchronized public Waluta getCurrency() {
        return currency;
    }

    /**
     * Ustawia walutę którą posługuje się giełda
     * @param currency Obiekt klasy Waluta
     */
    synchronized public void setCurrency(Waluta currency) {
        this.currency = currency;
    }

    /**
     * Zwraca miasto pochodzenia
     * @return Miasto
     */
    public String getTownPropertyValue() {
        return townProperty.get();
    }

    /**
     * Zwraca miasto pochodzenia
     * @return Miasto (StringProperty)
     */
    public StringProperty getTownProperty() {
        return townProperty;
    }

    /**
     * Ustawia miasto pochodzenia
     * @param townProperty Miasto
     */
    public void setTownPropertyValue(String townProperty) {
        this.townProperty.set(townProperty);
    }

    /**
     * Zwraca ulicę przy której mieści się giełda
     * @return Ulica
     */
    public String getStreetPropertyValue() {
        return streetProperty.get();
    }

    /**
     * Zwaraca ulicę przy której mieści się giełda
     * @return Ulica
     */
    public StringProperty getStreetProperty() {
        return streetProperty;
    }

    /**
     * Ustawia ulicę przy której mieści się giełda
     * @param streetProperty Ulica
     */
    public void setStreetPropertyValue(String streetProperty) {
        this.streetProperty.set(streetProperty);
    }

    /**
     * Zwraca listę indeksów
     * @return Lista indeksów
     */
    public ObservableList<Indeks> getIndexList() {
        return indexList;
    }

    /**
     * Zwraca listę indeksów
     * @return Lista indeksów
     */
    @Override
    public ObservableList getList() {
        return getStockList();
    }

    /**
     * Zlecenie kupna składane przez podmiot - zrealizowane zwraca mu pakiet Akcji z zabezpieczeniem usuwania jednostek funduszu usuniętego
     * @param inwestor Podmiot kupujący
     * @param aktywo Wybrana Akcja
     * @param quantity Ilość aktywa do kupienia
     */
    @Override
    synchronized public void zlecenieKupna(Podmiot inwestor, Aktywo aktywo, int quantity) {
        if (!aktywo.isUsuwany()) {
            double price, totalPrice, investorBudget;
            investorBudget = inwestor.getBudgetPropertyValue();
            if (quantity >= aktywo.getQuantityPropertyValue()) {
                price = przelicz(inwestor, aktywo, aktywo.getQuantityPropertyValue());
                totalPrice = przliczMarze(price);
                if (totalPrice <= investorBudget) {
                    //  Ustawianie budzetu
                    double temp = RandomModule.round(investorBudget - RandomModule.round(totalPrice) / 100) / 100;
                    inwestor.setBudgetPropertyValue(temp);
                    // Usuwanie aktywa z giełdy
                    this.stockList.remove(aktywo);
                    Aktywo.getMainDataClass().getGlobalStockList().remove(aktywo);
                    // Przypisanie aktywa inwestorowi
                    inwestor.dodajAktywo(aktywo);
                    ((Akcja) aktywo).getOwner().setVolumePropertyValue(((Akcja) aktywo).getOwner().getVolumePropertyValue() + aktywo.getQuantityPropertyValue());
                    ((Akcja) aktywo).getOwner().setSalesPropertyValue(RandomModule.round(((Akcja) aktywo).getOwner().getSalesPropertyValue() + price) / 100);
                } else {
                    kupnoZleceniaGdyMaloSrodkow(inwestor, aktywo);
                }
            } else {
                price = przelicz(inwestor, aktywo, aktywo.getQuantityPropertyValue());
                totalPrice = przliczMarze(price);
                if (totalPrice <= investorBudget) {
                    // Zmiana budzetu inwestora
                    double temp = RandomModule.round(investorBudget - RandomModule.round(totalPrice) / 100) / 100;
                    inwestor.setBudgetPropertyValue(temp);
                    // Przypisanie aktywa inwestorowi i aktualizacja jego ilości na giełdzie
                    inwestor.dodajAktywo(wydzielAkcje((Akcja) aktywo, quantity));
                    aktywo.setQuantityPropertyValue(aktywo.getQuantityPropertyValue() - quantity);
                    ((Akcja) aktywo).getOwner().setVolumePropertyValue(((Akcja) aktywo).getOwner().getVolumePropertyValue() + quantity);
                    ((Akcja) aktywo).getOwner().setSalesPropertyValue(RandomModule.round(((Akcja) aktywo).getOwner().getSalesPropertyValue() + price) / 100);
                } else {
                    kupnoZleceniaGdyMaloSrodkow(inwestor, aktywo);
                }
            }
        }
        else {
            inwestor.getStockList().remove(aktywo);
        }
    }

    /**
     * Wyznacza ilość akcji do kupienia gdy podmiotu nie stać na wybraną przez siebie ilość
     * @param inwestor Podmiot kupujący
     * @param aktywo Wybrane aktywo
     */
    private void kupnoZleceniaGdyMaloSrodkow(Podmiot inwestor, Aktywo aktywo) {
        double totalPrice, price, aktywoPrice, investorBudget;
        int quantity;
        // Pobranie kursu aktywa
        aktywoPrice = aktywo.getPricePropertyValue();
        // Popbranie maksymalnej, możliwej do wydania ilosci pieniedzy
        investorBudget = inwestor.getBudgetPropertyValue();
        // Wyliczenie ilości aktywa możliwej do pobrania
        price = przelicz(investorBudget);
        quantity = (int)(price/aktywoPrice);
        if (quantity > 0) {
            // Wyliczenie ceny kupowanej paczki aktyw i odciagniecie jej z budzetu
            totalPrice = przliczMarze(aktywoPrice * quantity);
            double temp = RandomModule.round(investorBudget - RandomModule.round(totalPrice) / 100) / 100;
            inwestor.setBudgetPropertyValue(temp);
            // Przypisanie paczki jednostek funduszu do inwestora i aktualizacja jej ilosci na rynku
            inwestor.dodajAktywo(wydzielAkcje((Akcja) aktywo, quantity));
            aktywo.setQuantityPropertyValue(aktywo.getQuantityPropertyValue() - quantity);
            ((Akcja) aktywo).getOwner().setVolumePropertyValue(((Akcja) aktywo).getOwner().getVolumePropertyValue() + quantity);
            ((Akcja) aktywo).getOwner().setSalesPropertyValue(RandomModule.round(((Akcja) aktywo).getOwner().getSalesPropertyValue() + price)/100);
        }
    }

    /**
     * Przydziela pakiet akcji kupującemu
     * @param akcja Wybrana akcja
     * @param quantity Ilość akcji
     * @return Paczka akcji wybranej spółki
     */
    private Akcja wydzielAkcje(Akcja akcja, int quantity){
        Akcja aktywo = new Akcja();
        aktywo.setNameProperty(akcja.getNameProperty());
        aktywo.setPriceProperty(akcja.getPriceProperty());
        aktywo.setMaxPriceProperty(akcja.getMaxPriceProperty());
        aktywo.setMinPriceProperty(akcja.getMinPriceProperty());
        aktywo.setQuantityPropertyValue(quantity);
        aktywo.setChartData(akcja.getChartData());
        aktywo.setOwner(akcja.getOwner());
        aktywo.setStockExchange(akcja.getStockExchange());
        aktywo.setUsuwanyProperty(akcja.getUsuwanyProperty());
        return aktywo;
    }

    /**
     * Zlecenie sprzedaży składane przez podmiot jest realizowane przez rynek - inwestor dostaje pieniądze - z zabezpieczeniem
     * usuwania akcji usuniętej spółki
     * @param inwestor Podmiot sprzedający
     * @param aktywo Sprzedawane aktywo
     * @param quantity Ilość aktywa
     */
    @Override
    synchronized public void zlecenieSprzedazy(Podmiot inwestor, Aktywo aktywo, int quantity) {
        if (!aktywo.isUsuwany()) {
            int aktywoQuantity = aktywo.getQuantityPropertyValue();
            if (quantity >= aktywoQuantity) {
                double price = przelicz(inwestor, aktywo, aktywoQuantity);
                inwestor.getStockList().remove(aktywo);
                double temp = RandomModule.round(inwestor.getBudgetPropertyValue() + RandomModule.round(price) / 100) / 100;
                inwestor.setBudgetPropertyValue(temp);
                gieldaOdkupAktywo(aktywo);
                ((Akcja) aktywo).getOwner().setVolumePropertyValue(((Akcja) aktywo).getOwner().getVolumePropertyValue() - aktywoQuantity);
                ((Akcja) aktywo).getOwner().setSalesPropertyValue(RandomModule.round(((Akcja) aktywo).getOwner().getSalesPropertyValue() - price) / 100);
            } else {
                double price = przelicz(inwestor, aktywo, quantity);
                aktywo.setQuantityPropertyValue(aktywo.getQuantityPropertyValue() - quantity);
                double temp = RandomModule.round(inwestor.getBudgetPropertyValue() + RandomModule.round(price) / 100) / 100;
                inwestor.setBudgetPropertyValue(temp);
                gieldaOdkupAktywo(wydzielAkcje((Akcja) aktywo, quantity));
                ((Akcja) aktywo).getOwner().setVolumePropertyValue(((Akcja) aktywo).getOwner().getVolumePropertyValue() - quantity);
                ((Akcja) aktywo).getOwner().setSalesPropertyValue(RandomModule.round(((Akcja) aktywo).getOwner().getSalesPropertyValue() - price) / 100);
            }
        }
        else {
            inwestor.getStockList().remove(aktywo);
        }
    }

    /**
     * Scalanie odkupionych akcji w jeden pakiet
     * @param aktywo Akcje
     */
    private void gieldaOdkupAktywo(Aktywo aktywo) {
        for (Aktywo a : stockList) {
            if (a.getNamePropertyValue().equals(aktywo.getNamePropertyValue())) {
                a.setQuantityPropertyValue(a.getQuantityPropertyValue() + aktywo.getQuantityPropertyValue());
                return;
            }
        }
        stockList.add((Akcja)aktywo);
        Aktywo.getMainDataClass().getGlobalStockList().add(aktywo);
    }

    /**
     * Wypuszczenie nowych jednostek funduszu i dodanie ich do rynku
     * @param akcja Wypuszcone akcje do scalenia
     */
    synchronized public void dodajNoweAkcje(Akcja akcja) {
        akcja.getOwner().setIncomePropertyValue(akcja.getOwner().getIncomePropertyValue() + akcja.getQuantityPropertyValue() * akcja.getPricePropertyValue());
        this.gieldaOdkupAktywo(akcja);
    }

    synchronized public void wykupAkcje(Spolka spolka, double price) {
        Akcja szukanaAkcja = null;
        for (Akcja akcja: getStockList()){
            if (akcja.getNamePropertyValue().equals("Akcja " + spolka.getNamePropertyValue())){
               szukanaAkcja = akcja;
            }
        }
        if (szukanaAkcja != null){
            spolka.setIncomePropertyValue(RandomModule.round(spolka.getIncomePropertyValue() - price * szukanaAkcja.getQuantityPropertyValue())/100);
            spolka.setQuantityPropertyValue(spolka.getQuantityPropertyValue() - szukanaAkcja.getQuantityPropertyValue());
            this.getStockList().remove(szukanaAkcja);
            Aktywo.getMainDataClass().getGlobalStockList().remove(szukanaAkcja);
        }
    }

    /**
     * Wyznacza wartość indeksów
     */
    public void obliczIndeksy() {
        double result = 0, opening = 1;
        for (Indeks indeks: getIndexList()){
            try {
                for (Spolka spolka : indeks.getCompanyList()) {
                    opening += spolka.getOpeningPriceProperty().get();
                    result += spolka.getPricePropertyValue();
                    spolka.setSalesPropertyValue(0);
                }
                result /= opening;
                result *= 100;
                indeks.setScorePropertyValue((int)result);
                result = 0; opening = 1;
            }catch (Exception ignored){}
        }
    }

    /**
     * Usuwanie akcji dopieroco usuniętej spółki
     * @param s Nazwa akcji
     */
    synchronized public void usunJednostki(String s) {
        Akcja akcja = null;
        for (Akcja aktywo: getStockList()){
            if (aktywo.getNamePropertyValue().equals(s)) {
                akcja = aktywo;
                break;
            }
        }
        if (akcja != null){
            for (Inwestor inwestor: Aktywo.getMainDataClass().getInvestorList()){
                inwestor.getStockList().remove(akcja);
            }
            for (FunduszInwestycyjny fundusz: Aktywo.getMainDataClass().getFundList()){
                fundusz.getStockList().remove(akcja);
            }
            Aktywo.getMainDataClass().getGlobalStockList().remove(akcja);
            getStockList().remove(akcja);
        }
    }


    // POZOSTALE
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(townProperty.get());
        oos.writeObject(streetProperty.get());
        oos.writeObject(new ArrayList<>(indexList));
        oos.writeObject(new ArrayList<>(stockList));
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        townProperty = new SimpleStringProperty((String) ois.readObject());
        streetProperty = new SimpleStringProperty((String) ois.readObject());
        indexList = FXCollections.observableArrayList((ArrayList<Indeks>)ois.readObject());
        stockList = FXCollections.observableArrayList((ArrayList<Akcja>)ois.readObject());
    }
}
