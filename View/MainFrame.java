import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class MainFrame extends JFrame {
    private MainPanel mp;
    private SubPanel sp;
    
    public MainFrame() {
	//mp = new MainPanel();
	sp = new SubPanel(mp);
	//mp.add(sp);
	this.setSize(500, 500);
	//this.add(mp); // 
	this.add(sp);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setVisible(true);
    }

    public static void main(String argv[]) {
	new MainFrame();
    }
}

class MainPanel extends JPanel {
    SubPanel sp;

    public MainPanel() {
	sp = new SubPanel(this);
	this.add(sp);
    }
}

class SubPanel extends JPanel implements KeyListener {
    //private Player p; //use this if model will have completed
    //private Boomb b; // same as comment above 
    private MainPanel mp;
    private ImageIcon icon;
    private int x, y, move; //insted of x, y, speed in Player p.

    public SubPanel(MainPanel mp) {
	this.mp = mp;
	x = 10;
	y = 10;
	move = 100;
	icon = new ImageIcon("./circle_s.jpg");
	setLayout(null); // deactivate layout manager
	setBackground(Color.white);
	this.setFocusable(true);
	this.addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	g.drawImage(icon.getImage(), x, y, this);
	//g.drawImage(icon.getImage(), p.getX(), p.getY(), this); // finally, use this

	// -------- for test
	g.setColor(Color.black);
	for(int i = 0; i<=10; i++) { // produce 10x10 squares
	    g.drawLine(i*100, 0, i*100, 1000);
	    g.drawLine(0, i*100, 1000, i*100);
	}
    }	

    public void keyPressed(KeyEvent e) {
	int k = e.getKeyCode();
	switch(k) {
	case KeyEvent.VK_RIGHT:
	    x = x + move;
	    //p.setXY(x+move, y);
	    break;
	case KeyEvent.VK_LEFT:
	    x = x - move;
	    //p.setXY(x-move, y);
	    break;
	case KeyEvent.VK_DOWN:
	    y = y + move;
	    //p.setXY(x, y+move);
	    break;
	case KeyEvent.VK_UP:
	    y = y - move;
	    //p.setXY(x, y-move);
	    break;
	case KeyEvent.VK_SPACE:
	    //b = new Bomb();
	    break;
	}
	repaint();
    }
    
    public void keyTyped(KeyEvent e) {
	char c = e.getKeyChar();
	switch(c){
	case 'f':
	    x = x+move; 
	    break;
	case 'b':
	    x = x-move; 
	    break;
	case 'd':
	    y = y+move;
	    break;
	case 'u':
	    y = y-move; 
	    break;
	}
	repaint();
    }
    
    public void keyReleased(KeyEvent e) {
	//pass
    }
}
