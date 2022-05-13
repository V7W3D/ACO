package ModeleVue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.awt.Color;
import java.lang.Thread;
import java.util.Random;

public class Plateau {

    private int maxDistanceAnt;
    //delai d'evaporation chaque 4s
    private int delayPheroms = 4000;
    //delai de deplacement de chaque fourmi chaque 100ms
    private int delayAnt = 100;
    private int height,width;
    private int alpha = 5,beta = 1;
    private double tauxDeVaporation = 0.05;//5%
    private int[] tuileDeDepart = new int[2];
    private int nombreFourmis = 10;
    private ArrayList<Fourmi> listeFourmis = new ArrayList<Fourmi>();
    private Tuile[][] plateau;
    private AntThread threads[];
    private Thread[] threadsColorAndPheroms;
    private Vue vue;
    private volatile Fourmi fourmiPlusRapide;
    private volatile boolean pauseColorsAndPheromsAndAntUpdate = false;

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public int getNbFourmi(){
        return nombreFourmis;
    }

    public void setParmsToDefault(){
        alpha = 5;
        beta = 1;
        tauxDeVaporation = 0.05;
        delayAnt = 100;
        delayPheroms = 4000;
    }
    
    public void setAlphaAndBeta(int alpha, int beta){
        this.alpha = alpha;
        this.beta = beta;
    }

    public int getAlpha(){
        return alpha;
    }

    public int getBeta(){
        return beta;
    }

    public double getTauxDeVaporation(){
        return tauxDeVaporation;
    }

    public int getDelayAnt(){
        return delayAnt;
    }

    public int getDelayPheroms(){
        return delayPheroms;
    }

    public void setdelayAntandVap(int delayAnt, int delaVap){
        this.delayAnt = delayAnt;
        this.delayPheroms = delaVap;
    }

    public void setTauxDeVaporation(double taux){
        this.tauxDeVaporation = taux;
    }

    public void setALpha(int a){
        alpha = a;
    }

    public void setBeta(int b){
        beta = b;
    }

    public void setNbFourmis(int nb){
        nombreFourmis = nb;
    }


    public void setVaporateRate(double rate){
        assert(rate < 1 && rate > 0);
        this.tauxDeVaporation = rate;
    }

    public Thread updateColors(){    
        Thread thread = new Thread(new Runnable(){
            public void run(){
                while (listeFourmis.size() > 1){
                    while (pauseColorsAndPheromsAndAntUpdate){
                        pause(100);
                    }
                    for (int i=0;i<height ;i++){
                        for (int j=0;j<width ;j++){
                            if (!plateau[i][j].isObstacle && !plateau[i][j].isColony() && !plateau[i][j].isFood()){
                                //plus il y'a de pheromones plus la couleur est rouge
                                if (plateau[i][j].hasAnt) vue.printAnt(i,j);
                                else vue.removeAnt(i,j);
                                double pherom = plateau[i][j].getPherom();
                                if (pherom == Tuile.pheromMin) pherom = 0;
                                else if (pherom == Tuile.pheromMax) pherom = 1;
                                double qtt = 1 - pherom;
                                int color = (int)(255 * qtt);
                                plateau[i][j].setBackground( new Color(255, color, color) );
                            }
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
        plateau = vue.mesTuiles;
        this.vue = vue;
        this.maxDistanceAnt = height * width * 10;
        this.fourmiPlusRapide = new Fourmi();
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
    }

    //vaporisation des pheromones
    private Thread initupdatePheroms(){
        Thread threadPheroms = new Thread(new Runnable() {
            public void run(){
                while (listeFourmis.size() > 1){
                        while (pauseColorsAndPheromsAndAntUpdate){
                            pause(100);
                        }
                        //attendre le temps de : delayPheroms avant chaque evaporation
                        try {
                            Thread.sleep(delayPheroms);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        for (int i=0;i< height;i++){
                            for (int j=0;j< width;j++){
                                if ( !plateau[i][j].isObstacle ) plateau[i][j].vaporate(tauxDeVaporation);
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
        prochaineTuile.hasAnt = true;
        prochaineTuile.incNbFourmis();
        boolean remove = false;
        ArrayList<Tuile> tmp = new ArrayList<Tuile>(fourmi.parcours);
        if (fourmi.wentFromIt(prochaineTuile)){
            for (Tuile tuileP:fourmi.parcours){
                if (tuileP.equals(prochaineTuile))
                    remove = true;
                if (remove)
                    tmp.remove(tuileP);
            }
        }
        tmp.add(prochaineTuile);
        fourmi.copyParcour(tmp);
        return prochaineTuile.isFood();
    }

    public void simulation(){
        this.threads = new AntThread[nombreFourmis];
    
        for (int i=0; i<nombreFourmis; i++)
            threads[i] = new AntThread();

        for (int i=0;i<nombreFourmis;i++){
            Fourmi fourmi = new Fourmi();
            listeFourmis.add( fourmi );
        }

        threadsColorAndPheroms = new Thread[2];       
        //thread des pheromones
        threadsColorAndPheroms[0] = initupdatePheroms();
        //thread des couleurs
        threadsColorAndPheroms[1] = updateColors(); 
        Fourmi fourmiCourante;
        //demarer les threads avec une fourmi
        for (int i=0;i<nombreFourmis;i++){
            fourmiCourante = listeFourmis.get(i); 
            threads[i].setFourmiCourante(fourmiCourante);
            threads[i].start();    
        }
        

        threadsColorAndPheroms[0].start();
        threadsColorAndPheroms[1].start();

    
    }
    
    
    public void showBestRoute(){
        vue.initColor();
        for (Tuile tuile:this.fourmiPlusRapide.parcours){
            tuile.setBackground(Color.red);
        }
    }

    public void pauseAllThreads(){
        this.pauseColorsAndPheromsAndAntUpdate = !this.pauseColorsAndPheromsAndAntUpdate;
    }

    public void pause(int delay){
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void restartAllThreads(){
        this.pauseColorsAndPheromsAndAntUpdate = false;
    }


    class AntThread implements Runnable {

        private Fourmi fourmiCourante;
        private Thread thread;
        private boolean isAlive = true;
        
        public AntThread(){}

        public AntThread(Fourmi fourmiCourante) {
            this.fourmiCourante = fourmiCourante;
        }
            
        @Override
        public synchronized void run() {
            boolean foundFoud = false;
            while (isAlive){
                while (pauseColorsAndPheromsAndAntUpdate){
                    pause(100);
                }
                if (!foundFoud){ 
                    pause(delayAnt);
                    foundFoud = moveAnt(fourmiCourante);
                    if (fourmiCourante.getDistance() > maxDistanceAnt){
                        isAlive = false;
                        listeFourmis.remove(fourmiCourante);
                        //end thread
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
        
    }
    public void initDepart(int i,int j){
        tuileDeDepart[0]=i;
        tuileDeDepart[1]=j;
    }
    public void initFood(int i,int j){
        plateau[i][j].setFood(true);
    }
}
