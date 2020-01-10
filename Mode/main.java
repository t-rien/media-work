import java.awt.*;
import java.util.TimerTask; 
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;


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


class Player extends Unit{
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
      this.status = 3;
    }
    public void showStatus(){
      System.out.println("This play's status:");
      System.out.println("speed:"+speed+"power:"+power+"bomb_num:"+bomb_num);
    }
    public void move(){            // destin:　移動先
      
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
    try {
      Thread.sleep(2600);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("After exploding, number of player's bombnum="+player.bomb_num);
    for(int i=0;i<25;i++){
      for(int j=0;j<25;j++){
        System.out.print(map[i][j].getStatus()+" ");
      }
      System.out.println("");
    }
  }

  public void operateBomb(int cmd){
    for(int i=1;i <= range;i++){          //　爆弾の　及ぶ範囲をcmdに塗り替える
      if(this.x-i < 25){
        map[this.x+i][this.y].setStatus(cmd);
      }
      if(this.x-i >= 0){
        map[this.x-i][this.y].setStatus(cmd);
      }
      if(this.y+i < 25){
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
    for(int i=0;i<25;i++){
      for(int j=0;j<25;j++){
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

class main {
  public static void setMap(Unit[][] map){
    for(int i=0;i<25;i++){
      for(int j=0;j<25;j++){
        map[i][j] = new Unit();
        map[i][j].setXY(i, j);
      }
    }
    for(int i=0;i<25;i++){                  //　マップの外側を壁にする　 status = 0
      map[0][i].setStatus(0);
      map[24][i].setStatus(0);              
      map[i][0].setStatus(0);
      map[i][24].setStatus(0);
    }
  }

  public static void createModel(){
    Unit[][] map = new Unit[25][25];
    Player p = new Player(5,5);               // 　プレイヤーを（５，５）で生成
    //p.bomb_num = 0;
    setMap(map);                              //　マップを生成する、枠が壁
    // if(map[x+1][y].getStatus != 0){        // 壁でなければ、移動できる。 
    //    move                              
    // }
    Boomb boomb = new Boomb(map, p);
  }
  public static void main(String argv[]) {
    createModel();
  }
}

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
