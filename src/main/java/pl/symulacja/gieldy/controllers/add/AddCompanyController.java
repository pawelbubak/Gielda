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
import pl.symulacja.gieldy.data.spolka.Spolka;
import pl.symulacja.gieldy.exceptions.AddItemException;
import pl.symulacja.gieldy.exceptions.WrongKindException;
import pl.symulacja.gieldy.utils.DialogsUtils;
import pl.symulacja.gieldy.utils.RandomModule;
import java.time.LocalDate;

/**
 * Kontroler pliku add_company.fxml
 * @author Paweł
 */
public class AddCompanyController implements Controller{
    private MainDataClass mainData;
    @FXML private Button add_button;
    @FXML private TextField nameTextField;
    @FXML private TextField kursTextField;
    @FXML private TextField akcjeTextField;
    @FXML private TextField kapitalWlasnyTextField;
    @FXML private TextField kapitalZakladowyTextField;
    @FXML private ComboBox<GieldaPapierowWartosciowych> gieldaComboBox;
    @FXML private ComboBox<Indeks> indeksComboBox;
    private Spolka newSpolka = new Spolka();
    private BooleanProperty nameBooleanProperty = new SimpleBooleanProperty(this,"nameBooleanProperty");
    private BooleanProperty kursBooleanProperty = new SimpleBooleanProperty(this,"kursBooleanProperty");
    private BooleanProperty akcjaBooleanProperty = new SimpleBooleanProperty(this,"akcjaBooleanProperty");
    private BooleanProperty kapitalBooleanProperty = new SimpleBooleanProperty(this,"kapitalBooleanProperty");
    private BooleanProperty kapitalTwoBooleanProperty = new SimpleBooleanProperty(this,"kapitalTwoBooleanProperty");
    private BooleanProperty gieldaBooleanProperty = new SimpleBooleanProperty(this,"gieldaBooleanProperty",true);
    private BooleanProperty indeksBooleanProperty = new SimpleBooleanProperty(this,"indeksBooleanProperty",true);
    private ObservableList<GieldaPapierowWartosciowych> gieldaObservableList = FXCollections.observableArrayList();
    private ObservableList<Indeks> indeksObservableList = FXCollections.observableArrayList();

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        //  NAME
        nameTextField.textProperty().bindBidirectional(newSpolka.getNameProperty());
        //  PRICE
        nameBooleanProperty.bind(newSpolka.getNameProperty().isEmpty());
        kursTextField.disableProperty().bind(nameBooleanProperty);
        //  STOCK
        kursBooleanProperty.bind(kursTextField.textProperty().isEmpty().or(nameBooleanProperty));
        akcjeTextField.disableProperty().bind(kursBooleanProperty);
        //  CAPITAL
        akcjaBooleanProperty.bind(akcjeTextField.textProperty().isEmpty().or(kursBooleanProperty));
        kapitalWlasnyTextField.disableProperty().bind(akcjaBooleanProperty);
        //  CAPITAL
        kapitalBooleanProperty.bind(kapitalWlasnyTextField.textProperty().isEmpty().or(akcjaBooleanProperty));
        kapitalZakladowyTextField.disableProperty().bind(kapitalBooleanProperty);
        //  GIELDA
        gieldaComboBox.setItems(gieldaObservableList);
        kapitalTwoBooleanProperty.bind(kapitalZakladowyTextField.textProperty().isEmpty().or(kapitalBooleanProperty));
        gieldaComboBox.disableProperty().bind(kapitalTwoBooleanProperty);
        //  INDEKS
        indeksComboBox.setItems(indeksObservableList);
        indeksComboBox.disableProperty().bind(gieldaBooleanProperty.or(kapitalTwoBooleanProperty));
        //  BUTTON
        add_button.disableProperty().bind(indeksBooleanProperty.or(gieldaBooleanProperty).or(kapitalTwoBooleanProperty));
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
        nameTextField.setText(RandomModule.randName("spolka"));
        kursTextField.setText((RandomModule.randName("priceA")));
        akcjeTextField.setText(RandomModule.randName("quantity"));
        kapitalWlasnyTextField.setText(RandomModule.randName("budget2"));
        kapitalZakladowyTextField.setText(RandomModule.randName("budget"));
    }

    /**
     * Dodaje Spółkę po zatwierdzeniu przez użytkownika i sprawdzeniu wszytskich warunków
     */
    @FXML public void dodajButtonOnAction() {
        try{
            Spolka spolka = new Spolka();
            sprawdzDostepnoscSpolki();
            spolka.setNamePropertyValue(newSpolka.getNamePropertyValue());
            spolka.setValuationDate(LocalDate.now());
            sprawdzPoprawnoscAkcji();
            spolka.setQuantityPropertyValue(Integer.parseInt(akcjeTextField.getText()));
            sprawdzPoprawnoscKursu();
            spolka.setStock(RandomModule.round(Double.parseDouble(kursTextField.getText())/100));
            sprawdzPoprawnoscKapitalu(kapitalWlasnyTextField);
            spolka.setEquityCapitalPropertyValue(Double.parseDouble(kapitalWlasnyTextField.getText()));
            sprawdzPoprawnoscKapitalu(kapitalZakladowyTextField);
            spolka.setShareCapitalPropertyValue(Double.parseDouble(kapitalZakladowyTextField.getText()));
            spolka.wypuscAkcje(gieldaComboBox.getValue(), mainData);
            indeksComboBox.getValue().getCompanyList().add(spolka);
            mainData.getCompanyList().add(spolka);
            new Thread(spolka).start();
            clearAllTextFields();
        }
        catch (AddItemException e){
            DialogsUtils.errorDialog(e.getWiadomosc());
            nameTextField.clear();
        }
        catch(WrongKindException e1){
            DialogsUtils.errorDialog(e1.getWiadomosc());
        }
    }

    //  OBSLUGA COMBOBOX
    /**
     * Aktywuje następne pole do uzupełnienia
     */
    @FXML public void gieldaComboBoxOnAction() {
        GieldaPapierowWartosciowych gielda = gieldaComboBox.getSelectionModel().getSelectedItem();
        if (gielda != null){
            indeksObservableList.setAll(gielda.getIndexList());
            gieldaBooleanProperty.setValue(false);
        }
        else
            gieldaBooleanProperty.setValue(true);
    }

    /**
     * Aktywuje następne pole do uzupełnienia
     */
    @FXML public void indeksComboBoxOnAction() {
        if (indeksComboBox.getSelectionModel().getSelectedItem() != null)
            indeksBooleanProperty.setValue(false);
        else
            indeksBooleanProperty.setValue(true);
    }

    //  SPRAWDZENIE MOZLIWOSCI UZYCIA WPROWADZONYCH DANYCH PRZEZ UZYTKOWNIKA
    /**
     * Sprawdza czy można utowrzyć spółkę
     * @throws AddItemException Brak możliwości dodania spółki
     */
    private void sprawdzDostepnoscSpolki() throws AddItemException {
        for (Spolka s: mainData.getCompanyList()){
            if (s.getNamePropertyValue().equals(newSpolka.getNamePropertyValue()))
                throw new AddItemException("Spółka o podanej nazwie już istnieje!");
        }
    }

    /**
     * Sprawdza czy Budżet pobrany od użytkownika jest poprawny
     * @throws WrongKindException Błędny budżet
     */
    private void sprawdzPoprawnoscKursu() throws WrongKindException {
        try {
            double x = Double.parseDouble(kursTextField.getText());
            if (x < 0.5 || x > 10)
                throw new WrongKindException();
        }
        catch (Exception e) {
            throw new WrongKindException("Błędny kurs!\n\nProszę podać wartość liczbową z zakresu [0.5, 10.0]");
        }
    }

    /**
     * Sprawdza czy wprowadzony kapitał jest poprawny
     * @param field Pole wprowadzania danych
     * @throws WrongKindException Błędny kapitał
     */
    private void sprawdzPoprawnoscKapitalu(TextField field) throws WrongKindException {
        try {
            double x = Double.parseDouble(field.getText());
            if (x < 1000.0)
                throw new WrongKindException();
        }
        catch (Exception e) {
            throw new WrongKindException("Błędny kapital!\n\nProszę podać wartość liczbową większą od 1000!");
        }
    }

    /**
     * Sprawdza poprawność wprowadzonego kursu akcji
     * @throws WrongKindException Błędny kurs akcji
     */
    private void sprawdzPoprawnoscAkcji() throws WrongKindException {
        try {
            int x = Integer.parseInt(akcjeTextField.getText());
            if (x > 1000 || x < 100)
                throw new WrongKindException();
        }
        catch (Exception e) {
            throw new WrongKindException("Błędna ilość akcji!\n\nProszę podać wartość liczbową z zakresu [100,1000]");
        }
    }


    // DODATKOWE METODY POPRAWIAJACE CZYTELNOSC KODU
    /**
     * Czyszczenie wszystkich pól wprowadzania danych
     */
    private void clearAllTextFields() {
        nameTextField.clear();
        kursTextField.clear();
        akcjeTextField.clear();
        kapitalWlasnyTextField.clear();
        kapitalZakladowyTextField.clear();
        gieldaComboBox.setValue(null);
        indeksComboBox.setValue(null);
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData) {
        this.mainData = mainData;
        gieldaObservableList.addAll(mainData.getStockExchangeList());
    }
}
