package pl.symulacja.gieldy.controllers.add;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import pl.symulacja.gieldy.interfaces.Controller;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.gielda.GieldaPapierowWartosciowych;
import pl.symulacja.gieldy.data.kraj.Kraj;
import pl.symulacja.gieldy.exceptions.AddItemException;
import pl.symulacja.gieldy.exceptions.WrongKindException;
import pl.symulacja.gieldy.utils.DialogsUtils;
import pl.symulacja.gieldy.utils.RandomModule;
import java.util.Random;

/**
 * Kontroler pliku add_stock_exchange.fxml
 * @author Paweł
 */
public class AddStockExchangeController implements Controller{
    private MainDataClass mainData;
    @FXML private Button add_button;
    @FXML private TextField nameTextField;
    @FXML private TextField marzaTextField;
    @FXML private ComboBox<Kraj> countryComboBox;
    @FXML private TextField miastoTextField;
    @FXML private TextField ulicaTextField;
    private GieldaPapierowWartosciowych newGielda = new GieldaPapierowWartosciowych();
    private BooleanProperty nameBooleanProperty = new SimpleBooleanProperty(this,"nameBooleanProperty");
    private BooleanProperty marzaBooleanProperty = new SimpleBooleanProperty(this,"marzaBooleanProperty");
    private BooleanProperty krajBooleanProperty = new SimpleBooleanProperty(this,"krajBooleanProperty",true);
    private BooleanProperty miastoBooleanProperty = new SimpleBooleanProperty(this,"miastoBooleanProperty");
    private BooleanProperty ulicaBooleanProperty = new SimpleBooleanProperty(this,"ulicaBooleanProperty");
    private ObservableList<Kraj> krajObservableList = FXCollections.observableArrayList();

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        //  NAME
        nameTextField.textProperty().bindBidirectional(newGielda.getNameProperty());
        //  MARZA
        nameBooleanProperty.bind(newGielda.getNameProperty().isEmpty());
        marzaTextField.disableProperty().bind(nameBooleanProperty);
        //  KRAJ
        countryComboBox.setItems(krajObservableList);
        marzaBooleanProperty.bind(marzaTextField.textProperty().isEmpty().or(nameBooleanProperty));
        countryComboBox.disableProperty().bind(marzaBooleanProperty);
        //  MIASTO
        miastoTextField.disableProperty().bind(krajBooleanProperty.or(nameBooleanProperty));
        miastoTextField.textProperty().bindBidirectional(newGielda.getTownProperty());
        //  ULICA
        miastoBooleanProperty.bind(newGielda.getTownProperty().isEmpty().or(krajBooleanProperty));
        ulicaTextField.disableProperty().bind(miastoBooleanProperty);
        ulicaTextField.textProperty().bindBidirectional(newGielda.getStreetProperty());
        //  BUTTON
        ulicaBooleanProperty.bind(newGielda.getStreetProperty().isEmpty().or(miastoBooleanProperty));
        add_button.disableProperty().bind(ulicaBooleanProperty);
    }


    //  OBSLUGA PRZYCISKOW
    /**
     * Dodaje giełdę papierów wartościowych po zatwierdzeniu przez użytkownika i sprawdzeniu wszytskich warunków
     */
    @FXML public void dodajButtonOnAction() {
        try {
            GieldaPapierowWartosciowych gielda = new GieldaPapierowWartosciowych();
            sprawdzDostepnoscGieldy();
            gielda.setNamePropertyValue(newGielda.getNamePropertyValue());
            sprawdzPoprawnoscMarzy();
            gielda.setMarzaPropertyValue(zaokraglij(Double.parseDouble(marzaTextField.getText())));
            gielda.setCountry(countryComboBox.getValue());
            gielda.setCurrency(gielda.getCountry().getWaluta());
            gielda.setTownPropertyValue(miastoTextField.getText());
            gielda.setStreetPropertyValue(ulicaTextField.getText());
            mainData.getStockExchangeList().add(gielda);
            clearAllTextFields();
        }
        catch (AddItemException e){
            DialogsUtils.errorDialog(e.getWiadomosc());
        }
        catch (WrongKindException e1) {
            DialogsUtils.errorDialog(e1.getWiadomosc());
            marzaTextField.clear();
        }
    }

    /**
     * Losuje dane do tworzonego obiektu po naciśnieciu przycisku
     */
    @FXML public void losujButtonOnAction() {
        nameTextField.setText(RandomModule.randName("stockExchange"));
        marzaTextField.setText(wyznaczMarze());
        miastoTextField.setText(RandomModule.randName("town"));
        ulicaTextField.setText(RandomModule.randName("street"));
    }

    /**
     * Dodaje Spółkę po zatwierdzeniu przez użytkownika i sprawdzeniu wszytskich warunków
     */
    @FXML public void cancelButtonOnAction() {
        clearAllTextFields();
    }

    //  OBSLUGA COMBOBOX
    /**
     * Aktywuje następne pole do uzupełnienia
     */
    @FXML public void countryComboBoxOnAction() {
        if (countryComboBox.getValue() != null)
            krajBooleanProperty.setValue(false);
        else
            krajBooleanProperty.setValue(true);
    }


    //  SPRAWDZANIE POPRAWNOSCI WPROWADZONYCH DANYCH

    /**
     * Sprawdza możliwośc dodania giełdy
     * @throws AddItemException Istenieje już giełda o tej nazwie
     */
    private void sprawdzDostepnoscGieldy() throws AddItemException {
        for (GieldaPapierowWartosciowych g: mainData.getStockExchangeList()) {
            if (g.getNamePropertyValue().equals(newGielda.getNamePropertyValue()))
                throw new AddItemException("Giełda o podanej nazwie już istnieje!");
        }
    }

    /**
     * Sprawdza poprawność wprowadzonej marży przez użytkownika
     * @throws WrongKindException Błedna marża
     */
    private void sprawdzPoprawnoscMarzy() throws WrongKindException{
        try {
            double x = Double.parseDouble(marzaTextField.getText());
            if (x > 0.2 || x < 0.01)
                throw new WrongKindException("Proszę podać wartość liczbową z przedziału 0,01 do 0,2!");
        }
        catch (Exception e){
            throw new WrongKindException("Proszę podać wartość liczbową z przedziału 0,01 do 0,2!");
        }
    }


    //  WYZNACZANIE I ZAOKRĄGLANIE MARZY

    /**
     * Zaokrągla marżę
     * @param v Wartość do zaokrąglenia
     * @return Zaokrąglona marżą
     */
    private double zaokraglij(double v) {
        v *= 100;
        v = Math.round(v);
        v /= 100;
        return v;
    }

    /**
     * Wyznacza marżę
     * @return Marżą
     */
    private String wyznaczMarze(){
        Random random = new Random();
        double temp = random.nextDouble();
        temp /= 5;
        if (temp < 0.01)
            temp *= 10;
        Double x = zaokraglij(temp);
        return x.toString();
    }


    // DODATKOWE METODY POPRAWIAJACE CZYTELNOSC KODU
    /**
     * Czyszczenie wszystkich pól wprowadzania danych
     */
    private void clearAllTextFields() {
        nameTextField.clear();
        marzaTextField.clear();
        countryComboBox.setValue(null);
        miastoTextField.clear();
        ulicaTextField.clear();
    }

    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData) {
        this.mainData = mainData;
        krajObservableList.setAll(this.mainData.getCountryList());
    }
}
