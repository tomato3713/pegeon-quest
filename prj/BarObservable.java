import javax.swing.Timer;
import java.util.Observable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;

// コマンドを打つ制限時間を表すバーが100%になったら通知を行うクラス
class BarObservable extends Observable implements ActionListener {
    // バーの実体
    private JProgressBar bar;
    // バーを増やすタイミングを決めるタイマー
    private Timer timer;
    // setValue() はバーに任意の値を設定する
    // 値がセットされたら通知を行う
    public void setValue(int v) {
        bar.setValue(v);
        if ( bar.getPercentComplete() >= 1.0 ) {
            setChanged(); // オブジェクトが変更されたとしてマークする
            notifyObservers();
        }
    }
    // actionPerformed() はバーの値を順繰りに増やす
    public void actionPerformed(ActionEvent e) {
        this.setValue(this.bar.getValue()+1);
    }
    // setBar() は監視対象を登録するメソッド
    public void setBar(JProgressBar bar) {
        this.bar = bar;
    }
    // start() はバーの値を増やし始めるメソッド
    public void start() {
        this.timer = new Timer(50, this);
        this.timer.start();
    }
    // stop() はタイマーの値を増やすのを止めるためのメソッド
    public void stop() {
        if( timer != null ) {
            this.timer.stop();
        }
    }
}
