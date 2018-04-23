package pl.symulacja.gieldy.controllers.content;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.symulacja.gieldy.controllers.MainController;
import pl.symulacja.gieldy.interfaces.ControllerTwo;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.gielda.Gielda;
import pl.symulacja.gieldy.data.gielda.RynekSurowcow;
import pl.symulacja.gieldy.data.gielda.RynekWalut;
import pl.symulacja.gieldy.utils.FxmlUtils;

/**
 * Kontroler pliku market_list.fxml
 * @author Paweł
 */
public class MarketListController implements ControllerTwo{
    private MainController mainController;
    @FXML private TableView<Gielda> market_table;
    @FXML private TableColumn<Gielda, String> name;
    @FXML private TableColumn<Gielda, Number> marza;
    @FXML private Button moreButton;
    @FXML private Button editButton;

    /**
     * Inicializuje i binduje pola
     */
    public void initialize(){
        moreButton.disableProperty().bind(market_table.getSelectionModel().selectedItemProperty().isNull());
        editButton.disableProperty().bind(market_table.getSelectionModel().selectedItemProperty().isNull());
    }


    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Otworzenie panelu z informacjami
     */
    @FXML public void moreOnAction() {
        if (market_table.getSelectionModel().selectedItemProperty().get() instanceof RynekSurowcow)
            FxmlUtils.openMoreInformationPanel("/fxml/panel/resources_market_information_panel.fxml",market_table.getSelectionModel().selectedItemProperty().get(),mainController,false);
        else if (market_table.getSelectionModel().selectedItemProperty().get() instanceof RynekWalut)
            FxmlUtils.openMoreInformationPanel("/fxml/panel/currency_market_information_panel.fxml",market_table.getSelectionModel().selectedItemProperty().get(),mainController,false);
        else
            FxmlUtils.openMoreInformationPanel("/fxml/panel/funds_market_information_panel.fxml",market_table.getSelectionModel().selectedItemProperty().get(),mainController,false);

    }

    /**
     * Brak
     */
    @FXML public void editOnAction() {
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu i kontrolera głównego okna
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData, MainController mainController) {
        this.mainController = mainController;
        this.market_table.setItems(mainData.getMarketList());
        this.name.setCellValueFactory(cellData-> cellData.getValue().getNameProperty());
        this.marza.setCellValueFactory(cellData-> cellData.getValue().getMarzaProperty());
    }
}
