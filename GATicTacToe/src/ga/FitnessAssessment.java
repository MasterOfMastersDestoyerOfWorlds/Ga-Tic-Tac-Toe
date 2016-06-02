package ga;

/**
 * @author benji
 * Extend this class and implement the @code{test(Individual)} method to use
 * before breeding a new population.  The Population object assigns fitness values
 * to every individual based on the output of the @code{test(Individual)} function.
 */
public abstract class FitnessAssessment {
    /**
     * @param individual the Individual object to assess for fitness
     * @return the fitness of the @param{individual}
     */
    public abstract double test(Individual individual);
    public abstract void compete(Individual i1, Individual i2);
}