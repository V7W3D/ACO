package ModeleVue;

import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JMenuBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Vue extends JFrame {
    private static final int hauteur = 600;
    private static final int largeur = 600;
    private int n;
    private int m;
    private Plateau plateau;
    private JLabel[][] textToPrint;
    public Tuile[][] mesTuiles;
    private String ressourcePath;
    private ImageIcon iconeAntResized;
    private JMenuBar menuBar;
    private boolean ColonieChoisie = false;
    private boolean FoodChoisie = false, lancer = false;

    public void initColor() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!mesTuiles[i][j].isObstacle)
                    mesTuiles[i][j].setBackground(Color.WHITE);
            }
        }
    }

    private ImageIcon resizedIcone(String path, int newsize) {
        ImageIcon imageIcon = new ImageIcon(path); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(newsize, newsize, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }

    public Vue(int n, int m) {
        this.n = n;
        this.m = m;
        this.ressourcePath = System.getProperty("user.dir") + "\\src\\resources";
        System.out.println(ressourcePath);
        setSize(hauteur, largeur);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Simulation Algo des colonies de Fourmis");
        textToPrint = new JLabel[n][m];
        mesTuiles = new Tuile[n][m];
        JPanel container = new JPanel();
        container.setLayout(new GridLayout(n, m));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                textToPrint[i][j] = new JLabel();
                mesTuiles[i][j] = new Tuile(i,j);
                mesTuiles[i][j].add(textToPrint[i][j]);
                mesTuiles[i][j].setBackground(new Color(255, 255, 255));
                mesTuiles[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY));
                container.add(mesTuiles[i][j]);
            }
        }
        setContentPane(container);
    }

    private JMenuBar initMenuBar() {
        JMenuBar menu = new JMenuBar();
        JMenu menuOPtions = new JMenu("Options");
        JMenu modifications = new JMenu("Parametres");
        JMenuItem modifierParametres = new JMenuItem("Modifications des parametres");
        JMenuItem afficherParametres = new JMenuItem("Afficher les parametres");
        JMenuItem pause = new JMenuItem("Pause");
        JMenuItem restart = new JMenuItem("Restart");
        JMenu LancerSim = new JMenu("Lancer");
        JMenuItem lancerLaSimulation = new JMenuItem("Lancer Simulation");
        LancerSim.add(lancerLaSimulation);
        modifications.add(modifierParametres);
        modifications.add(afficherParametres);
        lancerLaSimulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!lancer) {
                    plateau.simulation();
                    lancer = true;
                }
            }
        });
        modifierParametres.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plateau.pauseAllThreads();
                ModificationsMenu modMenu = new ModificationsMenu();
                modMenu.setVisible(true);
                modMenu.setLocationRelativeTo(null);
            }
        });
        afficherParametres.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                plateau.pauseAllThreads();
                ModificationsMenu modMenu = new ModificationsMenu(true);
                modMenu.setVisible(true);
                modMenu.setLocationRelativeTo(null);
            }

        });
        menuOPtions.add(pause);
        menuOPtions.add(restart);
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                plateau.pauseAllThreads();
            }
        });
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Fenetre();
            }
        });
        menu.add(menuOPtions);
        menu.add(LancerSim);
        menu.add(modifications);
        return menu;
    }

    public void init() {
        menuBar = initMenuBar();
        setJMenuBar(menuBar);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int i1 = i, j1 = j;
                mesTuiles[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (!ColonieChoisie) {
                            plateau.initDepart(i1, j1);
                            ColonieChoisie = true;
                            textToPrint[i1][j1].setIcon(resizedIcone(ressourcePath + "/home.png", 350 / Math.max(plateau.getHeight(),plateau.getWidth() )));
                            mesTuiles[i1][j1].setColony(true);
                        }
                        else if (!FoodChoisie) {
                            plateau.initFood(i1, j1);
                            FoodChoisie = true;
                            textToPrint[i1][j1].setIcon(resizedIcone(ressourcePath + "/food.png", 350 / Math.max(plateau.getHeight(),plateau.getWidth() )));
                        } else {
                            if (SwingUtilities.isRightMouseButton(e)) {
                                new Pop(mesTuiles[i1][j1]);
                            } else {
                                if (mesTuiles[i1][j1].isObstacle) {
                                    mesTuiles[i1][j1].setIsObstacle(false);
                                    mesTuiles[i1][j1].setBackground(Color.white);
                                } else {
                                    mesTuiles[i1][j1].setIsObstacle(true);
                                    mesTuiles[i1][j1].setBackground(Color.black);
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    public void setPlateau(Plateau plateau) {
        this.plateau = plateau;
        iconeAntResized = resizedIcone(this.ressourcePath + "/ant.png", 350 / Math.max(plateau.getHeight(),plateau.getWidth()));
    }

    public void printAnt(int i, int j) {
        textToPrint[i][j].setIcon(iconeAntResized);
    }

    public void removeAnt(int i, int j) {
        textToPrint[i][j].setIcon(null);
    }

    private class ModificationsMenu extends JFrame {
        private JSlider alphaSlide, betaSlide, inputVap, inputDelayVap, inputDelayAnt;
        private JPanel mainContainer, containersLables, containerData;
        private JLabel field1, field2, field3, field4, field5;

        public ModificationsMenu(boolean showInfo){
            JMenuBar menu = new JMenuBar();
            JMenu exit = new JMenu("exit");
            this.setJMenuBar(menu);
            menu.add(exit);
            exit.addMouseListener(initMenuButtons(()->{
                plateau.restartAllThreads();
                dispose();
            }));
            setTitle("Parametres");
            setSize(300,250);
            mainContainer = new JPanel();
            mainContainer.setLayout(new GridLayout(0,1));
            mainContainer.add(new JLabel("  Alpha : "+plateau.getAlpha()));
            mainContainer.add(new JLabel("  Beta : "+plateau.getBeta()));
            mainContainer.add(new JLabel("  Taux de vaporation : "+(int)(plateau.getTauxDeVaporation() * 100)+ "%"));
            mainContainer.add(new JLabel("  Temps d'evaporation : "+plateau.getDelayPheroms() + "ms"));
            mainContainer.add(new JLabel("  Nombre de fourmis : "+plateau.getNbFourmi()));
            mainContainer.add(new JLabel("  Vitesse fourmis : "+plateau.getDelayAnt() +"ms"));
            setContentPane(mainContainer);
            this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        }

        private void initTextFields() {
            field1 = new JLabel("       alpha : 1");
            field2 = new JLabel("       beta : 2");
            field3 = new JLabel("       taux de vaporation : 1%");
            field4 = new JLabel("       temps d'evaporation : 1000ms");
            field5 = new JLabel("       temps de deplacement fourmis : 100ms");
        }

        private MouseListener initMenuButtons(MenuFonction fonction) {
            return new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    fonction.run();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            };
        }

        private void setSlidersToDefaultValue() {
            alphaSlide.setValue(plateau.getAlpha());
            field1.setText("        alpha : " + plateau.getAlpha());
            betaSlide.setValue(plateau.getBeta());
            field2.setText("        beta : " + plateau.getBeta());
            int tauxDeVap = (int) (plateau.getTauxDeVaporation() * 100);
            inputVap.setValue(tauxDeVap);
            field3.setText("        taux de vaporation : " + tauxDeVap + "%");
            inputDelayVap.setValue(plateau.getDelayPheroms());
            field4.setText("        temps d'evaporation : " + plateau.getDelayPheroms() + "ms");
            inputDelayAnt.setValue(plateau.getDelayAnt());
            field5.setText("  temps de deplacement fourmis : " + plateau.getDelayAnt() + "ms");
        }

        public ModificationsMenu() {
            JMenuBar menu = new JMenuBar();
            JMenu apply = new JMenu("Apply");
            apply.addMouseListener(initMenuButtons(() -> {
                plateau.setAlphaAndBeta(alphaSlide.getValue(), betaSlide.getValue());
                double tauxDeVap = (double) (inputVap.getValue() / 100.0);
                plateau.setTauxDeVaporation(tauxDeVap);
                plateau.setdelayAntandVap(inputDelayAnt.getValue(), inputDelayVap.getValue());
                dispose();
                plateau.restartAllThreads();
            }));
            JMenu cancel = new JMenu("exit");
            cancel.addMouseListener(initMenuButtons(() -> {
                plateau.restartAllThreads();
                dispose();
            }));
            JMenu back = new JMenu("Back to origin");
            back.addMouseListener(initMenuButtons(() -> {
                plateau.setParmsToDefault();
                setSlidersToDefaultValue();
            }));
            setJMenuBar(menu);
            menu.add(apply);
            menu.add(back);
            menu.add(cancel);
            initTextFields();
            containerData = new JPanel();
            containerData.setLayout(new GridLayout(0, 1));
            containersLables = new JPanel();
            containersLables.setLayout(new GridLayout(0, 1));
            JLabel textLabel1 = new JLabel("alpha : ");
            JLabel textLabel2 = new JLabel("beta : ");
            JLabel textLabel3 = new JLabel("taux de vaporation : ");
            JLabel textLabel4 = new JLabel("temps d'evaporation : ");
            JLabel textLabel5 = new JLabel("temps de deplacement fourmis : ");
            inputVap = initSliders(5, 100, 5, 10);
            inputDelayVap = initSliders(1000, 8000, 1000, 1000);
            inputDelayAnt = initSliders(10, 1010, 100, 100);
            mainContainer = new JPanel();
            alphaSlide = initSliders(1, 16, 1, 1);
            betaSlide = initSliders(1, 16, 1, 1);
            mainContainer.setLayout(new GridLayout(0, 2));
            containersLables.add(textLabel1);
            containersLables.add(alphaSlide);
            containersLables.add(textLabel2);
            containersLables.add(betaSlide);
            containersLables.add(textLabel3);
            containersLables.add(inputVap);
            containersLables.add(textLabel4);
            containersLables.add(inputDelayVap);
            containersLables.add(textLabel5);
            containersLables.add(inputDelayAnt);
            containerData.add(field1);
            containerData.add(field2);
            containerData.add(field3);
            containerData.add(field4);
            containerData.add(field5);
            mainContainer.add(containersLables);
            mainContainer.add(containerData);
            initSlidersListner();
            setContentPane(mainContainer);
            setSize(500, 550);
            setTitle("Modifications");
            setSlidersToDefaultValue();
            this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        }

        private void initSlidersListner() {
            alphaSlide.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    field1.setText("        alpha : " + alphaSlide.getValue());
                }
            });
            betaSlide.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    field2.setText("        beta : " + betaSlide.getValue());
                }
            });
            inputVap.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    field3.setText("        taux de vaporation : " + inputVap.getValue() + "%");
                }
            });
            inputDelayVap.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    field4.setText("        temps d'evaporation : " + inputDelayVap.getValue() + "ms");
                }
            });
            inputDelayAnt.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    field5.setText("  temps de deplacement fourmis : " + inputDelayAnt.getValue() + "ms");
                }
            });
        }

        private JSlider initSliders(int min, int max, int val, int spacing) {
            JSlider slider = new JSlider(min, max, val);
            slider.setPaintTrack(true);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.setMajorTickSpacing(spacing);
            slider.setMinorTickSpacing(spacing);
            return slider;
        }
    }
    
    @FunctionalInterface
    interface MenuFonction {
        public void run();
    }
}