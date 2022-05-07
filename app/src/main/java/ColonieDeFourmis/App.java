package ColonieDeFourmis;

import javax.swing.SwingUtilities;

import ModeleVue.Fenetre;

public class App {

    public static int height = 10, width = 10;

    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            new Fenetre();
        });
    }
}
