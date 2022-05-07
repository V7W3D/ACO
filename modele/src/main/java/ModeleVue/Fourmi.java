package ModeleVue;

import java.util.ArrayList;
import java.util.Optional;

public class Fourmi {

    static double quantityPherom = 0.1;
    static int Ident=0;
    int id;
    ArrayList<Tuile> parcours = new ArrayList<>();
    boolean food=false;

    public Fourmi(){
        id = Ident;
        Ident++;
    }

    public Tuile avantDerniereTuile(){
        if (parcours.size() <= 1)
            return null;
        else
            return parcours.get(parcours.size() - 2);
    }

    public boolean wentFromIt(Tuile tuile){
        for (Tuile tuileP:parcours){
            if (tuileP.equals(tuile)) return true;
        }
        return false;
    }

    public int getDistance(){
        int distance = 0;
        for (Tuile tuile:parcours){
            distance += tuile.getCost();
        }
        return distance;
    }

    public Optional<Tuile> getLastTuile(){
        return parcours.size()>=1 ? Optional.of(parcours.get(parcours.size() - 1)) : Optional.empty();
    }

    public void init(){
        parcours.clear();
    }

    @Override
    public boolean equals(Object o){
        if (o == null || !( o instanceof Fourmi)) return false;
        return ((Fourmi)o).id == this.id;
    }

    public void copyParcourAndId(Fourmi fourmi){
        this.parcours.clear();
        this.parcours.addAll( fourmi.parcours );
        this.id = fourmi.id;
    }

    public void copyParcour(ArrayList<Tuile> parcour){
        this.parcours.clear();
        this.parcours.addAll( parcour );
    }

}
