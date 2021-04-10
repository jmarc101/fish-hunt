/*
 * Jeudi 30 avril 2020, TP3 IFT1025 - FishHunt - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Poisson spécial de type Étoile de mer. Elle oscille en Y à travers le
 * tableau, sans gravité par contre. Sa vitesse en X est la même que pour
 * les poissons normaux.
 */

public final class StarFish extends Fish {

    private final double amplitudeY;    // Amplitude des oscillations
    private final double angFreq;       // Fréquence angulaire des oscillation
    private final double initialHeight; // Hauteur initiale


    /**
     * Constructeur de l'étoile de mer - un poisson spécial qui oscille de
     * haut en bas a travers le tableau
     * @param lvlWidth         largeur du tableau
     * @param lvlHeight        hauteur du tableau
     * @param lvlSpeed         vitesse de l'etoile
     */
    public StarFish(double lvlWidth, double lvlHeight, double lvlSpeed){
        super(lvlWidth, lvlHeight, lvlSpeed);
        this.initialHeight = this.y;
        this.chooseImage("images/star.png");

        this.vy = 0;
        this.ay = 0;

        this.amplitudeY = 25; //amplitude totale = 50 en comptant sin négatifs
        this.angFreq = (2 * Math.PI); //fréquence en Hz = freqAng/2pi; ici 1 Hz
    }

    /**
     * updates les paramètres
     * @param dt    deltaTime
     */
    @Override
    public void update(double dt){

        super.update(dt);

        // oscillations
        y = initialHeight + (amplitudeY * Math.sin(angFreq * totalTime));
    }
}
