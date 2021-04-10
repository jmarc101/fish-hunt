/*
 * Jeudi 30 avril 2020, TP3 IFT1025 - FishHunt - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Classe qui gère le tableau du jeu, avec ses poissons,
 * ses balles et ses bulles.
 */

import javafx.scene.paint.Color;
import java.util.ArrayList;


public final class Level {


    // Attribut du niveau lui-même
    private final double width;
    private final double height;
    private double numLvl, lvlSpeed;
    private final Color color = Color.DARKBLUE;

    // Listes de différents types d'objets dans le level
    private final ArrayList<Fish> fish = new ArrayList<>();
    private final ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<Bubbles> bubbles = new ArrayList<>();

    // Lien avec la classe principale du jeu
    private final Game game;

    // Doubles et booleans pour les timers
    private double lvlTime;
    private double bubbleTime;
    private boolean bubbleTimer = true;
    private boolean fishTimer = false;
    private boolean specialFishTimer = false;



    /**
     * Constructeur de level
     * @param w         Largeur du tableau voulu
     * @param h         hauteur du tableau voulu
     * @param numLvl    numéro de level du tableau
     * @param game      Lien avec la classe principale du jeu
     */
    public Level(double w, double h, int numLvl, Game game){
        this.width = w;
        this.height = h;
        this.numLvl = numLvl;
        this.game = game;

        // Vitesse de base pour les poissons
        this.lvlSpeed = 200 + (Math.cbrt(numLvl) * 100);

        // Le jeu commence avec des bulles dans le background
        makeBubbles();
        this.bubbleTime = 0;

    }



    /**
     * Update des éléments du level
     * @param dt    delta time entre chaque update en secondes
     */
    public void update(double dt){

        // Temps total augmenté
        lvlTime += dt;

        // Création de nouveaux éléments du niveau; selon timer respectif
        createFish();
        createSpecialFish();
        createBubbles();

        // Updates des différents élément du niveau
        bubbleUpdate(dt);
        fishUpdate(dt);
        ballUpdate(dt);

        // Test des collisions
        ballCollision();

    }



    /**
     * Lance une balle lorsqu'on click sur la souris
     * @param x     pos X du clique
     * @param y     pos Y du clique
     */
    public void clicked(double x, double y){
        balls.add(new Ball(x,y));

    }



    /**
     * Augmentation de niveau
     */
    public void levelUp(){

        numLvl++;

        // Mise à jour de la vitesse du level
        lvlSpeed = 200 + (Math.cbrt(numLvl) * 100);

        // Rénitialisation pour les timers
        lvlTime = 0;
        fishTimer = false;
        specialFishTimer = false;
    }



    // ****************
    // *** POISSONS ***
    // ****************

    /**
     * Update les position des poissons
     * @param dt    delta time entre chaque update en secondes
     */
    private void fishUpdate(double dt){

        // Liste des poissons qui ne servent plus
        ArrayList<Fish> fishToRemove = new ArrayList<>();

        // Update chaque poisson
        for (Fish p : fish) {
            p.update(dt);

            // Vérifie si le poisson dépasse le tableau
            if (p.getOutOfBounds()){
                game.loseLife();        //On perd une vie
                fishToRemove.add(p);    //On va vouloir enlever le poisson
            }
        }


        // Enlève les poissons qui sont sortis du tableau
        fish.removeAll(fishToRemove);
    }



    /**
     * Création d'un poisson normal chaque 3 secondes.
     */
    private void createFish(){

        // lvlTime est le temps total en secondes: création chaque 3 secondes
        // fishTimer pour s'assurer qu'on crée un seul poisson à la fois
        if ( ((int) lvlTime %3  == 0) && fishTimer){
            fish.add(new basicFish(width, height, lvlSpeed));
            fishTimer = false;
        }

        // reset fishTimer après 1 seconde
        else if ((int) lvlTime % 3 != 0){
            fishTimer = true;
        }
    }


    /**
     * Création d'un poisson spécial chaque 5 secondes.
     * Crée soit un crabe, soit une étoile de mer, avec une
     * probabilité de 50/50
     */
    private void createSpecialFish(){

        // lvlTime est le temps total en secondes: création chaque 5 secondes
        // specialFishTimer pour s'assurer qu'on crée un seul poisson à la fois
        // à partir de level 2 tel que spécifié
        if ( ((int) lvlTime %5  == 0) && specialFishTimer && numLvl >=2 ){

            if (Math.random() > .50) {
                fish.add(new CrabFish(width, height, lvlSpeed));
            } else {
                fish.add(new StarFish(width, height, lvlSpeed));
            }
            specialFishTimer = false;
        }

        //reset specialFishTimer après 1 seconde
        if ((int) lvlTime % 5 != 0) {
            specialFishTimer = true;
        }
    }




    // **************
    // *** BALLES ***
    // **************

    /**
     * Updates chacune des balles
     * @param dt    delta time entre chaque update en secondes
     */
    private void ballUpdate(double dt){
        for ( Ball b : balls){
            b.update(dt);
        }
    }



    /**
     * Vérifie s'il y a des collisions entre les balles et les poissons
     */
    private void ballCollision(){

        // Liste des poisson et balles qui ne servent plus
        ArrayList<Ball> ballToRemove = new ArrayList<>();
        ArrayList<Fish> fishToRemove = new ArrayList<>();


        // Test de collission entre chaque balle et tous les poissons
        for ( Ball b : balls){

            // Hauteur de la balle = 0, i.e. elle a "avancé" jusqu'aux poissons
            if (b.getHeight() == 0){
                for (Fish p : fish)
                    if (p.testCollision(b.getX(), b.getY())){
                        fishToRemove.add(p);    // On va enlever le poisson
                        game.scoreUp();         // On augmente le score
                    }

                // On enlève la balle peu importe s'il y a eu collision ou pas
                ballToRemove.add(b);
            }
        }

        // Enlève les éléments de la mémoire
        fish.removeAll(fishToRemove);
        balls.removeAll(ballToRemove);
    }




    // **************
    // *** BULLES ***
    // **************

    /**
     * Création de bulles en 3 groupes de 5 bulles.
     */
    private void makeBubbles() {
        //Réinitialise bubbles pour vider la mémoire
        bubbles = new ArrayList<>();

        Color color =  Color.rgb(0, 0, 255, 0.4);

        // Position en x de la base des groupes de bulles
        double[] baseX = {
                Math.random() * width,
                Math.random() * width,
                Math.random() * width
        };

        for (int i = 0; i < 15; i++) {

            if (i % 3 == 0) {
                bubbles.add(new Bubbles(baseX[2], color, height));
            } else if (i % 2 == 0) {
                bubbles.add(new Bubbles(baseX[1], color, height));
            } else {
                bubbles.add(new Bubbles(baseX[0], color, height));
            }
        }

    }



    /**
     * Création de bulles chaque 3 secondes
     */
    private void createBubbles(){

        // bubbleTime est le temps total en secondes: création chaque 3 secondes
        // bubbleTimer pour s'assurer qu'on crée des bulles une seule fois
        if ( ((int) bubbleTime %3  == 0) && bubbleTimer){
            makeBubbles();         //fabrique les bulles
            bubbleTimer = false;
        }

        //reset bubbleTime après 1 seconde
        else if ((int) bubbleTime % 3 != 0){
            bubbleTimer = true;
        }
    }



    /**
     * Update les bulles
     * @param dt    delta time entre chaque update en secondes
     */
    private void bubbleUpdate(double dt){

        // Temps total pour les bulles est augmenté
        this.bubbleTime += dt;

        // Update chaque bulle
        for (Bubbles b : bubbles) {
            b.update(dt);
        }

    }




    // ***********
    // * GETTERS *
    // ***********

    public Color getColor(){
        return this.color;
    }
    
    public ArrayList<Bubbles> getBubbles(){
        return this.bubbles;
    }


    public ArrayList<Fish> getPoisson() {
        return this.fish;
    }

    public ArrayList<Ball> getBalls(){
        return this.balls;
    }



}
