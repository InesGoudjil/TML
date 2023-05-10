package mines;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JPanel {
    private static final long serialVersionUID = 6195235521361212179L;

    // Constantes pour différents types de cellules
    private final int NUM_IMAGES = 13;
    private final int CELL_SIZE = 15;
    private final int COVER_FOR_CELL = 10;
    private final int MARK_FOR_CELL = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;
    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_MARK = 11;
    private final int DRAW_WRONG_MARK = 12;

    private int[] field; // un tableau pour stocker l'état de chaque cellule sur la carte
    private boolean inGame;
    private int minesLeft;
    private Image[] img; // un tableau d'images à dessiner sur les cellules
    private int mines = 40; // nombre total de mines
    private int rows = 16; // nombre de lignes sur le Board
    private int cols = 16; // nombre de colonnes sur le Board
    private int allCells;
    private JLabel statusbar; // un label pour montrer l'état du jeu

    // Constructeur
    public Board(JLabel statusbar) {

        this.statusbar = statusbar;

        img = new Image[NUM_IMAGES];

        // charge toutes les images à dessiner sur les cellules
        for (int i = 0; i < NUM_IMAGES; i++) {
            img[i] = (new ImageIcon(getClass().getClassLoader().getResource((i)
                    + ".gif"))).getImage();
        }

        setDoubleBuffered(true);

        addMouseListener(new MinesAdapter()); // ajoute un MouseListener pour les cellules
        newGame(); // démarre une nouvelle Game

    }

    // initialise un nouveau Game
    public void newGame() {

        // Crée un nouveau objet Random
        Random random = new Random();
        // Déclare les variables à utiliser plus tard
        int current_col;
        int position;
        int i;
        int cell;

        // Définissez inGame sur true, indiquant que le jeu est en cours d'être joué
        inGame = true;
        // Fixe le nombre de mines restantes au nombre total de mines
        minesLeft = mines;

        // Calcule le nombre total de cellules sur le Board
        allCells = rows * cols;
        // Crée un nouveau tableau pour représenter le Board de jeu
        field = new int[allCells];

        // Définir toutes les cellules à couvrir initialement
        for (i = 0; i < allCells; i++)
            field[i] = COVER_FOR_CELL;

        // Mise à jour de la barre d'état pour afficher le nombre de mines restantes
        statusbar.setText(Integer.toString(minesLeft));

        // Placer les mines sur le Board au hasard
        i = 0;
        while (i < mines) {

            // Génère une position aléatoire sur le Board
            position = (int) (allCells * random.nextDouble());

            // Si la position est valide et pas déjà une mine, placez-y une mine
            if ((position < allCells) && (field[position] != COVERED_MINE_CELL)) {

                // Calcule la colonne de la position courante
                current_col = position % cols;
                // Définir la position actuelle comme une mine
                field[position] = COVERED_MINE_CELL;
                // Incrémente le nombre de mines placées
                i++;

                // Incrémente les cellules voisines de la position courante si ce ne sont pas
                // des mines
                if (current_col > 0) {
                    cell = position - 1 - cols;
                    if (cell >= 0 && field[cell] != COVERED_MINE_CELL)
                        field[cell] += 1;
                    cell = position - 1;
                    if (cell >= 0 && field[cell] != COVERED_MINE_CELL)
                        field[cell] += 1;
                    cell = position + cols - 1;
                    if (cell < allCells && field[cell] != COVERED_MINE_CELL)
                        field[cell] += 1;
                }

                cell = position - cols;
                if (cell >= 0 && field[cell] != COVERED_MINE_CELL)
                    field[cell] += 1;
                cell = position + cols;
                if (cell < allCells && field[cell] != COVERED_MINE_CELL)
                    field[cell] += 1;

                if (current_col < (cols - 1)) {
                    cell = position - cols + 1;
                    if (cell >= 0 && field[cell] != COVERED_MINE_CELL)
                        field[cell] += 1;
                    cell = position + cols + 1;
                    if (cell < allCells && field[cell] != COVERED_MINE_CELL)
                        field[cell] += 1;
                    cell = position + 1;
                    if (cell < allCells && field[cell] != COVERED_MINE_CELL)
                        field[cell] += 1;
                }
            }
        }
    }

    public void findEmptyCells(int j) {

        int current_col = j % cols; // calcule la colonne de la cellule courante
        int cell; // stocke l'index de la cellule adjacente en cours de vérification

        // vérifie les cellules à gauche de la cellule courante
        if (current_col > 0) {
            cell = j - cols - 1; // vérifie la cellule en diagonale vers le haut à gauche
            if (cell >= 0) // si l'index de la cellule n'est pas hors limites
                if (field[cell] > MINE_CELL) { // si la cellule n'a pas déjà été découverte
                    field[cell] -= COVER_FOR_CELL; // découvre la cellule
                    if (field[cell] == EMPTY_CELL) // si la cellule est vide
                        findEmptyCells(cell); // appelle récursivement cette méthode sur la cellule vide
                }

            cell = j - 1;
            if (cell >= 0)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        findEmptyCells(cell);
                }

            cell = j + cols - 1;
            if (cell < allCells)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        findEmptyCells(cell);
                }
        }

        cell = j - cols;
        if (cell >= 0)
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    findEmptyCells(cell);
            }

        cell = j + cols;
        if (cell < allCells)
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    findEmptyCells(cell);
            }

        if (current_col < (cols - 1)) {
            cell = j - cols + 1;
            if (cell >= 0)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        findEmptyCells(cell);
                }

            cell = j + cols + 1;
            if (cell < allCells)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        findEmptyCells(cell);
                }

            cell = j + 1;
            if (cell < allCells)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        findEmptyCells(cell);
                }
        }

    }

    public void paint(Graphics g) {

        int cell = 0;
        int uncover = 0;

        // Parcourir toutes les cellules du champ de mines
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                cell = field[(i * cols) + j];

                // Si on est toujours en jeu et qu'on tombe sur une mine, le jeu se termine
                if (inGame && cell == MINE_CELL)
                    inGame = false;

                // Si le jeu est terminé
                if (!inGame) {
                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == MARKED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    }

                    // Si le jeu est en cours
                } else {
                    if (cell > COVERED_MINE_CELL)
                        cell = DRAW_MARK;
                    else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }

                // Dessiner l'image correspondant à la cellule actuelle
                g.drawImage(img[cell], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
        }

        // Si toutes les cases non minées sont découvertes, le jeu est gagné
        if (uncover == 0 && inGame) {
            inGame = false;
            statusbar.setText("Game won");
        } else if (!inGame)
            statusbar.setText("Game lost");
    }

    class MinesAdapter extends MouseAdapter {
        public void mousePressed(MouseEvent e) {

            // Récupère les coordonnées X et Y du clic de souris.
            int x = e.getX();
            int y = e.getY();

            // Calcule le numéro de colonne et de rangée de la cellule cliquée.
            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;

            boolean rep = false;

            // Si le jeu n'a pas encore commencé, initialise une nouvelle partie et
            // actualise la vue.
            if (!inGame) {
                newGame();
                repaint();
            }

            // Si les coordonnées du clic sont dans la zone de jeu.
            if ((x < cols * CELL_SIZE) && (y < rows * CELL_SIZE)) {

                // Si le clic est effectué avec le bouton droit de la souris.
                if (e.getButton() == MouseEvent.BUTTON3) {

                    // Si la cellule cliquée n'est pas découverte.
                    if (field[(cRow * cols) + cCol] > MINE_CELL) {
                        rep = true;

                        // Si la cellule cliquée est marquée avec un drapeau.
                        if (field[(cRow * cols) + cCol] <= COVERED_MINE_CELL) {
                            if (minesLeft > 0) {
                                // Ajoute un marqueur pour cette cellule.
                                field[(cRow * cols) + cCol] += MARK_FOR_CELL;
                                minesLeft--;
                                statusbar.setText(Integer.toString(minesLeft));
                            } else
                                statusbar.setText("No marks left");
                        } else {
                            // Supprime le marqueur de cette cellule.
                            field[(cRow * cols) + cCol] -= MARK_FOR_CELL;
                            minesLeft++;
                            statusbar.setText(Integer.toString(minesLeft));
                        }
                    }

                } else {

                    // Si la cellule cliquée est découverte.
                    if (field[(cRow * cols) + cCol] > COVERED_MINE_CELL) {
                        return;
                    }
                    // Si la cellule cliquée n'est ni une mine ni marquée avec un drapeau.
                    if ((field[(cRow * cols) + cCol] > MINE_CELL) &&
                            (field[(cRow * cols) + cCol] < MARKED_MINE_CELL)) {

                        // Découvre la cellule.
                        field[(cRow * cols) + cCol] -= COVER_FOR_CELL;
                        rep = true;

                        if (field[(cRow * cols) + cCol] == MINE_CELL)
                            inGame = false;
                        if (field[(cRow * cols) + cCol] == EMPTY_CELL)
                            // Appelle récursivement find_empty_cells pour découvrir toutes les cellules
                            // adjacentes vides.
                            findEmptyCells((cRow * cols) + cCol);
                    }
                }

                if (rep)
                    repaint();

            }
        }
    }
}