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
        filterMovesLeadingToCheck(this.moveList);
    }

    @Override
    void legalTakes(int row, int col) {
        takeDownLeft(row, col);
        takeDownRight(row, col);
        takeUpLeft(row, col);
        takeUpRight(row, col);
        filterMovesLeadingToCheck(this.takesList);
    }

    @Override
    void drawPiece(PieceColor color, Label label) {
        if (color == PieceColor.WHITE) {
            label.setText("♗");
        } else {
            label.setText("♝");
        }
    }

    @Override
    public boolean isChecking() {
        Coordinates<Integer, Integer> kingPosition = findFigure(King.class, this.color.oppositeColor());
        if (kingPosition == null) return false;

        int dx = kingPosition.getX() - this.position.getX();
        int dy = kingPosition.getY() - this.position.getY();

        if (Math.abs(dx) != Math.abs(dy)) return false;

        if (dx == 0 || dy == 0) return false;

        int directionX = dx < 0 ? -1 : 1;
        int directionY = dy < 0 ? -1 : 1;

        int stepCount = Math.abs(dx) - 1;

        int r = this.position.getX();
        int c = this.position.getY();

        for (int i = 0; i < stepCount; i++) {
            r += directionX;
            c += directionY;

            if (Board.game_board[r][c] != null) {
                return false;
            }
        }

        r += directionX;
        c += directionY;

        return Board.game_board[r][c] instanceof King && Board.game_board[r][c].color != this.color;
    }

}
