package pl.symulacja.gieldy.controllers.panel;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import pl.symulacja.gieldy.interfaces.ControllerThree;
import pl.symulacja.gieldy.data.aktywo.Surowiec;
import pl.symulacja.gieldy.data.gielda.RynekSurowcow;

/**
 * Kontroler pliku resources_market_information_panel.fxml
 * @author Paweł
 */
public class ResourcesMarketInformationPanelController implements ControllerThree{
    private RynekSurowcow rynekSurowcow;
    @FXML private TextField namelabel;
    @FXML private TextField marza;
    @FXML private TableView<Surowiec> table;
    @FXML private TableColumn<Surowiec, String> surowiec_name;

    @FXML public void initialize(){
        surowiec_name.setCellValueFactory(param -> param.getValue().getNameProperty());
    }

    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór obiektu do wyświetlenia oraz trybu otwarcia panelu
     * @param object Obiekt do wyświetlenia
     * @param mainController Tryb otwarcia panelu
     */
    @Override
    public void setData(Object object, boolean mainController) {
        this.rynekSurowcow = (RynekSurowcow) object;
        StringConverter converter = new NumberStringConverter();
        namelabel.textProperty().bindBidirectional(rynekSurowcow.getNameProperty());
        marza.textProperty().bindBidirectional(rynekSurowcow.getMarzaProperty(),converter);
        table.setItems(rynekSurowcow.getResourcesList());
    }
}
