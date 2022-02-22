package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.awt.Color;
import java.lang.Thread;
import java.util.Random;

public class Plateau {

    private int height,width;
    private static int delay = 5;
    private static int alpha = 4,beta = 1;
    private static double tauxDeVaporation = 0.5;
    private int[] tuileDeDepart = new int[2];
    private int nombreFourmis = 5;
    private int nbEclaireuses = 5;
    private ArrayList<Fourmi> listeFourmis = new ArrayList<Fourmi>();
    private Tuile[][] plateau = new Tuile[30][30];
    boolean hungry = true;//n'a pas trouvé de nourririturre
    private Vue vue;
    private ArrayList<Tuile> plusCourtChemin = new ArrayList<Tuile>();

    /*public void setVue(Vue vue){
        this.vue = vue;
    }*/
    
    public Plateau(int height, int width,Vue vue){
        this.height = height;
        this.width = width;
        this.vue = vue;
        for(int i = 0;i<height;i++){
            for(int j = 0;j<width;j++){
                this.plateau[i][j]=new Tuile(i,j, this.vue);
            }
        }
        //Tuiles accessibles(la ca part dans toutes les directions)
        for(int i = 0;i<height;i++){
            for(int j = 0;j<width;j++){
                if(i+1<height && j+1<width && !plateau[i+1][j+1].isObstacle){
                plateau[i][j].addAsAdj(plateau[i+1][j+1]);
                }
                if(i-1>=0 && j+1<width && !plateau[i-1][j+1].isObstacle){
                    plateau[i][j].addAsAdj(plateau[i-1][j+1]);
                }
                if(j+1<width && !plateau[i][j+1].isObstacle){
                    plateau[i][j].addAsAdj(plateau[i][j+1]);
                }
                if(i+1<height && j-1>=0 && !plateau[i+1][j-1].isObstacle){
                    plateau[i][j].addAsAdj(plateau[i+1][j-1]);
                    }
                if(i-1>=0 && j-1>=0 && !plateau[i-1][j-1].isObstacle){
                        plateau[i][j].addAsAdj(plateau[i-1][j-1]);
                    }
                if(j-1>=0 && !plateau[i][j-1].isObstacle){
                        plateau[i][j].addAsAdj(plateau[i][j-1]);
                    }
                if(i-1>=0 && !plateau[i-1][j].isObstacle){
                        plateau[i][j].addAsAdj(plateau[i-1][j]);
                    }
                if(i+1<height && !plateau[i+1][j].isObstacle){
                        plateau[i][j].addAsAdj(plateau[i+1][j]);
                    }
            }
        }
        tuileDeDepart[0] = 3;
        tuileDeDepart[1] = 0;
        vue.mesTuiles[3][0].setBackground( Color.RED );
        plateau[3][0].setColony(true);
        plateau[9][9].setFood(true);
        vue.mesTuiles[9][9].setBackground( Color.green );
    }

    //vaporisation des pheromones
    private void updatePheroms(){
        for (int i=0;i<this.height;i++){
            for (int j=0;j<this.width;j++){
                plateau[i][j].vaporate(tauxDeVaporation);
            }
        }
    }

    private Tuile foodIsAdj(Tuile tuileCourante){
        for (Tuile tuile:tuileCourante.tuiles){
            if (tuile.isFood()) return tuile;
        }
        return null;
    }

    private Tuile choixTuile(HashMap<Tuile, Double> probs, Tuile tuileCourante){
        Tuile tuile = foodIsAdj(tuileCourante);
        if (tuile == null) return tuile;
        double random = new Random().nextDouble();
        double cumulativeProbability = 0.0;
        for (Map.Entry<Tuile, Double> mapentry : probs.entrySet()) {
            cumulativeProbability += mapentry.getValue(); 
            if (random <= cumulativeProbability)
                return mapentry.getKey();
        }
        return tuileCourante;
    }

    private boolean moveAnt(Fourmi fourmi){
        Optional<Tuile> derniereTuile = fourmi.getLastTuile();
        Tuile tuile,avantDerniereTuile = fourmi.avantDerniereTuile();
        if (!derniereTuile.isPresent())
            tuile = plateau[tuileDeDepart[0]][tuileDeDepart[1]];
        else
            tuile = derniereTuile.get();
        if (avantDerniereTuile == null) avantDerniereTuile = tuile;
        tuile.decnbfourmisCourante();
        HashMap<Tuile, Double> probs = new HashMap<Tuile, Double>();
        double sum = 0;
        for (Tuile tuiles:tuile.tuiles){
            if (!tuiles.equals(avantDerniereTuile) && !tuiles.isColony)
                sum += Math.pow(tuiles.getPherom(), alpha) * Math.pow((double) (1/tuiles.getCost()), beta);
        }
        for (Tuile tuiles:tuile.tuiles){
            if (!tuiles.equals(avantDerniereTuile) && !tuiles.isColony){
                double res = (Math.pow(tuiles.getPherom(), alpha)*Math.pow((double) (1/tuiles.getCost()), beta)) / sum;
                probs.put(tuiles, res);
            }
        }
        Tuile prochaineTuile = choixTuile(probs, tuile);
        prochaineTuile.inncbfourmisCourante();
        fourmi.parcours.add(prochaineTuile);
        return prochaineTuile.isFood();
    }

    private static boolean compareCost(ArrayList<Tuile> tuiles1, ArrayList<Tuile> tuiles2){
        int cost1 = tuiles1.size()>0 ? tuiles2.stream().map(tuile->tuile.getCost()).reduce(0, (a,b)->a+b) : 0;
        int cost2 = tuiles2.size()>0 ? tuiles2.stream().map(tuile->tuile.getCost()).reduce(0, (a,b)->a+b) : 0;
        return cost1 < cost2;
    }

    private void deplacerEclaireuses(){
        for (int i=0;i<nbEclaireuses;i++){
            Fourmi eclaireuse = new Fourmi();
            listeFourmis.add( eclaireuse );
        }
        boolean foundFood = false;
        while (!foundFood){
            for (int i=0;i<nbEclaireuses && !foundFood;i++)
                foundFood = moveAnt( listeFourmis.get(i) );
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    } 

    public void simulation(){
        /*Thread thread = new Thread(new Runnable() {
            //@Override
            public void run() {*/
                boolean foundFoud;
                int distanceplusrapide = 0;
                Fourmi plusrapide = null;
                deplacerEclaireuses();
                for (int i=0;i<nombreFourmis;i++){
                    Fourmi fourmi = new Fourmi();
                    listeFourmis.add( fourmi );
                }
                while (true){
                    for (int i=0;i<nombreFourmis + nbEclaireuses;i++){
                        foundFoud = false;
                        while (!foundFoud){
                            foundFoud = moveAnt(listeFourmis.get(i));
                        }
                        System.out.println(listeFourmis.get(i).getDistance());
                        if (compareCost(listeFourmis.get(i).parcours, this.plusCourtChemin) ||
                                this.plusCourtChemin.size() == 0){
                            this.plusCourtChemin.clear();
                            this.plusCourtChemin.addAll(listeFourmis.get(i).parcours);
                            plusrapide = listeFourmis.get(i);
                            distanceplusrapide = plusrapide.getDistance();
                        }
                        listeFourmis.get(i).init();
                    }
                    updatePheroms();//evaporate
                    for (Fourmi fourmi:listeFourmis){
                        if (distanceplusrapide != 0 && fourmi.equals(plusrapide)){
                            for (Tuile tuile:fourmi.parcours)
                                tuile.addPherom( (double) Fourmi.quantityPherom / fourmi.getDistance() );
                        }
                    }
                    vue.initColor();
                    for (Tuile tuile:this.plusCourtChemin){
                        tuile.addPherom( (double) (2 * Fourmi.quantityPherom) / distanceplusrapide );
                        tuile.setBackground(Color.red);
                    }
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("\n\n");
                    affiche();
                }
            /*}
        });
        thread.start();*/
    }

    public void affiche(){
        for(int i = 0;i<height;i++){
            System.out.println();
            for(int j = 0;j<width;j++){
                System.out.print(plateau[i][j].getPherom()*100+" | ");
            }
        }
    }
}
