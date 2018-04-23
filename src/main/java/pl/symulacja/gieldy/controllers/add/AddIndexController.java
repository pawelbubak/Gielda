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
import pl.symulacja.gieldy.data.indeks.Indeks;
import pl.symulacja.gieldy.exceptions.AddItemException;
import pl.symulacja.gieldy.utils.DialogsUtils;
import pl.symulacja.gieldy.utils.RandomModule;

/**
 * Kontroler pliku add_index.fxml
 * @author Paweł
 */
public class AddIndexController implements Controller {
    @FXML private Button add_button;
    @FXML private TextField nameTextField;
    @FXML private ComboBox<GieldaPapierowWartosciowych> gieldaComboBox;
    private Indeks newIndeks = new Indeks();
    private BooleanProperty nameBooleanProperty = new SimpleBooleanProperty(this,"nameBooleanProperty");
    private BooleanProperty gieldaBooleanProperty = new SimpleBooleanProperty(this,"gieldaBooleanProperty",true);
    private ObservableList<GieldaPapierowWartosciowych> gieldaObservableList = FXCollections.observableArrayList();
    private GieldaPapierowWartosciowych gielda = null;

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        gieldaComboBox.setItems(gieldaObservableList);
        //  NAME
        nameTextField.textProperty().bindBidirectional(newIndeks.getNameProperty());
        //  STOCKEXCHANGE
        nameBooleanProperty.bind(newIndeks.getNameProperty().isEmpty());
        gieldaComboBox.disableProperty().bind(nameBooleanProperty);
        //  BUTTON
        add_button.disableProperty().bind(gieldaBooleanProperty.or(nameBooleanProperty));
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
        nameTextField.setText(RandomModule.randName("index"));
    }

    /**
     * Dodaje Indeks po zatwierdzeniu przez użytkownika i sprawdzeniu wszytskich warunków
     */
    @FXML public void dodajButtonOnAction() {
        try {
            Indeks indeks = new Indeks();
            indeks.setNamePropertyValue(newIndeks.getNamePropertyValue());
            sprawdzDostepnoscIndeksu();
            gieldaComboBox.getValue().getIndexList().add(indeks);
            clearAllTextFields();
        } catch (AddItemException e) {
            DialogsUtils.errorDialog(e.getWiadomosc());
            nameTextField.clear();
        }
    }

    //  OBSLUGA COMBOBOX
    /**
     * Aktywuje następne pole do uzupełnienia
     */
    @FXML public void gieldaComboBoxOnAction() {
        gielda = gieldaComboBox.getSelectionModel().getSelectedItem();
        if (gielda != null)
            gieldaBooleanProperty.setValue(false);
        else
            gieldaBooleanProperty.setValue(true);
    }


    //  SPRAWDZENIE MOZLIWOSCI UZYCIA WPROWADZONYCH DANYCH PRZEZ UZYTKOWNIKA
    /**
     * Sprawdza czy można dodać indeks
     * @throws AddItemException Istnieje już taki indeks
     */
    private void sprawdzDostepnoscIndeksu() throws AddItemException {
        for (Indeks i: gielda.getIndexList()) {
            if (i.getNamePropertyValue().equals(nameTextField.getText()))
                throw new AddItemException("W wybranej giełdzie istnieje już Indeks o podanej nazwie!");
        }
    }


    // DODATKOWE METODY POPRAWIAJACE CZYTELNOSC KODU
    /**
     * Czyszczenie wszystkich pól wprowadzania danych
     */
    private void clearAllTextFields() {
        nameTextField.clear();
        gieldaComboBox.setValue(null);
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData) {
        gieldaObservableList.addAll(mainData.getStockExchangeList());
    }
}
