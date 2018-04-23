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
import pl.symulacja.gieldy.data.spolka.Spolka;

/**
 * Kontroler pliku remove_company.fxml
 * @author Paweł
 */
public class RemoveCompanyController implements Controller{
    @FXML private Button accept_button;
    @FXML private ComboBox<Indeks> indeksComboBox;
    @FXML private ComboBox<Spolka> spolkaComboBox;
    @FXML private ComboBox<GieldaPapierowWartosciowych> gieldaComboBox;
    private BooleanProperty gieldaBooleanProperty = new SimpleBooleanProperty(this,"gieldaBooleanProperty",true);
    private BooleanProperty indeksBooleanProperty = new SimpleBooleanProperty(this,"indeksBooleanProperty",true);
    private BooleanProperty spolkaBooleanProperty = new SimpleBooleanProperty(this,"spolkaBooleanProperty",true);

    /**
     * Inicializuje i binduje pola
     */
    public void initialize(){
        accept_button.disableProperty().bind(spolkaBooleanProperty.or(indeksBooleanProperty).or(gieldaBooleanProperty));
    }

    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Zatwierdzenie usunięcia obiektu
     */
    public void acceptButtonOnAction() {
        Spolka spolka = spolkaComboBox.getValue();
        spolka.setKill(true);
        spolka.setUsuwanyValue(true);
        spolka.getStockExchange().usunJednostki("Akcja "+spolka.getNamePropertyValue());
        spolka.getStockExchange().getIndexList().forEach(indeks -> indeks.getCompanyList().remove(spolka));
        Aktywo.getMainDataClass().getCompanyList().remove(spolka);
    }


    //  OBSLUGA COMBOBOX
    /**
     * Aktywacja przycisku usuwania
     */
    public void indeksComboBoxOnAction() {
        Indeks indeks = indeksComboBox.getValue();
        if (indeks != null){
            spolkaComboBox.setItems(indeks.getCompanyList());
            indeksBooleanProperty.setValue(false);
        }
        else
            indeksBooleanProperty.setValue(true);
    }

    /**
     * Aktywacja przycisku usuwania
     */
    public void spolkaComboBoxOnAction() {
        if (spolkaComboBox.getSelectionModel().getSelectedItem() != null)
            spolkaBooleanProperty.setValue(false);
        else
            spolkaBooleanProperty.setValue(true);
    }

    /**
     * Aktywacja przycisku usuwania
     */
    public void gieldaComboBoxOnAction() {
        GieldaPapierowWartosciowych gielda = gieldaComboBox.getValue();
        if (gielda != null) {
            indeksComboBox.setItems(gielda.getIndexList());
            gieldaBooleanProperty.set(false);
        }
        else
            gieldaBooleanProperty.set(true);
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
