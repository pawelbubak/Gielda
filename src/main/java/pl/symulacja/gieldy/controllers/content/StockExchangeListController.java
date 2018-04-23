package pl.symulacja.gieldy.controllers.content;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.symulacja.gieldy.controllers.MainController;
import pl.symulacja.gieldy.interfaces.ControllerTwo;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.gielda.GieldaPapierowWartosciowych;
import pl.symulacja.gieldy.utils.FxmlUtils;

/**
 * Kontroler pliku stock_exchange_list.fxml
 * @author Paweł
 */
public class StockExchangeListController implements ControllerTwo{
    private MainController mainController;
    @FXML private TableView<GieldaPapierowWartosciowych> investor_table;
    @FXML private TableColumn<GieldaPapierowWartosciowych, String> name;
    @FXML private TableColumn<GieldaPapierowWartosciowych, String> country;
    @FXML private TableColumn<GieldaPapierowWartosciowych, String> town;
    @FXML private TableColumn<GieldaPapierowWartosciowych, String> street;
    @FXML private TableColumn<GieldaPapierowWartosciowych, Number> marza;
    @FXML private Button moreButton;
    @FXML private Button editButton;

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        name.setCellValueFactory(param -> param.getValue().getNameProperty());
        country.setCellValueFactory(param -> param.getValue().getCountry().getNameProperty());
        town.setCellValueFactory(param -> param.getValue().getTownProperty());
        street.setCellValueFactory(param -> param.getValue().getStreetProperty());
        marza.setCellValueFactory(param -> param.getValue().getMarzaProperty());
        moreButton.disableProperty().bind(investor_table.getSelectionModel().selectedItemProperty().isNull());
        editButton.disableProperty().bind(investor_table.getSelectionModel().selectedItemProperty().isNull());
    }


    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Otworzenie panelu z informacjami
     */
    @FXML public void moreOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/stock_exchange_information_panel.fxml",investor_table.getSelectionModel().selectedItemProperty().get(),mainController,false);
    }

    /**
     * Otworzenie panelu informacji z możliwością edycji
     */
    @FXML public void editOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/stock_exchange_information_panel.fxml",investor_table.getSelectionModel().selectedItemProperty().get(),mainController,true);
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu i kontrolera głównego okna
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData, MainController mainController) {
        this.mainController = mainController;
        investor_table.setItems(mainData.getStockExchangeList());
    }
}
