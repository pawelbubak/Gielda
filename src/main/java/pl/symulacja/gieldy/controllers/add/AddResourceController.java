package pl.symulacja.gieldy.controllers.add;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import pl.symulacja.gieldy.interfaces.Controller;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.aktywo.Surowiec;
import pl.symulacja.gieldy.exceptions.WrongKindException;
import pl.symulacja.gieldy.utils.DialogsUtils;
import pl.symulacja.gieldy.utils.RandomModule;
import java.util.ArrayList;

/**
 * Kontroler pliku add_resource.fxml
 * @author Paweł
 */
public class AddResourceController implements Controller{
    private MainDataClass mainData;
    @FXML private Button add_button;
    @FXML private TextField nameTextField;
    @FXML private TextField cenaTextField;
    @FXML private ComboBox<String> jednostkaComboBox;
    private Surowiec newSurowiec = new Surowiec();
    private BooleanProperty nameBooleanProperty = new SimpleBooleanProperty(this,"booleanProperty",true);
    private BooleanProperty priceBooleanProperty = new SimpleBooleanProperty(this,"booleanProperty",true);
    private BooleanProperty lastBooleanProperty = new SimpleBooleanProperty(this,"booleanProperty",true);
    private DoubleProperty priceProperty = new SimpleDoubleProperty(this,"priceProperty",0);
    private ObservableList<String> jednostkaObservableList = FXCollections.observableArrayList();

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        init();
        //  NAME TEXTFIELD
        nameTextField.textProperty().bindBidirectional(newSurowiec.getNameProperty());
        //  PRICE TEXTFIELD
        nameBooleanProperty.bind(newSurowiec.getNameProperty().isEmpty());
        cenaTextField.disableProperty().bind(nameBooleanProperty);
        StringConverter converter = new NumberStringConverter();
        cenaTextField.textProperty().bindBidirectional(priceProperty, converter);
        //  TRADEUNIT TEXTFIELD
        priceBooleanProperty.bind(priceProperty.lessThanOrEqualTo(0).or(nameBooleanProperty));
        jednostkaComboBox.disableProperty().bind(priceBooleanProperty);
        jednostkaComboBox.setItems(jednostkaObservableList);
        //  ACCEPT BUTTON
        lastBooleanProperty.bind(newSurowiec.getJednostkaHandlowaProperty().isEmpty().or(priceBooleanProperty));
        add_button.disableProperty().bind(lastBooleanProperty);
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
        nameTextField.setText(RandomModule.randName("resource"));
        cenaTextField.setText((RandomModule.randName("price")));
    }

    /**
     * Dodaje Surowiec po zatwierdzeniu przez użytkownika i sprawdzeniu wszytskich warunków
     */
    @FXML public void dodajButtonOnAction() {
        try {
            Surowiec surowiec = new Surowiec();
            surowiec.setNamePropertyValue(nameTextField.getText());
            sprawdzPoprawnoscCeny();
            surowiec.setNewPrice(Double.parseDouble(cenaTextField.getText()));
            surowiec.setJednostkaHandlowaPropertyValue(jednostkaComboBox.getValue());
            mainData.getResourceList().add(surowiec);
            mainData.getRynekSurowcow().getResourcesList().add(surowiec);
            mainData.getGlobalStockList().add(surowiec);
            surowiec.setStockExchange(mainData.getRynekSurowcow());
            clearAllTextFields();
        } catch (WrongKindException e) {
            DialogsUtils.errorDialog(e.getWiadomosc());
        }
    }

    //  OBSLUGA COMBOBOX
    /**
     * Pobiera rodzaj jednostki handlowej
     */
    @FXML public void jednostkaComboBoxOnAction() {
        newSurowiec.setJednostkaHandlowaPropertyValue(jednostkaComboBox.getSelectionModel().getSelectedItem());
    }

    //  SPRAWDZENIE MOZLIWOSCI UZYCIA WPROWADZONYCH DANYCH PRZEZ UZYTKOWNIKA
    /**
     * Sprawdza poprawność wprowadzonej przez użytkownika ceny
     * @throws WrongKindException Błędna cena
     */
    private void sprawdzPoprawnoscCeny() throws WrongKindException {
        try {
            double x = Double.parseDouble(cenaTextField.getText());
            if (x <= 0)
                throw new WrongKindException("Proszę podać wartość liczbową większą od 0!");
        }
        catch (Exception e){
            throw new WrongKindException("Proszę podać wartość liczbową większą od 0!");
        }
    }


    // DODATKOWE METODY POPRAWIAJACE CZYTELNOSC KODU
    /**
     * Czyszczenie wszystkich pól wprowadzania danych
     */
    private void clearAllTextFields() {
        nameTextField.clear();
        cenaTextField.clear();
        jednostkaComboBox.setValue("kg");
    }

    /**
     * Tworzenie listy i inicjalizacja nią ComboBoxa
     */
    private void init(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("uncja");
        arrayList.add("g");
        arrayList.add("dag");
        arrayList.add("kg");
        arrayList.add("t");
        jednostkaObservableList.setAll(arrayList);
        jednostkaComboBox.setValue("kg");
        newSurowiec.setJednostkaHandlowaPropertyValue(jednostkaComboBox.getValue());
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
