import java.awt.*;
import java.util.TimerTask; 
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;


class Unit{                                                     //マップのセルのクラス
    public int x, y;
    public int status =  1;                                                 //statu = 0:障害物（移動不可）,　 statu = 1:空地（移動可能）, status = 2:死亡エリア（爆弾の余波)
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
    public void Player(){ 
        this.status = 3;
    }
    public void showStatus(){
      System.out.println("This play's status:");
      System.out.println("speed:"+speed+"power:"+power+"bomb_num:"+bomb_num);
    }
    public void move(Unit destin){            // destin:　移動先
      
    }
}

class Boomb extends TimerTask{
  int x, y;
  int range;
  Unit[][] map;
  java.util.Timer timer;
  //ImageIcon img;
  public Boomb(Unit[][] map, int x, int y){            // give coordinate when set a bomb
    this.range = 2;         //　爆弾の威力を設置(プレイヤーによる)
    this.map = map;
    System.out.println("Set Bomb at:" + x + "," + y);
    this.x = x; this.y = y;
    timer = new java.util.Timer(true);
    timer.schedule(this, 2000);
    try {
      Thread.sleep(2600);                 //　爆弾の余波は0.5秒続く
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("After exploding");
    for(int i=0;i<25;i++){
      for(int j=0;j<25;j++){
        System.out.print(map[i][j].getStatus()+" ");
      }
      System.out.println("");
    }
  }

  public void run() {                     // explode
    //this.bomb_num++;                    // 　プレイヤーの爆弾の持つ数が１回復する
    for(int i=1;i <= range;i++){          //　爆弾の爆発したところは２に塗り替える、プレイヤーがstatus＝２のところで立ったら死亡
      map[this.x+i][this.y].setStatus(2);
      map[this.x-i][this.y].setStatus(2);
      map[this.x][this.y+i].setStatus(2);
      map[this.x][this.y-i].setStatus(2);
    }
    System.out.println("Start exploding");
    for(int i=0;i<25;i++){
      for(int j=0;j<25;j++){
        System.out.print(map[i][j].getStatus()+" ");
      }
      System.out.println("");
    }
    //need repaint ?
    //　死亡判定
    try {
      Thread.sleep(500);                 //　爆弾の余波は0.5秒続く
    } catch (InterruptedException e) {
      e.printStackTrace();
    }                 
    for(int i=1;i <= range;i++){          //　爆弾の爆発が終わって、死亡エリア解除
      map[this.x+i][this.y].setStatus(1);  
      map[this.x-i][this.y].setStatus(1);
      map[this.x][this.y+i].setStatus(1);
      map[this.x][this.y-i].setStatus(1);
    }
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
    setMap(map);                               //　マップを生成する、枠が壁
    // if(map[x+1][y].getStatus != 0){        // 壁でなければ、移動できる。 
    //    move                              
    // }
    Boomb boomb = new Boomb(map, 10, 10);
  }
  public static void main(String argv[]) {
    createModel();
  }
}


