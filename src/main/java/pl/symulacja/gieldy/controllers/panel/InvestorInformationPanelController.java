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
import pl.symulacja.gieldy.data.podmiot.Inwestor;

/**
 * Kontroler pliku investor_information_panel.fxml
 * @author Paweł
 */
public class InvestorInformationPanelController implements ControllerThree{
    private boolean editOption = false;
    private Inwestor inwestor;
    @FXML private TextField namelabel;
    @FXML private TextField surname;
    @FXML private TextField pesel;
    @FXML private TextField waluta;
    @FXML private TextField budzet;
    @FXML private TableView<Aktywo> inwestor_table;
    @FXML private TableColumn<Aktywo,String> inwestor_name;
    @FXML private TableColumn<Aktywo,Number> investor_quantity;
    @FXML private ComboBox<Waluta> walutaComboBox;
    @FXML private Button saveButton;
    @FXML private Button removeButton;
    private StringProperty nameProperty = new SimpleStringProperty(this,"nameProperty");
    private StringProperty surnameProperty = new SimpleStringProperty(this,"surnameProperty");
    private BooleanProperty comboBoxProperty = new SimpleBooleanProperty(this, "comboBoxProperty",true);

    @FXML public void initialize(){
        inwestor_name.setCellValueFactory(param -> param.getValue().getNameProperty());
        investor_quantity.setCellValueFactory(param -> param.getValue().getQuantityProperty());
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
            nameProperty = new SimpleStringProperty(this,"nameProperty",inwestor.getNamePropertyValue());
            surnameProperty = new SimpleStringProperty(this,"nameProperty",inwestor.getSurnamePropertyValue());
            namelabel.textProperty().bindBidirectional(nameProperty);
            surname.textProperty().bindBidirectional(surnameProperty);
        }
        else {
            namelabel.textProperty().bindBidirectional(inwestor.getNameProperty());
            surname.textProperty().bindBidirectional(inwestor.getSurnameProperty());
        }
        init();
    }

    /**
     * Zapisuje obiekt
     */
    public void saveOnAction() {
        inwestor.setNamePropertyValue(nameProperty.get());
        inwestor.setSurnamePropertyValue(surnameProperty.get());
        inwestor.setDomyslnaWaluta(walutaComboBox.getValue());
    }

    /**
     * Inicjalizuje ComboBoxa z walutą
     */
    private void init() {
        if (editOption) {
            walutaComboBox.setVisible(true);
            waluta.setVisible(false);
            walutaComboBox.setValue(inwestor.getDomyslnaWaluta());
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
        inwestor.setKill(true);
        inwestor.sellAll();
        Aktywo.getMainDataClass().getInvestorList().remove(inwestor);
    }

    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór obiektu do wyświetlenia oraz trybu otwarcia panelu
     * @param object Obiekt do wyświetlenia
     * @param choose Tryb otwarcia panelu
     */
    @Override
    public void setData(Object object, boolean choose) {
        this.editOption = choose;
        this.inwestor = (Inwestor) object;
        StringConverter converter = new NumberStringConverter();
        pesel.textProperty().bindBidirectional(inwestor.getPESELProperty());
        waluta.textProperty().bindBidirectional(inwestor.getDomyslnaWaluta().getNameProperty());
        budzet.textProperty().bindBidirectional(inwestor.getBudgetProperty(),converter);
        inwestor_table.setItems(inwestor.getStockList());
        editOptionAction();
        walutaComboBox.setItems(Aktywo.getMainDataClass().getCurrencyList());
        saveButton.disableProperty().bind(namelabel.textProperty().isEmpty().or(surname.textProperty().isEmpty().or(comboBoxProperty)));
    }
}
