package pl.symulacja.gieldy.controllers.panel;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import pl.symulacja.gieldy.interfaces.ControllerThree;
import pl.symulacja.gieldy.data.aktywo.Akcja;

/**
 * Kontroler pliku stock_information_information_panel.fxml
 * @author Paweł
 */
public class StockInformationPanelController implements ControllerThree{
    @FXML private TextField namelabel;
    @FXML private TextField price;
    @FXML private TextField max_price;
    @FXML private TextField min_price;
    @FXML private TextField owner;
    @FXML private TextField quantity;
    @FXML private LineChart<Number,Number> wykres;
    private XYChart.Series<Number,Number> series = new XYChart.Series<>();
    private Akcja akcja;
    @FXML
    public void initialize(){
    }

    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór obiektu do wyświetlenia oraz trybu otwarcia panelu
     * @param object Obiekt do wyświetlenia
     * @param choose Tryb otwarcia panelu
     */
    @Override
    public void setData(Object object, boolean choose) {
        this.akcja = (Akcja)object;
        StringConverter converter = new NumberStringConverter();
        namelabel.textProperty().bind(akcja.getNameProperty());
        price.textProperty().bindBidirectional(akcja.getPriceProperty(),converter);
        max_price.textProperty().bindBidirectional(akcja.getMaxPriceProperty(),converter);
        min_price.textProperty().bindBidirectional(akcja.getMinPriceProperty(),converter);
        owner.textProperty().bind(akcja.getStockExchange().getNameProperty());
        quantity.textProperty().bindBidirectional(akcja.getQuantityProperty(),converter);
        series.setName("Zmiana ceny w czasie");
        for (int i = 0; i < akcja.getChartData().size(); i++){
            series.getData().add(new XYChart.Data<>(i,akcja.getChartData().get(i)));
        }
        wykres.getData().add(series);
    }
}
