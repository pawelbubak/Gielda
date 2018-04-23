package pl.symulacja.gieldy.data.gielda;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.aktywo.Surowiec;
import pl.symulacja.gieldy.data.podmiot.Podmiot;
import pl.symulacja.gieldy.utils.RandomModule;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Klasa imitująca Rynek Surowców - Rozszerzenie giełdy, dodatkowo zawiera listę Surowców
 * @author Paweł
 */
public class RynekSurowcow extends Gielda implements Serializable{
    transient private ObservableList<Surowiec> resourcesList = FXCollections.observableArrayList();

    /**
     * Zwraca listę surowców
     * @return Lista surowców
     */
    public ObservableList<Surowiec> getResourcesList() {
            return resourcesList;
    }

    /**
     * Zwraca listę surowców
     * @return Lista surowców
     */
    @Override
    public ObservableList getList() {
        return getResourcesList();
    }

    /**
     * Zlecenie kupna składane przez podmiot - zrealizowane zwraca mu pakiet Surowców
     * @param inwestor Podmiot kupujący
     * @param aktywo Wybrany surowiec
     * @param quantity Ilość aktywa do kupienia
     */
    @Override
    synchronized public void zlecenieKupna(Podmiot inwestor, Aktywo aktywo, int quantity) {
        double price, totalPrice, investorBudget;
        price = przelicz(inwestor,aktywo,quantity);
        totalPrice = przliczMarze(price);
        investorBudget = inwestor.getBudgetPropertyValue();
        if (totalPrice <= inwestor.getBudgetPropertyValue()){
            double temp = RandomModule.round(investorBudget - RandomModule.round(totalPrice)/100)/100;
            inwestor.setBudgetPropertyValue(temp);
            inwestor.dodajAktywo(wydzielSurowce((Surowiec)aktywo,quantity));
        }
        else {
            kupnoZleceniaGdyMaloSrodkow(inwestor, aktywo);
        }
    }

    /**
     * Wyznacza ilość surowców do kupienia gdy podmiotu nie stać na wybraną przez siebie ilość
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
            inwestor.dodajAktywo(wydzielSurowce((Surowiec) aktywo, quantity));
        }
    }

    /**
     * Przydziela pakiet jednostek surowca kupującemu
     * @param aktywo Wybrany surowiec
     * @param quantity Ilość surowca
     * @return Paczka jednostek wybranego surowca
     */
    private Aktywo wydzielSurowce(Surowiec aktywo, int quantity) {
        Surowiec surowiec = new Surowiec();
        surowiec.setNameProperty(aktywo.getNameProperty());
        surowiec.setPriceProperty(aktywo.getPriceProperty());
        surowiec.setMaxPriceProperty(aktywo.getMaxPriceProperty());
        surowiec.setMinPriceProperty(aktywo.getMinPriceProperty());
        surowiec.setQuantityProperty(new SimpleIntegerProperty(surowiec,"surowiec",quantity));
        surowiec.setChartData(aktywo.getChartData());
        surowiec.setJednostkaHandlowaProperty(aktywo.getJednostkaHandlowaProperty());
        surowiec.setStockExchange(aktywo.getStockExchange());
        return surowiec;
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
            double price = przelicz(inwestor, aktywo, quantity);
            aktywo.setQuantityPropertyValue(aktywo.getQuantityPropertyValue() - quantity);
            double temp = RandomModule.round(inwestor.getBudgetPropertyValue() + RandomModule.round(price) / 100) / 100;
            inwestor.setBudgetPropertyValue(temp);
        }
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(new ArrayList<>(resourcesList));
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        resourcesList = FXCollections.observableArrayList((ArrayList<Surowiec>)ois.readObject());
    }
}
