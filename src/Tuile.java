package src;

import java.util.ArrayList;

public class Tuile{

    private static int distancemax = 500;
    private double pherom;//initial value to 1
    static int IDEN=0;
    int id;
    ArrayList<Fourmi> Fourmis = new ArrayList<>();
    boolean isColony = false;
    private boolean isFood = false;
    boolean isObstacle = false;
    ArrayList<Tuile> tuiles = new ArrayList<>();
    boolean dejaVisite = false;
    private int cost = 1;//le cout de la tuile
    private boolean hasAnt = false;
    private String idAnt = "";
    
    public Tuile(){
        id=IDEN;
        IDEN++;
        pherom = (double) 1 / distancemax;
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
        if (hasAnt) return "|"+idAnt+"|";
        return"| |";
    }

    public void vaporate(double taux){
        this.pherom = (double) this.pherom * (1 - taux);
    }

    public void addPherom(double pherom){
        this.pherom += pherom;
    }

}