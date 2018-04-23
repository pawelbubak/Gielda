package pl.symulacja.gieldy.controllers.content;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.symulacja.gieldy.controllers.MainController;
import pl.symulacja.gieldy.interfaces.ControllerTwo;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.aktywo.Surowiec;
import pl.symulacja.gieldy.utils.FxmlUtils;

/**
 * Kontroler pliku resource_list.fxml
 * @author Paweł
 */
public class ResourceListController implements ControllerTwo{
    private MainController mainController;
    @FXML private TableView<Surowiec> resource_table;
    @FXML private TableColumn<Surowiec, String> name;
    @FXML private TableColumn<Surowiec, Number> cenaMax;
    @FXML private TableColumn<Surowiec, Number> cenaMin;
    @FXML private TableColumn<Surowiec, Number> cena;
    @FXML private TableColumn<Surowiec, String> jednostka;
    @FXML private Button moreButton;
    @FXML private Button editButton;

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        name.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        jednostka.setCellValueFactory(cellData -> cellData.getValue().getJednostkaHandlowaProperty());
        cenaMin.setCellValueFactory(cellData -> cellData.getValue().getMinPriceProperty());
        cenaMax.setCellValueFactory(cellData -> cellData.getValue().getMaxPriceProperty());
        cena.setCellValueFactory(cellData -> cellData.getValue().getPriceProperty());
        moreButton.disableProperty().bind(resource_table.getSelectionModel().selectedItemProperty().isNull());
        editButton.disableProperty().bind(resource_table.getSelectionModel().selectedItemProperty().isNull());
    }


    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Otworzenie panelu z informacjami
     */
    @FXML public void moreOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/resource_information_panel.fxml",resource_table.getSelectionModel().selectedItemProperty().get(),mainController,false);

    }

    /**
     * Otworzenie panelu informacji z możliwością edycji
     */
    @FXML public void editOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/resource_information_panel.fxml",resource_table.getSelectionModel().selectedItemProperty().get(),mainController,true);

    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu i kontrolera głównego okna
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData, MainController mainController) {
        this.mainController = mainController;
        resource_table.setItems(mainData.getResourceList());
    }
}
