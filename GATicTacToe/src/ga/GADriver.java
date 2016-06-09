package ga;

import java.io.File;
import ttt.TTTGene;
import ttt.TTTJudge;

/**
 * @author benji
 */
public class GADriver {
    public static void main(String[] args){
        try{
            final File individualFile = new File("C:\\Users\\benji\\Desktop\\solution.txt");
            Population population = new Population(TTTGene.class, true, 100, 261632);
            for(int x = 0; x < 100; x++){
                System.out.println("-----------------------\nGeneration " + x + "\n-----------------------");
                population.assignFitnesses(new TTTJudge(), false);
                population.breedNewGeneration(false);
            }
            population.getFittestIndividual().save(individualFile, TTTGene.class);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}