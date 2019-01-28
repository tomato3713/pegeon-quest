import javax.swing.*;
import java.awt.*;
import java.*;
import java.util.Observer;
import java.util.Observable;

// メインのゲームウィンドウ
class PegeonPanel extends JPanel implements Observer {
    private SceneClass scene;
    private PegeonClass pegeon;
    public PegeonPanel() {
        // 載せる部品を登録
        scene =  new SceneClass();
        pegeon = new PegeonClass(100, 200);
        pegeon.addObserver(this);

        // 可視化
        this.setVisible(true);

    }
    public void paintComponent(Graphics g) {
        scene.draw(g);
        pegeon.draw(g);
    }
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
