import javax.swing.*;
import java.awt.*;
import java.*;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class TitleClass extends JFrame implements KeyListener {
    public PegeonWindowClass pegeonWindow;
    public CommandListWindowClass commandListWindow;
    public PegeonControllWindowClass pegeonControllWindow;
    public BarObservable barObservable;

    public TitleClass(){
        JPanel p = new JPanel();
        JLabel label = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("/img/title1000700.jpg"));
        label.setIcon(icon);
        p.add(label);
        Container contentPane = getContentPane();
        contentPane.add(p, BorderLayout.CENTER);

        this.setBounds(150, 150, 1000, 725);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.setTitle("Pegeon QUEST");
        addKeyListener(this);
    }

    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                this.setVisible(false);
                barObservable = new BarObservable();
                pegeonWindow = new PegeonWindowClass(480, 100, 700, 550, barObservable);
                commandListWindow = new CommandListWindowClass(110, 100, 360, 305);
                pegeonControllWindow = new PegeonControllWindowClass(50, 415, 403, 235, pegeonWindow, commandListWindow, barObservable);

                // 進捗バーの値が変化したときにpegeonWindowと
                // pegeonControllWindowに通知を送る.
                barObservable.addObserver(pegeonWindow);
                barObservable.addObserver(pegeonControllWindow);
                break;
        }
        repaint();
    }

    public void keyReleased(KeyEvent e){

    }

    public void keyTyped(KeyEvent e){

    }

    public static void main(String[] args){
        new TitleClass();
    }
}
