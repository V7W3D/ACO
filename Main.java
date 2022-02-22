
public class Main {
    public static void main(String[] args){
        Vue vue = new Vue();
        Plateau p = new Plateau(30,30,vue);
        vue.setVisible(true);
        p.simulation();
    }
}
