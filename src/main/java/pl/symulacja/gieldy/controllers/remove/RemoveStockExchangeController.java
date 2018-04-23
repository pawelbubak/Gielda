package pl.symulacja.gieldy.controllers.remove;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import pl.symulacja.gieldy.interfaces.Controller;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.gielda.GieldaPapierowWartosciowych;
import pl.symulacja.gieldy.data.indeks.Indeks;
import pl.symulacja.gieldy.exceptions.DeleteItemException;
import pl.symulacja.gieldy.utils.DialogsUtils;

/**
 * Kontroler pliku remove_stock_exchange.fxml
 * @author Paweł
 */
public class RemoveStockExchangeController implements Controller{
    @FXML private Button accept_button;
    @FXML private ComboBox<GieldaPapierowWartosciowych> gieldaComboBox;
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
            Aktywo.getMainDataClass().getStockExchangeList().remove(gieldaComboBox.getValue());
        } catch (DeleteItemException e) {
            DialogsUtils.errorDialog(e.getWiadomosc());
        }
    }


    //  OBSLUGA COMBOBOX
    /**
     * Aktywacja przycisku usuwania
     */
    @FXML public void gieldaComboBoxOnAction() {
        if (gieldaComboBox.getValue() != null)
            chooseBooleanProperty.set(false);
        else
            chooseBooleanProperty.set(true);
    }


    //  SPRAWDZENIE MOZLIWOSCI UZYCIA WPROWADZONYCH DANYCH PRZEZ UZYTKOWNIKA
    /**
     * Sprawdzenie czy można usunąć giełdę
     * @throws DeleteItemException Nie można usunąć giełdy
     */
    private void sprawdzMozliwoscUsuniecia() throws DeleteItemException {
        int i = 0, j = 0;
        for (Indeks indeks : gieldaComboBox.getValue().getIndexList()) {
            i++;
            if (indeks.getCompanyList().isEmpty())
                j++;
        }
        if (i != j)
            throw new DeleteItemException("Nie można usunąć Giełdy, ponieważ przypisana jest do niej \nco najmniej jedna Spółka");
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData) {
        gieldaComboBox.setItems(mainData.getStockExchangeList());
    }
}
