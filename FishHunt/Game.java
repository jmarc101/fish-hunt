/*
 * Jeudi 30 avril 2020, TP3 IFT1025 - FishHunt - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Classe supervise la gestion de la logique du jeu au complet.
 */

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public final class Game {

    // Attributs de base
    private int numLvl, numLife, score, lvlUpCountdown;

    // Tableau du jeu
    private final Level level;

    // Gestion des High Score
    private ArrayList<String> highScore;
    private final String highScoreFile = "assets/HighScores";

    // Booleans pour les fonctions d'affichage avec du temps
    private boolean showLvl = false;
    private boolean gameOver = false;

    // Jeu en cours ou pas
    private boolean playing;



    /**
     * Constructeur
     * @param width largeur du tablbeau
     * @param height hauteur du tableau
     */
    public Game(double width, double height ){

        // Les attributs de base pour jouer
        this.numLvl = 0;           // Numéro de niveau
        this.numLife = 3;           // Nombre de vies
        this.score = 0;             // Score

        // Décompte de l'incrément de score à faire avant d'augmenter de niveau;
        // initialement à zéro donc va augmenter au niveau 1 dès le début.
        this.lvlUpCountdown = 0;

        // Création du niveau pour jouer
        this.level = new Level(width, height, numLvl, this);

        this.playing = true;

        //file reader pour les high scores
        this.highScore = HighScoreFiles.readHighScore(highScoreFile);
        
    }



    /**
     * Update du jeu à chaque intervalle de temps
     * @param dt delta time en secondes
     */
    public void update(double dt){

        if (!this.playing) {
            return;
        }

        this.level.update(dt);

        // Si on est mort
        if (this.numLife == 0){
            gameOver();
        }

        // Augmente le niveau si nécessaire;
        // paramètre false car on a pas appuyé sur H (debug).
        levelUp(false);

    }




    // *****************
    // ** LOGIQUE JEU **
    // *****************


    /**
     * Transmet la position en X et Y du click de la souris pour attaquer poisson
     * @param x     POS X
     * @param y     POS Y
     */
    public void clicked(double x, double y){
        level.clicked(x, y);
    }



    /**
     * Augmente le niveau si doit être augmenté
     * @param debug true si méthode appelée parce qu'on appuie sur H (debug)
     */
    public void levelUp(boolean debug){

        // Décompte à zéro, ou appel avec la touche H; sinon méthode ne fait rien
        if (lvlUpCountdown == 0 || debug ) {

            //réinitialise le décompte; prochain level up quand score augmente de +5
            lvlUpCountdown = 5;

            numLvl++;
            this.level.levelUp();

            // Affichage du texte indiquant le level, durée 3 secondes
            this.showLvl = true;    //boolean qui informe la vue

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    showLvl = false;
                }
            }, 3000);
        }
    }



    /**
     * Augmenter le score.
     */
    public void scoreUp(){
        if(!gameOver) {
            this.score++;
            this.lvlUpCountdown--;
        }
    }



    /**
     * Augmente le nombre de vie jusqu'à 3 maximum.
     */
    public void addLife(){
        if (numLife >= 3) {
            return;
        }

        this.numLife++;
    }



    /**
     * Perdre une vie jusqu'à minimum zéro
     */
    public void loseLife(){
        if (numLife > 0) {
            this.numLife--;
        }
    }



    /**
     * Termine la partie
     */
    public void gameOver(){
        // Avertira la vue pour affichage tu texte «Game Over»
        this.gameOver = true;

        // Arrête le jeu apres 3 secondes
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                playing = false;
            }
        }, 3000);
    }



    /**
     * Réorganise les High Scores avec un nouveau joueur et son score
     * @param name le nom du joueur
     * @param score son score
     */
    public void addNewHighScore(String name, int score){

        // Réarrange les high scores avec le nouveau score du joueur
        // Cette méthode statique met à jour directement dans le fichier
        HighScoreFiles.arrangeHighScore(highScoreFile, name, score, 10);

        // Récupère la nouvelle information du fichier mis à jour et la stocke sous forme de liste
        this.highScore = HighScoreFiles.readHighScore(highScoreFile);
    }



    // ***********
    // * GETTERS *
    // ***********

    public boolean getPlaying(){
        return this.playing;
    }

    public boolean getGameOver(){
        return this.gameOver;
    }

    public boolean getShowLvl(){
        return this.showLvl;
    }

    public int getNumLife() {
        return numLife;
    }

    public int getScore() {
        return score;
    }

    public int getNumLvl(){
        return this.numLvl;
    }

    public Level getLevel(){
        return this.level;
    }

    public ArrayList<String> getHighScore(){
        return this.highScore;
    }


}
