package src;

public class Main {
    public static void main(String[] args){
        //Plateau p = new Plateau(10, 5);
        //p.simulation();
        //SwingUtilities.invokeLater(new Runnable() {
            //@Override
            //public void run() {
                Vue vue = new Vue();
                Plateau p = new Plateau(10,10,vue);
                vue.setPlateau(p);
                vue.init();
                vue.setVisible(true);
                p.simulation();
           // }
        //});
    }
}
