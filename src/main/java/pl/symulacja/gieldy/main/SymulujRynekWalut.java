package pl.symulacja.gieldy.main;

import pl.symulacja.gieldy.data.gielda.RynekWalut;
import pl.symulacja.gieldy.utils.DialogsUtils;

/**
 * Pomocnicza klasa regulująca kurs walut w programie
 * @author Paweł
 */
public class SymulujRynekWalut implements Runnable{
    private RynekWalut rynekWalut;

    SymulujRynekWalut(RynekWalut rynekWalut){
        this.rynekWalut = rynekWalut;
    }

    /**
     * Symulacja kursu walut
     */
    @Override
    public void run() {
        int ilosc;
        while (true) {
            while (SymulacjaGieldy.symuluj) {
                ilosc = rynekWalut.getCurrencyList().size();
                for (int i = 0; i < ilosc; i++) {
                    try {
                        if (rynekWalut.getCurrencyList().get(i).getNamePropertyValue().equals("PLN")) {
                            rynekWalut.getCurrencyList().get(i).getChartData().add(1.0);
                            continue;
                        }
                        rynekWalut.getCurrencyList().get(i).zmianaCeny();
                    }catch (Exception ignored){}
                    try {
                        Thread.sleep(1500);
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
