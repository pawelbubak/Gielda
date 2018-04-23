package pl.symulacja.gieldy.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.io.Serializable;

/**
 * Główna klasa z danymi - instancja tej klasy przechowuje wszytskie dane używane w programie
 * @author Paweł
 */
public class MainDataClass implements Serializable {
    private ObservableList<Kraj> countryList = FXCollections.observableArrayList();
    private ObservableList<Waluta> currencyList = FXCollections.observableArrayList();
    private ObservableList<Inwestor> investorList = FXCollections.observableArrayList();
    private ObservableList<FunduszInwestycyjny> fundList = FXCollections.observableArrayList();
    private ObservableList<GieldaPapierowWartosciowych> stockExchangeList = FXCollections.observableArrayList();
    private ObservableList<Surowiec> resourceList = FXCollections.observableArrayList();
    private ObservableList<Indeks> indexList = FXCollections.observableArrayList();
    private ObservableList<Spolka> companyList = FXCollections.observableArrayList();
    private RynekSurowcow rynekSurowcow = new RynekSurowcow();
    private RynekWalut rynekWalut = new RynekWalut();
    private RynekFunduszyInwestycyjnych rynekFunduszyInwestycyjnych = new RynekFunduszyInwestycyjnych();
    private ObservableList<Gielda> marketList = FXCollections.observableArrayList();
    private ObservableList<Aktywo> globalStockList = FXCollections.observableArrayList();
    private ObservableList<JednostkaFunduszu> fundUnitList = FXCollections.observableArrayList();

    public ObservableList<JednostkaFunduszu> getFundUnitList() {
        return fundUnitList;
    }

    public void setFundUnitList(ObservableList<JednostkaFunduszu> fundUnitList) {
        this.fundUnitList = fundUnitList;
    }

    public RynekFunduszyInwestycyjnych getRynekFunduszyInwestycyjnych() {
        return rynekFunduszyInwestycyjnych;
    }

    public void setRynekFunduszyInwestycyjnych(RynekFunduszyInwestycyjnych rynekFunduszyInwestycyjnych) {
        this.rynekFunduszyInwestycyjnych = rynekFunduszyInwestycyjnych;
    }

    public ObservableList<Aktywo> getGlobalStockList() {
        return globalStockList;
    }

    public void setGlobalStockList(ObservableList<Aktywo> globalStockList) {
        this.globalStockList = globalStockList;
    }

    public ObservableList<Gielda> getMarketList() {
        return marketList;
    }

    public RynekSurowcow getRynekSurowcow() {
        return rynekSurowcow;
    }

    public void setRynekSurowcow(RynekSurowcow rynekSurowcow) {
        this.rynekSurowcow = rynekSurowcow;
    }

    public RynekWalut getRynekWalut() {
        return rynekWalut;
    }

    public void setRynekWalut(RynekWalut rynekWalut) {
        this.rynekWalut = rynekWalut;
    }

    public MainDataClass(){}

    public ObservableList<Spolka> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(ObservableList<Spolka> companyList) {
        this.companyList = companyList;
    }

    public ObservableList<Indeks> getIndexList() {
        return indexList;
    }

    public void setIndexList(ObservableList<Indeks> indexList) {
        this.indexList = indexList;
    }

    public ObservableList<Surowiec> getResourceList() {
        return resourceList;
    }

    public void setResourceList(ObservableList<Surowiec> resourceList) {
        this.resourceList = resourceList;
    }

    public ObservableList<GieldaPapierowWartosciowych> getStockExchangeList() {
        return stockExchangeList;
    }

    public void setStockExchangeList(ObservableList<GieldaPapierowWartosciowych> stockExchangeList) {
        this.stockExchangeList = stockExchangeList;
    }

    public ObservableList<FunduszInwestycyjny> getFundList() {
        return fundList;
    }

    public void setFundList(ObservableList<FunduszInwestycyjny> fundList) {
        this.fundList = fundList;
    }

    public ObservableList<Inwestor> getInvestorList() {
        return investorList;
    }

    public void setInvestorList(ObservableList<Inwestor> investorList) {
        this.investorList = investorList;
    }

    public ObservableList<Kraj> getCountryList() {
        return countryList;
    }

    public void setCountryList(ObservableList<Kraj> countryList) {
        this.countryList = countryList;
    }

    public ObservableList<Waluta> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(ObservableList<Waluta> currencyList) {
        this.currencyList = currencyList;
    }

    public void init(){
        marketList.add(rynekSurowcow);
        marketList.add(rynekWalut);
        marketList.add(rynekFunduszyInwestycyjnych);
    }
}
