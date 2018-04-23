package pl.symulacja.gieldy.controllers.content;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.symulacja.gieldy.controllers.MainController;
import pl.symulacja.gieldy.interfaces.ControllerTwo;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.aktywo.Akcja;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.aktywo.Surowiec;
import pl.symulacja.gieldy.data.aktywo.Waluta;
import pl.symulacja.gieldy.utils.FxmlUtils;

/**
 * Kontroler pliku stock_list.fxml
 * @author Paweł
 */
public class StockListController implements ControllerTwo{
    private MainController mainController;
    @FXML private TableView<Aktywo> main_table;
    @FXML private TableColumn<Aktywo,String> table_name;
    @FXML private TableColumn<Aktywo,String> table_owner;
    @FXML private TableColumn<Aktywo,Number> table_price;
    @FXML private TableColumn<Aktywo,Number> table_quantity;
    @FXML private Button moreButton;
    @FXML private Button editButton;

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        table_name.setCellValueFactory(param -> param.getValue().getNameProperty());
        table_owner.setCellValueFactory(param -> param.getValue().getStockExchange().getNameProperty());
        table_price.setCellValueFactory(param -> param.getValue().getPriceProperty());
        table_quantity.setCellValueFactory(param -> param.getValue().getQuantityProperty());
        moreButton.disableProperty().bind(main_table.getSelectionModel().selectedItemProperty().isNull());
        editButton.disableProperty().bind(main_table.getSelectionModel().selectedItemProperty().isNull());
    }


    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Otworzenie panelu z informacjami
     */
    @FXML public void moreOnAction() {
        if (main_table.getSelectionModel().selectedItemProperty().get() instanceof Akcja)
            FxmlUtils.openMoreInformationPanel("/fxml/panel/stock_information_panel.fxml",main_table.getSelectionModel().selectedItemProperty().get(),mainController,false);
        else if (main_table.getSelectionModel().selectedItemProperty().get() instanceof Surowiec)
            FxmlUtils.openMoreInformationPanel("/fxml/panel/resource_information_panel.fxml",main_table.getSelectionModel().selectedItemProperty().get(),mainController,false);
        else if (main_table.getSelectionModel().selectedItemProperty().get() instanceof Waluta)
            FxmlUtils.openMoreInformationPanel("/fxml/panel/currency_information_panel.fxml",main_table.getSelectionModel().selectedItemProperty().get(),mainController,false);
        else
            FxmlUtils.openMoreInformationPanel("/fxml/panel/fund_unit__information_panel.fxml",main_table.getSelectionModel().selectedItemProperty().get(),mainController,false);
    }

    /**
     * Otworzenie panelu informacji z możliwością edycji
     */
    @FXML public void editOnAction() {
        if (main_table.getSelectionModel().selectedItemProperty().get() instanceof Akcja)
            FxmlUtils.openMoreInformationPanel("/fxml/panel/stock_information_panel.fxml",main_table.getSelectionModel().selectedItemProperty().get(),mainController,true);
        else if (main_table.getSelectionModel().selectedItemProperty().get() instanceof Surowiec)
            FxmlUtils.openMoreInformationPanel("/fxml/panel/resource_information_panel.fxml",main_table.getSelectionModel().selectedItemProperty().get(),mainController,true);
        else if (main_table.getSelectionModel().selectedItemProperty().get() instanceof Waluta)
            FxmlUtils.openMoreInformationPanel("/fxml/panel/currency_information_panel.fxml",main_table.getSelectionModel().selectedItemProperty().get(),mainController,true);
        else
            FxmlUtils.openMoreInformationPanel("/fxml/panel/fund_unit__information_panel.fxml",main_table.getSelectionModel().selectedItemProperty().get(),mainController,true);
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu i kontrolera głównego okna
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData, MainController mainController) {
        this.mainController = mainController;
        main_table.setItems(mainData.getGlobalStockList());
    }
}
