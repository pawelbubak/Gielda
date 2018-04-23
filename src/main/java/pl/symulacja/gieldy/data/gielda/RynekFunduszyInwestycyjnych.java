package pl.symulacja.gieldy.data.gielda;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.aktywo.JednostkaFunduszu;
import pl.symulacja.gieldy.data.podmiot.FunduszInwestycyjny;
import pl.symulacja.gieldy.data.podmiot.Inwestor;
import pl.symulacja.gieldy.data.podmiot.Podmiot;
import pl.symulacja.gieldy.utils.RandomModule;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Klasa imitująca Rynek Jendostek Funduszy Inwestycyjnych - Rozszerzenie giełdy, dodatkowo zawiera listę Jednostek Funduszy
 * @author Paweł
 */
public class RynekFunduszyInwestycyjnych extends Gielda implements Serializable{
    transient private ObservableList<JednostkaFunduszu> fundList = FXCollections.observableArrayList();

    /**
     * Zwraca listę jednostek funduszy
     * @return Lista jednostek funduszy
     */
    public ObservableList<JednostkaFunduszu> getFundList() {
        return fundList;
    }

    /**
     * Zwraca listę jednostek funduszy
     * @return Lista jednostek funduszy
     */
    @Override
    public ObservableList getList() {
        return fundList;
    }

    /**
     * Zlecenie kupna składane przez podmiot - zrealizowane zwraca mu pakiet Jednostek Funduszu z zabezpieczeniem usuwania jednostek funduszu usuniętego
     * @param inwestor Podmiot kupujący
     * @param aktywo Wybrane jednostki funduszu
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
                    this.getFundList().remove(aktywo);
                    Aktywo.getMainDataClass().getGlobalStockList().remove(aktywo);
                    // Przypisanie aktywa inwestorowi
                    inwestor.dodajAktywo(aktywo);
                } else {
                    kupnoZleceniaGdyMaloSrodkow((Inwestor) inwestor, aktywo);
                }
            } else {
                price = przelicz(inwestor, aktywo, aktywo.getQuantityPropertyValue());
                totalPrice = przliczMarze(price);
                if (totalPrice <= investorBudget) {
                    // Zmiana budzetu inwestora
                    double temp = RandomModule.round(investorBudget - RandomModule.round(totalPrice) / 100) / 100;
                    inwestor.setBudgetPropertyValue(temp);
                    // Przypisanie aktywa inwestorowi i aktualizacja jego ilości na giełdzie
                    inwestor.dodajAktywo(wydzielJednostki((JednostkaFunduszu) aktywo, quantity));
                    aktywo.setQuantityPropertyValue(aktywo.getQuantityPropertyValue() - quantity);

                } else {
                    kupnoZleceniaGdyMaloSrodkow((Inwestor) inwestor, aktywo);
                }
            }
        }
        else {
            inwestor.getStockList().remove(aktywo);
        }
    }

    /**
     * Wyznacza ilość jednostek funduszu do kupienia gdy podmiotu nie stać na wybraną przez siebie ilość
     * @param inwestor Podmiot kupujący
     * @param aktywo Wybrane aktywo
     */
    private void kupnoZleceniaGdyMaloSrodkow(Inwestor inwestor, Aktywo aktywo) {
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
            double temp = RandomModule.round(investorBudget - RandomModule.round(totalPrice) / 100);
            inwestor.setBudgetPropertyValue(temp);
            // Przypisanie paczki jednostek funduszu do inwestora i aktualizacja jej ilosci na rynku
            inwestor.dodajAktywo(wydzielJednostki((JednostkaFunduszu) aktywo, quantity));
            aktywo.setQuantityPropertyValue(aktywo.getQuantityPropertyValue() - quantity);
        }
    }

    /**
     * Przydziela pakiet jednostek funduszu kupującemu
     * @param aktywo Wybrane jednostki
     * @param quantity Ilość jednostek
     * @return Paczka jednostek uczestnictwa wybranego funduszu
     */
    private Aktywo wydzielJednostki(JednostkaFunduszu aktywo, int quantity) {
        JednostkaFunduszu jednostka = new JednostkaFunduszu();
        jednostka.setNameProperty(aktywo.getNameProperty());
        jednostka.setPriceProperty(aktywo.getPriceProperty());
        jednostka.setMaxPriceProperty(aktywo.getMaxPriceProperty());
        jednostka.setMinPriceProperty(aktywo.getMinPriceProperty());
        jednostka.setQuantityPropertyValue(quantity);
        jednostka.setChartData(aktywo.getChartData());
        jednostka.setOwner(aktywo.getOwner());
        jednostka.setStockExchange(aktywo.getStockExchange());
        jednostka.setUsuwanyProperty(aktywo.getUsuwanyProperty());
        return jednostka;
    }

    /**
     * Zlecenie sprzedaży składane przez podmiot jest realizowane przez rynek - inwestor dostaje pieniądze - z zabezpieczeniem
     * usuwania jednostek usuniętego funduszu
     * @param inwestor Podmiot sprzedający
     * @param aktywo Sprzedawane aktywo
     * @param quantity Ilość aktywa
     */
    @Override
    synchronized public void zlecenieSprzedazy(Podmiot inwestor, Aktywo aktywo, int quantity) {
        if (!aktywo.isUsuwany()) {
            if (quantity >= aktywo.getQuantityPropertyValue()) {
                double price = przelicz(inwestor, aktywo, aktywo.getQuantityPropertyValue());
                inwestor.getStockList().remove(aktywo);
                double temp = RandomModule.round(inwestor.getBudgetPropertyValue() + RandomModule.round(price) / 100) / 100;
                inwestor.setBudgetPropertyValue(temp);
                gieldaOdkupAktywo(aktywo);
            } else {
                double price = przelicz(inwestor, aktywo, quantity);
                aktywo.setQuantityPropertyValue(aktywo.getQuantityPropertyValue() - quantity);
                double temp = RandomModule.round(inwestor.getBudgetPropertyValue() + RandomModule.round(price) / 100) / 100;
                inwestor.setBudgetPropertyValue(temp);
                gieldaOdkupAktywo(wydzielJednostki((JednostkaFunduszu) aktywo, quantity));
            }
        }
        else {
            inwestor.getStockList().remove(aktywo);
        }
    }

    /**
     * Scalanie odkupionych jednostek funduszy w jeden pakiet
     * @param aktywo Pakiet jednostek
     */
    private void gieldaOdkupAktywo(Aktywo aktywo) {
        for (Aktywo a : fundList) {
            if (a.getNamePropertyValue().equals(aktywo.getNamePropertyValue())) {
                a.setQuantityPropertyValue(a.getQuantityPropertyValue() + aktywo.getQuantityPropertyValue());
                return;
            }
        }
        fundList.add((JednostkaFunduszu)aktywo);
        Aktywo.getMainDataClass().getGlobalStockList().add(aktywo);
    }

    /**
     * Wypuszczenie nowych jednostek funduszu i dodanie ich do rynku
     * @param funduszInwestycyjny Fundusz wypuszczający jednostki
     * @param jednostka Jednostka wypuszczona wczesniej przez fundusz
     */
    synchronized public void dodajNoweAkcje(FunduszInwestycyjny funduszInwestycyjny, JednostkaFunduszu jednostka) {
        funduszInwestycyjny.setBudgetPropertyValue(funduszInwestycyjny.getBudgetPropertyValue() + 100 * jednostka.getPricePropertyValue());
        jednostka.setQuantityPropertyValue(jednostka.getQuantityPropertyValue() + 100);
        for (JednostkaFunduszu jednostkaFunduszu: getFundList()){
            if (jednostkaFunduszu.getNamePropertyValue().equals(jednostka.getNamePropertyValue())){
                jednostkaFunduszu.setQuantityPropertyValue(jednostkaFunduszu.getQuantityPropertyValue() + 100);
                return;
            }
        }
        JednostkaFunduszu jednostka2 = new JednostkaFunduszu();
        jednostka2.setNameProperty(jednostka.getNameProperty());
        jednostka2.setPriceProperty(jednostka.getPriceProperty());
        jednostka2.setMaxPriceProperty(jednostka.getMaxPriceProperty());
        jednostka2.setMinPriceProperty(jednostka.getMinPriceProperty());
        jednostka2.setChartData(jednostka.getChartData());
        jednostka2.setQuantityProperty(new SimpleIntegerProperty(jednostka2,"jednostka2", 100));
        jednostka2.setStockExchange(this);
        jednostka2.setOwner(funduszInwestycyjny);
        jednostka2.setUsuwanyProperty(jednostka.getUsuwanyProperty());
    }

    /**
     * Usuwanie jednostek dopieroco usuniętego funduszu
     * @param s Nazwa jednostek
     */
    synchronized public void usunJednostki(String s) {
        JednostkaFunduszu jednostka = null;
        for (JednostkaFunduszu jednostkaFunduszu: getFundList()){
            if (jednostkaFunduszu.getNamePropertyValue().equals(s)) {
                jednostka = jednostkaFunduszu;
                break;
            }
        }
        if (jednostka != null) {
            jednostka.setUsuwanyValue(true);
            for (Inwestor inwestor : Aktywo.getMainDataClass().getInvestorList()) {
                inwestor.getStockList().remove(jednostka);
            }
            Aktywo.getMainDataClass().getFundUnitList().remove(jednostka);
            getFundList().remove(jednostka);
            Aktywo.getMainDataClass().getGlobalStockList().remove(jednostka);
        }
        else {
            for (JednostkaFunduszu jednostkaFunduszu : Aktywo.getMainDataClass().getFundUnitList()) {
                if (jednostkaFunduszu.getNamePropertyValue().equals(s)) {
                    jednostka = jednostkaFunduszu;
                    break;
                }
            }
            if (jednostka != null) {
                jednostka.setUsuwanyValue(true);
                Aktywo.getMainDataClass().getFundUnitList().remove(jednostka);
                getFundList().remove(jednostka);
                Aktywo.getMainDataClass().getGlobalStockList().remove(jednostka);
            }
        }
    }

    // POZOSTALE
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(new ArrayList<>(fundList));
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        fundList = FXCollections.observableArrayList((ArrayList<JednostkaFunduszu>)ois.readObject());
    }
}
