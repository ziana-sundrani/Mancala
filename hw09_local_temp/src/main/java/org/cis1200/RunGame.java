package org.cis1200;

import javax.swing.*;

public class RunGame {
    /**
     * Main method run to start and run the game. Initializes the runnable game
     * class and runs it.
     */
    public static void main(String[] args) {
        // Set the game you want to run here
        Runnable game = new org.cis1200.mancala.RunMancala();

        SwingUtilities.invokeLater(game);
    }
}
