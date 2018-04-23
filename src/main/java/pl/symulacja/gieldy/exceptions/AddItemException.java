package pl.symulacja.gieldy.exceptions;

/**
 * Wyjątek rzucany gdy nie można dodać instancji wybranej klasy
 * @author Paweł
 */
public class AddItemException extends Exception {
    private String wiadomosc = null;
    private Object object = null;

    public AddItemException(String ms){
        super();
        this.wiadomosc = ms;
    }

    public AddItemException(Object object){
        super();
        this.object = object;
    }

    /**
     * Pobiera Obiekt pobrany podczas rzucania błędu
     * @return Istniejący już obiekt
     */
    public Object getObject() {
        return object;
    }

    /**
     * Pobiera treść wiadomości zainicjowanej podczas rzucania wyjątku
     * @return treść wiadomości
     */
    public String getWiadomosc() {
        return wiadomosc;
    }

    @Override
    public String toString() {
        return wiadomosc + "\n" + super.toString();
    }
}
