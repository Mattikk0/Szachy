import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public abstract class Pieces {
    public PieceColor color;
    public Label label;
    public int value;
    public Coordinates<Integer, Integer> position;
    public List<Coordinates<Integer, Integer>> moveList = new ArrayList<>();
    public List<Coordinates<Integer, Integer>> takesList = new ArrayList<>();
    public List<Coordinates<Integer, Integer>> checkPath = new ArrayList<>();
    abstract void legalMoves(int row, int col);
    abstract void legalTakes(int row, int col);
    abstract void drawPiece(PieceColor color, Label label);
    abstract boolean isChecking();
    static boolean isOutOfBoard(int row, int col) {
        return row > 7 || row < 0 || col > 7 || col < 0;
    }
    abstract int calculateMoveStrength(Coordinates<Integer, Integer> move);


    private Coordinates<Integer, Integer> lastSimOldPos = null;
    private Coordinates<Integer, Integer> lastSimMove = null;
    private Pieces lastSimTarget = null;
    void simulateMove(Coordinates<Integer, Integer> move){
        if (move == null) {
            return;
        }
        if (this.position == null) {
            return;
        }
        int oldRow = this.position.getX();
        int oldCol = this.position.getY();
        lastSimOldPos = new Coordinates<>(oldRow, oldCol);
        lastSimMove = move;
        lastSimTarget = Board.game_board[move.getX()][move.getY()];

        Board.game_board[move.getX()][move.getY()] = this;
        Board.game_board[oldRow][oldCol] = null;
        this.position = move;
    }

    void undoSimulateMove(Coordinates<Integer, Integer> move, int oldRow, int oldCol){
        if (move == null) {
            return;
        }
        Coordinates<Integer, Integer> simOld = lastSimOldPos;
        Coordinates<Integer, Integer> simMove = lastSimMove;
        Pieces simTarget = lastSimTarget;

        if (simOld == null || simMove == null) {
            simOld = new Coordinates<>(oldRow, oldCol);
            simMove = move;
            simTarget = null;
        }
        Board.game_board[simOld.getX()][simOld.getY()] = this;
        Board.game_board[simMove.getX()][simMove.getY()] = simTarget;
        this.position = new Coordinates<>(simOld.getX(), simOld.getY());
        lastSimOldPos = null;
        lastSimMove = null;
        lastSimTarget = null;
    }

    boolean canBeTaken(){
        for (Pieces[] line : Board.game_board) {
            for (Pieces piece : line) {
                if (piece != null && piece.color != this.color) {
                    piece.takesList.clear();
                    piece.legalTakes(piece.position.getX(), piece.position.getY());
                    for (Coordinates<Integer, Integer> take : piece.takesList) {
                        filterMovesLeadingToCheck(this.takesList);
                        if (Board.game_board[take.getX()][take.getY()] != null && take.getX() == (this.position.getX()) && take.getY() == (this.position.getY())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void setPosition(int newRow, int newCol){
        if (this.position != null) {
            int oldRow = this.position.getX();
            int oldCol = this.position.getY();
            Board.cells[oldRow][oldCol].getChildren().remove(this.label);
            Board.game_board[oldRow][oldCol] = null;
        }
        Board.game_board[newRow][newCol] = this;
        if (Board.cells[newRow][newCol] != null) {
            Board.cells[newRow][newCol].getChildren().remove(this.label);
        }
        this.position = new Coordinates<>(newRow, newCol);
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

    public void filterMovesLeadingToCheck(List<Coordinates<Integer, Integer>> filteredList){
        List<Coordinates<Integer, Integer>> safeMoves = new ArrayList<>();
        if (this.position == null) {
            filteredList.clear();
            return;
        }
        Coordinates<Integer, Integer> originalPosition = this.position;
        if (originalPosition == null) {
            filteredList.clear();
            return;
        }
        for(Coordinates<Integer, Integer> move : filteredList){
            Pieces originalTarget = Board.game_board[move.getX()][move.getY()];
            originalPosition = this.position;
            Board.game_board[move.getX()][move.getY()] = this;
            Board.game_board[originalPosition.getX()][originalPosition.getY()] = null;
            this.position = move;
            boolean isInCheck = false;
            for (Pieces[] line : Board.game_board) {
                for (Pieces piece : line) {
                    if (piece != null && piece.color != this.color) {
                        if(piece.isChecking()){
                            isInCheck = true;
                            break;
                        }
                    }
                }
                if(isInCheck) break;

            }
            if(!isInCheck){
                safeMoves.add(move);
            }
            Board.game_board[originalPosition.getX()][originalPosition.getY()] = this;
            Board.game_board[move.getX()][move.getY()] = originalTarget;
            this.position = originalPosition;
        }
        filteredList.clear();
        filteredList.addAll(safeMoves);
    }

}
