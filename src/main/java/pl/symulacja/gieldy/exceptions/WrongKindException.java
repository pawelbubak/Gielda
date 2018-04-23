package pl.symulacja.gieldy.exceptions;

/**
 * Wyjątek rzucany gdy wprowadzone przez użytkoownika dane są niepoprawne
 * @author Paweł
 */
public class WrongKindException extends Exception {
    private String wiadomosc = null;

    public WrongKindException(){
        super();
    }

    public WrongKindException(String ms){
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
