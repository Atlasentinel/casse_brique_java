package application;

import models.Balle;
import models.Barre;
import models.Brique;
import models.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Fenetre extends Canvas implements KeyListener {

    public static final int LARGEUR = 500;
    public static final int HAUTEUR = 700;

    protected boolean toucheEspace = false;
    protected boolean toucheGauche = false;
    protected boolean toucheDroite = false;
    protected int vitesseRectangle = 10;

    ArrayList<Balle> listeBalles = new ArrayList<>();
    ArrayList<Sprite> listeSprites = new ArrayList<>();
    Barre barre;

    Fenetre()  throws InterruptedException {

        JFrame fenetre = new JFrame();

        this.setSize(LARGEUR, HAUTEUR);
        this.setBounds(0, 0, LARGEUR, HAUTEUR);
        this.setIgnoreRepaint(true);
        this.setFocusable(false);

        fenetre.pack();
        fenetre.setSize(LARGEUR, HAUTEUR );
        fenetre.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fenetre.setResizable(false);
        fenetre.requestFocus();
        fenetre.addKeyListener(this);


        Container panneau = fenetre.getContentPane();
        panneau.add(this);

        fenetre.setVisible(true);
        this.createBufferStrategy(2);

        this.demarrer();
    }

    public void demarrer() throws InterruptedException {

        barre = new Barre();
        listeSprites.add(barre);

        Balle balle = new Balle(100, 200 , Color.WHITE, 30);

        listeBalles.add(balle);
        listeSprites.add(balle);

        // Générer les briques
        int espaceBrique = 10;
        int briqueWidth = 70;
        int briqueHeight = 20;
        int nombreColonnes = 6;
        int nombreRangees = 5;

        for (int row = 0; row < nombreRangees; row++) {
            for (int col = 0; col < nombreColonnes; col++) {
                int x = col * (briqueWidth + espaceBrique);
                int y = row * (briqueHeight + espaceBrique);
                Color couleur = Color.RED; // Couleur de la brique
                Brique brique = new Brique(x, y, briqueWidth, briqueHeight, couleur);
                listeSprites.add(brique);
            }
        }

        while(true) {

            Graphics2D dessin = (Graphics2D) this.getBufferStrategy().getDrawGraphics();
            dessin.setColor(Color.BLACK);
            dessin.fillRect(0,0,LARGEUR,HAUTEUR);

            //----- app -----

            for(Balle b : listeBalles) {

                for (int i = 0; i < listeSprites.size(); i++) {
                    Sprite sprite = listeSprites.get(i);
                    if (sprite instanceof Brique) {
                        Brique brique = (Brique) sprite;
                        if (balle.intersects(brique)) {
                            // Gére la collision ici, en supprimant la brique
                            listeSprites.remove(i);
                            // Inverse la direction de la balle après la collision avec la brique
                            balle.inverserDirectionY();
                            // Sortir de la boucle pour éviter de gérer la collision avec plusieurs briques en même temps
                            break;
                        }
                    }
                }

                b.deplacement();
                b.collider(barre);
            }

            for(Sprite s : listeSprites) {
                s.dessiner(dessin);
            }


            if(toucheEspace) {
                listeBalles.add( new Balle(200, 400 , Color.BLUE, 50));
            }

            //Déplacement à droite de la barre
            if(toucheDroite)
            {
                barre.moveRight(vitesseRectangle);
            }

            //Déplacement à gauche de la barre
            if(toucheGauche)
            {
                barre.moveLeft(vitesseRectangle);
            }

            //---------------

            dessin.dispose();
            this.getBufferStrategy().show();
            Thread.sleep(1000 / 60);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            toucheEspace = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            toucheGauche = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            toucheDroite = true;
        }

    }


    @Override
    public void  keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            toucheEspace = false;
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            toucheGauche = false;
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            toucheDroite = false;
        }
    }
}
