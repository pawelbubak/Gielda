package pl.symulacja.gieldy.data.spolka;

import javafx.beans.property.*;
import pl.symulacja.gieldy.data.MainDataClass;
import pl.symulacja.gieldy.interfaces.Owner;
import pl.symulacja.gieldy.data.aktywo.Akcja;
import pl.symulacja.gieldy.data.gielda.GieldaPapierowWartosciowych;
import pl.symulacja.gieldy.main.SymulacjaGieldy;
import pl.symulacja.gieldy.utils.DialogsUtils;
import pl.symulacja.gieldy.utils.RandomModule;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

/**
 * Klasa imitująca Spółkę, która wypuszcza akcje i zawiera:
 * Nazwe, datę wyceny, kurs otwarcia, kurs, maksymalny kurs, minimalny kurs, zysk, przychód, kapitał własny i zakładowy,
 * wolumen, obroty
 * @author Paweł
 */
public class Spolka implements Serializable, Owner, Runnable{
    transient private StringProperty nameProperty = new SimpleStringProperty(this, "nameProperty");
    private LocalDate valuationDate;
    transient private DoubleProperty openingPriceProperty = new SimpleDoubleProperty(this,"openingPriceProperty");
    transient private DoubleProperty priceProperty = new SimpleDoubleProperty(this,"priceProperty");
    transient private DoubleProperty maxPriceProperty = new SimpleDoubleProperty(this,"maxPriceProperty");
    transient private DoubleProperty minPriceProperty = new SimpleDoubleProperty(this,"minPriceProperty");
    transient private IntegerProperty quantityProperty = new SimpleIntegerProperty(this,"quantityProperty");
    transient private DoubleProperty profitProperty = new SimpleDoubleProperty(this,"profitProperty");
    transient private DoubleProperty incomeProperty = new SimpleDoubleProperty(this,"incomeProperty");
    transient private DoubleProperty equityCapitalProperty = new SimpleDoubleProperty(this,"equityCapitalProperty");
    transient private DoubleProperty shareCapitalProperty = new SimpleDoubleProperty(this,"shareCapitalProperty");
    transient private DoubleProperty volumeProperty = new SimpleDoubleProperty(this,"volumeProperty");
    transient private DoubleProperty salesProperty = new SimpleDoubleProperty(this,"salesProperty");
    private GieldaPapierowWartosciowych stockExchange;
    private transient boolean kill = false;
    private ArrayList<Double> chartDataY = new ArrayList<>();
    private transient BooleanProperty usuwany = new SimpleBooleanProperty(this,"usuwany",false);

    // KONSTRUKTOR
    public Spolka(){}

    /**
     * Zwraca nazwę spółki
     * @return Nazwa spółki
     */
    public String getNamePropertyValue() {
        return nameProperty.get();
    }

    /**
     * Zwraca nazwę spółki
     * @return Nazwa spółki (StringProperty)
     */
    public StringProperty getNameProperty() {
        return nameProperty;
    }

    /**
     * Ustawia nazwę spółki
     * @param nameProperty Nazwa spółki
     */
    public void setNamePropertyValue(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    /**
     * Ustawia nazwę spółki
     * @param nameProperty Nazwa spółki (StringProperty)
     */
    public void setNameProperty(StringProperty nameProperty) {
        this.nameProperty = nameProperty;
    }

    /**
     * Zwraca datę pierwszej wyceny
     * @return Data wyceny
     */
    public LocalDate getValuationDate() {
        return valuationDate;
    }

    /**
     * Ustawia datę pierwszej wyceny
     * @param valuationDate Data wyceny
     */
    public void setValuationDate(LocalDate valuationDate) {
        this.valuationDate = valuationDate;
    }

    /**
     * Zwraca kurs otwarcia
     * @return Kurs otwracia (DoubleProperty)
     */
    public DoubleProperty getOpeningPriceProperty() {
        return openingPriceProperty;
    }

    /**
     * Ustawia kurs otwarcia
     * @param openingPriceProperty Kurs otwracia
     */
    private void setOpeningPricePropertyValue(double openingPriceProperty) {
        this.openingPriceProperty.set(openingPriceProperty);
    }

    /**
     * Zwraca kurs
     * @return Kurs
     */
    synchronized public double getPricePropertyValue() {
        return priceProperty.get();
    }

    /**
     * Zwraca kurs
     * @return Kurs (DoubleProperty)
     */
    public DoubleProperty getPriceProperty() {
        return priceProperty;
    }

    /**
     * Ustawia kurs
     * @param priceProperty Kurs
     */
    synchronized private void setPricePropertyValue(double priceProperty) {
        this.priceProperty.set(priceProperty);
    }

    /**
     * Zwraca kurs maksymalny
     * @return Maksymalny kurs
     */
    private double getMaxPricePropertyValue() {
        return maxPriceProperty.get();
    }

    /**
     * Zwraca kurs maksymalny
     * @return Maksymalny kurs (DoubleProperty)
     */
    public DoubleProperty getMaxPriceProperty() {
        return maxPriceProperty;
    }

    /**
     * Ustawia kurs maksymalny
     * @param maxPriceProperty Maksymalny kurs
     */
    private void setMaxPricePropertyValue(double maxPriceProperty) {
        this.maxPriceProperty.set(maxPriceProperty);
    }

    /**
     * Zwraca kurs minimalny
     * @return Minimalny kurs
     */
    private double getMinPricePropertyValue() {
        return minPriceProperty.get();
    }

    /**
     * Zwraca kurs minimalny
     * @return Minimalny kurs (DoubleProperty)
     */
    public DoubleProperty getMinPriceProperty() {
        return minPriceProperty;
    }

    /**
     * Ustawia kurs minimalny
     * @param minPriceProperty Minimalny kurs
     */
    private void setMinPricePropertyValue(double minPriceProperty) {
        this.minPriceProperty.set(minPriceProperty);
    }

    /**
     * Zwraca ilość akcji
     * @return Ilość akcji
     */
    public int getQuantityPropertyValue() {
        return quantityProperty.get();
    }

    /**
     * Zwraca ilość akcji
     * @return Ilość akcji (DoubleProperty)
     */
    public IntegerProperty getQuantityProperty() {
        return quantityProperty;
    }

    /**
     * Ustawia ilość akcji
     * @param quantityProperty Ilość akcji
     */
    public void setQuantityPropertyValue(int quantityProperty) {
        this.quantityProperty.set(quantityProperty);
    }

    /**
     * Zwraca zysk
     * @return Zysk (DoubleProperty)
     */
    public DoubleProperty getProfitProperty() {
        return profitProperty;
    }

    /**
     * Ustawia zysk
     * @param profitProperty Zysk
     */
    synchronized private void setProfitPropertyValue(double profitProperty) {
        this.profitProperty.set(profitProperty);
    }

    /**
     * Zwraca przychód
     * @return Przychód
     */
    synchronized public double getIncomePropertyValue() {
        return incomeProperty.get();
    }

    /**
     * Zwraca przychód
     * @return Przychód (DoubleProperty)
     */
    public DoubleProperty getIncomeProperty() {
        return incomeProperty;
    }

    /**
     * Ustawia przychód
     * @param incomeProperty Przychód
     */
    synchronized public void setIncomePropertyValue(double incomeProperty) {
        this.incomeProperty.set(incomeProperty);
    }

    /**
     * Zwraca kapitał własny
     * @return Kapitał własny (DoubleProperty)
     */
    public DoubleProperty getEquityCapitalProperty() {
        return equityCapitalProperty;
    }

    /**
     * Ustawia kapitał własny
     * @param equityCapitalProperty Kapitał własny
     */
    public void setEquityCapitalPropertyValue(double equityCapitalProperty) {
        this.equityCapitalProperty.set(equityCapitalProperty);
    }

    /**
     * Zwraca kapitał zakładowy
     * @return Kapitał zakładowy
     */
    public DoubleProperty getShareCapitalProperty() {
        return shareCapitalProperty;
    }

    /**
     * Ustawia kapitał zakładowy
     * @param shareCapitalProperty Kapitał zakładowy
     */
    public void setShareCapitalPropertyValue(double shareCapitalProperty) {
        this.shareCapitalProperty.set(shareCapitalProperty);
    }

    /**
     * Zwraca wolumen
     * @return Walumen
     */
    synchronized public double getVolumePropertyValue() {
        return volumeProperty.get();
    }

    /**
     * Zwraca wolumen
     * @return Walumen (DoubleProperty)
     */
    public DoubleProperty getVolumeProperty() {
        return volumeProperty;
    }

    /**
     *  Ustawia wolumen
     * @param volumeProperty Walumen
     */
    synchronized public void setVolumePropertyValue(double volumeProperty) {
        this.volumeProperty.set(volumeProperty);
    }

    /**
     * Zwraca obroty
     * @return Obroty
     */
    public double getSalesPropertyValue() {
        return salesProperty.get();
    }

    /**
     * Zwraca obroty
     * @return Obroty (DoubleProperty)
     */
    public DoubleProperty getSalesProperty() {
        return salesProperty;
    }

    /**
     * Ustawia obroty
     * @param salesProperty Obroty
     */
    public void setSalesPropertyValue(double salesProperty) {
        this.salesProperty.set(salesProperty);
    }

    /**
     * Zwraca listę danych do wykresu
     * @return Dane do wykresu
     */
    public ArrayList<Double> getChartData() {
        return chartDataY;
    }

    /**
     * Ustawia listę danych do wykresu
     * @param chartDataY Dane do wykresu
     */
    private void setChartData(ArrayList<Double> chartDataY) {
        this.chartDataY = chartDataY;
    }

    /**
     * Zwraca giełdę papierów wartościowych
     * @return Giełda Papierów Wartościowych
     */
    public GieldaPapierowWartosciowych getStockExchange() {
        return stockExchange;
    }

    /**
     * Ustawia giełdę papierów wartościowych
     * @param stockExchange Giełda Papierów Wartościowych
     */
    private void setStockExchange(GieldaPapierowWartosciowych stockExchange) {
        this.stockExchange = stockExchange;
    }

    /**
     * Zwraca czy zabić wątek
     * @return Zabicie wątek
     */
    synchronized private boolean isKill() {
        return kill;
    }

    /**
     * Ustawia czy zabić wątek
     * @param kill Zabicie wątek
     */
    synchronized public void setKill(boolean kill) {
        this.kill = kill;
    }

    /**
     * Zwraca czy usunąc akcje
     * @return Usuwanie akcji
     */
    private BooleanProperty getUsuwanyProperty() {
        return usuwany;
    }

    /**
     * Ustawia czy usunąć akcje
     * @param usuwany Usuwanie akcji
     */
    synchronized public void setUsuwanyValue(boolean usuwany) {
        this.usuwany.set(usuwany);
    }


    /**
     * Ustawia cenę akcji
     * @param stock Cena
     */
    public void setStock(double stock){
        stock *= 100;
        stock = Math.round(stock);
        stock /= 100;
        setOpeningPricePropertyValue(stock);
        setPricePropertyValue(stock);
        setMaxPricePropertyValue(stock);
        setMinPricePropertyValue(stock);
    }

    /**
     * Pozwala na wypuszczenie akcji przez spółkę pierwszy raz
     * @param value Giełda Papierów Wartośćiowych
     * @param mainDataClass Instancja z wszystkimi danymi programu
     */
    public void wypuscAkcje(GieldaPapierowWartosciowych value, MainDataClass mainDataClass) {
        Akcja akcja = new Akcja("Akcja "+getNamePropertyValue(),getPriceProperty(),getMaxPriceProperty(),getMinPriceProperty(),getQuantityProperty(),this);
        akcja.setStockExchange(value);
        value.getStockList().add(akcja);
        mainDataClass.getGlobalStockList().add(akcja);
        setIncomePropertyValue(getIncomePropertyValue() + getPricePropertyValue() * getQuantityPropertyValue());
        akcja.getChartData().add(akcja.getPricePropertyValue());
        akcja.setUsuwanyProperty(this.getUsuwanyProperty());
        this.setChartData(akcja.getChartData());
        this.setStockExchange(value);
    }

    /**
     * Pozwala na ponowne wypuszczenie akcji przez spółkę
     * @param value Giełda Papierów Wartośćiowych
     * @param quantity Ilość akcji
     */
    private void wypuscAkcje(GieldaPapierowWartosciowych value, int quantity) {
        Akcja akcja = new Akcja("Akcja "+getNamePropertyValue(),getPriceProperty(),getMaxPriceProperty(),getMinPriceProperty(),quantity,this);
        akcja.setStockExchange(value);
        setIncomePropertyValue(getIncomePropertyValue() + getPricePropertyValue() * getQuantityPropertyValue());
        akcja.getChartData().add(akcja.getPricePropertyValue());
        akcja.setUsuwanyProperty(this.getUsuwanyProperty());
        this.setChartData(akcja.getChartData());
        this.setStockExchange(value);
        this.getStockExchange().dodajNoweAkcje(akcja);
        this.setQuantityPropertyValue(this.getQuantityPropertyValue() + quantity);
    }

    /**
     * Zwraca nazwę spółki
     * @return Nazwa spółki
     */
    @Override
    public StringProperty getName() {
        return getNameProperty();
    }

    /**
     * Modyfikuje kurs akcji spółki
     */
    synchronized public void zmianaCeny() {
        Random random = new Random();
        int temp = 1;
        double volume = getVolumePropertyValue();
        if (volume == 0)
            temp = 0;
        else if (volume < 0)
            temp = -1;
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
        this.setVolumePropertyValue(0);
    }

    /**
     * Symulacja zachowania Spółki, pozwala na wypuszczanie akcji i generowanie przychodu
     */
    @Override
    public void run() {
        Random random = new Random();
        double choose, quantity;
        quantity = RandomModule.round(random.nextDouble());
        while (!isKill()) {
            while (SymulacjaGieldy.symuluj) {
                choose = random.nextDouble();
                if (choose < 0.15) {
                    if (this.getQuantityPropertyValue() < 10000) {
                        wypuscAkcje(getStockExchange(), (int)quantity);
                    }
                }
                else{
                    generujZysk();
                }
                try {
                    Thread.sleep(8000);
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
     * Oblicza przychód i zysk spółki
     */
    synchronized private void generujZysk() {
        double budget = getSalesPropertyValue();
        if (budget > 0){
            budget += getIncomePropertyValue();
            setIncomePropertyValue(RandomModule.round(budget)/100);
        }
        setProfitPropertyValue(0.7 * budget);
    }

    /**
     * Pozwala na wykupienie swoich akcji
     * @param price Cena wykupu
     */
    synchronized public void wykup(double price) {
        this.getStockExchange().wykupAkcje(this, price);
    }


    // POZOSTALE
    @Override
    public String toString() {
        return getNamePropertyValue();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(nameProperty.get());
        oos.writeObject(openingPriceProperty.get());
        oos.writeObject(priceProperty.get());
        oos.writeObject(maxPriceProperty.get());
        oos.writeObject(minPriceProperty.get());
        oos.writeObject(quantityProperty.get());
        oos.writeObject(profitProperty.get());
        oos.writeObject(incomeProperty.get());
        oos.writeObject(equityCapitalProperty.get());
        oos.writeObject(shareCapitalProperty.get());
        oos.writeObject(volumeProperty.get());
        oos.writeObject(salesProperty.get());
        oos.writeObject(stockExchange);
        oos.writeObject(usuwany.get());
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        nameProperty = new SimpleStringProperty((String) ois.readObject());
        openingPriceProperty = new SimpleDoubleProperty((double) ois.readObject());
        priceProperty = new SimpleDoubleProperty((double) ois.readObject());
        maxPriceProperty = new SimpleDoubleProperty((double) ois.readObject());
        minPriceProperty = new SimpleDoubleProperty((double) ois.readObject());
        quantityProperty = new SimpleIntegerProperty((int) ois.readObject());
        profitProperty = new SimpleDoubleProperty((double) ois.readObject());
        incomeProperty = new SimpleDoubleProperty((double) ois.readObject());
        equityCapitalProperty = new SimpleDoubleProperty((double) ois.readObject());
        shareCapitalProperty = new SimpleDoubleProperty((double) ois.readObject());
        volumeProperty = new SimpleDoubleProperty((double) ois.readObject());
        salesProperty = new SimpleDoubleProperty((double) ois.readObject());
        stockExchange = (GieldaPapierowWartosciowych) ois.readObject();
        usuwany = new SimpleBooleanProperty((boolean) ois.readObject());
    }
}
