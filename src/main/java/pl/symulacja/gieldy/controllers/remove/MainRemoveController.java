package pl.symulacja.gieldy.controllers.remove;

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
 * Kontroler pliku main_remove.fxml, zarządza wyświetlaniem paneli usuwania obiektów
 * @author Paweł
 */
public class MainRemoveController implements Controller {
    private MainDataClass mainData;
    @FXML private Button accept_button;
    @FXML private BorderPane remove_content;
    @FXML private ComboBox<String> wybor;
    private ObservableList<String> listaWyboru = FXCollections.observableArrayList( "Fundusz Inwestycyjny", "Giełda", "Inwestor", "Kraj", "Surowiec", "Spółka", "Waluta");

    /**
     * Inicializuje i binduje pola
     */
    public void initialize() {
        wybor.setItems(listaWyboru);
        wybor.setValue("Fundusz Inwestycyjny");
        accept_button.setDisable(true);
    }

    public void wyborButtonOnAction() {
        wyborComboBoxOnAction();
    }

    //  OBSLUGA COMBOBOX
    /**
     * Wyświetla wybrany panel
     */
    public void wyborComboBoxOnAction() {
        switch (wybor.getValue()) {
            case "Spółka":
                FxmlUtils.openInformationPanel("/fxml/remove/remove_spolka.fxml", mainData, remove_content);
                break;
            case "Giełda":
                FxmlUtils.openInformationPanel("/fxml/remove/remove_gielda.fxml", mainData, remove_content);
                break;
            case "Waluta":
                FxmlUtils.openInformationPanel("/fxml/remove/remove_waluta.fxml", mainData, remove_content);
                break;
            case "Surowiec":
                FxmlUtils.openInformationPanel("/fxml/remove/remove_surowiec.fxml", mainData, remove_content);
                break;
            case "Inwestor":
                FxmlUtils.openInformationPanel("/fxml/remove/remove_inwestor.fxml", mainData, remove_content);
                break;
            case "Fundusz Inwestycyjny":
                FxmlUtils.openInformationPanel("/fxml/remove/remove_fundusz_inwestycyjny.fxml", mainData, remove_content);
                break;
            case "Kraj":
                FxmlUtils.openInformationPanel("/fxml/remove/remove_kraj.fxml", mainData, remove_content);
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
