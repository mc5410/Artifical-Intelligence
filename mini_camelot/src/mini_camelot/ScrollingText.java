/**
 * This class is implemented for the sole purpose of the Text scrolling on the Main Menu 
 * This class is used in the Main Menu class.
 */

package mini_camelot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

class ScrollingText extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	javax.swing.Timer timer;
	String message = "Tarun Rules!";
	int messagePosition = -1, messageHeight, messageWidth, charWidth, times;

	public ScrollingText(Color back, Color fore, Font fn, int cw, int time, String mesag) {
		this.setBackground(back);
		this.setForeground(fore);
		message = mesag;
		this.setFont(fn);
		FontMetrics fm = this.getFontMetrics(fn);
		messageWidth = fm.stringWidth(message);
		messageHeight = fm.getAscent();
		charWidth = cw;
		times = time;
		Dimension dddd = new Dimension(600, 75);
		setMinimumSize(dddd);
		setPreferredSize(dddd);
	}

	public void change(String m) {
		message = m;
		FontMetrics fm = this.getFontMetrics(this.getFont());
		messageWidth = fm.stringWidth(message);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString(message, getWidth() - messagePosition, getHeight() / 2
				+ messageHeight / 2);
	}

	public void start() {
		if (timer == null) {
			timer = new javax.swing.Timer(times, this);
			timer.start();
		} else {
			timer.restart();
		}
	}

	public void stop() {
		timer.stop();
	}

	public void actionPerformed(ActionEvent evt) {
		messagePosition += charWidth;
		if (getSize().width - messagePosition + messageWidth < 0) {
			messagePosition = 0;
		}
		repaint();
	}
}
