package pl.symulacja.gieldy.controllers.content;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.symulacja.gieldy.controllers.MainController;
import pl.symulacja.gieldy.interfaces.ControllerTwo;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.podmiot.Inwestor;
import pl.symulacja.gieldy.utils.FxmlUtils;

/**
 * Kontroler pliku investor_list.fxml
 * @author Paweł
 */
public class InvestorListController implements ControllerTwo{
    private MainController mainController;
    @FXML private TableView<Inwestor> investor_table;
    @FXML private TableColumn<Inwestor, String> name;
    @FXML private TableColumn<Inwestor, String> surname;
    @FXML private TableColumn<Inwestor, String> pesel;
    @FXML private TableColumn<Inwestor, String> budget;
    @FXML private Button moreButton;
    @FXML private Button editButton;

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        name.setCellValueFactory(param -> param.getValue().getNameProperty());
        surname.setCellValueFactory(param -> param.getValue().getSurnameProperty());
        pesel.setCellValueFactory(param -> param.getValue().getPESELProperty());
        budget.setCellValueFactory(param -> param.getValue().getDomyslnaWaluta().getNameProperty());
        moreButton.disableProperty().bind(investor_table.getSelectionModel().selectedItemProperty().isNull());
        editButton.disableProperty().bind(investor_table.getSelectionModel().selectedItemProperty().isNull());
    }


    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Otworzenie panelu z informacjami
     */
    @FXML public void moreOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/investor_information_panel.fxml",investor_table.getSelectionModel().selectedItemProperty().get(),mainController,false);
    }

    /**
     * Otworzenie panelu informacji z możliwością edycji
     */
    @FXML public void editOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/investor_information_panel.fxml",investor_table.getSelectionModel().selectedItemProperty().get(),mainController,true);

    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu i kontrolera głównego okna
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData, MainController mainController) {
        this.mainController = mainController;
        investor_table.setItems(mainData.getInvestorList());
    }
}
