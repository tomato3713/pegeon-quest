//package pegeon3;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class PegeonControllWindowClass extends JFrame implements ActionListener,KeyListener {
    private TextFieldPanel log; //ログ
    private JTextField command_area; //コマンドうつ場所
    private PegeonWindowClass pegeonWin; // メインのゲーム画面
    private ArrayList<String> history; //コマンドの履歴を保存
    private int history_count; //コマンドの履歴の数
    private AudioInputStream audioIn;
    private PegeonPanel panel; // pegeonPanel
    private PegeonClass pegeon; // 鳩本体

    PegeonControllWindowClass(int basex, int basey, int x, int y, PegeonWindowClass pegeonWin) {
        // 初期位置とサイズを指定
        this.setBounds(basex, basey, x, y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //初期化
        log = new TextFieldPanel();
        command_area = new JTextField();
        history = new ArrayList<String>();
        history_count = 0;
        panel = pegeonWin.getPanel();
        pegeon = panel.getPegeon();

        try{
            //   audioIn = null;
            // this.file = new File("/sound","koke.wav");
            // audioIn = AudioSystem.getAudioInputStream(this.file);
            // clip = AudioSystem.getClip();
            // clip.open(audioIn);
        }catch (Exception e){
            e.printStackTrace();
        }
        //        se = java.applet.Applet.newAudioClip(getClass().getResource("cancel2.wav"));
        //スクロールバーを追加
        JScrollPane scroll = new JScrollPane(log);
        //TODO setAutoscrollsしてるのに自動スクロールしない・・・
        scroll.setAutoscrolls(true);
        this.getContentPane().add(scroll);
        //コマンド打つ場所の追加
        this.add(command_area,BorderLayout.SOUTH);
        //イベント登録
        command_area.addActionListener(this);
        command_area.addKeyListener(this);
        // 可視化
        this.setVisible(true);
    }

    //入力文字列の処理
    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == command_area) {
            //コマンドの文字列を受け取る
            String s = command_area.getText().trim();
            //正規表現を訂正
            String[] commandlist = s.split("(\\s)+", 0);
            //コマンドのチェック
            commandCheck(commandlist);
            history.add(command_area.getText());
            //TODO 再入力するときこれではだめ
            history_count++;
            command_area.setText("");
        }
        //         this.getContentPane().getComponent(0);
        //   this.getContentPane().getComponent(0).setMaximumSize();
        //        SwingUtilities.invokeLater(new Runnable() {
        //            @Override
        //            public void run() {
        //                JScrollBar scrollBar =
        //                scrollBar.setValue(scrollBar.getMaximum());
        //            }
        //
        //        });


    }
    //コマンドのチェック
    void commandCheck(String[] commandlist){
        // System.out.println("a");
        // soundThread sound = new soundThread("koke.wav");
        // Thread th  = new Thread(sound);
        // th.start();
        if (commandlist.length == 1) {
            if (commandlist[0].equals("crows")) {
                log.addText(command_area.getText());
                pegeon.crow();
            } else if (commandlist[0].equals("feedJava")) {
                pegeon.food(1);
                log.addText(command_area.getText());
            } else if (commandlist[0].equals("feedReport")) {
                pegeon.food(2);
                log.addText(command_area.getText());
            } else if (commandlist[0].equals("feedFood")) {
                pegeon.food(3);
                log.addText(command_area.getText());
            } else if ((commandlist[0].equals("exit"))){
                System.exit(0);
            } else if ((commandlist[0].equals("beam"))){
                pegeon.beam();
            } else if ((commandlist[0].equals("restart"))){
                //初期化して消す？
                // メインゲーム画面のリセット
                panel.reset();
                // ログのリセット
                this.reset();
            } else {
                log.addText("Error!");
            }
        } else if (commandlist.length == 2) {
            if (commandlist[0].equals("setChildren")) {
                pegeon.setName(commandlist[1]);
                log.addText(command_area.getText());
            } else {
                log.addText("Error!");


            }
        } else {
            log.addText("Error!");
        }
    }

    private void reset(){
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    //KeyPressdがあるたびにhistory_countを増減させてhistoryから取り出す
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP:
                if(history_count>0){
                    command_area.setText(history.get(history_count-1));
                    if(history_count>0)history_count--;
                }
                break;
            case KeyEvent.VK_DOWN:
                if(history_count < history.size()){
                    if(history_count< history.size()-1)history_count++;
                    command_area.setText(history.get(history_count));
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}

class TextFieldPanel extends JPanel{
    TextFieldPanel(){
        this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
    }
    //入力されたコマンドをセット
    public void addText(String s){
        //command_history.add(s);
        String[] print_commands  = s.trim().split("(\\s)+ ",0);
        String command = "";
        for(String tmp: print_commands){
            command += " " + tmp;
        }
        JLabel command_label = new JLabel(command);
        command_label.setFont(new Font("Arial",Font.BOLD,18));
        this.add(command_label);
        this.revalidate();
    }
}
