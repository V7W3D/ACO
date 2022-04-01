package src;
import java.awt.*;
import javax.swing.*;

public class Fenetre extends JPanel{
public Fenetre(){
    JTextField field1 = new JTextField("5");
    JTextField field2 = new JTextField("1");
    JTextField field3 = new JTextField("20");
    JTextField field4 = new JTextField("20");
    JTextField field5 = new JTextField("0");
    this.setLayout(new GridLayout(0, 1));
    this.add(new JLabel("Alpha : "));
    this.add(field1);
    this.add(new JLabel("Beta : "));
    this.add(field2);
    this.add(new JLabel("x : "));
    this.add(field3);
    this.add(new JLabel("y : "));
    this.add(field4);
    this.add(new JLabel("Nombres de fourmis : "));
    this.add(field5);
    int result = JOptionPane.showConfirmDialog(null,this,"Parametres",JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
        //traitement des donnees
        Vue vue = new Vue(Integer.valueOf(field3.getText()),Integer.valueOf(field4.getText()));
        Plateau p = new Plateau(Integer.valueOf(field3.getText()),Integer.valueOf(field4.getText()),vue);
        vue.setPlateau(p);
        vue.init();
        vue.setVisible(true);
    }
}
public static void main(String[] args) {
    new Fenetre();
   
}
}