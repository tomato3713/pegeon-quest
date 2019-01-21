import javax.swing.*;
import java.awt.*;
import java.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.ArrayDeque;

// メインのゲームウィンドウ
class PegeonPanel extends JPanel implements ActionListener {
    private SceneClass scene;
    private PegeonClass pegeon;
    private Timer timer;
    public PegeonPanel() {
        // 載せる部品を登録
        scene =  new SceneClass();
        pegeon = new PegeonClass(100, 200);

        // 定期的に再描画を行う
        timer = new Timer(5, this);
        timer.start();

        // 可視化
        this.setVisible(true);

    }
    public void paintComponent(Graphics g) {
        scene.draw(g);
        pegeon.draw(g);
    }
    public void actionPerformed(ActionEvent e) {
        this.repaint();
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
        // 鳩の状態を初期化
        this.pegeon = new PegeonClass(100, 200);
    }
}
