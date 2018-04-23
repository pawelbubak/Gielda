package pl.symulacja.gieldy.controllers.content;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.symulacja.gieldy.controllers.MainController;
import pl.symulacja.gieldy.interfaces.ControllerTwo;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.aktywo.Waluta;
import pl.symulacja.gieldy.utils.FxmlUtils;

/**
 * Kontroler pliku currency_list.fxml
 * @author Paweł
 */
public class CurrencyListController implements ControllerTwo{
    private MainController mainController;
    @FXML private TableView<Waluta> currency_table;
    @FXML private TableColumn<Waluta, String> name;
    @FXML private TableColumn<Waluta, Number> cenaMax;
    @FXML private TableColumn<Waluta, Number> cenaMin;
    @FXML private TableColumn<Waluta, Number> cena;

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        name.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        cenaMax.setCellValueFactory(cellData -> cellData.getValue().getMaxPriceProperty());
        cenaMin.setCellValueFactory(cellData -> cellData.getValue().getMinPriceProperty());
        cena.setCellValueFactory(cellData -> cellData.getValue().getPriceProperty());
    }


    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Otworzenie panelu z informacjami
     */
    @FXML public void moreOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/currency_information_panel.fxml",currency_table.getSelectionModel().selectedItemProperty().get(),mainController,false);

    }

    /**
     * Otworzenie panelu informacji z możliwością edycji
     */
    @FXML public void editOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/currency_information_panel.fxml",currency_table.getSelectionModel().selectedItemProperty().get(),mainController,true);

    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu i kontrolera głównego okna
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData, MainController mainController) {
        this.mainController = mainController;
        currency_table.setItems(mainData.getCurrencyList());
    }
}
