package org.cis1200.mancala;

import javax.swing.*;
import java.awt.*;

public class RunMancala implements Runnable {
    private String gameDirections = "Capture Mode\n" +
            "Rules:\n" +
            "1. Each player has a store on one side of the board.\n" +
            "2. Players take turns choosing a pile from one of the holes.\n    " +
            "Moving counter-clockwise, stones from the selected pile are deposited " +
            "in each of the following hole " +
            "until you run out of stones.\n" +
            "3. If you drop the last stone into your store - you get a free turn.\n" +
            "4. If you drop the last stone into an empty hole on your side of the board " +
            "- you can capture stones from the hole on the opposite side.\n" +
            "5. The game ends when all six holes on either side of the board are empty. " +
            "\n    If a player has any stones on their side of the board when the game ends " +
            "- he will capture all of those stones.\n" +
            "\nUndo: \n" +
            "Undoes the previous move and allows the same player another turn \n" +
            "\nGoal:\n" +
            "The player with most stones in their store wins.\n";

    public void run() {
        JFrame frame = new JFrame("Mancala");
        frame.setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        JLabel playerStatus = new JLabel("Setting up...");
        bottomPanel.add(playerStatus);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        MancalaBoard board = new MancalaBoard(playerStatus);
        frame.add(board, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> board.undoBoard());
        topPanel.add(undoButton);

        JButton pause = new JButton("Pause");
        pause.addActionListener(e -> board.pause("src/savedGameState.txt"));
        topPanel.add(pause);

        JButton resume = new JButton("Resume");
        resume.addActionListener(e -> board.resume("src/savedGameState.txt"));
        topPanel.add(resume);

        JButton restart = new JButton("Restart");
        restart.addActionListener(e -> {board.reset();});
        topPanel.add(restart);

        JButton directions = new JButton("?");
        directions.addActionListener(e ->
                JOptionPane.showMessageDialog(board, gameDirections));
        topPanel.add(directions);

        frame.add(topPanel, BorderLayout.NORTH);

        JOptionPane.showMessageDialog(frame, gameDirections);


        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //start game
        board.reset();


    }
}
