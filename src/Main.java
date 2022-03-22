package src;

public class Main {

    private static int height = 10, width = 10;

    public static void main(String[] args){
        Vue vue = new Vue(height, width);
        Plateau p = new Plateau(height, width, vue);
        vue.setPlateau(p);
        vue.init();
        vue.setVisible(true);
        //lance la simulation
        p.simulation();
    }
}
