package pl.symulacja.gieldy.controllers.add;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import pl.symulacja.gieldy.interfaces.Controller;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.utils.FxmlUtils;

/**
 * Kontroler pliku main_add.fxml, zarządza wyświetlaniem paneli dodawania odpowiednich obiektów
 * @author Paweł
 */
public class MainAddController implements Controller {
    private MainDataClass mainData;
    @FXML private Button accept_button;
    @FXML private BorderPane add_content;
    @FXML private ComboBox<String> wybor;
    private ObservableList<String> listaWyboru = FXCollections.observableArrayList("Spółka","Giełda","Waluta","Surowiec","Inwestor","Fundusz Inwestycyjny","Indeks");

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        wybor.setItems(listaWyboru);
        wybor.setValue("Spółka");
        accept_button.setDisable(true);
    }

    @FXML public void wyborButtonOnAction() {
        wyborComboBoxOnAction();
    }

    //  OBSLUGA COMBOBOX
    /**
     * Wyświetla odpowiedni panel
     */
    @FXML public void wyborComboBoxOnAction() {
        switch (wybor.getValue()) {
            case "Spółka":
                FxmlUtils.openInformationPanel("/fxml/add/add_company.fxml", mainData, add_content);
                break;
            case "Giełda":
                FxmlUtils.openInformationPanel("/fxml/add/add_stock_exchange.fxml", mainData, add_content);
                break;
            case "Waluta":
                FxmlUtils.openInformationPanel("/fxml/add/add_currency.fxml", mainData, add_content);
                break;
            case "Surowiec":
                FxmlUtils.openInformationPanel("/fxml/add/add_resource.fxml", mainData, add_content);
                break;
            case "Inwestor":
                FxmlUtils.openInformationPanel("/fxml/add/add_investor.fxml", mainData, add_content);
                break;
            case "Fundusz Inwestycyjny":
                FxmlUtils.openInformationPanel("/fxml/add/add_fund.fxml", mainData, add_content);
                break;
            case "Indeks":
                FxmlUtils.openInformationPanel("/fxml/add/add_index.fxml", mainData, add_content);
                break;
        }
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData) {
        this.mainData = mainData;
        wyborComboBoxOnAction();
    }
}
