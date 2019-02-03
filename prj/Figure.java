// Figure とそれを継承するクラス達
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import java.awt.*;
import java.*;
import java.awt.Image;
import java.awt.Graphics;
import java.io.File;
import java.net.URL;

// 画像を描画するクラスの雛形となるクラス
class Figure {
    // 位置に関するもの
    protected int x,y;
    // 画像データを格納する変数
    protected Image img;

    Figure() {
        this.x = 0; this.y = 0;
    }

    // 現在の座標を取得
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    // setter
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    // x, yだけ画像を動かす
    public void move(int x, int y) {
        this.x += x; this.y += y;
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
    public void draw(Graphics g) {
        // 設定された画像を描画する
        g.drawImage(img, this.x, this.y, null);
    }
}
