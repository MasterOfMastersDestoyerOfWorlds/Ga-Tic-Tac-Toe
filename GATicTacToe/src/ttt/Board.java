package ttt;

import ga.Problem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author benji
 */
public class Board extends Problem {

    public final int[][] cells;
    private boolean xTurn;
    final int winConditions[][] = new int[][]{
        /*Horizontal Wins*/
        {0, 1, 2},
        {3, 4, 5},
        {6, 7, 8},
        /*Vertical Wins*/
        {0, 3, 6},
        {1, 4, 7},
        {2, 5, 8},
        /*Diagonal Wins*/
        {0, 4, 8},
        {2, 4, 6}
    };

    @Override
    public boolean equalsProblem(Problem board) {
        if (board instanceof Board) {
            for (int y = 0; y < cells.length; y++) {
                for (int x = 0; x < cells[y].length; x++) {
                    if (cells[y][x] != ((Board) board).cells[y][x]) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public enum CellValue {
        X,
        O,
        SPACE;

        public int getValue() {
            switch (this) {
                case X:
                    return 1;
                case O:
                    return 2;
                case SPACE:
                    return 0;
                default:
                    return 0;
            }
        }
    }

    public Board invert() {
        Board invertedBoard = new Board(-1);
        invertedBoard.xTurn = !xTurn;
        for (int y = 0; y < invertedBoard.cells.length; y++) {
            for (int x = 0; x < invertedBoard.cells[y].length; x++) {
                if (cells[y][x] == CellValue.X.getValue()) {
                    invertedBoard.cells[y][x] = CellValue.O.getValue();
                } else if (cells[y][x] == CellValue.O.getValue()) {
                    invertedBoard.cells[y][x] = CellValue.X.getValue();
                }
            }
        }
        return invertedBoard;
    }
    
    public Problem[] getAllProblemsWithoutWinners(int thoroughness, boolean repeats) {
        List<Problem> problems = Arrays.asList(getAllProblems(thoroughness, repeats));
        Iterator<Problem> iterator = problems.iterator();
        while(iterator.hasNext()){
            Board problem = (Board) iterator.next();
            if(!problem.hasWinner(false)){
                iterator.remove();
            }
        }
        return (Problem[]) problems.toArray(new Problem[0]);
    }

    @Override
    public Problem[] getAllProblems(int thoroughness, boolean repeats) {
        if (repeats) {
            Board[] boards = new Board[thoroughness];
            for (int x = 0; x < thoroughness; x++) {
                boards[x] = Board.getRandomBoard();
            }
            return boards;
        }
        List<Problem> boards = new ArrayList<>();
        for (int x = 0; x < thoroughness; x++) {
            Board board = Board.getRandomBoard();
            boolean contains = false;
            for (Problem b : boards) {
                if (b.equals(board)) {
                    contains = true;
                }
            }
            if (!contains) {
                boards.add(board);
                //System.out.println(board);
            }
            if(x % 10000 == 0){
                System.out.println(x + " - " + boards.size());
            }
        }
        return (Problem[]) boards.toArray(new Problem[0]);
    }
    
    public Board(){
        this(-1);
    }

    public Board(int xTurn) {
        switch (xTurn) {
            case 0:
                this.xTurn = false;
                break;
            case 1:
                this.xTurn = true;
                break;
            default:
                this.xTurn = Math.random() >= 0.5;
                break;
        }
        cells = new int[3][];
        for (int y = 0; y < 3; y++) {
            cells[y] = new int[3];
        }
    }

    public int setCell(int cell) {
        if (xTurn) {
            return setCell(CellValue.X, cell);
        } else {
            return setCell(CellValue.O, cell);
        }
    }

    public int setCell(CellValue cellValue, int cell) {
        return setCell(cellValue, (int) cell / 3, cell % 3);
    }

    public int setCell(CellValue cellValue, int row, int column) {
        if (row >= 0 && row < cells.length) {
            if (column >= 0 && column < cells[row].length) {
                if (cells[row][column] != CellValue.SPACE.getValue()) {
                    return -2;
                }
                xTurn = !xTurn;
                if (row < cells.length && row >= 0) {
                    if (column < cells[row].length && column >= 0) {
                        int replacedValue = cells[row][column];
                        cells[row][column] = cellValue.getValue();
                        return replacedValue;
                    }
                }
            }
        }
        return -1;
    }

    public static Board getRandomBoard() {
        final int turnsPlayed = (int) (Math.random() * 10);
        final int xTurns, oTurns;
        if (Math.random() >= 0.5) {
            xTurns = (int) Math.ceil((double) turnsPlayed / 2);
            oTurns = turnsPlayed - xTurns;
        } else {
            oTurns = (int) Math.ceil((double) turnsPlayed / 2);
            xTurns = turnsPlayed - oTurns;
        }
        int xRemainder = xTurns;
        int oRemainder = oTurns;
        int spaceRemainder = 9 - xTurns - oTurns;
        Board board = new Board(-1);
        for (int x = 0; x < 9; x++) {
            final double xRange = (double) xRemainder / (9 - x);
            final double oRange = (double) oRemainder / (9 - x) + xRange;
            final double spaceRange = (double) spaceRemainder / (9 - x + oRange);
            final double totalRange = xRange + oRange + spaceRange;
            final double selection = Math.random() * totalRange;
            if (selection <= xRange) {
                board.setCell(CellValue.X, x);
                xRemainder--;
            } else if (selection <= oRange) {
                board.setCell(CellValue.O, x);
                oRemainder--;
            } else {
                board.setCell(CellValue.SPACE, x);
                spaceRemainder--;
            }
        }
        return board;
    }

    public boolean hasWinner(boolean multiplicity) {
        return getWinner(multiplicity) != CellValue.SPACE.getValue();
    }

    public int getWinner(boolean multiplicity) {
        return getWinningCells(multiplicity)[0][0];
    }

    public int[][] getWinningCells(boolean multiplicity) {
        int[][] winningCells = new int[2][3];
        for (int y = 0; y < winningCells.length; y++) {
            for (int x = 0; x < winningCells[y].length; x++) {
                winningCells[y][x] = CellValue.SPACE.getValue();
            }
        }
        int turnsPlayed = 0;
        for (int[] row : cells) {
            for (int x : row) {
                if (x != CellValue.SPACE.getValue()) {
                    turnsPlayed++;
                }
            }
        }
        if (turnsPlayed < 5) {
            return winningCells;
        }
        boolean hasWinner = false;
        for (int[] winCondition : winConditions) {
            int[] targetCells = new int[winCondition.length];
            for (int x = 0; x < winCondition.length; x++) {
                targetCells[x] = getCell(winCondition[x]);
            }
            boolean allCellsEqual = true;
            for (int x = 0; x < targetCells.length - 1; x++) {
                if (targetCells[x] != targetCells[x + 1]) {
                    allCellsEqual = false;
                    break;
                }
            }
            if (allCellsEqual && targetCells[0] != CellValue.SPACE.getValue()) {
                if (!multiplicity) {
                    winningCells[0] = targetCells;
                    return winningCells;
                }
                if (!hasWinner) {
                    hasWinner = true;
                    winningCells[0] = targetCells;
                } else {
                    winningCells[1] = targetCells;
                    return winningCells;
                }
            }
        }
        return winningCells;
    }
    
    public boolean full(){
        for(int[] row : cells){
            for(int x : row){
                if(x == CellValue.SPACE.getValue()){
                    return false;
                }
            }
        }
        return true;
    }

    public void reset() {
        for (int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[y].length; x++) {
                cells[y][x] = CellValue.SPACE.getValue();
            }
        }
    }

    public boolean equals(Board board) {
        for (int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[y].length; x++) {
                if (cells[y][x] != board.cells[y][x]) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getCell(int cell) {
        return cells[cell / 3][cell % 3];
    }

    public int[][] getCells() {
        return cells;
    }

    public boolean getXTurn() {
        return xTurn;
    }

    @Override
    public String toString() {
        String string = "";
        for (int[] cell : cells) {
            for (int x = 0; x < cell.length; x++) {
                string += cell[x] + " ";
            }
            string += "\n";
        }
        return string;
    }
    
    @Override
    public boolean equals(Object board){
        if(board instanceof Board){
            for(int y = 0; y < cells.length; y++){
                for(int x = 0; x < cells[y].length; x++){
                    if(cells[y][x] != ((Board) board).cells[y][x]){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
