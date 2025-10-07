
package dk.easv.tictactoe.gui.controller;

// Java imports
import java.net.URL;
import java.util.ResourceBundle;
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
    private Button btnNewGame;

    @FXML
    private GridPane gridPane;
    
    private static final String TXT_PLAYER = "Player: ";
    private IGameBoard game;

    /**
     * Event handler for the grid buttons
     *
     * @param event
     */
    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        try
        {
            Integer row = GridPane.getRowIndex((Node) event.getSource());
            Integer col = GridPane.getColumnIndex((Node) event.getSource());
            int r = (row == null) ? 0 : row;
            int c = (col == null) ? 0 : col;
            int player = game.getNextPlayer();
            if (game.play(c, r))
            {
                if (game.isGameOver())
                {
                    int winner = game.getWinner();

                    Button btn = (Button) event.getSource();
                    String xOrO = player == 1 ? "X" : "O";
                    btn.setText(xOrO);
                    displayWinner(winner);
                    highlightWinningLine();
                }
                else
                {
                    Button btn = (Button) event.getSource();
                    String xOrO = player == 1 ? "X" : "O";
                    btn.setText(xOrO);
                    setPlayer();
                }
            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
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
        setPlayer();
    }

    // set the label to show the current player
    private void setPlayer()
    {
        lblPlayer.setText(TXT_PLAYER + game.getNextPlayer());
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
            default:
                message = "Player " + winner + " Wins!";
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
                    btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
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
            btn.setStyle("");
        }
    }

    // close the window
    @FXML
    private void onBtnExitClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
