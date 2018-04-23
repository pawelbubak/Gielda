package pl.symulacja.gieldy.main;

import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.gielda.GieldaPapierowWartosciowych;
import pl.symulacja.gieldy.utils.DialogsUtils;

import java.util.ArrayList;

/**
 * Dodatkowa klasa sprzątająca zbędne aktywa
 * @author Paweł
 */
public class WatekSprzatajacy implements Runnable{
    private MainDataClass mainDataClass;

    public WatekSprzatajacy(MainDataClass mainDataClass){
        this.mainDataClass = mainDataClass;
    }

    /**
     * Zainicjowanie przeszukiwania
     */
    @Override
    public void run() {
        while (true){
            while (SymulacjaGieldy.symuluj) {
                for (GieldaPapierowWartosciowych gielda: mainDataClass.getStockExchangeList()){
                    odsmiecanie(gielda);
                }
                try {
                    Thread.sleep(20000);
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

    /**
     * Przeszukiwanie wskazanej giełdy w celu usunięcia zbędnych aktyw
     * @param gieldaPapierowWartosciowych Giełda wybrana do przeszukiwania
     */
    private void odsmiecanie(GieldaPapierowWartosciowych gieldaPapierowWartosciowych) {
        ArrayList<Aktywo> doUsuniecia = new ArrayList<>();
        for (Aktywo aktywo: gieldaPapierowWartosciowych.getStockList()){
            if (aktywo.isUsuwany())
                doUsuniecia.add(aktywo);
        }
        if (!doUsuniecia.isEmpty())
            doUsuniecia.forEach(aktywo -> gieldaPapierowWartosciowych.getStockList().remove(aktywo));
    }
}
