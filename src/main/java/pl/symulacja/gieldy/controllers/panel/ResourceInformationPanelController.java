package pl.symulacja.gieldy.controllers.panel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import pl.symulacja.gieldy.interfaces.ControllerThree;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.aktywo.Surowiec;

import java.util.ArrayList;

/**
 * Kontroler pliku resource_information_panel.fxml
 * @author Paweł
 */
public class ResourceInformationPanelController implements ControllerThree{
    private boolean editOption = false;
    @FXML private TextField namelabel;
    @FXML private TextField jednostkaLabel;
    @FXML private TextField price;
    @FXML private TextField max_price;
    @FXML private TextField min_price;
    @FXML private TextField owner;
    @FXML private TextField quantity;
    @FXML private Button saveButton;
    @FXML private Button removeButton;
    @FXML private ComboBox<String> jednostkaComboBox;
    @FXML private LineChart<Number,Number> wykres;
    private XYChart.Series<Number,Number> series = new XYChart.Series<>();
    private Surowiec surowiec;
    private SimpleStringProperty nameProperty = new SimpleStringProperty(this,"nameProperty");
    private BooleanProperty comboBoxProperty = new SimpleBooleanProperty(this, "comboBoxProperty");
    private ObservableList<String> jednostkaObservableList = FXCollections.observableArrayList();

    /**
     * Inicjalizuje komponenty fxml
     */
    @FXML public void initialize(){
        saveButton.disableProperty().bind(namelabel.textProperty().isEmpty().or(comboBoxProperty));
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("uncja");
        arrayList.add("g");
        arrayList.add("dag");
        arrayList.add("kg");
        arrayList.add("t");
        jednostkaObservableList.addAll(arrayList);
        jednostkaComboBox.setItems(jednostkaObservableList);
    }

    /**
     * Otwiera panel w odpowiednim trybie
     */
    private void editOptionAction() {
        saveButton.setVisible(editOption);
        removeButton.setVisible(editOption);
        namelabel.setDisable(!editOption);
        if (editOption){
            nameProperty.setValue(surowiec.getNamePropertyValue());
            namelabel.textProperty().bindBidirectional(nameProperty);
        }
        else {
            namelabel.textProperty().bind(surowiec.getNameProperty());
        }
        init();
    }

    /**
     * Zapisuje obiekt
     */
    public void saveOnAction() {
        surowiec.setNamePropertyValue(nameProperty.get());
        surowiec.setJednostkaHandlowaPropertyValue(jednostkaComboBox.getValue());
    }

    /**
     * Inicjuje ComboBoxa z jednostkami handlowymi
     */
    private void init() {
        if (editOption) {
            jednostkaComboBox.setVisible(true);
            jednostkaLabel.setVisible(false);
            jednostkaComboBox.setMinHeight(25.0);
            jednostkaComboBox.setValue(surowiec.getJednostkaHandlowaPropertyValue());
        }
        else {
            jednostkaComboBox.setVisible(false);
            jednostkaLabel.setVisible(true);
        }
    }

    /**
     * Aktywuje przycisk
     */
    public void jednostkaComboBoxOnAction() {
        if (jednostkaComboBox.getValue() == null)
            comboBoxProperty.setValue(true);
        else
            comboBoxProperty.setValue(false);
    }

    /**
     * Usuwa obiekt
     */
    public void removeOnAction() {
        Aktywo.getMainDataClass().getResourceList().remove(surowiec);
        Aktywo.getMainDataClass().getGlobalStockList().remove(surowiec);
    }

    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór obiektu do wyświetlenia oraz trybu otwarcia panelu
     * @param object Obiekt do wyświetlenia
     * @param choose Tryb otwarcia panelu
     */
    @Override
    public void setData(Object object, boolean choose) {
        this.surowiec = (Surowiec)object;
        editOption = choose;
        StringConverter converter = new NumberStringConverter();
        jednostkaLabel.textProperty().bind(surowiec.getJednostkaHandlowaProperty());
        price.textProperty().bindBidirectional(surowiec.getPriceProperty(),converter);
        max_price.textProperty().bindBidirectional(surowiec.getMaxPriceProperty(),converter);
        min_price.textProperty().bindBidirectional(surowiec.getMinPriceProperty(),converter);
        owner.textProperty().bind(surowiec.getStockExchange().getNameProperty());
        quantity.textProperty().bindBidirectional(surowiec.getQuantityProperty(),converter);
        editOptionAction();
        series.setName("Zmiana ceny w czasie");
        for (int i = 0; i < surowiec.getChartData().size(); i++){
            series.getData().add(new XYChart.Data<>(i,surowiec.getChartData().get(i)));
        }
        wykres.getData().add(series);
    }
}
