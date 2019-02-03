import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Observer;

class PegeonControllWindowClass extends JFrame implements ActionListener, Observer {
    private TextFieldPanel log; //ログ
    private CommandInputField command_area; //コマンドうつ場所
    private PegeonWindowClass pegeonWin; // メインのゲーム画面
    private CommandListWindowClass cmdlistWin;
    private AutoScrollPane scroll;
    private PegeonClass pegeon; // 鳩本体
    private BarObservable observer;
    private boolean start = false;
    private final String beam_command = "youarethe'strongestpegeon`";

    PegeonControllWindowClass(int basex, int basey, int x, int y, PegeonWindowClass pegeonWin, CommandListWindowClass cmdlistWin, BarObservable o) {
        observer = o;
        // 初期位置とサイズを指定
        this.setBounds(basex, basey, x, y);
        this.setTitle("コマンド入力欄");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //初期化
        log = new TextFieldPanel();
        command_area = new CommandInputField("ここに入力してください",x,23);
        pegeon = pegeonWin.getPanel().getPegeon();
        this.pegeonWin = pegeonWin;
        this.cmdlistWin = cmdlistWin;
        //スクロールバーを追加
        scroll = new AutoScrollPane(log);
        log.addText("Enter start!");
        this.getContentPane().add(scroll);
        //コマンド打つ場所の追加
        this.add(command_area,BorderLayout.SOUTH);
        //イベント登録
        command_area.addActionListener(this);
        // 可視化
        this.setVisible(true);

        this.command_area.requestFocus();
    }
    //入力文字列の処理
    @Override
    public void actionPerformed(ActionEvent ev) {
        //コマンドの文字列を受け取る
        String s = command_area.getText().trim();
        if(s.equals(""))return;
        //正規表現を訂正
        String[] commandlist = s.split("(\\s)+", 0);
        //コマンドのチェック
        commandCheck(commandlist);
        scroll.getVerticalScrollBar().setValue(0);
        scroll.getViewport().scrollRectToVisible(new Rectangle(0,Integer.MAX_VALUE-1,1,1));
    }
    //コマンドのチェック
    void commandCheck(String[] commandlist){
        if(commandlist[0].equals("exit")){
            // exitした場合は、鳩の世話を途中で投げ出したということでバッドエンド画面に遷移する
            new BadendcloseClass();
            // 画面遷移
            this.setAllVisible(false);
            this.reset();
        }
        if(commandlist[0].equals("help")){
            new HelpWin(pegeonWin, this, cmdlistWin);
            this.setAllVisible(false);
            observer.stop();
            start = false;
        }
        if(start == false) {
            if(commandlist[0].equals("start")){
                observer.start();
                start = true;
                command_area.set_Start();
                log.addText("Start!");
                return;
            }else{
                command_area.reset_history();
                log.addText("Enter start!");
                return;
            }
        }
        if (commandlist.length == 1) {
            if (commandlist[0].equals("crows")) {
                log.addText(command_area.getText());
                pegeon.crow();
                observer.setValue(0);
            } else if (commandlist[0].equals("feedJava")) {
                pegeon.food(1);
                observer.setValue(0);
                log.addText(command_area.getText());
            } else if (commandlist[0].equals("feedReport")) {
                pegeon.food(2);
                observer.setValue(0);
                log.addText(command_area.getText());
            } else if (commandlist[0].equals("feedFood")) {
                pegeon.food(3);
                observer.setValue(0);
                log.addText(command_area.getText());
            } else if ( commandlist[0].equals("君に決めた")) {
                // 鳩の状態によって、複数のエンド画面に繊維する
                if( pegeon.getState().kind.equals("normal") ) {
                    // 進化していない
                    new BadendnotevolutionClass();
                } else {
                    // 進化している
                    if( pegeon.getName().equals("") ) {
                        // 名前がない
                        new BadendnotnameClass();
                    } else {
                        // 名前がある
                        new PegeonQUESTCLEARClass();
                    }
                }
                this.setAllVisible(false);
                this.reset();
            }  else if ((commandlist[0].equals(beam_command))){
                // true が返ってきたなら、ok
                // false が返って来たときは、yasokukku以外の状態のとき
                if( pegeon.beam() ) {
                    log.addText("Beam!");
                } else {
                    log.addText("この鳩ではない");
                }
                observer.setValue(0);
            } else if ((commandlist[0].equals("reset"))){
                //初期化して消す？
                this.reset();
            } else {
                log.addText("Error!");
                //	System.out.println("error!");
                soundThread sound = new soundThread("koke.wav");
                Thread th  = new Thread(sound);
                th.start();
            }
        } else if (commandlist.length == 2) {
            if (commandlist[0].equals("setChildren")) {
                pegeon.setName(commandlist[1]);
                log.addText(command_area.getText());
                observer.setValue(0);
            } else {
                log.addText("Error!");
                soundThread sound = new soundThread("koke.wav");
                Thread th  = new Thread(sound);
                th.start();
            }
        } else {
            log.addText("Error!");
            soundThread sound = new soundThread("koke.wav");
            Thread th  = new Thread(sound);
            th.start();
        }
    }
    private void reset(){
        command_area.reset_history();
        log.resetText();
        pegeon.reset();
        observer.stop();
        observer.setValue(0);
        this.start = false;
        command_area.set_Start();
    }
    // 進捗バーが100%以上になったので、ゲームオーバー
    public void update(Observable o, Object r) {
        new GAMEOVERClass();
        this.reset();
        this.setAllVisible(false);
    }
    private void setAllVisible(boolean b) {
        this.setVisible(b);
        this.pegeonWin.setVisible(b);
        this.cmdlistWin.setVisible(b);
    }
}

class TextFieldPanel extends JPanel{
    TextFieldPanel(){
        this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
    }
    //入力されたコマンドをセット
    public void addText(String s){
        String[] print_commands  = s.trim().split("(\\s)+ ",0);
        String command = "";
        for(String tmp: print_commands){
            command += " " + tmp;
        }
        JLabel command_label = new JLabel(command);
        command_label.setFont(new Font("Arial",Font.BOLD,23));
        this.add(command_label);
        this.revalidate();
    }
    public void resetText(){
        this.removeAll();
        this.repaint();
    }

}


//ヒストリー機能と透過文字がはいったテキストフィールド
class CommandInputField extends JTextField implements FocusListener,ActionListener,KeyListener{
    private String helpmsg="";
    private String bakstr="";
    private ArrayList<String> history;
    private int history_count;
    private boolean start=false; //kakikomikyoka


    CommandInputField(String msg,int x,int y){
        helpmsg = msg;
        history_count = 0;
        history = new ArrayList<String>();
        addFocusListener(this);
        addActionListener(this);
        addKeyListener(this);
        this.setPreferredSize(new Dimension(x,y));
        this.setFont(new Font("東風ゴシック",Font.PLAIN,y));
    }

    public void set_Start(){
        if(start){
            start = false;
        }else{
            start = true;
        }
    }

    public void reset_history(){
        history = new ArrayList<String>();
        history_count = 1;
    }
    void drawmsg(){
        setForeground(Color.LIGHT_GRAY);
        setText(helpmsg);
    }
    @Override
    public void focusGained(FocusEvent e) {
        setForeground(Color.black);
        setText(bakstr);
    }

    @Override
    public void focusLost(FocusEvent e) {
        bakstr = getText();
        if(bakstr.equals("")){
            drawmsg();
        }
    }
    @Override
    public void paste(){
        return;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(this.getText().trim().equals("")) {
            this.setText("");
            return;
        }
        if(start) history.add(this.getText());
        history_count = history.size();
        this.setText("");
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    //KeyPressdがあるたびにhistory_countを増減させてhistoryから取り出す
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP:
                if(history_count>0){
                    history_count--;
                    this.setText(history.get(history_count));
                }
                break;
            case KeyEvent.VK_DOWN:
                if(history_count < history.size()-1) {
                    history_count++;
                    this.setText(history.get(history_count));
                } else if(history_count == history.size()-1){
                    this.setText("");
                    history_count++;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    // 進捗バーが100%以上になったので、ゲームオーバー
    public void update(Observable o, Object r) {
        // TODO: ゲームオーバー時の処理を追加
        new GAMEOVERClass();
    }
}

class AutoScrollPane extends JScrollPane{
    AutoScrollPane(TextFieldPanel log){
        super(log);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                AutoScrollPane.super.horizontalScrollBar.setValue(AutoScrollPane.super.horizontalScrollBar.getMaximum());
            }
        });
    }
}
