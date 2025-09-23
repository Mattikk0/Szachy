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
        filterMovesLeadingToCheck(this.moveList);
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
        filterMovesLeadingToCheck(this.takesList);
    }

    @Override
    void drawPiece(PieceColor color, Label label) {
        if(color == PieceColor.WHITE) {
            label.setText("♕");
        }else{
            label.setText("♛");
        }
    }

    @Override
    public boolean isChecking() {
        Coordinates<Integer, Integer> kingPosition = findFigure(King.class, this.color.oppositeColor());
        if (kingPosition == null) {
            return false;
        }

        int queenRow = this.position.getX();
        int queenCol = this.position.getY();
        int kingRow = kingPosition.getX();
        int kingCol = kingPosition.getY();

        if (queenRow == kingRow) {
            int start = Math.min(queenCol, kingCol) + 1;
            int end = Math.max(queenCol, kingCol);

            for (int col = start; col < end; col++) {
                if (Board.game_board[queenRow][col] != null) {
                    return false;
                }
            }

            Pieces king = Board.game_board[kingRow][kingCol];
            return king instanceof King && king.color != this.color;
        }

        if (queenCol == kingCol) {
            int start = Math.min(queenRow, kingRow) + 1;
            int end = Math.max(queenRow, kingRow);

            for (int row = start; row < end; row++) {
                if (Board.game_board[row][queenCol] != null) {
                    return false;
                }
            }

            Pieces king = Board.game_board[kingRow][kingCol];
            return king instanceof King && king.color != this.color;
        }

        int dx = Math.abs(queenRow - kingRow);
        int dy = Math.abs(queenCol - kingCol);
        int dirX = Integer.compare(kingRow, queenRow);
        int dirY = Integer.compare(kingCol, queenCol);

        if (dx == dy) {
            int r = queenRow + dirX;
            int c = queenCol + dirY;

            while (r != kingRow && c != kingCol) {
                if (Board.game_board[r][c] != null) {
                    return false;
                }
                r += dirX;
                c += dirY;
            }

            Pieces king = Board.game_board[kingRow][kingCol];
            return king instanceof King && king.color != this.color;
        }

        return false;
    }

}
