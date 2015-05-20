package sb.nicholascook.testproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TicTacToe mTicTacToe;
    private Button[] mGameButtons;
    private Button mComputerStart;
    private Button mHumanStart;
    private TextView mInfo;
    private boolean isRestart = false;

    /**
     * This method will create a new instance of the game
     * and initialize the game board and buttons.
     *
     * @param savedInstanceState Saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTicTacToe = new TicTacToe();

        mGameButtons = new Button[TicTacToe.BOARD_SIZE];
        mGameButtons[0] = (Button) findViewById(R.id.squareOne);
        mGameButtons[1] = (Button) findViewById(R.id.squareTwo);
        mGameButtons[2] = (Button) findViewById(R.id.squareThree);
        mGameButtons[3] = (Button) findViewById(R.id.squareFour);
        mGameButtons[4] = (Button) findViewById(R.id.squareFive);
        mGameButtons[5] = (Button) findViewById(R.id.squareSix);
        mGameButtons[6] = (Button) findViewById(R.id.squareSeven);
        mGameButtons[7] = (Button) findViewById(R.id.squareEight);
        mGameButtons[8] = (Button) findViewById(R.id.squareNine);

        mComputerStart = (Button) findViewById(R.id.computerStart);
        mHumanStart = (Button) findViewById(R.id.humanStart);
        mInfo = (TextView) findViewById(R.id.info);

        startNewGame();
    }

    /**
     * Start a brand new game when app is launched. Disable game tiles until
     * user decides who will move first.
     */
    private void startNewGame() {
        mTicTacToe.clearBoard();
        for (int i = 0; i < TicTacToe.BOARD_SIZE; i++) {
            mGameButtons[i].setText("");
            disableGameButtons();
            mGameButtons[i].setOnClickListener(new GameTileClickListener(i));
        }
        mComputerStart.setOnClickListener(new StartButtonClickListener(TicTacToe.COMPUTER));
        mComputerStart.setEnabled(true);
        mHumanStart.setOnClickListener(new StartButtonClickListener(TicTacToe.HUMAN));
        mHumanStart.setEnabled(true);
    }

    /**
     * Signal that next game start is a restart.
     * Set start button labels to indicate restart.
     */
    private void setUpRestart() {
        isRestart = true;
        mComputerStart.setText(R.string.computer_restart);
        mHumanStart.setText(R.string.human_restart);
    }

    /**
     * Restart the game.
     * Clear the board and tiles.
     */
    private void restartGame() {
        mTicTacToe.clearBoard();
        for (int i = 0; i < TicTacToe.BOARD_SIZE; i++) {
            mGameButtons[i].setText("");
        }
    }

    /**
     * Mark the selected tile with the player's shape and color.
     * Disable the tile to prevent re-clicks.
     *
     * @param player The player making the move
     * @param index  The index of the move
     */
    private void makeMove(String player, int index) {
        String humanColor = "#006600";
        String computerColor = "#660066";

        mTicTacToe.makeMove(mTicTacToe.getGameBoard(), player, index);
        mGameButtons[index].setEnabled(false);
        mGameButtons[index].setText(player);
        if (TicTacToe.HUMAN.equals(player)) {
            mGameButtons[index].setTextColor(Color.parseColor(humanColor));
        } else {
            mGameButtons[index].setTextColor(Color.parseColor(computerColor));
        }
    }

    /**
     * Disable all game tiles.
     * Prevents user from clicking tiles before start/restart.
     */
    private void disableGameButtons() {
        for (Button button : mGameButtons) {
            button.setEnabled(false);
        }
    }

    /**
     * Enable all game tiles.
     * Allow user to make their move.
     */
    private void enableGameButtons() {
        for (Button button : mGameButtons) {
            button.setEnabled(true);
        }
    }

    /**
     * Find the winning moves so they can be marked.
     *
     * @param player The player who won
     * @param board  The main game board
     */
    private void displayWinner(String player, String[] board) {
        if (player.equals(board[0]) && player.equals(board[4]) && player.equals(board[8])) {
            setWinnerColor(0, 8, 4);
        } else if (player.equals(board[2]) && player.equals(board[4]) && player.equals(board[6])) {
            setWinnerColor(2, 6, 2);
        } else if (player.equals(board[0]) && player.equals(board[1]) && player.equals(board[2])) {
            setWinnerColor(0, 2, 1);
        } else if (player.equals(board[3]) && player.equals(board[4]) && player.equals(board[5])) {
            setWinnerColor(3, 5, 1);
        } else if (player.equals(board[6]) && player.equals(board[7]) && player.equals(board[8])) {
            setWinnerColor(6, 8, 1);
        } else if (player.equals(board[0]) && player.equals(board[3]) && player.equals(board[6])) {
            setWinnerColor(0, 6, 3);
        } else if (player.equals(board[1]) && player.equals(board[4]) && player.equals(board[7])) {
            setWinnerColor(1, 7, 3);
        } else if (player.equals(board[2]) && player.equals(board[5]) && player.equals(board[8])) {
            setWinnerColor(2, 8, 3);
        }
    }

    /**
     * Set the winning moves to a different color to showcase win.
     *
     * @param start     The start tile
     * @param end       The end tile
     * @param increment Increment between winning tiles
     */
    private void setWinnerColor(int start, int end, int increment) {
        String winnerColor = "#FF3300";
        for (int i = start; i <= end; i += increment) {
            mGameButtons[i].setTextColor(Color.parseColor(winnerColor));
        }
    }

    /**
     * Listener for the game tiles.
     * Allows user to make their move during the game.
     */
    private class GameTileClickListener implements View.OnClickListener {
        int index;

        /**
         * Constructs a new tile listener.
         *
         * @param index The index of the tile pressed
         */
        GameTileClickListener(int index) {
            this.index = index;
        }

        /**
         * Take user input then check for a winner.
         *
         * @param v The view
         */
        @Override
        public void onClick(View v) {
            makeMove(TicTacToe.HUMAN, index);
            String winner = mTicTacToe.getWinner(mTicTacToe.getGameBoard());
            if (winner.isEmpty()) {
                makeMove(TicTacToe.COMPUTER, mTicTacToe.getComputerMove(mTicTacToe.getGameBoard()));
                winner = mTicTacToe.getWinner(mTicTacToe.getGameBoard());
            }
            if (TicTacToe.TIE.equals(winner)) { //The only winning move is not to play...
                mInfo.setText(R.string.tie);
                disableGameButtons();
            } else if (TicTacToe.COMPUTER.equals(winner)) { //...unless the human makes a mistake.
                mInfo.setText(R.string.lose);
                disableGameButtons();
                displayWinner(TicTacToe.COMPUTER, mTicTacToe.getGameBoard());
            } else if (TicTacToe.HUMAN.equals(winner)) { //This shouldn't happen
                mInfo.setText(R.string.win);
                disableGameButtons();
                displayWinner(TicTacToe.HUMAN, mTicTacToe.getGameBoard());
            }
        }

    }

    /**
     * Listener for the start/restart buttons.
     * Allows user to start the initial game or restart a game.
     */
    private class StartButtonClickListener implements View.OnClickListener {
        String startPlayer;

        /**
         * Constructs a new start/restart listener.
         *
         * @param startPlayer The player starting/restarting the game
         */
        public StartButtonClickListener(String startPlayer) {
            this.startPlayer = startPlayer;
        }

        /**
         * Start/restart game. Enable game tiles to allow user input.
         * Make first computer move if user wants the computer to start.
         *
         * @param v The view
         */
        @Override
        public void onClick(View v) {
            if (TicTacToe.HUMAN.equals(startPlayer)) {
                if (isRestart) {
                    restartGame();
                } else {
                    setUpRestart();
                }
                enableGameButtons();
                mInfo.setText(R.string.play);
            } else if (TicTacToe.COMPUTER.equals(startPlayer)) {
                if (isRestart) {
                    restartGame();
                } else {
                    setUpRestart();
                }
                enableGameButtons();
                makeMove(TicTacToe.COMPUTER, mTicTacToe.getComputerMove(mTicTacToe.getGameBoard()));
                mInfo.setText(R.string.play);
            }
        }
    }

}
