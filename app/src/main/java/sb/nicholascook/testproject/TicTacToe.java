package sb.nicholascook.testproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by NicholasCook on 5/15/15.
 */
public class TicTacToe {

    public static final String HUMAN = "X";
    public static final String COMPUTER = "O";
    public static final String TIE = "TIE";
    public static final int BOARD_SIZE = 9;
    private String[] gameBoard;
    private int computerMove;

    /**
     * Constructs a new game of tic-tac-toe.
     * Initialize the board and set all slots to empty.
     */
    public TicTacToe() {
        gameBoard = new String[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            gameBoard[i] = "";
        }
    }

    /**
     * Get the main tic-tac-toe board.
     *
     * @return The main game board
     */
    public String[] getGameBoard() {
        return gameBoard;
    }

    /**
     * Clear the main board so it can be used again.
     */
    public void clearBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            gameBoard[i] = "";
        }
    }

    /**
     * Record the player's move at the given index.
     *
     * @param board  The game board being marked
     * @param player The player making the move
     * @param index  The index of the move
     */
    public void makeMove(String[] board, String player, int index) {
        board[index] = player;
    }

    /**
     * Find the next computer move.
     * If board is empty, choose a random tile.
     * Else, use miniMax to find next move.
     *
     * @param board The game board being marked
     * @return The index of the computer move
     */
    public int getComputerMove(String[] board) {
        if (getEmptySpaces(board).size() == BOARD_SIZE) {
            Random random = new Random();
            return random.nextInt(BOARD_SIZE);
        }
        miniMax(0, COMPUTER, board);
        return computerMove;
    }

    /**
     * Set the value of the next computer move to be made
     *
     * @param computerMove The next computer move
     */
    private void setComputerMove(int computerMove) {
        this.computerMove = computerMove;
    }

    /**
     * Algorithm that uses recursion to go through every possible future
     * move for a given board to find the best move for the computer to
     * make. Keeps a list of all moves and their associated scores. The
     * move with the highest score will be the one the computer uses.
     *
     * @param depth  How many moves away from the original board the current move is
     * @param player The player making the current possible move
     * @param board  One of the possible boards
     * @return
     */
    public int miniMax(int depth, String player, String[] board) {
        int score = getScore(depth, board);
        if (score != 0) { //Human or computer has one
            return score;
        }
        depth++;
        List<Integer> scores = new ArrayList<Integer>();
        List<Integer> moves = new ArrayList<Integer>();

        List<Integer> emptySpaces = getEmptySpaces(board);
        if (emptySpaces.isEmpty()) {
            return 0;
        }
        String opponent = COMPUTER.equals(player) ? HUMAN : COMPUTER;
        for (int i = 0; i < emptySpaces.size(); i++) {
            String[] newGame = copyBoard(board); //Create board for use at next level
            makeMove(newGame, player, emptySpaces.get(i)); //Make next possible move
            scores.add(miniMax(depth, opponent, newGame)); //Repeat process for this board
            moves.add(emptySpaces.get(i));
        }

        int maxIndex = 0, minIndex = 0;
        int maxValue = Integer.MIN_VALUE, minValue = Integer.MAX_VALUE;
        if (COMPUTER.equals(player)) {
            for (int i = 0; i < scores.size(); i++) {
                if (scores.get(i) > maxValue) {
                    maxIndex = i;
                    maxValue = scores.get(i);
                }
            }
            setComputerMove(moves.get(maxIndex)); //Computer will use move with max score
            return scores.get(maxIndex);
        } else {
            for (int i = 0; i < scores.size(); i++) {
                if (scores.get(i) < minValue) {
                    minIndex = i;
                    minValue = scores.get(i);
                }
            }
            return scores.get(minIndex); //Want to minimize opponent's score
        }
    }

    /**
     * Provides a copy of the original game board for use in miniMax.
     *
     * @param board The board to be copied
     * @return A copy of the board
     */
    private String[] copyBoard(String[] board) {
        String[] newBoard = new String[BOARD_SIZE];
        System.arraycopy(board, 0, newBoard, 0, board.length);
        return newBoard;
    }

    /**
     * Get the score for the current game using depth.
     * Use 10 as a base since the max depth is 9 and 0 indicates a tie.
     * Positive numbers used for computer to maximize its score.
     * Negative numbers used for human to minimize their score.
     * Adding depth to the calculation ensures that the computer
     * will win in the smallest number of moves, if there's not a tie.
     *
     * @param depth The current depth of minMax
     * @param board The current score
     * @return The score for this game, negative for human win, positive for computer
     */
    public int getScore(int depth, String[] board) {
        if (isHorizontalWin(HUMAN, board) || isVerticalWin(HUMAN, board) || isDiagonalWin(HUMAN, board)) {
            return depth - 10;
        } else if (isHorizontalWin(COMPUTER, board) || isVerticalWin(COMPUTER, board) || isDiagonalWin(COMPUTER, board)) {
            return 10 - depth;
        }
        return 0;
    }

    /**
     * Find out who has won the current game.
     *
     * @param board The board to be checked
     * @return The winner, or empty if not a tie and no winner yet
     */
    public String getWinner(String[] board) {
        if (isHorizontalWin(HUMAN, board) || isVerticalWin(HUMAN, board) || isDiagonalWin(HUMAN, board)) {
            return HUMAN;
        } else if (isHorizontalWin(COMPUTER, board) || isVerticalWin(COMPUTER, board) || isDiagonalWin(COMPUTER, board)) {
            return COMPUTER;
        } else if (getEmptySpaces(board).isEmpty()) {
            return TIE;
        }
        return "";
    }

    /**
     * Get the list of the indexes of the board's empty tiles
     *
     * @param board The board to check
     * @return A list of the indexes of the board's empty tiles
     */
    private List<Integer> getEmptySpaces(String[] board) {
        List<Integer> emptySpaces = new ArrayList<Integer>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i].isEmpty()) {
                emptySpaces.add(i);
            }
        }
        return emptySpaces;
    }

    /**
     * Check board's horizontals for a winner.
     *
     * @param player Possible winner
     * @param board  The board to check
     * @return True if the player won on a horizontal, otherwise false
     */
    public boolean isHorizontalWin(String player, String[] board) {
        if ((player.equals(board[0]) && player.equals(board[1]) && player.equals(board[2])) ||
                (player.equals(board[3]) && player.equals(board[4]) && player.equals(board[5])) ||
                (player.equals(board[6]) && player.equals(board[7]) && player.equals(board[8]))) {
            return true;
        }
        return false;
    }

    /**
     * Check board's verticals for a winner.
     *
     * @param player Possible winner
     * @param board  The board to check
     * @return True if the player won on a vertical, otherwise false
     */
    public boolean isVerticalWin(String player, String[] board) {
        if ((player.equals(board[0]) && player.equals(board[3]) && player.equals(board[6])) ||
                (player.equals(board[1]) && player.equals(board[4]) && player.equals(board[7])) ||
                (player.equals(board[2]) && player.equals(board[5]) && player.equals(board[8]))) {
            return true;
        }
        return false;
    }

    /**
     * Check board's diagonals for a winner.
     *
     * @param player Possible winner
     * @param board  The board to check
     * @return True if the player won on a diagonals, otherwise false
     */
    public boolean isDiagonalWin(String player, String[] board) {
        if ((player.equals(board[0]) && player.equals(board[4]) && player.equals(board[8])) ||
                (player.equals(board[2]) && player.equals(board[4]) && player.equals(board[6]))) {
            return true;
        }
        return false;
    }

}
