/**
 * December 21, 2014
 */
package geneticalgorithmframework.tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe implements ActionListener {

    JFrame window = new JFrame("Tic Tac Toe ");

    JMenuBar menuMain = new JMenuBar();
    JMenuItem menuMainMenu = new JMenuItem("    Main Menu"),
            menuInstruction = new JMenuItem("  Instructions"),
            menuExit = new JMenuItem("          Exit"),
            menuAbout = new JMenuItem("       About");

    JButton buttonPvP = new JButton("Player vs Player"),
            buttonPvCPU = new JButton("Player vs Computer"),
            buttonQuit = new JButton("Quit"),
            buttonSetName = new JButton("Set Player Names"),
            buttonContinue = new JButton("Continue"),
            buttonPlayAgain = new JButton("Play Again");
//buttons make the board
    JButton buttonEmpty[] = new JButton[10];

    JPanel panelNewGame = new JPanel(),
            panelMenu = new JPanel(),
            panelMain = new JPanel(),
            panelTop = new JPanel(),
            panelBottom = new JPanel(),
            panelQuitTryAgain = new JPanel(),
            panelPlayingField = new JPanel();

    JLabel labelTitle = new JLabel("Tic Tac Toe"),
            labelBackground = new JLabel(),
            labelTurn = new JLabel(),
            labelStatus = new JLabel("", JLabel.CENTER);
    JTextArea textMessage = new JTextArea();

    /*
 * Board number layout:
 * 
 * |1| |2| |3|
 * 
 * |4| |5| |6|
 * 
 * |7| |8| |9|
     */
    final int winCombo[][] = new int[][]{
        {1, 2, 3}, {1, 4, 7}, {1, 5, 9},
        {4, 5, 6}, {2, 5, 8}, {3, 5, 7},
        {7, 8, 9}, {3, 6, 9}
    /*Horizontal Wins*/ /*Vertical Wins*/ /*Diagonal Wins*/
    };

//background of the GUI
    ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("TTTbackground.png"));
//x image
    ImageIcon iconX = new ImageIcon(getClass().getResource("iconX.png"));
//o image
    ImageIcon iconO = new ImageIcon(getClass().getResource("iconO.png"));
//unselected box image is a metal mesh texture
    ImageIcon iconMetalMesh = new ImageIcon(getClass().getResource("metalMesh.png"));

    final int X = 535, Y = 342,
            mainColorR = 50, mainColorG = 50, mainColorB = 50,
            buttonColorR = 100, buttonColorG = 100, buttonColorB = 100;
    Color colorButtonWonColor = new Color(190, 190, 190);
    int turn = 1,
            player1Score = 0,
            player2Score = 0,
            winBox1 = 1,
            winBox2 = 1,
            winBox3 = 1;
    int option;
    boolean inGame = false;
    boolean win = false;
    boolean buttonEmptyClicked = false;
    String message, whoTurn,
            Player1 = "Player 1", Player2 = "Player 2"; //editable player names in game

//--------------------------------
//end of variable declarations
//--------------------------------
    public static void main(String[] args) {
        new TicTacToe();
    }

    /*
 * set the game properties, layout, and style
     */
    public TicTacToe() {
        //Set window properties:
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = gd.getDisplayMode().getWidth();   //width of monitor
        int screenHeight = gd.getDisplayMode().getHeight(); //height of monitor
        window.setSize(X, Y);
        window.setLocation(screenWidth / 4, screenHeight / 4);
        window.setResizable(false);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set Menu, Main, Top, Bottom Panel Layout/Backgrounds
        panelMenu.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelTop.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelBottom.setLayout(new FlowLayout(FlowLayout.CENTER));

        //Color panel backgrounds
        panelNewGame.setBackground(new Color(mainColorR - 50, mainColorG - 50, mainColorB - 50));
        panelMenu.setBackground(new Color((mainColorR - 50), (mainColorG - 50), (mainColorB - 50)));

        //set the labelBackground icon as backgroundIcon
        labelBackground.setIcon(backgroundIcon);
        panelMain.setBackground(new Color((mainColorR - 50), (mainColorG - 50), (mainColorB - 50)));
        panelMain.add(labelBackground);

        panelTop.setBackground(new Color(mainColorR, mainColorG, mainColorB));
        panelBottom.setBackground(new Color(mainColorR, mainColorG, mainColorB));

        //Set up Panel QuitTryAgain
        panelQuitTryAgain.setLayout(new GridLayout(1, 2, 2, 2));
        panelQuitTryAgain.add(buttonPlayAgain);
        panelQuitTryAgain.add(buttonQuit);

        //Add menu items to menu bar
        menuMain.add(menuMainMenu);
        menuMain.add(menuInstruction);
        menuMain.add(menuAbout);
        //Menu Bar is Complete
        menuMain.add(menuExit);

        //Add buttons to NewGame panel
        panelNewGame.setLayout(new GridLayout(2, 1, 40, 20));
        panelNewGame.add(buttonContinue);
        panelNewGame.add(buttonPvP);
        panelNewGame.add(buttonPvCPU);
        panelNewGame.add(buttonSetName);

        //Setting Button propertied
        buttonPlayAgain.setEnabled(false);
        buttonContinue.setEnabled(false);

        //Setting textMessage Properties
        textMessage.setBackground(new Color(mainColorR - 30, mainColorG - 30, mainColorB - 30));
        textMessage.setForeground(Color.white);
        textMessage.setEditable(false);

        //Adding Action Listener to all the Buttons and Menu Items
        menuMainMenu.addActionListener(this);
        menuExit.addActionListener(this);
        menuInstruction.addActionListener(this);
        menuAbout.addActionListener(this);
        buttonPvP.addActionListener(this);
        buttonPvCPU.addActionListener(this);
        buttonQuit.addActionListener(this);
        buttonSetName.addActionListener(this);
        buttonContinue.addActionListener(this);
        buttonPlayAgain.addActionListener(this);

        //Setting up the playing field
        panelPlayingField.setLayout(new GridLayout(3, 3, 2, 2));
        panelPlayingField.setBackground(Color.black);
        for (int i = 1; i <= 9; i++) {
            buttonEmpty[i] = new JButton();
            buttonEmpty[i].setBackground(new Color(buttonColorR, buttonColorG, buttonColorB));
            buttonEmpty[i].addActionListener(this);
            panelPlayingField.add(buttonEmpty[i]);// Playing Field is Compelte
        }

        //Add everything needed to panelMenu and panelMain
        panelMenu.add(menuMain);

        //make the default screen the menu attached to New Game menuItem
        setDefaultLayout();
        panelTop.setLayout(new GridLayout(1, 1, 20, 20));
        panelTop.add(panelNewGame);
        panelMain.add(panelTop);

        //Adding to window and Showing window
        window.add(panelMenu, BorderLayout.NORTH);
        window.add(panelMain, BorderLayout.CENTER);
        window.setVisible(true);
    }

    /*
-------------------------
Start of all methods
-------------------------
     */

 /*
 * Show the playing field
 * The game does not start anew: it simply shows what was before the method call
     */
    public void showGame() {
        clearPanelSouth();
        panelMain.remove(labelBackground);
        panelMain.setLayout(new BorderLayout());
        panelTop.setLayout(new BorderLayout());
        panelBottom.setLayout(new BorderLayout());
        panelTop.add(panelPlayingField);
        panelBottom.add(labelTurn, BorderLayout.WEST);
        panelBottom.add(labelStatus, BorderLayout.CENTER);
        panelBottom.add(panelQuitTryAgain, BorderLayout.EAST);
        panelMain.add(panelTop, BorderLayout.CENTER);
        panelMain.add(panelBottom, BorderLayout.SOUTH);
        panelPlayingField.requestFocus();
        inGame = true;
        checkTurn();
        checkWinStatus();
    }

    /*
 * Set all game required variables to default
 * then show the playing field
     */
    public void newPvPGame() {
        for (int x = 1; x < 10; x++) {
            buttonEmpty[x].setBackground(new Color(buttonColorR, buttonColorG, buttonColorB));
        }

        for (int i = 1; i < 10; i++) {
            buttonEmpty[i].setSize(100, 50);
            buttonEmpty[i].setIcon(iconMetalMesh);
            buttonEmpty[i].setEnabled(true);
        }
        turn = 1;
        win = false;
        showGame();
    }

    public void goBack() {
        clearPanelSouth();
        setDefaultLayout();
        panelTop.setLayout(new GridLayout(1, 1, 20, 20));
        panelTop.add(panelNewGame);
        panelMain.add(panelTop);
    }

    /*
 * Check the symbols for three of a kind in a row vertically, horizonatally, or diagonally
 * if the game is over, ask the player if the game should be restarted
     */
    public void checkWin() {
        for (int i = 0; i < 8; i++) {
            if (!buttonEmpty[winCombo[i][0]].getIcon().equals(iconMetalMesh)
                    && buttonEmpty[winCombo[i][0]].getIcon().equals(buttonEmpty[winCombo[i][1]].getIcon())
                    && //if(1 == 2 && 2 == 3)
                    buttonEmpty[winCombo[i][1]].getIcon().equals(buttonEmpty[winCombo[i][2]].getIcon())) {

                /*
        * How this checks:
        * First: it checks if the buttonEmpty[x] is not equal to an empty string (x being the array number
        * inside the multi-dementional array winCombo[checks inside each of the 7 sets][the first number])
        * Second: it checks if buttonEmpty[x] is equal to buttonEmpty[y] (x being winCombo[each set][the first number])
        * y being winCombo[each set the same as x][the second number] (checks if the first and
        * second number in each set is equal to each other)
        * Third: it checks if buttonEmtpy[y] is equal to buttonEmpty[z] (y being the same y as last time and z being
        * winCombo[each set as y][the third number])
        * Basically it checks if it is equal to the buttonEmpty is equal to each set of numbers
                 */
                win = true;
                winBox1 = winCombo[i][0];
                winBox2 = winCombo[i][1];
                winBox3 = winCombo[i][2];
                buttonEmpty[winBox1].setBackground(new Color(100, 255, 100));
                buttonEmpty[winBox2].setBackground(new Color(100, 255, 100));
                buttonEmpty[winBox3].setBackground(new Color(100, 255, 100));
            }

            /*
         * if there are multiple winning combos:
         * change the background of all the buttons in the combos to
         * the background displayed when a player wins
         * 
         * it is impossible to have more than
         * two winning combos in a single game;
         * only one additional for loop is required
             */
            if (win) {
                for (int ix = 0; ix < 8; ix++) {
                    if (!buttonEmpty[winCombo[ix][0]].getIcon().equals(iconMetalMesh)
                            && buttonEmpty[winCombo[ix][0]].getIcon().equals(buttonEmpty[winCombo[ix][1]].getIcon())
                            && //if(1 == 2 && 2 == 3)
                            buttonEmpty[winCombo[ix][1]].getIcon().equals(buttonEmpty[winCombo[ix][2]].getIcon())
                            && //do not identify the same winCombo previously found
                            (winBox1 != winCombo[ix][0]
                            || winBox2 != winCombo[ix][1]
                            || winBox3 != winCombo[ix][2])) {
                        buttonEmpty[winCombo[ix][0]].setBackground(new Color(100, 255, 100));
                        buttonEmpty[winCombo[ix][1]].setBackground(new Color(100, 255, 100));
                        buttonEmpty[winCombo[ix][2]].setBackground(new Color(100, 255, 100));
                    }
                }
            }
        }

        if (win || (!win && turn > 9)) {
            if (win) {
                if (turn % 2 == 0) {
                    message = Player1 + " won";
                    player1Score++;
                } else {
                    message = Player2 + " won";
                    player2Score++;
                }
                win = false;
            } else if (!win && turn > 9) {
                message = "  Draw";
            }
            showMessage(message);
            for (int i = 1; i <= 9; i++) {
                buttonEmpty[i].setEnabled(false);
            }
            buttonPlayAgain.setEnabled(true);
            checkWinStatus();
        } else {
            checkTurn();
        }
    }

    public void checkTurn() {
        if (!(turn % 2 == 0)) {
            whoTurn = Player1 + " [X]";
        } else {
            whoTurn = Player2 + " [O]";
        }
        labelTurn.setText("Turn: " + whoTurn);
    }

    public void checkWinStatus() {
        labelStatus.setText(Player1 + ": " + player1Score + " | " + Player2 + ": " + player2Score);
    }

    public void askUserForPlayerNames() {
        String playerName = Player2;

        playerName = getInput("Enter player 1 name [X]:", Player1);
        if (playerName == null) {
            //Do nothing
        } else if (playerName.equals("")) {
            do {
                showMessage("Invalid Player Name:" + "" + "\n" + "a" + "name must consist of at least one character");
                playerName = getInput("Enter player 1 name [X]:", Player1);
            } while (playerName.equals(""));
        } else if (playerName.equals(Player2)) {
            option = askMessage("Player 1 name matches Player 2's" + "\n" + "Do you want to continue?", "Name Match", JOptionPane.YES_NO_OPTION);
        }
        if (option == JOptionPane.YES_OPTION) {
            Player1 = playerName;
        } else if (playerName != null) {
            Player1 = playerName;
        }

        playerName = getInput("Enter player 2 name [O]:", Player2);
        if (playerName == null) {
            //Do Nothing
        } else if (playerName.equals("")) {
            do {
                showMessage("Invalid Player Name:" + "\n" + "a name must consist of at least one character");
                playerName = getInput("Enter player 2 name [O]:", Player1);
            } while (playerName.equals(""));
        } else if (playerName.equals(Player1)) {
            option = askMessage("Player names match" + "\n" + "Do you want to continue?", "Name Match", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                Player2 = playerName;
            } else if (playerName != null) {
                Player2 = playerName;
            }
        }
    }

    public void setDefaultLayout() {
        panelMain.setLayout(new GridLayout(2, 1, 2, 5));
        panelMain.add(labelBackground);
        panelTop.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelBottom.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    public int askMessage(String message, String title, int option) {
        return JOptionPane.showConfirmDialog(null, message, title, option);
    }

    public String getInput(String message, String setText) {
        return JOptionPane.showInputDialog(null, message, setText);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /*
 * Remove all possible panels that
 * panelMain, panelTop, panelBottom could have.
     */
    public void clearPanelSouth() {
        panelMain.remove(labelTitle);
        panelMain.remove(panelTop);
        panelMain.remove(panelBottom);
        panelTop.remove(panelNewGame);
        panelTop.remove(textMessage);
        panelTop.remove(panelPlayingField);
        panelBottom.remove(labelTurn);
        panelBottom.remove(panelQuitTryAgain);
    }

//-------------------------------------
//End of all non abstract methods
//-------------------------------------
    @Override
    public void actionPerformed(ActionEvent click) {
        Object source = click.getSource();
        for (int i = 1; i <= 9; i++) {
            if (source == buttonEmpty[i] && turn < 10) {
                buttonEmptyClicked = true;

                if (!(turn % 2 == 0)) {
                    buttonEmpty[i].setIcon(iconX);
                } else {
                    buttonEmpty[i].setIcon(iconO);
                }

                buttonEmpty[i].setEnabled(false);
                panelPlayingField.requestFocus();
                turn++;
            }
        }

        if (buttonEmptyClicked) {
            checkWin();
            buttonEmptyClicked = false;
        }

        if (source == menuMainMenu || source == menuInstruction || source == menuAbout) {
            clearPanelSouth();
            setDefaultLayout();

            //new game
            if (source == menuMainMenu) {
                panelTop.setLayout(new GridLayout(1, 1, 20, 20));
                panelTop.add(panelNewGame);
            } else if (source == menuInstruction || source == menuAbout) {
                if (source == menuInstruction) {// Instructions
                    message = "Instructions:" + "\n" + "" + "\n" + ""
                            + "Your goal is to be the first player to get 3 X's or O's in a" + "\n" + ""
                            + "row. (horizontally, diagonally, or vertically)" + "\n" + ""
                            + Player1 + ": X" + "\n" + ""
                            + Player2 + ": O" + "\n" + "";
                } else {
                    //About
                    message = "About:" + "\n" + "" + "\n" + ""
                            + "Title: Tic Tac Toe" + "\n" + "";
                }
                textMessage.setText(message);
                panelTop.add(textMessage);
            }
            panelMain.add(panelTop);
        } else if (source == buttonPvP || source == buttonPvCPU) {
            if (inGame) {
                option = askMessage("If you start a new game,"
                        + "your current game will be lost" + "" + "\n" + ""
                        + "Are you sure you want to continue?",
                        "Quit Game?", JOptionPane.YES_NO_OPTION
                );
                if (option == JOptionPane.YES_OPTION) {
                    inGame = false;
                }
            }
            if (!inGame) {
                if (source == buttonPvP) {// player vs player Game
                    buttonContinue.setEnabled(true);
                    newPvPGame();
                } else {// 1 v CPU Game
                    showMessage("Coming Soon!");
                }
            }
        } else if (source == buttonContinue) {
            checkTurn();
            showGame();
        } else if (source == buttonSetName) {
            askUserForPlayerNames();
        } else if (source == menuExit) {
            option = askMessage("Are you sure you want to exit?", "Exit Game", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else if (source == buttonPlayAgain) {
            newPvPGame();
            buttonPlayAgain.setEnabled(false);
        } else if (source == buttonQuit) {
            inGame = false;
            buttonContinue.setEnabled(false);
            goBack();
        }

        panelMain.setVisible(false);
        panelMain.setVisible(true);
    }
}
/* Bugs:
 * Scoreboard is not displayed
 * If player 2 wins, the player 2 name is not shown in the win message:
 *      the message always displays "player 2 wins"
 *      instead of the custom name entered by the user
 */

/* Future Plans:
 * AI
 */
