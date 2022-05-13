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
    private Plateau p;

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

    int nbLines(String fileName){
        File file = new File(fileName); 
        int nbrLine = 0;            
        try (FileReader fr = new FileReader(file)) {
            BufferedReader br = new BufferedReader(fr);  
            String str;
            while((str = br.readLine()) != null){
                nbrLine++;                               
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nbrLine;
    }

    public Sauvegarde(Vue v, Plateau p) {
        this.v = v;
        this.p = p;
    }

    public void readMatrice(String filename){
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            int i = 0;
            if (nbLines(filename) != v.mesTuiles.length) return;
            while (line != null) {
               String[] ligne = line.split(",");
               if(ligne.length == v.mesTuiles[0].length){
                    for(int j = 0; j < ligne.length; j++){
                        if(Integer.parseInt(ligne[j]) == 3){
                            p.initFood(i, j);
                            v.FoodChoisie = true;
                            v.textToPrint[i][j].setIcon(v.resizedIcone(v.ressourcePath + "/food.png", 350 / Math.max(p.getHeight(),p.getWidth() )));
                        }
                        else{
                            if(Integer.parseInt(ligne[j]) == 2){
                                p.initDepart(i, j);
                                v.ColonieChoisie = true;
                                v.textToPrint[i][j].setIcon(v.resizedIcone(v.ressourcePath + "/home.png", 350 / Math.max(p.getHeight(),p.getWidth())));
                                v.mesTuiles[i][j].setColony(true);
                            }
                            else{
                                if(Integer.parseInt(ligne[j]) == 1){
                                    v.mesTuiles[i][j].setIsObstacle(true);
                                    v.mesTuiles[i][j].setBackground(Color.black);
                                }
                                if(Integer.parseInt(ligne[j]) == 0){
                                    v.mesTuiles[i][j].setIsObstacle(false);
                                    v.mesTuiles[i][j].setBackground(Color.white);
                                }
                            }
                        }

                    } 
                    line = br.readLine();
                    i++;
               }else return;
            }
        }
        catch (IOException e){}
      }

    void writeMatrice(String filename, Tuile[][] matrix) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    if(matrix[i][j].isColony){
                        bw.write(2 + ((j == matrix[i].length-1) ? "" : ","));
                    }
                    else{
                        if(matrix[i][j].isFood()){
                            bw.write(3 + ((j == matrix[i].length-1) ? "" : ","));
                        }
                        else{bw.write((matrix[i][j].isObstacle ? 1 : 0) + ((j == matrix[i].length-1) ? "" : ","));}
                    }

                }
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {}
    }
}
