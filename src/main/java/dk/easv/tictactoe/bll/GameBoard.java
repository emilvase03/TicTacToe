package dk.easv.tictactoe.bll;
/**
 *
 * @author EASV
 */
public class GameBoard implements IGameBoard
{
    private int currentPlayer = 1;
    private int[][] board = new int[3][3];
    private int[][] winningLine = null;
    private boolean gameOver = false;
    private int winner = -1;

    public GameBoard()
    {
        newGame();
    }

    public int getNextPlayer()
    {
        return currentPlayer;
    }

    // cleaned up this function (cleaner validation)
    public boolean play(int col, int row)
    {
        if (gameOver || col < 0 || col > 2 || row < 0 || row > 2 || board[col][row] != 0) {
            return false;
        }
        board[col][row] = currentPlayer;
        if (checkWin()) {
            gameOver = true;
            winner = currentPlayer;
        } else if (isBoardFull()) {
            gameOver = true;
            winner = -1;
            winningLine = null;
        } else {
            currentPlayer = (currentPlayer == 2) ? 1 : 2;
        }
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

    // board = new int[3][3] is better than nested loops
    public void newGame()
    {
        currentPlayer = 1;
        gameOver = false;
        winner = -1;
        board = new int[3][3];
        winningLine = null;
    }

    private boolean checkWin()
    {
        int m = currentPlayer;
        // check rows
        for (int row = 0; row < 3; row++)
        {
            if (board[0][row] == m && board[1][row] == m && board[2][row] == m)
            {
                winningLine = new int[][]{{0, row}, {1, row}, {2, row}};
                return true;
            }
        }
        // check columns
        for (int col = 0; col < 3; col++)
        {
            if (board[col][0] == m && board[col][1] == m && board[col][2] == m)
            {
                winningLine = new int[][]{{col, 0}, {col, 1}, {col, 2}};
                return true;
            }
        }
        // check diagonals
        if (board[0][0] == m && board[1][1] == m && board[2][2] == m)
        {
            winningLine = new int[][]{{0, 0}, {1, 1}, {2, 2}};
            return true;
        }
        if (board[0][2] == m && board[1][1] == m && board[2][0] == m)
        {
            winningLine = new int[][]{{0, 2}, {1, 1}, {2, 0}};
            return true;
        }
        return false;
    }

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

    public int[][] getWinningLine()
    {
        return winningLine;
    }

    /**
     * Returns a copy of the current board state for AI processing
     * @return A deep copy of the board array
     */
    public int[][] getBoardCopy()
    {
        int[][] copy = new int[3][3];
        for (int i = 0; i < 3; i++)
        {
            System.arraycopy(board[i], 0, copy[i], 0, 3);
        }
        return copy;
    }
}