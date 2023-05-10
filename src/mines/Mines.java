package mines;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

// Source: http://zetcode.com/tutorials/javagamestutorial/minesweeper/

public class Mines extends JFrame {
    private static final long serialVersionUID = 4772165125287256837L;

    // Définition des constantes
    private final int WIDTH = 250; // Largeur de la fenêtre
    private final int HEIGHT = 290; // Hauteur de la fenêtre

    private JLabel statusbar; // Barre d'état

    // Constructeur de la classe
    public Mines() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ferme la fenêtre lorsque l'utilisateur clique sur le bouton
                                                        // de fermeture
        setSize(WIDTH, HEIGHT); // Définit la taille de la fenêtre
        setLocationRelativeTo(null); // Centre la fenêtre sur l'écran
        setTitle("Minesweeper"); // Définit le titre de la fenêtre

        statusbar = new JLabel(""); // Crée une nouvelle barre d'état
        add(statusbar, BorderLayout.SOUTH); // Ajoute la barre d'état en bas de la fenêtre

        add(new Board(statusbar)); // Ajoute le Board de jeu

        setResizable(false); // Empêche la redimension de la fenêtre
        setVisible(true); // Rend la fenêtre visible
    }

    // Méthode principale de la classe
    public static void main(String[] args) {
        new Mines(); // Crée une nouvelle instance de la classe Mines
    }
}