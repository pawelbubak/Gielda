package pl.symulacja.gieldy.controllers.panel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.gielda.GieldaPapierowWartosciowych;
import pl.symulacja.gieldy.data.podmiot.FunduszInwestycyjny;
import pl.symulacja.gieldy.data.podmiot.Inwestor;
import pl.symulacja.gieldy.exceptions.DeleteItemException;
import pl.symulacja.gieldy.exceptions.WrongKindException;
import pl.symulacja.gieldy.interfaces.ControllerThree;
import pl.symulacja.gieldy.data.aktywo.Waluta;
import pl.symulacja.gieldy.data.kraj.Kraj;
import pl.symulacja.gieldy.utils.DialogsUtils;

/**
 * Kontroler pliku currency_information_panel.fxml
 * @author Paweł
 */
public class CurrencyInformationPanelController implements ControllerThree{
    private boolean editOption = false;
    @FXML private TextField namelabel;
    @FXML private TextField price;
    @FXML private TextField max_price;
    @FXML private TextField min_price;
    @FXML private TextField owner;
    @FXML private TextField quantity;
    @FXML private Button saveButton;
    @FXML private Button removeButton;
    @FXML private LineChart<Number,Number> wykres;
    @FXML private TableView<Kraj> country_table;
    @FXML private TableColumn<Kraj, String> country_name;
    @FXML private VBox hbox;
    private XYChart.Series<Number,Number> series = new XYChart.Series<>();
    private Waluta waluta;
    private SimpleStringProperty nameProperty = new SimpleStringProperty(this,"nameProperty");
    private BooleanProperty comboBoxProperty = new SimpleBooleanProperty(this, "comboBoxProperty");

    /**
     * Inicjalizuje panel
     */
    @FXML public void initialize(){
        saveButton.disableProperty().bind(namelabel.textProperty().isEmpty().or(comboBoxProperty));
        country_name.setCellValueFactory(param -> param.getValue().getNameProperty());
    }

    /**
     * Otwiera panel w odpowiednim trybie
     */
    private void editOptionAction() {
        saveButton.setVisible(editOption);
        removeButton.setVisible(editOption);
        hbox.setVisible(!editOption);
        namelabel.setDisable(!editOption);
        if (editOption){
            nameProperty.setValue(waluta.getNamePropertyValue());
            namelabel.textProperty().bindBidirectional(nameProperty);
        }
        else {
            namelabel.textProperty().bind(waluta.getNameProperty());
        }
    }

    /**
     * Zapisuje zmiany
     */
    public void saveOnAction() {
        try {
            sprawdzMozliwoscEdycji();
            waluta.setNamePropertyValue(nameProperty.get());
        } catch (WrongKindException e) {
            DialogsUtils.errorDialog(e.getWiadomosc());
        }
    }

    /**
     * Sprawdza czy można edytować obiekt
     * @throws WrongKindException Nie mozna edytować
     */
    private void sprawdzMozliwoscEdycji() throws WrongKindException {
        if (waluta.getNamePropertyValue().equals("PLN"))
            throw new WrongKindException("Nie można edytować tej waluty.");
    }

    /**
     * Usuwa obiekt
     */
    public void removeOnAction() {
        try {
            sprawdzMozliwoscUsuniecia();
            waluta.getCountryList().forEach(kraj -> Aktywo.getMainDataClass().getCountryList().remove(kraj));
            Aktywo.getMainDataClass().getRynekWalut().getList().remove(waluta);
            Aktywo.getMainDataClass().getGlobalStockList().remove(waluta);
            Aktywo.getMainDataClass().getCurrencyList().remove(waluta);
        } catch (DeleteItemException e) {
            DialogsUtils.errorDialog(e.getWiadomosc());
        }
    }

    /**
     * Sprawdza możliwość usunięcia
     * @throws DeleteItemException Nie można usunąć
     */
    private void sprawdzMozliwoscUsuniecia() throws DeleteItemException {
        for (Inwestor inwestor: Aktywo.getMainDataClass().getInvestorList()){
            if (inwestor.getDomyslnaWaluta().equals(waluta))
                throw new DeleteItemException("Nie można usunąć waluty, ponieważ jest usuwana.");
        }
        for (FunduszInwestycyjny funduszInwestycyjny: Aktywo.getMainDataClass().getFundList())
            if (funduszInwestycyjny.getDomyslnaWaluta().equals(waluta))
                throw new DeleteItemException("Nie można usunąć waluty, ponieważ jest usuwana.");
        for (GieldaPapierowWartosciowych gieldaPapierowWartosciowych: Aktywo.getMainDataClass().getStockExchangeList())
            if (gieldaPapierowWartosciowych.getCurrency().equals(waluta))
                throw new DeleteItemException("Nie można usunąć waluty, ponieważ jest usuwana.");
        if (waluta.getNamePropertyValue().equals("PLN"))
            throw new DeleteItemException("Nie można usunąć tej waluty");
    }

    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór obiektu do wyświetlenia oraz trybu otwarcia panelu
     * @param object Obiekt do wyświetlenia
     * @param choose Tryb otwarcia panelu
     */
    @Override
    public void setData(Object object, boolean choose) {
        this.waluta = (Waluta)object;
        editOption = choose;
        StringConverter converter = new NumberStringConverter();
        price.textProperty().bindBidirectional(waluta.getPriceProperty(),converter);
        max_price.textProperty().bindBidirectional(waluta.getMaxPriceProperty(),converter);
        min_price.textProperty().bindBidirectional(waluta.getMinPriceProperty(),converter);
        owner.textProperty().bind(waluta.getStockExchange().getNameProperty());
        quantity.textProperty().bindBidirectional(waluta.getQuantityProperty(),converter);
        editOptionAction();
        series.setName("Zmiana ceny w czasie");
        for (int i = 0; i < waluta.getChartData().size(); i++){
            series.getData().add(new XYChart.Data<>(i,waluta.getChartData().get(i)));
        }
        wykres.getData().add(series);
        country_table.setItems(waluta.getCountryList());
    }
}
