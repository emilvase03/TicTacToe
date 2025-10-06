
package dk.easv.tictactoe.bll;

/**
 *
 * @author EASV
 */
public class GameBoard implements IGameBoard
{
    private int currentPlayer = 1;
    private int[][] board = new int[3][3];
    private boolean gameOver = false;
    private int winner = -1;

    public GameBoard()
    {
        newGame();
    }

    // returns the current player
    public int getNextPlayer()
    {
        return currentPlayer;
    }

    // checks if attempted play is valid
    public boolean play(int col, int row)
    {
        // check if game is over
        if (gameOver) {
            return false;
        }

        // check if coords are valid
        if (col < 0 || col > 2 || row < 0 || row > 2) {
            return false;
        }

        // check if cell is empty (0 means empty, 1 means player 1, 2 means player 2)
        if (board[col][row] != 0) {
            return false;
        }

        // place the marker (player 1 uses 1, player 2 uses 2)
        board[col][row] = currentPlayer + 1;

        // check if this move resulted in a win
        if (checkWin()) {
            gameOver = true;
            winner = currentPlayer;
        }
        // check if board is full (draw)
        else if (isBoardFull()) {
            gameOver = true;
            winner = -1;
        }

        // switch to next player
        currentPlayer = (currentPlayer == 0) ? 1 : 0;

        return true;
    }

    public boolean isGameOver()
    {
        return gameOver;
    }

    public int getWinner()
    {
        return winner;
    }
    
    public void newGame()
    {
        currentPlayer = 0;
        gameOver = false;
        winner = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }
    }

    /**
     * helper method to check if the current player has won.
     *
     * @return true if current player has three in a row.
     */
    private boolean checkWin()
    {
        int marker = currentPlayer + 1;

        // Check rows
        for (int row = 0; row < 3; row++)
        {
            if (board[0][row] == marker && board[1][row] == marker && board[2][row] == marker)
            {
                return true;
            }
        }

        // check columns
        for (int col = 0; col < 3; col++)
        {
            if (board[col][0] == marker && board[col][1] == marker && board[col][2] == marker)
            {
                return true;
            }
        }

        // check diagonal (top-left to bottom-right)
        if (board[0][0] == marker && board[1][1] == marker && board[2][2] == marker)
        {
            return true;
        }

        // check diagonal (top-right to bottom-left)
        if (board[0][2] == marker && board[1][1] == marker && board[2][0] == marker)
        {
            return true;
        }

        return false;
    }

    /**
     * Helper method to check if the board is full.
     *
     * @return true if all cells are occupied.
     */
    private boolean isBoardFull()
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                if (board[i][j] == 0)
                {
                    return false;
                }
            }
        }
        return true;
    }
}
