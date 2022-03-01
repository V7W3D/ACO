
package src;

import java.awt.Color;
import java.util.ArrayList;

public class Tuile{

    public static double pheromMin = 0.10;
    public static double pheromMax = 0.80;
    private double pherom;//initial value to 1
    static int IDEN=0;
    int id;
    ArrayList<Fourmi> Fourmis = new ArrayList<>();
    boolean isColony = false;
    private boolean isFood = false;
    boolean isObstacle = false;
    ArrayList<Tuile> tuiles = new ArrayList<>();
    private int cost = 1;//le cout de la tuile
    boolean hasAnt = false;
    private String idAnt = "";
    private int i,j;
    private Vue vue;
    
    public void initColor(){
        vue.mesTuiles[i][j].setBackground(Color.white);
    }

    public void showPheroms(){
        vue.printText(i, j, String.valueOf( this.pherom * 100 ).substring(0,3));
    }

    public void setBackground(Color couleur){
        vue.mesTuiles[i][j].setBackground(couleur);
    }

    public Tuile(int i, int j, Vue vue){
        id=IDEN;
        IDEN++;
        pherom = pheromMin;
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

    public void setPherom(double pherom){
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
        if (hasAnt) return "|"+idAnt+"|";
        return"| |";
    }

    public void setIsObstacle(boolean isObstacle){
        this.isObstacle = isObstacle;
    }

    public void vaporate(double taux){
        this.pherom = (double) this.pherom * (1 - taux);
        if(this.pherom < pheromMin) this.pherom = pheromMin;
        showPheroms();
    }

    public void addPherom(double pherom){
        if (this.pherom + pherom  < pheromMax){
            this.pherom += pherom;
        }else this.pherom = pheromMax;
        showPheroms();
    }

    public void setPhermoToMin(){
        pherom = pheromMin;
    }

}