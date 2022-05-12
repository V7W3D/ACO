package ModeleVue;

import javax.swing.*;

import java.awt.*;
class Pop extends JFrame {
    JPanel mainePanel;
    JTextField text1;
    Pop(Tuile a)
    {
        mainePanel = new JPanel();
        text1 = new JTextField();
        mainePanel.add(text1);
        setSize(200, 100);
        setVisible(true);
        setContentPane(mainePanel);
    }
    
}