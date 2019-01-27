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
    private bgmThread bgm;

    public PegeonWindowClass(int basex, int basey, int x, int y, BarObservable o) {
        // ウィンドウの初期位置とサイズを指定
        this.setBounds(basex, basey, x, y);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container pane = getContentPane();
        panel = new PegeonPanel();
        pane.add(panel, BorderLayout.CENTER);
        bar = new JProgressBar(0, 100);
        pane.add(bar, BorderLayout.SOUTH);

        this.observer = o;
        this.observer.setBar(bar);

        // 可視化
        this.setVisible(true);
    }

    public PegeonPanel getPanel() { return panel; }

    // BGM 再生用を行うクラス
    class bgmThread extends Thread {
        File file;
        private boolean isActive = true;
        private boolean isPlay = false;
        private Clip clip;
        @Override
        public void run() {
            // BGM をループして再生する
            // bgm.WAV の引用元
            // [[http://www.music-note.jp/bgm/nature.html]]
            // 大自然のイメージ (壮大・爽やか) Africa
            if( !isPlay ) {
                file = new File("./sound", "bgm.wav");
                AudioInputStream audioIn;
                try {
                    audioIn = AudioSystem.getAudioInputStream(this.file);
                    AudioFormat af = audioIn.getFormat();

                    DataLine.Info dataLine = new DataLine.Info(Clip.class, af);

                    clip = (Clip)AudioSystem.getLine(dataLine);

                    clip.open(audioIn);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    isPlay = true;
                } catch (Exception e) {
                    // オーディオファイルが存在しない。
                    // 読み込み不可能な形式である可能性がある。
                    e.printStackTrace();
                }
            }
        }
        public void stopBgm() {
            // 終了直前の処理
            clip.stop();
        }
    }
    public void update(Observable o, Object r) {
        timeOver = true;
    }

    // ウィンドウが表示されているときのみ、bgmを再生する。
    @Override
    public void setVisible(boolean b) {
        if( b ) {
            bgm = new bgmThread();
            bgm.start();
        } else {
            bgm.stopBgm();
        }
        super.setVisible(b);
    }
}
