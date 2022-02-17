import java.util.ArrayList;

public class Tuile{

    int Pherom=0;
    static int IDEN=0;
    int id;
    ArrayList<Fourmi> Fourmis = new ArrayList<>();
    boolean isColony = false;
    boolean isFood = false;
    boolean isObstacle = false;
    int tauxEvaportaion;
    int temps=5000;
    ArrayList<Tuile> tuiles = new ArrayList<>();
    boolean dejaVisite = false;

    public Tuile(){
        id=IDEN;
        IDEN++;
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

    public boolean thereAreAnts(){
        return (Fourmis.size()>0);
    }
    
    public String toString(){
        if(isColony) return"|C|";
        if(isFood) return "|F|";
        if (thereAreAnts()) return "|.|";
        return"| |";
    }
}