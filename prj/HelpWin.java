import javax.swing.*;
import java.awt.*;
import java.*;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

// Help 画面を表示するためのクラス
class HelpWin extends JFrame implements KeyListener {
    // もともとあったゲーム画面を保持するための変数
    PegeonWindowClass pegeonWin;
    PegeonControllWindowClass pegeonctlWin;
    CommandListWindowClass cmdlistWin;

    // もとの画面に戻って来る必要があるため、３つの画面への参照を渡す
    public HelpWin(PegeonWindowClass pegeonWin, PegeonControllWindowClass pegeonctlWin, CommandListWindowClass cmdlistWin){
        this.pegeonWin = pegeonWin;
        this.pegeonctlWin = pegeonctlWin;
        this.cmdlistWin = cmdlistWin;

        // ヘルプ画面を表示
        JPanel p = new JPanel();
        JLabel label = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("/img/help.png"));
        label.setIcon(icon);
        p.add(label);
        Container contentPane = getContentPane();
        contentPane.add(p, BorderLayout.CENTER);

        this.setBounds(150, 150, 1200, 690);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.setTitle("Pegeon QUEST");
        addKeyListener(this);
    }

    // Enter keyが押されたら、Help画面を閉じ、もとの画面に戻す
    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                this.setVisible(false);
                // 画面を閉じるだけ
                this.dispose();
                this.pegeonWin.setVisible(true);
                this.pegeonctlWin.setVisible(true);
                this.cmdlistWin.setVisible(true);
                this.cmdlistWin.requestFocus();
                break;
        }
        repaint();
    }
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
}
