package dk.easv.tictactoe.gui.controller;
// Java imports
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
// Project imports
import dk.easv.tictactoe.bll.GameBoard;
import dk.easv.tictactoe.bll.IGameBoard;
import dk.easv.tictactoe.bll.MinimaxAI;
import javafx.stage.Stage;
/**
 *
 * @author EASV
 */
public class TicTacViewController implements Initializable
{
    @FXML
    private Label lblPlayer;
    @FXML
    private GridPane gridPane;

    private static final String TXT_PLAYER = "Player: ";
    private IGameBoard game;
    private MinimaxAI ai;
    private boolean aiMode = false;
    private boolean processingAIMove = false;

    /**
     * Event handler for the grid buttons
     *
     * @param event
     */
    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        if (processingAIMove) {
            return;
        }

        try
        {
            Integer row = GridPane.getRowIndex((Node) event.getSource());
            Integer col = GridPane.getColumnIndex((Node) event.getSource());
            int r = (row == null) ? 0 : row;
            int c = (col == null) ? 0 : col;
            int player = game.getNextPlayer();

            if (game.play(c, r))
            {
                Button btn = (Button) event.getSource();
                String xOrO = player == 1 ? "X" : "O";
                btn.setText(xOrO);

                if (game.isGameOver())
                {
                    int winner = game.getWinner();
                    displayWinner(winner);
                    highlightWinningLine();
                }
                else
                {
                    setPlayer();

                    // If AI mode is on and it's AI's turn, make AI move
                    if (aiMode && game.getNextPlayer() == 2)
                    {
                        makeAIMove();
                    }
                }
            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Makes the AI move using minimax algorithm
     */
    private void makeAIMove()
    {
        processingAIMove = true;

        // Show different thinking messages
        String[] thinkingMessages = {
                "AI is analyzing...",
                "AI is thinking...",
                "AI is calculating...",
                "AI is planning..."
        };

        Platform.runLater(() -> {
            lblPlayer.setText(thinkingMessages[(int)(Math.random() * thinkingMessages.length)]);
        });

        // Add delay so user can see their move and the thinking message
        new Thread(() -> {
            try {
                // Random delay between 800-1500ms for more realistic thinking
                Thread.sleep(800 + (long)(Math.random() * 700));

                Platform.runLater(() -> {
                    lblPlayer.setText("AI is making move...");
                });

                Thread.sleep(300);

                Platform.runLater(() -> {
                    int[][] boardCopy = ((GameBoard) game).getBoardCopy();
                    int[] move = ai.findBestMove(boardCopy);

                    if (move[0] != -1 && move[1] != -1)
                    {
                        int player = game.getNextPlayer();
                        if (game.play(move[0], move[1]))
                        {
                            Button btn = getButtonAt(move[0], move[1]);
                            if (btn != null)
                            {
                                String xOrO = player == 1 ? "X" : "O";
                                btn.setText(xOrO);
                            }

                            if (game.isGameOver())
                            {
                                int winner = game.getWinner();
                                displayWinner(winner);
                                highlightWinningLine();
                            }
                            else
                            {
                                setPlayer();
                            }
                        }
                    }
                    processingAIMove = false;
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
                processingAIMove = false;
            }
        }).start();
    }

    /**
     * Event handler for starting a new game
     *
     * @param event
     */
    @FXML
    private void handleNewGame(ActionEvent event)
    {
        game.newGame();
        setPlayer();
        clearBoard();
        processingAIMove = false;

        if (aiMode)
        {
            lblPlayer.setText("You are X - AI is O");
        }
    }

    /**
     * Initializes a new controller
     *
     * @param url
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param rb
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        game = new GameBoard();
        ai = new MinimaxAI(2); // AI is player 2 (O)
        setPlayer();
    }

    /**
     * Sets the AI mode for the game
     * Called by IntroViewController to configure the game mode
     * @param enabled Whether AI mode should be enabled
     */
    public void setAIMode(boolean enabled)
    {
        this.aiMode = enabled;
        game.newGame();
        clearBoard();

        if (aiMode)
        {
            lblPlayer.setText("You are X - AI is O");
        }
        else
        {
            setPlayer();
        }
    }

    // set the label to show the current player
    private void setPlayer()
    {
        if (!aiMode)
        {
            lblPlayer.setText(TXT_PLAYER + game.getNextPlayer());
        }
        else
        {
            String currentPlayerText = game.getNextPlayer() == 1 ? "Your turn (X)" : "AI thinking...";
            lblPlayer.setText(currentPlayerText);
        }
    }

    // display the winner
    private void displayWinner(int winner)
    {
        String message = "";
        switch (winner)
        {
            case -1:
                message = "It's a draw!";
                break;
            case 1:
                message = aiMode ? "You Win!" : "Player 1 Wins!";
                break;
            case 2:
                message = aiMode ? "AI Wins!" : "Player 2 Wins!";
                break;
        }
        lblPlayer.setText(message);
    }

    // highlight the winning line
    private void highlightWinningLine()
    {
        int[][] winningLine = game.getWinningLine();
        if (winningLine != null)
        {
            for (int[] position : winningLine)
            {
                int col = position[0];
                int row = position[1];
                Button btn = getButtonAt(col, row);
                if (btn != null)
                {
                    btn.getStyleClass().add("winning-button");
                }
            }
        }
    }

    // get button at specific grid position
    private Button getButtonAt(int col, int row)
    {
        for (Node node : gridPane.getChildren())
        {
            Integer nodeCol = GridPane.getColumnIndex(node);
            Integer nodeRow = GridPane.getRowIndex(node);
            int c = (nodeCol == null) ? 0 : nodeCol;
            int r = (nodeRow == null) ? 0 : nodeRow;
            if (c == col && r == row && node instanceof Button)
            {
                return (Button) node;
            }
        }
        return null;
    }

    // clear the board
    private void clearBoard()
    {
        for(Node n : gridPane.getChildren())
        {
            Button btn = (Button) n;
            btn.setText("");
            btn.getStyleClass().remove("winning-button");
        }
    }

    // close the window
    @FXML
    private void onBtnExitClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}