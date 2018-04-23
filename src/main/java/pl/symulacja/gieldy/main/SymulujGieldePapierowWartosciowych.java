package pl.symulacja.gieldy.main;

import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.utils.DialogsUtils;

/**
 * Symuluje zmiane ceny kursu akcji
 * @author Paweł
 */
public class SymulujGieldePapierowWartosciowych implements Runnable{
    private MainDataClass mainData;

    SymulujGieldePapierowWartosciowych(MainDataClass mainData) {
        this.mainData = mainData;
    }

    /**
     * Symuluje ceny
     */
    @Override
    public void run() {
        int ilosc;
        while (true){
            while (SymulacjaGieldy.symuluj) {
                ilosc = mainData.getCompanyList().size();
                for (int i = 0; i < ilosc; i++) {
                    try {
                        mainData.getCompanyList().get(i).zmianaCeny();
                    } catch (Exception ignored){}
                    try {
                        Thread.sleep(50);
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                DialogsUtils.errorDialog(e.getMessage());
            }
        }
    }
}
