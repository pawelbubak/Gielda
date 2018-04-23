package pl.symulacja.gieldy.data.gielda;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.aktywo.Waluta;
import pl.symulacja.gieldy.data.podmiot.Podmiot;
import pl.symulacja.gieldy.utils.RandomModule;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Klasa imitująca Rynek Walut - Rozszerzenie giełdy, dodatkowo zawiera listę Walut
 * @author Paweł
 */
public class RynekWalut extends Gielda implements Serializable{
    transient private ObservableList<Waluta> currencyList = FXCollections.observableArrayList();

    /**
     * Zwraca Listę Walut
     * @return Lista walut
     */
    synchronized public ObservableList<Waluta> getCurrencyList() {
        return currencyList;
    }

    /**
     * Zwraca Listę Walut
     * @return Lista walut
     */
    @Override
    public ObservableList getList() {
        return currencyList;
    }

    /**
     * Zlecenie kupna składane przez podmiot - zrealizowane zwraca mu pakiet Waluty
     * @param inwestor Podmiot kupujący
     * @param aktywo Wybrana waluta
     * @param quantity Ilość aktywa do kupienia
     */
    @Override
    synchronized public void zlecenieKupna(Podmiot inwestor, Aktywo aktywo, int quantity) {
        double price, totalPrice, investorBudget;
        price = przelicz(inwestor,aktywo,quantity);
        totalPrice = przliczMarze(price);
        investorBudget = inwestor.getBudgetPropertyValue();
        if (totalPrice <= investorBudget){
            double temp = RandomModule.round(investorBudget - RandomModule.round(totalPrice)/100)/100;
            inwestor.setBudgetPropertyValue(temp);
            inwestor.dodajAktywo(wydzielWalute((Waluta)aktywo,quantity));
        }
        else {
            kupnoZleceniaGdyMaloSrodkow(inwestor, aktywo);
        }
    }

    /**
     * Wyznacza ilość waluty do kupienia gdy podmiotu nie stać na wybraną przez siebie ilość
     * @param inwestor Podmiot kupujący
     * @param aktywo Wybrane aktywo
     */
    private void kupnoZleceniaGdyMaloSrodkow(Podmiot inwestor, Aktywo aktywo) {
        double totalPrice, price, aktywoPrice, investorBudget;
        int quantity;
        aktywoPrice = aktywo.getPricePropertyValue();
        investorBudget = inwestor.getBudgetPropertyValue();
        price = przelicz(investorBudget);
        quantity = (int)(price/aktywoPrice);
        if (quantity > 0) {
            totalPrice = przliczMarze(aktywoPrice * quantity);
            double temp = RandomModule.round(investorBudget - RandomModule.round(totalPrice) / 100) / 100;
            inwestor.setBudgetPropertyValue(temp);
            inwestor.dodajAktywo(wydzielWalute((Waluta) aktywo, quantity));
        }
    }

    /**
     * Przydziela pakiet jednostek waluty kupującemu
     * @param aktywo Wybrana waluta
     * @param quantity Ilość waluty
     * @return Paczka jednostek wybranej waluty
     */
    private Aktywo wydzielWalute(Waluta aktywo, int quantity) {
        Waluta waluta = new Waluta();
        waluta.setNameProperty(aktywo.getNameProperty());
        waluta.setPriceProperty(aktywo.getPriceProperty());
        waluta.setMaxPriceProperty(aktywo.getMaxPriceProperty());
        waluta.setMinPriceProperty(aktywo.getMinPriceProperty());
        waluta.setQuantityProperty(new SimpleIntegerProperty(waluta,"waluta",quantity));
        waluta.setChartData(aktywo.getChartData());
        waluta.setCountryList(aktywo.getCountryList());
        waluta.setStockExchange(aktywo.getStockExchange());
        return waluta;
    }

    /**
     * Zlecenie sprzedaży składane przez podmiot jest realizowane przez rynek - inwestor dostaje pieniądze
     * @param inwestor Podmiot sprzedający
     * @param aktywo Sprzedawane aktywo
     * @param quantity Ilość aktywa
     */
    @Override
    synchronized public void zlecenieSprzedazy(Podmiot inwestor, Aktywo aktywo, int quantity) {
        if (quantity >= aktywo.getQuantityPropertyValue()){
            double price = przelicz(inwestor,aktywo,aktywo.getQuantityPropertyValue());
            inwestor.getStockList().remove(aktywo);
            double temp = RandomModule.round(inwestor.getBudgetPropertyValue() + RandomModule.round(price)/100)/100;
            inwestor.setBudgetPropertyValue(temp);
        }
        else {
            double price = przelicz(inwestor,aktywo,quantity);
            aktywo.setQuantityPropertyValue(aktywo.getQuantityPropertyValue() - quantity);
            double temp = RandomModule.round(inwestor.getBudgetPropertyValue() + RandomModule.round(price)/100)/100;
            inwestor.setBudgetPropertyValue(temp);
        }
    }


    // POZOSTALE
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(new ArrayList<>(currencyList));
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        currencyList = FXCollections.observableArrayList((ArrayList<Waluta>) ois.readObject());
    }
}
