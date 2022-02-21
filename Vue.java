import javax.swing.*;
import java.awt.*;

public class Vue extends JFrame{
    private static final int hauteur = 600;
    private static final int largeur = 600;
    private static final int n = 30;
    private static final int m = 30;
    JPanel[][] mesTuiles = new JPanel[n][m];


    public Vue() {
        setSize(hauteur, largeur);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Simulation Algo des colonies de Fourmis");

        JPanel container = new JPanel();
        container.setLayout(new GridLayout(n,m));

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                mesTuiles[i][j] = new JPanel();
                mesTuiles[i][j].setBackground(new Color(255,255,255));
                mesTuiles[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
                container.add(mesTuiles[i][j]);
            }
        }
        setContentPane(container);
    }
}
