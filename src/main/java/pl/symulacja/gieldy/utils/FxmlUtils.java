package pl.symulacja.gieldy.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import pl.symulacja.gieldy.controllers.MainController;
import pl.symulacja.gieldy.interfaces.Controller;
import pl.symulacja.gieldy.interfaces.ControllerThree;
import pl.symulacja.gieldy.interfaces.ControllerTwo;
import pl.symulacja.gieldy.data.MainDataClass;
import java.io.IOException;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Klasa z metodami ułatwiającymi przekazywanie danych do kontrolerów oraz odpowiadająca za ładowanie plików fxml
 * @author Paweł
 */
public class FxmlUtils {
    /**
     * Używana do tworzenia głównego okna
     * @param fxmlPath Scieżka do pliku fxml
     * @return Zwraca Pane do załadowania okna oraz FXMLLoader do pobrania kontrolera obsługującego plik fxml
     */
    public static Pair<Pane, FXMLLoader> fxmlLoader(String fxmlPath){
        FXMLLoader loader = new FXMLLoader(FxmlUtils.class.getResource(fxmlPath));
        loader.setResources(getResourceBundle());
        try {
            Pair<Pane,FXMLLoader> pair = new Pair<>(loader.load(), loader);
            return pair;
        }
        catch (Exception e){
            DialogsUtils.errorDialog(e.getMessage());
        }
        return null;
    }

    /**
     * Laduje plik bundle
     * @return Zwraca bundle
     */
    public static ResourceBundle getResourceBundle(){
        return ResourceBundle.getBundle("bundles.messages");
    }

    /**
     * Otwiera nowy panel z danymi wyświetlanymi użytkownikowi i przekazuje mu obiekt z wszystkimi danymi
     * @param fxmlPath Scieżka do pliku fxml
     * @param mainData Obiekt z wszystkimi danymi programu
     * @param content BorderPane w którym ma być wyświetlony widok fxml
     */
    public static void openInformationPanel(String fxmlPath, MainDataClass mainData, BorderPane content){
        FXMLLoader loader = new FXMLLoader(FxmlUtils.class.getResource(fxmlPath));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
        content.setCenter(root);
        Controller controller = loader.getController();
        controller.setMainData(mainData);
    }

    /**
     * Otwiera nowy panel z danymi wyświetlanymi użytkownikowi i przekazuje mu obiekt z wszystkimi danymi oraz kontroler głównego okna
     * @param fxmlPath Scieżka do pliku fxml
     * @param mainData Obiekt z wszystkimi danymi programu
     * @param mainController Instancja kontrolera głównego okna
     */
    public static void openInformationPanelTwo(String fxmlPath, MainDataClass mainData, MainController mainController){
        FXMLLoader loader = new FXMLLoader(FxmlUtils.class.getResource(fxmlPath));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
        mainController.getListPane().setCenter(root);
        ControllerTwo controller = loader.getController();
        controller.setMainData(mainData, mainController);
    }

    public static FXMLLoader getLoader(String fxmlPath){
        FXMLLoader loader = new FXMLLoader(FxmlUtils.class.getResource(fxmlPath));
        loader.setResources(getResourceBundle());
        return loader;
    }

    /**
     * Otwiera nowy panel z danymi wyświetlanymi użytkownikowi i przekazuje mu wskazany obiekt do wyświetlenia oraz tryb otwarcia panelu informacyjnego
     * @param fxmlPath Scieżka do pliku fxml
     * @param object Obiekt przekazywany
     * @param mainController Instancja kontrolera głównego okna
     * @param choice Tryb otwarcia panelu informacyjnego
     */
    public static void openMoreInformationPanel(String fxmlPath, Object object, MainController mainController, boolean choice) {
        FXMLLoader loader = new FXMLLoader(FxmlUtils.class.getResource(fxmlPath));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            DialogsUtils.errorDialog(e.getMessage()+"\n"+ Arrays.toString(e.getStackTrace()));
        }
        mainController.getInformation_container().setCenter(root);
        ControllerThree controller = loader.getController();
        controller.setData(object, choice);
    }
}
