package gui;
/**
 * Here all the functions  which make moving the GUI piece possible are defined* */
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import logic.CamelotGame;
import logic.Piece;

public class PiecesDragAndDropListener implements MouseListener, MouseMotionListener {

	private List<GuiPiece> guiPieces;
	private GuiCamelot guiCamelot;
	
	private int dragOffsetX;
	private int dragOffsetY;
	

	public PiecesDragAndDropListener(List<GuiPiece> guiPieces, GuiCamelot guiCamelot) {
		this.guiPieces = guiPieces;
		this.guiCamelot = guiCamelot;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent evt) {
		if( !this.guiCamelot.isDraggingGamePiecesEnabled()){
			return;
		}
		
		int x = evt.getPoint().x;
		int y = evt.getPoint().y;
		

		// find out which piece to move.
		// we check the list from top to buttom
		// (therefore we itereate in reverse order)
		//
		for (int i = this.guiPieces.size()-1; i >= 0; i--) {
			GuiPiece guiPiece = this.guiPieces.get(i);
			if (guiPiece.isCaptured()) continue;

			if(mouseOverPiece(guiPiece,x,y)){
				
				if( (	this.guiCamelot.getGameState() == CamelotGame.GAME_STATE_WHITE
						&& guiPiece.getColor() == Piece.COLOR_WHITE
					) ||
					(	this.guiCamelot.getGameState() == CamelotGame.GAME_STATE_BLACK
							&& guiPiece.getColor() == Piece.COLOR_BLACK
						)
					){
					// calculate offset, because we do not want the drag piece
					// to jump with it's upper left corner to the current mouse
					// position
					//
					this.dragOffsetX = x - guiPiece.getX();
					this.dragOffsetY = y - guiPiece.getY();
					this.guiCamelot.setDragPiece(guiPiece);
					this.guiCamelot.repaint();
					break;
				}
			}
		}
		
		// move drag piece to the top of the list
		if(this.guiCamelot.getDragPiece() != null){
			this.guiPieces.remove( this.guiCamelot.getDragPiece() );
			this.guiPieces.add(this.guiCamelot.getDragPiece());
		}
	}

	/**
	 * check whether the mouse is currently over this piece
	 * @param piece the playing piece
	 * @param x x coordinate of mouse
	 * @param y y coordinate of mouse
	 * @return true if mouse is over the piece
	 */
	private boolean mouseOverPiece(GuiPiece guiPiece, int x, int y) {

		return guiPiece.getX() <= x 
			&& guiPiece.getX()+guiPiece.getWidth() >= x
			&& guiPiece.getY() <= y
			&& guiPiece.getY()+guiPiece.getHeight() >= y;
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		if( this.guiCamelot.getDragPiece() != null){
			int x = evt.getPoint().x - this.dragOffsetX;
			int y = evt.getPoint().y - this.dragOffsetY;
			
			// set game piece to the new location if possible
			//
			guiCamelot.setNewPieceLocation(this.guiCamelot.getDragPiece(), x, y);
			this.guiCamelot.repaint();
			this.guiCamelot.setDragPiece(null);
		}
	}
/**
 * Its checks for the event where the mouse is dragged.
 */
	@Override
	public void mouseDragged(MouseEvent evt) {
		if(this.guiCamelot.getDragPiece() != null){
			
			int x = evt.getPoint().x - this.dragOffsetX;
			int y = evt.getPoint().y - this.dragOffsetY;
			
			GuiPiece dragPiece = this.guiCamelot.getDragPiece();
			dragPiece.setX(x);
			dragPiece.setY(y);
			
			this.guiCamelot.repaint();
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}

}
