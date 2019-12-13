import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;


class Unit{                                                     //マップのセルのクラス
    public int x, y;
    public int status =  1;                                                 //statu = 0:障害物（移動不可）,　 statu = 1:空地（移動可能）, 　statu = 2:死亡エリア（爆弾の余波)
    
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
}

class Boomb extends TimerTask{          // class b = new Bomb(range);
    //running timer task as daemon thread
    Timer timer = new Timer(true);
    int range;
    public void Boomb( ){
        this.range = 2;         //　爆弾の威力を設置(プレイヤーによる)
        timer.schedule(this, 2000); //explodes in 2 seconds
    }
    
    private void explode(Unit Map[][]) {        // 爆発
        //this.bomb_num++;                    // 　プレイヤーの爆弾の持つ数が１回復する
        for(int i=1;i <= range;i++){         //　爆弾の爆発したところは２に塗り替える、プレイヤーがstatus＝２のところで立ったら死亡
          Map[this.x+i][this.y].setStatus(2);
          Map[this.x-i][this.y].setStatus(2);
          Map[this.x][this.y].setStatus(2);
          Map[this.x][this.y].setStatus(2);
        }
        Thread.sleep(500);                    //　爆弾の余波は0.5秒続く
        for(int i=1;i <= range;i++){          //　爆弾の爆発が終わって、死亡エリア解除
          Map[this.x+i][this.y].setStatus(1);  
          Map[this.x-i][this.y].setStatus(1);
          Map[this.x][this.y].setStatus(1);
          Map[this.x][this.y].setStatus(1);
        }
    }
    public void run() {
      explode();
  }
}

class main {
  //Timer timer = new Timer();
  //timer.schedule(this, 2000);
  public static void main(String argv[]) {
    Unit[][] Map = new Unit[100][100];
    for(int i=0;i<100;i++){
      for(int j=0;j<100;j++){
        Map[i][j] = new Unit();
        Map[i][j].setXY(i, j);
      }
    }
    System.out.println(Map[50][70].getX());
  }
}
