import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;


	public class ChessPiece{
		public static boolean debug = false;
		public Team color;
		public Type type;
		BufferedImage img;
		public int row;
		public int col;
		public ChessPiece(int row,int col){
			type = Type.EMPTY;
			color = Team.EMPTY;
			this.row = row;
			this.col = col;
		}
		
		public ChessPiece(Type type, Team color,int row,int col) {
			this.color = color;
			this.type = type;
			this.row = row;
			this.col = col;
			try {
				switch(type) {
				case PAWN:
					if(color == Team.BLACK)
						img = ImageIO.read(new File("pawnblack.png"));
					else
						img = ImageIO.read(new File("pawnwhite.png"));
					break;
				case ROOK:
					if(color == Team.BLACK)
						img = ImageIO.read(new File("rookblack.png"));
					else
						img = ImageIO.read(new File("rookwhite.png"));
					break;
				case KNIGHT:
					if(color == Team.BLACK)
						img = ImageIO.read(new File("knightblack.png"));
					else
						img = ImageIO.read(new File("knightwhite.png"));
					break;
				case BISHOP:
					if(color == Team.BLACK)
						img = ImageIO.read(new File("bishopblack.png"));
					else
						img = ImageIO.read(new File("bishopwhite.png"));
					break;
				case QUEEN:
					if(color == Team.BLACK)
						img = ImageIO.read(new File("queenblack.png"));
					else
						img = ImageIO.read(new File("queenwhite.png"));
					break;
				case KING:
					if(color == Team.BLACK)
						img = ImageIO.read(new File("kingblack.png"));
					else
						img = ImageIO.read(new File("kingwhite.png"));
					break;
				default: // piece is empty
					img = null;
				}
			}
			catch (IOException e) {
				System.out.println("Error reading chess piece images");
				img = null;
			}
			
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof ChessPiece) {
				ChessPiece c = (ChessPiece) o;
				return (c.row == this.row && c.col == this.col && c.type == this.type && c.color == this.color);
			}
			return false;
		}
		
		public int value() {
			if (type == Type.PAWN)
				return 100;
			if (type == Type.ROOK)
				return 500;
			if (type == Type.KNIGHT)
				return 300;
			if (type == Type.BISHOP)
				return 350;
			if (type == Type.QUEEN)
				return 1000;
			if (type == Type.KING)
				return 10000;
			return 0;
		}
		//method to get moves
		public Set<ChessPiece> getPossibleMoves(ChessPiece[][] board) {
			Set<ChessPiece> moves = new HashSet<ChessPiece>();
			switch(type){
			case PAWN:
				//different move possibilities for different pawn colors
				if(color == Team.BLACK){
					if(row + 1 < 8) {
						if(board[row+1][col].type == Type.EMPTY)
							moves.add(board[row + 1][col]);
						if(this.row == 1 && board[row+2][col].type == Type.EMPTY)
							moves.add(board[row+2][col]);
						if(col + 1 < 8) {
							ChessPiece diagRight = board[row+1][col+1];
							if(diagRight.color != this.color && diagRight.type != Type.EMPTY)
								moves.add(diagRight);
						}
						if(col - 1 > -1) {
							ChessPiece diagLeft = board[row+1][col-1];
								if(diagLeft.color != this.color && diagLeft.type != Type.EMPTY)
									moves.add(diagLeft);
						}
					}
				}
				else {
					if(row - 1 > -1) {
						if(board[row-1][col].type == Type.EMPTY)
							moves.add(board[row - 1][col]);
						if(this.row == 6 && board[row-2][col].type == Type.EMPTY)
							moves.add(board[row-2][col]);
						if(col + 1 < 8){
							ChessPiece diagRight = board[row-1][col+1];
							if(diagRight.color != this.color && diagRight.type != Type.EMPTY)
								moves.add(diagRight);
						}
						if(col - 1 > -1){
							ChessPiece diagLeft = board[row-1][col-1];
							if(diagLeft.color != this.color && diagLeft.type != Type.EMPTY)
								moves.add(diagLeft);
						}
					}
				}
				break;
			case ROOK:
					//loops for moves in all four directions, that terminate when
					// running into another piece
					for(int i = this.row + 1; i < 8; i++) {
						ChessPiece current = board[i][this.col];
						if(current.type != Type.EMPTY){
							if(current.color != this.color)
								moves.add(current);
							break;
						}
						moves.add(current);
					}
					for(int i = this.row - 1; i > -1; i--) {
						ChessPiece current = board[i][this.col];
						if(current.type != Type.EMPTY){
							if(current.color != this.color)
								moves.add(current);
							break;
						}
						moves.add(current);
					}
					for(int i = this.col + 1; i < 8; i++) {
						ChessPiece current = board[this.row][i];
						if(current.type != Type.EMPTY){
							if(current.color != this.color)
								moves.add(current);
							break;
						}
						moves.add(current);
					}
					for(int i = this.col - 1; i > -1; i--) {
						ChessPiece current = board[this.row][i];
						if(current.type != Type.EMPTY){
							if(current.color != this.color)
								moves.add(current);
							break;
						}
						moves.add(current);
					}
				break;
			case KNIGHT:
				//check that move position is in bounds and 
				//that friendly piece is not occupying space
				if(row+2 < 8 && col+1 < 8 && board[row+2][col+1].color != this.color)
					moves.add(board[row+2][col+1]);
				if(row+2 < 8 && col-1 > -1 && board[row+2][col-1].color != this.color)
					moves.add(board[row+2][col-1]);
				if(row-2 > -1 && col+1 < 8 && board[row-2][col+1].color != this.color)
					moves.add(board[row-2][col+1]);
				if(row-2 > -1 && col-1 > -1 && board[row-2][col-1].color != this.color)
					moves.add(board[row-2][col-1]);
				if(row+1 < 8 && col+2 < 8 && board[row+1][col+2].color != this.color)
					moves.add(board[row+1][col+2]);
				if(row+1 < 8 && col-2 > -1 && board[row+1][col-2].color != this.color)
					moves.add(board[row+1][col-2]);
				if(row-1 > -1 && col+2 < 8 && board[row-1][col+2].color != this.color)
					moves.add(board[row-1][col+2]);
				if(row-1 > -1 && col-2 > -1 && board[row-1][col-2].color != this.color)
					moves.add(board[row-1][col-2]);
				break;
			case BISHOP:
				// consider moves down and to the right
				for (int i = 1; i <= row; i++) {
					if(col + i < 8){
						ChessPiece current = board[row-i][col+i];
						// if space is occupied, need to check type of piece
						if(current.type != Type.EMPTY){
							// if piece is friendly, break
							if(current.color == this.color) 
								break;
							//else add the spot as a move, and then break
							moves.add(current);
							break;
						}
						//just add the space if it isnt occupied
						moves.add(current);
					}
				}
				//moves down and to the left
				for (int i = 1; i <= row; i++) {
					if(col - i > -1) {	
						ChessPiece current = board[row-i][col-i];
						if(current.type != Type.EMPTY) {
							if(current.color == this.color)
								break;
							moves.add(current);
							break;
						}
						moves.add(current);
					}
				}
				//moves up and to the right
				for (int i = 1; i <= 7 - row; i++) {
					if(col + i < 8) {
						ChessPiece current = board[row + i][col + i];
						if(current.type != Type.EMPTY) {
							if(current.color == this.color)
								break;
							moves.add(current);
							break;
						}
						moves.add(current);
					}
				}
				//moves up and to the left
				for (int i = 1; i <= 7 - row; i++) {
					if(col - i > -1) {
						ChessPiece current = board[row + i][col - i];
						if(current.type != Type.EMPTY) {
							if(current.color == this.color)
								break;
							moves.add(current);
							break;
						}
						moves.add(current);
					}
				}
				break;
			case QUEEN:
				// queen moves, this is the rook's moves,
				// and the bishops moves
				for(int i = this.row + 1; i < 8; i++) {
					ChessPiece current = board[i][this.col];
					if(current.type != Type.EMPTY){
						if(current.color != this.color)
							moves.add(current);
						break;
					}
					moves.add(current);
				}
				for(int i = this.row - 1; i > -1; i--) {
					ChessPiece current = board[i][this.col];
					if(current.type != Type.EMPTY){
						if(current.color != this.color)
							moves.add(current);
						break;
					}
					moves.add(current);
				}
				for(int i = this.col + 1; i < 8; i++) {
					ChessPiece current = board[this.row][i];
					if(current.type != Type.EMPTY){
						if(current.color != this.color)
							moves.add(current);
						break;
					}
					moves.add(current);
				}
				for(int i = this.col - 1; i > -1; i--) {
					ChessPiece current = board[this.row][i];
					if(debug)
						System.out.println(board[this.row][i].type);
					if(current.type != Type.EMPTY){
						if(current.color != this.color)
							moves.add(current);
						break;
					}
					if(debug)
						System.out.println("adding improper move");
					moves.add(current);
				}
				debug = false;
				for (int i = 1; i <= row; i++) {
					if(col + i < 8){
						ChessPiece current = board[row-i][col+i];
						// if space is occupied, need to check type of piece
						if(current.type != Type.EMPTY){
							// if piece is friendly, break
							if(current.color == this.color) 
								break;
							//else add the spot as a move, and then break
							moves.add(current);
							break;
						}
						//just add the space if it isnt occupied
						moves.add(current);
					}
				}
				//moves down and to the left
				for (int i = 1; i <= row; i++) {
					if(col - i > -1) {	
						ChessPiece current = board[row-i][col-i];
						if(current.type != Type.EMPTY) {
							if(current.color == this.color)
								break;
							moves.add(current);
							break;
						}
						moves.add(current);
					}
				}
				//moves up and to the right
				for (int i = 1; i <= 7 - row; i++) {
					if(col + i < 8) {
						ChessPiece current = board[row + i][col + i];
						if(current.type != Type.EMPTY) {
							if(current.color == this.color)
								break;
							moves.add(current);
							break;
						}
						moves.add(current);
					}
				}
				//moves up and to the left
				for (int i = 1; i <= 7 - row; i++) {
					if(col - i > -1) {
						ChessPiece current = board[row + i][col - i];
						if(current.type != Type.EMPTY) {
							if(current.color == this.color)
								break;
							moves.add(current);
							break;
						}
						moves.add(current);
					}
				}
				break;
			case KING:
				if(row+1 < 8 && board[row+1][col].color != this.color)
					moves.add(board[row+1][col]);
				if(row-1 > -1 && board[row-1][col].color != this.color)
					moves.add(board[row-1][col]);
				if(col+1 < 8 && board[row][col+1].color != this.color)
					moves.add(board[row][col+1]);
				if(col-1 > -1 && board[row][col-1].color != this.color)
					moves.add(board[row][col-1]);
				if(row+1 < 8 && col+1 < 8 && board[row+1][col+1].color != this.color)
					moves.add(board[row+1][col+1]);
				if(row+1 < 8 && col-1 > -1 && board[row+1][col-1].color != this.color)
					moves.add(board[row+1][col-1]);
				if(row-1 > -1 && col+1 < 8 && board[row-1][col+1].color != this.color)
					moves.add(board[row-1][col+1]);
				if(row-1 > -1 && col-1 > -1 && board[row-1][col-1].color != this.color)
					moves.add(board[row-1][col-1]);
				break;
			default:
				break;
			}
			/*if(this.color == Team.BLACK) {
				System.out.println(this.type);
			for(ChessPiece p : moves)
					System.out.println(board[p.row][p.col].type + " " + board[p.row][p.col].color);
			}*/
			return moves;
		}
		
		
		//method to filter out moves that put oneself in check
		public Set<ChessPiece> filterMoves(Set<ChessPiece> possibleMoves, ChessPiece[][] board) {
			ChessPiece[][] tempBoard = new ChessPiece[8][8];
			Set<ChessPiece> checkedMoves = new HashSet<ChessPiece>();
			//copy the board over piece by piece, to eliminate aliasing
			//also, find the coordinates of the selected piece
			for(int i = 0; i < 8; i++) {
				for(int j = 0; j < 8; j++) {
					tempBoard[i][j] = board[i][j];
					if(board[i][j] == this) {
						tempBoard[i][j] = new ChessPiece(i,j);
					}
				}
			}
			
			for(ChessPiece move : possibleMoves) {
				int rowMove = -1;
				int colMove = -1;
				for(int i = 0; i < 8; i++) {
					for(int j = 0; j < 8; j++) {
						if(tempBoard[i][j] == move) {
							rowMove = i;
							colMove = j;
							i = 8; //to break from outerloop
							break;
						}
					}
				}
				ChessPiece previousPiece = tempBoard[rowMove][colMove];
				tempBoard[rowMove][colMove] = this;
				if(!ChessBoard.inCheck(this.color, tempBoard)) {
					checkedMoves.add(move);
				}
				tempBoard[rowMove][colMove] = previousPiece;
			}
			
			return checkedMoves;
		}
	}
