import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Pieces {
    public String color;
    public Label label;
    public int value;
    public static List<Coordinates<Integer, Integer>> moveList = new ArrayList<>();
    public static List<Coordinates<Integer, Integer>> takesList = new ArrayList<>();
    abstract void legalMoves(int row, int col);
    abstract void legalTakes(int row, int col);
    abstract void drawPiece(String color, Label label);
    boolean isOutOfBoard(int row, int col) {
        if(row>7 || row<0 || col>7 || col<0){
            return true;
        }
        return false;
    }
    public GameState convertToGS(int row, int col){
        GameState player = new GameState(Board.game_board, Board.game_board[row][col].color);
        return player;
    }
}
