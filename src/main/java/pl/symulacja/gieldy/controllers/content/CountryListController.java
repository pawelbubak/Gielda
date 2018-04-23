package pl.symulacja.gieldy.controllers.content;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.symulacja.gieldy.controllers.MainController;
import pl.symulacja.gieldy.interfaces.ControllerTwo;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.kraj.Kraj;
import pl.symulacja.gieldy.utils.FxmlUtils;

/**
 * Kontroler pliku country_list.fxml
 * @author Paweł
 */
public class CountryListController implements ControllerTwo{
    private MainController mainController;
    @FXML private TableView<Kraj> country_table;
    @FXML private TableColumn<Kraj,String> country_name;
    @FXML private TableColumn<Kraj,String> currency;
    @FXML private Button moreButton;
    @FXML private Button editButton;

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        this.country_name.setCellValueFactory(cellData-> cellData.getValue().getNameProperty());
        this.currency.setCellValueFactory(cellData-> cellData.getValue().getWaluta().getNameProperty());
    }


    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Otworzenie panelu z informacjami
     */
    @FXML public void moreOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/country_information_panel.fxml",country_table.getSelectionModel().selectedItemProperty().get(),mainController,false);
    }

    /**
     * Otworzenie panelu informacji z możliwością edycji
     */
    @FXML public void editOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/country_information_panel.fxml",country_table.getSelectionModel().selectedItemProperty().get(),mainController,true);
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu i kontrolera głównego okna
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData, MainController mainController) {
        this.mainController = mainController;
        this.country_table.setItems(mainData.getCountryList());
        moreButton.disableProperty().bind(country_table.getSelectionModel().selectedItemProperty().isNull());
        editButton.disableProperty().bind(country_table.getSelectionModel().selectedItemProperty().isNull());
    }
}
