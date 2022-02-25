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
    private static int alpha = 4,beta = 0;
    private static double tauxDeVaporation = 0.03;
    private int[] tuileDeDepart = new int[2];
    private int nombreFourmis = 5;
    private int nbEclaireuses = 5;
    private ArrayList<Fourmi> listeFourmis = new ArrayList<Fourmi>();
    private Tuile[][] plateau = new Tuile[30][30];
    boolean hungry = true;//n'a pas trouvé de nourririturre
    private Vue vue;
    private volatile Fourmi fourmiPlusRapide;
    
    public Tuile[][] getTuiles(){
        return this.plateau;
    }

    public Plateau(int height, int width,Vue vue){
        this.height = height;
        this.width = width;
        this.vue = vue;
        this.fourmiPlusRapide = new Fourmi();
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
        vue.mesTuiles[3][0].setBackground( Color.BLUE );
        plateau[3][0].setColony(true);
        plateau[9][9].setFood(true);
        vue.mesTuiles[9][9].setBackground( Color.GREEN );
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
        if (tuile != null) return tuile;
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
        HashMap<Tuile, Double> probs = new HashMap<Tuile, Double>();
        double sum = 0;
        for (Tuile tuiles:tuile.tuiles){
            if (!tuiles.equals(avantDerniereTuile) && !tuiles.isColony && !tuiles.isObstacle)
                sum += Math.pow(tuiles.getPherom(), alpha) * Math.pow((double) (1/tuiles.getCost()), beta);
        }
        for (Tuile tuiles:tuile.tuiles){
            if (!tuiles.equals(avantDerniereTuile) && !tuiles.isColony && !tuiles.isObstacle){
                double res = (Math.pow(tuiles.getPherom(), alpha)*Math.pow((double) (1/tuiles.getCost()), beta)) / sum;
                probs.put(tuiles, res);
            }
        }
        Tuile prochaineTuile = choixTuile(probs, tuile);
        fourmi.parcours.add(prochaineTuile);
        return prochaineTuile.isFood();
    }

    private void deplacerEclaireuses(){
        Fourmi lastAnt = null;
        for (int i=0;i<nbEclaireuses;i++){
            Fourmi eclaireuse = new Fourmi();
            listeFourmis.add( eclaireuse );
        }
        boolean foundFood = false;
        while (!foundFood){
            for (int i=0;i<nbEclaireuses && !foundFood;i++){
                lastAnt =  listeFourmis.get(i);
                foundFood = moveAnt( listeFourmis.get(i) );
            }
            this.fourmiPlusRapide.copyParcourAndId(lastAnt);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    } 

    private static boolean containObstacle(ArrayList<Tuile> tuiles){
        for (Tuile tuile:tuiles){
            if (tuile.isObstacle) return true;
        }
        return false;
    }

    private void init(){
        listeFourmis.clear();
    }

    public void simulation(){
        Fourmi fourmiCourante;
        AntThread threads[] = new AntThread[nombreFourmis+nbEclaireuses];
        deplacerEclaireuses();

        for (int i=0;i<nombreFourmis+nbEclaireuses;i++)
            threads[i] = new AntThread();

        for (int i=0;i<nombreFourmis;i++){
            Fourmi fourmi = new Fourmi();
            listeFourmis.add( fourmi );
        }

        while (true){
            for (int i=0;i<nombreFourmis + nbEclaireuses;i++){
                fourmiCourante = listeFourmis.get(i); 
                threads[i].setFourmiCourante(fourmiCourante);
                threads[i].start();     
                threads[i].join();
            }
            updatePheroms();//evaporate
            for (Fourmi fourmi:listeFourmis){
                if (!fourmi.equals(this.fourmiPlusRapide)){
                    for (Tuile tuile:fourmi.parcours)
                        tuile.addPherom( (double) Fourmi.quantityPherom / fourmi.getDistance() );
                }
            }
            vue.initColor();
            //si obstacle on relance la partie
            if (containObstacle(this.fourmiPlusRapide.parcours)){
                init();
                simulation();
            }
            for (Tuile tuile:this.fourmiPlusRapide.parcours){
                tuile.addPherom( (double) (1.5 * Fourmi.quantityPherom) / this.fourmiPlusRapide.getDistance());
                if(!tuile.isFood()) tuile.setBackground(Color.red);
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*affiche();
            System.out.println("\n\n");*/
        }
    }

    public void affiche(){
        for(int i = 0;i<height;i++){
            System.out.println();
            for(int j = 0;j<width;j++){
                System.out.print(plateau[i][j].getPherom()*100+" | ");
            }
        }
    }

    class AntThread implements Runnable {

        private Fourmi fourmiCourante;
        private Thread thread;
        
        public AntThread(){}

        public AntThread(Fourmi fourmiCourante) {
            this.fourmiCourante = fourmiCourante;
        }
            
        @Override
        public void run() {
            var foundFoud = false;
            while (!foundFoud)
                foundFoud = moveAnt(fourmiCourante);
            if (fourmiCourante.getDistance() < fourmiPlusRapide.getDistance())
                fourmiPlusRapide.copyParcourAndId(fourmiCourante);//make a copy of the ant
            fourmiCourante.init();               
        }

        public void setFourmiCourante(Fourmi fourmi){
            this.fourmiCourante = fourmi;
        }

        public void start(){
            thread = new Thread(this);
            thread.start();
        }

        public void join(){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
}
