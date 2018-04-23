package pl.symulacja.gieldy.controllers.remove;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import pl.symulacja.gieldy.interfaces.Controller;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.aktywo.Waluta;
import pl.symulacja.gieldy.data.gielda.GieldaPapierowWartosciowych;
import pl.symulacja.gieldy.data.podmiot.FunduszInwestycyjny;
import pl.symulacja.gieldy.data.podmiot.Inwestor;
import pl.symulacja.gieldy.exceptions.DeleteItemException;
import pl.symulacja.gieldy.utils.DialogsUtils;

/**
 * Kontroler pliku remove_currency.fxml
 * @author Paweł
 */
public class RemoveCurrencyController implements Controller{
    private MainDataClass mainData;
    @FXML private Button accept_button;
    @FXML private ComboBox<Waluta> walutaComboBox;
    private BooleanProperty chooseBooleanProperty = new SimpleBooleanProperty(this,"choose",true);

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        accept_button.disableProperty().bind(chooseBooleanProperty);
    }


    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Zatwierdzenie usunięcia obiektu
     */
    @FXML public void acceptButtonOnAction() {
       try {
           sprawdzMozliwoscUsuniecia();
           walutaComboBox.getValue().getCountryList().forEach(kraj -> mainData.getCountryList().remove(kraj));
           mainData.getRynekWalut().getList().remove(walutaComboBox.getValue());
           mainData.getGlobalStockList().remove(walutaComboBox.getValue());
           mainData.getCurrencyList().remove(walutaComboBox.getValue());
       } catch (DeleteItemException e) {
           DialogsUtils.errorDialog(e.getWiadomosc());
       }
    }


    //  SPRAWDZENIE MOZLIWOSCI UZYCIA WPROWADZONYCH DANYCH PRZEZ UZYTKOWNIKA
    /**
     * Sprawdzenie czy można usunąć walute
     * @throws DeleteItemException Nie można usunąć waluty
     */
    private void sprawdzMozliwoscUsuniecia() throws DeleteItemException {
        for (Inwestor inwestor: mainData.getInvestorList()){
            if (inwestor.getDomyslnaWaluta().equals(walutaComboBox.getValue()))
                throw new DeleteItemException("Nie można usunąć waluty, ponieważ jest usuwana.");
        }
        for (FunduszInwestycyjny funduszInwestycyjny: mainData.getFundList())
            if (funduszInwestycyjny.getDomyslnaWaluta().equals(walutaComboBox.getValue()))
                throw new DeleteItemException("Nie można usunąć waluty, ponieważ jest usuwana.");
        for (GieldaPapierowWartosciowych gieldaPapierowWartosciowych: mainData.getStockExchangeList())
            if (gieldaPapierowWartosciowych.getCurrency().equals(walutaComboBox.getValue()))
                throw new DeleteItemException("Nie można usunąć waluty, ponieważ jest usuwana.");
        if (walutaComboBox.getValue().getNamePropertyValue().equals("PLN"))
            throw new DeleteItemException("Nie można usunąć tej waluty");
    }


    //  OBSLUGA COMBOBOX
    /**
     * Aktywacja przycisku usuwania
     */
    @FXML public void walutaComboBoxOnAction() {
        if (walutaComboBox.getValue() != null)
            chooseBooleanProperty.set(false);
        else
            chooseBooleanProperty.set(true);
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData) {
        this.mainData = mainData;
        walutaComboBox.setItems(mainData.getCurrencyList());
    }
}
