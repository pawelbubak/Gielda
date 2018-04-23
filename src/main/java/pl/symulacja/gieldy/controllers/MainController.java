package pl.symulacja.gieldy.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.main.SerializeProgram;
import pl.symulacja.gieldy.main.SymulacjaGieldy;
import pl.symulacja.gieldy.utils.DialogsUtils;
import pl.symulacja.gieldy.utils.FxmlUtils;
import java.io.IOException;
import java.util.Optional;

/**
 * Kontroler plik main.fxml, zarządza całym oknem głównym
 * @author Paweł
 */
public class MainController {
    private MainDataClass mainData;
    private boolean startStop = true;
    @FXML private BorderPane information_container;
    @FXML private MenuItem play_menu_item;
    @FXML private MenuItem pause_menu_item;
    @FXML private Button play_button;
    @FXML private Button pause_button;
    @FXML private MenuItem file_close;
    @FXML private MenuItem file_save;
    @FXML private MenuItem edit_add;
    @FXML private MenuItem edit_remove;
    @FXML private MenuItem help_guide;
    @FXML private BorderPane listPane;
    @FXML private Label symulacja_label;
    @FXML private Label save_label;

    public MainController(){
    }

    /**
     * Inicializuje i ustawia akceleratory menu
     */
    @FXML public void initialize(){
        play_menu_item.setAccelerator(new KeyCodeCombination(KeyCode.O,KeyCombination.CONTROL_DOWN));
        pause_menu_item.setAccelerator(new KeyCodeCombination(KeyCode.P,KeyCombination.CONTROL_DOWN));
        file_save.setAccelerator(new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN));
        file_close.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
        edit_add.setAccelerator(new KeyCodeCombination(KeyCode.D,KeyCombination.CONTROL_DOWN));
        edit_remove.setAccelerator(new KeyCodeCombination(KeyCode.R,KeyCombination.CONTROL_DOWN));
        help_guide.setAccelerator(new KeyCodeCombination(KeyCode.H,KeyCombination.CONTROL_DOWN));
        save_label.setVisible(false);
    }

    //  USTAWIENIE FUNKCJONALNOSCI PRZYCISKOM
    /**
     * Wznawia symulacje
     */
    @FXML public void playSimulation() {
        changeStartStop();
        SymulacjaGieldy.symuluj = true;
        symulacja_label.setText("Stan: działa");
    }

    /**
     * Zatrzymuje symulacje
     */
    @FXML public void pauseSimulation() {
        changeStartStop();
        SymulacjaGieldy.symuluj = false;
        symulacja_label.setText("Stan: gotowy");
    }

    /**
     * Wznawia symulacje
     */
    @FXML public void playSimulationButton() {
        playSimulation();
    }

    /**
     * Zatrzymuje symulacje
     */
    @FXML public void PauseSimulationButton() {
        pauseSimulation();
    }

    /**
     * Zapisuje stan
     */
    @FXML public void zapisz() {
        save_label.setVisible(true);
        if (SymulacjaGieldy.symuluj)
            PauseSimulationButton();
        SerializeProgram.save(mainData);
        save_label.setVisible(false);
    }

    @FXML public void saveButtonOnAction() {
        zapisz();
    }

    /**
     * Zamyka program
     */
    @FXML public void closeApplication() {
        Optional<ButtonType> result = DialogsUtils.confiramationDialog();
        if (result.get() == ButtonType.OK) {
            Platform.exit();
            SerializeProgram.save(mainData);
            System.exit(0);
        }
    }

    /**
     * Otwiera panel dodawania elementu
     */
    @FXML public void addElement() {
        FxmlUtils.openInformationPanel("/fxml/add/main_add.fxml", mainData, information_container);
    }

    /**
     * Otwiera panel usuwania elementu
     */
    @FXML public void removeElement() {
        FxmlUtils.openInformationPanel("/fxml/remove/main_remove.fxml", mainData, information_container);
    }

    /**
     * Otwiera panel dodawania elementu
     */
    @FXML public void addElementButton() {
        addElement();
    }

    /**
     * Otwiera panel usuwania elementu
     */
    @FXML public void removeElementButton() {
        removeElement();
    }

    /**
     * Otwiera instrukcje
     */
    @FXML public void openGuide() {
        openInformationPanel("/fxml/main_information_panel.fxml");
    }

    /**
     * Otwiera okno z informacjami
     */
    @FXML public void about() {
        openInformationPanel("/fxml/main_information_panel.fxml");
        DialogsUtils.dialogAboutApplication();
    }

    /**
     * Otwiera panel wyboru
     */
    @FXML public void openSelectionPanel() {
        FxmlUtils.openInformationPanelTwo("/fxml/content/selection_panel.fxml", mainData, this);
    }

    /**
     * Otwiera panel z wszystkimi akcjami
     */
    @FXML public void openStockPanel() {
        FxmlUtils.openInformationPanelTwo("/fxml/content/stock_list.fxml", mainData, this);
    }


    // DODATKOWE METODY
    /**
     * Zmienia stan symulacji
     */
    private void changeStartStop(){
        if (this.startStop){
            this.pause_menu_item.setDisable(true);
            this.pause_button.setDisable(true);
            this.play_menu_item.setDisable(false);
            this.play_button.setDisable(false);
            this.startStop = !this.startStop;
        }
        else {
            this.pause_menu_item.setDisable(false);
            this.pause_button.setDisable(false);
            this.play_menu_item.setDisable(true);
            this.play_button.setDisable(true);
            this.startStop = !this.startStop;
        }
    }

    /**
     * Otwiera panel informacyjny
     * @param fxmlPath Scieżka do pliku
     */
    private void openInformationPanel(String fxmlPath){
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fxmlPath));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
        information_container.setCenter(root);
    }

    /**
     * Zwraca umiejscowienie panelu informacyjnego
     * @return Miejsce panelu
     */
    public BorderPane getInformation_container() {
        return information_container;
    }

    /**
     * Zwraca umijscowienie głównego panelu
     * @return Miejsce głównego panelu
     */
    public BorderPane getListPane() {
        return listPane;
    }


    //  USTAWIENIE REFERENCJI DO DANYCH
    /**
     * Odbiór instancji przechowującej wszystkie dane programu
     * @param mainData Dane programu
     */
    public void setMainData(MainDataClass mainData){
        this.mainData = mainData;
        FxmlUtils.openInformationPanelTwo("/fxml/content/stock_list.fxml", mainData, this);
    }
}
