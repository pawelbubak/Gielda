package pl.symulacja.gieldy.controllers.panel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import pl.symulacja.gieldy.interfaces.ControllerThree;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.gielda.GieldaPapierowWartosciowych;
import pl.symulacja.gieldy.data.kraj.Kraj;
import pl.symulacja.gieldy.exceptions.DeleteItemException;
import pl.symulacja.gieldy.utils.DialogsUtils;

/**
 * Kontroler pliku country_information_panel.fxml
 * @author Paweł
 */
public class CountryInformationPanelController implements ControllerThree {
    private boolean editOption = false;
    private Kraj kraj = new Kraj();
    @FXML private TextField nameLabel;
    @FXML private TextField walutaLabel;
    @FXML private Button saveButton;
    @FXML private Button removeButton;
    private StringProperty nameProperty = new SimpleStringProperty(this,"nameProperty");

    /**
     * Inicjalizuje panel
     */
    @FXML public void initialize(){
        saveButton.disableProperty().bind(nameLabel.textProperty().isEmpty());
    }

    /**
     * Zapisuje zmiany
     */
    public void saveOnAction() {
        kraj.setNamePropertyValue(nameProperty.get());
    }

    /**
     * Otwiera panel w odpowiednim trybie
     */
    private void editOptionAction() {
        saveButton.setVisible(editOption);
        removeButton.setVisible(editOption);
        nameLabel.setDisable(!editOption);
        if (editOption){
            nameProperty = new SimpleStringProperty(this,"nameProperty",kraj.getNamePropertyValue());
            nameLabel.textProperty().bindBidirectional(nameProperty);
        }
        else
            nameLabel.textProperty().bindBidirectional(this.kraj.getNameProperty());
    }

    /**
     * Usuwa obiekt
     */
    public void removeOnAction() {
        try {
            sprawdzMozliwoscUsuniecia();
            kraj.getWaluta().getCountryList().remove(kraj);
            Aktywo.getMainDataClass().getCountryList().remove(kraj);
        } catch (DeleteItemException e) {
            DialogsUtils.errorDialog(e.getWiadomosc());
        }
    }

    /**
     * Sprawdza czy można usunąć obiekt
     * @throws DeleteItemException Nie można usunąc obiektu
     */
    private void sprawdzMozliwoscUsuniecia() throws DeleteItemException {
        for (GieldaPapierowWartosciowych gielda: Aktywo.getMainDataClass().getStockExchangeList())
            if (gielda.getCountry().equals(kraj))
                throw new DeleteItemException("Nie można usunąć kraju, ponieważ przypisana jest do niego \nco najmniej jedna Giełda Papierów Wartościowych");
    }

    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór obiektu do wyświetlenia oraz trybu otwarcia panelu
     * @param kraj Obiekt do wyświetlenia
     * @param choose Tryb otwarcia panelu
     */
    @Override
    public void setData(Object kraj, boolean choose) {
        editOption = choose;
        this.kraj = (Kraj) kraj;
        walutaLabel.textProperty().bindBidirectional(this.kraj.getWaluta().getNameProperty());
        editOptionAction();
    }
}
