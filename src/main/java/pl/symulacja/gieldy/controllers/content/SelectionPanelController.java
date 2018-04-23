package pl.symulacja.gieldy.controllers.content;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import pl.symulacja.gieldy.controllers.MainController;
import pl.symulacja.gieldy.interfaces.ControllerTwo;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.main.SerializeProgram;
import pl.symulacja.gieldy.utils.DialogsUtils;
import pl.symulacja.gieldy.utils.FxmlUtils;
import java.util.Optional;

/**
 * Kontroler pliku selection_panel.fxml
 * @author Paweł
 */
public class SelectionPanelController implements ControllerTwo {
    private MainDataClass mainData;
    private MainController mainController;

    public SelectionPanelController(){}

    public void initialize(){}

    /**
     * Otwiera panel akcji
     */
    public void openStockList() {
        FxmlUtils.openInformationPanelTwo("/fxml/content/stock_list.fxml", mainData, mainController);
    }

    /**
     * Otwiera panel funduszy
     */
    public void openFundList() {
        FxmlUtils.openInformationPanelTwo("/fxml/content/fund_list.fxml", mainData, mainController);
    }

    /**
     * Otwiera panel giełd
     */
    public void openStockExchangeList() {
        FxmlUtils.openInformationPanelTwo("/fxml/content/stock_exchange_list.fxml", mainData, mainController);
    }

    /**
     * Otwiera panel indeksów
     */
    public void openIndexList() {
        FxmlUtils.openInformationPanelTwo("/fxml/content/index_list.fxml", mainData, mainController);
    }

    /**
     * Otwiera panel inwestorów
     */
    public void openInvestorList() {
        FxmlUtils.openInformationPanelTwo("/fxml/content/investor_list.fxml", mainData, mainController);
    }

    /**
     * Otwiera panel krajów
     */
    public void openCountryList() {
        FxmlUtils.openInformationPanelTwo("/fxml/content/country_list.fxml", mainData, mainController);
    }

    /**
     * Otwiera panel rynków
     */
    public void openMarketList() {
        FxmlUtils.openInformationPanelTwo("/fxml/content/market_list.fxml", mainData, mainController);
    }

    /**
     * Otwiera panel surowców
     */
    public void openResourceList() {
        FxmlUtils.openInformationPanelTwo("/fxml/content/resource_list.fxml", mainData, mainController);
    }

    /**
     * Otwiera panel spółek
     */
    public void openCompanyList() {
        FxmlUtils.openInformationPanelTwo("/fxml/content/company_list.fxml", mainData, mainController);
    }

    /**
     * Otwiera panel walut
     */
    public void openCurrencyList() {
        FxmlUtils.openInformationPanelTwo("/fxml/content/currency_list.fxml", mainData, mainController);
    }

    /**
     * Zamyka program
     */
    public void exit() {
        Optional<ButtonType> result = DialogsUtils.confiramationDialog();
        if (result.get() == ButtonType.OK) {
            Platform.exit();
            SerializeProgram.save(mainData);
            System.exit(0);
        }
    }

    /**
     * Otwiera panel z wykresami
     */
    public void openCharts() {
        FxmlUtils.openInformationPanelTwo("/fxml/content/chart_panel.fxml", mainData, mainController);
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu i kontrolera głównego okna
     * @param mainData Dane programu
     */
    @Override
    public void setMainData(MainDataClass mainData, MainController mainController) {
        this.mainData = mainData;
        this.mainController = mainController;
    }
}
