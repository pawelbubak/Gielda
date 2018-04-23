package pl.symulacja.gieldy.main;

import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.gielda.GieldaPapierowWartosciowych;
import pl.symulacja.gieldy.utils.DialogsUtils;

import java.util.ArrayList;

/**
 * Pomocnicza klasa obliczająca/symulująca wartości indeksów poszczególnych giełd
 * @author Paweł
 */
public class SymulujIndeksy implements Runnable{
    private MainDataClass mainData;

    SymulujIndeksy(MainDataClass mainDataClass) {
        this.mainData = mainDataClass;
    }

    /**
     * Oblicza wartości
     */
    @Override
    public void run() {
        while (true){
            while (SymulacjaGieldy.symuluj) {
                for (GieldaPapierowWartosciowych gielda: mainData.getStockExchangeList()){
                    gielda.obliczIndeksy();
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    DialogsUtils.errorDialog(e.getMessage());
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                DialogsUtils.errorDialog(e.getMessage());
            }
        }
    }
}
