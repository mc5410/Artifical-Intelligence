/**
 * Here all the images of the pieces are made so that they can fit on the board
 */

package gui;

import java.awt.Image;

import logic.Piece;

public class GuiPiece {
	
	private Image img;
	private int x;
	private int y;
	private Piece piece;

	public GuiPiece(Image img, Piece piece) {
		this.img = img;
		this.piece = piece;

		this.piecePosition();
	}

	public Image getImage() {
		return img;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return img.getHeight(null);
	}

	public int getHeight() {
		return img.getHeight(null);
	}

	public int getColor() {
		return this.piece.getColor();
	}
	
	@Override
	public String toString() {
		return this.piece+" "+x+"/"+y;
	}

	/**
	 * move the gui piece back to the coordinates that
	 * correspond with the underlying piece's row and column
	 */
	public void piecePosition() {
		this.x = GuiCamelot.convertColumnToX(piece.getColumn());
		this.y = GuiCamelot.convertRowToY(piece.getRow());
	}

	public Piece getPiece() {
		return piece;
	}

	public boolean isCaptured() {
		return this.piece.isCaptured();
	}

}
