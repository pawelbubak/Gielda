package pl.symulacja.gieldy.exceptions;

/**
 * Wyjątek rzucany gdy nie można usunąć wybranego obiektu
 * @author Paweł
 */
public class DeleteItemException extends Exception{
    private String wiadomosc = null;

    public DeleteItemException(String ms){
        super();
        this.wiadomosc = ms;
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
