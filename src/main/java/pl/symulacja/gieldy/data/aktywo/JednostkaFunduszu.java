package pl.symulacja.gieldy.data.aktywo;

import pl.symulacja.gieldy.data.podmiot.FunduszInwestycyjny;
import pl.symulacja.gieldy.utils.RandomModule;
import java.io.Serializable;
import java.util.Random;

/**
 * Klasa imitująca jednostki uczestnictwa funduszu (rozszerzająca aktywo), zawiera dodatkowo odniesienie do właściciela - funduszu inwestycyjnego
 * @author Paweł
 */
public class JednostkaFunduszu extends Aktywo implements Serializable {
    private FunduszInwestycyjny owner;

    /**
     * Zwraca podmiot wypuszczający daną jednostkę uczestnictwa
     * @return Podmiot wypuszczający jednostkę
     */
    public FunduszInwestycyjny getOwner() {
        return owner;
    }

    /**
     * Ustawia podmiot wypuszczający daną jednostkę uczestnictwa
     * @param owner Podmiot wypuszczający jednostkę
     */
    public void setOwner(FunduszInwestycyjny owner) {
        this.owner = owner;
    }

    /**
     * Zmienia cenę jednostki funduszu na podstawie jej kupowania
     */
    public void zmianaCeny() {
        Random random = new Random();
        int temp = 1;
        for (JednostkaFunduszu jednostka: Aktywo.getMainDataClass().getFundUnitList()){
            if (jednostka.getNamePropertyValue().equals(this.getNamePropertyValue())){
                if (this.getQuantityPropertyValue() > jednostka.getQuantityPropertyValue()/2)
                    temp = -1;
            }
        }
        double price = random.nextDouble();
        if (price < 0.1)
            price += 0.1;
        price *= temp;
        double cena = this.getPricePropertyValue();
        if (cena + price < 0 || cena + price > 100)
            cena -= price;
        else
            cena += price;
        cena = RandomModule.round(cena)/100;
        if (cena > getMaxPricePropertyValue())
            setMaxPricePropertyValue(cena);
        else if (cena < getMinPricePropertyValue())
            setMinPricePropertyValue(cena);
        this.setPricePropertyValue(cena);
        this.getChartData().add(cena);
    }
}
