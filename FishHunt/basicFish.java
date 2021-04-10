/*
 * Jeudi 30 avril 2020, TP3 IFT1025 - FishHunt - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Poissons normaux du jeu. Leur vitesse en X dépend du niveau auquel le
 * joueur est rendu. Ils ont une gravité en Y
 */

public final class basicFish extends Fish {

    /**
     * Constructeur.
     * Les poissons normaux sont choisis de façon aléatoire (pour l'image).
     * Tout le reste est fait dans la classe parent.
     * @param lvlWidth         largeur du tableau
     * @param lvlHeight        hauteur du tableau
     * @param lvlSpeed         vitesse du poisson
     */
    public basicFish(double lvlWidth, double lvlHeight, double lvlSpeed){
        super(lvlWidth, lvlHeight, lvlSpeed);

        // choisit aléatoirement l'image et la couleur
        this.chooseImage("images/fish/0" + (int)Math.floor(Math.random()* 8) + ".png");
        this.color = ImageHelpers.randomColor(); // méthode crée par nous
        this.image = ImageHelpers.colorize(this.image, this.color);

        // vitesse et accélération en y
        this.vy = - (Math.random() * 100 + 100);
        this.ay = 100;
    }
}
