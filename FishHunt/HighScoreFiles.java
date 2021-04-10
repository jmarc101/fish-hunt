/*
 * Jeudi 30 avril 2020, TP3 IFT1025 - FishHunt - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Classe fournissant des méthodes statiques qui permettent de garder
 * une liste de highscore que dans un fichier.
 */

import java.io.*;
import java.util.ArrayList;

/**

 */
public final class HighScoreFiles {

    /**
     * Le constructeur est défini comme étant privé : cette classe fournit des
     * méthodes statiques et l'"instancier" n'aurait pas de sens.
     */
    private HighScoreFiles(){}



    /**
     * Lecteur de fichier de high scores ligne par ligne.
     * @param filePath     fichier à lire
     * @return un ArrayList ou chaque élément est une ligne du fichier lu
     */
    public static ArrayList<String> readHighScore(String filePath){

        File file = new File(filePath);
        ArrayList<String> list = new ArrayList<>();

        //Lecture ligne par ligne du fichier contenant les high scores
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);

            String textLine;

            //rajoute la ligne au HashSet du dictionnaire
            while((textLine = br.readLine()) != null) {
                list.add(textLine);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;

    }

    /**
     * Methode qui écrit les high scores au fichier
     * @param list          liste à écrire dans le fichier
     * @param filePath      path du fichier dans lequel écrire
     */
    private static void writeHighScore(ArrayList<String> list, String filePath) {

        File file = new File(filePath);

        try {

            FileOutputStream fos = new FileOutputStream(file);

            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fos));

            for (String s : list) {
                br.write(s);
                br.newLine();
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Ajoute un nom et un score à un fichier de high scores, et s'assure que
     * la longueur maximale de high score voulue n'est pas dépassée.
     * Lit le fichier au début puis écrit le nouveau résultat dans le fichier
     * à la fin.
     * Notre convention pour le stockage des données dans le fichier est la suivante:
     *              #<rang> - <nom> - <score>
     * Par exemple: #1 - Jimmy Whooper - 42
     * @param filePath  path du fichier contenant les high scores
     * @param name      le nom a rajouter
     * @param newScore     le score a rajouter
     */
    public static void arrangeHighScore(String filePath, String name, int newScore, int sizeWanted){

        // Lit le fichier; chaque élément de la liste est une ligne du fichier
        ArrayList<String> list = readHighScore(filePath);



        boolean added = false;
        int index = 0;
        int scoreInList;

        // On compare le nouveau score avec ceux déjà présents
        for (int i =0; i < list.size(); i++) {

            // On sépare le string pour lire le dernier numéro, soit le score
            String[] scoreElements = list.get(i).split(" - ");
            scoreInList = Integer.parseInt(scoreElements[scoreElements.length - 1]);

            // Si le nouveau score > score dans la liste, on l'insère à cette position
            if (newScore > scoreInList) {
                list.add(i, "#" + (i + 1) + " - " + name + " - " + newScore);
                added = true;   // Nouveau score ajouté
                index = i;      // On garde l'index du nouveau score
                break;
            }
        }

        // Si le nouveau score est moindre que tous ceux déjà présents
        if (!added) {
            list.add("#" + (list.size() + 1) + " - " + name + " - " + newScore);
        }



        // On update les éléments après le nouveau score
        for (int j = index + 1; j < list.size(); j++){

            // On sépare le string pour faire varier le rang de +1
            String[] scoreElements = list.get(j).split(" - ");
            list.set(j, "#" + (j+1) + " - " + scoreElements[1] + " - " + scoreElements[2]);

        }



        // On enlève les éléments de trop s'il y en a
        while (list.size() > sizeWanted){
            list.remove(list.size()-1);
        }


        // Écriture du fichier
        writeHighScore(list, filePath);

    }

}
