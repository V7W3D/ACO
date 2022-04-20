package src;

import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JMenuBar;


public class Vue extends JFrame{
    private static final int hauteur = 600;
    private static final int largeur = 600;
    private int n;
    private int m;
    private Plateau plateau;
    private JLabel[][] textToPrint;
    JPanel[][] mesTuiles;
    private String ressourcePath;
    private ImageIcon iconeAntResized;
    private JMenuBar menuBar;
    private boolean ColonieChoisie = false;;
    private boolean FoodChoisie = false, lancer = false;


    public void initColor(){
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                if (!plateau.getTuiles()[i][j].isObstacle) mesTuiles[i][j].setBackground(Color.WHITE);
           }
        }
    }

    private ImageIcon resizedIcone(String path, int newsize){
        ImageIcon imageIcon = new ImageIcon(path); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(newsize, newsize,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }

    public Vue(int n, int m) {
        this.n = n;
        this.m = m;
        this.ressourcePath = System.getProperty("user.dir") + "\\src\\ressources";
        iconeAntResized = resizedIcone(this.ressourcePath + "/ant.png", 30);
        setSize(hauteur, largeur);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Simulation Algo des colonies de Fourmis");
        textToPrint = new JLabel[n][m];
        mesTuiles = new JPanel[n][m];
        JPanel container = new JPanel();
        container.setLayout(new GridLayout(n,m));
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                textToPrint[i][j] = new JLabel();
                mesTuiles[i][j] = new JPanel();
                mesTuiles[i][j].add( textToPrint[i][j] );
                mesTuiles[i][j].setBackground(new Color(255,255,255));
                mesTuiles[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
                container.add(mesTuiles[i][j]);
            }
        }
        setContentPane(container);
    }

    private JMenuBar initMenuBar(){
        JMenuBar menu = new JMenuBar();
        JMenu menuOPtions = new JMenu("Options");
        JMenu modifications = new JMenu("Modifications");
        JMenuItem modifierParametres = new JMenuItem("Modifications des parametres");
        JMenuItem pause = new JMenuItem("Pause");
        JMenuItem restart = new JMenuItem("Restart");
        JMenu LancerSim = new JMenu("Lancer");
        JMenuItem lancerLaSimulation = new JMenuItem("Lancer Simulation");
        LancerSim.add(lancerLaSimulation);
        modifications.add(modifierParametres);
        lancerLaSimulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!lancer) {
                    plateau.simulation();
                    lancer = true;
                }
            }
        });
        modifierParametres.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plateau.pauseAllThreads();
                ModificationsMenu modMenu =  new ModificationsMenu();
                modMenu.setVisible(true);
                modMenu.setLocationRelativeTo(null);
            }
        });
        menuOPtions.add(pause);
        menuOPtions.add(restart);
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plateau.pauseAllThreads(); 
            }
        });
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                dispose();
                new Fenetre();
            }
        });
        menu.add(menuOPtions);
        menu.add(LancerSim);
        menu.add(modifications);
        return menu;
    }

    public void init(){
        menuBar = initMenuBar();
        setJMenuBar(menuBar);
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                int i1 = i,j1 = j;
                mesTuiles[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(!ColonieChoisie){
                            plateau.initDepart(i1, j1);
                            ColonieChoisie = true;
                            textToPrint[i1][j1].setIcon( resizedIcone(ressourcePath+"/home.png", 40) );
                            plateau.getTuiles()[i1][j1].setColony(true);
                        }

                        else if(!FoodChoisie){
                            plateau.initFood(i1, j1);
                            FoodChoisie = true;
                            textToPrint[i1][j1].setIcon( resizedIcone(ressourcePath+"/food.png", 40) );
                        }
                        else{
                            if(SwingUtilities.isRightMouseButton(e)){
                                new Pop(plateau.getTuiles()[i1][j1]);
                            }
                            else{
                            if (plateau.getTuiles()[i1][j1].isObstacle){
                                plateau.getTuiles()[i1][j1].setIsObstacle(false);
                                mesTuiles[i1][j1].setBackground( Color.white );
                            }else{
                                plateau.getTuiles()[i1][j1].setIsObstacle(true);
                                mesTuiles[i1][j1].setBackground( Color.black );
    
                    }}}}}
                );
            }
        }
    }

    public void setPlateau(Plateau plateau){
        this.plateau = plateau;
    }

    public void printAnt(int i,int j){
        textToPrint[i][j].setIcon(iconeAntResized);
    }

    public void removeAnt(int i,int j){
        textToPrint[i][j].setIcon(null);
    }

    private class ModificationsMenu extends JFrame{
        private JSlider alphaSlide, betaSlide, inputVap, inputDelayVap, inputDelayAnt;
        private JPanel mainContainer, containersLables,containerData,containerButtons;
        private JButton apply,cancel;
        public ModificationsMenu(){
            containerData = new JPanel();
            containersLables = new JPanel();
            containersLables.setLayout(new GridLayout(0,1));
            containerButtons = new JPanel();
            containerButtons.setLayout(new GridLayout(0,2));
            JLabel textLabel1 = new JLabel("alpha : ");
            JLabel textLabel2 = new JLabel("beta : ");
            JLabel textLabel3 = new JLabel("taux de vaporation : ");
            JLabel textLabel4 = new JLabel("temps d'evaporation : ");
            JLabel textLabel5 = new JLabel("temps de deplacement fourmis : ");
            apply = new JButton("Apply");
            cancel = new JButton("Cancel");
            inputVap = initSliders(5,100,5,10);
            inputDelayVap = initSliders(1000,8000,1000,1000);
            inputDelayAnt = initSliders(100,1000,100,100);
            mainContainer = new JPanel();
            alphaSlide = initSliders(1,16,1,1);
            betaSlide = initSliders(1,16,1,1);
            containerButtons.add(apply);
            containerButtons.add(cancel);
            mainContainer.setLayout(new GridLayout(0,2));
            containersLables.add(textLabel1);
            containersLables.add(alphaSlide);
            containersLables.add(textLabel2);
            containersLables.add(betaSlide);
            containersLables.add(textLabel3);
            containersLables.add(inputVap);
            containersLables.add(textLabel4);
            containersLables.add(inputDelayVap);
            containersLables.add(textLabel5);
            containersLables.add(inputDelayAnt);
            containersLables.add(containerButtons);
            mainContainer.add(containersLables);
            mainContainer.add(containerData);
            setContentPane(mainContainer);
            setSize(500,550);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setTitle("Modifications");
        }

        private JSlider initSliders(int min, int max, int val, int spacing) {
            JSlider slider = new JSlider(min,max,val);
            slider.setPaintTrack(true); 
            slider.setPaintTicks(true); 
            slider.setPaintLabels(true);
            slider.setMajorTickSpacing(spacing); 
            slider.setMinorTickSpacing(spacing); 
            return slider;
        }
    }

}
