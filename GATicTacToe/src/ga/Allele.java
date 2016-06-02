package ga;

/**
 * @author benji
 * A representation of a solution to a problem.
 * This class and subclasses are not concerned with the problem to which it solves.
 */
public abstract class Allele{
    /**
     * Randomizes this object's solution.
     */
    public abstract void randomize();
}