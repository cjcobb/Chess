import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class ChessBoard extends JPanel {
	
	//Two teams
	/*public enum Team{
		EMPTY,
		WHITE,
		BLACK;
	}*/
	//All types of pieces
/*	public enum Type{
		EMPTY,
		PAWN,
		ROOK,
		KNIGHT,
		BISHOP,
		QUEEN,
		KING;
	}*/
	//class that embodies particular piece

	
	
	// this is how we keep track of the board. Every space on the board has
	// either one chesspiece on it, or it is empty. 
	static ChessPiece[][] board = new ChessPiece[8][8];
	ChessPiece clickedPiece;
	Team whoseMove;
	Set<ChessPiece> possibleMoves = new HashSet<ChessPiece>();
	LinkedList<ChessPiece[][]> previousBoards = new LinkedList<ChessPiece[][]>();
	final JLabel status;
	boolean AIMode = false;
	public ChessBoard(JLabel s) {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.setPreferredSize(new Dimension(65*8,65*8));
		this.status = s;
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me){
				
				int x_pos = me.getX();
				int y_pos = me.getY();
				
				int col = x_pos/65;
				int row = y_pos/65;
				// determine possible moves
				if(clickedPiece == null) {
					if(board[row][col].color == whoseMove) {
					clickedPiece = board[row][col];
					possibleMoves = clickedPiece.getPossibleMoves(board);
					possibleMoves = clickedPiece.filterMoves(possibleMoves, board);
					}
				}
				else {
				if(board[row][col].color == clickedPiece.color) {
					clickedPiece = board[row][col];
					possibleMoves = clickedPiece.getPossibleMoves(board);
					possibleMoves = clickedPiece.filterMoves(possibleMoves, board);
				}
				//move piece
				if((board[row][col].type == Type.EMPTY || 
						board[row][col].color != clickedPiece.color) && 
						possibleMoves.contains(board[row][col])) {
					ChessPiece[][] oldBoard = new ChessPiece[8][8];
					for(int i = 0; i < 8; i++) {
						for(int j = 0; j < 8; j++) {
							oldBoard[i][j] = board[i][j];
						}
					}
					
					previousBoards.add(oldBoard);
					board[row][col] = clickedPiece;
					board[clickedPiece.row][clickedPiece.col] = 
							new ChessPiece(clickedPiece.row,clickedPiece.col);
					clickedPiece.row = row;
					clickedPiece.col = col;
					if(clickedPiece.type == Type.PAWN && (row == 0 || row == 7)) {
						board[row][col]= pawnPromotion(clickedPiece);
					}
					clickedPiece = null;
					possibleMoves = new HashSet<ChessPiece>();
					
					if(AIMode) {
						repaint();
						status.setText("Computer is thinking");
						ChessPiece[][] copy_board = new ChessPiece[8][8];
						for(int i = 0; i < 8; i++) {
							for(int j = 0; j < 8; j++) {
								copy_board[i][j] = board[i][j];
							}
						}
						
						AI.AImove(copy_board,2);
						//System.out.println("out of negamax");
						status.setText("Your move!");
					}
					else
						setStatus();
				}
				}
				repaint();
			}
		});
		
		setBoard();
		
	}	
	//set the status
	public void setStatus() {
		if(whoseMove == Team.WHITE) {
			whoseMove = Team.BLACK;
			if(inCheck(whoseMove,board)) {
				if(checkmate(whoseMove,board))
					status.setText("Checkmate! White wins");
				else
					status.setText("Check! Black's move");
			}
			else 
					status.setText("Black's move");
		}
		else {
			whoseMove = Team.WHITE;
			if(inCheck(whoseMove,board)) {
				if(checkmate(whoseMove,board))
					status.setText("Checkmate! Black wins");
				else
					status.setText("Check! White's move");
			}
			else 
					status.setText("White's move");
		}
	}
	// set the initial board, with all the pieces in their correct spots
	public void setBoard() {
		for (int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				switch(i) {
				case 0:
					if(j == 0 || j == 7)
						board[i][j] = new ChessPiece(Type.ROOK,Team.BLACK,i,j);
					if(j == 1 || j == 6)
						board[i][j] = new ChessPiece(Type.KNIGHT,Team.BLACK,i,j);
					if(j == 2 || j == 5)
						board[i][j] = new ChessPiece(Type.BISHOP,Team.BLACK,i,j);
					if(j == 3)
						board[i][j] = new ChessPiece(Type.QUEEN,Team.BLACK,i,j);
					if(j == 4)
						board[i][j] = new ChessPiece(Type.KING,Team.BLACK,i,j);
					break;
 				case 1:
					board[i][j] = new ChessPiece(Type.PAWN,Team.BLACK,i,j);
					break;
				case 6:	
					board[i][j] = new ChessPiece(Type.PAWN,Team.WHITE,i,j);
					break;
				case 7:
					if(j == 0 || j == 7)
						board[i][j] = new ChessPiece(Type.ROOK,Team.WHITE,i,j);
					if(j == 1 || j == 6)
						board[i][j] = new ChessPiece(Type.KNIGHT,Team.WHITE,i,j);
					if(j == 2 || j == 5)
						board[i][j] = new ChessPiece(Type.BISHOP,Team.WHITE,i,j);
					if(j == 3)
						board[i][j] = new ChessPiece(Type.QUEEN,Team.WHITE,i,j);
					if(j == 4)
						board[i][j] = new ChessPiece(Type.KING,Team.WHITE,i,j);
					break;
				default:
					board[i][j] = new ChessPiece(i,j);
				}
			}
		}
		whoseMove = Team.WHITE;
		clickedPiece = null;
		possibleMoves = new HashSet<ChessPiece>();
		status.setText("White's move");
		repaint();
	}
	
	public static void move(ChessPiece to_be_moved, int row, int col) {
		//System.out.println(to_be_moved.type + " " + row + " " + col);
		if(to_be_moved == null){
			//status.setText("Checkmate! You win")
		}
		board[to_be_moved.row][to_be_moved.col] = new ChessPiece(to_be_moved.row,to_be_moved.col);
		board[row][col] = to_be_moved;
		to_be_moved.row = row;
		to_be_moved.col = col;
	}
	
	public void undo() {	
		if(previousBoards.size() == 0)
			return;
		board = previousBoards.removeLast();
		for (int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				board[i][j].row = i;
				board[i][j].col = j;
			}
		}
		clickedPiece = null;
		possibleMoves = new HashSet<ChessPiece>();
		setStatus();
		repaint();
	}
	
	public ChessPiece pawnPromotion(ChessPiece piece) {
		Object[] choices = {"Queen","Rook","Knight","Bishop"};
		String choice = (String) JOptionPane.showInputDialog(Game.frame, "You have the option to promote your pawn! \n"
				+ "Pick which piece you would like to change into:","PAWN PROMOTION",
				JOptionPane.INFORMATION_MESSAGE,null,choices,"Queen");
		
		if (choice.equals("Queen"))
			return new ChessPiece(Type.QUEEN,piece.color,piece.row,piece.col);
		if (choice.equals("Rook"))
			return new ChessPiece(Type.ROOK,piece.color,piece.row,piece.col);
		if (choice.equals("Knight"))
			return new ChessPiece(Type.KNIGHT,piece.color,piece.row,piece.col);
		if (choice.equals("Bishop"))
			return new ChessPiece(Type.BISHOP,piece.color,piece.row,piece.col);
		return piece;
	}
	
	//method to check if team is in check on a particular board
	public static boolean inCheck(Team thisTeamColor,ChessPiece[][] tempBoard) {
		ChessPiece king = null;
		Set<ChessPiece> allOpposingMoves = new HashSet<ChessPiece>();
		for(int i = 0; i < 8 && king == null; i++) {
			for(int j = 0; j < 8 && king == null; j++) {
				if(tempBoard[i][j].type == Type.KING && tempBoard[i][j].color == thisTeamColor) {
					king = tempBoard[i][j];
				}		
			}
		}
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(tempBoard[i][j].color != thisTeamColor)
				allOpposingMoves.addAll(tempBoard[i][j].getPossibleMoves(tempBoard));
			}
		}
		
		if(allOpposingMoves.contains(king))
			return true;
		else
			return false;
	}
	
	//method to check for checkmate
	public boolean checkmate(Team thisTeamColor,ChessPiece[][] tempBoard) {
		Set<ChessPiece> allMoves = new HashSet<ChessPiece>();
		
		for(int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if(tempBoard[i][j].color == thisTeamColor) {
					allMoves.addAll(tempBoard[i][j].filterMoves(tempBoard[i][j].getPossibleMoves(tempBoard), board));
				}
			}
		}
		if(allMoves.size() == 0) 
			return true;
		else
			return false;
	}
	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//draw board
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if((j + i) % 2 == 0)
					g.setColor(new Color(218,165,32));
				else
					g.setColor(new Color(238,232,170));
				if(board[i][j] == clickedPiece)
					g.setColor(Color.YELLOW);
				if(inCheck(board[i][j].color,board) && board[i][j].type == Type.KING)
					g.setColor(Color.RED);
				g.fillRect(65 * j, 65 * i, 65, 65);
				g.drawImage(board[i][j].img, 65*j, 65*i, 65, 65, null);
				if(possibleMoves.contains(board[i][j])){
					g.setColor(Color.RED);
					g.fillOval(65*j + 25,65*i + 25,15,15);
				}
			}
		}

		
		
	}
}
