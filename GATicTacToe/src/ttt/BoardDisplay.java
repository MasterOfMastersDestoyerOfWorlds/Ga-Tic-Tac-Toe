package ttt;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author benji
 */
public class BoardDisplay extends JFrame {

    private final Board board;
    private final JButton[][] buttons;
    private final boolean loseOnIllegality;

    public BoardDisplay(Board board, boolean loseOnIllegality) {
        this.board = board;
        this.loseOnIllegality = loseOnIllegality;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        buttons = new JButton[3][3];
        ActionListener onClickCellButton = (ActionEvent e) -> {
            for (int y = 0; y < buttons.length; y++) {
                for (int x = 0; x < buttons[y].length; x++) {
                    if (buttons[y][x] == e.getSource()) {
                        buttons[y][x].setEnabled(false);
                        if (board.getXTurn()) {
                            if(board.setCell(Board.CellValue.X, y, x) == -1){
                                System.out.println("error setting cell value");
                            }
                            buttons[y][x].setText("X");
                            revalidate();
                            if(board.hasWinner(false)){
                                displayMessage("player X won!");
                            }
                        } else {
                            if(board.setCell(Board.CellValue.O, y, x) == -1){
                                System.out.println("error setting cell value");
                            }
                            buttons[y][x].setText("O");
                            revalidate();
                            if(board.hasWinner(false)){
                                displayMessage("player O won!");
                            }
                        }
                        break;
                    }
                }
            }
        };
        GridLayout gridLayout = new GridLayout(3, 3);
        setLayout(gridLayout);
        for(int y = 0; y < buttons.length; y++){
            for(int x = 0; x < buttons.length; x++){
                buttons[y][x] = new JButton();
                buttons[y][x].setFont(new Font(Font.SERIF, Font.BOLD, 100));
                buttons[y][x].setAlignmentX(JButton.CENTER);
                buttons[y][x].setHorizontalAlignment(JButton.CENTER);
                buttons[y][x].addActionListener(onClickCellButton);
                add(buttons[y][x]);
            }
        }
    }
    
    private JButton getButton(int cellNumber){
        return buttons[cellNumber / 3][cellNumber % 3];
    }

    public void clickCell(int cellNumber){
        getButton(cellNumber).doClick();
    }
    
    public void clickCell(int row, int column){
        if(buttons[row][column].isEnabled()){
            buttons[row][column].doClick();
        } else if(loseOnIllegality){
            if(board.getXTurn()){
                displayMessage("player X attempted an illegal move and lost!");
            } else{
                displayMessage("player X attempted an illegal move and lost!");
            }
        }
    }
    
    private void displayMessage(String message){
        JOptionPane.showOptionDialog(this, message, null, JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"OK"}, "OK");
        reset();
    }
    
    public Board getBoard(){
        return board;
    }
    
    public void reset(){
        board.reset();
        for(int y = 0; y < buttons.length; y++){
            for(int x = 0; x < buttons[y].length; x++){
                buttons[y][x].setText("");
                buttons[y][x].setEnabled(true);
            }
        }
    }
}