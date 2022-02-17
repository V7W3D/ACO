import javax.swing.JFrame;

public class PlateauView extends JFrame{

    public PlateauView(String title){
        setTitle(title);
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false );
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        PlateauView p = new PlateauView("Fourmis");
    }
}