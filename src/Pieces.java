import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public abstract class Pieces {
    public PieceColor color;
    //public String color;
    public Label label;
    public int value;
    public Coordinates<Integer, Integer> position;
    public static List<Coordinates<Integer, Integer>> moveList = new ArrayList<>();
    public static List<Coordinates<Integer, Integer>> takesList = new ArrayList<>();
    public List<Coordinates<Integer, Integer>> checkPath = new ArrayList<>();
    abstract void legalMoves(int row, int col);
    abstract void legalTakes(int row, int col);
    abstract void drawPiece(PieceColor color, Label label);
    abstract List<Coordinates<Integer, Integer>> getCheckPath();
    abstract boolean isChecking();
    boolean isOutOfBoard(int row, int col) {
        return row > 7 || row < 0 || col > 7 || col < 0;
    }
    public void setPosition(int newRow, int newCol){
        int oldRow = this.position.getX();
        int oldCol = this.position.getY();
        Board.game_board[newRow][newCol] = this;
        Board.cells[oldRow][oldCol].getChildren().remove(Board.game_board[oldRow][oldCol].label);
        Board.game_board[oldRow][oldCol] = null;
        Board.cells[newRow][newCol].getChildren().add(this.label);
        this.drawPiece(this.color, this.label);
    }
    public static Coordinates findFigure(Class<?> type, PieceColor color){
        for (Pieces[] line : Board.game_board) {
            for (Pieces piece : line) {
                if (piece != null) {
                    if(type.isInstance(piece) && color == piece.color){
                        return piece.position;
                    }
                }
            }
        }
        return null;
    }
}
