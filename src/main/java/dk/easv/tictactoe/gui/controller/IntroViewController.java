package dk.easv.tictactoe.gui.controller;

import dk.easv.tictactoe.gui.TicTacToe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class IntroViewController {
    // open new window for 1v1 game
    @FXML
    private void onBtnPlay1v1Click(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TicTacToe.class.getResource("/views/TicTacView.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        Stage gameStage = new Stage();
        gameStage.setTitle("Tic Tac Toe - 1v1");
        gameStage.setScene(scene);
        gameStage.initModality(Modality.NONE); // allow both windows to exist
        gameStage.setResizable(false);
        gameStage.show();

        // hide intro window
        Stage introStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        introStage.hide();

        // when game window closes, show intro again
        gameStage.setOnHidden(e -> introStage.show());
    }

    public void onBtnQuitGameClick(ActionEvent event) {
        System.exit(0);
    }
}
