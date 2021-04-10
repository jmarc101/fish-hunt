/*
 * Jeudi 30 avril 2020, TP3 IFT1025 - FishHunt - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Classe abstraite pour les différentes entités qui feront partie
 * du Level.
 */

import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public abstract class Entity {

    // Couleur ou image pour dessiner l'entité selon le cas
    protected Color color;
    protected Image image;

    // Autres attributs de base
    protected double width,height;
    protected double x,y;
    protected double vx, vy;
    protected double ay;
    protected double totalTime = 0;




    // ***********
    // * GETTERS *
    // ***********

    public Color getColor() {
        return this.color;
    }

    public Image getImage(){
        return this.image;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }


}