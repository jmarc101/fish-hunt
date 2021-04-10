/*
 * Jeudi 30 avril 2020, TP3 IFT1025 - FishHunt - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Bulles qui seront dans le fond de l'écran comme décoration.
 */

import javafx.scene.paint.Color;


public final class Bubbles extends Entity{

    private final double initialHeight;


    /**
     * Constructeur.
     * Classe reprise du TP2.
     * @param x             position de la base en X
     * @param color         couleur désirée
     * @param heightLevel   haut du tableau pour partir du bas
     */
    public Bubbles(double x, Color color, double heightLevel){
        this.vy = (Math.random() * 100) + 350;
        this.initialHeight = heightLevel + 40;
        this.y = initialHeight;
        this.height = ((Math.random() * 30) + 10);
        this.width = this.height ;
        this.x = (Math.random() * 40) -20 + x ;
        this.color = color;
    }



    /**
     * Update de la position des bulles.
     * Ici la convention utilisée est que (0,0) est en haut à gauche de l'écran
     * @param dt    deltatime entre updates
     */
    public void update(double dt){
        this.totalTime += dt;
        this.y = this.initialHeight - (this.totalTime * this.vy);
    }



}