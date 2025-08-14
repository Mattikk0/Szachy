import java.util.ArrayList;
import java.util.List;

public class GameState {
    List<Pair<Pieces, Coordinates>> piecesList = new ArrayList<>();
    int material;
    public GameState(Pieces[][] board, PieceColor color){
        getState(board, color);
        this.material = 0;
    }
    public GameState(){}
    void getState(Pieces[][] board, PieceColor color){
        piecesList.clear();
        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < board[row].length; col++){
                Pieces piece = board[row][col];
                if(piece != null && piece.color.equals(color)){
                    Coordinates<Integer, Integer> coordinates = new Coordinates<>(row, col);
                    piecesList.add(new Pair<>(piece, coordinates));
                }
            }
        }
    }

}
