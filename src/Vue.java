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
        iconeAntResized = resizedIcone(this.ressourcePath + "\\ant.png", 30);
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
        JMenuItem pause = new JMenuItem("Pause");
        JMenuItem restart = new JMenuItem("Restart");
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plateau.pauseAllThreads(); 
            }
        });
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plateau.restartAllThreads(); 
            }
        });
        menu.add(restart);
        menu.add(pause);
        return menu;
    }

    public void init(){
        menuBar = initMenuBar();
        setJMenuBar(menuBar);
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                if (plateau.getTuiles()[i][j].isFood())
                    textToPrint[i][j].setIcon( resizedIcone(this.ressourcePath+"\\food.png", 40) );
                if (plateau.getTuiles()[i][j].isColony)
                    textToPrint[i][j].setIcon( resizedIcone(this.ressourcePath+"\\home.png", 40) );
                int i1 = i,j1 = j;
                mesTuiles[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (plateau.getTuiles()[i1][j1].isObstacle){
                            plateau.getTuiles()[i1][j1].setIsObstacle(false);
                            mesTuiles[i1][j1].setBackground( Color.white );
                        }else{
                            plateau.getTuiles()[i1][j1].setIsObstacle(true);
                            mesTuiles[i1][j1].setBackground( Color.black );
                        }
                    }
                });
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

}
