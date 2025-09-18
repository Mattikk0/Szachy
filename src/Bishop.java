import javafx.scene.control.Label;

import java.util.List;

public class Bishop extends Pieces {
    Bishop(PieceColor color, Label label) {
        this.drawPiece(color, label);
        this.color = color;
        this.label = label;
        this.value = 3;
    }

    private void moveUpRight(int row, int col) {
        if (!isOutOfBoard(row - 1, col + 1) && Board.game_board[row - 1][col + 1] == null) {
            this.moveList.add(new Coordinates<>(row - 1, col + 1));
            moveUpRight(row - 1, col + 1);
        }
    }

    private void moveDownRight(int row, int col) {
        if (!isOutOfBoard(row + 1, col + 1) && Board.game_board[row + 1][col + 1] == null) {
            this.moveList.add(new Coordinates<>(row + 1, col + 1));
            moveDownRight(row + 1, col + 1);
        }
    }

    private void moveUpLeft(int row, int col) {
        if (!isOutOfBoard(row - 1, col - 1) && Board.game_board[row - 1][col - 1] == null) {
            this.moveList.add(new Coordinates<>(row - 1, col - 1));
            moveUpLeft(row - 1, col - 1);
        }
    }

    private void moveDownLeft(int row, int col) {
        if (!isOutOfBoard(row + 1, col - 1) && Board.game_board[row + 1][col - 1] == null) {
            this.moveList.add(new Coordinates<>(row + 1, col - 1));
            moveDownLeft(row + 1, col - 1);
        }
    }

    private void takeDownLeft(int row, int col) {
        if (!isOutOfBoard(row + 1, col - 1)) {
            if (Board.game_board[row + 1][col - 1] != null && !(Board.game_board[row + 1][col - 1].color.equals(this.color)) && !(Board.game_board[row + 1][col - 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row + 1, col - 1));
            } else if (Board.game_board[row + 1][col - 1] == null) {
                takeDownLeft(row + 1, col - 1);
            }
        }
    }

    private void takeDownRight(int row, int col) {
        if (!isOutOfBoard(row + 1, col + 1)) {
            if (Board.game_board[row + 1][col + 1] != null && !(Board.game_board[row + 1][col + 1].color.equals(this.color)) && !(Board.game_board[row + 1][col + 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row + 1, col + 1));
            } else if (Board.game_board[row + 1][col + 1] == null) {
                takeDownRight(row + 1, col + 1);
            }
        }
    }

    private void takeUpRight(int row, int col) {
        if (!isOutOfBoard(row - 1, col + 1)) {
            if (Board.game_board[row - 1][col + 1] != null && !(Board.game_board[row - 1][col + 1].color.equals(this.color)) && !(Board.game_board[row - 1][col + 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row - 1, col + 1));
            } else if (Board.game_board[row - 1][col + 1] == null) {
                takeUpRight(row - 1, col + 1);
            }
        }
    }

    private void takeUpLeft(int row, int col) {
        if (!isOutOfBoard(row - 1, col - 1)) {
            if (Board.game_board[row - 1][col - 1] != null && !(Board.game_board[row - 1][col - 1].color.equals(this.color)) && !(Board.game_board[row - 1][col - 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row - 1, col - 1));
            } else if (Board.game_board[row - 1][col - 1] == null) {
                takeUpLeft(row - 1, col - 1);
            }
        }
    }

    @Override
    void legalMoves(int row, int col) {
        moveUpLeft(row, col);
        moveDownLeft(row, col);
        moveDownRight(row, col);
        moveUpRight(row, col);
    }

    @Override
    void legalTakes(int row, int col) {
        takeDownLeft(row, col);
        takeDownRight(row, col);
        takeUpLeft(row, col);
        takeUpRight(row, col);
    }

    @Override
    void drawPiece(PieceColor color, Label label) {
        if (color == PieceColor.WHITE) {
            label.setText("♗");
        } else {
            label.setText("♝");
        }
    }

    public List<Coordinates<Integer, Integer>> getCheckPath(){
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
    public boolean isChecking() {
        return this.getCheckPath() != null;
    }
}
