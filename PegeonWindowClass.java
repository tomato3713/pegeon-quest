// TODO 画像の大きさを揃える
// TODO 背景の透明化
// TODO マウスを近づけるとハトが逃げていく
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.*;
import java.awt.event.*;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.util.Queue;
import java.util.ArrayDeque;
// For sound
import javax.sound.sampled.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

// メインのゲームウィンドウ
class PegeonPanel extends JPanel implements ActionListener {
    private SceneClass scene;
    private PegeonClass pegeon;
    private Queue<PegeonFeedClass> foods;
    private Timer timer;
    private Timer feedTimer;
    public PegeonPanel() {
        // 載せる部品を登録
        scene =  new SceneClass();
        pegeon = new PegeonClass(100, 200);
        foods = new ArrayDeque<PegeonFeedClass>();

        // 定期的に再描画を行う
        timer = new Timer(5, this);
        timer.start();

        // 可視化
        this.setVisible(true);

    }
    public void paintComponent(Graphics g) {
        scene.draw(g);
        pegeon.draw(g);
        for (PegeonFeedClass food : foods){
            food.draw(g);
        }
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(feedTimer)) {
            // 一定時間後に餌を削除する
            foods.poll();
        }
        this.repaint();
    }
    public void feed(int kind) {
        getPegeon().setstate(kind); // ハトの状態を更新
        // 餌を画面に追加
        foods.add(new PegeonFeedClass(getPegeon().getX() - 5, getPegeon().getY() + 5, kind));
        feedTimer = new Timer(500, this);
        feedTimer.setRepeats(true);
        feedTimer.start();
    }

    // getter
    public PegeonClass getPegeon() {
        return pegeon;
    }
    public PegeonClass.State getPegeonState(){
        return pegeon.getState();
    }

    public void reset() {
        // 初期化する
        // すべての餌を削除
        for ( PegeonFeedClass food: foods){
            foods.poll();
        }
        // 鳩の状態を初期化
        this.pegeon = new PegeonClass(100, 200);
    }
}

// メインのゲームウィンドウ
class PegeonWindowClass extends JFrame {
    private PegeonPanel panel;
    public PegeonWindowClass(int basex, int basey, int x, int y) {
        // ウィンドウの初期位置とサイズを指定
        this.setBounds(basex, basey, x, y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new PegeonPanel();
        this.add(panel);

        Thread bgm = new Thread(new bgmThread());
        bgm.start();

        // 可視化
        this.setVisible(true);
    }
    public PegeonPanel getPanel() {
        return panel;
    }
    // BGM 再生用を行うクラス
    class bgmThread implements Runnable {
        File file;
        @Override
        public void run() {
            // BGM をループして再生する
            // bgm.WAV の引用元
            // [[http://www.music-note.jp/bgm/nature.html]]
            // 大自然のイメージ (壮大・爽やか) Africa
            file = new File("./sound", "bgm.WAV");
            AudioInputStream audioIn;
            Clip clip;
            try {
                audioIn = AudioSystem.getAudioInputStream(this.file);
                AudioFormat af = audioIn.getFormat();

                DataLine.Info dataLine = new DataLine.Info(Clip.class, af);

                clip = (Clip)AudioSystem.getLine(dataLine);

                clip.open(audioIn);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (Exception e) {
                // オーディオファイルが存在しない。
                // 読み込み不可能な形式である可能性がある。
                e.printStackTrace();
            }
        }
    }
}
