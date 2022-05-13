package ModeleVue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
class Pop extends JFrame {
    JLabel titre, nbFourmis, nbPherom;
    JPanel mainePanel;
    JTextField text1;
    JButton apply;
    Pop(Tuile a)
    {
        nbFourmis = new JLabel("nombre de fourmis : "+a.getNbFourmi());
        nbFourmis.setBounds(50,10,150,20);
        String phrrom = String.valueOf(a.getPherom() * 100).substring(0,4);
        nbPherom = new JLabel("taux de pheromones : "+phrrom+"%");
        nbPherom.setBounds(50,30,160,20);
        mainePanel = new JPanel();
        text1 = new JTextField(String.valueOf(a.getCost()));
        apply = new JButton("Apply");
        apply.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int cost = Integer.valueOf(text1.getText());
                a.setCost(cost);
                dispose();
            }
            
        });
        apply.setBounds(50,90,100,20);
        text1.setBounds(50,70,100,20);
        titre = new JLabel("Entrez un cout : ");
        titre.setBounds(50,50,100,20);
        mainePanel.setLayout(null);
        mainePanel.add(titre);
        mainePanel.add(text1);
        mainePanel.add(apply);
        mainePanel.add(nbFourmis);
        mainePanel.add(nbPherom);
        setSize(300, 180);
        setVisible(true);
        setContentPane(mainePanel);
        setResizable(false);
        setTitle("informations");
    }
    
}