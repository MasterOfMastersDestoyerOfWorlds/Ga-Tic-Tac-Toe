package ttt;

import ga.FitnessAssessment;
import ga.Individual;

/**
 * @author benji
 */
public class TTTJudge extends FitnessAssessment {

    private int illegalMoves;
    private int wins;

    public TTTJudge() {
        illegalMoves = 0;
        wins = 0;
    }

    public void drivethrougthepark(Individual indv, Individual indv2, Board board, boolean i1turn) {
        if (board == null) {
            board = new Board();
        }
        if (board.full()){
            indv.setFitness(indv.getFitness()  + 5);
            indv2.setFitness(indv2.getFitness() + 5);
        }
        int solution;
        if (i1turn) {
            solution = ((TTTGene) (indv.getChromosome().getGenes()[board.bitmap()])).getTargetCell();
        } else {
            solution = ((TTTGene) (indv2.getChromosome().getGenes()[board.bitmap()])).getTargetCell();
        }
        int postSetCell = board.setCell(Board.CellValue.X, solution);
        if(postSetCell != -1){
            if(board.hasWinner(false)){
                if(i1turn){
                    indv.setFitness(indv.getFitness() + 1);
                    indv2.setFitness(indv2.getFitness() - 1);
                } else{
                    indv.setFitness(indv.getFitness() - 1);
                    indv2.setFitness(indv2.getFitness() + 1);
                }
            }
            board.invert();
            drivethrougthepark(indv, indv2, board, !i1turn);
        } else{
            if(i1turn){
                indv.setFitness(indv.getFitness() - 5);
            } else{
                indv2.setFitness(indv2.getFitness() - 5);
            }
        }
    }

    @Override
    public void compete(Individual i1, Individual i2) {
        Board board = new Board();
        boolean i1Turn = Math.random() >= 0.5;
        while (!board.full()) {
            if (!board.getXTurn()) {
                board = board.invert();
            }
            if (i1Turn) {
                TTTGene solution = (TTTGene) i1.getSolution(board);
                if (solution == null) {
                    System.out.println("Unknown Problem:\n" + board.toString());
                    return;
                } else {
                    int targetCell = solution.getTargetCell();
                    if (board.setCell(targetCell) == -2) {
//                        System.out.println("Illegal Move: " + i1.getFitness());
                        illegalMoves++;
                        i1.setFitness(i1.getFitness() - 1);
                        return;
                    } else if (board.hasWinner(false)) {
                        i1.setFitness(i1.getFitness() + 10);
//                        System.out.println("Winner: " + i1.getFitness());
                        wins++;
                        return;
                    }
                }
            } else {
                TTTGene solution = (TTTGene) i2.getSolution(board);
                if (solution == null) {
                    System.out.println("Unknown Problem:\n" + board.toString());
                    return;
                } else {
                    int targetCell = solution.getTargetCell();
                    if (board.setCell(targetCell) == -2) {
//                        System.out.println("Illegal Move: " + i2.getFitness());
                        illegalMoves++;
                        i2.setFitness(i2.getFitness() - 1);
                        return;
                    } else if (board.hasWinner(false)) {
                        i2.setFitness(i2.getFitness() + 10);
//                        System.out.println("Winner: "  + i2.getFitness());
                        wins++;
                        return;
                    }
                }
            }
            i1Turn = !i1Turn;
        }
    }

    @Override
    public double test(Individual individual) {
        compete(individual, individual);
        return individual.getFitness();
    }

    @Override
    public String toString() {
        return "Illegal Moves: " + illegalMoves + "\n" + "Wins: " + wins;
    }
}
