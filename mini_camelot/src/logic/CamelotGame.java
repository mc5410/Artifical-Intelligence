package logic;

import java.util.ArrayList;
import java.util.List;

import console.ConsoleCamelot;

public class CamelotGame implements Runnable{

	public int gameState = GAME_STATE_WHITE;
	public static final int GAME_STATE_WHITE = 0;
	public static final int GAME_STATE_BLACK = 1;
	public static final int GAME_STATE_END_BLACK_WON = 2;
	public static final int GAME_STATE_END_WHITE_WON = 3;


	public List<Piece> pieces = new ArrayList<Piece>();
	private List<Piece> capturedPieces = new ArrayList<Piece>();

	private MoveValidator moveValidator;
	private IPlayerHandler blackPlayerHandler;
	private IPlayerHandler whitePlayerHandler;
	private IPlayerHandler activePlayerHandler;

	/**
	 * initialize game
	 */
	public CamelotGame() {

		this.moveValidator = new MoveValidator(this);

		// create and place pieces
		
		createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_PAWN, Piece.ROW_5, Piece.COLUMN_C);
		createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_PAWN, Piece.ROW_5, Piece.COLUMN_D);
		createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_PAWN, Piece.ROW_5, Piece.COLUMN_E);
		createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_PAWN, Piece.ROW_5, Piece.COLUMN_F);
		createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_PAWN, Piece.ROW_6, Piece.COLUMN_D);
		createAndAddPiece(Piece.COLOR_WHITE, Piece.TYPE_PAWN, Piece.ROW_6, Piece.COLUMN_E);
		

		
		createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_PAWN, Piece.ROW_10, Piece.COLUMN_F);
		createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_PAWN, Piece.ROW_10, Piece.COLUMN_C);
		createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_PAWN, Piece.ROW_10, Piece.COLUMN_D);
		createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_PAWN, Piece.ROW_10, Piece.COLUMN_E);
		createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_PAWN, Piece.ROW_9, Piece.COLUMN_D);
		createAndAddPiece(Piece.COLOR_BLACK, Piece.TYPE_PAWN, Piece.ROW_9, Piece.COLUMN_E);
	}
	
	/**
	 * set the client/player for the specified piece color
	 * @param pieceColor - color the client/player represents 
	 * @param playerHandler - the client/player
	 */
	public void setPlayer(int pieceColor, IPlayerHandler playerHandler){
		switch (pieceColor) {
			case Piece.COLOR_BLACK: this.blackPlayerHandler = playerHandler; break;
			case Piece.COLOR_WHITE: this.whitePlayerHandler = playerHandler; break;
			default: throw new IllegalArgumentException("Invalid pieceColor: "+pieceColor);
		}
	}

	/**
	 * start main game flow
	 */
	public void startGame(){
		// check if all players are ready
		System.out.println("CamelotGame: waiting for players");
		while (this.blackPlayerHandler == null || this.whitePlayerHandler == null){
			// players are still missing
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		}
		
		// set start player
		this.activePlayerHandler = this.whitePlayerHandler;
		
		// start game flow
		System.out.println("CamelotGame: starting game flow");
		while(!isGameEndConditionReached()){
			gettingTheMove();
			swapActivePlayer();
		}
		
		System.out.println("CamelotGame: game ended");
		ConsoleCamelot.printCurrentGameState(this);
		if(this.gameState == CamelotGame.GAME_STATE_END_BLACK_WON){
			System.out.println("Black won!");
			
		}else if(this.gameState == CamelotGame.GAME_STATE_END_WHITE_WON){
			System.out.println("White won!");
			
		}else{
			throw new IllegalStateException("Illegal end state: "+this.gameState);
		}
	}

	/**
	 * swap active player and change game state
	 */
	private void swapActivePlayer() {
		if( this.activePlayerHandler == this.whitePlayerHandler ){
			this.activePlayerHandler = this.blackPlayerHandler;
		}else{
			this.activePlayerHandler = this.whitePlayerHandler;
		}
		
		this.changeGameState();
	}

	/**
	 * Wait for client/player move and execute it.
	 * Notify all clients/players about successful execution of move.
	 */
	private void gettingTheMove() {
		ThisMove thisMove = null;
		// wait for a valid move
		do{
			thisMove = this.activePlayerHandler.getMove();
			try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
			if( thisMove != null && this.moveValidator.isMoveValid(thisMove, false) ){
				break;
			}else if( thisMove != null && !this.moveValidator.isMoveValid(thisMove,true)){
				System.out.println("provided move was invalid: "+thisMove);
				
				ConsoleCamelot.printCurrentGameState(this);
				thisMove=null;
				//System.exit(0);
			}
		}while(thisMove == null);
		
		//execute move
		boolean success = this.movePiece(thisMove);
		if(success){
			this.blackPlayerHandler.moveSuccessfullyExecuted(thisMove);
			this.whitePlayerHandler.moveSuccessfullyExecuted(thisMove);
		}else{
			throw new IllegalStateException("move was valid, but failed to execute it");
		}
	}

	/**
	 * create piece instance and add it to the internal list of pieces
	 * 
	 * @param color on of Pieces.COLOR_..
	 * @param type on of Pieces.TYPE_..
	 * @param row on of Pieces.ROW_..
	 * @param column on of Pieces.COLUMN_..
	 */
	public void createAndAddPiece(int color, int type, int row, int column) {
		Piece piece = new Piece(color, type, row, column);
		this.pieces.add(piece);
	}

	/**
	 * ThisMove piece to the specified location. If the target location is occupied
	 * by an opponent piece, that piece is marked as 'captured'. If the move
	 * could not be executed successfully, 'false' is returned and the game
	 * state does not change.
	 * 
	 * @param thisMove to execute
	 * @return true, if piece was moved successfully
	 */
	public boolean movePiece(ThisMove thisMove) {
		//set captured piece in move
		// this information is needed in the undoMove() method
		int targetRow = thisMove.tRow;
		int targetColumn = thisMove.tColumn;
		

		//move.capturedPiece = this.getNonCapturedPieceAtLocation(move.targetRow, move.targetColumn);
		
		Piece piece = getNonCapturedPieceAtLocation(thisMove.sRow,thisMove.sColumn);
		System.out.println("movepiece");
//		System.out.println(move.sourceRow);
//		System.out.println(move.sourceColumn);
		
//		System.out.println(piece.getColor());
		// check if the move is capturing an opponent piece
	int opponentColor = (piece.getColor() == Piece.COLOR_BLACK ? Piece.COLOR_WHITE: Piece.COLOR_BLACK);
		
//	System.out.println(opponentColor);
	/*	int opponentColor=0;
		int x=piece.getColor();
		if (x==Piece.COLOR_BLACK){
			opponentColor=Piece.COLOR_WHITE;
		} else if (x==Piece.COLOR_WHITE){
			opponentColor=Piece.COLOR_BLACK;
		}else{
			
		}
		
		*/
		
		if (thisMove.tColumn-thisMove.sColumn==0 && thisMove.tRow-thisMove.sRow==2){

			if (isNonCapturedPieceAtLocation(opponentColor, thisMove.tRow-1, thisMove.tColumn)) {
				thisMove.capturedPiece = this.getNonCapturedPieceAtLocation(thisMove.tRow-1, thisMove.tColumn);
				// handle captured piece
				Piece opponentPiece = getNonCapturedPieceAtLocation(thisMove.tRow-1, thisMove.tColumn);
				this.pieces.remove(opponentPiece);
				this.capturedPieces.add(opponentPiece);
				opponentPiece.isCaptured(true);
				piece.setRow(targetRow);
				piece.setColumn(targetColumn);
				return true;
			}
			}
		
		//down
		else if (thisMove.tColumn-thisMove.sColumn==0 && thisMove.tRow-thisMove.sRow==-2){
					
				if (isNonCapturedPieceAtLocation(opponentColor, thisMove.tRow+1, thisMove.tColumn)) {
					thisMove.capturedPiece = this.getNonCapturedPieceAtLocation(thisMove.tRow+1, thisMove.tColumn);
					// handle captured piece
					Piece opponentPiece = getNonCapturedPieceAtLocation(thisMove.tRow+1, thisMove.tColumn);
					this.pieces.remove(opponentPiece);
					this.capturedPieces.add(opponentPiece);
					opponentPiece.isCaptured(true);
					piece.setRow(targetRow);
					piece.setColumn(targetColumn);
					return true;
				}
				}		
				
				//right
		else if (thisMove.tColumn-thisMove.sColumn==2 && thisMove.tRow-thisMove.sRow==0){
				if (isNonCapturedPieceAtLocation(opponentColor, thisMove.tRow, thisMove.tColumn-1)) {
					thisMove.capturedPiece = this.getNonCapturedPieceAtLocation(thisMove.tRow, thisMove.tColumn-1);
					// handle captured piece
					Piece opponentPiece = getNonCapturedPieceAtLocation(thisMove.tRow, thisMove.tColumn-1);
					this.pieces.remove(opponentPiece);
					this.capturedPieces.add(opponentPiece);
					opponentPiece.isCaptured(true);
					piece.setRow(targetRow);
					piece.setColumn(targetColumn);
					return true;
				}
				}

				//left
		else	if (thisMove.tColumn-thisMove.sColumn==-2 && thisMove.tRow-thisMove.sRow==0){
				if (isNonCapturedPieceAtLocation(opponentColor, thisMove.tRow, thisMove.tColumn+1)) {
					thisMove.capturedPiece = this.getNonCapturedPieceAtLocation(thisMove.tRow, thisMove.tColumn+1);
					// handle captured piece
					Piece opponentPiece = getNonCapturedPieceAtLocation(thisMove.tRow, thisMove.tColumn+1);
					this.pieces.remove(opponentPiece);
					this.capturedPieces.add(opponentPiece);
					opponentPiece.isCaptured(true);
					piece.setRow(targetRow);
					piece.setColumn(targetColumn);
					return true;
				}
				}

				
				//topleft
		else if (thisMove.tColumn-thisMove.sColumn==-2 && thisMove.tRow-thisMove.sRow==2){
				if (isNonCapturedPieceAtLocation(opponentColor, thisMove.tRow-1, thisMove.tColumn+1)) {
					thisMove.capturedPiece = this.getNonCapturedPieceAtLocation(thisMove.tRow-1, thisMove.tColumn+1);
					// handle captured piece
					Piece opponentPiece = getNonCapturedPieceAtLocation(thisMove.tRow-1, thisMove.tColumn+1);
					this.pieces.remove(opponentPiece);
					this.capturedPieces.add(opponentPiece);
					opponentPiece.isCaptured(true);
					piece.setRow(targetRow);
					piece.setColumn(targetColumn);
					return true;
				}
				}
				
				//bottomleft
		else if (thisMove.tColumn-thisMove.sColumn==-2 && thisMove.tRow-thisMove.sRow==-2){
				if (isNonCapturedPieceAtLocation(opponentColor, thisMove.tRow+1, thisMove.tColumn+1)) {
					thisMove.capturedPiece = this.getNonCapturedPieceAtLocation(thisMove.tRow+1, thisMove.tColumn+1);
					// handle captured piece
					Piece opponentPiece = getNonCapturedPieceAtLocation(thisMove.tRow+1, thisMove.tColumn+1);
					this.pieces.remove(opponentPiece);
					this.capturedPieces.add(opponentPiece);
					opponentPiece.isCaptured(true);
					piece.setRow(targetRow);
					piece.setColumn(targetColumn);
					return true;
				}
				}
				
				//topright
		else if (thisMove.tColumn-thisMove.sColumn==2 && thisMove.tRow-thisMove.sRow==2){
				if (isNonCapturedPieceAtLocation(opponentColor, thisMove.tRow-1, thisMove.tColumn-1)) {
					thisMove.capturedPiece = this.getNonCapturedPieceAtLocation(thisMove.tRow-1, thisMove.tColumn-1);
					// handle captured piece
					Piece opponentPiece = getNonCapturedPieceAtLocation(thisMove.tRow-1, thisMove.tColumn-1);
					this.pieces.remove(opponentPiece);
					this.capturedPieces.add(opponentPiece);
					opponentPiece.isCaptured(true);
					piece.setRow(targetRow);
					piece.setColumn(targetColumn);
					return true;
				}
				}
				
				//bottomright
		else if (thisMove.tColumn-thisMove.sColumn==2 && thisMove.tRow-thisMove.sRow==-2){
				if (isNonCapturedPieceAtLocation(opponentColor, thisMove.tRow+1, thisMove.tColumn-1)) {
					thisMove.capturedPiece = this.getNonCapturedPieceAtLocation(thisMove.tRow+1, thisMove.tColumn-1);
					// handle captured piece
					Piece opponentPiece = getNonCapturedPieceAtLocation(thisMove.tRow+1, thisMove.tColumn-1);
					this.pieces.remove(opponentPiece);
					this.capturedPieces.add(opponentPiece);
					opponentPiece.isCaptured(true);
					piece.setRow(targetRow);
					piece.setColumn(targetColumn);
					return true;
				}
				}

		// move piece to new position
		piece.setRow(targetRow);
		piece.setColumn(targetColumn);

		// reset game state
		if(piece.getColor() == Piece.COLOR_BLACK){
			this.gameState = CamelotGame.GAME_STATE_BLACK;
		}else{
			this.gameState = CamelotGame.GAME_STATE_WHITE;
		}
		
		return true;
	}
	
	/**
	 * Undo the specified move. It will also adjust the game state appropriately.
	 * @param thisMove
	 */
	public void undoMove(ThisMove thisMove){
		int sourceRow = thisMove.sRow;
		int sourceColumn = thisMove.sColumn;
		int targetRow = thisMove.tRow;
		int targetColumn = thisMove.tColumn;
		
		if(sourceColumn==targetColumn){
			if (sourceRow+1==targetRow||sourceRow-1==targetRow){
				Piece piece = getNonCapturedPieceAtLocation(targetRow, targetColumn);
				piece.setRow(sourceRow);
				piece.setColumn(sourceColumn);

//				if(piece.getColor() == Piece.COLOR_BLACK){
//					this.gameState = CamelotGame.GAME_STATE_BLACK;
//				}else{
//					this.gameState = CamelotGame.GAME_STATE_WHITE;
//				}
				System.out.println("1");
			} else if(sourceRow+2 == targetRow){
				Piece piece = getNonCapturedPieceAtLocation(targetRow, targetColumn);
				piece.setRow(sourceRow);
				piece.setColumn(sourceColumn);
				if(thisMove.capturedPiece != null){
					thisMove.capturedPiece.setRow(targetRow-1);
					thisMove.capturedPiece.setColumn(targetColumn);
					thisMove.capturedPiece.isCaptured(false);
					this.capturedPieces.remove(thisMove.capturedPiece);
					this.pieces.add(thisMove.capturedPiece);
					
//					if(piece.getColor() == Piece.COLOR_BLACK){
//						this.gameState = CamelotGame.GAME_STATE_BLACK;
//					}else{
//						this.gameState = CamelotGame.GAME_STATE_WHITE;
//					}
				}
				System.out.println("2");
			} else if(sourceRow-2 == targetRow){
				Piece piece = getNonCapturedPieceAtLocation(targetRow, targetColumn);
				piece.setRow(sourceRow);
				piece.setColumn(sourceColumn);
				if(thisMove.capturedPiece != null){
					thisMove.capturedPiece.setRow(targetRow+1);
					thisMove.capturedPiece.setColumn(targetColumn);
					thisMove.capturedPiece.isCaptured(false);
					this.capturedPieces.remove(thisMove.capturedPiece);
					this.pieces.add(thisMove.capturedPiece);
//					
//					if(piece.getColor() == Piece.COLOR_BLACK){
//						this.gameState = CamelotGame.GAME_STATE_BLACK;
//					}else{
//						this.gameState = CamelotGame.GAME_STATE_WHITE;
//					}
				}
				System.out.println("3");
			}
		}
		
		if(sourceColumn-2==targetColumn){
			if (sourceRow==targetRow){
				Piece piece = getNonCapturedPieceAtLocation(targetRow, targetColumn);
				piece.setRow(sourceRow);
				piece.setColumn(sourceColumn);
				if(thisMove.capturedPiece != null){
					thisMove.capturedPiece.setRow(targetRow);
					thisMove.capturedPiece.setColumn(targetColumn+1);
					thisMove.capturedPiece.isCaptured(false);
					this.capturedPieces.remove(thisMove.capturedPiece);
					this.pieces.add(thisMove.capturedPiece);
//					
//					if(piece.getColor() == Piece.COLOR_BLACK){
//						this.gameState = CamelotGame.GAME_STATE_BLACK;
//					}else{
//						this.gameState = CamelotGame.GAME_STATE_WHITE;
//					}
				}
				System.out.println("4");
				} else if(sourceRow+2 == targetRow){
				Piece piece = getNonCapturedPieceAtLocation(targetRow, targetColumn);
				piece.setRow(sourceRow);
				piece.setColumn(sourceColumn);
				if(thisMove.capturedPiece != null){
					thisMove.capturedPiece.setRow(targetRow-1);
					thisMove.capturedPiece.setColumn(targetColumn+1);
					thisMove.capturedPiece.isCaptured(false);
					this.capturedPieces.remove(thisMove.capturedPiece);
					this.pieces.add(thisMove.capturedPiece);
					
//					if(piece.getColor() == Piece.COLOR_BLACK){
//						this.gameState = CamelotGame.GAME_STATE_BLACK;
//					}else{
//						this.gameState = CamelotGame.GAME_STATE_WHITE;
//					}
				}
				System.out.println("5");
			} else if(sourceRow-2 == targetRow){
				Piece piece = getNonCapturedPieceAtLocation(targetRow, targetColumn);
				piece.setRow(sourceRow);
				piece.setColumn(sourceColumn);
				if(thisMove.capturedPiece != null){
					thisMove.capturedPiece.setRow(targetRow+1);
					thisMove.capturedPiece.setColumn(targetColumn+1);
					thisMove.capturedPiece.isCaptured(false);
					this.capturedPieces.remove(thisMove.capturedPiece);
					this.pieces.add(thisMove.capturedPiece);
					
//					if(piece.getColor() == Piece.COLOR_BLACK){
//						this.gameState = CamelotGame.GAME_STATE_BLACK;
//					}else{
//						this.gameState = CamelotGame.GAME_STATE_WHITE;
//					}
				}
				System.out.println("6");
			}
		}
		
		
		if(sourceColumn+2==targetColumn){
			if (sourceRow==targetRow){
				Piece piece = getNonCapturedPieceAtLocation(targetRow, targetColumn);
				piece.setRow(sourceRow);
				piece.setColumn(sourceColumn);
				if(thisMove.capturedPiece != null){
					thisMove.capturedPiece.setRow(targetRow);
					thisMove.capturedPiece.setColumn(targetColumn-1);
					thisMove.capturedPiece.isCaptured(false);
					this.capturedPieces.remove(thisMove.capturedPiece);
					this.pieces.add(thisMove.capturedPiece);
					
//					if(piece.getColor() == Piece.COLOR_BLACK){
//						this.gameState = CamelotGame.GAME_STATE_BLACK;
//					}else{
//						this.gameState = CamelotGame.GAME_STATE_WHITE;
//					}
				}
				System.out.println("7");
				} else if(sourceRow+2 == targetRow){
				Piece piece = getNonCapturedPieceAtLocation(targetRow, targetColumn);
				piece.setRow(sourceRow);
				piece.setColumn(sourceColumn);
				if(thisMove.capturedPiece != null){
					thisMove.capturedPiece.setRow(targetRow-1);
					thisMove.capturedPiece.setColumn(targetColumn-1);
					thisMove.capturedPiece.isCaptured(false);
					this.capturedPieces.remove(thisMove.capturedPiece);
					this.pieces.add(thisMove.capturedPiece);
					
//					if(piece.getColor() == Piece.COLOR_BLACK){
//						this.gameState = CamelotGame.GAME_STATE_BLACK;
//					}else{
//						this.gameState = CamelotGame.GAME_STATE_WHITE;
//					}
				}
				System.out.println("8");
			} else if(sourceRow-2 == targetRow){
				Piece piece = getNonCapturedPieceAtLocation(targetRow, targetColumn);
				piece.setRow(sourceRow);
				piece.setColumn(sourceColumn);
				if(thisMove.capturedPiece != null){
					thisMove.capturedPiece.setRow(targetRow+1);
					thisMove.capturedPiece.setColumn(targetColumn-1);
					thisMove.capturedPiece.isCaptured(false);
					this.capturedPieces.remove(thisMove.capturedPiece);
					this.pieces.add(thisMove.capturedPiece);
					
//					if(piece.getColor() == Piece.COLOR_BLACK){
//						this.gameState = CamelotGame.GAME_STATE_BLACK;
//					}else{
//						this.gameState = CamelotGame.GAME_STATE_WHITE;
//					}
				}
				System.out.println("9");
			}
		}
		
		if (sourceColumn-1==targetColumn||sourceColumn+1==targetColumn){
			if(sourceRow+1==targetRow||sourceRow-1==targetRow||sourceRow==targetRow){
				Piece piece = getNonCapturedPieceAtLocation(targetRow, targetColumn);
				piece.setRow(sourceRow);
				piece.setColumn(sourceColumn);
				
//				if(piece.getColor() == Piece.COLOR_BLACK){
//					this.gameState = CamelotGame.GAME_STATE_BLACK;
//				}else{
//					this.gameState = CamelotGame.GAME_STATE_WHITE;
//				}
			}
			System.out.println("10");
		}
		
		
	}

	/**
	 * check if the games end condition is met: One color has a captured king
	 * 
	 * @return true if the game end condition is met
	 */
	private boolean isGameEndConditionReached() {
		
		
		
		 if(blackcondition()== 1)//||whitecondition()==1)
		 {
			 return true;
		}
		 
		else if(whitecondition()==1)
			 {
			return true;
			
			 }
		
		
		for (Piece piece1 : this.capturedPieces) {
			
			if (piece1.getColor() == piece1.COLOR_BLACK && blackcount() == 6 || piece1.getColor() == piece1.COLOR_WHITE && whitecount()==6 )
			{
				
				return true;
			} 
			
			//else if(blackcondition()== 1||whitecondition()==1){
				//return true;
				
			//}
				//else {
				// continue iterating
			//}
		}

		return false;
	}

	/**
	 * returns the first piece at the specified location that is not marked as
	 * 'captured'.
	 * 
	 * @param row one of Piece.ROW_..
	 * @param column one of Piece.COLUMN_..
	 * @return the first not captured piece at the specified location
	 */
	public Piece getNonCapturedPieceAtLocation(int row, int column) {
		for (Piece piece : this.pieces) {
			if (piece.getRow() == row && piece.getColumn() == column) {
				return piece;
			}
		}
		return null;
	}

	/**
	 * Checks whether there is a piece at the specified location that is not
	 * marked as 'captured' and has the specified color.
	 * 
	 * @param color one of Piece.COLOR_..
	 * @param row one of Piece.ROW_..
	 * @param column on of Piece.COLUMN_..
	 * @return true, if the location contains a not-captured piece of the
	 *         specified color
	 */
	boolean isNonCapturedPieceAtLocation(int color, int row, int column) {
		for (Piece piece : this.pieces) {
			if (piece.getRow() == row && piece.getColumn() == column
					&& piece.getColor() == color) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether there is a non-captured piece at the specified location
	 * 
	 * @param row one of Piece.ROW_..
	 * @param column on of Piece.COLUMN_..
	 * @return true, if the location contains a piece
	 */
	boolean isNonCapturedPieceAtLocation(int row, int column) {
		for (Piece piece : this.pieces) {
			if (piece.getRow() == row && piece.getColumn() == column) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return current game state (one of CamelotGame.GAME_STATE_..)
	 */
	public int getGameState() {
		return this.gameState;
	}

	/**
	 * @return the internal list of pieces
	 */
	public List<Piece> getPieces() {
		return this.pieces;
	}

	/**
	 * switches the game state depending on the current board situation.
	 */
	public void changeGameState() {

		// check if game end condition has been reached
		//
		if (this.isGameEndConditionReached()) {

			if (this.gameState == CamelotGame.GAME_STATE_BLACK) {
				this.gameState = CamelotGame.GAME_STATE_END_BLACK_WON;
			} else if(this.gameState == CamelotGame.GAME_STATE_WHITE){
				this.gameState = CamelotGame.GAME_STATE_END_WHITE_WON;
			}else{
				System.out.println(this.gameState+"hello");
				// leave game state as it is
			}
			return;
		}

		switch (this.gameState) {
			case GAME_STATE_BLACK:
				this.gameState = GAME_STATE_WHITE;
				break;
			case GAME_STATE_WHITE:
				this.gameState = GAME_STATE_BLACK;
				break;
			case GAME_STATE_END_WHITE_WON:
			case GAME_STATE_END_BLACK_WON:// don't change anymore
				break;
			default:
				throw new IllegalStateException("unknown game state:" + this.gameState);
		}
	}
	
	/**
	 * @return current move validator
	 */
	public MoveValidator getMoveValidator(){
		return this.moveValidator;
	}

	@Override
	public void run() {
		this.startGame();
	  }


	/** 
	 * Black Count is a function which returns the number of captured black pieces which is used to determine the game end condition
	 * @return
	 */
    public int blackcount(){
    	int counter = 0;
    	for(Piece piece : this.capturedPieces){
				if(piece.getColor() == piece.COLOR_BLACK){
  				counter ++ ;
				}
			}
			return counter;
			}
    
    /** 
	 * White Count is a function which returns the number of captured White pieces which is used to determine the game end condition
	 * @return
	 */
    
    public int whitecount(){
    	int counter = 0;
    	for(Piece piece : this.capturedPieces){
    		if(piece.getColor() == piece.COLOR_WHITE){
    			counter ++;
    		}
    	}
    	return counter;
    }
    
    /** 
	 * White Condition is a function which returns the number assigned when the piece is there at that particular location
	 * which is used to determine the Game end condition
	 * @return
	 */
    
    public int whitecondition(){
    	int num1 = 0;
		
		if(isNonCapturedPieceAtLocation(0,13,3) || isNonCapturedPieceAtLocation(0,13,4)){
			num1 =1;
		}
		 if (isNonCapturedPieceAtLocation(0,13,3)&& isNonCapturedPieceAtLocation(0,13,4)){
			num1 = 2;
		}
		return num1;
    }
    
    
    /** 
	 * Black Condition is a function which returns the number assigned when the piece is there at that particular location
	 * which is used to determine the Game end condition
	 * @return
	 */
    
    public int blackcondition(){
    	int num2 = 0;
		
		 if(isNonCapturedPieceAtLocation(1,0,3) || isNonCapturedPieceAtLocation(1,0,4)){
			num2 =1;
		}
		 
		 if(isNonCapturedPieceAtLocation(1,0,3)&& isNonCapturedPieceAtLocation(1,0,4)){
			 num2 = 2;
		 }
		return num2
				;
    }
}

