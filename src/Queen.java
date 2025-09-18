import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Queen extends Pieces{
    public Queen(PieceColor color, Label label) {
        this.drawPiece(color, label);
        this.color = color;
        this.label = label;
        this.value = 9;
    }
    private void moveUpRight(int row, int col){
        if (!isOutOfBoard(row - 1, col + 1) && Board.game_board[row - 1][col + 1] == null) {
            this.moveList.add(new Coordinates<>(row - 1, col + 1));
            moveUpRight(row - 1, col + 1);
        }
    }
    private void moveDownRight(int row, int col){
        if (!isOutOfBoard(row + 1, col + 1) && Board.game_board[row + 1][col + 1] == null) {
            this.moveList.add(new Coordinates<>(row + 1, col + 1));
            moveDownRight(row + 1, col + 1);
        }
    }
    private void moveUpLeft(int row, int col){
        if(!isOutOfBoard(row - 1, col - 1) && Board.game_board[row - 1][col - 1] == null) {
            this.moveList.add(new Coordinates<>(row - 1, col - 1));
            moveUpLeft(row - 1, col - 1);
        }
    }
    private void moveDownLeft(int row, int col){
        if (!isOutOfBoard(row + 1, col - 1) && Board.game_board[row + 1][col - 1] == null) {
            this.moveList.add(new Coordinates<>(row + 1, col - 1));
            moveDownLeft(row + 1, col - 1);
        }
    }
    private void takeDownLeft(int row, int col){
        if (!isOutOfBoard(row + 1, col - 1)) {
            if (Board.game_board[row + 1][col - 1] != null && !(Board.game_board[row + 1][col - 1].color.equals(this.color)) && !(Board.game_board[row + 1][col - 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row + 1, col - 1));
            }else if(Board.game_board[row + 1][col - 1] == null){
                takeDownLeft(row + 1, col - 1);
            }
        }
    }
    private void takeDownRight(int row, int col){
        if (!isOutOfBoard(row + 1, col + 1)) {
            if (Board.game_board[row + 1][col + 1] != null && !(Board.game_board[row + 1][col + 1].color.equals(this.color)) && !(Board.game_board[row + 1][col + 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row + 1, col + 1));
            }else if(Board.game_board[row + 1][col + 1] == null){
                takeDownRight(row + 1, col + 1);
            }
        }
    }
    private void takeUpRight(int row, int col){
        if (!isOutOfBoard(row - 1, col + 1)) {
            if (Board.game_board[row - 1][col + 1] != null && !(Board.game_board[row - 1][col + 1].color.equals(this.color)) && !(Board.game_board[row - 1][col + 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row - 1, col + 1));
            }else if(Board.game_board[row - 1][col + 1] == null){
                takeUpRight(row - 1, col + 1);
            }
        }
    }
    private void takeUpLeft(int row, int col){
        if (!isOutOfBoard(row - 1, col - 1)) {
            if (Board.game_board[row - 1][col - 1] != null && !(Board.game_board[row - 1][col - 1].color.equals(this.color)) && !(Board.game_board[row - 1][col - 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row - 1, col - 1));
            }else if(Board.game_board[row - 1][col - 1] == null){
                takeUpLeft(row - 1, col - 1);
            }
        }
    }
    private void moveUp(int row, int col){
        if (!isOutOfBoard(row - 1, col)) {
            if (Board.game_board[row - 1][col] == null) {
                this.moveList.add(new Coordinates<>(row - 1, col));
                moveUp(row - 1, col);
            }
        }
    }
    private void moveRight(int row, int col){
        if (!isOutOfBoard(row, col + 1)) {
            if (Board.game_board[row][col + 1] == null) {
                this.moveList.add(new Coordinates<>(row, col + 1));
                moveRight(row, col + 1);
            }
        }
    }
    private void moveLeft(int row, int col){
        if (!isOutOfBoard(row, col - 1)) {
            if (Board.game_board[row][col - 1] == null) {
                this.moveList.add(new Coordinates<>(row, col - 1));
                moveLeft(row, col - 1);
            }
        }
    }
    private void moveDown(int row, int col){
        if (!isOutOfBoard(row + 1, col)) {
            if (Board.game_board[row + 1][col] == null) {
                this.moveList.add(new Coordinates<>(row + 1, col));
                moveDown(row + 1, col);
            }
        }
    }
    private void takeUp(int row, int col) {
        if (!isOutOfBoard(row - 1, col)) {
            if (Board.game_board[row - 1][col] != null && !(Board.game_board[row - 1][col].color.equals(this.color))  && !(Board.game_board[row - 1][col] instanceof King)) {
                this.takesList.add(new Coordinates<>(row - 1, col));
            } else if(Board.game_board[row - 1][col] == null){
                takeUp(row - 1, col);
            }
        }
    }
    private void takeDown(int row, int col) {
        if (!isOutOfBoard(row + 1, col)) {
            if (Board.game_board[row + 1][col] != null && !(Board.game_board[row + 1][col].color.equals(this.color)) && !(Board.game_board[row + 1][col] instanceof King)) {
                this.takesList.add(new Coordinates<>(row + 1, col));
            }else if(Board.game_board[row + 1][col] == null){
                takeDown(row + 1, col);
            }
        }
    }
    private void takeRight(int row, int col){
        if (!isOutOfBoard(row, col + 1)) {
            if (Board.game_board[row][col + 1] != null && !(Board.game_board[row][col + 1].color.equals(this.color)) && !(Board.game_board[row][col + 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row, col + 1));
            }else if(Board.game_board[row][col + 1] == null){
                takeRight(row, col + 1);
            }
        }
    }
    private void takeLeft(int row, int col){
        if (!isOutOfBoard(row, col - 1)) {
            if (Board.game_board[row][col - 1] != null && !(Board.game_board[row][col - 1].color.equals(this.color)) && !(Board.game_board[row][col - 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row, col - 1));
            }else if(Board.game_board[row][col - 1] == null){
                takeLeft(row, col - 1);
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
            label.setText("♕");
        }else{
            label.setText("♛");
        }
    }

    List<Coordinates<Integer, Integer>> bishopCheckPath(){
        Coordinates king_position = null;
        king_position = findFigure(King.class, this.color.oppositeColor());
        int drow = Integer.compare(king_position.getX(), this.position.getX());
        int dcol = Integer.compare(king_position.getY(), this.position.getY());
        int row = this.position.getX() + drow;
        int col = this.position.getY() + dcol;
        while(!isOutOfBoard(row, col)){
            if(Board.game_board[row][col] != null){
                if(Board.game_board[row][col] instanceof King && Board.game_board[row][col].color != this.color){
                    return this.checkPath;
                }else{
                    this.checkPath.clear();
                    break;
                }
            }
            this.checkPath.add(new Coordinates<>(row, col));
            row+=drow;
            col+=dcol;
        }
        return null;
    }
    List<Coordinates<Integer, Integer>> rookCheckPath(){
        Coordinates king_position = null;
        king_position = findFigure(King.class, this.color.oppositeColor());
        int[][] directions = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };
        for (int[] dir : directions) {
            checkPath.clear();
            int row = this.position.getX() + dir[0];
            int col = this.position.getY() + dir[1];
            while (!isOutOfBoard(row, col)) {
                if (Board.game_board[row][col] != null) {
                    if (Board.game_board[row][col] instanceof King && Board.game_board[row][col].color != this.color) {
                        return this.checkPath;
                    } else {
                        this.checkPath.clear();
                        break;
                    }
                }
                this.checkPath.add(new Coordinates(row, col));
                row += dir[0];
                col += dir[1];
            }
        }
        return null;
    }
    @Override
    List<Coordinates<Integer, Integer>> getCheckPath() {
        List<Coordinates<Integer, Integer>> rook = rookCheckPath();
        List<Coordinates<Integer, Integer>> bishop = bishopCheckPath();
        Set<Coordinates<Integer, Integer>> merged = new LinkedHashSet<>();
        if(rook != null) merged.addAll(rook);
        if(bishop != null) merged.addAll(bishop);
        if(!merged.isEmpty()){
            this.checkPath = new ArrayList<>(merged);
            return checkPath;
        }
        return null;
    }

    @Override
    public boolean isChecking() {
        return this.getCheckPath() != null;
    }
}
