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
import pl.symulacja.gieldy.data.gielda.GieldaPapierowWartosciowych;
import pl.symulacja.gieldy.data.indeks.Indeks;
import pl.symulacja.gieldy.data.kraj.Kraj;
import pl.symulacja.gieldy.exceptions.DeleteItemException;
import pl.symulacja.gieldy.exceptions.WrongKindException;
import pl.symulacja.gieldy.utils.DialogsUtils;

/**
 * Kontroler pliku stock_exchange_information_panel.fxml
 * @author Paweł
 */
public class StockExchangeInformationPanelController implements ControllerThree{
    private boolean editOption = false;
    private GieldaPapierowWartosciowych gieldaPapierowWartosciowych;
    StringConverter converter = new NumberStringConverter();
    @FXML private TextField namelabel;
    @FXML private TextField marzaTextField;
    @FXML private TextField country;
    @FXML private TextField miasto;
    @FXML private TextField ulica;
    @FXML private TextField waluta;
    @FXML private TreeView<String> indeks_tree;
    @FXML private Button saveButton;
    @FXML private Button removeButton;
    @FXML private ComboBox<Kraj> krajComboBox;
    private StringProperty nameProperty = new SimpleStringProperty(this,"nameProperty");
    private StringProperty marzaProperty = new SimpleStringProperty(this,"surnameProperty");
    private StringProperty townProperty = new SimpleStringProperty(this,"surnameProperty");
    private StringProperty streetProperty = new SimpleStringProperty(this,"surnameProperty");
    private BooleanProperty comboBoxProperty = new SimpleBooleanProperty(this, "comboBoxProperty",false);


    @FXML public void initialize(){
    }

    /**
     * inicjalizuje widok drzewa indeksów i spółek
     */
    private void initTreeView(){
        TreeItem<String> root = new TreeItem<>();
        gieldaPapierowWartosciowych.getIndexList().forEach(indeks -> {
            TreeItem<String> treeItem = new TreeItem<>(indeks.getNamePropertyValue());
            indeks.getCompanyList().forEach(spolka -> {
                treeItem.getChildren().add(new TreeItem<>(spolka.getNamePropertyValue()));
            });
            root.getChildren().add(treeItem);
        });
        indeks_tree.setRoot(root);
    }

    /**
     * uruchamia panel w odpowiednim trybie
     */
    private void editOptionAction() {
        saveButton.setVisible(editOption);
        removeButton.setVisible(editOption);
        namelabel.setDisable(!editOption);
        marzaTextField.setDisable(!editOption);
        miasto.setDisable(!editOption);
        ulica.setDisable(!editOption);
        if (editOption){
            nameProperty = new SimpleStringProperty(this,"nameProperty",gieldaPapierowWartosciowych.getNamePropertyValue());
            marzaProperty = new SimpleStringProperty(this,"marzaProperty",String.valueOf(gieldaPapierowWartosciowych.getMarzaPropertyValue()));
            townProperty = new SimpleStringProperty(this,"townProperty",gieldaPapierowWartosciowych.getTownPropertyValue());
            streetProperty = new SimpleStringProperty(this,"streetProperty",gieldaPapierowWartosciowych.getStreetPropertyValue());
            namelabel.textProperty().bindBidirectional(nameProperty);
            marzaTextField.textProperty().bindBidirectional(marzaProperty);
            miasto.textProperty().bindBidirectional(townProperty);
            ulica.textProperty().bindBidirectional(streetProperty);
        }
        else {
            namelabel.textProperty().bindBidirectional(gieldaPapierowWartosciowych.getNameProperty());
            marzaTextField.textProperty().bindBidirectional(gieldaPapierowWartosciowych.getMarzaProperty(),converter);
            miasto.textProperty().bindBidirectional(gieldaPapierowWartosciowych.getTownProperty());
            ulica.textProperty().bindBidirectional(gieldaPapierowWartosciowych.getStreetProperty());
        }
        init();
    }

    /**
     * Inicjalizuje ComboBoxa z krajami
     */
    private void init() {
        if (editOption) {
            krajComboBox.setVisible(true);
            country.setVisible(false);
            krajComboBox.setValue(gieldaPapierowWartosciowych.getCountry());
        }
        else {
            krajComboBox.setVisible(false);
            country.setVisible(true);
        }
    }

    /**
     * Zapisuje zmiany
     */
    @FXML public void saveOnAction() {
        try {
            sprawdzMarze();
            gieldaPapierowWartosciowych.setNamePropertyValue(nameProperty.get());
            gieldaPapierowWartosciowych.setMarzaPropertyValue(Double.parseDouble(marzaProperty.getValue()));
            gieldaPapierowWartosciowych.setCountry(krajComboBox.getValue());
            gieldaPapierowWartosciowych.setTownPropertyValue(townProperty.get());
            gieldaPapierowWartosciowych.setStreetPropertyValue(streetProperty.get());
            gieldaPapierowWartosciowych.setCurrency(krajComboBox.getValue().getWaluta());
        } catch (WrongKindException e) {
            DialogsUtils.errorDialog(e.getWiadomosc());
        }
    }

    /**
     * Sparwdza poprawnosc wprowadzonej marży
     * @throws WrongKindException Marża nie jest poprawna
     */
    private void sprawdzMarze() throws WrongKindException {
        try {
            double x = Double.parseDouble(marzaTextField.getText());
            if (x > 0.2 || x < 0.01)
                throw new WrongKindException("Proszę podać wartość liczbową z przedziału 0,01 do 0,2!");
        }
        catch (Exception e){
            throw new WrongKindException("Proszę podać wartość liczbową z przedziału 0,01 do 0,2!");
        }
    }

    /**
     * usuwa obiekt
     */
    @FXML public void removeOnAction() {
        try {
            sprawdzMozliwoscUsuniecia();
            Aktywo.getMainDataClass().getStockExchangeList().remove(gieldaPapierowWartosciowych);
        } catch (DeleteItemException e) {
            DialogsUtils.errorDialog(e.getWiadomosc());
        }
    }

    /**
     * Sprawdza możliwość usunięcia obiektu
     * @throws DeleteItemException nie można usunąc obiektu
     */
    private void sprawdzMozliwoscUsuniecia() throws DeleteItemException {
        int i = 0, j = 0;
        for (Indeks indeks : gieldaPapierowWartosciowych.getIndexList()) {
            i++;
            if (indeks.getCompanyList().isEmpty())
                j++;
        }
        if (i != j)
            throw new DeleteItemException("Nie można usunąć Giełdy, ponieważ przypisana jest do niej \nco najmniej jedna Spółka");
    }

    /**
     * aktywuje przycisk
     */
    @FXML public void krajComboBoxOnAction() {
        if (krajComboBox.getValue() != null)
            comboBoxProperty.setValue(false);
        else
            comboBoxProperty.setValue(true);
    }

    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór obiektu do wyświetlenia oraz trybu otwarcia panelu
     * @param object Obiekt do wyświetlenia
     * @param controller Tryb otwarcia panelu
     */
    @Override
    public void setData(Object object, boolean controller) {
        this.editOption = controller;
        this.gieldaPapierowWartosciowych = (GieldaPapierowWartosciowych) object;
        country.textProperty().bindBidirectional(gieldaPapierowWartosciowych.getCountry().getNameProperty());
        waluta.textProperty().bindBidirectional(gieldaPapierowWartosciowych.getCurrency().getNameProperty());
        krajComboBox.setItems(Aktywo.getMainDataClass().getCountryList());
        initTreeView();
        editOptionAction();
        saveButton.disableProperty().bind(namelabel.textProperty().isEmpty().or(marzaTextField.textProperty().isEmpty().or(comboBoxProperty).or(ulica.textProperty().isEmpty().or(miasto.textProperty().isEmpty()))));
    }
}
