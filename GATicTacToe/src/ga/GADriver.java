package ga;

import java.io.File;
import ttt.Board;
import ttt.TTTGene;
import ttt.TTTJudge;

/**
 * @author benji
 */
public class GADriver {
    public static void main(String[] args){
        try{
            final File file = new File("C:\\Users\\benji\\Desktop\\tttboards.txt");
            final File individualFile = new File("C:\\Users\\benji\\Desktop\\solution.txt");
            //final Individual fittestIndividual = Individual.getIndividualFromFile(individualFile, Board.class, TTTGene.class);
            Problem[] data = new Board(-1).getProblemsFromFile(file, Board.class);
            //Problem[] data = board.getAllProblems(1000000, false);
            //board.save(data, file);
            Population population = new Population(data, TTTGene.class, true, 100);
            final int generations = 20;
            for(int x = 0; x < generations; x++){
                System.out.println(population.assignFitnesses(new TTTJudge()));
                System.out.println("----------------------------\nbreeding generation " + String.valueOf(x +1) + "\n----------------------------");
                population = population.breedNewGeneration(false);
            }
            population.getFittestIndividual().save(individualFile, Board.class, Allele.class);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}