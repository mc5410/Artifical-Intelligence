/**
 * This class is created to implement the difficulty of the game 
 * which is divided into three levels easy medium and hard .
 * each level has it's own depth 
 * and depending upon the depth the Min prunning and max prunning is done.
 * 
 */

package mini_camelot;


import javax.swing.JFrame;

import ai.AiPlayer;
import gui.GuiCamelot;
import logic.CamelotGame;
import logic.Piece;

	
	public class Camelot extends JFrame {
		/**
		 * 
		 */
		private static final long serialVersionUID = -546865235030949313L;
		public static final int EASY = 1, MED = 2, HARD = 4;
		public static final int WHITE = 0, BLACK = 1;
		int level = EASY;
		int color = WHITE;

		public Camelot(int lvl ,int clr) {
			level = lvl;
			color = clr;
			if (level == 1){
				
			CamelotGame camelotGame = new CamelotGame();
			GuiCamelot guiCamelot = new GuiCamelot(camelotGame);
			AiPlayer ai1 = new AiPlayer(camelotGame);
			ai1.maxDepth = 1;
			if(clr == 1){
			camelotGame.setPlayer(Piece.COLOR_WHITE, ai1);
			camelotGame.setPlayer(Piece.COLOR_BLACK, guiCamelot);
			new Thread(camelotGame).start();
			}
			else 
			{
				camelotGame.setPlayer(Piece.COLOR_BLACK, ai1);
				camelotGame.setPlayer(Piece.COLOR_WHITE, guiCamelot);
				new Thread(camelotGame).start();
			}
			
			}
			
			else if( level == 2){
				CamelotGame camelotGame = new CamelotGame();
				GuiCamelot guiCamelot = new GuiCamelot(camelotGame);
				AiPlayer ai2 = new AiPlayer(camelotGame);
				ai2.maxDepth = 2;
				if(clr == 1){
					camelotGame.setPlayer(Piece.COLOR_WHITE, ai2);
					camelotGame.setPlayer(Piece.COLOR_BLACK, guiCamelot);
					new Thread(camelotGame).start();
					}
					else 
					{
						camelotGame.setPlayer(Piece.COLOR_BLACK, ai2);
						camelotGame.setPlayer(Piece.COLOR_WHITE, guiCamelot);
						new Thread(camelotGame).start();
					}
				
			}
			
			else if( level == 4){
				CamelotGame camelotGame = new CamelotGame();
				GuiCamelot guiCamelot = new GuiCamelot(camelotGame);
				AiPlayer ai3 = new AiPlayer(camelotGame);
				ai3.maxDepth = 4;
				if(clr == 1){
					camelotGame.setPlayer(Piece.COLOR_WHITE, ai3);
					camelotGame.setPlayer(Piece.COLOR_BLACK, guiCamelot);
					new Thread(camelotGame).start();
					}
					else 
					{
						camelotGame.setPlayer(Piece.COLOR_BLACK, ai3);
						camelotGame.setPlayer(Piece.COLOR_WHITE, guiCamelot);
						new Thread(camelotGame).start();
					}
			}
			
		}

	}


