package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.awt.Color;
import java.lang.Thread;
import java.util.Random;

public class Plateau {

    private int maxDistance;
    private static int delayPheroms = 4000;
    private static int delayAnt = 100;
    private int height,width;
    private static int alpha = 5,beta = 1;
    private static double tauxDeVaporation = 0.05;//5%
    private int[] tuileDeDepart = new int[2];
    private int nombreFourmis = 10;
    private ArrayList<Fourmi> listeFourmis = new ArrayList<Fourmi>();
    private Tuile[][] plateau = new Tuile[30][30];
    private Vue vue;
    private volatile Fourmi fourmiPlusRapide;
    
    public Tuile[][] getTuiles(){
        return this.plateau;
    }

    public Thread updateColors(){
        Thread thread = new Thread(new Runnable(){
            public void run(){
                while (true){
                    
                    for (int i=0;i<height;i++){
                        for (int j=0;j<width;j++){
                            if (plateau[i][j].hasAnt) vue.printText(i,j,"A");
                            else vue.printText(i,j,"");
                            double pherom = plateau[i][j].getPherom();
                            if (pherom == Tuile.pheromMin) pherom = 0;
                            else if (pherom == Tuile.pheromMax) pherom = 1;
                            double qtt = 1 - pherom;
                            int color =(int)(255 * qtt);
                            if (!plateau[i][j].isObstacle) vue.mesTuiles[i][j].setBackground( new Color(255, color, color) );
                        }
                    }
                }
            }
        });
        return thread;
    }

    public Plateau(int height, int width,Vue vue){
        this.height = height;
        this.width = width;
        this.vue = vue;
        maxDistance = height * width * 100;
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
        vue.mesTuiles[3][0].setBackground( Color.RED );
        plateau[3][0].setColony(true);
        plateau[9][9].setFood(true);
        vue.mesTuiles[9][9].setBackground( Color.green );
    }

    //vaporisation des pheromones
    private Thread initupdatePheroms(){
        Thread threadPheroms = new Thread(new Runnable() {
            public void run(){
                while (true){
                        try {
                            Thread.sleep(delayPheroms);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (int i=0;i< height;i++){
                            for (int j=0;j< width;j++){
                                if (!plateau[i][j].isObstacle) plateau[i][j].vaporate(tauxDeVaporation);
                            }
                        }
                    }
                }
        });
        return threadPheroms;
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

    private synchronized boolean moveAnt(Fourmi fourmi){
        Optional<Tuile> derniereTuile = fourmi.getLastTuile();
        Tuile tuile,avantDerniereTuile = fourmi.avantDerniereTuile();
        if (!derniereTuile.isPresent())
            tuile = plateau[tuileDeDepart[0]][tuileDeDepart[1]];
        else
            tuile = derniereTuile.get();
        tuile.hasAnt = false;
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
        //prochaineTuile.addPherom( Fourmi.quantityPherom );
        prochaineTuile.hasAnt = true;
        boolean remove = false;
        ArrayList<Tuile> tmp = new ArrayList<Tuile>(fourmi.parcours);
        //suppression des boucles inutiles
        if (fourmi.wentFromIt(prochaineTuile)){
            for (Tuile tuileP:fourmi.parcours){
                if (tuileP.equals(prochaineTuile))
                    remove=true;
                if (remove)
                    tmp.remove(tuileP);
            }
        }
        tmp.add(prochaineTuile);
        fourmi.copyParcour(tmp);
        return prochaineTuile.isFood();
    }

    private static boolean containObstacle(ArrayList<Tuile> tuiles){
        for (Tuile tuile:tuiles){
            if (tuile.isObstacle) return true;
        }
        return false;
    }

    private void init(){
        listeFourmis.clear();
        //maj des pheromones
        for (int i=0;i<height;i++){
            for (int j=0;j<width;j++){
                plateau[i][j].setPhermoToMin();
            }
        }
    }

    public void simulation(boolean restarted){
        Fourmi fourmiCourante;
        AntThread threads[] = new AntThread[nombreFourmis];
    
        for (int i=0;!restarted && i<nombreFourmis;i++)
            threads[i] = new AntThread();

        for (int i=0;i<nombreFourmis;i++){
            Fourmi fourmi = new Fourmi();
            listeFourmis.add( fourmi );
        }

        Thread trehadPheroms = initupdatePheroms();
        Thread threadColors = updateColors();

        for (int i=0;i<nombreFourmis;i++){
            fourmiCourante = listeFourmis.get(i); 
            threads[i].setFourmiCourante(fourmiCourante);
            threads[i].start();     
        }

        
        trehadPheroms.start();
        threadColors.start();

        for (int i=0;i<nombreFourmis;i++)
            threads[i].join();

        try {
            trehadPheroms.join();
            threadColors.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    
    public void showBestRoute(){
        vue.initColor();
        for (Tuile tuile:this.fourmiPlusRapide.parcours){
            tuile.setBackground(Color.red);
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
        private boolean isPause;
        private boolean isAlive = true;
        
        public AntThread(){}

        public AntThread(Fourmi fourmiCourante) {
            this.fourmiCourante = fourmiCourante;
        }
            
        @Override
        public synchronized void run() {
            var foundFoud = false;
            while (isAlive){
                if (!foundFoud){ 
                    pause(delayAnt);
                    foundFoud = moveAnt(fourmiCourante);
                    if (fourmiCourante.getDistance() > maxDistance){
                        isAlive = false;
                        listeFourmis.remove(fourmiCourante);
                        return;
                    }
                }else{
                    for (Tuile tuile:fourmiCourante.parcours)
                        tuile.addPherom( (double) Fourmi.quantityPherom / fourmiCourante.getDistance() );
                    if (fourmiCourante.getDistance() < fourmiPlusRapide.getDistance())
                    fourmiPlusRapide.copyParcourAndId(fourmiCourante);//make a copy of the ant
                    fourmiCourante.init();
                    foundFoud = false;   
                }
            }
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

        public void pause(int delay){
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //pour mettre le thread en pause
        public synchronized void pause(){
            while(isPause){
                try{
                    wait();
                }catch(InterruptedException ie){
                    ie.printStackTrace();
                }
            }
            notify();
        }

        public void setIsPause(boolean isPause){
            this.isPause = isPause;
        }
        
    }
}
