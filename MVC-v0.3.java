import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.TimerTask; 
import java.util.Arrays;



class Size_xy {
    private int x, y;
    
    public Size_xy() {
	x = 13; // 13x9
	y = 9;
    }

    public int xis() {
	return x;
    }
    public int yis() {
	return y;
    }
}

class MainFrame extends JFrame {
    private MainPanel mp;
    private SubPanel sp;
    private Size_xy size;
    
    public MainFrame() {
	//mp = new MainPanel();
	sp = new SubPanel(mp);
	size = new Size_xy();
	//mp.add(sp);
	this.setSize(1600, 1200);
	//this.add(mp); //
	this.setLayout(null); // layout指定なし
	sp.setBounds(100, 100, size.xis()*100+1, size.yis()*100+1);
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
    //private MainPanel mp;
    private ImageIcon bicon, obs, road, wall, exwave;
    private ImageIcon picon;           //変更->changed again by takezaki
    private int px, py, bx, by, move; //insted of x, y, speed in Player p.
    private boolean bflag;
    private int count = 0;
    private Unit map[][];
    private Player player;
    private Boomb bomb;
    
    
    public SubPanel(MainPanel mp) {
	//P = new Player(); // use this if model completes
	//this.mp = mp;
	//bflag = false;
	//px = 1;  // 1~23まで
	//py = 1;  // 1~23まで
	move = 1; // 1
	
	map = new Unit[13][9];
	
	////////////////////////////////////////////////////////////////////
	for(int i=0;i<13;i++){// initialize map　あとから余裕あれば修正する
	    for(int j=0;j<9;j++){
		map[i][j] = new Unit();
		map[i][j].setXY(i, j);
	    }
	}
	for(int i=0;i<13;i++){                  //　マップの外側を壁にする　 status = 0
	    map[i][0].setStatus(0);
	    map[i][8].setStatus(0);              
	}
	for(int i=0;i<9;i++){
	    map[0][i].setStatus(0);
	    map[12][i].setStatus(0);
	}
	player = new Player(1, 1);
	System.out.println("Player:" + player.getX() + player.getY());
	//-----------------------------------------------------------------
	picon = new ImageIcon("./View/stop-w.png");     //変更->changed again by t(1/23)
	bicon = new ImageIcon("./View/bakudan-w.png"); // changed by t
	wall = new ImageIcon("./View/obs-w.png");
	obs = new ImageIcon("./View/obs-w.png");
	exwave = new ImageIcon("./View/stop-w.png");
	// road = new ImageIcon("./View/road-w.png");
	// wall = new ImageIcon("./View/wall-w.png");
	
	setLayout(null); // deactivate layout manager
	setBackground(Color.white);
	this.setFocusable(true);
	this.addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	for(int i=0; i<13; i++) {
	    for(int j=0; j<9; j++) {
		//if(map[i][j].getStatus() == 0)
		    //g.drawImage(wall.getImage(), i*100, j*100, this);
	        if(map[i][j].getStatus() == 2)
		    g.drawImage(exwave.getImage(), i*100, j*100, this);
		else if(map[i][j].getStatus() == 3)
		    g.drawImage(road.getImage(), i*100, j*100, this);
	    }
	}
	/*	
	if(bflag) {
	    g.drawImage(bicon.getImage(), bx*100, by*100, this);
	    g.drawImage(exwave.getImage(), (bx-1)*100, by*100, this);
	}*/
	//g.drawImage(bicon.getImage(), bx*100, by*100, this);
	//g.drawImage(picon.getImage(), px*100, py*100, this);
	g.drawImage(picon.getImage(), player.getX()*100, player.getY()*100, this); // finally, use this

	// -------- for test
	g.setColor(Color.black);
	for(int i = 0; i<=13; i++) // produce 10x10 squares
	    g.drawLine(i*100, 0, i*100, 900); // tatesen
	for(int i = 0; i<=9; i++)
	    g.drawLine(0, i*100, 1300, i*100); // yokosen
	                            // produce 10x10 squares
    }

   public void keyPressed(KeyEvent e){       //↓変更
      int k = e.getKeyCode(); // changed int to char by t
      switch(k){
        case KeyEvent.VK_RIGHT:
	  count++;              //キーを押すたびにcountを増やして、長押ししたときにはゆっくり動く
	  /*if(count==1)
	      repaint();// changed by takezaki(1/23)*/
	  if(count%5 == 1)
	      player.move(player.getX()+move, player.getY(), map);
	      //px = px+move;
	  //player.move(px, py, move, 0, map)
	  picon = new ImageIcon("./View/walkr-w.jpg"); // changed by t
          break;
        case KeyEvent.VK_LEFT:
	  count++;
	  /*if(count==1)
	      repaint();// changed by takezaki*/
	  if(count%5 == 1)
	      player.move(player.getX()-move, player.getY(), map);
	  //px = px-move;
	  //player.move(px-move, py, map)
	  picon = new ImageIcon("./View/walkl-w.jpg"); // changed by t
          break;
        case KeyEvent.VK_DOWN:
	  count++;
	  /*if(count==1)
	      repaint(); // changed by t*/
	  if(count%5 == 1)
	      player.move(player.getX(), player.getY()+move, map);
	  //py = py+move;
	  //
	  picon = new ImageIcon("./View/walkd-w.jpg"); // changed by t
          break;
        case KeyEvent.VK_UP:
	  count++;
	  /*if(count==1)
	    repaint(); // changed by t*/
	  if(count%5 == 1)
	      player.move(player.getX(), player.getY()-move, map);
	      //py = py-move;
	  //
	  picon = new ImageIcon("./View/walku-w.jpg"); // changed by t
          break;
        case KeyEvent.VK_SPACE: //爆弾を置く
	    //bx = px; by = py;
	    //bx = player.getX(); by = player.getY();
	    //bflag = true;//changed by takezaki(1/23)
	    new Boomb(map, player);
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
	   picon = new ImageIcon("./View/stop-w.png");  //アイコンを戻す // changed by t
          break;
        case KeyEvent.VK_LEFT:
	    count = 0;
	    picon = new ImageIcon("./View/stop-w.png"); // changed by t
          break;
        case KeyEvent.VK_DOWN:
	    count = 0;
	    picon = new ImageIcon("./View/stop-w.png"); // changed by t
          break;
        case KeyEvent.VK_UP:
	    count = 0;
	    picon = new ImageIcon("./View/stop-w.png"); // changed by t
          break;
      }
      repaint();
    }
    public void setMap(Unit[][] map){
	for(int i=0;i<13;i++){
	    for(int j=0;j<9;j++){
		map[i][j] = new Unit();
		//map[i][j].setXY(i, j);
	    }
	}
	for(int i=0;i<13;i++){                  //　マップの外側を壁にする　 status = 0              
	    map[i][0].setStatus(0);
	    map[i][8].setStatus(0);
	}
	for(int i=0;i<9;i++){
	    map[0][i].setStatus(0);
	    map[12][i].setStatus(0);
	}
    }
} 

/* ---------------------------------------------------------------------------------------*/


class Unit{                                                     //マップのセルのクラス
    public int x, y;
    public int status =  1;                                                 //0:障害物（移動不可）,　1:空地（移動可能）, 2:死亡エリア（爆弾の余波), 3:　
    //ImageIcon img;                                              
    public void Unit(int x, int y){
      this.x = x; this.y = y;
    }

    public int getStatus(){
        return status;
    }
    public void setStatus(int s){
      if(this.status != 0) this.status = s;                               //　壁の状態は変えられない
    }
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return this.x;
    }
    public int getY(){
        return this.y;
    }
}

class GameCheck{
  public boolean is_Danger(Player player, Unit[][] map){                   //　マップとプレイヤーの情報を渡す
    int x = player.getX(); int y = player.getY();
    if(map[x][y].getStatus() == 2){                                       //プレイヤーの立つところに爆弾の余波があったら、失敗
      return true;
    }else{
      return false;
    }
  }
  public boolean is_Movable(Player player, Unit desti, Unit[][] map){
    if(desti.getStatus() != 0){
      return true;
    }else{
      return false;
    }
  }
}

class Player extends Unit{
    //private int x, y; // added by t.
    int speed = 1;                  //　プレイヤーの初期速度
    int bomb_num = 3;               //　持っている爆弾の数
    int power = 2;                  //爆弾の最初の威力は2マス
    //ImageIcon img;
    
    public int setBomb(){           //　爆弾を持っていれば、設置可能
      if(bomb_num > 0){
          bomb_num--;
          return 1;
      }else{
          return 0;
      }
    } 
    public Player(int x, int y){    //プレイヤーを生成するときに、座標を与える
      this.x = x;
      this.y = y;
      //this.status = 3;
    }
    public void showStatus(){
      System.out.println("This play's status:");
      System.out.println("speed:"+speed+"power:"+power+"bomb_num:"+bomb_num);
    }
    public void move(int dest_x, int dest_y, Unit[][] map){            // destin:　移動先
      if(map[dest_x][dest_y].getStatus() != 0){
        this.x = dest_x;
        this.y = dest_y;
        //return true;
      }else{
	  System.out.println("WALLLLLLLLLLLLLLLL");
	  //return false;
      }
    }
    public void addRange(){            // destin:　移動先
      this.power++;
    }
    public void addBomb(){            // destin:　移動先
      this.bomb_num++;
    }
    public void addspeed(){            // destin:　移動先
      this.speed++;
    }
}

class Boomb extends TimerTask{                        // Viewでは ArrayListでBombを格納して処理する？
  int x, y;
  int range;                                          //　この爆弾の威力：爆弾を置く瞬間のプレイヤーのステータスで決まる
  Unit[][] map;                                       // 個々のボンブごとに爆発を処理するために、Bomb　Objectにマップを渡す
  Player player;                                      // この爆弾の”持ち主”
  java.util.Timer timer;
  //ImageIcon img;
  public Boomb(Unit[][] map , Player player){            // 　プレイヤーの居るところに爆弾を置く
    this.player = player;
    if(player.bomb_num == 0){       //　手に余った爆弾がなければ、ボンブを置くのを中止する
      System.out.println("Not bomb left, stop creating bomb");
      return;
    }
    this.range = player.power;         //　爆弾の威力を設置(プレイヤーによる)
    this.map = map;
    this.x = player.getX(); this.y = player.getY();
    System.out.println("Player Set Bomb at:" + x + "," + y);
    player.bomb_num--;                     //　爆弾を置くことで、プレイヤーの持つ爆弾が１減る
    timer = new java.util.Timer(true);
    timer.schedule(this, 2000);
    //　これ以下は結果を実証するためのテストコード
    /*
    try {
      Thread.sleep(2600);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }*/
    System.out.println("After exploding, number of player's bombnum="+player.bomb_num);
    for(int i=0;i<13;i++){
      for(int j=0;j<9;j++){
        System.out.print(map[i][j].getStatus()+" ");
      }
      System.out.println("");
    }
  }

  public void operateBomb(int cmd){
    for(int i=1;i <= range;i++){          //　爆弾の　及ぶ範囲をcmdに塗り替える
      if(this.x-i < 13){
        map[this.x+i][this.y].setStatus(cmd);
      }
      if(this.x-i >= 0){
        map[this.x-i][this.y].setStatus(cmd);
      }
      if(this.y+i < 9){
        map[this.x][this.y+i].setStatus(cmd);
      }
      if(this.y-i >= 0){
        map[this.x][this.y-i].setStatus(cmd);
      }
    }
  }

  public void exlodeInterval(int interval){
    try {
      Thread.sleep(interval);                 //　爆弾の余波は0.5秒続く
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  public void run() {                     // explode
    //this.bomb_num++;                    // プレイヤーの爆弾の持つ数が１回復する
    operateBomb(2);                       // 爆弾の及ぶ範囲を２（爆弾の余波）に塗り替える。この間にプレイヤーがstatus＝２のところで立ったら死亡。
    System.out.println("Start exploding, number of player's bombnum = "+player.bomb_num);
    for(int i=0;i<13;i++){
      for(int j=0;j<9;j++){
        System.out.print(map[i][j].getStatus()+" ");
      }
      System.out.println("");
    }

    //　need repaint ?
    //　ここで一度死亡判定
    //　 bomb man　の座標を取って、もしその座標の statusが２、
    //　つまり爆弾の余波にいたら、死亡判定する。Viewで Game Overを表示する？

    exlodeInterval(500);     //　爆弾の余波は0.5秒続く         
    operateBomb(1);          //　爆弾の爆発が終わって、死亡エリア解除、１に塗り替える
    player.bomb_num++;       //  爆弾が爆発したので、プレイヤーの手に持った爆弾の数を回復;
  }
}
/*
class main {
  public static void setMap(Unit[][] map){
    for(int i=0;i<13;i++){
      for(int j=0;j<9;j++){
        map[i][j] = new Unit();
        map[i][j].setXY(i, j);
      }
    }
    for(int i=0;i<13;i++){                  //　マップの外側を壁にする　 status = 0
      map[0][i].setStatus(0);
      map[24][i].setStatus(0);              
      map[i][0].setStatus(0);
      map[i][24].setStatus(0);
    }
  }

  public static void createModel(){
    Unit[][] map = new Unit[13][9];
    Player p = new Player(5,5);               // 　プレイヤーを（５，５）で生成
    //p.bomb_num = 0;
    setMap(map);                              //　マップを生成する、枠が壁
    // if(map[x+1][y].getStatus != 0){        // 壁でなければ、移動できる。 
    //    move                              
    // }
    Boomb boomb = new Boomb(map, p);
  }/*
  public static void main(String argv[]) {
    createModel();
  }
  }*/

 /*

a63:~/game> javac main.java
a63:~/game> java main
Player Set Bomb at:5,5
Start exploding, number of player's bombnum = 2
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 2 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 2 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 2 2 1 2 2 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 2 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 2 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 
After exploding, number of player's bombnum=3
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 

爆弾を爆裂させる動作を完成した。爆弾を置くことで、プレイヤーの手に持つ爆弾の更新も完成。
これからはアイテムシステムを作る段階に移る。

システム予想：Death　Matchの感じでいきたいと思う。
障害物を壊せて、中にあるアイテムを手に入れるより、
１０秒ごとにマップにアイテムをばら撒く感じでいきたいと思う。

 */
