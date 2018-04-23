package pl.symulacja.gieldy.interfaces;

import pl.symulacja.gieldy.controllers.MainController;
import pl.symulacja.gieldy.data.MainDataClass;

/**
 * Interfejs pozwalający na przekazywanie instancji z wszystkimi danymi oraz kontrolera głównego okna
 * @author Paweł
 */
public interface ControllerTwo {
    void setMainData(MainDataClass mainData, MainController mainController);
}
