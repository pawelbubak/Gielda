package pl.symulacja.gieldy.main;

import javafx.collections.FXCollections;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.aktywo.JednostkaFunduszu;
import pl.symulacja.gieldy.data.aktywo.Surowiec;
import pl.symulacja.gieldy.data.aktywo.Waluta;
import pl.symulacja.gieldy.data.gielda.*;
import pl.symulacja.gieldy.data.indeks.Indeks;
import pl.symulacja.gieldy.data.kraj.Kraj;
import pl.symulacja.gieldy.data.podmiot.FunduszInwestycyjny;
import pl.symulacja.gieldy.data.podmiot.Inwestor;
import pl.symulacja.gieldy.data.spolka.Spolka;
import pl.symulacja.gieldy.utils.DialogsUtils;
import java.io.*;
import java.util.ArrayList;

/**
 * Pomocnicza klasa serializująca dane
 * @author Paweł
 */
public class SerializeProgram {
    private static final String SAVE = "symulacja.bin";

    /**
     * Zapisuje dane do pliku
     * @param mainDataClass Instancja z danymi
     */
    public static void save(MainDataClass mainDataClass){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(SAVE)));
            outputStream.writeObject(new ArrayList<>(mainDataClass.getCountryList()));
            outputStream.writeObject(new ArrayList<>(mainDataClass.getCurrencyList()));
            outputStream.writeObject(new ArrayList<>(mainDataClass.getCompanyList()));
            outputStream.writeObject(new ArrayList<>(mainDataClass.getInvestorList()));
            outputStream.writeObject(new ArrayList<>(mainDataClass.getFundList()));
            outputStream.writeObject(new ArrayList<>(mainDataClass.getIndexList()));
            outputStream.writeObject(new ArrayList<>(mainDataClass.getResourceList()));
            outputStream.writeObject(new ArrayList<>(mainDataClass.getStockExchangeList()));
            outputStream.writeObject(mainDataClass.getRynekSurowcow());
            outputStream.writeObject(mainDataClass.getRynekWalut());
            outputStream.writeObject(mainDataClass.getRynekFunduszyInwestycyjnych());
            outputStream.writeObject(new ArrayList<>(mainDataClass.getGlobalStockList()));
            outputStream.writeObject(new ArrayList<>(mainDataClass.getFundUnitList()));
            outputStream.close();
        } catch (IOException e) {
            DialogsUtils.errorDialog("Nie można zapisać danych!");
        }

    }

    /**
     * Odczytuje z pliku zapisane dane
     * @param mainDataClass Instancja z danymi
     */
    static void load(MainDataClass mainDataClass){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(SAVE)));
            mainDataClass.setCountryList(FXCollections.observableArrayList((ArrayList<Kraj>) inputStream.readObject()));
            mainDataClass.setCurrencyList(FXCollections.observableArrayList((ArrayList<Waluta>) inputStream.readObject()));
            mainDataClass.setCompanyList(FXCollections.observableArrayList((ArrayList<Spolka>) inputStream.readObject()));
            mainDataClass.setInvestorList(FXCollections.observableArrayList((ArrayList<Inwestor>) inputStream.readObject()));
            mainDataClass.setFundList(FXCollections.observableArrayList((ArrayList<FunduszInwestycyjny>) inputStream.readObject()));
            mainDataClass.setIndexList(FXCollections.observableArrayList((ArrayList<Indeks>) inputStream.readObject()));
            mainDataClass.setResourceList(FXCollections.observableArrayList(FXCollections.observableArrayList((ArrayList<Surowiec>) inputStream.readObject())));
            mainDataClass.setStockExchangeList(FXCollections.observableArrayList((ArrayList<GieldaPapierowWartosciowych>) inputStream.readObject()));
            mainDataClass.setRynekSurowcow((RynekSurowcow) inputStream.readObject());
            mainDataClass.setRynekWalut((RynekWalut) inputStream.readObject());
            mainDataClass.setRynekFunduszyInwestycyjnych((RynekFunduszyInwestycyjnych) inputStream.readObject());
            mainDataClass.setGlobalStockList(FXCollections.observableArrayList((ArrayList<Aktywo>) inputStream.readObject()));
            mainDataClass.setFundUnitList(FXCollections.observableArrayList((ArrayList<JednostkaFunduszu>) inputStream.readObject()));
        } catch (IOException e) {
            DialogsUtils.errorDialog("Błąd podczas wczytywania pliku!");
            wczytajRynki(mainDataClass);
        } catch (ClassNotFoundException e) {
            DialogsUtils.errorDialog("Nie znaleziono danych!");
        }
    }

    /**
     * Wczytuje podstawowe dane jeśli zapis ulegnie zniszczeniu
     * @param mainData Instancja z danymi
     */
    private static void wczytajRynki(MainDataClass mainData) {
        RynekSurowcow rynekSurowcow = new RynekSurowcow();
        RynekWalut rynekWalut = new RynekWalut();
        rynekSurowcow.setNamePropertyValue("Ogólnoświatowy Rynek Surowców");
        rynekWalut.setNamePropertyValue("Ogólnoświatowy Rynek Walut");
        rynekWalut.setMarzaPropertyValue(0.04);
        rynekSurowcow.setMarzaPropertyValue(0.025);
        mainData.setRynekWalut(rynekWalut);
        mainData.setRynekSurowcow(rynekSurowcow);
        RynekFunduszyInwestycyjnych rynekFunduszyInwestycyjnych = new RynekFunduszyInwestycyjnych();
        rynekFunduszyInwestycyjnych.setNamePropertyValue("Ogólnoświatowy Rynek Funduszy Inwestycyjnych");
        rynekFunduszyInwestycyjnych.setMarzaPropertyValue(0.01);
        mainData.setRynekFunduszyInwestycyjnych(rynekFunduszyInwestycyjnych);
        Kraj kraj = new Kraj();
        kraj.setNamePropertyValue("Polska");
        Waluta waluta = new Waluta();
        kraj.setWaluta(waluta);
        waluta.setNamePropertyValue("PLN");
        waluta.setPricePropertyValue(1.0);
        waluta.setMaxPricePropertyValue(1.0);
        waluta.setMinPricePropertyValue(1.0);
        waluta.setQuantityPropertyValue(0);
        waluta.getChartData().add(1.0);
        waluta.getCountryList().add(kraj);
        waluta.setStockExchange(rynekWalut);
        rynekWalut.getCurrencyList().add(waluta);
        mainData.getGlobalStockList().add(waluta);
        mainData.getCurrencyList().add(waluta);
    }
}
