import java.util.Set;


public class AI {
	
	//need to check evaluation function, ai seems to be making dumb moves
	
	private static int root_depth;
	
	public static void AImove(ChessPiece[][] board,int depth) {
		root_depth = depth;
		System.out.println(evaluate(board));
		max(depth, Integer.MIN_VALUE, Integer.MAX_VALUE, board);
	}
	
	public static int evaluate(ChessPiece[][] board) {
		int eval = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j].color == Team.BLACK)
					eval += board[i][j].value();
				else {
					if(board[i][j].type != Type.EMPTY)
						eval += (-1 * board[i][j].value());
				}
			}
		}
		return eval;
	}

	public static int evaluate(Team color, ChessPiece[][] board) {
		int eval = 0;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j].color == color)
					eval += board[i][j].value();
				else {
					if(board[i][j].type != Type.EMPTY)
						eval += -1 * board[i][j].value();
				}
			}
		}
		return eval;
	}
	
	public static int max(int depth, int alpha, int beta, ChessPiece[][] board) {
		if (depth == 0) return evaluate(board);
		
		System.out.println(root_depth);
		int max = Integer.MIN_VALUE;
		Set<ChessPiece> moves;
		ChessPiece selected;
		ChessPiece old_piece;
		ChessPiece to_be_moved = null;
		int score;
		int move_row = -1;
		int move_col = -1;
		
		
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				selected = board[i][j];
				if(selected.color == Team.BLACK) {
					moves = selected.filterMoves(selected.getPossibleMoves(board), board);
					//System.out.println(board[i][j].type);
					for(ChessPiece p : moves) {
						//make move
						old_piece = board[p.row][p.col];
						board[p.row][p.col] = selected;
						board[i][j] = new ChessPiece(i,j);
						selected.row = p.row;
						selected.col = p.col;
						
						score = min(depth - 1, alpha, beta, board);
						
						if (score > alpha) {
							alpha = score;
							if(depth == root_depth) {
								to_be_moved = selected;
								move_row = p.row;
								move_col = p.col;
							}		
						}
						//unmake move
						board[p.row][p.col] = old_piece;
						board[i][j] = selected;
						selected.row = i;
						selected.col = j;
						
						System.out.println(board[i][j].type + " " + i + " " + j + " " + p.row + " " + p.col + " " + score);
						if(score >= beta)
							return score;
					}
				}
			}
		}
		if(depth == root_depth) {
			//System.out.println("making move");
			ChessBoard.move(to_be_moved, move_row, move_col);
		}
		return alpha;
	}
	
	public static int min(int depth, int alpha, int beta, ChessPiece[][] board) {
		if (depth == 0) return evaluate(board);
		int min = Integer.MAX_VALUE;
		Set<ChessPiece> moves;
		ChessPiece selected;
		ChessPiece old_piece;
		int score;
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				selected = board[i][j];
				if(selected.color == Team.WHITE) {
					moves = selected.filterMoves(selected.getPossibleMoves(board), board);
					
					for(ChessPiece p : moves) {
						//make move
						old_piece = board[p.row][p.col];
						board[p.row][p.col] = selected;
						board[i][j] = new ChessPiece(i,j);
						selected.row = p.row;
						selected.col = p.col;
						
						score = min(depth - 1, alpha, beta, board);
						if (score < beta)
							beta = score;
						
						
						//unmake move
						board[p.row][p.col] = old_piece;
						board[i][j] = selected;
						selected.row = i;
						selected.col = j;
						
						if (score <= alpha)
							return score;
					}
				}
			}
		}
		return beta;
	}
 	
	public static int negaMax(Team color, ChessPiece[][] board, int depth, boolean root, int alpha, int beta) {
		if (depth == 0)
			return evaluate(color, board);
		int max = Integer.MIN_VALUE;
		ChessPiece to_be_moved = null;
		int final_row = -1;
		int final_col =-1;
		

		//first, scan the board for pieces of desired color
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				Set<ChessPiece> moves;
				ChessPiece selected = board[i][j];
				
				if(selected.color == color) {
					moves = selected.filterMoves(selected.getPossibleMoves(board),board);

					for(ChessPiece p : moves) {

						int score;
						int old_row = selected.row;
						int old_col = selected.col;
						board[p.row][p.col] = selected;
						board[selected.row][selected.col] = new ChessPiece(selected.row,selected.col);
						selected.row = p.row;
						selected.col = p.col;
						if(color == Team.BLACK)
							score = -negaMax(Team.WHITE, board, depth - 1,false, -beta, -alpha);
						else
							score = -negaMax(Team.BLACK, board, depth - 1,false, -beta, -alpha);
						
						board[p.row][p.col] = p;
						board[old_row][old_col] = selected;
						selected.col = old_col;
						selected.row = old_row;
						if(score >= beta)
							return beta;
						if(score > alpha) {
							alpha = score;
							if(root) {
								to_be_moved = selected;
								final_row = p.row;
								final_col = p.col;
							}
						}

					}
				}
			}
		}
		if(root)
			ChessBoard.move(to_be_moved,final_row,final_col);
		return alpha;
	}
	

}
