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
import pl.symulacja.gieldy.data.podmiot.FunduszInwestycyjny;
import pl.symulacja.gieldy.exceptions.AddItemException;
import pl.symulacja.gieldy.exceptions.WrongKindException;
import pl.symulacja.gieldy.utils.DialogsUtils;
import pl.symulacja.gieldy.utils.RandomModule;

/**
 * Kontroler pliku add_fund.fxml
 * @author Paweł
 */
public class AddFundController implements Controller {
    private MainDataClass mainData;
    @FXML private Button add_button;
    @FXML private TextField funduszNameTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField surnameTextField;
    @FXML private ComboBox<Waluta> walutaComboBox;
    @FXML private TextField budzetTextField;
    private FunduszInwestycyjny newFunduszInwestycyjnyTemp = new FunduszInwestycyjny();
    private BooleanProperty nameBooleanProperty = new SimpleBooleanProperty(this,"nameBooleanProperty");
    private BooleanProperty surnameBooleanProperty = new SimpleBooleanProperty(this,"surnameBooleanProperty");
    private BooleanProperty fundBooleanProperty = new SimpleBooleanProperty(this,"peselBooleanProperty");
    private BooleanProperty walutaBooleanProperty = new SimpleBooleanProperty(this,"walutaBooleanProperty",true);
    private BooleanProperty budgetBooleanProperty = new SimpleBooleanProperty(this,"walutaBooleanProperty");
    private ObservableList<Waluta> walutaObservableList = FXCollections.observableArrayList();

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        //  FUNDNAME
        funduszNameTextField.textProperty().bindBidirectional(newFunduszInwestycyjnyTemp.getFundNameProperty());
        //  NAME
        fundBooleanProperty.bind(newFunduszInwestycyjnyTemp.getFundNameProperty().isEmpty());
        nameTextField.disableProperty().bind(fundBooleanProperty);
        nameTextField.textProperty().bindBidirectional(newFunduszInwestycyjnyTemp.getNameProperty());
        //  SURNAME
        nameBooleanProperty.bind(newFunduszInwestycyjnyTemp.getNameProperty().isEmpty().or(fundBooleanProperty));
        surnameTextField.disableProperty().bind(nameBooleanProperty);
        surnameTextField.textProperty().bindBidirectional(newFunduszInwestycyjnyTemp.getSurnameProperty());
        //  WALUTA
        walutaComboBox.setItems(walutaObservableList);
        surnameBooleanProperty.bind(newFunduszInwestycyjnyTemp.getSurnameProperty().isEmpty().or(nameBooleanProperty));
        walutaComboBox.disableProperty().bind(surnameBooleanProperty);
        //  BUDGET
        budzetTextField.disableProperty().bind(walutaBooleanProperty.or(surnameBooleanProperty));
        //  BUTTON
        budgetBooleanProperty.bind(budzetTextField.textProperty().isEmpty().or(surnameBooleanProperty));
        add_button.disableProperty().bind(budgetBooleanProperty.or(walutaBooleanProperty));
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
        funduszNameTextField.setText(RandomModule.randName("fund"));
        nameTextField.setText(RandomModule.randName("name"));
        surnameTextField.setText(RandomModule.randName("surname"));
        budzetTextField.setText(RandomModule.randName("budget"));
    }

    /**
     * Dodaje Fundusz Inwestycyjny po zatwierdzeniu przez użytkownika i sprawdzeniu wszytskich warunków
     */
   @FXML public void dodajButtonOnAction() {
        try{
            FunduszInwestycyjny funduszInwestycyjny = new FunduszInwestycyjny();
            sprawdzDostepnoscFunduszu();
            sprawdzDostepnoscGieldy();
            funduszInwestycyjny.setFundNamePropertyValue(newFunduszInwestycyjnyTemp.getFundNamePropertyValue());
            funduszInwestycyjny.setNamePropertyValue(newFunduszInwestycyjnyTemp.getNamePropertyValue());
            funduszInwestycyjny.setSurnamePropertyValue(newFunduszInwestycyjnyTemp.getSurnamePropertyValue());
            funduszInwestycyjny.setDomyslnaWaluta(walutaComboBox.getValue());
            sprawdzPoprawnoscBudzetu();
            funduszInwestycyjny.setBudgetPropertyValue(Double.parseDouble(budzetTextField.getText()));
            funduszInwestycyjny.wypuscUdzialy(mainData);
            mainData.getFundList().add(funduszInwestycyjny);
            new Thread(funduszInwestycyjny).start();
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
     * Aktywuje następne pole do uzupełnienia
     */
    @FXML public void walutaComboBoxOnAction() {
        newFunduszInwestycyjnyTemp.setDomyslnaWaluta(walutaComboBox.getSelectionModel().getSelectedItem());
        if (newFunduszInwestycyjnyTemp.getDomyslnaWaluta()!=null)
            walutaBooleanProperty.setValue(false);
        else
            walutaBooleanProperty.setValue(true);
    }


    //  SPRAWDZENIE MOZLIWOSCI UZYCIA WPROWADZONYCH DANYCH PRZEZ UZYTKOWNIKA
    /**
     * Sprawdza czy można utworzyć giełdę o podanej nazwie
     * @throws AddItemException Istnieje już taka giełda
     */
    private void sprawdzDostepnoscGieldy() throws AddItemException {
        try {
            if (mainData.getRynekFunduszyInwestycyjnych() == null);
        }
        catch (Exception e) {
            throw new AddItemException("Nie istenieje jeszcze żadna giełda na której możnaby wystawić Jednostki!");
        }
    }

    /**
     * Sprawdza możliwość dodania funduszu
     * @throws AddItemException Istnieje już taki fundusz
     */
    private void sprawdzDostepnoscFunduszu() throws AddItemException {
        for (FunduszInwestycyjny f: mainData.getFundList()){
            if (f.getFundNamePropertyValue().equals(newFunduszInwestycyjnyTemp.getFundNamePropertyValue()))
                throw new AddItemException("Fundusz o podanej nazwie już istnieje!");
        }
    }

    /**
     * Sprawdza czy Budżet pobrany od użytkownika jest poprawny
     * @throws WrongKindException Błędny budżet
     */
    public void sprawdzPoprawnoscBudzetu() throws WrongKindException {
        try {
            double x = Double.parseDouble(budzetTextField.getText());
            if (x < 1000.0)
                throw new WrongKindException("Podano zbyt małą wartość!");
        }
        catch (Exception e) {
            throw new WrongKindException("Podano błędną wartość!\n\nNajmniejsza możliwa wartość w przypadku \nFunduszu Inwestycyjnego wynosi 1000.");
        }
    }


    // DODATKOWE METODY POPRAWIAJACE CZYTELNOSC KODU
    /**
     * Czyszczenie wszystkich pól wprowadzania danych
     */
    private void clearAllTextFields() {
        funduszNameTextField.clear();
        nameTextField.clear();
        surnameTextField.clear();
        walutaComboBox.setValue(null);
        budzetTextField.clear();
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu i ustawienie Waluty do wybrania z listy
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData) {
        this.mainData = mainData;
        walutaObservableList.addAll(mainData.getCurrencyList());
    }
}
