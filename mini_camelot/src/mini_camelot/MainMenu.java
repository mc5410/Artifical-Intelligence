/**
 * This class is implemented to provide the GUI to the user where he can select the color of the piece and also the diffculty
 * of the game. 
 */

package mini_camelot;

/*This class is created for the purpose of creating a small popup menu so that the 
 * palyer can select the color he wants to choose and also the difficulty of the game which has 3 levels
 * easy , meduim , hard 
 * 
 * 
 */
import mini_camelot.ScrollingText;
import mini_camelot.ButtonGui;
import mini_camelot.Camelot;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class MainMenu extends JFrame implements ActionListener {
	
	
		/**
	 * 
	 */
	private static final long serialVersionUID = 7528008692193183835L;
	public static int levl;
		JTextField pl1 = new JTextField();
		ButtonGui start = new ButtonGui("Start");
		ButtonGui close = new ButtonGui("X");
		ButtonGui icon = new ButtonGui("-");
		JRadioButton sp = new JRadioButton("White", true);
		JRadioButton mp = new JRadioButton("Black", false);
		JRadioButton easy = new JRadioButton("Easy", false);
		JRadioButton med = new JRadioButton("Medium", true);
		JRadioButton hard = new JRadioButton("Hard", false);
		CardLayout card = new CardLayout();
		JPanel bottom = new JPanel(card);
		ScrollingText dis = new ScrollingText(
				Color.white.darker(),
				Color.cyan,
				new Font("Arial", Font.BOLD, 12),
				3,
				50,
				"Welcome to Mini Camelot, the game!!!                                                                                         Created by Manish                                                                                     Enjoy the game!!!                 ");

		public MainMenu(Dimension d) {
			this.setSize(400, 210);
			this.setLocation(d.width / 2 - 200, d.height / 2 - 100);
			this.setTitle("Mini Camelot");
			this.setResizable(false);
			this.setUndecorated(true);
			JPanel pane = new JPanel();
			JPanel top = new JPanel();
			JLabel comp = new JLabel("What level should the computer play at?");
			JLabel c = new JLabel("Choose any Color:");
			JLabel j1 = new JLabel("Player 1, enter your name:");
			JSeparator sep = new JSeparator();
			top.setBackground(Color.red.brighter());
			top.setLayout(null);
			top.add(mp);
			top.add(sp);
			top.add(icon);
			top.add(dis);
			top.add(c);
			top.add(sep);
			top.add(close);
			sp.setBounds(150, 25, 125, 25);
			mp.setBounds(300, 25, 125, 25);
			c.setBounds(25, 25, 125, 25);
			mp.setBackground(Color.red.brighter());
			sp.setBackground(Color.red.brighter());
			easy.setBackground(Color.green);
			med.setBackground(Color.green);
			hard.setBackground(Color.green);
			pane.setBackground(Color.red.brighter());
			pane.setLayout(null);
			JPanel b1 = new JPanel(null);
			b1.add(comp);
			b1.add(easy);
			b1.add(med);
			b1.add(hard);
			b1.add(pl1);
			b1.add(start);
			b1.add(j1);
			pane.add(top);
			b1.setBackground(Color.green);
			bottom.add(b1, "0");
			bottom.setBackground(Color.red);
			pane.add(bottom);
			bottom.setBounds(0, 50, 400, 160);
			this.setContentPane(pane);
			top.setBounds(0, 0, 400, 50);
			close.setBounds(380, 5, 15, 15);
			icon.setBounds(360, 5, 15, 15);
			sep.setBounds(0, 21, 400, 4);
			dis.setBounds(0, 0, 355, 20);
			comp.setBounds(25, 50, 350, 25);
			easy.setBounds(25, 75, 100, 25);
			med.setBounds(125, 75, 110, 25);
			hard.setBounds(260, 75, 100, 25);
			pl1.setBounds(175, 25, 200, 20);
			j1.setBounds(10, 25, 165, 25);
			start.setBounds(250, 105, 100, 25);
			this.setVisible(true);
			dis.start();
			ButtonGroup bg = new ButtonGroup();
			ButtonGroup bg2 = new ButtonGroup();
			bg2.add(easy);
			bg2.add(med);
			bg2.add(hard);
			bg.add(sp);
			bg.add(mp);
			close.addActionListener(this);
			icon.addActionListener(this);
			start.addActionListener(this);
			mp.addActionListener(this);
			sp.addActionListener(this);
			pl1.addActionListener(this);
		//	pl11.addActionListener(this);
		///	pl2.addActionListener(this);
			card.next(bottom);
			//dragListener drag = new dragListener(this);
			//pl11.requestFocus();
		
		}
		
		/*
		 * This function is created so that it observes the actions performed by the user on the menu 
		 * If the user selects the color and the level and selects the start button then this function starts the game depending on the users input
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			Object com = e.getSource();
			if (com == close) {
				System.exit(0);
			} else if (com == icon) {
				this.setExtendedState(JFrame.ICONIFIED);
			}  else if (com == start && easy.isSelected() && mp.isSelected()) {
				levl=1;
				start(Camelot.EASY,Camelot.BLACK);
			}  else if (com == start && med.isSelected() && mp.isSelected()) {
				levl=2;
				start(Camelot.MED,Camelot.BLACK);
				
			}else if (com == start && hard.isSelected() && mp.isSelected()) {
				levl=4;
				start(Camelot.HARD,Camelot.BLACK);
			}
			 else if (com == start && easy.isSelected() && sp.isSelected()) {
				 levl=1;
					start(Camelot.EASY,Camelot.WHITE);
				}  else if (com == start && med.isSelected() && sp.isSelected()) {
					levl=2;
					start(Camelot.MED,Camelot.WHITE);
				}else if (com == start && hard.isSelected() && sp.isSelected()) {
					levl=4;
					start(Camelot.HARD,Camelot.WHITE);
				}
		}

		protected void start(int lvl,int clr) {
			this.dispose();
			System.gc();
			new Camelot(lvl,clr);
		}
	}

