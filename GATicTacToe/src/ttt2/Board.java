package ttt2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author benji
 */
public class Board {
    public final int[][] cells;
    public enum CellValue{
        X,
        O,
        SPACE;
        public int getValue(){
            switch(this){
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
    
    public static void main(String[] args){
        for(int x = 0; x < 10; x++){
            System.out.println(getAllBoards(1000000).size());
        }
    }
    
    public static List<Board> getAllBoards(int thoroughness){
        List<Board> boards = new ArrayList<>();
        for(int x = 0; x < thoroughness; x++){
            Board board = Board.getRandomBoard();
            boolean contains = false;
            for(Board b : boards){
                if(b.equals(board)){
                    contains = true;
                }
            }
            if(!contains){
                boards.add(board);
                //System.out.println(board);
            }
        }
        return boards;
    }
    
    public Board(){
        cells = new int[3][];
        for(int y = 0; y < 3; y++){
            cells[y]= new int[3];
        }
    }
    
    private int setCell(CellValue cellValue, int cell){
        return setCell(cellValue, (int) cell / 3, cell % 3);
    }
    
    private int setCell(CellValue cellValue, int row, int column){
        if(row < cells.length && row >= 0){
            if(column < cells[row].length && column >= 0){
                int replacedValue = cells[row][column];
                cells[row][column] = cellValue.getValue();
                return replacedValue;
            }
        }
        return -1;
    }
    
    public static Board getRandomBoard(){
        final int turnsPlayed = (int) (Math.random() * 10);
        final int xTurns, oTurns;
        if(Math.random() >= 0.5){
            xTurns = (int) Math.ceil((double)turnsPlayed / 2);
            oTurns = turnsPlayed - xTurns;
        } else{
            oTurns = (int) Math.ceil((double)turnsPlayed / 2);
            xTurns = turnsPlayed - oTurns;
        }
        final int spaces = 9 - turnsPlayed;
        int xRemainder = xTurns;
        int oRemainder = oTurns;
        int spaceRemainder = 9 - xTurns - oTurns;
        Board board = new Board();
        for(int x = 0; x < 9; x++){
            final double xRange = (double) xRemainder / (9 - x);
            final double oRange = (double) oRemainder / (9 - x) + xRange;
            final double spaceRange = (double) spaceRemainder / (9 - x + oRange);
            final double totalRange = xRange + oRange + spaceRange;
            final double selection = Math.random() * totalRange;
            if(selection <= xRange){
                board.setCell(CellValue.X, x);
                xRemainder--;
            } else if(selection <= oRange){
                board.setCell(CellValue.O, x);
                oRemainder--;
            } else{
                board.setCell(CellValue.SPACE, x);
                spaceRemainder--;
            }
        }
        return board;
    }
    
    public boolean equals(Board board){
        for(int y = 0; y < cells.length; y++){
            for(int x = 0; x < cells[y].length; x++){
                if(cells[y][x] != board.cells[y][x]){
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public String toString(){
        String string = "";
        for (int[] cell : cells) {
            for (int x = 0; x < cell.length; x++) {
                string += cell[x] + " ";
            }
            string += "\n";
        }
        return string;
    }
}