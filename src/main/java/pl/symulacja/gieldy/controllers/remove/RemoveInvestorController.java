package pl.symulacja.gieldy.controllers.remove;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.podmiot.Inwestor;
import pl.symulacja.gieldy.interfaces.Controller;
import pl.symulacja.gieldy.data.MainDataClass;

/**
 * Kontroler pliku remove_investor.fxml
 * @author Paweł
 */
public class RemoveInvestorController implements Controller{
    @FXML private Button accept_button;
    @FXML private ComboBox<Inwestor> inwestorComboBox;
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
        inwestorComboBox.getValue().setKill(true);
        inwestorComboBox.getValue().sellAll();
        Aktywo.getMainDataClass().getInvestorList().remove(inwestorComboBox.getValue());
    }


    //  OBSLUGA COMBOBOX
    /**
     * Aktywacja przycisku usuwania
     */
    @FXML public void inwestorComboBoxOnAction() {
        if (inwestorComboBox.getValue() != null)
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
        inwestorComboBox.setItems(mainData.getInvestorList());
    }
}
