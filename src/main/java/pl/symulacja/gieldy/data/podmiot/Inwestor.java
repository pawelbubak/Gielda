package pl.symulacja.gieldy.data.podmiot;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pl.symulacja.gieldy.data.aktywo.*;
import pl.symulacja.gieldy.data.gielda.*;
import pl.symulacja.gieldy.utils.DialogsUtils;
import pl.symulacja.gieldy.utils.RandomModule;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

/**
 * Klasa imitująca Inwestora i rozszerzająca Podmiot o PESEL
 * @author Paweł
 */
public class Inwestor extends Podmiot implements Serializable{
    transient private StringProperty PESELProperty = new SimpleStringProperty(this, "PESELProperty");

    /**
     * Zwraca PESEL Inwestora
     * @return PESEL
     */
    public String getPESELPropertyValue() {
        return PESELProperty.get();
    }

    /**
     * Zwraca PESEL Inwestora
     * @return PESEL (StringProperty)
     */
    public StringProperty getPESELProperty() {
        return PESELProperty;
    }

    /**
     * Ustawia PESEL inwestorowi
     * @param PESEL PESEL
     */
    public void setPESELPropertyValue(String PESEL) {
        this.PESELProperty.set(PESEL);
    }

    /**
     * Inwestor wybiera sobie aktywo, które chce sprzedać i zleca jego sprzedaż
     */
    @Override
    synchronized public void zlecSprzedaz() {
        int choose;
        Random random = new Random();
        try {
            choose = random.nextInt(this.getStockList().size());
            Aktywo aktywo = this.getStockList().get(choose);
            double quantity = random.nextDouble();
            quantity = RandomModule.round(quantity);
            if (quantity < 10)
                quantity += 10;
            Gielda gielda = aktywo.getStockExchange();
            gielda.zlecenieSprzedazy(this,aktywo,(int)quantity);
        }catch (Exception ignored){}
        try {
            Thread.sleep((random.nextInt(10) + 1) * 1000);
        } catch (InterruptedException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
    }

    /**
     * Inwestor wybiera sobie aktywo, które chce kupić i zleca jego kupno
     */
    @Override
    synchronized public void zlecKupno() {
        int choose;
        Random random = new Random();
        //  ILOSC AKTYW
        double quantity = random.nextDouble();
        quantity = RandomModule.round(quantity);
        if (quantity < 5)
            quantity += 10;
        //  WYBÓR AKTYWA
        double temp = random.nextDouble();
        if (temp < 0.25){
            try {
                choose = random.nextInt(Aktywo.getMainDataClass().getStockExchangeList().size());
                GieldaPapierowWartosciowych gielda = Aktywo.getMainDataClass().getStockExchangeList().get(choose);
                choose = random.nextInt(gielda.getList().size());
                gielda.zlecenieKupna(this, gielda.getStockList().get(choose), (int) quantity);
            } catch (Exception ignored){}
        }
        else if (temp < 0.5){
            try {
                RynekSurowcow rynek = Aktywo.getMainDataClass().getRynekSurowcow();
                choose = random.nextInt(rynek.getList().size());
                rynek.zlecenieKupna(this,(Aktywo)rynek.getList().get(choose),(int)quantity);
            } catch (Exception ignored){}
        }
        else if (temp < 0.75){
            try {
                RynekWalut rynek = Aktywo.getMainDataClass().getRynekWalut();
                choose = random.nextInt(rynek.getList().size());
                rynek.zlecenieKupna(this,(Aktywo)rynek.getList().get(choose),(int)quantity);
            } catch (Exception ignored){}
        }
        else{
            try {
                RynekFunduszyInwestycyjnych rynek = Aktywo.getMainDataClass().getRynekFunduszyInwestycyjnych();
                choose = random.nextInt(rynek.getList().size());
                rynek.zlecenieKupna(this,(Aktywo)rynek.getList().get(choose),(int)quantity);
            } catch (Exception ignored){}
        }
        try {
            Thread.sleep((random.nextInt(10) + 1) * 100);
        } catch (InterruptedException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
    }


    // POZOSTALE
    @Override
    public String toString() {
        return super.toString() + " " + getPESELPropertyValue();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(PESELProperty.get());
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        PESELProperty = new SimpleStringProperty((String) ois.readObject());
    }
}
