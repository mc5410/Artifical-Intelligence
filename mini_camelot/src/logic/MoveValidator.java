/**
 * Move validator class defines all the functions which help to define if a particular move is correct or not
 */

package logic;


public class MoveValidator {

	private CamelotGame camelotGame;
	private Piece sPiece;
	private Piece tPiece;
	private boolean debug;
	private Piece tp1;
	private Piece tp2;
	private Piece tp3;
	private Piece tp4;
	private Piece tp5;
	private Piece tp6;
	private Piece tp7;
	private Piece tp8;


	public MoveValidator(CamelotGame camelotGame){
		this.camelotGame = camelotGame;
	}
	
	/**
	 * Checks if the specified move is valid
	 * @param thisMove to validate
	 * @param debug 
	 * @return true if move is valid, false if move is invalid
	 */
	public boolean isMoveValid(ThisMove thisMove, boolean debug) {
		this.debug = debug;
		int sRow = thisMove.sRow;
		int sColumn = thisMove.sColumn;
		int tRow = thisMove.tRow;
		int tColumn = thisMove.tColumn;
		
		sPiece = this.camelotGame.getNonCapturedPieceAtLocation(sRow, sColumn);
		tPiece = this.camelotGame.getNonCapturedPieceAtLocation(tRow, tColumn);
		tp1 = this.camelotGame.getNonCapturedPieceAtLocation(tRow-1, tColumn);
		tp2 = this.camelotGame.getNonCapturedPieceAtLocation(tRow-1, tColumn-1);
		tp3 = this.camelotGame.getNonCapturedPieceAtLocation(tRow, tColumn-1);
		tp4 = this.camelotGame.getNonCapturedPieceAtLocation(tRow+1, tColumn-1);
		tp5 = this.camelotGame.getNonCapturedPieceAtLocation(tRow+1, tColumn);
		tp6 = this.camelotGame.getNonCapturedPieceAtLocation(tRow+1, tColumn+1);
		tp7 = this.camelotGame.getNonCapturedPieceAtLocation(tRow, tColumn+1);
		tp8 = this.camelotGame.getNonCapturedPieceAtLocation(tRow-1, tColumn+1);
	
		// source piece does not exist
		if( sPiece == null ){
			log("source piece does not exist");
			return false;
		}
		
		if(tRow == 0 && tColumn == 0){
			
			return false;
		}
		if(tRow == 0 && tColumn == 1){
		
			return false;
		}
		if(tRow == 0 && tColumn == 2){
			
			return false;
		}
		if(tRow == 0 && tColumn == 5){
			
			return false;
		}
		if(tRow == 0 && tColumn == 6){
			
			return false;
		}
		if(tRow == 0 && tColumn == 7){
			
			return false;
		}
		if(tRow == 1 && tColumn == 0){
			
			return false;
		}
		if(tRow == 1 && tColumn == 1){
			
			return false;
		}
		if(tRow == 1 && tColumn == 6){
		
			return false;
		}
		if(tRow == 1 && tColumn == 7){
			return false;
		}
		if(tRow == 2 && tColumn == 0){
			return false;
		}
		if(tRow == 2 && tColumn == 7){
			return false;
		}
		if(tRow == 11 && tColumn == 0){
			return false;
		}
		if(tRow == 11 && tColumn == 7){
			return false;
		}
		if(tRow == 12 && tColumn == 0){
			return false;
		}
		if(tRow == 12 && tColumn == 1){
			return false;
		}
		if(tRow == 12 && tColumn == 6){
			return false;
		}
		if(tRow == 12 && tColumn == 7){
			return false;
		}
		if(tRow == 13 && tColumn == 0){
			return false;
		}
		if(tRow == 13 && tColumn == 1){
			return false;
		}
		if(tRow == 13 && tColumn == 2){
			return false;
		}
		if(tRow == 13 && tColumn == 5){
			return false;
		}
		if(tRow == 13 && tColumn == 6){
			return false;
		}
		if(tRow == 13 && tColumn == 7){
			return false;
		}
	/*	
		// source piece has right color?
		if( sPiece.getColor() == Piece.COLOR_WHITE
				&& this.chessGame.getGameState() == CamelotGame.GAME_STATE_WHITE){
			// ok
		}else if( sPiece.getColor() == Piece.COLOR_BLACK
				&& this.chessGame.getGameState() == CamelotGame.GAME_STATE_BLACK){
			// ok
		}else{
			log("it's not your turn: "
					+"pieceColor="+Piece.getColorString(sPiece.getColor())
					+"gameState="+this.chessGame.getGameState());
			ConsoleCamelot.printCurrentGameState(this.chessGame);
			// it's not your turn
			return false;
		}
		*/
		// check if target location within boundaries
		if( tRow < Piece.ROW_1 || tRow > Piece.ROW_14
				|| tColumn < Piece.COLUMN_A || tColumn > Piece.COLUMN_H){
			//target row or column out of scope
			log("target row or column out of scope");
			return false;
		}
		
		// validate piece movement rules
		boolean validPieceMove = false;
		switch (sPiece.getType()) {
			
			case Piece.TYPE_PAWN:
				validPieceMove = isValidPawnMove(sRow,sColumn,tRow,tColumn);
				break;
			
			default: break;
		}
		
		if( !validPieceMove){
			return false;
		}
		
		else{
			// ok
		}
		
		
		// handle stalemate and checkmate
		// ..
		
		return true;
	}

	private boolean isTargetLocationCaptureable() {
		
		if(tp1 == null || tp2 == null || tp3 == null || tp4 == null || tp5 == null || tp6 == null || tp7 == null || tp8 == null ){
			return false;
		}
		
		 if( tp1.getColor() != sPiece.getColor() || tp2.getColor() != sPiece.getColor() || tp3.getColor() != sPiece.getColor() || tp4.getColor() != sPiece.getColor() || tp5.getColor() != sPiece.getColor() || tp6.getColor() != sPiece.getColor() || tp7.getColor() != sPiece.getColor() || tp8.getColor() != sPiece.getColor()){
			return true;
		}
		else{
			return false;
		}
	}

	private boolean isTargetLocationFree() {
		return tPiece == null;
	}


	private boolean isTargetLocationCaptureable1() {
		
		if(tp1 == null || tp2 == null || tp3 == null || tp4 == null || tp5 == null || tp6 == null || tp7 == null || tp8 == null ){
			return false;
		}
		
		 if( tp1.getColor() != sPiece.getColor() || tp2.getColor() != sPiece.getColor() || tp3.getColor() != sPiece.getColor() || tp4.getColor() != sPiece.getColor() || tp5.getColor() != sPiece.getColor() || tp6.getColor() != sPiece.getColor() || tp7.getColor() != sPiece.getColor() || tp8.getColor() != sPiece.getColor()){
			return true;
		}
		else{
			return false;
		}
	}

	private boolean isTargetLocationFree1() {
		if(tp1 == null || tp2 == null || tp3 == null || tp4 == null || tp5 == null || tp6 == null || tp7 == null || tp8 == null ){
			return true;
		}
		
		 if( tp1.getColor() == sPiece.getColor() || tp2.getColor() == sPiece.getColor() || tp3.getColor() == sPiece.getColor() || tp4.getColor() == sPiece.getColor() || tp5.getColor() == sPiece.getColor() || tp6.getColor() == sPiece.getColor() || tp7.getColor() == sPiece.getColor() || tp8.getColor() == sPiece.getColor()){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	/**
	 * This function checks for the valid move and returns true if it is valid and returns false if the move is incorrect
	 * @param sourceRow
	 * @param sourceColumn
	 * @param targetRow
	 * @param targetColumn
	 * @return
	 */
private boolean isValidPawnMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		
		boolean isValid = false;
		if( isTargetLocationFree() ){
		label1:{
			
			if (sourceColumn == targetColumn){	
			if (sourceRow+2==targetRow){
			if (tp1!=null){
				if (tp1.getColor()!=sPiece.getColor()){
				isValid=true;
				break label1;
				}
			}
		}   
			else if (sourceRow-2==targetRow) {
			if (tp5!=null){
				if (tp5.getColor()!=sPiece.getColor()){
					isValid=true;
					break label1;
			}			
		}
		}
		}
			
			
			
			if (sourceColumn+2 == targetColumn){
				if (sourceRow+2 == targetRow){
					if (tp2!=null ){
						if (tp2.getColor()!=sPiece.getColor()){
							isValid=true;
							break label1;
					}	
					}
				} else if (sourceRow-2 == targetRow){
					if (tp4!=null ){
						if (tp4.getColor()!=sPiece.getColor()){
							isValid=true;
							break label1;
					}	
				}
				} else if (sourceRow == targetRow){
					if (tp3 != null){
						if (tp3.getColor()!=sPiece.getColor()){
							isValid=true;
							break label1;
					}	
				}
			}
			}
			

			if (sourceColumn-2 == targetColumn){
				if (sourceRow+2 == targetRow){
					if (tp8!=null ){
						if (tp8.getColor()!=sPiece.getColor()){
							isValid=true;
							break label1;
					}	
					}
				} else if (sourceRow-2 == targetRow){
					if (tp6!=null ){
						if (tp6.getColor()!=sPiece.getColor()){
							isValid=true;
							break label1;
					}	
				}
				} else if (sourceRow == targetRow){
					if (tp7!=null ){
						if (tp7.getColor()!=sPiece.getColor()){
							isValid=true;
							break label1;
					}	
				}
			}
			}
			
			
			
			
			
			if (sourceColumn == targetColumn){	
			if( sourceRow+1 == targetRow || sourceRow-1 == targetRow){
			isValid = true;
		}
			else if (sourceRow+2==targetRow){
			if (tp1!=null){
				isValid=true;
			}
		}   
			else if (sourceRow-2==targetRow) {
			if (tp5!=null){
				isValid=true;
			}			
		}
		}
		
		if (sourceColumn-1 == targetColumn){	
			if(  sourceRow == targetRow || sourceRow+1 == targetRow || sourceRow-1 == targetRow){
			isValid = true;
		}else{
			isValid = false;
		}
		}	

		if (sourceColumn+1 == targetColumn){	
			if(  sourceRow == targetRow || sourceRow+1 == targetRow || sourceRow-1 == targetRow){
			isValid = true;
		}else{
			
			isValid = false;
		}
		}	
		
		if (sourceColumn+2 == targetColumn){
			if (sourceRow+2 == targetRow){
				if (tp2!=null ){
					isValid=true;
				}
			} else if (sourceRow-2 == targetRow){
				if (tp4!=null ){
					isValid=true;
			}
			} else if (sourceRow == targetRow){
				if (tp3 != null){
					isValid=true;
			}
		}
		}
		

		if (sourceColumn-2 == targetColumn){
			if (sourceRow+2 == targetRow){
				if (tp8!=null ){
					isValid=true;
				}
			} else if (sourceRow-2 == targetRow){
				if (tp6!=null ){
					isValid=true;
			}
			} else if (sourceRow == targetRow){
				if (tp7!=null ){
					isValid=true;
			}
		}
		}
		
	
		
		}
		
		
		// or it may move
		// to a square occupied by an opponent’s piece, which is diagonally in front
		// of it on an adjacent file, capturing that piece. 
		}else if( isTargetLocationCaptureable() ){
			


			
			
		}
		

		return isValid;
	}



	public static void main(String[] args) {

	}

	private void log(String message) {
		if(debug) System.out.println(message);
		
	}
	
}
