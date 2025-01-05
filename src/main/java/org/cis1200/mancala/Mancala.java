package org.cis1200.mancala;

import java.io.*;
import java.util.LinkedList;

public class Mancala {
    private int [][] board;
    private LinkedList<int[][]> previousBoards;
    private LinkedList<Boolean> turnRecords;
    private boolean playerOne;
    private int landingRow;
    private int landingCol;
    private String playerStatus;
    private String whilePlaying;

    //helper method to get default board
    private int[][] getStartingBoard() {
        int[][] startingBoard = new int [8][2];
        //start with 4 stones in each space
        for (int i = 1; i < 7; i++) {
            startingBoard[i][0] = 4;
            startingBoard[i][1] = 4;
        }
        return startingBoard;
    }

    //constructor to set up board to default
    public Mancala() {
        board = getStartingBoard();
        playerOne = true;
        previousBoards = new LinkedList<>();
        turnRecords = new LinkedList<>();
        playerStatus = "Player 1 Starts!";
        whilePlaying = "What do you want to do?";
    }

    //method to change players
    public void changePlayer() {
        playerOne = !playerOne;
        if (playerOne) {
            playerStatus = "Player 1 turn";
        } else {
            playerStatus = "Player 2 turn";
        }
    }

    /**
     * method returns game state as an array
     * maintains encapsulation
     **/
    public int [][] getGameState() {
        int [][] copy = new int[8][2];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 2; j++) {
                copy[i][j] = getStoneNum(i,j);
            }
        }
        return copy;
    }

    public int getStoneNum(int r, int c) {
        return board[r][c];
    }

    //method to distribute stones when a space is clicked on
    public void distribute(int row, int col) {
        int stones = board[row][col];
        int r = row;
        int c = col;
        while (stones > 0) {
            board[row][col] -= 1;
            if (c == 0) {
                r ++;
                if (r < 8) {
                    board[r][c] += 1;
                } else {
                    r = 6;
                    c = 1;
                    board[r][c] += 1;
                }
            } else {
                r --;
                if (r >= 0) {
                    board[r][c] += 1;
                } else {
                    r = 1;
                    c = 0;
                    board[r][c] += 1;
                }
            }
            stones --;
        }
        landingRow = r;
        landingCol = c;
    }

    //method to capture stones if necessary
    public void capture() {
        if (board[landingRow][landingCol] == 1) {
            if (playerOne && landingCol == 0) {
                int capturedStones = board[landingRow][1];
                if (capturedStones > 0) {
                    whilePlaying = "Captured!";
                    board[landingRow][landingCol] = 0;
                    board[landingRow][1] = 0;
                    board[7][0] += (capturedStones + 1);
                }
            } else if (!playerOne && landingCol == 1) {
                int capturedStones = board[landingRow][0];
                if (capturedStones > 0) {
                    whilePlaying = "Captured!";
                    board[landingRow][landingCol] = 0;
                    board[landingRow][0] = 0;
                    board[0][1] += (capturedStones + 1);
                }
            } else {
                whilePlaying = "Nice Move!";
            }
        } else {
            whilePlaying = "Nice Move!";
        }
    }

    /**
     * method to check if the board has reached its end state
     * a board is in end state if all 6 holes on either side of the board are empty
     **/
    public boolean isEndState(int [][] b) {
        boolean endState = true;
        for (int i = 1; i < 7; i++) {
            if (b[i][0] != 0) {
                endState = false;
            }
        }
        if (!endState) {
            endState = true;
            for (int j = 1; j < 7; j++) {
                if (b[j][1] != 0) {
                    endState = false;
                }
            }
        }
        return endState;
    }

    /**
     * after achieving end state, remaining stones are put into the store
     * and the player with the most stones is declared the winner
     */
    public int declareWinner(int [][] b) {
        //put all remaining stones on Player 1 side in store
        int lastStonesOne = 0;
        for (int r = 1; r < 7; r++) {
            lastStonesOne += b[r][0];
            b[r][0] = 0;
        }
        b[7][0] += lastStonesOne;

        //put all remaining stones on Player 2 side in store
        int lastStonesTwo = 0;
        for (int r = 1; r < 7; r++) {
            lastStonesTwo += b[r][1];
            b[r][1] = 0;
        }
        b[0][1] += lastStonesTwo;

        whilePlaying = "Captured!";
        previousBoards.add(b);
        if (b[7][0] > b[0][1]) {
            playerStatus = "Player 1 wins!";
            return 1;
        } else if (b[7][0] < b[0][1]) {
            playerStatus = "Player 2 wins!";
            return 2;
        } else {
            playerStatus = "It's a tie!";
            return 0;
        }
    }

    //method to determine if the player gets another turn or if it's the next player's turn
    public boolean nextTurn() {
        if (playerOne && landingRow == 7 && landingCol == 0) {
            playerStatus = "Player 1 gets another turn!";
            whilePlaying = "Nice Move!";
            return true;
        } else if (!playerOne && landingRow == 0 && landingCol == 1) {
            playerStatus = "Player 2 gets another turn!";
            whilePlaying = "Nice Move!";
            return true;
        } else {
            changePlayer();
            return false;
        }
    }

    // method to check if a click is valid
    public boolean invalidClick(int r, int c) {
        if (playerOne && c == 1) {
            return true;
        } else if (!playerOne && c == 0) {
            return true;
        } else if (r == 0 || r == 7) {
            return true;
        } else {
            return board[r][c] == 0;
        }
    }

    //combination of methods to play a complete turn and check if someone gets another
    public void playTurn(int r, int c) {
        if (invalidClick(r, c)) {
            whilePlaying = "Invalid click!";
        } else {
            distribute(r, c);
            capture();
            if (isEndState(board)) {
                turnRecords.add(playerOne);
                declareWinner(board);
            } else {
                previousBoards.add(getGameState());
                turnRecords.add(playerOne);
                nextTurn();
            }
        }
    }

    //method to undo previous move
    public void undo() {
        if (previousBoards.isEmpty()) {
            whilePlaying = "No previous moves!";
        } else if (isEndState(board)) {
            whilePlaying = "Game is over! Please restart!";
        } else if (previousBoards.size() == 1) {
            previousBoards.removeLast();
            board = getStartingBoard();
            playerOne = true;
            playerStatus = "Player 1 redo your turn";
            whilePlaying = "What do you want to do?";
        } else {
            previousBoards.removeLast();
            int[][] copy = new int[8][2];
            int [][] prev = previousBoards.getLast();
            for (int i = 0; i < 8; i ++) {
                System.arraycopy(prev[i], 0, copy[i], 0, 2);
            }
            board = copy;
            whilePlaying = "What do you want to do?";
            playerOne = turnRecords.removeLast();
            if (playerOne) {
                playerStatus = "Player 1 redo your turn!";
            } else {
                playerStatus = "Player 2 redo your turn!";
            }
        }
    }

    //helper method to write 2D arrays to a file
    private void boardToFile(BufferedWriter w, int [][] b, String filename) {
        for (int[] ints : b) {
            try {
                w.write(ints[0] + " " + ints[1]);
                w.newLine();
            } catch (IOException e) {
                System.out.println("Error Writing to File");
            }
        }
    }

    public void saveBoard(String filename) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename, true));
            writer.write(previousBoards.size() + "");
            writer.newLine();
        } catch (IOException e) {
            whilePlaying = "Warning: Board not saved. Error writing to file";
        }

        //write all boards in the PreviousBoards Linked List
        for (int[][] x: previousBoards) {
            boardToFile(writer, x, filename);
        }

        //clear the Linked List because it will be reconstructed when resumed
        previousBoards.clear();

        try {
            if (playerOne) {
                writer.write("Player 1 resumes");
            } else {
                writer.write("Player 2 resumes");
            }
            writer.close();
            whilePlaying = "Progress Saved!";
        } catch (IOException e) {
            whilePlaying = "Warning: Board not saved. Error writing to file";
        }
    }

    //helper method to return a 2D array from file
    private int[][] fileToBoard(BufferedReader r, String filename) {
        int[][] currentBoard = new int[8][2];
        for (int i = 0; i < 8; i++) {
            try {
                String[] row = r.readLine().split(" ");
                currentBoard[i][0] = Integer.parseInt(row[0]);
                currentBoard[i][1] = Integer.parseInt(row[1]);
            } catch (IOException e) {
                whilePlaying =  "Warning: Progress not resumed. Empty File";
            }
        }
        return currentBoard;
    }
    public void reUpload(String filename) {
        BufferedReader reader = null;
        FileWriter writer = null;
        int numArrays;
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            whilePlaying = "Warning: Progress not resumed. Error reading file";
        }
        try {
            numArrays = Integer.parseInt(reader.readLine());
            for (int i = 0; i < numArrays; i++) {
                int[][] recreated = fileToBoard(reader, filename);
                previousBoards.add(recreated);
            }
            try {
                playerStatus = reader.readLine();
                if (getPlayerStatus().equals("Player 2 resumes")) {
                    playerOne = false;
                }
            } catch (NullPointerException e) {
                whilePlaying = "Warning: Progress not resumed. Empty File";
            }
        } catch (IOException e) {
            whilePlaying = "Warning: Progress not resumed. Error reading file";
        }

        //clear file again
        try {
            writer = new FileWriter(filename);
            whilePlaying = "Progress Resumed!";
        } catch (IOException e) {
            whilePlaying = "Warning: File not cleared. Please clear file before saving again";
        }

        //reconstruct and set current board
        if (previousBoards.isEmpty()) {
            board = getStartingBoard();
        } else {
            int[][] currentBoard = new int[8][2];
            int[][] lastInList = previousBoards.getLast();
            for (int i = 0; i < 8; i++) {
                System.arraycopy(lastInList[i], 0, currentBoard[i], 0, 2);
            }
            board = currentBoard;
        }
    }

    public String getPlayerStatus() {
        return playerStatus;
    }

    public String getWhilePlayingStatus() {
        return whilePlaying;
    }

    //for debugging purposes
    public void printGameState() {
        System.out.println("Next Turn");
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                System.out.print(board[r][c]);
                if (c == 0) {
                    System.out.print(" | ");
                }
            }
            if (r < 8) {
                System.out.println("\n ----------");
            }
        }
    }
}
