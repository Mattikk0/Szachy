import javafx.scene.control.Label;

import java.util.List;
import java.util.Objects;

public class King extends Pieces{
    public boolean moved;
    public boolean isChecked;
    public King(PieceColor color, Label label) {
        this.drawPiece(color, label);
        this.color = color;
        this.label = label;
        this.moved = false;
        this.isChecked = false;
    }
    private void moveUpRight(int row, int col){
        if (!isOutOfBoard(row - 1, col + 1) && Board.game_board[row - 1][col + 1] == null) {
            moveList.add(new Coordinates<>(row - 1, col + 1));
        }
    }
    private void moveDownRight(int row, int col){
        if (!isOutOfBoard(row + 1, col + 1) && Board.game_board[row + 1][col + 1] == null) {
            moveList.add(new Coordinates<>(row + 1, col + 1));
        }
    }
    private void moveUpLeft(int row, int col){
        if(!isOutOfBoard(row - 1, col - 1) && Board.game_board[row - 1][col - 1] == null) {
            moveList.add(new Coordinates<>(row - 1, col - 1));
        }
    }
    private void moveDownLeft(int row, int col){
        if (!isOutOfBoard(row + 1, col - 1) && Board.game_board[row + 1][col - 1] == null) {
            moveList.add(new Coordinates<>(row + 1, col - 1));
        }
    }
    private void takeDownLeft(int row, int col){
        if (!isOutOfBoard(row + 1, col - 1)) {
            if (Board.game_board[row + 1][col - 1] != null && !(Board.game_board[row + 1][col - 1].color.equals(this.color)) && !(Board.game_board[row + 1][col - 1] instanceof King)) {
                takesList.add(new Coordinates<>(row + 1, col - 1));
            }
        }
    }
    private void takeDownRight(int row, int col){
        if (!isOutOfBoard(row + 1, col + 1)) {
            if (Board.game_board[row + 1][col + 1] != null && !(Board.game_board[row + 1][col + 1].color.equals(this.color)) && !(Board.game_board[row + 1][col + 1] instanceof King)) {
                takesList.add(new Coordinates<>(row + 1, col + 1));
            }
        }
    }
    private void takeUpRight(int row, int col){
        if (!isOutOfBoard(row - 1, col + 1)) {
            if (Board.game_board[row - 1][col + 1] != null && !(Board.game_board[row - 1][col + 1].color.equals(this.color)) && !(Board.game_board[row - 1][col + 1] instanceof King)) {
                takesList.add(new Coordinates<>(row - 1, col + 1));
            }
        }
    }
    private void takeUpLeft(int row, int col){
        if (!isOutOfBoard(row - 1, col - 1)) {
            if (Board.game_board[row - 1][col - 1] != null && !(Board.game_board[row - 1][col - 1].color.equals(this.color)) && !(Board.game_board[row - 1][col - 1] instanceof King)) {
                takesList.add(new Coordinates<>(row - 1, col - 1));
            }
        }
    }
    private void moveUp(int row, int col){
        if (!isOutOfBoard(row - 1, col)) {
            if (Board.game_board[row - 1][col] == null) {
                moveList.add(new Coordinates<>(row - 1, col));
            }
        }
    }
    private void moveRight(int row, int col){
        if (!isOutOfBoard(row, col + 1)) {
            if (Board.game_board[row][col + 1] == null) {
                moveList.add(new Coordinates<>(row, col + 1));
            }
        }
    }
    private void moveLeft(int row, int col){
        if (!isOutOfBoard(row, col - 1)) {
            if (Board.game_board[row][col - 1] == null) {
                moveList.add(new Coordinates<>(row, col - 1));
            }
        }
    }
    private void moveDown(int row, int col){
        if (!isOutOfBoard(row + 1, col)) {
            if (Board.game_board[row + 1][col] == null) {
                moveList.add(new Coordinates<>(row + 1, col));
            }
        }
    }
    private void takeUp(int row, int col) {
        if (!isOutOfBoard(row - 1, col)) {
            if (Board.game_board[row - 1][col] != null && !(Board.game_board[row - 1][col].color.equals(this.color))  && !(Board.game_board[row - 1][col] instanceof King)) {
                takesList.add(new Coordinates<>(row - 1, col));
            }
        }
    }
    private void takeDown(int row, int col) {
        if (!isOutOfBoard(row + 1, col)) {
            if (Board.game_board[row + 1][col] != null && !(Board.game_board[row + 1][col].color.equals(this.color)) && !(Board.game_board[row + 1][col] instanceof King)) {
                takesList.add(new Coordinates<>(row + 1, col));
            }
        }
    }
    private void takeRight(int row, int col){
        if (!isOutOfBoard(row, col + 1)) {
            if (Board.game_board[row][col + 1] != null && !(Board.game_board[row][col + 1].color.equals(this.color)) && !(Board.game_board[row][col + 1] instanceof King)) {
                takesList.add(new Coordinates<>(row, col + 1));
            }
        }
    }
    private void takeLeft(int row, int col){
        if (!isOutOfBoard(row, col - 1)) {
            if (Board.game_board[row][col - 1] != null && !(Board.game_board[row][col - 1].color.equals(this.color)) && !(Board.game_board[row][col - 1] instanceof King)) {
                takesList.add(new Coordinates<>(row, col - 1));
            }
        }
    }
    private void castling(Pieces leftRook, Pieces rightRook){
        if(leftRook instanceof Rook) {
            if (leftRook != null && !this.moved && !((Rook)(leftRook)).moved) {
                for (int c = 1; c < 4; c++) {
                    if (Objects.equals(this.color, "black")) {
                        if (Board.game_board[0][c] != null) {
                            break;
                        }else{
                            moveList.add(new Coordinates<>(0, 2));
                        }

                    } else {
                        if (Board.game_board[7][c] != null) {
                            break;
                        }else{
                            moveList.add(new Coordinates<>(7, 2));
                        }
                    }
                }
            }
        }
        if(rightRook instanceof Rook) {
            if (rightRook != null && !this.moved && !((Rook) (rightRook)).moved) {
                for (int c = 5; c < 7; c++) {
                    if (Objects.equals(this.color, "black")) {
                        if (Board.game_board[0][c] != null) {
                            break;
                        }else{
                            moveList.add(new Coordinates<>(0, 6));
                        }
                    } else {
                        if (Board.game_board[7][c] != null) {
                            break;
                        }else{
                            moveList.add(new Coordinates<>(7, 6));
                        }
                    }

                }
            }
        }
    }
    @Override
    void legalMoves(int row, int col) {
        moveDown(row, col);
        moveUp(row, col);
        moveRight(row, col);
        moveLeft(row, col);
        moveUpLeft(row, col);
        moveDownLeft(row, col);
        moveDownRight(row, col);
        moveUpRight(row, col);
        if(this.color == PieceColor.WHITE){
            castling(Board.game_board[7][0], Board.game_board[7][7]);
        }else{
            castling(Board.game_board[0][0], Board.game_board[0][7]);
        }
    }

    @Override
    void legalTakes(int row, int col) {
        takeDownLeft(row,col);
        takeDownRight(row,col);
        takeUpLeft(row, col);
        takeUpRight(row, col);
        takeUp(row, col);
        takeDown(row,col);
        takeRight(row, col);
        takeLeft(row, col);
    }

    @Override
    void drawPiece(PieceColor color, Label label) {
        if(color == PieceColor.WHITE) {
            label.setText("♔");
        }else{
            label.setText("♚");
        }
    }

    @Override
    List<Coordinates<Integer, Integer>> getCheckPath() {
        return null;
    }

    @Override
    boolean isChecking() {
        return false;
    }
}
