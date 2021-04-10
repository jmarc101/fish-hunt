/*
 * Jeudi 30 avril 2020, TP3 IFT1025 - FishHunt - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Balles lancées par le joueur pour atteindre les poissons.

 */

import javafx.scene.paint.Color;


public final class Ball extends Entity {

    private final double vz; //vitesse à laquelle la balle "avance"; i.e. rapetisse



    /**
     * Constructeur de balle - Object qui cree un cercle de rayon 50 et
     * qui diminu progressivement. Simule un projectile sur le canvas
     * @param x     pos X de la balle
     * @param y     pos Y de lea balle
     */
    public Ball(double x, double y) {
        this.x = x;
        this.y = y;
        this.height = 100; // hauteur = diamètre = 2x rayon de 50px
        this.width = 100;
        this.color = Color.BLACK;

        this.vz = 300; // vitesse à laquelle le rayon rapetisse
    }


    /**
     * update la taille ad 0px
     * @param dt delta time entre chaque update
     */
    public void update(double dt){

        // déjà à 0
        if (this.height == 0) {
            return;
        }

        // rends le cercle plus petit
        // la hauteur est 2x le rayon, donc doit diminuer à 2x la vitesse vz
        this.height -= 2 * vz * dt;

        // ne peut pas être < 0
        if (this.height < 0) {
            this.height = 0;
        }

        // largeur = diamètre = hauteur
        this.width = this.height;
    }
}
