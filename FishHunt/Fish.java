/*
 * Jeudi 30 avril 2020, TP3 IFT1025 - FishHunt - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Classe abstraite qui contient les méthodes et fonctionnalités
 * de base pour les poissons qui se trouve dans notre jeu.
 * Les poissons arrivent soit de la gauche ou la droite de l'écran, et
 * finissent par sortir de l'écran de l'autre côté ou par être tués par une
 * balle.
 */


import javafx.scene.image.Image;


public abstract class Fish extends Entity {

    //true si arrive de la gauche de l’écran
    private final boolean left;

    // position en X au-delà de laquelle le poisson est hors-écran
    private final double outValue;

    /**
     * Constructeur de base, sera appelé en super().
     * @param lvlWidth         largeur du tableau
     * @param lvlHeight        hauteur du tableau
     * @param lvlSpeed         vitesse de base du poisson
     */
    protected Fish(double lvlWidth, double lvlHeight, double lvlSpeed){
        this.width = 60;
        this.height = 60;
        this.vx = lvlSpeed;

        // Arrive de la gauche ou de la droite avec probabilité 50/50
        this.left = (Math.random() > 0.5);

        // Position initiale en x, valeur pour hors-écran (outValue), et
        // direction de la vitesse : selon de quel côté regarde le poisson.
        if (this.left) {
            this.x = -this.width;
            this.outValue = lvlWidth;
        } else {
            this.x = lvlWidth;
            this.outValue = -this.width;
            this.vx = -this.vx;
        }

        // Position initiale en y entre 1/5 et 4/5 de la hauteur
        this.y = (Math.random() * 3 * lvlHeight / 5) + lvlHeight/5;

    }



    /**
     * Update des paramètres communs à chaque poisson, à appeler en super().
     * @param dt    delta time entre chaque update en secondes
     */
    public void update(double dt) {
        this.totalTime += dt;

        // Physique des poissons
        this.vy += this.ay * dt;
        this.x += this.vx * dt;
        this.y += this.vy * dt;

    }



    /**
     * Methode pour choisir une image pour nos poissons
     * et renverser la photo dépendant de son point de départ
     * @param url   l'adresse fichier de la photo voulu
     */
    protected void chooseImage(String url) {
        this.image = new Image(url);

        if (!left){
            this.image = ImageHelpers.flop(this.image);
        }
    }


    /**
     * Test une collission entre un point (X,Y) et le poisson actuel
     * @param posX      position X a tester
     * @param posY      position Y a tester
     * @return          retourne si collision
     */
    public boolean testCollision(double posX, double posY){
        if (posX >= this.x && posX < this.x + this.width) {
            return (posY > this.y && posY < this.y + this.height);
        }
        return false;
    }



    /**
     * Teste si le poisson est hors de l'écran
     * @return      le statut hors-écran du poisson (boolean)
     */
    public boolean getOutOfBounds() {

        // Dépend de quel côté le poisson regarde
        if (left) {
            return (this.x > this.outValue);
        } else {
            return (this.x < this.outValue);
        }
    }
}
