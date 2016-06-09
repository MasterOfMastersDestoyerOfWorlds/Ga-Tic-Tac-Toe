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
     */
    public abstract void test(Individual individual);

    /**
     * all individuals have their fitnesses set
     * @param individual
     * @param competitors
     */
    public abstract void compete(Individual individual, Individual... competitors);
}