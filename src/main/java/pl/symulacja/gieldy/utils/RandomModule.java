package pl.symulacja.gieldy.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

/**
 * Klasa generująca informacje potrzebne do stworzenia obiektów
 * @author Paweł
 */
public class RandomModule {
    private static ArrayList<String> currencyName = new ArrayList<>();
    private static ArrayList<String> indexName = new ArrayList<>();
    private static ArrayList<String> stockExchangeName = new ArrayList<>();
    private static ArrayList<String> countryName = new ArrayList<>();
    private static ArrayList<String> townName = new ArrayList<>();
    private static ArrayList<String> streetName = new ArrayList<>();
    private static ArrayList<String> companyName = new ArrayList<>();
    private static ArrayList<String> nameName = new ArrayList<>();
    private static ArrayList<String> surnameName = new ArrayList<>();
    private static ArrayList<String> resourceName = new ArrayList<>();
    private static ArrayList<String> fundName = new ArrayList<>();

    /**
     * Zwraca wygenerowaną odpowiedź na żądanie
     * @param choise Żądanie dotyczące odpowiedniej odpowiedzi
     * @return wygenerowana/wylosowana wartość
     */
    public static String randName(String choise){
        int x;
        String result = null;
        Random random = new Random();

            switch (choise){
                case "currency":
                    x = currencyName.size();
                    if (x > 0)
                        result = currencyName.get(random.nextInt(x));
                    break;
                case "country":
                    x = countryName.size();
                    if (x > 0)
                        result = countryName.get(random.nextInt(x));
                    break;
                case "index":
                    x = indexName.size();
                    if (x > 0)
                        result = indexName.get(random.nextInt(x));
                    break;
                case "stockExchange":
                    x = stockExchangeName.size();
                    if (x > 0)
                        result = stockExchangeName.get(random.nextInt(x));
                    break;
                case "town":
                    x = townName.size();
                    if (x > 0)
                        result = townName.get(random.nextInt(x));
                    break;
                case "street":
                    x = streetName.size();
                    if (x > 0)
                        result = streetName.get(random.nextInt(x));
                    break;
                case "spolka":
                    x = companyName.size();
                    if (x > 0)
                        result = companyName.get(random.nextInt(x));
                    break;
                case "name":
                    x = nameName.size();
                    if (x > 0)
                        result = nameName.get(random.nextInt(x));
                    break;
                case "surname":
                    x = surnameName.size();
                    if (x > 0)
                        result = surnameName.get(random.nextInt(x));
                    break;
                case "resource":
                    x = resourceName.size();
                    if (x > 0)
                        result = resourceName.get(random.nextInt(x));
                    break;
                case "fund":
                    x = fundName.size();
                    if (x > 0)
                        result = fundName.get(random.nextInt(x));
                    break;
                case "pesel":
                        result = randPesel();
                    break;
                case "budget":
                    result = randBudget();
                    break;
                case "budget2":
                    result = randBudget2();
                    break;
                case "quantity":
                    result = randQuantity();
                    break;
                case "price":
                    result = randPrice();
                    break;
                case "priceA":
                    result = randPriceA();
                    break;
            }
        return result;
    }

    /**
     * Zaokrągla wartość pomnożoną przez 100
     * @param temp Wartość do zaokrąglenia typu double
     * @return Zaokrąglona wartość z zakresu od 0 do 100
     */
    public static double round(double temp) {
        temp *= 100;
        temp = Math.round(temp);
        return temp;
    }

    /**
     * Wczytywanie danych potrzebnych do losowania odpowiednich danych obiektów
     */
    public static void init(){
        try {
            countryName = wczytajPlik("utils/countryName.txt");
            indexName = wczytajPlik("utils/indexName.txt");
            stockExchangeName = wczytajPlik("utils/stockExchangeName.txt");
            townName = wczytajPlik("utils/townName.txt");
            streetName = wczytajPlik("utils/streetsName.txt");
            companyName = wczytajPlik("utils/companyName.txt");
            nameName = wczytajPlik("utils/nameName.txt");
            surnameName = wczytajPlik("utils/surnameName.txt");
            resourceName = wczytajPlik("utils/resourcesName.txt");
            currencyName = wczytajPlik("utils/currencyName.txt");
            fundName = wczytajPlik("utils/fundName.txt");
        } catch (IOException e) {
            DialogsUtils.errorDialog(e.getMessage() + "\n" );
        } catch (Exception e){
            DialogsUtils.errorDialog(e.toString());
        }
    }

    /**
     * Wczytuje pojedynczy plik z odpowiednimi nazwami do listy
     * @param path Scieżka do pliku
     * @return lista z danymi
     * @throws IOException Rzuca wyjątkiem jeśli nie znajdzie pliku
     */
    private static ArrayList<String> wczytajPlik(String path) throws IOException {
        ArrayList<String> temp = new ArrayList<>();
        String line;
        Charset ch = Charset.forName("UTF-8");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path),ch));
        while ((line = reader.readLine()) != null){
            temp.add(line);
        }
        reader.close();
        return temp;
    }

    private static String randPesel(){
        Random random = new Random();
        long x = random.nextInt(1000000);
        x = x + 10000000 * (random.nextInt(8)+1);
        Long temp = x;
        return temp.toString();
    }

    private static String randQuantity(){
        Random random = new Random();
        long x = random.nextInt(90);
        x = (x + 11) * 10;
        Long temp = x;
        return temp.toString();
    }

    private static String randBudget(){
        Random random = new Random();
        Integer x = random.nextInt(10000);
        if (x < 1000)
            x += 1000;
        return x.toString();
    }

    private static String randBudget2(){
        Random random = new Random();
        Integer x = random.nextInt(10000);
        if (x < 1000)
            x += 1000;
        return x.toString();
    }

    private static String randPrice(){
        Random random = new Random();
        Double x = random.nextDouble();
        if (x == 0)
            x += 0.1;
        x *= 100000;
        x = (double) Math.round(x);
        x /= 100;
        return x.toString();
    }

    private static String randPriceA(){
        Random random = new Random();
        Double x = random.nextDouble();
        if (x < 0.1)
            x += 0.1;
        x *= 1000;
        x = (double) Math.round(x);
        x /= 100;
        return x.toString();
    }
}
