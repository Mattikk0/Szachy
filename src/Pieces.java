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
    public Coordinates getPosition(){
        for(int row = 0; row<Board.game_board.length; row++){
            for(int col = 0; col<Board.game_board.length; col++){
                if(this.equals(Board.game_board[row][col])){
                    return new Coordinates<>(row, col);
                }
            }
        }
        return null;
    }
    public void setPosition(int newRow, int newCol){
        int oldRow = this.getPosition().getX();
        int oldCol = this.getPosition().getY();
        Board.game_board[newRow][newCol] = this;
        Board.cells[oldRow][oldCol].getChildren().remove(Board.game_board[oldRow][oldCol].label);
        Board.game_board[oldRow][oldCol] = null;
        Board.cells[newRow][newCol].getChildren().add(this.label);
        this.drawPiece(this.color, this.label);
    }
}
