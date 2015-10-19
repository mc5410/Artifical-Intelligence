/**
 * ThisMove class initializes the source row , source column , target row and target Column and also returns the move in a format so that 
 * it can be used in other classes. 
 */
package logic;

public class ThisMove {
	public int sRow;
	public int sColumn;
	public int tRow;
	public int tColumn;
	
	public int score;
	public Piece capturedPiece;
	
	public ThisMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
		this.sRow = sourceRow;
		this.sColumn = sourceColumn;
		this.tRow = targetRow;
		this.tColumn = targetColumn;
	}
	
	@Override
	public String toString() {
		return Piece.getColumnString(sColumn)+"/"+Piece.getRowString(sRow)
		+" -> "+Piece.getColumnString(tColumn)+"/"+Piece.getRowString(tRow);
		//return sRow+"/"+sColumn+" -> "+tRow+"/"+tColumn;
	}

	public ThisMove clone(){
		return new ThisMove(sRow,sColumn,tRow,tColumn);
	}
}
