# Mancala
CIS 1200 Final Game Project

This project uses the following 4 main concepts to create an interactive, two-player Mancala game. 

Core Concepts: 
1. 2D Arrays: a 2D array is necessary for modeling the state of the board.
A standard Mancala board is 6 by 2 with an additional well on each side.
To represent the board, I used a 2D array of integers (8 by 2) to store the current state of each
space and well. The integer represents the number of stones in that space on the board.

2. Collections: Since order matters when implementing undo functionality, I stored my previous moves in a
Linked List collection, with the most recent move at the tail of the list. It stored entries of type int[][] which
represented the board after every move. To undo the last move, I changed the current board to reflect the previous
board saved. I iterated through this collection to save undo functionality when the game is written into File I/O
and used it to reinstate the game when resumed.

3. File I/O: In order to save the current game state and resume where the game left off after exiting out of
the application, I used a File I/O Buffered File Writer to save the state of the array and the player whose turn it was.
To resume the game, I used a Buffered Reader to re-read the information and set the game board back to how it was
when it was saved.

4. JUnit Testable Component: I tested the state of my 2D array and other variables such as the player turn
functionality. I tested that the array was updated correctly depending on which space was chosen,
that capturing happens correctly, that the end state was correctly recognized, that the leftover stones were properly
distributed after an end state was achieved, that the correct winner was identified, that the undo button resets the
board correctly, that a player's turn was only repeated when they reached the right condition, that invalid clicks on
the board prevented changing the game state, and that there was proper encapsulation.

Overview of Classes: 

The Mancala class models the internal game state by setting up the 2D array and the game's functionality. It also
includes the undo, save, and resume methods as these methods require accessing data about the 2D array.

The Mancala Board class models the Board component on JavaSwing. It overrides a paint component method to draw the spaces
and stones on the board in the correct place. It also implements a mouse listener that triggers the playTurn method
from the Mancala class and updates the status correspondingly. This class also has the undo, save, and resume methods
that call the respective classes from Mancala when triggered.

The RunMancala class sets up the entire game frame including the undo, save, resume, and directions buttons along
with panels for the game board and different game statuses. It implements mouse click listeners so that when a
specific button is pressed, the corresponding method is called from the MancalaBoard class.

Finally, the stone class simply contains the .png image file and includes a drawStone method so that stones can be
drawn easily in the MancalaBoard.repaint() method.

To play the game run the RunGame class. 

External Resources:

Stone Image: https://www.pngegg.com/en/png-bfkcp
