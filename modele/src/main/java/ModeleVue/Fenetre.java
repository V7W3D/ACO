package ModeleVue;
import java.awt.*;
import javax.swing.*;

public class Fenetre extends JPanel{

public Fenetre(){
    JTextField field1 = new JTextField("5");
    JTextField field2 = new JTextField("1");
    JTextField field3 = new JTextField("10");
    JTextField field4 = new JTextField("10");
    JTextField field5 = new JTextField("10");
    this.setLayout(new GridLayout(0, 1));
    this.add(new JLabel("Alpha : (entre -10 et 10) "));
    this.add(field1);
    this.add(new JLabel("Beta :(entre -10 et 10) "));
    this.add(field2);
    this.add(new JLabel("x : (entre 10 et 30)"));
    this.add(field3);
    this.add(new JLabel("y : (entre 10 et 30)"));
    this.add(field4);
    this.add(new JLabel("Nombres de fourmis : ( entre 5 et 200)"));
    this.add(field5);
    int result = JOptionPane.showConfirmDialog(null,this,"Parametres",JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION){
        if(allFieldsCorrect(field1.getText(), field2.getText(), field3.getText(), field4.getText(), field5.getText())){
            Vue vue = new Vue(Integer.valueOf(field3.getText()),Integer.valueOf(field4.getText()));
            Plateau p = new Plateau(Integer.valueOf(field3.getText()),Integer.valueOf(field4.getText()),vue);
            p.setALpha(Integer.valueOf(field1.getText()));
            p.setBeta(Integer.valueOf(field2.getText()));
            p.setNbFourmis(Integer.valueOf(field5.getText()));
            vue.setPlateau(p);
            Sauvegarde s = new Sauvegarde(vue, p);
            vue.setSauvegarde(s);
            vue.init();
            vue.setVisible(true);
            vue.setLocationRelativeTo(null);
        }else{
            JOptionPane.showMessageDialog(null, "Entrez des valeurs corrects","ERREUR",JOptionPane.ERROR_MESSAGE);
            new Fenetre();
        }
    }
}

    public boolean taillePlateauCorrect(String s){
        int n =0;
        try {
            n = Integer.valueOf(s);
        } catch (Exception e) {
            return false;
        }
        if(n>30 || n<10) return false;
        return true;
    }

    public boolean alphaCorrect(String s){
        int n =0;
        try {
            n = Integer.valueOf(s);
        } catch (Exception e) {
            return false;
        }
        if(n>10 || n<-10) return false;
        return true;
    }

    public boolean betaCorrect(String s){
        int n =0;
        try {
            n = Integer.valueOf(s);
        } catch (Exception e) {
            return false;
        }
        if(n>10 || n<-10) return false;
        return true;
    }

    public boolean nbFourmisCorrect(String s){
        int n =0;
        try {
            n = Integer.valueOf(s);
        } catch (Exception e) {
            return false;
        }
        if(n>200 || n<5) return false;
        return true;
    }

    public boolean allFieldsCorrect(String s1,String s2,String s3,String s4,String s5){
        return alphaCorrect(s1) && betaCorrect(s2) && taillePlateauCorrect(s3) && taillePlateauCorrect(s4) && nbFourmisCorrect(s5);
    }
}