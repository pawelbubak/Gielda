package pl.symulacja.gieldy.controllers.content;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pl.symulacja.gieldy.controllers.MainController;
import pl.symulacja.gieldy.interfaces.ControllerTwo;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.gielda.GieldaPapierowWartosciowych;
import pl.symulacja.gieldy.data.indeks.Indeks;
import pl.symulacja.gieldy.utils.FxmlUtils;

/**
 * Kontroler pliku index_list.fxml
 * @author Paweł
 */
public class IndexListController implements ControllerTwo{
    private MainController mainController;
    @FXML private TableView<Indeks> index_table;
    @FXML private TableColumn<Indeks, String> table_name;
    @FXML private TableColumn<Indeks, Number> table_score;
    @FXML private ComboBox<GieldaPapierowWartosciowych> gieldaComboBox;
    @FXML private Button moreButton;
    @FXML private Button editButton;

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        table_name.setCellValueFactory(param -> param.getValue().getNameProperty());
        table_score.setCellValueFactory(param -> param.getValue().getScoreProperty());
        moreButton.disableProperty().bind(index_table.getSelectionModel().selectedItemProperty().isNull());
        editButton.disableProperty().bind(index_table.getSelectionModel().selectedItemProperty().isNull());
    }


    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Otworzenie panelu z informacjami
     */
    @FXML public void moreOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/index_information_panel.fxml",index_table.getSelectionModel().selectedItemProperty().get(),mainController,false);
    }

    /**
     * Otworzenie panelu informacji z możliwością edycji
     */
    @FXML public void editOnAction() {
        FxmlUtils.openMoreInformationPanel("/fxml/panel/index_information_panel.fxml",index_table.getSelectionModel().selectedItemProperty().get(),mainController,true);

    }


    //  OBSLUGA COMBOBOX
    /**
     * Wybiera giełdę z której wypisywane są indeksy
     */
    @FXML public void gieldaComboBoxOnAction() {
        index_table.setItems(gieldaComboBox.getValue().getIndexList());
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu i kontrolera głównego okna
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData, MainController mainController) {
        this.mainController = mainController;
        gieldaComboBox.setItems(mainData.getStockExchangeList());
    }
}
