import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
// コマンド一覧を表示するウィンドウ
class CommandListWindowClass extends JFrame implements ActionListener {
    CommandListWindowClass(int basex, int basey, int x, int y) {
        // ウィンドウの表示位置とサイズを指定
        this.setBounds(basex, basey, x, y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ウィンドウに載せる部品を登録
        //HTMLの中身をtextに移植
        StringBuffer text = new StringBuffer();
        try{
            //こちらが
            FileReader html_reader = new FileReader(System.getProperty("user.dir")+"//media.html");
            BufferedReader tmp = new BufferedReader(html_reader);
            String line;
            while((line = tmp.readLine()) != null){
                text.append(line + "\n");
            }
            tmp.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        //HTMLをはっつける
        try{
            JEditorPane html = new JEditorPane("text/html",text.toString());
	    html.setEditable(false);
            html.getDocument();
            this.getContentPane().add(new JScrollPane(html));

        }
        catch(Exception err){System.out.println(err);}
        // 可視化
        this.setTitle("コマンド一覧");
        this.setVisible(true);
    }
    public void actionPerformed(ActionEvent ev) {
    }
}
