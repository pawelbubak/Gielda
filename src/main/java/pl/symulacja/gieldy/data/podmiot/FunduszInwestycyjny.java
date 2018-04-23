package pl.symulacja.gieldy.data.podmiot;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.interfaces.Owner;
import pl.symulacja.gieldy.data.aktywo.Aktywo;
import pl.symulacja.gieldy.data.aktywo.JednostkaFunduszu;
import pl.symulacja.gieldy.data.gielda.Gielda;
import pl.symulacja.gieldy.data.gielda.RynekSurowcow;
import pl.symulacja.gieldy.data.gielda.RynekWalut;
import pl.symulacja.gieldy.utils.DialogsUtils;
import pl.symulacja.gieldy.utils.RandomModule;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

/**
 * Klasa imitująca Fundusz Inwestycyjny i rozszerzająca Podmiot o nazwę funduszu
 * @author Paweł
 */
public class FunduszInwestycyjny extends Podmiot implements Serializable, Owner{
    transient private StringProperty fundNameProperty = new SimpleStringProperty(this,"fundNameProperty");

    /**
     * Zwraca nazwę funduszu
     * @return Nazwa funduszu
     */
    public String getFundNamePropertyValue() {
        return fundNameProperty.get();
    }

    /**
     * Zwraca nazwę funduszu
     * @return Nazwa funduszu (StringProperty)
     */
    public StringProperty getFundNameProperty() {
        return fundNameProperty;
    }

    /**
     * Ustawia nazwę funduszu
     * @param fundNameProperty Nazwa funduszu
     */
    public void setFundNamePropertyValue(String fundNameProperty) {
        this.fundNameProperty.set(fundNameProperty);
    }

    /**
     * Pozwala funduszowi wypuścić swoje udziały - zostają umieszczone na Rynku Funduszy
     * @param mainDataClass Instancja zawierająca wszystkie dane w programie
     */
    public void wypuscUdzialy(MainDataClass mainDataClass){
        JednostkaFunduszu jednostka = new JednostkaFunduszu();
        jednostka.setNamePropertyValue("Jednostka " + getFundNamePropertyValue());
        setNewPrice(Double.parseDouble(RandomModule.randName("priceA")),jednostka);
        jednostka.setStockExchange(mainDataClass.getRynekFunduszyInwestycyjnych());
        jednostka.setOwner(this);
        mainDataClass.getRynekFunduszyInwestycyjnych().getFundList().add(jednostka);
        mainDataClass.getGlobalStockList().add(jednostka);
        jednostka.getChartData().add(jednostka.getPricePropertyValue());
        this.setBudgetPropertyValue(this.getBudgetPropertyValue() + jednostka.getPricePropertyValue() * jednostka.getQuantityPropertyValue());
        // dodaje do listy pomocniczej
        JednostkaFunduszu jednostka2 = new JednostkaFunduszu();
        jednostka2.setNameProperty(jednostka.getNameProperty());
        jednostka2.setPriceProperty(jednostka.getPriceProperty());
        jednostka2.setMaxPriceProperty(jednostka.getMaxPriceProperty());
        jednostka2.setMinPriceProperty(jednostka.getMinPriceProperty());
        jednostka2.setChartData(jednostka.getChartData());
        jednostka2.setQuantityProperty(new SimpleIntegerProperty(jednostka2,"jednostka2", 100));
        jednostka2.setStockExchange(mainDataClass.getRynekFunduszyInwestycyjnych());
        jednostka2.setOwner(this);
        jednostka2.setUsuwanyProperty(jednostka.getUsuwanyProperty());
        mainDataClass.getFundUnitList().add(jednostka2);
    }

    /**
     * Ustala cenę jednostek wypuszczanych przez fundusz
     * @param price cena
     * @param jednostka Jednostka uczestnictwa
     */
    private void setNewPrice(double price, JednostkaFunduszu jednostka){
        jednostka.setPricePropertyValue(price);
        jednostka.setMaxPricePropertyValue(price);
        jednostka.setMinPricePropertyValue(price);
        jednostka.setQuantityPropertyValue(100);
    }

    /**
     * Fundusz wybiera sobie aktywo, które chce sprzedać i zleca jego sprzedaż
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
            if (quantity < 5)
                quantity += 10;
            Gielda stockExchange = aktywo.getStockExchange();
            stockExchange.zlecenieSprzedazy(this,aktywo,(int)quantity);
        }catch (Exception ignored){}
        try {
            Thread.sleep((random.nextInt(10) + 1) * 100);
        } catch (InterruptedException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
    }

    /**
     * Fundusz wybiera sobie aktywo, które chce kupić i zleca jego kupno
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
                Gielda gielda = Aktywo.getMainDataClass().getStockExchangeList().get(choose);
                choose = random.nextInt(gielda.getList().size());
                gielda.zlecenieKupna(this, gielda.getList().get(choose), (int) quantity);
            }
            catch (Exception ignored){}
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
        try {
            Thread.sleep((random.nextInt(10) + 1) * 1000);
        } catch (InterruptedException e) {
            DialogsUtils.errorDialog(e.getMessage());
        }
    }

    /**
     * Pozwala zwiększyć budżet funduszowi przez wypuszczenie nowych jednostek uczestnictwa
     */
    @Override
    void zwiekszBudget(){
        for (JednostkaFunduszu jednostka: Aktywo.getMainDataClass().getFundUnitList()){
            if (jednostka.getNamePropertyValue().equals("Jednostka " + getFundNamePropertyValue())){
                if (jednostka.getQuantityPropertyValue() < 10000)
                    Aktywo.getMainDataClass().getRynekFunduszyInwestycyjnych().dodajNoweAkcje(this,jednostka);
            }
        }
    }

    @Override
    public StringProperty getName() {
        return getFundNameProperty();
    }


    // POZOSTALE
    @Override
    public String toString() {
        return getFundNamePropertyValue() + " " + super.toString();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(fundNameProperty.get());
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        fundNameProperty = new SimpleStringProperty((String) ois.readObject());
    }
}
