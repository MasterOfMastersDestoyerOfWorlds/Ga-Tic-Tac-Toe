package ga;
/**
 * @author benji
 */
public class IndividualException extends Exception{

    public IndividualException() {
    }

    public IndividualException(String string) {
        super(string);
    }

    public IndividualException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public IndividualException(Throwable thrwbl) {
        super(thrwbl);
    }

    public IndividualException(String string, Throwable thrwbl, boolean bln, boolean bln1) {
        super(string, thrwbl, bln, bln1);
    }
}