//package MediaPegeon2;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
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
        //	  JButton b = new JButton("CommandListWindow");
        // JScrollBar vertical  = new JScrollBar(JScrollBar.VERTICAL); //スクロールバーをつける
        //JScrollBar horizonal = new JScrollBar(JScrollBar.HORIZONTAL);

        //HTMLの中身をtextに移植
        StringBuffer text = new StringBuffer();

        try{
            //こちらが
            FileReader html_reader = new FileReader(System.getProperty("user.dir")+"//media.html");
            //	      FileReader html_reader = new FileReader("C:\\Users\\MASA\\IdeaProjects\\untitled1\\src\\MediaPegeon2\\media.html");
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
            HTMLEditorKit edit = new HTMLEditorKit();
            StyleSheet css = new StyleSheet();
            html.getDocument();
            //     this.getContentPane().add(BorderLayout.CENTER,html);
            this.getContentPane().add(new JScrollPane(html));

        }
        catch(Exception err){System.out.println(err);}
        //    this.add(horizonal, BorderLayout.SOUTH);
        // this.add(vertical,BorderLayout.EAST);
        //	this.add(test,BorderLayout.CENTER);
        //  b.addActionListener(this);

        // 可視化
        this.setTitle("コマンド一覧");
        this.setVisible(true);
    }
    public void actionPerformed(ActionEvent ev) {
    }
}

//class JScrollPaneTest extends JScrollPaneTest{
//  test = new JTextField("Hello,world");
//}
