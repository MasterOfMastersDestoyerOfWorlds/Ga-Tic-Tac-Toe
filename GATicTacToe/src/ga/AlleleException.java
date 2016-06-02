package ga;


/**
 * @author benji
 */
public class AlleleException extends Exception{

    public AlleleException() {
    }

    public AlleleException(String string) {
        super(string);
    }

    public AlleleException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public AlleleException(Throwable thrwbl) {
        super(thrwbl);
    }

    public AlleleException(String string, Throwable thrwbl, boolean bln, boolean bln1) {
        super(string, thrwbl, bln, bln1);
    }
}
