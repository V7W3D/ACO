
import java.util.ArrayList;
import java.awt.*;

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
    private int i,j;
    private Vue vue;
    
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
        if (hasAnt) return "|"+idAnt+"|";
        return"| |";
    }

    public void vaporate(double taux){
        if(this.pherom > 1/distancemax) {
            this.pherom = (double) this.pherom * (1 - taux);
            vue.mesTuiles[i][j].setBackground(vue.mesTuiles[i][j].getBackground().brighter());
            vue.revalidate();
            vue.repaint();
        }
    }

    public void addPherom(double pherom){
        this.pherom += pherom;
        vue.mesTuiles[i][j].setBackground(vue.mesTuiles[i][j].getBackground().darker());
        vue.revalidate();
        vue.repaint();
    }

}