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

// 鳩を定義するクラス
class PegeonClass extends Observable implements ActionListener {
    // 位置に関するもの
    private int x,y;
    // 鳩の位置を決定する変数
    private double t;

    // 画像データを格納する変数
    private Image img;

    // *Effect が true のときに、エフェクトの文字を表示する
    private boolean changeEffect = false;
    private boolean beamEffect = false;
    private boolean foodVisible = false;

    // 進化、ビームのエフェクトの持続を管理する
    private Timer evolutionTimer;
    private Timer beamTimer;
    private Timer foodTimer;

    // 鳩を定期的に動かすためのタイマー
    private Timer moveTimer;

    // 鳩の名前
    private String name;
    // 名前を表示するための Label
    private JLabel nameLabel;

    // 音声ファイル
    private soundThread crowsSound;
    private soundThread beamSound;
    // 鳩ビーム用画像
    private Image beam_img;
    private Image food_img;

    private State state;

    public void reset() {
        this.state = new State();
        this.setImg("pegeon_small.png");
    }

    //　各餌の摂取状態を管理するためのクラス
    public class State{
        // 餌をやった量を表示するかを決める。表示するときは true
        private boolean visible = true;
        // 現在の種類 -- normal
        //                   |-----> javako ( Java )
        //                   |-----> nagaashigoso ( Report )
        //                   |-----> yasokukku ( Food )
        public String kind = "normal";
        // 各餌の上げた回数
        private int java, food, report;
        // 各餌の進化するために必要な餌の数
        private final int javaLimit = 5, reportLimit = 4, foodLimit = 3;
        State() {
            this.java = 0;
            this.food = 0;
            this.report = 0;
        }
        // draw function draw the state
        private void draw(Graphics g, int x, int y) {
            if ( visible ) {
                g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 32));
                g.setColor(Color.black);
                g.drawString("Java:" + this.java, x, y);
                g.drawString("Food:" + this.food, x, y + 35);
                g.drawString("Report:" + this.report, x, y + 35 * 2);
                // g.drawRect(x, y, 130, 100);
            }
        }
    }
    public State getState() { return state; }
    // 餌番号を受け取って、受け取った番号に対応する変数に＋１する
    // *Limitを超えたら、進化させる。
    // すでに進化しているのなら、進化させない。
    public void food(int feednum) {
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
            if (this.state.java >= state.javaLimit) {
                setImg("javako.png"); this.changeEffect = true;
                state.kind = "javako";
            }
            if (this.state.food >= state.foodLimit) {
                setImg("yasokukku.png"); this.changeEffect = true;
                state.kind = "yasokukku";
            }
            if (this.state.report >= state.reportLimit) {
                setImg("nagaashigoso.png");  this.changeEffect = true;
                state.kind = "nagaashigoso";
            }
            if( state.kind != "normal" ){
                evolutionTimer = new Timer(800, this);
                evolutionTimer.start();
            }
        }

        // 画像ファイルを読み込む
        String path = new File("./img",foodFname).getPath();
        try {
            this.food_img = Toolkit.getDefaultToolkit().getImage(path);
        } catch (Exception e) {
            // 画像ファイルが存在しない。
            // 読み込み不可能なファイル形式である可能性がある。
            e.printStackTrace();
        }

        // 餌の画像が表示されるようにする
        this.foodVisible = true;
        this.foodTimer = new Timer(1000, this);
        this.foodTimer.start();
    }

    PegeonClass(int x, int y) {
        state = new State();
        this.setX(x);
        this.setY(y);
        this.setImg("pegeon_small.png");
        this.crowsSound = new soundThread("poppoo.wav");
        this.beamSound = new soundThread("fm_shot4.wav");

        this.name = null;
        this.t = 0;

        // 鳩ビーム用画像の読み込み
        String path = new File("./img", "yasokukku-beam.png").getPath();
        try {
            this.beam_img = Toolkit.getDefaultToolkit().getImage(path);
        } catch (Exception e) {
            // 画像ファイルが存在しない。
            // 読み込み不可能なファイル形式である可能性がある。
            e.printStackTrace();
        }

        moveTimer = new Timer(15, this);
        moveTimer.start();
    }
    // getter
    // 現在の座標を取得
    public int getX() { return this.x; }
    public int getY() { return this.y; }

    // setter
    // 画像の読み込みは渡されたファイル名をimgディレクトリ以下から探す
    public void setImg(String fname) {
        String path = new File("./img", fname).getPath();
        try {
            this.img = Toolkit.getDefaultToolkit().getImage(path);
        } catch (Exception e) {
            // 画像ファイルが存在しない。
            // 読み込み不可能なファイル形式である可能性がある。
            e.printStackTrace();
        }
    }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public void draw(Graphics g) {
        if( this.beamEffect == true ) {
            // 鳩ビーム時は他のエフェクトは描画しない
            drawPegeonbeam(g);
        } else {
            // 鳩を動かす
            int futureX = Math.abs((int)(400 * Math.cos(this.t) + 100));
            int futureY = Math.abs((int)(400 * Math.sin(this.t) + 50));

            // 餌が表示されるときは動きを止める。
            if( !foodVisible ) {
                if( t > Math.PI * 2) { t = 0; }
                else { t += 0.005; }
            } else {
                if( t > Math.PI * 2) { t = 0; }
                else { t += 0.002; }
            }

            // 鳩が進む方向に鳩の頭を向ける
            if ( this.getX() < futureX ) {
                // 左右反転させて描画
                int width = img.getWidth(null);
                int height = img.getHeight(null);
                this.setX(futureX); this.setY(futureY);
                g.drawImage(img, this.getX(), this.getY(), -1 * width, height, null);

                if( this.foodVisible ) {
                    g.drawImage(food_img, this.getX() + 50, this.getY() - 20, null);
                }

            } else {
                this.setX(futureX); this.setY(futureY);
                g.drawImage(this.img, getX(), getY(), null);

                if( this.foodVisible ) {
                    g.drawImage(food_img, this.getX() - 100, this.getY(), null);
                }
            }

            if( getName() != null ) {
                // もし鳩に名前がつけられていれば実行
                g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 32));
                g.setColor(Color.black);
                g.drawString(this.name, this.x + 50, this.y + 10); // draw pegeon name
            }

            // 進化のエフェクトを追加
            if( this.changeEffect ) { evolution(g); }

            // 状態を表示
            state.draw(g, getX() + 50, getY() + 40);
        }
    }
    // crow は鳩の鳴き声を鳴らします。
    public void crow() {
        Thread sound = new Thread(this.crowsSound);
        sound.start();
    }

    // 進化時のエフェクト
    private void evolution(Graphics g) {
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 120));
        g.drawString("EVOLUTION!!", 20, 100);
    }

    // 鳩ビームのエフェクト
    private void drawPegeonbeam(Graphics g) {
        // 鳩ビーム時のエフェクトを描画する
        // 鳩ビームの画像を描画
        g.drawImage(this.beam_img, 100, 200, null);

        // write string
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 80));
        g.drawString("（・⊝・）BEAM!!", 80, 250);
    }

    // 鳩ビーム開始用メソッド
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

    public void actionPerformed(ActionEvent e) {
        if( e.getSource().equals(beamTimer) ){
            // System.out.println("beamTimer off");
            this.beamEffect = false;
            this.beamTimer.stop();
        } else if ( e.getSource().equals(evolutionTimer) ) {
            // System.out.println("evolutionTimer off");
            this.changeEffect = false;
            this.evolutionTimer.stop();
        } else if ( e.getSource().equals(foodTimer)) {
            this.foodVisible = false;
        }
        setChanged();
        notifyObservers();
    }
}
