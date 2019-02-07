import javax.swing.*;
import java.awt.*;
import java.*;
import java.util.Observer;
import java.util.Observable;

// メインのゲームウィンドウ
class PegeonPanel extends JPanel implements Observer {
    // 背景を表すのオブジェクト
    private SceneClass scene;
    // 鳩
    private PegeonClass pegeon;
    public PegeonPanel() {
        // 載せる部品を登録
        // 背景
        scene =  new SceneClass();
        // 鳩
        pegeon = new PegeonClass(100, 200);

        // 鳩を監視する
        pegeon.addObserver(this);

        // 可視化
        this.setVisible(true);

    }
    public void paintComponent(Graphics g) {
        scene.draw(g);
        pegeon.draw(g);
    }
    // 鳩から通知を受け取った段階で再描画を行う
    public void update(Observable o, Object obj) {
        this.repaint();
    }
    // getter
    public PegeonClass getPegeon() {
        return pegeon;
    }
    public PegeonClass.State getPegeonState(){
        return pegeon.getState();
    }
}
