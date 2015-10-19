package ai;

/*
 * In this class all the functions of a computer Player are defined .
 * 
 * 
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import logic.CamelotGame;
import logic.IPlayerHandler;
import logic.ThisMove;
import logic.MoveValidator;
import logic.Piece;
import mini_camelot.MainMenu;

public class AiPlayer implements IPlayerHandler {

	private CamelotGame camelotGame;
	private MoveValidator validator;
	  boolean Cutoff = false;
    int max_prunning = 0;
    int min_prunning = 0;
    ThisMove bestMove;
	
	/**
	 * number of moves to look into the future
	 */
	public int maxDepth;


	public AiPlayer(CamelotGame camelotGame) {
		this.camelotGame = camelotGame;
		this.validator = this.camelotGame.getMoveValidator();
		}

	/**
	 * get best move for current game situation
	 * @return a valid ThisMove instance
	 */

	public ThisMove getMove() {
		//return getBestMove();
        ThisMove bestOne= alpha_beta_fullsearch();
        System.out.println("bestMove= "+bestOne);
        return bestOne;
	}

	/*
	 * This move successfullyexecuted fucntion makes sure the Gui piece is in its place and its executed successfully 
	 * if it  is not executed successfully it will throw and exception 
	 */
		@Override
	public void moveSuccessfullyExecuted(ThisMove thisMove) {
		// we are using the same camelotGame instance, so no need to do anything here.
		System.out.println("executed: "+thisMove);
	}

	
	/*
	 * This is the most famous alpha beta search which searches the tree and prunes the branches which are not needed.
	 */
	private ThisMove alpha_beta_fullsearch(){
        min_prunning = 0;
        max_prunning = 0;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        Date sysdate = new Date();
       // System.out.println("before max, in ab");
        int v = MAX_VALUE(maxDepth, sysdate, alpha, beta);
        System.out.println(v);
        System.out.println("max_prunning= "+max_prunning);
        System.out.println("min_prunning= "+min_prunning);
        System.out.println("Total nodes Reached == "+ max_prunning+min_prunning);
        return bestMove;
    }

    private boolean depthCutOff(int depth, Date sysdate){
        if (this.camelotGame.getGameState()==CamelotGame.GAME_STATE_END_BLACK_WON)
            return true;
        Date date = new Date();
//        System.out.println(date.compareTo(sysdate));
        if (depth<=0||date.compareTo(sysdate)>10) {
            Cutoff = true;
            //System.out.println("terminal");
            return true;
        } else return false;

    }

    private int MAX_VALUE(int Depth, Date sysdate, int alpha, int beta) {
        // Cutoff-test
        // if ()
        if (depthCutOff(Depth,sysdate)){
        	//System.out.println("in max terminal condition");
        	return evaluateState();
        
        }
        int v = Integer.MIN_VALUE;
//        if (Depth<=0){
//            return evaluateState();
//        }
//        camelotGame.availableNodes
        List<ThisMove> validMoves = returnPossibleMoves(false);
        for (ThisMove thisMove: validMoves){
//            System.out.println("MAX_move= "+move);
        	//System.out.println("in max condition, before executemove");
            executeMove(thisMove);
            //System.out.println("in max condition, after executemove, before min");
            //            v = evaluateState();
            int temp = MIN_VALUE(Depth-1, sysdate, alpha, beta);
            //System.out.println("in max condition, before undo");
            undoMove(thisMove);
            //System.out.println("in max condition, after undo");
            if (temp>v) {
                bestMove = thisMove;
                v = temp;
            }
            if (v>=beta) {
                max_prunning++;
                bestMove = thisMove;
                return v;
            }
            alpha = Math.max(v,alpha);
        }
        //System.out.println("end of max");
        return v;
    }
    private int MIN_VALUE(int Depth, Date sysdate, int alpha, int beta){
        if (depthCutOff(Depth,sysdate)){
        	//System.out.println("in min terminal condition");
        	return evaluateState();
            }
            int v = Integer.MAX_VALUE;
        List<ThisMove> validMoves = returnPossibleMoves(false);
        for (ThisMove thisMove : validMoves){
        	//System.out.println("in min condition, before executemove");
            executeMove(thisMove);
            //System.out.println("in min condition, after executemove, before max");
//            v = evaluateState();
            v = Math.min(v, MAX_VALUE(Depth - 1, sysdate, alpha, beta));
            //System.out.println("in min condition, before undo");
            undoMove(thisMove);
            //System.out.println("in min condition, after undo");
            if (v<=alpha) {
                min_prunning++;
                return v;
            }
            beta = Math.min(beta,v);
//            if (alpha>=beta) {
//                System.out.println("pruning in Min");
//                break;
//            }
        }
        //System.out.println("end of min");
        return v;
    }
	
	
	
	
	
	/**
	 * undo specified move
	 * UNDO MOVE function undoes all the moves that needed to be and this will also reverse the game state and put it as the same
	 * state before the move happened
	 */
	private void undoMove(ThisMove thisMove) {
		//System.out.println("undoing move");
		this.camelotGame.undoMove(thisMove);
		//state.changeGameState();
		this.camelotGame.changeGameState();
	}

	/**
	 * Execute specified move. This will also change the game state after the
	 * move has been executed.
	 */
	private void executeMove(ThisMove thisMove) {
		//System.out.println("executing move");
//		System.out.println(move);
		this.camelotGame.movePiece(thisMove);
		this.camelotGame.changeGameState();
	}
	
	
	/**
	* generate all possible/valid moves for the specified game
	* @param state - game state for which the moves should be generated
	* @return list of all possible/valid moves
	*/
	private List<ThisMove> returnPossibleMoves(boolean debug) {

		List<Piece> pieces = this.camelotGame.getPieces();
		List<ThisMove> validMoves = new ArrayList<ThisMove>();
		ThisMove testMove = new ThisMove(0,0,0,0);
		
		int pieceColor = (this.camelotGame.getGameState()==CamelotGame.GAME_STATE_WHITE
			?Piece.COLOR_WHITE
			:Piece.COLOR_BLACK);

		// iterate over all non-captured pieces
		for (Piece piece : pieces) {

			// only look at pieces of current players color
			if (pieceColor == piece.getColor()) {
				// start generating move
				testMove.sRow = piece.getRow();
				testMove.sColumn = piece.getColumn();

				// iterate over all board rows and columns
				for (int targetRow = Piece.ROW_1; targetRow <= Piece.ROW_14; targetRow++) {
					for (int targetColumn = Piece.COLUMN_A; targetColumn <= Piece.COLUMN_H; targetColumn++) {

						// finish generating move
						testMove.tRow = targetRow;
						testMove.tColumn = targetColumn;

						if(debug) System.out.println("testing move: "+testMove);
						
						// check if generated move is valid
						if (this.validator.isMoveValid(testMove, true)) {
							// valid move
							validMoves.add(testMove.clone());
						} else {
							// generated move is invalid, so we skip it
						}
					}
				}

			}
		}
		
		return validMoves;
		
	}

	/**
	 * evaluate the current game state from the view of the
	 * current player. High numbers indicate a better situation for
	 * the current player.
	 *
	 * @return integer score of current game state
	 */
	private int evaluateState() {

		// add up score
		//
		int scoreWhite = 0;
		int scoreBlack = 0;
		if(MainMenu.levl==2||MainMenu.levl==4){
		for (Piece piece : this.camelotGame.getPieces()) {
			if(piece.getColor() == Piece.COLOR_BLACK){
				scoreBlack +=
					getScoreForPieceType(piece.getType());
				scoreBlack +=
					getScoreForPiecePositionWhite(piece.getRow(),piece.getColumn());
			}else if( piece.getColor() == Piece.COLOR_WHITE){
				scoreWhite +=
					getScoreForPieceType(piece.getType());
				scoreWhite +=
					getScoreForPiecePositionBlack(piece.getRow(),piece.getColumn());
			}else{
				throw new IllegalStateException(
						"unknown piece color found: "+piece.getColor());
			}
		}
		}
		else
		{
			for (Piece piece : this.camelotGame.getPieces()) {
				if(piece.getColor() == Piece.COLOR_BLACK){
					scoreBlack +=
						getScoreForPieceType(piece.getType());
					scoreBlack +=
						getScoreForPiecePositionBlack(piece.getRow(),piece.getColumn());
				}else if( piece.getColor() == Piece.COLOR_WHITE){
					scoreWhite +=
						getScoreForPieceType(piece.getType());
					scoreWhite +=
						getScoreForPiecePositionWhite(piece.getRow(),piece.getColumn());
				}else{
					throw new IllegalStateException(
							"unknown piece color found: "+piece.getColor());
				}
			}	
		}
		// return evaluation result depending on who's turn it is
		int gameState = this.camelotGame.getGameState();
		
		if( gameState == CamelotGame.GAME_STATE_BLACK){
			return scoreBlack - scoreWhite;
		
		}else if(gameState == CamelotGame.GAME_STATE_WHITE){
			return scoreWhite - scoreBlack;
		
		}else if(gameState == CamelotGame.GAME_STATE_END_WHITE_WON
				|| gameState == CamelotGame.GAME_STATE_END_BLACK_WON){
			return Integer.MIN_VALUE + 1;
		
		}else{
			throw new IllegalStateException("unknown game state: "+gameState);
		}
	}
	
	/**This function calculates the score for the position of the piece , depending on the score the move of the  the best move is choosen
	 *This function is called in evaluate state function.
	 * get the evaluation bonus for the specified position
	 * @param row - one of Piece.ROW_..
	 * @param column - one of Piece.COLUMN_..
	 * @return integer score
	 */
	
	
	private int getScoreForPiecePositionWhite(int row, int column) {
		byte[][] positionWeight =
			{ {0, 0, 0, 121, 121, 0, 0, 0}
            , {0, 0, 70, 70, 70, 70, 0, 0}
            , {0, 60, 60, 60, 60, 60, 60, 0}
            , {50, 50, 50, 50, 50, 50, 50, 50}
            , {45, 45, 45, 45, 45, 45, 45, 45}
            , {40, 40, 40, 40, 40, 40, 40, 40}
            , {35, 35, 35, 35, 35, 35, 35, 35}
            , {30, 30, 30, 30, 30, 30, 30, 30}
            , {25, 25, 25, 25, 25, 25, 25, 25}
            , {20, 20, 20, 20, 20, 20, 20, 20}
            , {15, 15, 15, 15, 15, 15, 15, 15}
            , {0, 10, 10, 10, 10, 10, 10, 0}
            , {0, 0, 5, 5, 5, 5, 0, 0}
            , {0, 0, 0, 0, 0, 0, 0, 0}
			};
		return positionWeight[row][column];
	}
	
	private int getScoreForPiecePositionBlack(int row, int column) {
		byte[][] positionWeight =
			{ {0, 0, 0, 0, 0, 0, 0, 0}
            , {0, 0, 5, 5, 5, 5, 0, 0}
            , {0, 10, 10, 10, 10, 10, 10, 0}
            , {15, 15, 15, 15, 15, 15, 15, 15}
            , {20, 20, 20, 20, 20, 20, 20, 20}
            , {25, 25, 25, 25, 25, 25, 25, 25}
            , {30, 30, 30, 30, 30, 30, 30, 30}
            , {35, 35, 35, 35, 35, 35, 35, 35}
            , {40, 40, 40, 40, 40, 40, 40, 40}
            , {45, 45, 45, 45, 45, 45, 45, 45}
            , {50, 50, 50, 50, 50, 50, 50, 50}
            , {0, 60, 60, 60, 60, 60, 60, 0}
            , {0, 0, 70, 70, 70, 70, 0, 0}
            , {0, 0, 0, 121, 121, 0, 0, 0}
			};
		return positionWeight[row][column];
	}
	
	
	
	/**
	 * get the evaluation score for the specified piece type
	 * @param type - one of Piece.TYPE_..
	 * @return integer score
	 */
	private int getScoreForPieceType(int type){
		switch (type) {
		
			case Piece.TYPE_PAWN: return 1;
			
			default: throw new IllegalArgumentException("unknown piece type: "+type);
		}
	}

	public static void main(String[] args) {
		
		
	}
}
