/**
 * Console Camelot displays the game board and game state and the moves in the console 
 * This is implemented to make sure there are no errors in the game .
 */


package console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import logic.CamelotGame;
import logic.IPlayerHandler;
import logic.ThisMove;
import logic.Piece;

public class ConsoleCamelot implements IPlayerHandler{

	private CamelotGame camelotGame;

	public ConsoleCamelot(CamelotGame camelotGame) {

		// create a new chess game
		//
		this.camelotGame = camelotGame;
		
		printCurrentGameState(this.camelotGame);
	}

	public static void main(String[] args) {
		CamelotGame camelotGame = new CamelotGame();
		ConsoleCamelot consoleGui = new ConsoleCamelot(camelotGame);
		camelotGame.setPlayer(Piece.COLOR_WHITE, consoleGui);
		camelotGame.setPlayer(Piece.COLOR_BLACK, consoleGui);
		new Thread(camelotGame).start();
	}

	
	/**
	 *This function converts the input string of the move to the Move.
	 * 
	 * 
	 * @param input
	 * @return
	 */
	private ThisMove convertStringToMove(String input) {
		if(input == null || input.length() != 5 && input.length() !=6){ return null;}
		
		String strSourceColumn = input.substring(0, 1);
		String strSourceRow = input.substring(1, 2);
		String strTargetColumn = input.substring(3, 4);
		String strTargetRow = input.substring(4, 5);

		int sourceColumn = 0;
		int sourceRow = 0;
		int targetColumn = 0;
		int targetRow = 0;

		sourceColumn = convertColumnStrToColumnInt(strSourceColumn);
		sourceRow = convertRowStrToRowInt(strSourceRow);
		targetColumn = convertColumnStrToColumnInt(strTargetColumn);
		targetRow = convertRowStrToRowInt(strTargetRow);

		return new ThisMove(sourceRow, sourceColumn, targetRow, targetColumn);
	}

	/**
	 * Converts a column string (e.g. 'a') into its internal representation.
	 * 
	 * @param strColumn a valid column string (e.g. 'a')
	 * @return internal integer representation of the column
	 */
	private int convertColumnStrToColumnInt(String strColumn) {
		if (strColumn.equalsIgnoreCase("a")) {
			return Piece.COLUMN_A;
		} else if (strColumn.equalsIgnoreCase("b")) {
			return Piece.COLUMN_B;
		} else if (strColumn.equalsIgnoreCase("c")) {
			return Piece.COLUMN_C;
		} else if (strColumn.equalsIgnoreCase("d")) {
			return Piece.COLUMN_D;
		} else if (strColumn.equalsIgnoreCase("e")) {
			return Piece.COLUMN_E;
		} else if (strColumn.equalsIgnoreCase("f")) {
			return Piece.COLUMN_F;
		} else if (strColumn.equalsIgnoreCase("g")) {
			return Piece.COLUMN_G;
		} else if (strColumn.equalsIgnoreCase("h")) {
			return Piece.COLUMN_H;
		} else
			throw new IllegalArgumentException("invalid column: " + strColumn);
	}

	/**
	 * Converts a row string (e.g. '1') into its internal representation.
	 * 
	 * @param strRow a valid row string (e.g. '1')
	 * @return internal integer representation of the row
	 */
	private int convertRowStrToRowInt(String strRow) {
		if (strRow.equalsIgnoreCase("1")) {
			return Piece.ROW_1;
		} else if (strRow.equalsIgnoreCase("2")) {
			return Piece.ROW_2;
		} else if (strRow.equalsIgnoreCase("3")) {
			return Piece.ROW_3;
		} else if (strRow.equalsIgnoreCase("4")) {
			return Piece.ROW_4;
		} else if (strRow.equalsIgnoreCase("5")) {
			return Piece.ROW_5;
		} else if (strRow.equalsIgnoreCase("6")) {
			return Piece.ROW_6;
		} else if (strRow.equalsIgnoreCase("7")) {
			return Piece.ROW_7;
		} else if (strRow.equalsIgnoreCase("8")) {
			return Piece.ROW_8;
		} else if (strRow.equalsIgnoreCase("9")) {
			return Piece.ROW_9;
		} else if (strRow.equalsIgnoreCase("10")) {
			return Piece.ROW_10;
		} else if (strRow.equalsIgnoreCase("11")) {
			return Piece.ROW_11;
		} else if (strRow.equalsIgnoreCase("12")) {
			return Piece.ROW_12;
		} else if (strRow.equalsIgnoreCase("13")) {
			return Piece.ROW_13;
		} else if (strRow.equalsIgnoreCase("14")) {
			return Piece.ROW_14;
		} else
			throw new IllegalArgumentException("invalid column: " + strRow);
	}

	/**
	 * Print current game board and game state information.
	 */
	public static void printCurrentGameState(CamelotGame game) {

		System.out.println("  a  b  c  d  e  f  g  h  ");
		for (int row = Piece.ROW_14; row >= Piece.ROW_1; row--) {

			System.out.println(" +--+--+--+--+--+--+--+--+");
			String strRow = (row + 1) + "|";
			for (int column = Piece.COLUMN_A; column <= Piece.COLUMN_H; column++) {
				Piece piece = game.getNonCapturedPieceAtLocation(row, column);
				String pieceStr = getNameOfPiece(piece);
				strRow += pieceStr + "|";
			}
			System.out.println(strRow + (row + 1));
		}
		System.out.println(" +--+--+--+--+--+--+--+--+");
		System.out.println("  a  b  c  d  e  f  g  h  ");

		String gameStateStr = "unknown";
		switch (game.getGameState()) {
			case CamelotGame.GAME_STATE_BLACK: gameStateStr="black";break;
			case CamelotGame.GAME_STATE_END_WHITE_WON: gameStateStr="white won";break;
			case CamelotGame.GAME_STATE_END_BLACK_WON: gameStateStr="black won";break;
			case CamelotGame.GAME_STATE_WHITE: gameStateStr="white";break;
		}
		System.out.println("state: " + gameStateStr);

	}

	private static String getNameOfPiece(Piece piece) {
		if (piece == null)
			return "  ";

		String strColor = "";
		switch (piece.getColor()) {
			case Piece.COLOR_BLACK:
				strColor = "B";
				break;
			case Piece.COLOR_WHITE:
				strColor = "W";
				break;
			default:
				strColor = "?";
				break;
		}

		String strType = "";
		switch (piece.getType()) {
			
			case Piece.TYPE_PAWN:
				strType = "P";
				break;
			
			default:
				strType = "?";
				break;
		}

		return strColor + strType;
	}

	@Override
	public ThisMove getMove() {
		System.out.println("your move (format: e2-e3): ");
		
		ThisMove thisMove = null;
		while(thisMove == null){
			//read user input
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
			String input;
			try {
				input = inputReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			// exit, if user types 'exit'
			if (input.equalsIgnoreCase("exit")){
				System.exit(0);
			}else{
				thisMove = this.convertStringToMove(input);
			}
		}
		return thisMove;
	}

	@Override
	public void moveSuccessfullyExecuted(ThisMove thisMove) {
		printCurrentGameState(this.camelotGame);
		
		if( this.camelotGame.getGameState() == CamelotGame.GAME_STATE_END_BLACK_WON ){
			System.out.println("game end reached! Black won!");
		}else if( this.camelotGame.getGameState() == CamelotGame.GAME_STATE_END_WHITE_WON){
			System.out.println("game end reached! White won!");
		}
	}

}
