/*
    addStatusを追加し、元のステータスを変更する方式の他、ステータスを「重ねる」という方式を追加した。
    ex:プレイヤーがUnit[1][1]からUnit[2][1]に移動するときに、Unit[1][1]から自分のステータス8を引いて(9->1)、
    Unit[2][1]のステータスに自分のステータス8を重ねる(1->9)
    0:障害物（移動不可）,　1:空地（移動可能）, 2:壊せる壁:wall,
    4:死亡エリア（爆弾の余波), 8:プレイヤー 1, 16:爆弾
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.TimerTask; 
import java.util.Arrays;



class Size_xy {
    private int x, y;
    
    public Size_xy() {
	x = 15; // 15x13
	y = 13;
    }

    public int xis() {
	return x;
    }
    public int yis() {
	return y;
    }
}

class StartGame {
    public static void main(String args[]) {
	MainFrame mf = new MainFrame();
	//mf.add(new SubPanel());
	mf.setVisible(true);
	mf.startPaint();
    }
}
	

class MainFrame extends JFrame implements Runnable {
    private MainPanel mp;
    private SubPanel sp;
    private Size_xy size;
    private Thread th = null;
    
    public MainFrame() {
	//mp = new MainPanel();
	sp = new SubPanel(mp);
	size = new Size_xy();
	//mp.add(sp);
	this.setSize(1600, 1200);
	//this.add(mp); //
	this.setLayout(null); // no layout specified. This layout -> absolute coordinate.
	sp.setBounds(75, 50, size.xis()*75+1, size.yis()*75+1);
	this.add(sp);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setVisible(true);
    }

    public synchronized void startPaint() {
	if(th == null) {
	    th = new Thread(this);
	    th.start();
	}
    }
    public synchronized void stopPaint() {
	if(th != null) {
	    th = null;
	}
    }
    public void run() {
	while(th != null) {
	    try {
		Thread.sleep(50);
		repaint();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }

    /*
    public static void main(String argv[]) {
	new MainFrame();
    }
    */
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
    private Thread th = null;
    
    
    public SubPanel(MainPanel mp) {
	//P = new Player(); // use this if model completes
	//this.mp = mp;
	//bflag = false;
	//px = 1;  // 1~23まで
	//py = 1;  // 1~23まで
	move = 1; // 1
	
	map = new Unit[15][13];
	setMap(map);
	player = new Player(1, 1, map[1][1]); // マップの(1,1)にプレイヤーを生成
	System.out.println("Player:" + player.getX() + player.getY());
	//-----------------------------------------------------------------
	picon = new ImageIcon("./View/stop-x.png");     //変更->changed again by t(1/23)
	bicon = new ImageIcon("./View/bakudan-x.png"); // changed by t
	wall = new ImageIcon("./View/wall-x.png");
	obs = new ImageIcon("./View/obs-x.png");
	exwave = new ImageIcon("./View/bomb_explode-x.png");
	road = new ImageIcon("./View/road-x.jpg");
	
	setLayout(null); // deactivate layout manager
	setBackground(Color.white);
	this.setFocusable(true);
	this.addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	for(int i=0; i<15; i++) {
	    for(int j=0; j<13; j++) {
            if(map[i][j].getStatus() == 16 + 8 + 1) { // bomb + player + space
                g.drawImage(bicon.getImage(), i*75, j*75, this);
                g.drawImage(picon.getImage(), i*75, j*75, this);
            } else if(map[i][j].getStatus() == 16 + 1) { // bomb + space
                g.drawImage(bicon.getImage(), i*75, j*75, this);
            } else if(map[i][j].getStatus() == 8 + 1) { // player + space
                //g.drawImage(road.getImage(), i*75, j*75, this);
                g.drawImage(picon.getImage(), i*75, j*75, this);
            } else if(map[i][j].getStatus() == 4 + 1) { // explosion + space
                g.drawImage(road.getImage(), i*75, j*75, this);
                g.drawImage(exwave.getImage(), i*75, j*75, this);
            } else if(map[i][j].getStatus() == 1) { // space
                //g.drawImage(road.getImage(), i*75, j*75, this);
            } else if(map[i][j].getStatus() == 0) { // wall
                g.drawImage(wall.getImage(), i*75, j*75, this);
            }else if(map[i][j].getStatus() == 1+2) { // space + 壊せる壁
                g.drawImage(road.getImage(), i*75, j*75, this);
            }else if(map[i][j].getStatus() == 1+2+4) { // 溶けた障害物
                g.drawImage(picon.getImage(), i*75, j*75, this);
            }else{
               // System.out.println("Nothing");
            }
	    }
	}

	// -------- for test
	g.setColor(Color.black);
	for(int i = 0; i<=15; i++) // produce 10x10 squares
	    g.drawLine(i*75, 0, i*75, 15*75); // tatesen
	for(int i = 0; i<=13; i++)
	    g.drawLine(0, i*75, 13*75, i*75); // yokosen
	                            // produce 10x10 squares
    }

   public void keyPressed(KeyEvent e){       //↓変更
      int k = e.getKeyCode(); // changed int to char by t
      switch(k){
        case KeyEvent.VK_RIGHT:
	  count++;              //キーを押すたびにcountを増やして、長押ししたときにはゆっくり動く
	  if(count%5 == 1)
	      player.move(player.getX()+move, player.getY(), map);
	  picon = new ImageIcon("./View/walkr-x.jpg"); // changed by t
          break;
        case KeyEvent.VK_LEFT:
	  count++;
	  if(count%5 == 1)
	      player.move(player.getX()-move, player.getY(), map);
	  picon = new ImageIcon("./View/walkl-x.jpg"); // changed by t
          break;
        case KeyEvent.VK_DOWN:
	  count++;
	  if(count%5 == 1)
	      player.move(player.getX(), player.getY()+move, map);
	  picon = new ImageIcon("./View/walkd-x.jpg"); // changed by t
          break;
        case KeyEvent.VK_UP:
	  count++;
	  if(count%5 == 1)
	      player.move(player.getX(), player.getY()-move, map);
	  picon = new ImageIcon("./View/walku-x.jpg"); // changed by t
          break;
        case KeyEvent.VK_SPACE: //爆弾を置く
	  player.setBomb(map[player.getX()][player.getY()]);
	  new Boomb(map, player, this);
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
	   picon = new ImageIcon("./View/stop-x.png");  //アイコンを戻す // changed by t
          break;
        case KeyEvent.VK_LEFT:
	    count = 0;
	    picon = new ImageIcon("./View/stop-x.png"); // changed by t
          break;
        case KeyEvent.VK_DOWN:
	    count = 0;
	    picon = new ImageIcon("./View/stop-x.png"); // changed by t
          break;
        case KeyEvent.VK_UP:
	    count = 0;
	    picon = new ImageIcon("./View/stop-x.png"); // changed by t
          break;
      }
      repaint();
    }
    public void setMap(Unit[][] map){
	for(int i=0;i<15;i++){
	    for(int j=0;j<13;j++){
		map[i][j] = new Unit();
	    }
	}
	for(int i=0;i<15;i++){                  //　マップの外側を壁にする　 status = 0              
	    map[i][0].setStatus(0);
	    map[i][12].setStatus(0);
    }
    
    for(int i=4;i<6;i++){                   //壊せる壁を設置
        map[i][i].setStatus(2+1);
    }
    map[5][4].setStatus(2+1);
	for(int i=0;i<13;i++){
	    map[0][i].setStatus(0);
	    map[14][i].setStatus(0);
	}
    }
} 

/* ---------------------------------------------------------------------------------------*/


class Unit{                                                     //マップのセルのクラス
    public int x, y;
    public int status =  1;                                     //0:障害物（移動不可）,　1:空地（移動可能）, 2:プレイヤー２（未定）、
    //ImageIcon img;                                            //4:死亡エリア（爆弾の余波), 8:プレイヤー 1, 16:爆弾
    public Unit(int x, int y){
      this.x = x; this.y = y;
    }
    public Unit(){ }
    public int getStatus(){
        return status;
    }
    public void setStatus(int s){
      if(this.status != 0) this.status = s;                               //　壁の状態は変えられない
    }
    public void addStatus(int s){                                         //　スタータスを重ねる: 4+8 = 余波に立つプレイヤー
      if(this.status != 0) this.status += s;                               //　壁の状態は変えられない
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
    if(desti.getStatus() == 0 || desti.getStatus() == 3){
      return false;
    }else{
      return true;
    }
  }
}

class Player extends Unit{
    //private int x, y; // added by t.
    int speed = 1;                  //　プレイヤーの初期速度
    int bomb_num = 3;               //　持っている爆弾の数
    int power = 2;                  //爆弾の最初の威力は2マス
    //ImageIcon img;
    
    public int setBomb(Unit map_ij){           //　爆弾を持っていれば、設置可能
      if(bomb_num > 0){

          return 1;
      }else{
	  //bomb_num = 0;
          return 0;
      }
    } 
    public Player(int x, int y, Unit map_ij){    //プレイヤーを生成するときに、座標を与える
      this.x = x;
      this.y = y;
      this.status = 8;
      map_ij.setStatus(this.status + map_ij.getStatus()); // activated by t(2/1/20:50)
    }
    public void reduceBomb() {
	    bomb_num--;
    }
    public void showStatus(){
      System.out.println("This play's status:");
      System.out.println("speed:"+speed+"power:"+power+"bomb_num:"+bomb_num);
    }
    public void move(int dest_x, int dest_y, Unit[][] map){            // destin:　移動先
      if(map[dest_x][dest_y].getStatus() != 0 && map[dest_x][dest_y].getStatus() != 3){
	  map[dest_x][dest_y].addStatus(this.status); // added by t(2/1/21:05): 移動先のステータスに自分のステータスを重ねる
	  map[this.x][this.y].addStatus(-this.status); // added by t(same as above): プレイヤが元いた場所はステータスを自分のステータスを引く
	  
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
  SubPanel sp;
  java.util.Timer timer;
  //ImageIcon img;
  public Boomb(Unit[][] map , Player player, SubPanel sp){            // 　プレイヤーの居るところに爆弾を置く
    if(player.bomb_num == 0){       //　手に余った爆弾がなければ、ボンブを置くのを中止する
      System.out.println("Not bomb left, stop creating bomb");
      return;
    }else{
        player.reduceBomb();    //　爆弾を置くことで、プレイヤーの持つ爆弾が１減る
        System.out.println(player.bomb_num);
    }
    this.player = player;
    map[player.getX()][player.getY()].addStatus(16);// 爆弾のステータスを重ねる
    sp.repaint();

    this.range = player.power;         //　爆弾の威力を設置(プレイヤーによる)
    this.map = map;
    this.x = player.getX(); this.y = player.getY();
    sp.repaint(); // added by t(2/1/21:35)
    System.out.println("Player Set Bomb at:" + x + "," + y);
    timer = new java.util.Timer(true);
    timer.schedule(this, 2000);
    System.out.println("After exploding, number of player's bombnum="+player.bomb_num);
    sp.repaint();
    for(int i=0;i<15;i++){
      for(int j=0;j<13;j++){
        System.out.print(map[i][j].getStatus()+" ");
      }
      System.out.println("");
    }
  }

    public void operateBomb(int cmd, int x, int y, int flag){
      Unit temp = new Unit();
    // for(int i=1;i <= range;i++){          //　爆弾の　及ぶ範囲をcmdのステータスを重ねる
    //   if(this.x+i < 15){
    //     map[this.x+i][this.y].addStatus(cmd);
    //   }
    //   if(this.x-i >= 0){
    //     map[this.x-i][this.y].addStatus(cmd);
    //   }
    //   if(this.y+i < 13){
    //     map[this.x][this.y+i].addStatus(cmd);
    //   }
    //   if(this.y-i >= 0){
    //     map[this.x][this.y-i].addStatus(cmd);
    //   }
    // }
    for(int i=1;i <= range;i++){
        if(this.x+i < 15) {
            map[this.x+i][this.y].addStatus(cmd);
            if( map[this.x+i][this.y].getStatus() == 3+4){             // 壊せる壁までしか、爆弾の余波が及ばない
                break;
            }else if(map[this.x+i][this.y].getStatus() == 7-4){        // 壊せる壁までしか、爆弾の余波を戻しかつ空地にする
                map[this.x+i][this.y].setStatus(1);
                break;
            }   
        }
    }
    for(int i=1;i <= range;i++){
        if(this.x+i < 15) {
            map[this.x-i][this.y].addStatus(cmd);
            if( map[this.x-i][this.y].getStatus() == 3+4){             // 壊せる壁までしか、爆弾の余波が及ばない
                break;
            }else if(map[this.x-i][this.y].getStatus() == 7-4){        // 壊せる壁までしか、爆弾の余波を戻しかつ空地にする
                map[this.x-i][this.y].setStatus(1);
                break;
            }   
        }
    }
    for(int i=1;i <= range;i++){
        if(this.x+i < 15) {
            map[this.x][this.y+i].addStatus(cmd);
            if( map[this.x][this.y+i].getStatus() == 3+4){             // 壊せる壁までしか、爆弾の余波が及ばない
                break;
            }else if(map[this.x][this.y+i].getStatus() == 7-4){        // 壊せる壁までしか、爆弾の余波を戻しかつ空地にする
                map[this.x][this.y+i].setStatus(1);
                break;
            }   
        }
    }
    for(int i=1;i <= range;i++){
        if(this.x+i < 15) {
            map[this.x][this.y-i].addStatus(cmd);
            if( map[this.x][this.y-i].getStatus() == 3+4){             // 壊せる壁までしか、爆弾の余波が及ばない
                break;
            }else if(map[this.x][this.y-i].getStatus() == 7-4){        // 壊せる壁までしか、爆弾の余波を戻しかつ空地にする
                map[this.x][this.y-i].setStatus(1);
                break;
            }   
        }
    }
    if(flag  == 1)
      map[x][y].setStatus(5); // 爆弾が爆発うしたので、自体が余波になる
    else
      map[x][y].setStatus(1);
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
      operateBomb(4, this.x, this.y, 1);                       // 爆弾の及ぶ範囲4（爆弾の余波）を重ねる。この間にプレイヤーがstatus＝２のところで立ったら死亡。第4引数が1なら爆弾自体のステータスを設定
    System.out.println("Start exploding, number of player's bombnum = "+player.bomb_num);
    for(int i=0;i<15;i++){
      for(int j=0;j<13;j++){
        System.out.print(map[i][j].getStatus()+" ");
      }
      System.out.println("");
    }

    //　need repaint ?
    //　ここで一度死亡判定
    //　 bomb man　の座標を取って、もしその座標の statusが２、
    //　つまり爆弾の余波にいたら、死亡判定する。Viewで Game Overを表示する？

    exlodeInterval(500);     //　爆弾の余波は0.5秒続く         
    operateBomb(-4, this.x, this.y, 0);          //　爆弾の爆発が終わって、死亡エリア解除、重ねたstatus＝4を引く, 第4引数が１なら爆弾をoperateBombの最後で爆弾のステータスを設定、0なら設定しない
    player.addBomb();
  }
}
