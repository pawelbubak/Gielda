package pl.symulacja.gieldy.controllers.remove;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import pl.symulacja.gieldy.interfaces.Controller;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.gielda.GieldaPapierowWartosciowych;
import pl.symulacja.gieldy.data.kraj.Kraj;
import pl.symulacja.gieldy.exceptions.DeleteItemException;
import pl.symulacja.gieldy.utils.DialogsUtils;

/**
 * Kontroler pliku remove_country.fxml
 * @author Paweł
 */
public class RemoveCountryController implements Controller{
    private MainDataClass mainData;
    @FXML private Button accept_button;
    @FXML private ComboBox<Kraj> krajComboBox;
    private BooleanProperty chooseBooleanProperty = new SimpleBooleanProperty(this,"choose",true);

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){
        accept_button.disableProperty().bind(chooseBooleanProperty);
    }


    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Zatwierdzenie usunięcia obiektu
     */
    @FXML public void acceptButtonOnAction() {
        try {
            sprawdzMozliwoscUsuniecia();
            krajComboBox.getValue().getWaluta().getCountryList().remove(krajComboBox.getValue());
            mainData.getCountryList().remove(krajComboBox.getValue());
        } catch (DeleteItemException e) {
            DialogsUtils.errorDialog(e.getWiadomosc());
        }
    }

    //  SPRAWDZENIE MOZLIWOSCI UZYCIA WPROWADZONYCH DANYCH PRZEZ UZYTKOWNIKA
    /**
     * Sprawdzenie czy można usunąć kraj
     * @throws DeleteItemException Nie można usunąć kraju
     */
    private void sprawdzMozliwoscUsuniecia() throws DeleteItemException {
        for (GieldaPapierowWartosciowych gielda: mainData.getStockExchangeList())
            if (gielda.getCountry().equals(krajComboBox.getValue()))
                throw new DeleteItemException("Nie można usunąć kraju, ponieważ przypisana jest do niego \nco najmniej jedna Giełda Papierów Wartościowych");
    }


    //  OBSLUGA COMBOBOX
    /**
     * Aktywacja przycisku usuwania
     */
    @FXML public void krajComboBoxOnAction() {
        if (krajComboBox.getValue() != null)
            chooseBooleanProperty.set(false);
        else
            chooseBooleanProperty.set(true);
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData) {
        this.mainData = mainData;
        krajComboBox.setItems(mainData.getCountryList());

    }
}
