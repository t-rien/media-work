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
    private ImageIcon bicon;
    private ImageIcon picon;           //変更->changed again by takezaki
    private int px, py, bx, by, move; //insted of x, y, speed in Player p.
    private boolean bflag;
    private int count = 0;
    
    public SubPanel(MainPanel mp) {
	//P = new Player(); // use this if model completes
	this.mp = mp;
	bflag = false;
	px = 10;
	py = 10;
	move = 100;
	picon = new ImageIcon("./stop-w.png");     //変更->changed again by t(1/23)
	bicon = new ImageIcon("./bakudan-w.png"); // changed by t
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

   public void keyPressed(KeyEvent e){       //↓変更
      int k = e.getKeyCode(); // changed int to char by t
      switch(k){
        case KeyEvent.VK_RIGHT:
	  count++;              //キーを押すたびにcountを増やして、長押ししたときにはゆっくり動く
	  /*if(count==1)
	      repaint();// changed by takezaki(1/23)*/
	  if(count%5 == 1)
            px = px+move;
	  picon = new ImageIcon("./walkr-w.jpg"); // changed by t
          break;
        case KeyEvent.VK_LEFT:
	  count++;
	  /*if(count==1)
	      repaint();// changed by takezaki*/
	  if(count%5 == 1)
            px = px-move;
	  picon = new ImageIcon("./walkl-w.jpg"); // changed by t
          break;
        case KeyEvent.VK_DOWN:
	  count++;
	  /*if(count==1)
	      repaint(); // changed by t*/
	  if(count%5 == 1)
            py = py+move;
	  picon = new ImageIcon("./walkd-w.jpg"); // changed by t
          break;
        case KeyEvent.VK_UP:
	  count++;
	  /*if(count==1)
	    repaint(); // changed by t*/
	  if(count%5 == 1)
            py = py-move;
	  picon = new ImageIcon("./walku-w.jpg"); // changed by t
          break;
        case KeyEvent.VK_SPACE: //爆弾を置く
	  bx = px;
	  by = py;
	  bflag = true;//changed by takezaki(1/23)
          break;
      }
      repaint();
   }
   public void keyTyped(KeyEvent e){}
   public void keyReleased(KeyEvent e){    
       int k = e.getKeyCode();   
    switch(k){
       case KeyEvent.VK_RIGHT:
	   count = 0;         //キーを離したらcountが0になる
	   picon = new ImageIcon("./stop-w.png");  //アイコンを戻す // changed by t
          break;
        case KeyEvent.VK_LEFT:
	    count = 0;
	    picon = new ImageIcon("./stop-w.png"); // changed by t
          break;
        case KeyEvent.VK_DOWN:
	    count = 0;
	    picon = new ImageIcon("./stop-w.png"); // changed by t
          break;
        case KeyEvent.VK_UP:
	    count = 0;
	    picon = new ImageIcon("./stop-w.png"); // changed by t
          break;
      }
      repaint();
    }
} 
