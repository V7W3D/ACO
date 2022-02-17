
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.lang.Thread;
import java.util.Random;

public class Plateau {

    private int height,width;
    private static int delay = 50;
    private static int alpha = 4,beta = 1;
    private static double tauxDeVaporation = 0.01;
    private int[] tuileDeDepart = new int[2];
    private int nombreFourmis = 200;
    private int nbEclaireuses = 50;
    private ArrayList<Fourmi> listeFourmis = new ArrayList<Fourmi>();
    private Tuile[][] plateau = new Tuile[30][30];
    boolean hungry = true;//n'a pas trouvé de nourririturre
    private Vue vue;

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
                if(i+1<height && j+1<width){
                plateau[i][j].addAsAdj(plateau[i+1][j+1]);
                }
                if(i-1>=0 && j+1<width){
                    plateau[i][j].addAsAdj(plateau[i-1][j+1]);
                }
                if(j+1<width){
                    plateau[i][j].addAsAdj(plateau[i][j+1]);
                }
                if(i+1<height && j-1>=0){
                    plateau[i][j].addAsAdj(plateau[i+1][j-1]);
                    }
                if(i-1>=0 && j-1>=0){
                        plateau[i][j].addAsAdj(plateau[i-1][j-1]);
                    }
                if(j-1>=0){
                        plateau[i][j].addAsAdj(plateau[i][j-1]);
                    }
                if(i-1>=0){
                        plateau[i][j].addAsAdj(plateau[i-1][j]);
                    }
                if(i+1<height){
                        plateau[i][j].addAsAdj(plateau[i+1][j]);
                    }
            }
        }
        tuileDeDepart[0] = 3;
        tuileDeDepart[1] = 0;
        plateau[3][0].setColony(true);
        plateau[10][10].setFood(true);
    }

    //vaporisation des pheromones
    private void updatePheroms(){
        for (int i=0;i<this.height;i++){
            for (int j=0;j<this.width;j++){
                plateau[i][j].vaporate(tauxDeVaporation);
            }
        }
    }

    private Tuile choixTuile(HashMap<Tuile, Double> probs, Tuile tuileCourante){
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
        tuile.setAnt(false);
        tuile.setAntID("");
        //calcul des probas
        HashMap<Tuile, Double> probs = new HashMap<Tuile, Double>();
        double sum = 0;
        for (Tuile tuiles:tuile.tuiles){
            if (!tuiles.equals(avantDerniereTuile) && !tuiles.hasAnt() && !tuiles.isColony)
                sum += Math.pow(tuiles.getPherom(), alpha)*Math.pow((double) (1/tuiles.getCost()), beta);
        }
        for (Tuile tuiles:tuile.tuiles){
            if (!tuiles.equals(avantDerniereTuile) && !tuiles.hasAnt() && !tuiles.isColony){
                double res = (Math.pow(tuiles.getPherom(), alpha)*Math.pow((double) (1/tuiles.getCost()), beta)) / sum;
                probs.put(tuiles, res);
            }
        }
        Tuile prochaineTuile = choixTuile(probs, tuile);
        if (prochaineTuile.isFood()){
            for (Tuile tuiles:fourmi.parcours){
                System.out.println(tuiles.id);
                tuiles.addPherom( (double) 1 / fourmi.getDistance() );//ajout des pheromones
            }
            fourmi.init();//retour a la case de depart
        }else{
            fourmi.parcours.add(prochaineTuile);
            prochaineTuile.setAnt(true);
            prochaineTuile.setAntID(String.valueOf( fourmi.id ));
        }
        return prochaineTuile.isFood();
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
            System.out.println("\n\n");
            affiche();
        }
    } 

    public void simulation(){
        /*Thread thread = new Thread(new Runnable() {
            //@Override
            public void run() {*/
                deplacerEclaireuses();
                for (int i=0;i<nombreFourmis - nbEclaireuses;i++){
                    Fourmi fourmi = new Fourmi();
                    listeFourmis.add( fourmi );
                }
                while (true){
                    for (int i=0;i<nombreFourmis;i++){
                        moveAnt(listeFourmis.get(i));
                    }
                    updatePheroms();
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
        for(int i = 0;i<30;i++){
            System.out.println();
            for(int j = 0;j<30;j++){
                System.out.print(plateau[i][j]);
            }
        }
    }
}
