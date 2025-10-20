package dk.easv.tictactoe.bll;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * AI player using the Minimax algorithm for Tic-Tac-Toe
 * With adjustable difficulty level
 */
public class MinimaxAI {

    private final int aiPlayer;
    private final int humanPlayer;
    private final Random random;
    private static final double MISTAKE_PROBABILITY = 0.25; // 25% chance to make a non-optimal move

    public MinimaxAI(int aiPlayer) {
        this.aiPlayer = aiPlayer;
        this.humanPlayer = (aiPlayer == 1) ? 2 : 1;
        this.random = new Random();
    }

    /**
     * Finds the best move for the AI player
     * @param board The current game board
     * @return An array with [col, row] for the best move
     */
    public int[] findBestMove(int[][] board) {
        // Sometimes make a random move instead of optimal (makes AI beatable)
        if (random.nextDouble() < MISTAKE_PROBABILITY) {
            return findRandomMove(board);
        }

        int bestScore = Integer.MIN_VALUE;
        List<int[]> bestMoves = new ArrayList<>();

        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 3; row++) {
                if (board[col][row] == 0) {
                    board[col][row] = aiPlayer;
                    int score = minimax(board, 0, false);
                    board[col][row] = 0;

                    if (score > bestScore) {
                        bestScore = score;
                        bestMoves.clear();
                        bestMoves.add(new int[]{col, row});
                    } else if (score == bestScore) {
                        bestMoves.add(new int[]{col, row});
                    }
                }
            }
        }

        // Randomly pick from equally good moves
        if (!bestMoves.isEmpty()) {
            return bestMoves.get(random.nextInt(bestMoves.size()));
        }

        return new int[]{-1, -1};
    }

    /**
     * Finds a random valid move
     * @param board The current game board
     * @return An array with [col, row] for a random move
     */
    private int[] findRandomMove(int[][] board) {
        List<int[]> availableMoves = new ArrayList<>();

        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 3; row++) {
                if (board[col][row] == 0) {
                    availableMoves.add(new int[]{col, row});
                }
            }
        }

        if (!availableMoves.isEmpty()) {
            return availableMoves.get(random.nextInt(availableMoves.size()));
        }

        return new int[]{-1, -1};
    }

    /**
     * Minimax algorithm implementation
     * @param board The current board state
     * @param depth Current depth in the game tree
     * @param isMaximizing Whether this is a maximizing or minimizing move
     * @return The score for this board state
     */
    private int minimax(int[][] board, int depth, boolean isMaximizing) {
        int result = checkWinner(board);

        // Terminal state
        if (result != 0) {
            if (result == aiPlayer) {
                return 10 - depth;
            } else if (result == humanPlayer) {
                return depth - 10;
            } else {
                return 0; // Draw
            }
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int col = 0; col < 3; col++) {
                for (int row = 0; row < 3; row++) {
                    if (board[col][row] == 0) {
                        board[col][row] = aiPlayer;
                        int score = minimax(board, depth + 1, false);
                        board[col][row] = 0;
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int col = 0; col < 3; col++) {
                for (int row = 0; row < 3; row++) {
                    if (board[col][row] == 0) {
                        board[col][row] = humanPlayer;
                        int score = minimax(board, depth + 1, true);
                        board[col][row] = 0;
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    /**
     * Checks if there's a winner on the board
     * @param board The current board state
     * @return The winner (1 or 2), -1 for draw, 0 for ongoing game
     */
    private int checkWinner(int[][] board) {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0];
            }
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return board[0][i];
            }
        }

        // Check diagonals
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2];
        }

        // Check for draw
        boolean isFull = true;
        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 3; row++) {
                if (board[col][row] == 0) {
                    isFull = false;
                    break;
                }
            }
            if (!isFull) break;
        }

        if (isFull) {
            return -1; // Draw
        }

        return 0; // Game ongoing
    }
}