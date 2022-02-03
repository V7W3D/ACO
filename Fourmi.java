import java.util.ArrayList;

public class Fourmi {
    static int Ident=0;
    int id;
    ArrayList<Tuile> parcours = new ArrayList<>();
    boolean food=false;
    int disatnce=0;

    public Fourmi(){
        id = Ident;
        Ident++;
    }
}
