import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;


class Unit{                                                     //マップのセルのクラス
    public int x, y;
    public int status =  -100;                                                 //statu = 0:障害物,　 statu = 1:空地, 　statu = 2:死亡エリア（爆弾の余波）, status = 3:　プレイヤー
    public void getStatus(){
        return status;
    }
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void getX() {
        return this.x;
    }
    public void getY(){
        return this.y;
    }
}

class Player extends Unit{
    int speed = 1;                  //　プレイヤーの初期速度
    int bomb_num = 3;               //　持っている爆弾の数
    int power = 2;                  //爆弾の最初の威力は2マス
    public int setBomb(){           //　爆弾を持っていれば、設置可能
        if(bomb_num > 0){
            bomb_num--;
            return 1;
        }else{
            return 0;
        }
    }
    public static Player{ 
        this.status = 3;
    }
}

class Boomb extends Player{          // class b = new Bomb(range);
    int timer;                      //　タイマー、２秒後爆発
    int range;
    public static Boomb( ){
        this.status = 0;            //　爆弾を置くことで障害物になった
        this.range = power;         //　爆弾の威力を設置(プレイヤーによる)
    }
    
    private void explode() {        // 爆発したら自己壊滅
        this.bomb_num++;            // 　プレイヤーの爆弾の持つ数が１回復する

    }
}

class MoveByKeyPanel extends JPanel implements KeyListener{
    private int x=200, y=200;                                   //座標
    private int xbomb[] = new int[100];                         //爆弾のx座標で初期値0
    private int ybomb[] = new int[100];                         //爆弾のy座標で初期値0
    private int move = 40;                                      //移動距離
    private int r = 30;
    private int i = 0;                                          //爆弾の配列の添字
    public MoveByKeyPanel(){
	this.setBackground(Color.white);
	this.setFocusable(true);
	this.addKeyListener(this);
    }
    protected void paintComponent(Graphics g){
	super.paintComponent(g);
	g.setColor(Color.BLUE);                                     //主人公は青色
	g.fillOval(x, y, r, r);
	g.setColor(Color.RED); //爆弾は赤色
	for(int j = 0;j <100;j++){  
	    g.fillOval(xbomb[j], ybomb[j], r, r);
	}                        //爆弾を１００個まで生成
   }
   public void keyPressed(KeyEvent e){
      int k = e.getKeyCode();
      switch(k){
        case KeyEvent.VK_RIGHT:
          x = x+move; 
          break;
        case KeyEvent.VK_LEFT:
          x = x-move; 
          break;
        case KeyEvent.VK_DOWN:
          y = y+move;
          break;
        case KeyEvent.VK_UP:
          y = y-move; 
          break;
        case KeyEvent.VK_SPACE: //爆弾を置く
	    xbomb[i] = x;
	    ybomb[i] = y;
	    i++;
	  break;
      }
      repaint();
   }
   public void keyTyped(KeyEvent e){ 
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
   public void keyReleased(KeyEvent e){ }
}

class MoveByKeyFrame extends JFrame {
  public MoveByKeyFrame(){
    this.setTitle("MoveByKey Frame");
    this.setSize(500,500);
    this.add(new MoveByKeyPanel());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }
  public static void main(String argv[]) {
    new MoveByKeyFrame();
 }
}
