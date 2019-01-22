import javax.swing.Timer;
import java.util.Observable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;

// 観察対象
class BarObservable extends Observable implements ActionListener {
    private JProgressBar bar;
    private Timer timer;
    public void setValue(int v) {
        bar.setValue(v);
        if ( bar.getPercentComplete() >= 1.0 ) {
            setChanged(); // オブジェクトが変更されたとしてマークする
            notifyObservers();
        }
    }
    public void actionPerformed(ActionEvent e) {
        this.setValue(this.bar.getValue()+1);
    }
    public void setBar(JProgressBar bar) {
        this.bar = bar;
        timer = new Timer(50, this);
        timer.start();
    }
}
