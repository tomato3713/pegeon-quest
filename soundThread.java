import java.io.File;
// For sound
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Clip;

class soundThread implements Runnable {
    File file;
    public soundThread(String fname) {
        this.file = new File("./sound", fname);
    }
    @Override
    public void run() {
        AudioInputStream audioIn;
        Clip clip;
        try {
            audioIn = AudioSystem.getAudioInputStream(this.file);
            AudioFormat af = audioIn.getFormat();

            DataLine.Info dataLine = new DataLine.Info(Clip.class, af);

            clip = (Clip)AudioSystem.getLine(dataLine);

            clip.open(audioIn);
            clip.start();
            Thread.sleep(10000); // スレッドが止まると音の再生が止まってしまうので、スレッドをスリープさせる
            clip.close();
        } catch (Exception e) {
            // オーディオファイルが存在しない。
            // 読み込み不可能な形式である可能性がある。
            e.printStackTrace();
        }

    }
}
