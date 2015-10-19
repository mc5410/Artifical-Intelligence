/**
 * This class is created with the main purpose of installing start , close and minimize the buttons on the Menu panel
 */

package mini_camelot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;

public class ButtonGui extends AbstractButton implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9150583646553377769L;
	boolean ison;
	String text;
	ActionEvent evt;
	BasicStroke bs = new BasicStroke(5);
	GradientPaint gp = new GradientPaint(0, 0, Color.orange, 25, 50,
			Color.yellow, true);
	GradientPaint gp1 = new GradientPaint(0, 0, Color.green, 25, 50,
			Color.black, true);
	Font f = new Font("Arail", Font.BOLD, 12);
	FontMetrics fm = /* Toolkit.getDefaultToolkit(). */getFontMetrics(f);
	int offset = 0, sw;

	public ButtonGui(String tex) {
		text = tex;
		sw = fm.stringWidth(text) / 2;
		evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text);
		this.addMouseListener(this);
		setVisible(true);
	}

	public void paint(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		g.setPaint(gp);
		g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
		g.setPaint(gp1);
		if (ison) {
			g.setStroke(bs);
			g.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 20, 20);
		}
		g.setFont(f);
		g.drawString(text, this.getWidth() / 2 - sw + offset, getHeight() / 2
				+ 5 + offset);
	}

	public void setText(String tex) {
		text = tex;
		sw = fm.stringWidth(text) / 2;
		evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text);
		repaint();
	}

	public void setFont(Font fo) {
		f = fo;
		fm = /* Toolkit.getDefaultToolkit(). */getFontMetrics(f);
		sw = fm.stringWidth(text) / 2;
	}

	public Font getFont() {
		return f;
	}

	public String getText() {
		return text;
	}

	public void mouseEntered(MouseEvent e) {
		ison = true;
		fireStateChanged();
		repaint();
	}

	public void doClick() {
		this.fireActionPerformed(evt);
	}

	public void mouseExited(MouseEvent e) {
		ison = false;
		repaint();
	}

	public void mouseReleased(MouseEvent e) {
		offset = 0;
		this.fireActionPerformed(evt);
		repaint();
	}

	public void mousePressed(MouseEvent e) {
		offset = 5;
		repaint();
	}

	public void mouseClicked(MouseEvent e) {
	}
}
