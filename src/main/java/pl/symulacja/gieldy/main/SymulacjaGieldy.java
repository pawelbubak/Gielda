package pl.symulacja.gieldy.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;
import pl.symulacja.gieldy.controllers.MainController;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.utils.DialogsUtils;
import pl.symulacja.gieldy.utils.FxmlUtils;
import pl.symulacja.gieldy.utils.RandomModule;
import java.util.Optional;

/**
 * Klasa główna wywołująca wątki przetwarzające dane oraz otiwera okno programu
 * @author Paweł
 */
public class SymulacjaGieldy extends Application {
    public static boolean symuluj = true;
    private Stage primaryStage;
    private MainDataClass mainData = new MainDataClass();
    private static final String SAMPLE_FXML = "/fxml/main.fxml";
    public static String MAIN_CSS = "/stylesheets/main.css";
    public static final String MAIN_ICON = "/icons/main_icon.png";

    /**
     * Obsługuje/wywołuje program
     * @param primaryStage Okno programu
     * @throws Exception Błąd programu
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        SerializeProgram.load(mainData);
        mainData.init();
        Aktywo.setMainDataClass(mainData);
        //  USTAWIENIA PRIMARY STAGE
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(FxmlUtils.getResourceBundle().getString("title.application"));
        this.primaryStage.getIcons().add(new Image(MAIN_ICON));
        this.primaryStage.setMinWidth(1300);
        this.primaryStage.setMinHeight(900);
        setCloseAction();
        //  POBRANIE PANE I KONTROLERA
        Pair<Pane, FXMLLoader> pair = FxmlUtils.fxmlLoader(SAMPLE_FXML);
        assert pair != null;
        Pane pane = pair.getKey();
        MainController mainController = pair.getValue().getController();
        mainController.setMainData(mainData);
        //  USTAWIENIE SCENY I ARKUSZA CSS
        Scene scene = new Scene(pane,1300,900);
        scene.getStylesheets().add(MAIN_CSS);
        primaryStage.setScene(scene);
        primaryStage.show();
        // LADUJE DANE LOSOWE I TWORZY WATKI OBSLUGUJACE DANE
        RandomModule.init();
        uruchomSymulacje();
    }

    /**
     * Uruchamia wszyskie wątki pomocnicze
     */
    private void uruchomSymulacje() {
        symulacjaRynku();
        symulacjaPodmiotu();
        symulacjaFunduszu();
        symulacjaSpolki();
        symulacjaIlosciPodmiotow();
        symulacjaIndeksow();
        watekSprzatajacy();
    }

    /**
     * Inicjuje wątek sprzątający
     */
    private void watekSprzatajacy() {
        new Thread(new WatekSprzatajacy(mainData)).start();
    }
    /**
     * Inicjuje symulacje Indeksów
     */
    private void symulacjaIndeksow(){
        SymulujIndeksy symulujIndeksy = new SymulujIndeksy(mainData);
        new Thread(symulujIndeksy).start();
    }
    /**
     * Inicjuje wątek dodający inwestorów i fundusze
     */
    private void symulacjaIlosciPodmiotow() {
        SymulujIlosc symulujIloscPodmiotow = new SymulujIlosc(mainData);
        new Thread(symulujIloscPodmiotow).start();
    }
    /**
     * Inicjuje wątki symulujące spółki
     */
    private void symulacjaSpolki() {
        mainData.getCompanyList().forEach(spolka -> new Thread(spolka).start());
    }
    /**
     * Inicjuje wątki symulujące inwestorów
     */
    private void symulacjaPodmiotu() {
        mainData.getInvestorList().forEach(inwestor -> new Thread(inwestor).start());
    }
    /**
     * Inicjuje wątki symulujące fundusze
     */
    private void symulacjaFunduszu() {
        mainData.getFundList().forEach(fundusz -> new Thread(fundusz).start());
    }

    /**
     * Inicjuje symulacje rynków
     */
    private void symulacjaRynku(){
        SymulujRynekWalut symulujRynekWalut = new SymulujRynekWalut(mainData.getRynekWalut());
        SymulujRynekSurowcow symulujRynekSurowcow = new SymulujRynekSurowcow(mainData.getRynekSurowcow());
        SymulujRynekFunduszy symulujRynekFunduszy = new SymulujRynekFunduszy(mainData.getRynekFunduszyInwestycyjnych());
        SymulujGieldePapierowWartosciowych symulujGielde = new SymulujGieldePapierowWartosciowych(mainData);
        new Thread(symulujRynekWalut).start();
        new Thread(symulujRynekSurowcow).start();
        new Thread(symulujRynekFunduszy).start();
        new Thread(symulujGielde).start();
    }

    /**
     * Przechwytuje akcje zamknięcia okna, wyswietla okno dialogowe i w razie co rozpoczyna zapis
     */
    private void setCloseAction() {
        this.primaryStage.setOnCloseRequest(event -> {Optional<ButtonType> result = DialogsUtils.confiramationDialog();
            if (result.get() == ButtonType.OK) {
                Platform.exit();
                SerializeProgram.save(mainData);
                System.exit(0);
            } else
                event.consume();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
