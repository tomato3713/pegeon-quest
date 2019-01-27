import javax.swing.*;
import java.awt.*;
import java.*;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class HelpWin extends JFrame implements KeyListener {
    PegeonWindowClass pegeonWin;
    PegeonControllWindowClass pegeonctlWin;
    CommandListWindowClass cmdlistWin;
    public HelpWin(PegeonWindowClass pegeonWin, PegeonControllWindowClass pegeonctlWin, CommandListWindowClass cmdlistWin){
        this.pegeonWin = pegeonWin;
        this.pegeonctlWin = pegeonctlWin;
        this.cmdlistWin = cmdlistWin;

        JPanel p = new JPanel();
        JLabel label = new JLabel();
        ImageIcon icon = new ImageIcon("./img/help-1.png");
        label.setIcon(icon);
        p.add(label);
        Container contentPane = getContentPane();
        contentPane.add(p, BorderLayout.CENTER);

        this.setBounds(150, 150, 1206, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.setTitle("Pegeon QUEST");
        addKeyListener(this);
    }
    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                this.setVisible(false);
                // 画面を閉じるだけ
                // titleWin = new TitleClass();
                this.dispose();
                this.pegeonWin.setVisible(true);
                this.pegeonctlWin.setVisible(true);
                this.cmdlistWin.setVisible(true);
                break;
        }
        repaint();
    }
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
}
