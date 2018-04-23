package pl.symulacja.gieldy.controllers.panel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import pl.symulacja.gieldy.interfaces.ControllerThree;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.aktywo.Waluta;
import pl.symulacja.gieldy.data.podmiot.FunduszInwestycyjny;

/**
 * Kontroler pliku fund_information_panel.fxml
 * @author Paweł
 */
public class FundInformationPanelController implements ControllerThree{
    private boolean editOption = false;
    private FunduszInwestycyjny funduszInwestycyjny;
    @FXML private TextField namelabel;
    @FXML private TextField surname;
    @FXML private TextField fundname;
    @FXML private TextField waluta;
    @FXML private TextField budzet;
    @FXML private TableView<Aktywo> fund_table;
    @FXML private TableColumn<Aktywo,String> fund_name;
    @FXML private TableColumn<Aktywo,Number> fund_quantity;
    @FXML private ComboBox<Waluta> walutaComboBox;
    @FXML private Button saveButton;
    @FXML private Button removeButton;
    private StringProperty nameProperty = new SimpleStringProperty(this,"nameProperty");
    private StringProperty surnameProperty = new SimpleStringProperty(this,"surnameProperty");
    private BooleanProperty comboBoxProperty = new SimpleBooleanProperty(this, "comboBoxProperty");

    /**
     * Inicjalizuje panel
     */
    @FXML public void initialize(){
        fund_name.setCellValueFactory(param -> param.getValue().getNameProperty());
        fund_quantity.setCellValueFactory(param -> param.getValue().getQuantityProperty());
    }

    /**
     * Otwiera panel w odpowiednim trybie
     */
    private void editOptionAction() {
        saveButton.setVisible(editOption);
        removeButton.setVisible(editOption);
        namelabel.setDisable(!editOption);
        surname.setDisable(!editOption);
        if (editOption){
            nameProperty = new SimpleStringProperty(this,"nameProperty",funduszInwestycyjny.getNamePropertyValue());
            surnameProperty = new SimpleStringProperty(this,"nameProperty",funduszInwestycyjny.getSurnamePropertyValue());
            namelabel.textProperty().bindBidirectional(nameProperty);
            surname.textProperty().bindBidirectional(surnameProperty);
        }
        else {
            namelabel.textProperty().bindBidirectional(funduszInwestycyjny.getNameProperty());
            surname.textProperty().bindBidirectional(funduszInwestycyjny.getSurnameProperty());
        }
        init();
    }

    /**
     * Inicjuje pola tekstowe
     */
    private void init() {
        if (editOption) {
            walutaComboBox.setVisible(true);
            walutaComboBox.setValue(funduszInwestycyjny.getDomyslnaWaluta());
            waluta.setVisible(false);
        }
        else {
            walutaComboBox.setVisible(false);
            waluta.setVisible(true);
        }
    }

    /**
     * Aktywuje przycisk
     */
    @FXML public void walutaComboBoxOnAction() {
        if (walutaComboBox.getValue() == null)
            comboBoxProperty.setValue(true);
        else
            comboBoxProperty.setValue(false);
    }

    /**
     * Usuwa obiekt
     */
    public void removeOnAction() {
        funduszInwestycyjny.setKill(true);
        funduszInwestycyjny.sellAll();
        Aktywo.getMainDataClass().getFundList().remove(funduszInwestycyjny);
        Aktywo.getMainDataClass().getRynekFunduszyInwestycyjnych().usunJednostki("Jednostka " + funduszInwestycyjny.getFundNamePropertyValue());
    }

    /**
     * Zapisuje obiekt
     */
    public void saveOnAction() {
        funduszInwestycyjny.setNamePropertyValue(nameProperty.get());
        funduszInwestycyjny.setSurnamePropertyValue(surnameProperty.get());
        funduszInwestycyjny.setDomyslnaWaluta(walutaComboBox.getValue());
    }

    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór obiektu do wyświetlenia oraz trybu otwarcia panelu
     * @param object Obiekt do wyświetlenia
     * @param choose Tryb otwarcia panelu
     */
    @Override
    public void setData(Object object, boolean choose) {
        editOption = choose;
        this.funduszInwestycyjny = (FunduszInwestycyjny) object;
        StringConverter converter = new NumberStringConverter();
        fundname.textProperty().bindBidirectional(funduszInwestycyjny.getFundNameProperty());
        waluta.textProperty().bindBidirectional(funduszInwestycyjny.getDomyslnaWaluta().getNameProperty());
        budzet.textProperty().bindBidirectional(funduszInwestycyjny.getBudgetProperty(),converter);
        fund_table.setItems(funduszInwestycyjny.getStockList());
        walutaComboBox.setItems(Aktywo.getMainDataClass().getCurrencyList());
        editOptionAction();
        saveButton.disableProperty().bind(namelabel.textProperty().isEmpty().or(surname.textProperty().isEmpty().or(fundname.textProperty().isEmpty()).or(comboBoxProperty)));
    }
}
