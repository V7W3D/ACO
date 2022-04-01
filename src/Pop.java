package src;

import javax.swing.*;

import java.awt.*;
class Pop extends JFrame {
    Popup p;
    Pop(Tuile a)
    {
        JFrame f = new JFrame("Tuile");
        JLabel l = new JLabel("This is a Popup");
 
        f.setSize(400, 200);
 
        PopupFactory pf = new PopupFactory();
        JPanel p2 = new JPanel();
        p2.setBackground(Color.WHITE);
 
        p2.add(l);
        p = pf.getPopup(f, p2, 180, 100);
        JLabel jlabel = new JLabel(""+a.toString());
        jlabel.setFont(new Font("Verdana",1,20));
        JPanel p1 = new JPanel();
        f.add(p1);
        f.add(jlabel);
        f.setVisible(true);
        f.setLayout(new GridBagLayout());
    }
    
}