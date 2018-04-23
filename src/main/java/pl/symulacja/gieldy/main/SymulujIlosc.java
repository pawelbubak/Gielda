package pl.symulacja.gieldy.main;

import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.aktywo.JednostkaFunduszu;
import pl.symulacja.gieldy.data.podmiot.FunduszInwestycyjny;
import pl.symulacja.gieldy.data.podmiot.Inwestor;
import pl.symulacja.gieldy.data.spolka.Spolka;
import pl.symulacja.gieldy.utils.DialogsUtils;
import pl.symulacja.gieldy.utils.RandomModule;

import java.util.Random;

/**
 * Pomocnicza klasa dodająca inwestorów i fundusze
 * @author Paweł
 */
public class SymulujIlosc implements Runnable{
    private MainDataClass mainData;

    SymulujIlosc(MainDataClass mainData) {
        this.mainData = mainData;
    }

    /**
     * Dodaje podmioty według wzoru (ilosc surowcow + ilosc walut) * 2 / 3 + suma wszystkich jednostek akcji / 2500 + suma wszystkich jednostek funduszy / 1500
     */
    @Override
    public void run() {
        long walutaQuantity = 0, surowiecQuantity = 0, akcjeQuantity = 0, funduszQuantity = 0, zapotrzebowanie = 0;
        int aktualnie = 0;
        while (true) {
            while (SymulacjaGieldy.symuluj) {
                akcjeQuantity = 0; funduszQuantity = 0;
                aktualnie = mainData.getInvestorList().size() + mainData.getFundList().size();
                for(Spolka spolka: mainData.getCompanyList()){
                    akcjeQuantity += spolka.getQuantityPropertyValue();
                }
                for (JednostkaFunduszu jednostka: mainData.getFundUnitList()){
                    funduszQuantity += jednostka.getQuantityPropertyValue();
                }
                surowiecQuantity = mainData.getResourceList().size();
                walutaQuantity =  mainData.getCurrencyList().size();
                zapotrzebowanie = (walutaQuantity + surowiecQuantity) * 2 / 3;
                zapotrzebowanie += akcjeQuantity / 2500 + funduszQuantity / 1500;
                while (zapotrzebowanie > aktualnie){
                    dodajPodmiot(aktualnie);
                    aktualnie++;
                }
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    DialogsUtils.errorDialog(e.getMessage());
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                DialogsUtils.errorDialog(e.getMessage());
            }
        }
    }

    /**
     * Sprawdza jakiego typu ma być dodawany podmiot - zakladamy ze 65% podmiotów to inwestorzy
     * @param aktualnie aktualna liczba podmiotów
     */
    private void dodajPodmiot(int aktualnie) {
        if (aktualnie != 0) {
            if (1.0 * mainData.getInvestorList().size() / aktualnie < 0.65) {
                dodajInwestora();
            } else
                dodajFundusz();
        }
        else
            dodajInwestora();
    }

    /**
     * Dodaje nowy fundusz
     */
    private void dodajFundusz() {
        Random random = new Random();
        int choose = random.nextInt(mainData.getCurrencyList().size());
        FunduszInwestycyjny funduszInwestycyjny = new FunduszInwestycyjny();
        funduszInwestycyjny.setFundNamePropertyValue(RandomModule.randName("fund"));
        funduszInwestycyjny.setNamePropertyValue(RandomModule.randName("name"));
        funduszInwestycyjny.setSurnamePropertyValue(RandomModule.randName("surname"));
        funduszInwestycyjny.setDomyslnaWaluta(mainData.getCurrencyList().get(choose));
        funduszInwestycyjny.setBudgetPropertyValue(Double.parseDouble(RandomModule.randName("budget")));
        funduszInwestycyjny.wypuscUdzialy(mainData);
        mainData.getFundList().add(funduszInwestycyjny);
        new Thread(funduszInwestycyjny).start();
    }

    /**
     * Dodaje nowego inwestora
     */
    private void dodajInwestora() {
        Random random = new Random();
        int choose = random.nextInt(mainData.getCurrencyList().size());
        Inwestor inwestor = new Inwestor();
        inwestor.setNamePropertyValue(RandomModule.randName("name"));
        inwestor.setSurnamePropertyValue(RandomModule.randName("surname"));
        inwestor.setPESELPropertyValue(RandomModule.randName("pesel"));
        inwestor.setDomyslnaWaluta(mainData.getCurrencyList().get(choose));
        inwestor.setBudgetPropertyValue(Double.parseDouble(RandomModule.randName("budget")));
        mainData.getInvestorList().add(inwestor);
        new Thread(inwestor).start();
    }
}
