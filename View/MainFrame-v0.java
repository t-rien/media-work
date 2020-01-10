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
    //private Player p; //use this if model completes
    //private Boomb b; // same as comment above
    private MainPanel mp;
    private ImageIcon picon, bicon;
    private int px, py, bx, by, move; //insted of x, y, speed in Player p.
    private boolean bflag;
    
    public SubPanel(MainPanel mp) {
	//P = new Player(); // use this if model completes
	this.mp = mp;
	bflag = false;
	px = 10;
	py = 10;
	move = 100;
	picon = new ImageIcon("./circle_s.jpg");
	bicon = new ImageIcon("./bakudan.png");
	setLayout(null); // deactivate layout manager
	setBackground(Color.white);
	this.setFocusable(true);
	this.addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	if(bflag)
	    g.drawImage(bicon.getImage(), bx, by, this);
	g.drawImage(picon.getImage(), px, py, this);
	//g.drawImage(icon.getImage(), p.getX(), p.getY(), this); // finally, use this

	// -------- for test
	g.setColor(Color.black);
	for(int i = 0; i<=10; i++) { // produce 10x10 squares
	    g.drawLine(i*100, 0, i*100, 1000);
	    g.drawLine(0, i*100, 1000, i*100);
	}                            // produce 10x10 squares
    }

    public void keyPressed(KeyEvent e) {
	int k = e.getKeyCode();
	switch(k) {
	case KeyEvent.VK_RIGHT:
	    px = px + move;
	    //p.setXY(x+move, y);
	    break;
	case KeyEvent.VK_LEFT:
	    px = px - move;
	    //p.setXY(x-move, y);
	    break;
	case KeyEvent.VK_DOWN:
	    py = py + move;
	    //p.setXY(x, y+move);
	    break;
	case KeyEvent.VK_UP:
	    py = py - move;
	    //p.setXY(x, y-move);
	    break;
	case KeyEvent.VK_SPACE:
	    //b = new Bomb(p.getX(), p.getY());
	    bx = px;
	    by = py;
	    bflag = true;
	    break;
	}
	repaint();
    }
    
    public void keyTyped(KeyEvent e) {
	char c = e.getKeyChar();
	switch(c){
	case 'f':
	    px = px+move; 
	    break;
	case 'b':
	    px = px-move; 
	    break;
	case 'd':
	    py = py+move;
	    break;
	case 'u':
	    py = py-move; 
	    break;
	}
	repaint();
    }
    
    public void keyReleased(KeyEvent e) {
	//pass
    }
}
