package pl.symulacja.gieldy.controllers.panel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import pl.symulacja.gieldy.interfaces.ControllerThree;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.spolka.Spolka;
import pl.symulacja.gieldy.exceptions.WrongKindException;
import pl.symulacja.gieldy.utils.DialogsUtils;

/**
 * Kontroler pliku company_information_panel.fxml
 * @author Paweł
 */
public class CompanyInformationPanelController implements ControllerThree{
    private boolean editOption = false;
    private Spolka spolka;
    @FXML private TextField namelabel;
    @FXML private TextField data_wyceny;
    @FXML private TextField kurs;
    @FXML private TextField kurs_otwarcia;
    @FXML private TextField max_kurs;
    @FXML private TextField min_kurs;
    @FXML private TextField liczba_akcji;
    @FXML private TextField zysk;
    @FXML private TextField przychod;
    @FXML private TextField wkapital;
    @FXML private TextField zkapital;
    @FXML private TextField wolumen;
    @FXML private TextField obroty;
    @FXML private TextField priceToBuy;
    @FXML private Button removeButton;
    @FXML private Button buyButton;
    @FXML private VBox boxToBuy;
    private StringProperty buyProperty = new SimpleStringProperty(this,"nameProperty");

    /**
     * Inicjalizuje panel
     */
    @FXML public void initialize(){
        buyButton.disableProperty().bind(buyProperty.isEmpty());
    }

    /**
     * otwiera panel w odpowiednim trybie
     */
    private void editOptionAction() {
        removeButton.setVisible(editOption);
        buyButton.setVisible(editOption);
        boxToBuy.setVisible(editOption);
        priceToBuy.textProperty().bindBidirectional(buyProperty);
        namelabel.textProperty().bindBidirectional(spolka.getNameProperty());
    }

    /**
     * usuwa obiekt
     */
    public void removeOnAction() {
        spolka.setKill(true);
        spolka.setUsuwanyValue(true);
        spolka.getStockExchange().usunJednostki("Akcja "+spolka.getNamePropertyValue());
        spolka.getStockExchange().getIndexList().forEach(indeks -> indeks.getCompanyList().remove(spolka));
        Aktywo.getMainDataClass().getCompanyList().remove(spolka);
    }

    /**
     * Wykupuje akcje na żądanie
     */
    public void buyOnAction() {
        double price = 0;
        try {
            price = sprawdzCeneWykupu();
            if (price <= 0 || price > 10)
                throw new WrongKindException("Proszę podać cenę z przedziału (0,10]");
            spolka.wykup(price);
            priceToBuy.clear();
        } catch (WrongKindException e) {
            DialogsUtils.errorDialog(e.getWiadomosc());
        }
    }

    /**
     * Sparwdza czy cena wykupu jest poprawna
     * @return Cena wykupu akcji
     * @throws WrongKindException Błedna cena
     */
    private double sprawdzCeneWykupu() throws WrongKindException {
        try{
            double price = Double.parseDouble(priceToBuy.getText());
            return price;
        } catch (Exception e){
            throw new WrongKindException("Proszę podać cenę z przedziału (0,10]");
        }
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór obiektu do wyświetlenia oraz trybu otwarcia panelu
     * @param object Obiekt do wyświetlenia
     * @param choose Tryb otwarcia panelu
     */
    @Override
    public void setData(Object object, boolean choose) {
        this.spolka = (Spolka) object;
        this.editOption = choose;
        StringConverter converter = new NumberStringConverter();
        data_wyceny.textProperty().bindBidirectional(new SimpleStringProperty(spolka.getValuationDate().toString()));
        kurs.textProperty().bindBidirectional(spolka.getPriceProperty(),converter);
        kurs_otwarcia.textProperty().bindBidirectional(spolka.getOpeningPriceProperty(),converter);
        max_kurs.textProperty().bindBidirectional(spolka.getMaxPriceProperty(),converter);
        min_kurs.textProperty().bindBidirectional(spolka.getMinPriceProperty(),converter);
        liczba_akcji.textProperty().bindBidirectional(spolka.getQuantityProperty(),converter);
        zysk.textProperty().bindBidirectional(spolka.getProfitProperty(),converter);
        przychod.textProperty().bindBidirectional(spolka.getIncomeProperty(),converter);
        wkapital.textProperty().bindBidirectional(spolka.getEquityCapitalProperty(),converter);
        zkapital.textProperty().bindBidirectional(spolka.getShareCapitalProperty(),converter);
        wolumen.textProperty().bindBidirectional(spolka.getVolumeProperty(),converter);
        obroty.textProperty().bindBidirectional(spolka.getSalesProperty(),converter);
        editOptionAction();
    }
}
