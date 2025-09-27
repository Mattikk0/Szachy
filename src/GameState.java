import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GameState {
    List<Pair<Pieces, Coordinates>> piecesList = new ArrayList<>();
    int material;


    void getBoard(Pieces[][] board){
        this.piecesList.clear();
        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < board[row].length; col++){
                Pieces piece = board[row][col];
                if(piece != null){
                    Coordinates<Integer, Integer> coordinates = new Coordinates<>(row, col);
                    this.piecesList.add(new Pair<>(piece, coordinates));
                }
            }
        }
    }

    void saveToFile(){
        try (PrintWriter writer = new PrintWriter("SavedGame.txt")) {
            for (var pair : this.piecesList) {
                writer.println(pair.first().getClass().getSimpleName() + " " + pair.second().getX() + " " + pair.second().getY() + " " + pair.first().color);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
