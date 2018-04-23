package pl.symulacja.gieldy.controllers.content;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import pl.symulacja.gieldy.controllers.MainController;
import pl.symulacja.gieldy.interfaces.ControllerTwo;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.aktywo.JednostkaFunduszu;
import pl.symulacja.gieldy.data.aktywo.Surowiec;
import pl.symulacja.gieldy.data.aktywo.Waluta;
import pl.symulacja.gieldy.data.spolka.Spolka;

import java.util.Comparator;

/**
 * Kontroler pliku chart_panel.fxml
 * @author Paweł
 */
public class ChartPanelController implements ControllerTwo{
    private MainDataClass mainData;
    @FXML private ListView dataTable;
    @FXML private LineChart<Number,Number> chart;
    @FXML private ComboBox<String> daneComboBox;
    private ObservableList<String> listaWyboru = FXCollections.observableArrayList( "Jednostki funduszu", "Spółki", "Surowce", "Waluty", "Indeks TOP3", "Indeks TOP10");

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        daneComboBox.setItems(listaWyboru);
    }


    //  OBSLUGA COMBOBOX
    /**
     * Wyświetla w liście dane które zostały wybrane w ComboBox
     */
    public void daneComboBoxOnAction() {
        switch (daneComboBox.getValue()){
            case "Jednostki funduszu":
                dataTable.setItems(mainData.getFundUnitList());
                break;
            case "Spółki":
                dataTable.setItems(mainData.getCompanyList());
                break;
            case "Surowce":
                dataTable.setItems(mainData.getResourceList());
                break;
            case "Waluty":
                dataTable.setItems(mainData.getCurrencyList());
                break;
            case "Indeks TOP3":

                dataTable.setItems(writeTop3());
                break;
            case "Indeks TOP10":
                dataTable.setItems(writeTop10());
                break;
        }
    }

    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Dodanie pojedynczej serii danych do wykresu
     */
    public void oneButtonOnAction() {
        if (dataTable.getSelectionModel().getSelectedItem() != null) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Zmiana ceny w czasie");
            if (dataTable.getSelectionModel().getSelectedItem() instanceof Aktywo) {
                series.setName(((Aktywo) dataTable.getSelectionModel().getSelectedItem()).getNamePropertyValue());
                for (int i = 0; i < ((Aktywo) dataTable.getSelectionModel().getSelectedItem()).getChartData().size(); i++) {
                    series.getData().add(new XYChart.Data<>(i, ((Aktywo) dataTable.getSelectionModel().getSelectedItem()).getChartData().get(i)));
                }
                chart.getData().add(series);
            } else {
                series.setName(((Spolka) dataTable.getSelectionModel().getSelectedItem()).getNamePropertyValue());
                for (int i = 0; i < ((Spolka) dataTable.getSelectionModel().getSelectedItem()).getChartData().size(); i++) {
                    series.getData().add(new XYChart.Data<>(i, ((Spolka) dataTable.getSelectionModel().getSelectedItem()).getChartData().get(i)));
                }
                chart.getData().add(series);
            }
        }
    }

    /**
     * Dodanie wszystkich serii danych do wykresu
     */
    public void AllButtonOnAction() {
        switch (daneComboBox.getValue()){
            case "Jednostki funduszu":
                for (JednostkaFunduszu jednostka: mainData.getFundUnitList()) {
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    series.setName(jednostka.getNamePropertyValue());
                    for (int i = 0; i < jednostka.getChartData().size(); i++) {
                        series.getData().add(new XYChart.Data<>(i, jednostka.getChartData().get(i)));
                    }
                    chart.getData().add(series);
                }
                break;
            case "Spółki":
                for (Spolka spolka: mainData.getCompanyList()) {
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    series.setName(spolka.getNamePropertyValue());
                    for (int i = 0; i < spolka.getChartData().size(); i++) {
                        series.getData().add(new XYChart.Data<>(i, spolka.getChartData().get(i)));
                    }
                    chart.getData().add(series);
                }
                break;
            case "Surowce":
                for (Surowiec surowiec: mainData.getResourceList()) {
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    series.setName(surowiec.getNamePropertyValue());
                    for (int i = 0; i < surowiec.getChartData().size(); i++) {
                        series.getData().add(new XYChart.Data<>(i, surowiec.getChartData().get(i)));
                    }
                    chart.getData().add(series);
                }
                break;
            case "Waluty":
                for (Waluta waluta: mainData.getCurrencyList()) {
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    series.setName(waluta.getNamePropertyValue());
                    for (int i = 0; i < waluta.getChartData().size(); i++) {
                        series.getData().add(new XYChart.Data<>(i, waluta.getChartData().get(i)));
                    }
                    chart.getData().add(series);
                }
                break;
            case "Indeks TOP3":
                for (Spolka spolka: writeTop3()){
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    series.setName(spolka.getNamePropertyValue());
                    for (int i = 0; i < spolka.getChartData().size(); i++) {
                        series.getData().add(new XYChart.Data<>(i, spolka.getChartData().get(i)));
                    }
                    chart.getData().add(series);
                }
                break;
            case "Indeks TOP10":
                for (Spolka spolka: writeTop10()){
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    series.setName(spolka.getNamePropertyValue());
                    for (int i = 0; i < spolka.getChartData().size(); i++) {
                        series.getData().add(new XYChart.Data<>(i, spolka.getChartData().get(i)));
                    }
                    chart.getData().add(series);
                }
                break;
        }
    }

    /**
     * Dodanie pojedynczej serii danych do wykresu
     */
    @FXML public void clearButtonOnAction() {
        chart.getData().removeAll(chart.getData());
    }


    // DODATKOWE METODY POPRAWIAJACE CZYTELNOSC KODU
    private ObservableList<Spolka> writeTop3() {
        int x = mainData.getCompanyList().size();
        if (x > 3)
            x = 3;
        mainData.getCompanyList().sort(Comparator.comparing(spolka -> spolka.getPricePropertyValue()));
        ObservableList<Spolka> observableList = FXCollections.observableArrayList();
        for (int i = 0; i < x; i++){
            observableList.add(mainData.getCompanyList().get(i));
        }
        return observableList;
    }

    private ObservableList<Spolka> writeTop10() {
        int y = mainData.getCompanyList().size();
        if (y > 10)
            y = 10;
        mainData.getCompanyList().sort(Comparator.comparing(spolka -> spolka.getPricePropertyValue()));
        ObservableList<Spolka> observableList1 = FXCollections.observableArrayList();
        for (int i = 0; i < y; i++){
            observableList1.add(mainData.getCompanyList().get(i));
        }
        return observableList1;
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu i kontrolera głównego okna
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData, MainController mainController) {
        this.mainData = mainData;
    }
}
