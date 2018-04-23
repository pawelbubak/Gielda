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
import pl.symulacja.gieldy.data.aktywo.Waluta;
import pl.symulacja.gieldy.data.podmiot.Inwestor;
import pl.symulacja.gieldy.exceptions.AddItemException;
import pl.symulacja.gieldy.exceptions.WrongKindException;
import pl.symulacja.gieldy.utils.DialogsUtils;
import pl.symulacja.gieldy.utils.RandomModule;

/**
 * Kontroler pliku add_investor.fxml
 * @author Paweł
 */
public class AddInvestorController implements Controller{
    private MainDataClass mainData;
    @FXML private Button add_button;
    @FXML private TextField nameTextField;
    @FXML private TextField surnameTextField;
    @FXML private TextField peselTextField;
    @FXML private ComboBox<Waluta> walutaComboBox;
    @FXML private TextField budzetTextField;
    private Inwestor newInwestor = new Inwestor();
    private BooleanProperty nameBooleanProperty = new SimpleBooleanProperty(this,"nameBooleanProperty");
    private BooleanProperty surnameBooleanProperty = new SimpleBooleanProperty(this,"surnameBooleanProperty");
    private BooleanProperty peselBooleanProperty = new SimpleBooleanProperty(this,"peselBooleanProperty");
    private BooleanProperty walutaBooleanProperty = new SimpleBooleanProperty(this,"walutaBooleanProperty",true);
    private BooleanProperty budgetBooleanProperty = new SimpleBooleanProperty(this,"walutaBooleanProperty");
    private ObservableList<Waluta> walutaObservableList = FXCollections.observableArrayList();

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        //  NAME
        nameTextField.textProperty().bindBidirectional(newInwestor.getNameProperty());
        //  SURNAME
        nameBooleanProperty.bind(newInwestor.getNameProperty().isEmpty());
        surnameTextField.disableProperty().bind(nameBooleanProperty);
        surnameTextField.textProperty().bindBidirectional(newInwestor.getSurnameProperty());
        //  PESEL
        surnameBooleanProperty.bind(newInwestor.getSurnameProperty().isEmpty().or(nameBooleanProperty));
        peselTextField.disableProperty().bind(surnameBooleanProperty);
        peselTextField.textProperty().bindBidirectional(newInwestor.getPESELProperty());
        //  WALUTA
        walutaComboBox.setItems(walutaObservableList);
        peselBooleanProperty.bind(newInwestor.getPESELProperty().isEmpty().or(surnameBooleanProperty));
        walutaComboBox.disableProperty().bind(peselBooleanProperty);
        //  BUDGET
        budzetTextField.disableProperty().bind(walutaBooleanProperty.or(peselBooleanProperty));
        //  BUTTON
        budgetBooleanProperty.bind(budzetTextField.textProperty().isEmpty().or(walutaBooleanProperty.or(peselBooleanProperty)));
        add_button.disableProperty().bind(budgetBooleanProperty);
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
        nameTextField.setText(RandomModule.randName("name"));
        surnameTextField.setText(RandomModule.randName("surname"));
        peselTextField.setText(RandomModule.randName("pesel"));
        budzetTextField.setText(RandomModule.randName("budget"));
    }

    /**
     * Dodaje Inwestora po zatwierdzeniu przez użytkownika i sprawdzeniu wszytskich warunków
     */
    @FXML public void dodajButtonOnAction() {
        try {
            Inwestor inwestor = new Inwestor();
            sprawdzDostepnoscInwestora();
            inwestor.setNamePropertyValue(newInwestor.getNamePropertyValue());
            inwestor.setSurnamePropertyValue(newInwestor.getSurnamePropertyValue());
            inwestor.setPESELPropertyValue(newInwestor.getPESELPropertyValue());
            inwestor.setDomyslnaWaluta(walutaComboBox.getValue());
            sprawdzPoprawnoscBudzetu();
            inwestor.setBudgetPropertyValue(Double.parseDouble(budzetTextField.getText()));
            mainData.getInvestorList().add(inwestor);
            new Thread(inwestor).start();
            clearAllTextFields();
        }
        catch (AddItemException e){
            DialogsUtils.errorDialog(e.getWiadomosc());
        }
        catch (WrongKindException e1)
        {
            DialogsUtils.errorDialog(e1.getWiadomosc());
            budzetTextField.clear();
        }
    }

    //  OBSLUGA COMBOBOX
    /**
     * Aktywuje następne pole do uzupełnienia i pobiera wartość
     */
    @FXML public void walutaComboBoxOnAction() {
        newInwestor.setDomyslnaWaluta(walutaComboBox.getSelectionModel().getSelectedItem());
        if (newInwestor.getDomyslnaWaluta()!=null)
            walutaBooleanProperty.setValue(false);
        else
            walutaBooleanProperty.setValue(true);
    }


    //  SPRAWDZENIE MOZLIWOSCI UZYCIA WPROWADZONYCH DANYCH PRZEZ UZYTKOWNIKA
    /**
     * Sprawdza czy można dodać inwestora
     * @throws AddItemException Inwestor o podanym numerze PESEL już istnije
     */
    private void sprawdzDostepnoscInwestora() throws AddItemException {
        for (Inwestor i: mainData.getInvestorList()) {
            if (i.getPESELPropertyValue().equals(newInwestor.getPESELPropertyValue()))
                throw new AddItemException("Inwestor o podanym numerze PESEL już istnieje!");
        }
    }

    /**
     * Sprawdza czy Budżet pobrany od użytkownika jest poprawny
     * @throws WrongKindException Błędny budżet
     */
    private void sprawdzPoprawnoscBudzetu() throws WrongKindException {
        try {
            double x = Double.parseDouble(budzetTextField.getText());
            if (x < 100.0)
                throw new WrongKindException("Podano zbyt małą wartość!");
        }
        catch (Exception e) {
            throw new WrongKindException("Podano błędną wartość!\n\nNajmniejsza możliwa wartość w przypadku \nInwestora wynosi 100.");
        }
    }


    // DODATKOWE METODY POPRAWIAJACE CZYTELNOSC KODU
    /**
     * Czyszczenie wszystkich pól wprowadzania danych
     */
    private void clearAllTextFields() {
        nameTextField.clear();
        surnameTextField.clear();
        peselTextField.clear();
        walutaComboBox.setValue(null);
        budzetTextField.clear();
    }


//  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData) {
        this.mainData = mainData;
        walutaObservableList.addAll(mainData.getCurrencyList());
    }
}
