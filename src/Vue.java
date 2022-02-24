package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Vue extends JFrame{
    private static final int hauteur = 600;
    private static final int largeur = 600;
    private static final int n = 10;
    private static final int m = 10;
    private Plateau plateau;
    private JLabel[][] textToPrint = new JLabel[n][m];
    JPanel[][] mesTuiles = new JPanel[n][m];

    public void initColor(){
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                if (!plateau.getTuiles()[i][j].isObstacle && !plateau.getTuiles()[i][j].isColony && !plateau.getTuiles()[i][j].isFood()) mesTuiles[i][j].setBackground(Color.WHITE);
            }
        }
    }

    public Vue() {
        setSize(hauteur, largeur);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Simulation Algo des colonies de Fourmis");

        JPanel container = new JPanel();
        container.setLayout(new GridLayout(n,m));

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                textToPrint[i][j] = new JLabel("");
                mesTuiles[i][j] = new JPanel();
                mesTuiles[i][j].add( textToPrint[i][j] );
                mesTuiles[i][j].setBackground(new Color(255,255,255));
                mesTuiles[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
                container.add(mesTuiles[i][j]);
            }
        }
        setContentPane(container);
    }

    public void init(){
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                int i1 = i,j1 = j;
                mesTuiles[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if(SwingUtilities.isRightMouseButton(e)){ new Pop(plateau.getTuiles()[i1][j1]);} //Click droit ouvre une fenetre
                        else{
                            if(!plateau.getTuiles()[i1][j1].isObstacle){
                                plateau.getTuiles()[i1][j1].setIsObstacle(true);
                                mesTuiles[i1][j1].setBackground( Color.black );
                            }
                            else{
                                plateau.getTuiles()[i1][j1].setIsObstacle(false);
                                mesTuiles[i1][j1].setBackground( Color.WHITE );
                                }
                    }}
    
                    });
                }
            }
        }

    public void setPlateau(Plateau plateau){
        this.plateau = plateau;
    }

    public void printText(int i,int j,String text){
        textToPrint[i][j].setText(text);
    }
}
