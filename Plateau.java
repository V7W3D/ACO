public class Plateau {
    public Tuile[][] plateau = new Tuile[10][30];
    Tuile tuileActu;//on va surement s'en debarrasser car elle sert que quand y'a une seule fourmi
    boolean hungry = true;//n'a pas trouvé de nourririturre
    public Plateau(){
    for(int i = 0;i<10;i++){
        for(int j = 0;j<30;j++){
            this.plateau[i][j]=new Tuile();
        }
    }
//Tuiles accessibles(la ca part dans toutes les directions)
    for(int i = 0;i<10;i++){
        for(int j = 0;j<30;j++){
            if(i+1<10 && j+1<30){
            plateau[i][j].addAsAdj(plateau[i+1][j+1]);
            }
            if(i-1>=0 && j+1<30){
                plateau[i][j].addAsAdj(plateau[i-1][j+1]);
            }
            if(j+1<30){
                plateau[i][j].addAsAdj(plateau[i][j+1]);
            }
              if(i+1<10 && j-1>=0){
                plateau[i][j].addAsAdj(plateau[i+1][j-1]);
                }
            if(i-1>=0 && j-1>=0){
                    plateau[i][j].addAsAdj(plateau[i-1][j-1]);
                }
            if(j-1>=0){
                    plateau[i][j].addAsAdj(plateau[i][j-1]);
                }
            if(i-1>=0){
                    plateau[i][j].addAsAdj(plateau[i-1][j]);
                }
            if(i+1<10){
                    plateau[i][j].addAsAdj(plateau[i+1][j]);
                }
        }
    }

    plateau[3][0].setColony(true);
    plateau[3][20].setFood(true);
    tuileActu = plateau[3][0];
    Fourmi test = new Fourmi();
    plateau[3][0].Fourmis.add(test);

}
public void moveAntFrom(Tuile z){
    //choisie une tuile au hazard c'est la qu'on va rajouter le concepte de pheromone
    int tuileAccessible = z.tuiles.size();
    tuileActu.dejaVisite=true;
    int choix=(int)(Math.random()*tuileAccessible);
    Tuile x = z.tuiles.get(choix);
    //La fourmi change de tuile
    x.tuiles.remove(z);   
    x.Fourmis.add(z.Fourmis.get(0));
    z.Fourmis.remove(0);
    tuileActu = x;
  /* if(x.Fourmis.get(0).parcours.contains(x)){     //ELIMINER LES BOUCLES DANS LE PARCOURS
        int i = 0;
        int k = -1;
        for(Tuile t : x.Fourmis.get(0).parcours){
            if(t.equals(x)) k = i;
            else i++;
        }
        for(int l = k;l<x.Fourmis.get(0).parcours.size();i++){
            x.Fourmis.get(0).parcours.remove(l);
        }
    }*/
    x.Fourmis.get(0).parcours.add(x);
    System.out.println("la taille du parcours est :" + x.Fourmis.get(0).parcours.size());
}
    



    public void affiche(){
        for(int i = 0;i<10;i++){
            System.out.println();
            for(int j = 0;j<30;j++){
                System.out.print(plateau[i][j]);
    }
}
    }

    public static void main(String[] args) {
        Plateau a = new Plateau();
        int k =0;
        while(a.hungry && k<2000){
            a.moveAntFrom(a.tuileActu); //deplace la fourmi
            if(a.tuileActu.isFood) break;
            a.affiche();
            k++;
            System.out.println("Step " + k);
            System.out.println();
        }
        for( Tuile z : a.plateau[3][20].Fourmis.get(0).parcours){
            System.out.println(z.id);
            z.Pherom++;
        }
        a.affiche();
        
    }

}
