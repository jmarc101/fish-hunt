/*
 * Jeudi 30 avril 2020, TP3 IFT1025 - FishHunt - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Cette classe est la classe principale qui sera exécutée, et est la
 * vue dans notre architecture MVC.
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import java.util.ArrayList;


public final class FishHunt extends Application {

    private static final int WIDTH = 640, HEIGHT = 480;
    private final Image cursor = new Image("images/cible.png"); //Image du curseur
    private double cursorX, cursorY;    //Position du curseur
    private int numLvl, numScore;       //No de level et score actuel du joueur, pour affichage
    private boolean lost = false;       //Si on a perdu la partie
    private ArrayList<String> highScores = new ArrayList<>();   //Liste des high scores

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage){

        // Controller, relie la vue (FishHunt) et le jeu
        Controller controller = new Controller(this);



        // ***************
        // ***** GUI *****
        // ***************



        // ------------------ MainMenu ------------------

        Pane mainMenu = new Pane();
        Scene menuScene = new Scene(mainMenu, WIDTH, HEIGHT);


        // LOGO
        ImageView logo = new ImageView("images/logo.png");
        logo.setFitWidth(350);
        logo.setFitHeight(250);
        logo.setLayoutX(145);
        logo.setLayoutY(80);

        // BOUTONS
        VBox buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);
        buttons.setLayoutY(360);
        buttons.setLayoutX(245);
        Button newGame = new Button("Nouvelle Partie");
        newGame.setMinWidth(150);
        newGame.setMinHeight(20);
        Button highScore =  new Button("Meilleurs Scores");
        highScore.setMinWidth(150);
        highScore.setMinHeight(20);
        newGame.setFont(Font.font("Impact", 15));
        highScore.setFont(Font.font("Impact", 15));

        mainMenu.setStyle("-fx-background-color: #00008B;");
        buttons.getChildren().addAll(newGame, highScore);

        // Ajout à mainMenu
        mainMenu.getChildren().addAll(logo, buttons);






        // --------------- SCENE PRINCIPALE (Jeu en cours) -----------------


        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Création Canvas
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext context = canvas.getGraphicsContext2D();


        // TEXTE du score
        Text score = new Text(310, 50,"0");
        score.setFill(Color.WHITE);
        score.setFont(Font.font("Impact", 25));

        // TEXTE du numéro de level OU du texte «Game Over»
        Text levelText = new Text(280, 250, "Level    " + numLvl);
        levelText.setFill(Color.WHITE);
        levelText.setFont(Font.font("Impact", 25));

        // Ajout à root
        root.getChildren().addAll(canvas, score, levelText);





        // -------------------- HIGHSCORE ------------------

        Pane scoreMenu = new Pane();
        Scene scoreScene = new Scene(scoreMenu, WIDTH, HEIGHT);
        scoreMenu.setStyle("-fx-background-color: #00008B;");

        VBox highScoreVBox = new VBox();
        highScoreVBox.setAlignment(Pos.CENTER);
        highScoreVBox.setLayoutX(50);
        highScoreVBox.setLayoutY(10);
        highScoreVBox.setSpacing(15);

        // TEXTE
        Text highText = new Text("Meilleurs Scores");
        highText.setFill(Color.WHITE);
        highText.setFont(Font.font("Impact", 35));

        // LISTE SCORES
        ListView<String> highList = new ListView<>();
        highList.setMaxSize(540,300);
        highList.setMinSize(540,300);
        highList.setFixedCellSize(29.5);


        // BOUTON
        Button scoreMainMenu = new Button("Menu");
        scoreMainMenu.setMinWidth(70);
        scoreMainMenu.setMinHeight(30);
        scoreMainMenu.setFont(Font.font("Impact", 15));

        // Éléments pour ajout d'un nouveau highscore
        HBox input = new HBox();
        input.setSpacing(25);
        input.setAlignment(Pos.CENTER);
        Label nom = new Label("Votre nom :");
        nom.setTextFill(Color.LIGHTGRAY);
        TextField name = new TextField();
        Text points = new Text();
        points.setFill(Color.WHITE);
        Button ajouter = new Button(" Ajouter!");

        ajouter.setFont(Font.font("Impact", 15));
        nom.setFont(Font.font("Impact", 12));
        points.setFont(Font.font("Impact", 12));
        name.setFont(Font.font("Impact", 18));
        points.setFont(Font.font("Impact", 12));


        // Met le tout ensemble
        input.getChildren().addAll(nom, name, points, ajouter );
        highScoreVBox.getChildren().setAll(highText, highList, input, scoreMainMenu);
        scoreMenu.getChildren().addAll(highScoreVBox);
        input.setVisible(false);
        highList.getItems().addAll(highScores);





        // ***********
        // ** TIMER **
        // ***********


        AnimationTimer timer = new AnimationTimer() {

            private long lastTime = 0;

            @Override
            public void handle(long now) {

                if (lastTime == 0){
                    lastTime = now;
                    return;
                }


                // Update le jeu
                double dt = (now - lastTime) * 1e-9;
                controller.updateLevel(dt, context);
                score.setText(""+numScore);

                // Dessine le cursor
                drawCursor(context);

                // Affichage du numéro level si nécessaire
                if (controller.getShowLevel()){
                    levelText.setText("Level    " + numLvl );
                    levelText.setVisible(true);
                }
                else {
                    levelText.setVisible(false);
                }


                // Affichage du texte «Game Over» si nécessaire
                if (controller.getGameOver()){
                    levelText.setText("GAME OVER !");
                    levelText.setFill(Color.RED);
                    levelText.setFont(Font.font("Impact", 50));
                    levelText.setX(200);
                    levelText.setVisible(true);
                }

                // Lorsqu'on fini une partie, affichage des High Scores
                if (lost){
                    stage.setScene(scoreScene);
                    points.setText(" a fait " + numScore + " points!");
                    input.setVisible(true);
                    lost = false;
                }

                lastTime = now;
            }
        };

        timer.start();




        // *************
        // *** Stage ***
        // *************


        stage.setTitle("Fish Hunt");
        stage.getIcons().add(new Image("images/crabe.png"));
        stage.setResizable(false);
        stage.setScene(menuScene);
        stage.show();

        // Si l'utilisateur ferme avec le X de la fenêtre, fermeture de l'app
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,event ->{
            Platform.exit();
            System.exit(0);
        });

        // ESCAPE --> fermeture APP
        stage.getScene().setOnKeyPressed(e->{
            if (e.getCode() == KeyCode.ESCAPE){
                Platform.exit();
                System.exit(0);
            }
        });





        // ********************
        // **** KEY EVENTS ****
        // ********************


        // -------------- EVENTS MAINMENU ------------------


        // Bouton newGame
        newGame.setOnMouseClicked(e->{
            controller.restartGame();
            stage.setScene(scene);

            //replacement de Level Affichage
            levelText.setX(280);
            levelText.setFill(Color.WHITE);
            levelText.setFont(Font.font("Impact", 25));
        });

        // Bouton highScore
        highScore.setOnMouseClicked(e-> stage.setScene(scoreScene));





        // ------------- EVENTS HIGHSCORE --------------

        // Bouton pour retourner au mainMenu
        scoreMainMenu.setOnMouseClicked(e-> {
                stage.setScene(menuScene);
                input.setVisible(false);
        });

        // Bouton pour ajout d'un nouveau highScore
        ajouter.setOnMouseClicked(e->{
            addScore(name, controller, highList, input);
        });

        // Si on appuie sur Enter dans le TextField name
        name.setOnKeyPressed(e->{
            if (e.getCode() == KeyCode.ENTER){
                addScore(name, controller, highList, input);
            }
        });





        //  ----------- EVENTS MAIN GAME ----------

        // Le cursor suit la souris
        scene.setOnMouseMoved(e->{
            cursorX = e.getX();
            cursorY = e.getY();
        });

        // Envoie les coordonnées d'un click de souris au jeu
        scene.setOnMouseClicked(e-> controller.clicked(e.getX(),e.getY()));




        // Touches pour DEBUG
        scene.setOnKeyPressed(e->{
            // Augmente le level de +1
            if (e.getCode() == KeyCode.H){
                controller.levelUp();
            }
            // Augmente le score de +1
            if (e.getCode() == KeyCode.J){
                controller.scoreUp();
            }
            // Augmente le nombre de vies de +1
            if (e.getCode() == KeyCode.K){
                controller.addLife();
            }
            // Force à perdre la partie
            if (e.getCode() == KeyCode.L){
                controller.loseGame();
            }
        });
    }



    // **************************
    // *** MÉTHODES DE DESSIN ***
    // **************************

    /** Dessine le background
     * @param context   GraphicsContext sur lequel dessiner
     * @param color     Couleur du fond d'écran
     */
    public void drawLevel(GraphicsContext context, Color color){
        context.setFill(color);
        context.fillRect(0, 0, WIDTH, HEIGHT);
    }



    /** Dessine le cursor
     * @param context   GraphicsContext sur lequel dessiner
     */
    private void drawCursor(GraphicsContext context){
        context.drawImage(cursor, cursorX -25, cursorY -25, 50, 50);
    }



    /** Dessine les poissons
     * @param context   GraphicsContext sur lequel dessiner
     * @param img       Image à dessiner
     * @param x         Position en x
     * @param y         Position en y
     * @param w         Largeur
     * @param h         Hauteur
     */
    public void drawFish(GraphicsContext context, Image img, double x, double y, double w, double h){
        context.drawImage(img,x,y,w,h);
    }



    /** Dessine les balles et les bulles (les objets ronds)
     *
     * @param context   GraphicsContext sur lequel dessiner
     * @param color     Couleur de la balle ou de la bulle
     * @param x         Position en x
     * @param y         Position en y
     * @param w         Largeur
     * @param h         Hauteur
     */
    public void drawBalls(GraphicsContext context, Color color, double x, double y, double w, double h){
        context.setFill(color);
        context.fillOval(x,y,w,h);
    }


    /** Dessine les poissons représentant le nombre de vies
     * @param context   GraphicsContext sur lequel dessiner
     * @param numLife   Nombre de vies du joueur
     */
    public void drawLife(GraphicsContext context, int numLife){

        Image fishImg = new Image("/images/fish/00.png");

        for (int i=0; i<numLife; i++){
            context.drawImage(fishImg, 250 + i*50,70,30,30);
        }
    }




    // ***********************
    // *** AUTRES MÉTHODES ***
    // ***********************

    /**
     * Update de certains paramètres du jeu, enregistrés dans la vue pour
     * servir pour l'affichage de ces paramètres.
     * @param intLvl    numero du level
     * @param score     score
     */
    public void updateLvl(int intLvl, int score){
        numLvl = intLvl;
        this.numScore = score;
    }

    /**
     * Update la liste des high scores avec le nouveau score fait par le joueur.
     * @param name          TextField ou le joueur écrit son nom
     * @param controller    Controller du MVC
     * @param highList      Liste contenant les high scores
     * @param input         HBox avec les éléments pour l'ajout du score
     */
    private void addScore(TextField name, Controller controller, ListView<String> highList, HBox input){
        // Si input vide on rentre "Jouer Secret"
        if (name.getText().length() == 0){
            controller.addNewHighScore("Joueur Secret", numScore);
        }

        // Sinon, le nom provient de l'input du TextField name
        else {
            controller.addNewHighScore(name.getText(), numScore);
        }

        // Mise à jour du high score pour l'affichage
        highScores = controller.getHighScore();

        highList.getItems().clear();
        highList.getItems().addAll(highScores);
        input.setVisible(false);
    }



    // ***************
    // *** Setters ***
    // ***************

    public void setHighScores(ArrayList<String> highScore){
        this.highScores = highScore;
    }

    public void setLost(){
        this.lost = true;
    }



    // ***************
    // *** Getters ***
    // ***************

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }


}