
import java.util.ArrayList;
import java.awt.*;

import javax.swing.JLabel;

public class Tuile extends JLabel{

    private static int distancemax = 500;
    private double pherom = 100;//initial value to 1
    double pheroMax = 0.01;
    static int IDEN=0;
    int id;
    ArrayList<Fourmi> Fourmis = new ArrayList<>();
    boolean isColony = false;
    boolean isFood = false;
    boolean isObstacle = false;
    ArrayList<Tuile> tuiles = new ArrayList<>();
    boolean dejaVisite = false;
    private int cost = 1;//le cout de la tuile
    private boolean hasAnt = false;
    private String idAnt = "";
    private int i,j;
    private Vue vue;
    Color color = new Color(255,255,255);
    
    public Tuile(int i, int j, Vue vue){
        id=IDEN;
        IDEN++;
        pherom = (double) 1 / distancemax;
        this.setI(i); this.setJ(j);
        this.vue = vue;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setAntID(String idAnt){
        this.idAnt = idAnt;
    }

    public boolean hasAnt(){
        return hasAnt;
    }

    public void setAnt(boolean hasAnt){
        this.hasAnt = hasAnt;
    }

    public int getCost(){
        return cost;
    }

    @Override
    public boolean equals(Object t){
        return ((Tuile)t).id == id;
    }

    public boolean isFood(){
        return isFood;
    }

    public double getPherom(){
        return pherom;
    }

    public void setPherom(float pherom){
        this.pherom = pherom;
    }

    public void setColony(boolean isColony) {
        this.isColony = isColony;
    }

    public void addAsAdj(Tuile x ){
        tuiles.add(x);
    }

    public void setFood(boolean isFood) {
        this.isFood = isFood;
    }

    public String toString(){
        if(isColony) return"|C|";
        if(isFood) return "|F|";
        if (hasAnt) return "|"+pherom+"|";
        return"| |";
    }

    public void vaporate(double taux){
        if(this.pherom > 1/distancemax) {
            this.pherom = (double) this.pherom * (1 - taux)<0.002 ? 0.002 : (double) this.pherom * (1 - taux);
            
            double x = pherom<0.01? 0 : this.pherom/pheroMax;
            vue.mesTuiles[i][j].setBackground(new Color(vue.mesTuiles[i][j].getBackground().getRed(),(int) (vue.mesTuiles[i][j].getBackground().getGreen()*(1-x)),(int) (vue.mesTuiles[i][j].getBackground().getBlue()*(1-x))));
            vue.revalidate();
            vue.repaint();
        }
    }

    public void addPherom(double pherom){
        this.pherom += pherom;
        double x = this.pherom/pheroMax > 1 ? 1 : this.pherom/pheroMax ;
        vue.mesTuiles[i][j].setBackground(new Color(vue.mesTuiles[i][j].getBackground().getRed(),(int) (vue.mesTuiles[i][j].getBackground().getGreen()*(1-x)),(int) (vue.mesTuiles[i][j].getBackground().getBlue()*(1-x))));
        vue.revalidate();
        vue.repaint();
    }

    public void setObstacle(boolean b) {
        this.isObstacle = b;
    }

}