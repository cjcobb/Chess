/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** 
 * Game
 * Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	final static JFrame frame = new JFrame("CHESS!");
    public void run(){
        // NOTE : recall that the 'final' keyword notes inmutability
		  // even for local variables. 

        // Top-level frame in which game components live
		  // Be sure to change "TOP LEVEL FRAME" to the name of your game
        frame.setLocation(300,300);

		  // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("White's Move!");
        status_panel.add(status);

        // Main playing area
        /*final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);*/
        
        final ChessBoard board = new ChessBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset
        // button, we define it as an anonymous inner class that is 
        // an instance of ActionListener with its actionPerformed() 
        // method overridden. When the button is pressed,
        // actionPerformed() will be called.
        final JButton undo = new JButton("Undo Move");        
        final JButton newGame = new JButton("New Game");
        undo.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		board.undo();
        	}
        });
        newGame.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		board.setBoard();
        	}
        });
        final JButton AIMode = new JButton("AI Mode");
        
        AIMode.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		board.AIMode = !board.AIMode;
        	}
        });
        control_panel.add(undo);
        control_panel.add(newGame);
        control_panel.add(AIMode);
        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        JOptionPane.showMessageDialog(frame, 
        		"Hi! Welcome to CHESS! This is a standard game of chess, except \n"
        		+ "without castling and en passant, but there is pawn promotion! \n"
        		+ "The goal of chess it to put your opponent in checkmate. You \n"
        		+ "take turns moving one piece at a time. Checkmate is achieved \n"
        		+ "when a player has no possible moves that keep his king out of \n"
        		+ "danger. A brief summary of how the pieces move: \n"
        		+ "Pawns may move forward one space, except on their first move, \n"
        		+ "at which time they can move forward two spaces. They capture \n"
        		+ "other pieces by moving diagonal.\n"
        		+ "Rooks may move as far as they want forward, backward or to \n"
        		+ "either side. \n"
        		+ "Knights move in an L-shape (2 spaces in one direction, and \n"
        		+ "then one space in a perpendicular direction). They may not \n"
        		+ "move diagonal. Knights are the only pieces that can jump over \n"
        		+ "other pieces in their way. \n"
        		+ "Bishops can move as far as they want in any diagonal direction.\n"
        		+ "The queen may move as far as she wants in any single direction, \n"
        		+ "forward, backward, side to side or diaganol. \n"
        		+ "The king may move only one space in any direction. Your king \n"
        		+ "cannot get captured.\n"
        		+ "When a player moves into a position in which he can take the \n"
        		+ "other player's king, the player in danger is in check. If the \n"
        		+ "player in danger has no way to move out of check, he is in \n"
        		+ "checkmate, and he loses. The status bar at the bottom will \n"
        		+ "display who is currently moving, if a player is in check and \n"
        		+ "if checkmate has occurred.\n"
        		+ "Press New Game at any time to start over. White moves first.");
        // Start game
        //court.reset();
    }

    /*
     * Main method run to start and run the game
     * Initializes the GUI elements specified in Game and runs it
     * NOTE: Do NOT delete! You MUST include this in the final submission of your game.
     */
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Game());
    }
}
