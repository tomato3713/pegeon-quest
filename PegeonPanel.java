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
