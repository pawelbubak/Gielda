package pl.symulacja.gieldy.controllers.content;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.symulacja.gieldy.controllers.MainController;
import pl.symulacja.gieldy.interfaces.ControllerTwo;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.podmiot.FunduszInwestycyjny;
import pl.symulacja.gieldy.utils.FxmlUtils;

/**
 * Kontroler pliku fund_list.fxml
 * @author Paweł
 */
public class FundListController implements ControllerTwo{
    private MainController mainController;
    @FXML private TableView<FunduszInwestycyjny> investor_table;
    @FXML private TableColumn<FunduszInwestycyjny, String> fundName;
    @FXML private TableColumn<FunduszInwestycyjny, String> name;
    @FXML private TableColumn<FunduszInwestycyjny, String> surname;
    @FXML private TableColumn<FunduszInwestycyjny, String> currency;
    @FXML private Button moreButton;
    @FXML private Button editButton;

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize() {
        fundName.setCellValueFactory(param -> param.getValue().getFundNameProperty());
        name.setCellValueFactory(param -> param.getValue().getNameProperty());
        surname.setCellValueFactory(param -> param.getValue().getSurnameProperty());
        currency.setCellValueFactory(param -> param.getValue().getDomyslnaWaluta().getNameProperty());
        moreButton.disableProperty().bind(investor_table.getSelectionModel().selectedItemProperty().isNull());
        editButton.disableProperty().bind(investor_table.getSelectionModel().selectedItemProperty().isNull());
    }


    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Otworzenie panelu z informacjami
     */
    @FXML public void moreOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/fund_information_panel.fxml",investor_table.getSelectionModel().selectedItemProperty().get(),mainController,false);
    }

    /**
     * Otworzenie panelu informacji z możliwością edycji
     */
    @FXML public void editOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/fund_information_panel.fxml",investor_table.getSelectionModel().selectedItemProperty().get(),mainController,true);

    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu i kontrolera głównego okna
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData, MainController mainController) {
        this.mainController = mainController;
        investor_table.setItems(mainData.getFundList());
    }
}
