package ttt;

//package ttt;
///**
// * @author benji
// */
//public class BoardDisplayDriver {
//    public static void main(String[] args){
//        BoardDisplay boardDisplay = new BoardDisplay(new Board(-1), true);
//        boardDisplay.setVisible(true);
//    }
//}
////package ttt;
////
////import ga.Individual;
////import java.io.File;
////
/////**
//// * @author benji
//// */
////public class BoardDisplayDriver {
////
////    public static void main(String[] args) {
////        try {
////            BoardDisplay boardDisplay = new BoardDisplay(new Board(), true);
////            boardDisplay.setVisible(true);
////            final File file = new File("C:\\Users\\benji\\Desktop\\solution.txt");
////            Individual computer = Individual.getIndividualFromFile(file, Board.class, TTTGene.class);
////            boardDisplay.clickCell(((TTTGene) computer.getSolution(boardDisplay.getBoard())).getTargetCell());
////        } catch(Exception e){
////            e.printStackTrace();
////        }
////    }
////}
