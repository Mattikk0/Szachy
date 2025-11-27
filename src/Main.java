import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javax.script.ScriptException;
import java.io.IOException;

public class Main extends Application {
    public static BooleanProperty restart_game = new SimpleBooleanProperty(false);

    public void restartSetup(){
        Board.game_board = new Pieces[8][8];
        Board.hashList.clear();
        Board.board_hash = 0;
        Board.combinations_table = ChessGame.allCombinations();
        Board.turn = new Turn();
        Board.current = new GameState();
        Board.whole_board = new GameState();
        Board.game_over = false;

        ChessGame.no_progress_moves = 0;
        ChessGame.full_turns = 0;
        ChessGame.winner = null;

        Pawn.epList.clear();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Board.game_board[row][col] = null;
                if (Board.cells[row][col] != null) {
                    Board.cells[row][col].getChildren().clear();
                }
            }
        }

        Board.hashList.clear();
    }

    @Override
    public void start(Stage stage) throws ScriptException, IOException, InterruptedException {
        Board.combinations_table = ChessGame.allCombinations();
        ChessGame game = new ChessGame();
        StartMenu startMenu = new StartMenu();
        startMenu.launchMenu();
        if(startMenu.new_game_selected){
            game.new_game = true;
        }else{
            game.new_game = false;
        }
        Board board = new Board(game);
        Scene scene = new Scene(board, 800, 800);
        stage.setScene(scene);
        stage.show();
        restart_game.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                restart_game.set(false);
                stage.close();
                Platform.runLater(() -> {
                    try {
                        restartSetup();
                        start(stage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }




    public static void main (String[]args){
            launch(args);
    }
}


