import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedInputStream;

class soundThread implements Runnable {
    // 音声ファイルのURL
    URL url;
    public soundThread(String fname) {
        this.url = getClass().getResource("/sound/" + fname);
    }
    // run() は別スレッドで音声をClipを使用して再生する.
    // wav ファイルでの再生のみ確認
    @Override
    public void run() {
        AudioInputStream audioIn;
        Clip clip;
        try {
            // データストリームを読み込み
            InputStream is = this.url.openStream();
            audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            // 音声の保存形式を取得
            AudioFormat af = audioIn.getFormat();

            DataLine.Info dataLine = new DataLine.Info(Clip.class, af);

            clip = (Clip)AudioSystem.getLine(dataLine);

            clip.open(audioIn);

            long frameLen = audioIn.getFrameLength(); // 総フレーム数
            float frameNum = af.getSampleRate(); // 1秒あたりのフレーム数

            clip.start();
            // スレッドが止まると音の再生が止まってしまうので、スレッドを音声の
            // 再生時間分スリープさせる
            Thread.sleep((int) (frameLen/frameNum * 1000) );

            // 再生終了後クリップを閉じる
            clip.close();
        } catch (Exception e) {
            // オーディオファイルが存在しない。
            // 読み込み不可能な形式である可能性がある。
            e.printStackTrace();
        }
    }
}
