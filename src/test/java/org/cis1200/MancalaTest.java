package org.cis1200;

import org.cis1200.mancala.Mancala;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class MancalaTest {
    @Test
    public void testDistributeInSameCol() {
        Mancala mancala = new Mancala();

        mancala.distribute(1, 0);
        int [][] testBoard = mancala.getGameState();
        assertEquals(0, testBoard[1][0]);
        for (int i = 2; i < 6; i++) {
            assertEquals(5, testBoard[i][0]);
        }

        mancala.distribute(5, 1);
        testBoard = mancala.getGameState();
        assertEquals(0, testBoard[5][1]);
        for (int i = 1; i < 5; i++) {
            assertEquals(5, testBoard[i][1]);
        }
    }

    @Test
    public void testDistributeSpillsToNextCol() {
        Mancala mancala = new Mancala();
        mancala.distribute(6, 0);
        int [][] testBoard = mancala.getGameState();
        assertEquals(0, testBoard[6][0]);
        assertEquals(1, testBoard[7][0]);
        assertEquals(0, testBoard[7][1]);// unused array space remains empty
        assertEquals(5, testBoard[6][1]);
        assertEquals(5, testBoard[5][1]);
        assertEquals(5, testBoard[4][1]);

        mancala.distribute(4, 1);
        testBoard = mancala.getGameState();
        assertEquals(0, testBoard[4][1]);
        assertEquals(5, testBoard[3][1]);
        assertEquals(5, testBoard[2][1]);
        assertEquals(5, testBoard[1][1]);
        assertEquals(1, testBoard[0][1]);
        assertEquals(0, testBoard[0][0]); // unused array space remains empty
        assertEquals(5, testBoard[1][0]);
    }

    @Test
    public void testDistributeNoStonesInSpace() {
        Mancala mancala = new Mancala();
        int [][] before = mancala.getGameState();
        mancala.distribute(7, 0);
        int [][] after = mancala.getGameState();
        for (int i = 0; i < before.length; i++) {
            assertArrayEquals(before[i], after[i]);
        }
        mancala.distribute(0,1);
        after = mancala.getGameState();
        for (int i = 0; i < before.length; i++) {
            assertArrayEquals(before[i], after[i]);
        }
    }

    @Test
    public void testCapturePlayerOne() {
        Mancala mancala = new Mancala();
        mancala.distribute(6, 0);
        mancala.distribute(2,0);
        assertEquals(0, mancala.getStoneNum(2, 0));
        assertEquals(1, mancala.getStoneNum(6, 0)); //condition to capture
        //number of stones to be captured from other side
        assertEquals(5, mancala.getStoneNum(6, 1));
        assertEquals(1, mancala.getStoneNum(7, 0)); //number of stones already in store
        mancala.capture();
        assertEquals(0, mancala.getStoneNum(6, 0)); //no stones left in both columns of captured row
        assertEquals(0, mancala.getStoneNum(6, 1));
        assertEquals(7, mancala.getStoneNum(7, 0)); //right number of stones in store
    }

    @Test
    public void testCapturePlayerTwo() {
        Mancala mancala = new Mancala();
        mancala.changePlayer();
        mancala.distribute(1,1);
        mancala.distribute(5, 1);
        assertEquals(0, mancala.getStoneNum(5,1));
        assertEquals(1, mancala.getStoneNum(1,1)); //condition to capture
        assertEquals(5, mancala.getStoneNum(1,0)); //number of stones to be captured from other side
        assertEquals(1, mancala.getStoneNum(0,1)); //number of stones already in store
        mancala.capture();
        assertEquals(0, mancala.getStoneNum(1,1)); //no stones left in both columns of captured row
        assertEquals(0, mancala.getStoneNum(1,0));
        assertEquals(7, mancala.getStoneNum(0,1)); //right number of stones in store
    }

    @Test
    public void testCaptureConditionsNotMet() {
        Mancala mancala = new Mancala();
        int [][] before;
        int [][] after;
        //doesn't land in previously empty space
        mancala.distribute(2, 0);
        before = mancala.getGameState();
        assertNotEquals(0, before[6][0]);
        mancala.capture();
        after = mancala.getGameState();
        for (int i = 0; i < before.length; i++) {
            assertArrayEquals(before[i], after[i]);
        }

        //doesn't land on right side of board or empty space
        mancala.changePlayer();
        mancala.distribute(3, 1);
        before = mancala.getGameState();
        mancala.capture();
        after = mancala.getGameState();
        for (int i = 0; i < before.length; i++) {
            assertArrayEquals(before[i], after[i]);
        }

        //doesn't land on previously empty space right side of board
        mancala.changePlayer();
        mancala.distribute(6, 0);
        before = mancala.getGameState();
        assertEquals(1, before[3][1]); // previously empty space
        mancala.capture();
        after = mancala.getGameState();
        for (int i = 0; i < before.length; i++) {
            assertArrayEquals(before[i], after[i]);
        }
    }

    @Test
    public void testCaptureZero() {
        Mancala mancala = new Mancala();
        mancala.distribute(6,0);
        mancala.changePlayer();
        mancala.distribute(6,1);
        mancala.changePlayer();
        mancala.distribute(2,0);
        assertEquals(0,mancala.getStoneNum(6,1)); //0 stones on opposite side
        assertEquals(1, mancala.getStoneNum(6,0)); //space was previously empty
        assertEquals(1, mancala.getStoneNum(7,0)); // only one stone in store
        mancala.capture();
        assertEquals(0, mancala.getStoneNum(6,1));
        assertEquals(1, mancala.getStoneNum(7,0)); //store should not change

    }

    @Test
    public void testPlayer1EndState() {
        Mancala mancala = new Mancala();
        int [][] board = new int [8][2];
        board[7][0] = 5; // stones in player1 store
        //stones in all player2 spaces
        for (int i = 0; i <= 6; i++) {
            board[6 - i][1] = i;
        }
        assertTrue(mancala.isEndState(board));
    }

    @Test
    public void testPlayer2EndState() {
        Mancala mancala = new Mancala();
        int [][] board = new int [8][2];
        board[0][1] = 5; //stones in player2 store
        //stones in all player1 spaces
        for (int i = 0; i < 8; i++) {
            board[i][0] = i;
        }
        assertTrue(mancala.isEndState(board));
        board[0][1] = 0; //no stones in player2 store
        assertTrue(mancala.isEndState(board));
    }

    @Test
    public void testNotEndState() {
        Mancala mancala = new Mancala();
        int [][] board = new int [8][2];
        board[3][0] = 4;
        board[6][1] = 5;
        assertFalse(mancala.isEndState(board));
    }

    @Test
    public void declareWinnerPlayer2StonesNotInStore() {
        Mancala mancala = new Mancala();
        int [][] board = new int [8][2];
        board[7][0] = 5;
        board[3][1] = 3;
        board[4][1] = 3;
        assertEquals(2, mancala.declareWinner(board));
    }

    @Test
    public void declareWinnerPlayer1StonesNotInStore() {
        Mancala mancala = new Mancala();
        int [][] board = new int [8][2];
        board[0][1] = 5;
        board[6][0] = 4;
        board[4][0] = 1;
        assertEquals(0, mancala.declareWinner(board));
    }

    @Test
    public void declareWinnerAllStonesInStore() {
        Mancala mancala = new Mancala();
        int [][] board = new int [8][2];
        board[0][1] = 5;
        board[7][0] = 10;
        assertEquals(1, mancala.declareWinner(board));
    }


    @Test
    public void undoSameSide() {
        Mancala mancala = new Mancala();
        int [][] before = mancala.getGameState();
        mancala.playTurn(2,0);
        mancala.undo();
        assertArrayEquals(before, mancala.getGameState());

        mancala.playTurn(2,0);
        before = mancala.getGameState();
        mancala.playTurn(5, 1);
        mancala.undo();
        assertArrayEquals(before, mancala.getGameState());
    }

    @Test
    public void undoNoMovesLeft() {
        Mancala mancala = new Mancala();
        int [][] before = mancala.getGameState();
        mancala.undo();
        assertArrayEquals(before, mancala.getGameState());
    }

    @Test
    public void undoStonesSpilledToNextColumn() {
        Mancala mancala = new Mancala();
        int [][] before = mancala.getGameState();
        mancala.playTurn(5,0);
        mancala.undo();
        assertArrayEquals(before, mancala.getGameState());

        mancala.changePlayer();
        mancala.playTurn(2,1);
        mancala.undo();
        assertArrayEquals(before, mancala.getGameState());
    }

    @Test
    public void undoAfterMultipleTurns() {
        Mancala mancala = new Mancala();
        mancala.playTurn(1,0); //player 1
        mancala.playTurn(3,1); //player 2
        int [][] moveTwo = mancala.getGameState();
        mancala.playTurn(3,0); //player 1
        mancala.undo(); //Player 1 undoes
        assertArrayEquals(moveTwo, mancala.getGameState());
        assertFalse(mancala.invalidClick(3,0));

        mancala.playTurn(4,0); //player 1
        int[][] moveThree = mancala.getGameState();
        mancala.playTurn(5,1); //player 2
        mancala.undo(); //Player 2 undoes
        assertArrayEquals(moveThree, mancala.getGameState());
        assertFalse(mancala.invalidClick(5,1));

        //undo again
        mancala.undo();
        assertArrayEquals(moveTwo, mancala.getGameState()); //something wrong with refs
    }

    @Test
    public void undoGetAnotherTurnAfter() {
        Mancala mancala = new Mancala();
        int [][] before = mancala.getGameState();
        mancala.playTurn(2,0); //Player 1 would not get another turn
        mancala.undo(); //Player 1 gets another shot at their turn
        assertArrayEquals(before, mancala.getGameState());
        //Clicking on Player 1's// side is not invalid
        assertFalse(mancala.invalidClick(1,0));

        mancala.playTurn(1,0);
        before = mancala.getGameState();
        mancala.playTurn(2,1); //Player 2 would not get another turn
        mancala.undo(); //Player 2 gets another shot at their turn
        assertArrayEquals(before, mancala.getGameState());
        //Clicking on Player 2's// side is not invalid
        assertFalse(mancala.invalidClick(1,1));
    }

    @Test
    public void undoSameMoveTwice() {
        Mancala mancala = new Mancala();
        int [][] before = mancala.getGameState();
        mancala.playTurn(4,0);
        mancala.undo(); //undo turn
        assertArrayEquals(before, mancala.getGameState());
        mancala.playTurn(4,0); //play same turn after Undoing
        mancala.undo(); //undo same turn again
        assertArrayEquals(before, mancala.getGameState());

        mancala.playTurn(4,0);

        //player 2
        before = mancala.getGameState();
        mancala.playTurn(5,1);
        mancala.undo(); //undo same turn again
        assertArrayEquals(before, mancala.getGameState());
        mancala.playTurn(5,1); //play same turn again
        mancala.undo(); //undo same turn again
        assertArrayEquals(before, mancala.getGameState());
    }

    @Test
    public void invalidClickWrongSideOfBoard() {
        Mancala mancala = new Mancala();
        //player 1 clicks on wrong side of board
        for (int i = 0; i < 8; i++) {
            assertTrue(mancala.invalidClick(i, 1));
        }
        //player 2 clicks on wrong side of board
        mancala.changePlayer();
        for (int i = 0; i < 8; i++) {
            assertTrue(mancala.invalidClick(i, 0));
        }
    }

    @Test
    public void invalidClickNoStonesInSpace() {
        Mancala mancala = new Mancala();
        //store spaces start empty
        assertTrue(mancala.invalidClick(0, 0));
        assertTrue(mancala.invalidClick(7,1));
        assertTrue(mancala.invalidClick(7, 0));
        assertTrue(mancala.invalidClick(0, 1));
        //space empty immediately after clicking it
        mancala.playTurn(2,0);
        assertTrue(mancala.invalidClick(2, 0));
        mancala.playTurn(4,1);
        assertTrue(mancala.invalidClick(4, 1));
        //space still empty so click still invalid
        assertTrue(mancala.invalidClick(2, 0));
    }

    @Test
    public void getGameStateAccuracy() {
        Mancala mancala = new Mancala();
        //before playing turns
        int [][] defaultBoard = mancala.getGameState();
        for (int i = 0; i < defaultBoard.length; i++) {
            for (int j = 0; j < defaultBoard[i].length; j++) {
                assertEquals(defaultBoard[i][j], mancala.getStoneNum(i, j));
            }
        }
        //after playing turns
        mancala.playTurn(2,0);
        mancala.playTurn(4,0);
        int[][] afterTurns = mancala.getGameState();
        for (int i = 0; i < afterTurns.length; i++) {
            for (int j = 0; j < afterTurns[i].length; j++) {
                assertEquals(afterTurns[i][j], mancala.getStoneNum(i, j));
            }
        }
    }

    @Test
    public void getGameStateEncapsulation() {
        Mancala mancala = new Mancala();
        int [][] defaultBoard = mancala.getGameState();
        defaultBoard[0][0] = 10;
        assertNotEquals(defaultBoard[0][0], mancala.getStoneNum(0, 0));
        assertEquals(0, mancala.getStoneNum(0, 0));
    }

    @Test
    public void getStoneNumEncapsulation() {
        Mancala mancala = new Mancala();
        int x = mancala.getStoneNum(0, 0);
        x = 10;
        assertNotEquals(x, mancala.getStoneNum(0, 0));
        assertEquals(0, mancala.getStoneNum(0, 0));
    }

    @Test
    public void nextTurnDontLandInStore() {
        Mancala mancala = new Mancala();
        mancala.distribute(2,0);
        assertFalse(mancala.nextTurn());
        mancala.changePlayer();
        mancala.playTurn(4,1);
        assertFalse(mancala.nextTurn());
    }

    @Test
    public void nextTurnCaptured() {
        Mancala mancala = new Mancala();
        mancala.distribute(6,0);
        mancala.distribute(2,0);
        mancala.capture(); //player 1 captured stones
        //player 1 doesn't get another turn, nextTurn method changes player
        assertFalse(mancala.nextTurn());

        mancala.distribute(1,1);
        mancala.distribute(6,1);
        mancala.capture(); //player 2 captured stones
        assertFalse(mancala.nextTurn()); //player 2 doesn't get another turn
    }

    @Test
    public void nextTurnTrue() {
        Mancala mancala = new Mancala();
        mancala.distribute(3,0); //last stone landed in store
        assertTrue(mancala.nextTurn());
        mancala.distribute(4,0); //on the next turn, stones didn't land in store
        //player 1 doesn't get another turn, nextTurn method changes player
        assertFalse(mancala.nextTurn());

        mancala.distribute(4, 1); //last stone landed in store
        assertTrue(mancala.nextTurn());
        mancala.distribute(5,1);
        assertTrue(mancala.nextTurn()); //last stone landed in store again
    }

    @Test
    public void playTurn() {
        Mancala mancala = new Mancala();
        mancala.playTurn(1,0);
        //board is correct
        int [][] testBoard = mancala.getGameState();
        assertEquals(0, testBoard[1][0]);
        for (int i = 2; i < 6; i++) {
            assertEquals(5, testBoard[i][0]);
        }
        //switches player
        assertEquals(mancala.getPlayerStatus(), "Player 2 turn");
    }
}
