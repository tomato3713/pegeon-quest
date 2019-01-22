// TODO 画像の大きさを揃える
// TODO 背景の透明化
// TODO マウスを近づけるとハトが逃げていく
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Insets;
// For sound
import javax.sound.sampled.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

import java.util.Observer;
import java.util.Observable;

// メインのゲームウィンドウ
class PegeonWindowClass extends JFrame implements Observer {
    private boolean timeOver = false;
    private PegeonPanel panel;
    private JProgressBar bar;
    private BarObservable observer;
    public PegeonWindowClass(int basex, int basey, int x, int y, BarObservable o) {
        this.observer = o;
        // ウィンドウの初期位置とサイズを指定
        this.setBounds(basex, basey, x, y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container pane = getContentPane();
        panel = new PegeonPanel();
        pane.add(panel, BorderLayout.CENTER);
        bar = new JProgressBar(0, 100);
        pane.add(bar, BorderLayout.SOUTH);

        Thread bgm = new Thread(new bgmThread());
        bgm.start();

        // 可視化
        this.setVisible(true);
    }

    public JProgressBar getBar() { return bar; }
    public PegeonPanel getPanel() { return panel; }

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
    public void update(Observable o, Object r) {
        timeOver = true;
    }
}
