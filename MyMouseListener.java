
import java.awt.event.MouseAdapter;
import java.awt.event.*;

public class MyMouseListener extends MouseAdapter {

    private Plateau p;

    public MyMouseListener(Plateau x) {
        this.p = x;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            p.labelPressed((Tuile) e.getSource());
        }
    
    if (e.getButton() == MouseEvent.BUTTON3){
        p.openTab((Tuile) e.getSource());
    }
}
}