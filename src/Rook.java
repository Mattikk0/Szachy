import javafx.scene.control.Label;

import java.util.List;

public class Rook extends Pieces {
    public boolean moved;

    public Rook(PieceColor color, Label label) {
        this.drawPiece(color, label);
        this.color = color;
        this.label = label;
        this.value = 5;
        this.moved = false;
    }

    private void moveUp(int row, int col) {
        if (!isOutOfBoard(row - 1, col)) {
            if (Board.game_board[row - 1][col] == null) {
                moveList.add(new Coordinates<>(row - 1, col));
                moveUp(row - 1, col);
            }
        }
    }

    private void moveRight(int row, int col) {
        if (!isOutOfBoard(row, col + 1)) {
            if (Board.game_board[row][col + 1] == null) {
                moveList.add(new Coordinates<>(row, col + 1));
                moveRight(row, col + 1);
            }
        }
    }

    private void moveLeft(int row, int col) {
        if (!isOutOfBoard(row, col - 1)) {
            if (Board.game_board[row][col - 1] == null) {
                moveList.add(new Coordinates<>(row, col - 1));
                moveLeft(row, col - 1);
            }
        }
    }

    private void moveDown(int row, int col) {
        if (!isOutOfBoard(row + 1, col)) {
            if (Board.game_board[row + 1][col] == null) {
                moveList.add(new Coordinates<>(row + 1, col));
                moveDown(row + 1, col);
            }
        }
    }

    private void takeUp(int row, int col) {
        if (!isOutOfBoard(row - 1, col)) {
            if (Board.game_board[row - 1][col] != null && !(Board.game_board[row - 1][col].color.equals(this.color)) && !(Board.game_board[row - 1][col] instanceof King)) {
                takesList.add(new Coordinates<>(row - 1, col));
            } else if (Board.game_board[row - 1][col] == null) {
                takeUp(row - 1, col);
            }
        }
    }

    private void takeDown(int row, int col) {
        if (!isOutOfBoard(row + 1, col)) {
            if (Board.game_board[row + 1][col] != null && !(Board.game_board[row + 1][col].color.equals(this.color)) && !(Board.game_board[row + 1][col] instanceof King)) {
                takesList.add(new Coordinates<>(row + 1, col));
            } else if (Board.game_board[row + 1][col] == null) {
                takeDown(row + 1, col);
            }
        }
    }

    private void takeRight(int row, int col) {
        if (!isOutOfBoard(row, col + 1)) {
            if (Board.game_board[row][col + 1] != null && !(Board.game_board[row][col + 1].color.equals(this.color)) && !(Board.game_board[row][col + 1] instanceof King)) {
                takesList.add(new Coordinates<>(row, col + 1));
            } else if (Board.game_board[row][col + 1] == null) {
                takeRight(row, col + 1);
            }
        }
    }

    private void takeLeft(int row, int col) {
        if (!isOutOfBoard(row, col - 1)) {
            if (Board.game_board[row][col - 1] != null && !(Board.game_board[row][col - 1].color.equals(this.color)) && !(Board.game_board[row][col - 1] instanceof King)) {
                takesList.add(new Coordinates<>(row, col - 1));
            } else if (Board.game_board[row][col - 1] == null) {
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
    }

    @Override
    void legalTakes(int row, int col) {
        takeUp(row, col);
        takeDown(row, col);
        takeRight(row, col);
        takeLeft(row, col);
    }

    @Override
    void drawPiece(PieceColor color, Label label) {
        if (color == PieceColor.WHITE) {
            label.setText("♖");
        } else {
            label.setText("♜");
        }
    }

    @Override
    List<Coordinates<Integer, Integer>> getCheckPath() {
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
    public boolean isChecking() {
        return this.getCheckPath() != null;
    }
}
