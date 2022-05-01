package src;

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
}
