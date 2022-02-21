import javax.swing.*;
public class Main {
    private static void createAndShowGui() {
        int rows =30;
        int cols =30;
        int cellWidth = 20;
        Plateau mainPanel = new Plateau(rows, cols, cellWidth);
  
        JFrame frame = new JFrame("Color Grid Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
     }
  
     public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
           public void run() {
              createAndShowGui();
           }
        });
     }
  }
    /*    //Plateau p = new Plateau(10, 5);
        //p.simulation();
        //SwingUtilities.invokeLater(new Runnable() {
            //@Override
            //public void run() {
                Vue vue = new Vue();
                Plateau p = new Plateau(30,30,vue);
                vue.setVisible(true);
                p.simulation();
           // }
        //});
    }
}*/


