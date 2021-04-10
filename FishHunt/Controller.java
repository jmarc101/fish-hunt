/*
 * Jeudi 30 avril 2020, TP3 IFT1025 - FishHunt - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Controller dans notre architecture MVC.
 */

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;


public final class Controller {

    private final int WIDTH, HEIGHT;    //Dimensions de la fenêtre
    private boolean ended;              //Partie terminée ou pas

    private final FishHunt fishHunt;    //Vue
    private Game game;                  //Classe qui s'occupe de la logique du jeu



    /**
     * Constructeur
     * @param fishHunt la vue, qui se passe elle-même en paramètre
     *                     lors de la construction du controller
     */
    public Controller(FishHunt fishHunt){
        this.WIDTH = fishHunt.getWIDTH();
        this.HEIGHT = fishHunt.getHEIGHT();
        this.fishHunt = fishHunt;

        //Initialise certains attributs
        this.restartGame();
    }



    /**
     * Recommence la partie, réinitialise certains attributs.
     * Utilisé dans le constructeur et par la vue (fishHunt)
     */
    public void restartGame(){

        // Commence le Jeu
        this.game = new Game(WIDTH, HEIGHT);

        // Update les Highscores lorsqu'on recommence
        this.fishHunt.setHighScores(this.game.getHighScore());
        this.ended = false;

    }



    /**
     * Update des différents éléments du jeu
     * @param dt        delta time entre chaque update
     * @param context   canvas a dessiner dessus
     */
    public void updateLevel(double dt, GraphicsContext context){

        // -------------------- UPDATE JEU ----------------------

        game.update(dt);

        //Informe la vue pour le no de level et le score
        fishHunt.updateLvl(game.getNumLvl(), game.getScore());

        //Informe la vue si le jeu n'est plus en train de jouer, pour changer de scene
        if (!game.getPlaying() && !ended){
            fishHunt.setLost();
            ended = true;
        }



        // ------------------ DESSINE JEU ----------------------

        Level level = game.getLevel();

        // Dessine la couleur de fond
        fishHunt.drawLevel(context, level.getColor());

        // Dessine les bulles
        for (Bubbles b : level.getBubbles()){
            fishHunt.drawBalls(context, b.getColor(),b.getX(),
                    b.getY(), b.getWidth(),b.getHeight());
        }

        // Dessine les poissons
        for (Fish p : level.getPoisson()){
            fishHunt.drawFish(context, p.getImage(), p.getX(),
                    p.getY(), p.getWidth(), p.getHeight());
        }

        // Dessine les balles
        for (Ball b : level.getBalls()) {
            fishHunt.drawBalls(context, b.color, b.getX() - b.getWidth()/2,
                    b.getY() - b.getHeight()/2, b.getWidth(), b.getHeight() );
        }

        // Dessine les poissons représentant les vies
        fishHunt.drawLife(context, game.getNumLife());
    }



    /**
     * Transmet l'information de la vue vers le jeu pour le nouveau score du joueur.
     * Retourne ensuite l'information de tous les high scores du jeu vers la vue
     * @param name  nouveau nom à ajouter
     * @param score nouveau score à ajouter
     */
    public void addNewHighScore(String name, int score){
        game.addNewHighScore(name,score);
    }




    // ***********************************************
    // *** Méthodes de liens de la vue vers le jeu ***
    // ***********************************************

    public void clicked(double x, double y){
        game.clicked(x,y);
    }

    public void levelUp(){
        //Paramètre true car vient de la vue, donc on a appuyé sur H (debug)
        game.levelUp(true);
    }

    public void scoreUp(){
        game.scoreUp();
    }

    public void addLife(){
        game.addLife();
    }

    public void loseGame(){
        game.gameOver();
    }




    // ***************
    // *** Getters ***
    // ***************

    public boolean getShowLevel(){
        return game.getShowLvl();
    }

    public boolean getGameOver(){
        return game.getGameOver();
    }

    public ArrayList<String> getHighScore(){
        return game.getHighScore();
    }


}
