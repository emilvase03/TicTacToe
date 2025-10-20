package dk.easv.tictactoe.gui.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class IntroViewController {

    @FXML
    private void onBtnPlay1v1Click(ActionEvent event) {
        loadGameView(event, false);
    }

    @FXML
    private void onBtnPlayVsAIClick(ActionEvent event) {
        loadGameView(event, true);
    }

    @FXML
    private void onBtnQuitGameClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void loadGameView(ActionEvent event, boolean aiMode) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/TicTacView.fxml"));
            Parent root = loader.load();

            // Get the controller and set AI mode
            TicTacViewController controller = loader.getController();
            controller.setAIMode(aiMode);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading game view:");
            e.printStackTrace();
        }
    }
}