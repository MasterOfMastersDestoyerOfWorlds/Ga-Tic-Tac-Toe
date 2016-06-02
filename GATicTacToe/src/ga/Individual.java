package ga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;

/**
 * @author benji
 */
public class Individual {

    private final Chromosome chromosome;
    /**
     * a representation of the effectiveness of this individual for the problems
     * presented in the @code{data} objects
     */
    private double fitness;
    /**
     * a comparator for ordering individuals by fitness from least to greatest
     * by using Arrays.sort(Individual[], Individual.FITNESS_COMPARATOR)
     * Individual.FITNESS_COMPARATOR
     */
    public static final Comparator<Individual> FITNESS_COMPARATOR = (Individual i1, Individual i2) -> ((Double) i1.fitness).compareTo(i2.fitness);

    public Individual() {
        chromosome = null;
    }

    /**
     * @param data
     * @param type a subclass of Allele that represents a solution to a single
     * datum in the data parameter
     * @param randomizedSolutions true if the array of alleles of this object
     * should have randomized solutions (for generation 0)
     * @throws ga.AlleleException
     */
    public Individual(Problem[] data, Class<? extends Allele> type, boolean randomizedSolutions) throws AlleleException {
        this.chromosome = new Chromosome(data, type, randomizedSolutions);
    }

    /**
     * The ordering of the i1 and i2 parents have no affect on the children.
     *
     * @param i1 first parent of the new children
     * @param i2 second parent of the new children
     * @return two new children with genes based on a random cross of the parent
     * genes
     * @throws ga.IndividualException
     * @throws ga.AlleleException
     */
    public static Individual[] cross(Individual i1, Individual i2) throws IndividualException, AlleleException {
        if (i1.chromosome.genes.length != i2.chromosome.genes.length) {
            throw new IndividualException("chromosome lengths of i1 and i2 are unequal");
        } else if (i1.chromosome.data != i2.chromosome.data) {
            throw new IndividualException("i1 data and i2 data are unequal");
        }
        final Individual[] children = new Individual[2];
        children[0] = new Individual(i1.chromosome.data, i1.chromosome.genes[0].getClass(), false);
        children[1] = new Individual(i1.chromosome.data, i1.chromosome.genes[0].getClass(), false);
        Allele[][] childrenSolutions = new Allele[2][i1.chromosome.genes.length];
        final int chiasma = (int) (Math.random() * i1.chromosome.genes.length);
        for (int x = 0; x < i1.chromosome.genes.length; x++) {

            if (x < chiasma) {
                childrenSolutions[0][x] = i1.chromosome.genes[x];
                childrenSolutions[1][x] = i2.chromosome.genes[x];
            } else {
                childrenSolutions[0][x] = i2.chromosome.genes[x];
                childrenSolutions[1][x] = i1.chromosome.genes[x];
            }
        }
        children[0].chromosome.setSolutions(childrenSolutions[0]);
        children[1].chromosome.setSolutions(childrenSolutions[1]);
        return children;
    }

    public void save(File file, Class<? extends Problem> problemClass, Class<? extends Allele> alleleClass) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(problemClass, new ProblemTypeAdapter());
        gsonBuilder.registerTypeAdapter(alleleClass, new AlleleTypeAdapter());
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(this);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(json);
            fileWriter.flush();
        }
    }

    public static Individual getIndividualFromFile(File file, Class<? extends Problem> problemClass, Class<? extends Allele> alleleClass) throws FileNotFoundException, IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String json = bufferedReader.readLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(problemClass, new ProblemTypeAdapter());
            gsonBuilder.registerTypeAdapter(alleleClass, new AlleleTypeAdapter());
            Gson gson = gsonBuilder.create();
            return gson.fromJson(json, Individual.class);
        }
    }

    public Allele getSolution(Problem datum) {
        return chromosome.getSolution(datum);
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

    private class Chromosome {

        /**
         * objects to represent situations a solution corresponds to
         */
        private final Problem[] data;
        /**
         * solutions to the problems represented in the @code{data} objects
         */
        private Allele[] genes;

        public Chromosome(Problem[] data, Class<? extends Allele> type, boolean randomizedGenes) throws AlleleException {
            try {
                this.data = data;
                genes = new Allele[data.length];
                for (int x = 0; x < genes.length; x++) {
                    genes[x] = (Allele) type.newInstance();
                    if (randomizedGenes) {
                        genes[x].randomize();
                    }
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new AlleleException(e);
            }
        }

        /**
         * @param datum the situation to solve
         * @return the corresponding allele that solves the given situation: the
         * index of the @param{datum} in the @code{data} array is the same index
         * of the Allele object in the @code{genes} array. i.e. the alleles
         * correspond to their respective datum based on index of the
         * @code{gene} array and @code{data} array.
         */
        public Allele getSolution(Problem datum) {
            for (int x = 0; x < data.length; x++) {
                if (data[x].equalsProblem(datum)) {
                    return genes[x];
                }
            }
            return null;
        }

        public void setSolutions(Allele[] genes) {
            this.genes = genes;
        }
    }
}
