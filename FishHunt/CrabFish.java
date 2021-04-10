/*
 * Jeudi 30 avril 2020, TP3 IFT1025 - FishHunt - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Poisson spécial de type crabe, qui n'a pas de vitesse ni de gravité en Y,
 * et avance/recule en alternance en X. Sa vitesse en X est 1.3 fois plus
 * vite que les poissons normaux.
 */

import java.util.Timer;
import java.util.TimerTask;

public final class CrabFish extends Fish {

    // booleans pour l'inversement de la vitesse en X
    private boolean timerBool1 = true;
    private boolean timerBool2 = false;



    /**
     * Crabe est un poisson special qui bouge de gauche a droite
     * il n'a pas de gravite sur ce poisson
     * @param lvlWidth             largeur of level
     * @param lvlHeight            hauteur of level
     * @param lvlSpeed             vitesse du poisson
     */
    public CrabFish(double lvlWidth, double lvlHeight, double lvlSpeed) {
        super(lvlWidth, lvlHeight, lvlSpeed);

        this.chooseImage("images/crabe.png");

        // Vitesse en X est 1.3 * plus vite que les poissons normaux
        // Direction de la vitesse déjà choisi dans le constructeur super
        this.vx = 1.3 * this.vx;

        // Aucune gravité en Y
        this.vy = 0;
        this.ay = 0;
    }


    /**
     * update les paramètres
     * @param dt    delta time entre chaque update en secondes
     */
    @Override
    public void update(double dt) {
        super.update(dt);
        inverseVX();
    }



    /**
     * Fonction pour faire bouger le crab à gauche et à droite.
     * Utilise des timer et si c'est le temps, inverse la vitesse en X.
     */
    private void inverseVX(){

        Timer timer = new Timer();

        // Avance 0.5 sec
        if (timerBool1){
            timerBool1 = false;

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // Exécuté après 0.5 sec de délai
                    vx = -vx;           // Inverse la vitesse pour faire reculer
                    timerBool2 = true;  // Part l'autre timer
                }
            }, 500);
        }

        // Recule 0.25 sec
        else if (timerBool2){
            timerBool2 = false;

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // Exécuté après 0.25 sec de délai
                    vx = -vx;           // Inverse la vitesse pour faire avancer
                    timerBool1 = true;  // Part l'autre timer
                }
            }, 250);
        }

    }
}
