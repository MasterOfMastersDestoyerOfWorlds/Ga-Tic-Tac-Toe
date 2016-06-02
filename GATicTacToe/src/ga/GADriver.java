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
            Problem[] data = new Board(-1).getProblemsFromFile(file, Board.class);
            Board board = new Board();
            //Problem[] data = board.getAllProblems(1000000, false);
            //board.save(data, file);
            Population population = new Population(data, TTTGene.class, true, 1000);
            TTTJudge judge = new TTTJudge();
            for(int x = 0; x < 25; x++){
                System.out.println(population.assignFitnesses(judge));
                System.out.println("----------------------------\nbreeding generation " + String.valueOf(x +1) + "\n----------------------------");
                population = population.breedNewGeneration(true);
            }
            population.getFittestIndividual().save(individualFile, Board.class, Allele.class);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}