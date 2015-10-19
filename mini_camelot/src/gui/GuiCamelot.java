package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.CamelotGame;
import logic.IPlayerHandler;
import logic.ThisMove;
import logic.MoveValidator;
import logic.Piece;

/**
 * all x and y coordinates point to the upper left position of a component all
 * lists are treated as 0 being the bottom and size-1 being the top piece
 * 
 */
public class GuiCamelot extends JPanel implements IPlayerHandler{
	
	private static final long serialVersionUID = -8207574964820892354L;
	
	private static final int START_X = 476;
	private static final int START_Y = -6;

	private static final int SQUARE_WIDTH = 46;
	private static final int SQUARE_HEIGHT = 43;

	private static final int PIECE_WIDTH = 48;
	private static final int PIECE_HEIGHT = 48;
	
	private static final int PIECES_START_X = START_X + (int)(SQUARE_WIDTH/2.0 - PIECE_WIDTH/2.0);
	private static final int PIECES_START_Y = START_Y + (int)(SQUARE_HEIGHT/2.0 - PIECE_HEIGHT/2.0);
	
	private static final int DRAG_TARGET_SQUARE_START_X = START_X - (int)(PIECE_WIDTH/2.0);
	private static final int DRAG_TARGET_SQUARE_START_Y = START_Y - (int)(PIECE_HEIGHT/2.0);

	private Image imgBg;
	private JLabel lblState;
	
	private CamelotGame camelotGame;
	private List<GuiPiece> guiPieces = new ArrayList<GuiPiece>();

	private GuiPiece dragPiece;

	public ThisMove lastMove;
	private ThisMove currentMove;

	private boolean draggingGamePiecesEnabled;

	/**
	 * 
	 * constructor - creating the user interface
	 * @param camelotGame - the game to be presented
	 */
	public GuiCamelot(CamelotGame camelotGame) {
		this.setLayout(null);

		// background
		URL urlBackgroundImg = getClass().getResource("/gui/img/bo.png");
		this.imgBg = new ImageIcon(urlBackgroundImg).getImage();
		
		// create chess game
		this.camelotGame = camelotGame;
		
		
		//wrap game pieces into their graphical representation
		for (Piece piece : this.camelotGame.getPieces()) {
			createGuiPiece(piece);
		}
		

		// add listeners to enable drag and drop
		//
		PiecesDragAndDropListener listener = new PiecesDragAndDropListener(this.guiPieces,
				this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);

		// label to display game state
		String labelText = this.getGameStateText();
		this.lblState = new JLabel(labelText);
		lblState.setBounds(0, 30, 80, 30);
		lblState.setForeground(Color.WHITE);
		this.add(lblState);

		// create application frame and set visible
		//
		JFrame f = new JFrame();
		f.setSize(90, 90);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this);
		f.setSize(imgBg.getWidth(null), imgBg.getHeight(null));
	}

	/**
	 * @return textual description of current game state
	 */
	private String getGameStateText() {
		String state = "unknown";
		switch (this.camelotGame.getGameState()) {
			case CamelotGame.GAME_STATE_BLACK: state = "black";break;
			case CamelotGame.GAME_STATE_END_WHITE_WON: state = "white won";break;
			case CamelotGame.GAME_STATE_END_BLACK_WON: state = "black won";break;
			case CamelotGame.GAME_STATE_WHITE: state = "white";break;
		}
		return state;
	}

	/**
	 * create a game piece
	 * 
	 * @param color color constant
	 * @param type type constant
	 * @param x x position of upper left corner
	 * @param y y position of upper left corner
	 */
	private void createGuiPiece(Piece piece) {
		Image img = this.getImageForPiece(piece.getColor());
		GuiPiece guiPiece = new GuiPiece(img, piece);
		this.guiPieces.add(guiPiece);
	}

	/**
	 * load image for given color and type. This method translates the color and
	 * type information into a filename and loads that particular file.
	 * 
	 * @param color color constant
	 * @param type type constant
	 * @return image
	 */
	private Image getImageForPiece(int color) {

		String filename = "";

		filename += (color == Piece.COLOR_WHITE ? "w" : "b");
		
				filename += "p";
			
			
		filename += ".png";

		URL urlPieceImg = getClass().getResource("/gui/img/" + filename);
		return new ImageIcon(urlPieceImg).getImage();
	}

	@Override
	protected void paintComponent(Graphics g) {

		// draw background
		g.drawImage(this.imgBg, 0, 0, null);

		// draw pieces
		for (GuiPiece guiPiece : this.guiPieces) {
			if( !guiPiece.isCaptured()){
				g.drawImage(guiPiece.getImage(), guiPiece.getX(), guiPiece.getY(), null);
			}
		}
		
		// draw last move, if user is not dragging game piece
		if( !isUserDraggingPiece() && this.lastMove != null ){
			int highlightSourceX = convertColumnToX(this.lastMove.sColumn);
			int highlightSourceY = convertRowToY(this.lastMove.sRow);
			int highlightTargetX = convertColumnToX(this.lastMove.tColumn);
			int highlightTargetY = convertRowToY(this.lastMove.tRow);
			
			g.setColor(Color.YELLOW);
			g.drawRoundRect( highlightSourceX+4, highlightSourceY+4, SQUARE_WIDTH-8, SQUARE_HEIGHT-8,10,10);
			g.drawRoundRect( highlightTargetX+4, highlightTargetY+4, SQUARE_WIDTH-8, SQUARE_HEIGHT-8,10,10);
			//g.drawLine(highlightSourceX+SQUARE_WIDTH/2, highlightSourceY+SQUARE_HEIGHT/2
			//		, highlightTargetX+SQUARE_WIDTH/2, highlightTargetY+SQUARE_HEIGHT/2);
		}
		
		// draw valid target locations, if user is dragging a game piece
		if( isUserDraggingPiece() ){
			
			MoveValidator moveValidator = this.camelotGame.getMoveValidator();
			
			// iterate the complete board to check if target locations are valid
			for (int column = Piece.COLUMN_A; column <= Piece.COLUMN_H; column++) {
				for (int row = Piece.ROW_1; row <= Piece.ROW_14; row++) {
					int sourceRow = this.dragPiece.getPiece().getRow();
					int sourceColumn = this.dragPiece.getPiece().getColumn();
					
					// check if target location is valid
					if( moveValidator.isMoveValid( new ThisMove(sourceRow, sourceColumn, row, column), false) ){
						
						int highlightX = convertColumnToX(column);
						int highlightY = convertRowToY(row);
						
						// draw a black drop shadow by drawing a black rectangle with an offset of 1 pixel
						g.setColor(Color.BLACK);
						g.drawRoundRect( highlightX+5, highlightY+5, SQUARE_WIDTH-8, SQUARE_HEIGHT-8,10,10);
						// draw the highlight
						g.setColor(Color.GREEN);
						g.drawRoundRect( highlightX+4, highlightY+4, SQUARE_WIDTH-8, SQUARE_HEIGHT-8,10,10);
					}
				}
			}
		}
		
		
		// draw game state label
		this.lblState.setText(this.getGameStateText());
	}

	/**
	 * check if the user is currently dragging a game piece
	 * @return true - if the user is currently dragging a game piece
	 */
	private boolean isUserDraggingPiece() {
		return this.dragPiece != null;
	}

	/**
	 * @return current game state
	 */
	public int getGameState() {
		return this.camelotGame.getGameState();
	}
	
	/**
	 * convert logical column into x coordinate
	 * @param column
	 * @return x coordinate for column
	 */
	public static int convertColumnToX(int column){
		return PIECES_START_X + SQUARE_WIDTH * column;
	}
	
	/**
	 * convert logical row into y coordinate
	 * @param row
	 * @return y coordinate for row
	 */
	public static int convertRowToY(int row){
		return PIECES_START_Y + SQUARE_HEIGHT * (Piece.ROW_14 - row);
	}
	
	/**
	 * convert x coordinate into logical column
	 * @param x
	 * @return logical column for x coordinate
	 */
	public static int convertXToColumn(int x){
		return (x - DRAG_TARGET_SQUARE_START_X)/SQUARE_WIDTH;
	}
	
	/**
	 * convert y coordinate into logical row
	 * @param y
	 * @return logical row for y coordinate
	 */
	public static int convertYToRow(int y){
		return Piece.ROW_14 - (y - DRAG_TARGET_SQUARE_START_Y)/SQUARE_HEIGHT;
	}

	/**
	 * change location of given piece, if the location is valid.
	 * If the location is not valid, move the piece back to its original
	 * position.
	 * @param dragPiece
	 * @param x
	 * @param y
	 */
	public void setNewPieceLocation(GuiPiece dragPiece, int x, int y) {
		int targetRow = GuiCamelot.convertYToRow(y);
		int targetColumn = GuiCamelot.convertXToColumn(x);
		
		ThisMove thisMove = new ThisMove(dragPiece.getPiece().getRow(), dragPiece.getPiece().getColumn()
				, targetRow, targetColumn);
		if( this.camelotGame.getMoveValidator().isMoveValid(thisMove, true) ){
			this.currentMove = thisMove;
		}else{
			dragPiece.piecePosition();
		}
	}

	/**
	 * set the game piece that is currently dragged by the user
	 * @param guiPiece
	 */
	public void setDragPiece(GuiPiece guiPiece) {
		this.dragPiece = guiPiece;
	}
	
	/**
	 * return the gui piece that the user is currently dragging
	 * @return the gui piece that the user is currently dragging
	 */
	public GuiPiece getDragPiece(){
		return this.dragPiece;
	}

	@Override
	public ThisMove getMove() {
		this.draggingGamePiecesEnabled = true; 
		ThisMove moveForExecution = this.currentMove;
		this.currentMove = null;
		return moveForExecution;
	}

	@Override
	public void moveSuccessfullyExecuted(ThisMove thisMove) {
		// adjust GUI piece
		GuiPiece guiPiece = this.getGuiPieceAt(thisMove.tRow, thisMove.tColumn);
		if( guiPiece == null){
			throw new IllegalStateException("no guiPiece at "+thisMove.tRow+"/"+thisMove.tColumn);
		}
		guiPiece.piecePosition();
		
		// remember last move
		this.lastMove = thisMove;
		
		// disable dragging until asked by CamelotGame for the next move
		this.draggingGamePiecesEnabled = false;
				
		// repaint the new state
		this.repaint();
		
	}
	
	/**
	 * @return true - if the user is allowed to drag game pieces
	 */
	public boolean isDraggingGamePiecesEnabled(){
		return draggingGamePiecesEnabled;
	}

	/**
	 * get non-captured the gui piece at the specified position
	 * @param row
	 * @param column
	 * @return the gui piece at the specified position, null if there is no piece
	 */
	private GuiPiece getGuiPieceAt(int row, int column) {
		for (GuiPiece guiPiece : this.guiPieces) {
			if( guiPiece.getPiece().getRow() == row
					&& guiPiece.getPiece().getColumn() == column
					&& guiPiece.isCaptured() == false){
				return guiPiece;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		CamelotGame camelotGame = new CamelotGame();
		GuiCamelot guiCamelot = new GuiCamelot(camelotGame);
		camelotGame.setPlayer(Piece.COLOR_WHITE, guiCamelot);
		camelotGame.setPlayer(Piece.COLOR_BLACK, guiCamelot);
		new Thread(camelotGame).start();
	}
}
