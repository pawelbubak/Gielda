package pl.symulacja.gieldy.controllers.remove;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import pl.symulacja.gieldy.interfaces.Controller;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.aktywo.Surowiec;

/**
 * Kontroler pliku remove_resource.fxml
 * @author Paweł
 */
public class RemoveResourceController implements Controller{
    private MainDataClass mainData;
    @FXML private Button accept_button;
    @FXML private ComboBox<Surowiec> surowiecComboBox;
    private BooleanProperty chooseBooleanProperty = new SimpleBooleanProperty(this,"choose",true);

    /**
     * Inicializuje i binduje pola
     */
    @FXML public void initialize(){accept_button.disableProperty().bind(chooseBooleanProperty);}


    //  OBSLUGA COMBOBOX
    /**
     * Aktywacja przycisku usuwania
     */
    public void surowiecComboBoxOnAction() {
        if (surowiecComboBox.getValue() != null)
            chooseBooleanProperty.set(false);
        else
            chooseBooleanProperty.set(true);
    }

    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Zatwierdzenie usunięcia obiektu
     */
    @FXML public void acceptButtonOnAction() {
        mainData.getGlobalStockList().remove(surowiecComboBox.getValue());
        mainData.getRynekSurowcow().getList().remove(surowiecComboBox.getValue());
        mainData.getResourceList().remove(surowiecComboBox.getValue());
    }

    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData) {
        this.mainData = mainData;
        surowiecComboBox.setItems(mainData.getResourceList());
    }
}
