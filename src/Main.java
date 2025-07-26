import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicBoolean;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        boolean newGame = true;
        ChessGame game = new ChessGame();
        Board board = new Board(game);
        Scene scene = new Scene(board, 800, 800);
        stage.setScene(scene);
        stage.show();
    }
    public static void main (String[]args){
            launch(args);
    }
}


