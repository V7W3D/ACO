
package src;

import java.awt.Color;
import java.util.ArrayList;

public class Tuile{

    private static double pheromMin = 0.09;
    private static double pheromMax = 0.80;
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
    private int foncee = 0;
    private int nbfourmisCourante = 0;
    
    public void inncbfourmisCourante(){
        this.nbfourmisCourante++;
        vue.printText(i, j, String.valueOf(nbfourmisCourante));
    }

    public void initColor(){
        vue.mesTuiles[i][j].setBackground(Color.white);
    }

    public void showPheroms(){
        vue.printText(i, j, String.valueOf( this.pherom * 100 ).substring(0,3));
    }

    public void setBackground(Color couleur){
        vue.mesTuiles[i][j].setBackground(couleur);
    }

    public void decnbfourmisCourante(){
        this.nbfourmisCourante--;
        vue.printText(i, j, String.valueOf(nbfourmisCourante) );
    }

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

    public void vaporate(double taux){
        if(this.pherom > pheromMin) {
            this.pherom = (double) this.pherom * (1 - taux);
           /* initprintPheroms();
            showPheroms();*/
            if (!isColony && !isFood){
               /* vue.mesTuiles[i][j].setBackground( vue.mesTuiles[i][j].getBackground().brighter() );
                vue.revalidate();
                vue.repaint();*/
            }
        }else this.pherom = pheromMin;
        showPheroms();
    }

    public void addPherom(double pherom){
        if (this.pherom < pheromMax){
            this.pherom += pherom;
            if (!isColony && !isFood && foncee<255){
                /*removeAntfoncee += 5;
                vue.mesTuiles[i][j].setBackground( vue.mesTuiles[i][j].getBackground().darker() );
                vue.revalidate();
                vue.repaint();*/
            }
        }else this.pherom = pheromMax;
        showPheroms();
    }

}