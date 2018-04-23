package pl.symulacja.gieldy.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import pl.symulacja.gieldy.main.SymulacjaGieldy;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Klasa wyświetlająca dodatkowe okna dialogowe
 * @author Paweł
 */
@SuppressWarnings("ALL")
public class DialogsUtils {
    static ResourceBundle bundle = FxmlUtils.getResourceBundle();

    /**
     * Wyświetla informacje o aplikacji
     */
    public static void dialogAboutApplication(){
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) informationAlert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(IconLoader.Iconpath(SymulacjaGieldy.MAIN_ICON));
        informationAlert.getDialogPane().getStylesheets().add(SymulacjaGieldy.MAIN_CSS);
        informationAlert.getDialogPane().getStyleClass().add("myAlert");
        informationAlert.setTitle(bundle.getString("about.title"));
        informationAlert.setHeaderText(bundle.getString("about.header"));
        informationAlert.setContentText(bundle.getString("about.content"));
        informationAlert.showAndWait();
    }

    /**
     * Wyświetla okno dialogowe z pytaniem o potwierdzenie wyjścia z programu
     * @return Zwraca wartość dokonanego wyboru
     */
    public static Optional<ButtonType> confiramationDialog(){
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) confirmationDialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(IconLoader.Iconpath(SymulacjaGieldy.MAIN_ICON));
        confirmationDialog.getDialogPane().getStylesheets().add(SymulacjaGieldy.MAIN_CSS);
        confirmationDialog.getDialogPane().getStyleClass().add("myAlert");
        confirmationDialog.setTitle(bundle.getString("exit.title"));
        confirmationDialog.setHeaderText(bundle.getString("exit.header"));
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        return result;
    }

    /**
     * Wyświetla okno informujące użytkownika o popełnionym przez niego błędzie lub problemach programu
     * @param error Wiadomość wyświetlana użytkownikowi
     */
    public static void errorDialog(String error){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(IconLoader.Iconpath(SymulacjaGieldy.MAIN_ICON));
        errorAlert.getDialogPane().getStylesheets().add(SymulacjaGieldy.MAIN_CSS);
        errorAlert.getDialogPane().getStyleClass().add("myAlert");
        errorAlert.setTitle(bundle.getString("error.title"));
        errorAlert.setHeaderText(bundle.getString("error.header"));

        TextArea textArea = new TextArea(error);
        errorAlert.getDialogPane().setContent(textArea);
        errorAlert.showAndWait();
    }

    /**
     * Wyświetla okno z pomocnymi informacjami
     */
    public static void help(){
        Dialog addDialog = new Dialog();
        Stage stage = (Stage)addDialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(IconLoader.Iconpath(SymulacjaGieldy.MAIN_ICON));
        addDialog.setTitle("Dodaj Element");
        addDialog.getDialogPane().getStylesheets().add(SymulacjaGieldy.MAIN_CSS);
        addDialog.getDialogPane().getStyleClass().add("myAlert");
        addDialog.showAndWait();
    }
}
