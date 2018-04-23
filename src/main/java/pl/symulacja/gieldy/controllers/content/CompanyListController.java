package pl.symulacja.gieldy.controllers.content;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.symulacja.gieldy.controllers.MainController;
import pl.symulacja.gieldy.interfaces.ControllerTwo;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.spolka.Spolka;
import pl.symulacja.gieldy.utils.FxmlUtils;

/**
 * Kontroler pliku company_list.fxml
 * @author Paweł
 */
public class CompanyListController implements ControllerTwo{
    private MainController mainController;
    @FXML private TableView<Spolka> company_table;
    @FXML private TableColumn<Spolka, String> name;
    @FXML private TableColumn<Spolka, Number> cenaMax;
    @FXML private TableColumn<Spolka, Number> cenaMin;
    @FXML private TableColumn<Spolka, Number> cena;
    @FXML private TableColumn<Spolka, Number> income;
    @FXML private TableColumn<Spolka, Number> quantity;
    @FXML private Button moreButton;
    @FXML private Button editButton;

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        name.setCellValueFactory(param -> param.getValue().getNameProperty());
        cenaMax.setCellValueFactory(param -> param.getValue().getMaxPriceProperty());
        cenaMin.setCellValueFactory(param -> param.getValue().getMinPriceProperty());
        cena.setCellValueFactory(param -> param.getValue().getPriceProperty());
        income.setCellValueFactory(param -> param.getValue().getIncomeProperty());
        quantity.setCellValueFactory(param -> param.getValue().getQuantityProperty());
        moreButton.disableProperty().bind(company_table.getSelectionModel().selectedItemProperty().isNull());
        editButton.disableProperty().bind(company_table.getSelectionModel().selectedItemProperty().isNull());
    }


    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Otworzenie panelu z informacjami
     */
    @FXML public void moreOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/company_information_panel.fxml",company_table.getSelectionModel().selectedItemProperty().get(),mainController,false);
    }

    /**
     * Otworzenie panelu informacji z możliwością edycji
     */
    @FXML public void editOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/company_information_panel.fxml",company_table.getSelectionModel().selectedItemProperty().get(),mainController,true);
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu i kontrolera głównego okna
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData, MainController mainController) {
        this.mainController = mainController;
        company_table.setItems(mainData.getCompanyList());
    }
}
