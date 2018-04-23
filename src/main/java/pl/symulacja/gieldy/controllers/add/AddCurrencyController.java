package pl.symulacja.gieldy.controllers.add;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import pl.symulacja.gieldy.interfaces.Controller;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.aktywo.Waluta;
import pl.symulacja.gieldy.data.kraj.Kraj;
import pl.symulacja.gieldy.exceptions.AddItemException;
import pl.symulacja.gieldy.utils.DialogsUtils;
import pl.symulacja.gieldy.utils.RandomModule;

/**
 * Kontroler pliku add_currency.fxml
 * @author Paweł
 */
public class AddCurrencyController implements Controller{
    private MainDataClass mainData;
    @FXML private Button add_button;
    @FXML private TextField nameTextField;
    @FXML private TextField krajTextField;
    private Waluta newWaluta = new Waluta();
    private BooleanProperty nameBooleanProperty = new SimpleBooleanProperty(this,"nameBooleanProperty");
    private BooleanProperty countryBooleanProperty = new SimpleBooleanProperty(this,"countryBooleanProperty");
    private Kraj newCountry = new Kraj();

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        //  NAME
        nameTextField.textProperty().bindBidirectional(newWaluta.getNameProperty());
        //  COUNTRY
        nameBooleanProperty.bind(nameTextField.textProperty().isEmpty());
        krajTextField.disableProperty().bind(nameBooleanProperty);
        krajTextField.textProperty().bindBidirectional(newCountry.getNameProperty());
        //  BUTTON
        countryBooleanProperty.bind(krajTextField.textProperty().isEmpty().or(nameBooleanProperty));
        add_button.disableProperty().bind(countryBooleanProperty);
    }


    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Czyści pola przyjmujące dane od użytkownika po naciśnięciu przycisku
     */
    @FXML public void cancelButtonOnAction() {
        clearAllTextFields();
    }

    /**
     * Losuje dane do tworzonego obiektu po naciśnieciu przycisku
     */
    @FXML public void losujButtonOnAction() {
        nameTextField.setText(RandomModule.randName("currency"));
        krajTextField.setText(RandomModule.randName("country"));
    }

    /**
     * Dodaje Walutę po zatwierdzeniu przez użytkownika i sprawdzeniu wszytskich warunków
     */
    @FXML public void dodajButtonOnAction() {
        try {
            sprawdzDostepnoscWaluty();
            sprawdzDostepnoscKraju();
            Waluta w = new Waluta();
            w.setNamePropertyValue(newWaluta.getNamePropertyValue());
            Kraj k = new Kraj();
            k.setNamePropertyValue(newCountry.getNamePropertyValue());
            k.setWaluta(w);
            w.getCountryList().add(k);
            Waluta.setValue(w);
            w.setStockExchange(mainData.getRynekWalut());
            mainData.getCurrencyList().add(w);
            mainData.getCountryList().add(k);
            mainData.getRynekWalut().getCurrencyList().add(w);
            mainData.getGlobalStockList().add(w);
            clearAllTextFields();
        } catch (AddItemException e) {
            try {
                if (e.getWiadomosc() != null)
                    wywolajException(e);
                sprawdzDostepnoscKraju();
                Kraj k = new Kraj();
                k.setNamePropertyValue(newCountry.getNamePropertyValue());
                k.setWaluta((Waluta)e.getObject());
                ((Waluta) e.getObject()).getCountryList().add(k);
                mainData.getCountryList().add(k);
                clearAllTextFields();
            } catch (AddItemException e1) {
                DialogsUtils.errorDialog(e1.getWiadomosc());
            }
        }
    }

    //  SPRAWDZENIE MOZLIWOSCI UZYCIA WPROWADZONYCH DANYCH PRZEZ UZYTKOWNIKA
    /**
     * Sprawdza możliwość dodania waluty
     * @throws AddItemException Podana waluta już istnieje
     */
    private void sprawdzDostepnoscWaluty() throws AddItemException {
        for (Waluta w: mainData.getCurrencyList()){
            if (w.getNamePropertyValue().equals(newWaluta.getNamePropertyValue()))
                throw new AddItemException(w);
        }
    }

    /**
     * Sprawdza poprawność przypisania kraju
     * @throws AddItemException Kraj i tej nazwie posiada już inną Walutę
     */
    private void sprawdzDostepnoscKraju() throws AddItemException {
        for (Kraj k: mainData.getCountryList()){
            if (k.getNamePropertyValue().equals(newCountry.getNamePropertyValue()))
                throw new AddItemException("Kraj o takiej nazwie posiada już walute.");
        }
    }

    /**
     * Wywołuje błąd, żeby przekazać wiadomość
     * @param e Błąd zawierający ważną wiadomość
     * @throws AddItemException Przekazanie wiadomości
     */
    private void wywolajException(AddItemException e) throws AddItemException {
        throw new AddItemException(e.getWiadomosc());
    }

    // DODATKOWE METODY POPRAWIAJACE CZYTELNOSC KODU
    /**
     * Czyszczenie wszystkich pól wprowadzania danych
     */
    private void clearAllTextFields() {
        nameTextField.clear();
        krajTextField.clear();
    }

    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData) {
        this.mainData = mainData;
    }
}
