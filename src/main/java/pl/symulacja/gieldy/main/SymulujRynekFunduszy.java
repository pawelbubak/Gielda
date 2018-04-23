package pl.symulacja.gieldy.main;

import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.gielda.RynekFunduszyInwestycyjnych;
import pl.symulacja.gieldy.utils.DialogsUtils;
import java.util.ArrayList;

/**
 * Pomocnicza klasa wyznaczająca kurs funduszy
 * @author Paweł
 */
public class SymulujRynekFunduszy implements Runnable {
    private RynekFunduszyInwestycyjnych rynekFunduszy;

    SymulujRynekFunduszy(RynekFunduszyInwestycyjnych rynekFunduszyInwestycyjnych) {
        this.rynekFunduszy = rynekFunduszyInwestycyjnych;
    }

    /**
     * Symuluje kurs funduszy
     */
    @Override
    public void run() {
        int ilosc;
        while (true) {
            while (SymulacjaGieldy.symuluj) {
                ilosc = rynekFunduszy.getFundList().size();
                for (int i = 0; i < ilosc; i++) {
                    try {
                        rynekFunduszy.getFundList().get(i).zmianaCeny();
                    } catch (Exception ignored) {
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        DialogsUtils.errorDialog(e.getMessage());
                    }
                }
                odsmiecanie();
                try {
                    Thread.sleep(2000);
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
     * Odśmieca symulacje z niepotrzebnych danych - jednostki usuniętych funduszy
     */
    private void odsmiecanie() {
        ArrayList<Aktywo> doUsuniecia = new ArrayList<>();
        for (Aktywo aktywo: rynekFunduszy.getFundList()){
            try {
                if (aktywo.isUsuwany())
                    doUsuniecia.add(aktywo);
            }catch (Exception ignored){}
        }
        if (!doUsuniecia.isEmpty())
            doUsuniecia.forEach(aktywo -> rynekFunduszy.getFundList().remove(aktywo));
    }
}

