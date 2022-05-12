package ModeleVue;

import java.util.ArrayList;

import javax.swing.JPanel;

public class Tuile extends JPanel {

    public static double pheromMin = 0.10;
    public static double pheromMax = 0.80;
    private double pherom;//initial value to 1
    static int IDEN=0;
    private int nbFourmis = 0;
    int id;
    ArrayList<Fourmi> Fourmis = new ArrayList<>();
    boolean isColony = false;
    private boolean isFood = false;
    public boolean isObstacle = false;
    ArrayList<Tuile> tuiles = new ArrayList<>();
    private int cost = 1;//le cout de la tuile
    boolean hasAnt = false;
    private String idAnt = "";
    private int i,j;

    public void setCost(int cost){
        this.cost = cost;
    }

    public void incNbFourmis(){
        nbFourmis++;
    }

    public int getNbFourmi(){
        return nbFourmis;
    }
    
    public Tuile(int i, int j){
        id=IDEN;
        IDEN++;
        pherom = pheromMin;
        this.setI(i); this.setJ(j);
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

    public boolean isFood(){
        return isFood;
    }

    public double getPherom(){
        return pherom;
    }

    public boolean isColony(){
        return isColony;
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
    }

    public void addPherom(double pherom){
        if (this.pherom + pherom  < pheromMax){
            this.pherom += pherom;
        }else this.pherom = pheromMax;
    }

    public void setPhermoToMin(){
        pherom = pheromMin;
    }

}