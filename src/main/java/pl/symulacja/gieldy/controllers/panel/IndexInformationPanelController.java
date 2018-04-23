package pl.symulacja.gieldy.controllers.panel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import pl.symulacja.gieldy.interfaces.ControllerThree;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.indeks.Indeks;
import pl.symulacja.gieldy.data.spolka.Spolka;
import pl.symulacja.gieldy.exceptions.DeleteItemException;
import pl.symulacja.gieldy.utils.DialogsUtils;

/**
 * Kontroler pliku index_information_panel.fxml
 * @author Paweł
 */
public class IndexInformationPanelController implements ControllerThree{
    private boolean editOption = false;
    private Indeks indeks;
    @FXML private TextField namelabel;
    @FXML private TextField score;
    @FXML private TableView<Spolka> spolka_table;
    @FXML private TableColumn<Spolka, String> table_name;
    @FXML private Button saveButton;
    @FXML private Button removeButton;
    StringConverter converter = new NumberStringConverter();

    @FXML public void initialize(){
        table_name.setCellValueFactory(param -> param.getValue().getNameProperty());
    }

    /**
     * Otwiera panel w odpowiednim trybie
     */
    private void editOptionAction(){
        saveButton.setVisible(editOption);
        removeButton.setVisible(editOption);
        namelabel.setDisable(!editOption);
        if (editOption){
            StringProperty nameProperty = new SimpleStringProperty(this, "nameProperty", indeks.getNamePropertyValue());
            namelabel.textProperty().bindBidirectional(nameProperty);
        }
        else {
            namelabel.textProperty().bindBidirectional(indeks.getNameProperty());
        }
    }

    /**
     * Zapisuje zmiany
     */
    @FXML public void saveOnAction() {
        indeks.setNamePropertyValue(namelabel.getText());
    }

    /**
     * Usuwa obiekt
     */
    @FXML public void removeOnAction() {
        try {
            sprawdzCzyMoznaUsunac();
            Aktywo.getMainDataClass().getStockExchangeList().forEach(gieldaPapierowWartosciowych -> gieldaPapierowWartosciowych.getIndexList().remove(indeks));
        } catch (DeleteItemException e) {
            DialogsUtils.errorDialog(e.getWiadomosc());
        }
    }

    /**
     * Sprawdza czy można usunąć obiekt
     * @throws DeleteItemException Nie można usunąć obiektu
     */
    private void sprawdzCzyMoznaUsunac() throws DeleteItemException {
        if (!indeks.getCompanyList().isEmpty())
            throw new DeleteItemException("Nie można usunąć indeksu, ponieważ znajdują się w nim spółki.");
    }

    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór obiektu do wyświetlenia oraz trybu otwarcia panelu
     * @param object Obiekt do wyświetlenia
     * @param choose Tryb otwarcia panelu
     */
    @Override
    public void setData(Object object, boolean choose) {
        this.editOption = choose;
        this.indeks = (Indeks) object;
        spolka_table.setItems(this.indeks.getCompanyList());
        score.textProperty().bindBidirectional(indeks.getScoreProperty(),converter);
        editOptionAction();
    }
}
