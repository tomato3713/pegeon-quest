import javax.swing.*;
import java.util.TimerTask;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.util.Observable;
import java.net.URL;

// 鳩を定義するクラス
class PegeonClass extends Observable implements ActionListener {
    // *Effect が true のときに、エフェクトの文字を表示する
    private boolean changeEffect = false; // 進化時のエフェクト
    private boolean beamEffect = false; // ビームのエフェクト
    private boolean foodVisible = false; // 餌を食べるときのエフェクト

    // 進化、ビームのエフェクトの持続を管理する
    private Timer evolutionTimer; // 進化時のエフェクトの持続時間を管理
    private Timer beamTimer; // ビームの持続時間を管理
    private Timer foodTimer; // 餌を食べるエフェクトの持続時間を管理

    // 鳩を定期的に動かすためのタイマー
    private Timer moveTimer;

    // 名前を表示するための Label
    private JLabel nameLabel;

    // 音声を再生するためのオブジェクト
    private soundThread crowsSound; // 鳴き声
    private soundThread beamSound; // ビーム
    private soundThread evolutionSound; // 進化
    private soundThread foodSound; // おいしい
    private soundThread namedSound; // 名前をつけてくれてありがとう

    // 鳩ビーム用画像
    private Image beam_img;
    // 餌の画像
    private Image food_img;

    // 鳩の状態を管理するオブジェクト
    private State state;

    // reset() は鳩の状態をリセットするためのメソッド
    public void reset() {
        this.state = new State();
    }

    //　鳩の状態を管理するためのクラス - 餌を食べた量、進化状態等
    public class State{
        // 鳩の位置を決定する変数
        private double t;
        // 餌をやった量を表示するかを決める。表示するときは true
        private boolean visible = true;
        // 現在の種類 下記の文字列で管理
        //                  -- normal
        //                   |-----> javako ( Java )
        //                   |-----> nagaashigoso ( Report )
        //                   |-----> yasokukku ( Food )
        public String kind = "normal";
        // 各餌の上げた回数
        private int java, food, report;
        // 各餌の進化するために必要な餌の数
        private final int javaLimit = 5, reportLimit = 4, foodLimit = 3;
        // 鳩の名前
        private String name;
        // 鳩の画像
        private Image img;
        State() {
            // 初期値を設定
            this.java = 0;
            this.food = 0;
            this.report = 0;
            this.name = "";
            this.t = 0;
            this.setImg("pegeon_small.png");
        }
        // draw() は餌を食べた量を描画するメソッド
        private void draw(Graphics g, int x, int y) {
            if ( visible ) {
                g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 32));
                g.setColor(Color.black);
                g.drawString("Java:" + this.java, x, y);
                g.drawString("Food:" + this.food, x, y + 35);
                g.drawString("Report:" + this.report, x, y + 35 * 2);
            }
        }
        // 画像の読み込みは渡されたファイル名をimgディレクトリ以下から探す
        public void setImg(String fname) {
            URL url = getClass().getResource("/img/" + fname);
            try {
                this.img = Toolkit.getDefaultToolkit().getImage(url);
            } catch (Exception e) {
                // 画像ファイルが存在しない。
                // 読み込み不可能なファイル形式である可能性がある。
                e.printStackTrace();
            }
        }
    }
    // getState() は鳩の状態を表すオブジェクトを返す
    public State getState() { return state; }

    // food() は餌を与え、上限を超えたら鳩を進化させる
    // 引数によって餌の種類を指定する - 1: Java ファイル, 2: レポート, 3: 食パン
    public void food(int feednum) {
        // 餌番号を受け取って、受け取った番号に対応する変数に＋１する
        // *Limitを超えたら、進化させる。
        // すでに進化しているのなら、進化させない。

        // 餌を与える前の状態
        String pre = this.state.kind;

        // 与える餌の名称
        String foodFname = "";

        // まず、食べた餌の量をインクリメントする
        // 餌の画像を更新する
        if( feednum == 1 ) {
            this.state.java++;
            foodFname = "javaesa.jpg";
        } else if( feednum == 2){
            this.state.report++;
            foodFname = "reportesa.jpg";
        } else if( feednum == 3 ){
            this.state.food++;
            foodFname = "esa.png";
        }
        // 未進化のとき
        if( state.kind == "normal" ) {
            // 進化するかを判定
            // 進化する場合は、鳩の画像、進化状態の変更と進化エフェクトをオンにする
            if (this.state.java >= state.javaLimit) {
                this.state.setImg("javako.png"); this.changeEffect = true;
                state.kind = "javako";
            }
            if (this.state.food >= state.foodLimit) {
                this.state.setImg("yasokukku.png"); this.changeEffect = true;
                state.kind = "yasokukku";
            }
            if (this.state.report >= state.reportLimit) {
                this.state.setImg("nagaashigoso.png");  this.changeEffect = true;
                state.kind = "nagaashigoso";
            }
            if( state.kind != "normal" ){
                // 進化していた場合は、進化エフェクトを停止するためのタイマーを開始する
                evolutionTimer = new Timer(800, this);
                evolutionTimer.start();
            }
        }

        // 餌の画像ファイルを読み込む
        try {
            URL url = getClass().getResource("/img/" + foodFname);
            this.food_img = Toolkit.getDefaultToolkit().getImage(url);
        } catch (Exception e) {
            // 画像ファイルが存在しない。
            // 読み込み不可能なファイル形式である可能性がある。
            e.printStackTrace();
        }

        // 餌の画像が表示されるようにする
        this.foodVisible = true;
        // 餌の表示時間を設定する
        this.foodTimer = new Timer(1000, this);
        this.foodTimer.start();

        // 進化時の音声と餌を食べたときの音声が同時に再生されないようにする
        if ( pre == this.state.kind ) {
            Thread sound = new Thread(this.foodSound);
            sound.start();
        } else {
            // 進化時は別の音声を再生する
            Thread sound = new Thread(this.evolutionSound);
            sound.start();
        }
    }

    PegeonClass(int x, int y) {
        this.state = new State();
        this.crowsSound = new soundThread("poppoo.wav");
        this.beamSound = new soundThread("fm_shot4.wav");
        this.evolutionSound = new soundThread("evolution.wav");
        this.foodSound = new soundThread("delicious.wav");
        this.namedSound = new soundThread("thanks.wav");

        // 鳩ビーム用画像の読み込み
        URL url = getClass().getResource("/img/yasokukku-beam.png");
        try {
            this.beam_img = Toolkit.getDefaultToolkit().getImage(url);
        } catch (Exception e) {
            // 画像ファイルが存在しない。
            // 読み込み不可能なファイル形式である可能性がある。
            e.printStackTrace();
        }

        // 鳩の再描画を支持するためのタイマーを開始する
        this.moveTimer = new Timer(15, this);
        this.moveTimer.start();
    }
    // getter
    // getX(), getY() は現在の座標を返すメソッド
    public int getX() { return Math.abs((int)(400 * Math.cos(this.state.t) + 100)); }
    public int getY() { return Math.abs((int)(400 * Math.sin(this.state.t) + 50 )); }
    // getName() は鳩の名前を返すメソッド
    public String getName() { return this.state.name; }

    // setter
    // setName() は鳩の名前を設定するメソッド
    public void setName(String name) {
        Thread sound = new Thread(this.namedSound);
        sound.start();
        this.state.name = name;
    }

    // draw() は鳩に関係するすべてのものの描画を管理するメソッド
    public void draw(Graphics g) {
        if( this.beamEffect == true ) {
            // 鳩ビーム時は他のエフェクトは描画しない
            drawPegeonbeam(g);
        } else {
            // 鳩を動かす
            int postX = this.getX();
            // 餌が表示されるときは動きをゆっくりにする。
            if( !foodVisible ) {
                if( this.state.t > Math.PI * 2) { this.state.t = 0; }
                else { this.state.t += 0.005; }
            } else {
                if( this.state.t > Math.PI * 2) { this.state.t = 0; }
                else { this.state.t += 0.002; }
            }

            // 鳩が進む方向に鳩の頭を向ける
            if ( this.getX() > postX ) {
                // 右向きに進むとき
                // 左右反転させて描画
                int width = this.state.img.getWidth(null);
                int height = this.state.img.getHeight(null);
                g.drawImage(this.state.img, this.getX(), this.getY(), -1 * width, height, null);

                if( this.foodVisible ) {
                    g.drawImage(food_img, this.getX() + 50, this.getY() - 20, null);
                }

            } else {
                // 左向きに進むとき
                g.drawImage(this.state.img, getX(), getY(), null);
                if( this.foodVisible ) {
                    g.drawImage(food_img, this.getX() - 100, this.getY(), null);
                }
            }

            if( getName() != null ) {
                // もし鳩に名前がつけられていれば実行
                // 鳩の名前を描画
                g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 32));
                g.setColor(Color.black);
                g.drawString(this.state.name, this.getX() + 50, this.getY() + 10); // draw pegeon name
            }

            // 進化のエフェクトを描画
            if( this.changeEffect ) { evolution(g); }

            // 状態を表示
            state.draw(g, getX() + 50, getY() + 40);
        }
    }
    // crow() は鳩の鳴き声を鳴らすメソッド
    public void crow() {
        Thread sound = new Thread(this.crowsSound);
        sound.start();
    }

    // evolution() は進化時のエフェクトを描画するメソッド
    private void evolution(Graphics g) {
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 120));
        g.drawString("EVOLUTION!!", 20, 100);
    }

    // drawPegeonbeam() は鳩ビームのエフェクトを描画するメソッド
    private void drawPegeonbeam(Graphics g) {
        // 鳩ビームの画像を描画
        g.drawImage(this.beam_img, 100, 200, null);

        // write string
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 80));
        g.drawString("（・⊝・）BEAM!!", 80, 250);
    }

    // beam() は鳩ビーム開始用メソッド
    // もしyasokukku以外なら、falseを返す
    public boolean beam() {
        if ( this.state.kind == "yasokukku" ) {
            this.beamEffect = true;
            this.beamTimer = new Timer(1000, this);
            this.beamTimer.start();

            Thread sound = new Thread(this.beamSound);
            sound.start();
            return true;
        }
        return false;
    }

    // actionPerformed() はタイマーからの通知を受け取って対応する処理を行うメソッド
    public void actionPerformed(ActionEvent e) {
        if( e.getSource().equals(beamTimer) ){
            // ビームエフェクトの描画を終了する
            // System.out.println("beamTimer off");
            this.beamEffect = false;
            this.beamTimer.stop();
        } else if ( e.getSource().equals(evolutionTimer) ) {
            // 進化エフェクトの描画を終了する
            // System.out.println("evolutionTimer off");
            this.changeEffect = false;
            this.evolutionTimer.stop();
        } else if ( e.getSource().equals(foodTimer)) {
            // 餌の描画を終了する
            this.foodVisible = false;
        }
        // タイマーから呼び出された時点でオブザーバーに通知を行う
        setChanged();
        notifyObservers();
    }
}
