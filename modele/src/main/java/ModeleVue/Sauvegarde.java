package ModeleVue;

import java.io.FileWriter;
import java.io.IOException;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class Sauvegarde {
    private Vue v;

    public Sauvegarde(Vue v) {
        this.v = v;
    }

    public void sauvgarder() {
        JFileChooser choose = new JFileChooser(
            FileSystemView
            .getFileSystemView()
            .getHomeDirectory()
        );
        int res = choose.showSaveDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            File fileToSave = choose.getSelectedFile();
            writeMatrice(fileToSave.getAbsolutePath(), v.mesTuiles);
        }
    }

    public void ouvrir(){
        JFileChooser choose = new JFileChooser(
            FileSystemView
            .getFileSystemView()
            .getHomeDirectory()
        );
        int res = choose.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
          File file = choose.getSelectedFile();
          readMatrice(file.getAbsolutePath());
        }
        
    }

    void readMatrice(String filename){
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            int i = 0;
            while (line != null) {
               String[] ligne = line.split(",");
               for(int j = 0; j < ligne.length; j++){
                   if(Integer.parseInt(ligne[j]) == 1){
                        v.mesTuiles[i][j].setIsObstacle(true);
                        v.mesTuiles[i][j].setBackground(Color.black);
                   }
                   else{
                        v.mesTuiles[i][j].setIsObstacle(false);
                        v.mesTuiles[i][j].setBackground(Color.white);
                   }
               } 
               line = br.readLine();
               i++;
            }
        }
        catch (IOException e){}
      }

    void writeMatrice(String filename, Tuile[][] matrix) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    bw.write((matrix[i][j].isObstacle ? 1 : 0) + ((j == matrix[i].length-1) ? "" : ","));
                }
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {}
    }
}
