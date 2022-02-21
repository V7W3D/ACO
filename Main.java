
public class Main {
    public static void main(String[] args){
        //Plateau p = new Plateau(10, 5);
        //p.simulation();
        //SwingUtilities.invokeLater(new Runnable() {
            //@Override
            //public void run() {
                Vue vue = new Vue();
                Plateau p = new Plateau(30,30,vue);
                vue.setVisible(true);
                p.simulation();
           // }
        //});
    }
}
