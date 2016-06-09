package ttt;

import ga.FitnessAssessment;
import ga.Individual;

/**
 * @author benji
 */
public class TTTJudge extends FitnessAssessment {

    private int illegalMoves;
    private int wins;
    private int ties;

    public void compete(Individual i1, Individual i2, Board board, boolean i1turn) {
        if (board == null) {
            board = new Board();
        }
        if (board.full()){
            ties++;
            i1.setFitness(i1.getFitness()  + 5);
            i2.setFitness(i2.getFitness() + 5);
        }
        int solution;
        if (i1turn) {
            solution = ((TTTGene) (i1.getChromosome().getGenes()[board.bitmap()])).getTargetCell();
        } else {
            solution = ((TTTGene) (i2.getChromosome().getGenes()[board.bitmap()])).getTargetCell();
        }
        int postSetCell = board.setCell(Board.CellValue.X, solution);
        if(postSetCell != -2){
            if(board.hasWinner(false)){
                wins++;
                if(i1turn){
                    i1.setFitness(i1.getFitness() + 1);
                    i2.setFitness(i2.getFitness() - 1);
                } else{
                    i1.setFitness(i1.getFitness() - 1);
                    i2.setFitness(i2.getFitness() + 1);
                }
            } else{
                board.invert();
                compete(i1, i2, board, !i1turn);
            }
        } else{
            illegalMoves++;
            if(i1turn){
                i1.setFitness(i1.getFitness() - 5);
            } else{
                i2.setFitness(i2.getFitness() - 5);
            }
        }
    }

    @Override
    public void compete(Individual individual, Individual... competitors) {
        compete(individual, competitors[0], new Board(), Math.random() >= 0.5);
    }

    @Override
    public void test(Individual individual) {
    }

    @Override
    public String toString() {
        return "Illegal Moves: " + illegalMoves + "\n" + "Wins: " + wins + "\n" + "Ties: " + ties;
    }
}