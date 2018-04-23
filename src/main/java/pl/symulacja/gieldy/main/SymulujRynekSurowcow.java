package pl.symulacja.gieldy.main;

import pl.symulacja.gieldy.data.gielda.RynekSurowcow;
import pl.symulacja.gieldy.utils.DialogsUtils;

/**
 * Główna klasa z danymi - instancja tej klasy przechowuje wszytskie dane używane w programie
 * @author Paweł
 */
public class SymulujRynekSurowcow implements Runnable{
    private RynekSurowcow rynekSurowcow;

    SymulujRynekSurowcow(RynekSurowcow rynekSurowcow){
        this.rynekSurowcow = rynekSurowcow;
    }

    /**
     * Symulacja kursu surowców
     */
    @Override
    public void run() {
        int ilosc;
        while (true){
            while (SymulacjaGieldy.symuluj) {
                ilosc = rynekSurowcow.getResourcesList().size();
                for (int i = 0; i < ilosc; i++) {
                    try{
                        rynekSurowcow.getResourcesList().get(i).zmianaCeny();
                    }
                    catch (Exception ignored){}
                    try {
                        Thread.sleep(3000);
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
