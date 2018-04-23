package pl.symulacja.gieldy.controllers.remove;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import pl.symulacja.gieldy.interfaces.Controller;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.podmiot.FunduszInwestycyjny;

/**
 * Kontroler pliku remove_stock_exchange.fxml
 * @author Paweł
 */
public class RemoveFundController implements Controller{
    @FXML private Button accept_button;
    @FXML private ComboBox<FunduszInwestycyjny> funduszInwestycyjnyComboBox;
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
        funduszInwestycyjnyComboBox.getValue().setKill(true);
        funduszInwestycyjnyComboBox.getValue().sellAll();
        Aktywo.getMainDataClass().getRynekFunduszyInwestycyjnych().usunJednostki("Jednostka " + funduszInwestycyjnyComboBox.getValue().getFundNamePropertyValue());
        Aktywo.getMainDataClass().getFundList().remove(funduszInwestycyjnyComboBox.getValue());
    }


    //  OBSLUGA COMBOBOX
    /**
     * Aktywacja przycisku usuwania
     */
    @FXML public void funduszComboBoxOnAction() {
        if (funduszInwestycyjnyComboBox.getValue() != null)
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
        funduszInwestycyjnyComboBox.setItems(mainData.getFundList());
    }
}
