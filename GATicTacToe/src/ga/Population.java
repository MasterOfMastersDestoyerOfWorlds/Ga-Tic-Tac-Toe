package ga;

import java.util.Arrays;
import ttt.TTTJudge;

/**
 * @author benji
 */
public class Population {
    /** The set of problems that Individual objects' genes solve: used in the Individual constructor @see{Individual}*/
    private final Problem[] data;
    /** A subclass of Allele: used in the Individual constructor @see{Individual}*/
    private final Class<? extends Allele> type;
    /** Array of all the Individual objects composing this Population, sorted by fitness from least to greatest */
    private final Individual[] individuals;
    /** Summation of all the fitness values of the Individual objects in this population */
    private float totalFitness;
    /**
     * @param data
     * @param type @see{Individual}
     * @param randomPopulation randomizes the solutions of the Individuals in this population for generation 0 @see{Individual}
     * @param size number of individuals to compose this individual
     * @throws ga.AlleleException
     */
    public Population(Problem[] data, Class<? extends Allele> type, boolean randomPopulation, int size) throws AlleleException {
        this.data = data;
        this.type = type;
        individuals = new Individual[size];
        for (int x = 0; x < individuals.length; x++) {
            individuals[x] = new Individual(data, type, randomPopulation);
        }
    }
    
    /**
     * @param fitnessAssessment used to calculate the fitness of Individual objects
     * @return the total fitness of this Population
     * We sort @code{individuals} array is sorted by fitness from least to greatest
     */
    public double assignFitnesses(FitnessAssessment fitnessAssessment){
        totalFitness = 0;
        for(int x = 0; x < individuals.length; x++){
            for(int i = 0; i < 10; i++){
                fitnessAssessment.compete(individuals[x], individuals[(int) (Math.random() * individuals.length)]);
                //fitnessAssessment.test(individuals[x]);
            }
            totalFitness += individuals[x].getFitness();
        }
        System.out.println(fitnessAssessment.toString());
        Arrays.sort(individuals, Individual.FITNESS_COMPARATOR);
        return totalFitness;
    }
    
    /**
     * @param elitism changes the method by which parents are selected a new generation:
     *                <ul>
     *                  if true:
     *                  <li>The fittest half of the population are chosen to be bred</li>
     *                  else
     *                  <li>Individual objects are still chosen by fitness, but the selection process is more random.</li>
     *                  <li>The higher the fitness, the greater the chance a given Individual object may be chosen to breed</li>
     *                </ul>
     *                Setting elitism false explores the solution space more, but at the cost of time.
     *                For instance, Sickle Cell Anemia helps those afflicted resist Malaria; elitism would
     *                prevent Sickle Cell Anemics from breeding, whereas the lack of elitism may allow this
     *                benefit to continue through the generations.  These sorts of benefits may not be apparent
     *                at first and they may even seem like a negative aspect of an Individual; Sickle Cell Anemics'
     *                resistance to Malaria went unnoticed for a long time, but the supposedly pure negative disease has benefits.
     *                Depending on the type and complexity of the problem, elitism may or may not result in new solutions,
     *                so trading time for thoroughness may or may not be preferable.
     * @return a new generation of children, the parents of whom are the
     *         Individual objects in this Population object.
     * @throws ga.IndividualException
     * @throws ga.AlleleException
     */
    public Population breedNewGeneration(boolean elitism) throws IndividualException, AlleleException {
        Population population = new Population(data, type, false, individuals.length);
        Individual[] sortedIndividuals = individuals.clone();
        if (elitism) {
            for (int x = 0; x < sortedIndividuals.length; x += 2) {
                Individual[] children = Individual.cross(sortedIndividuals[x], sortedIndividuals[x + 1]);
                population.individuals[x] = children[0];
                population.individuals[x + 1] = children[1];
            }
            if (population.individuals.length % 2 == 1) {
                population.individuals[individuals.length - 1] = Individual.cross(sortedIndividuals[population.individuals.length / 2 + 1], sortedIndividuals[population.individuals.length / 2 + 2])[(int) (Math.random() * 2)];
            }
        } else {
            for (int i = 0; i < sortedIndividuals.length; i += 2) {
                int minimaIndividual = 0;
                double requiredFitness = Math.random() * sortedIndividuals[0].getFitness();
                for (int x = 0; x < sortedIndividuals.length; x++) {
                    if(sortedIndividuals[x].getFitness() < requiredFitness){
                        minimaIndividual = x;
                        break;
                    }
                }
                Individual[] parents = new Individual[2];
                parents[0] = sortedIndividuals[(int) (Math.random() * minimaIndividual)];
                parents[1] = sortedIndividuals[(int) (Math.random() * minimaIndividual)];
                Individual[] children = Individual.cross(parents[0], parents[1]);
                population.individuals[i] = children[0];
                population.individuals[i + 1] = children[1];
            }
            if(population.individuals.length % 2 == 1){
                int minimaIndividual = 0;
                double requiredFitness = Math.random() * sortedIndividuals[0].getFitness();
                for (int x = 0; x < sortedIndividuals.length; x++) {
                    if(sortedIndividuals[x].getFitness() < requiredFitness){
                        minimaIndividual = x;
                        break;
                    }
                }
                Individual[] parents = new Individual[2];
                parents[0] = sortedIndividuals[(int) (Math.random() * minimaIndividual)];
                parents[1] = sortedIndividuals[(int) (Math.random() * minimaIndividual)];
                Individual[] children = Individual.cross(parents[0], parents[1]);
                population.individuals[population.individuals.length - 1] = children[(int) (Math.random() * 2)];
            }
        }
        return population;
    }
    
    public Individual getFittestIndividual(){
        return individuals[0];
    }
    
    public Allele getBestSolution(Problem datum){
        return individuals[0].getSolution(datum);
    }
}