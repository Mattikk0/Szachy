import javafx.scene.control.Label;

import java.util.ArrayList;
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
                this.moveList.add(new Coordinates<>(row - 1, col));
                moveUp(row - 1, col);
            }
        }
    }

    private void moveRight(int row, int col) {
        if (!isOutOfBoard(row, col + 1)) {
            if (Board.game_board[row][col + 1] == null) {
                this.moveList.add(new Coordinates<>(row, col + 1));
                moveRight(row, col + 1);
            }
        }
    }

    private void moveLeft(int row, int col) {
        if (!isOutOfBoard(row, col - 1)) {
            if (Board.game_board[row][col - 1] == null) {
                this.moveList.add(new Coordinates<>(row, col - 1));
                moveLeft(row, col - 1);
            }
        }
    }

    private void moveDown(int row, int col) {
        if (!isOutOfBoard(row + 1, col)) {
            if (Board.game_board[row + 1][col] == null) {
                this.moveList.add(new Coordinates<>(row + 1, col));
                moveDown(row + 1, col);
            }
        }
    }

    private void takeUp(int row, int col) {
        if (!isOutOfBoard(row - 1, col)) {
            if (Board.game_board[row - 1][col] != null && !(Board.game_board[row - 1][col].color.equals(this.color)) && !(Board.game_board[row - 1][col] instanceof King)) {
                this.takesList.add(new Coordinates<>(row - 1, col));
            } else if (Board.game_board[row - 1][col] == null) {
                takeUp(row - 1, col);
            }
        }
    }

    private void takeDown(int row, int col) {
        if (!isOutOfBoard(row + 1, col)) {
            if (Board.game_board[row + 1][col] != null && !(Board.game_board[row + 1][col].color.equals(this.color)) && !(Board.game_board[row + 1][col] instanceof King)) {
                this.takesList.add(new Coordinates<>(row + 1, col));
            } else if (Board.game_board[row + 1][col] == null) {
                takeDown(row + 1, col);
            }
        }
    }

    private void takeRight(int row, int col) {
        if (!isOutOfBoard(row, col + 1)) {
            if (Board.game_board[row][col + 1] != null && !(Board.game_board[row][col + 1].color.equals(this.color)) && !(Board.game_board[row][col + 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row, col + 1));
            } else if (Board.game_board[row][col + 1] == null) {
                takeRight(row, col + 1);
            }
        }
    }

    private void takeLeft(int row, int col) {
        if (!isOutOfBoard(row, col - 1)) {
            if (Board.game_board[row][col - 1] != null && !(Board.game_board[row][col - 1].color.equals(this.color)) && !(Board.game_board[row][col - 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row, col - 1));
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
        filterMovesLeadingToCheck(this.moveList);
    }

    @Override
    void legalTakes(int row, int col) {
        takeUp(row, col);
        takeDown(row, col);
        takeRight(row, col);
        takeLeft(row, col);
        filterMovesLeadingToCheck(this.takesList);
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
    public boolean isChecking() {
        Coordinates<Integer, Integer> kingPosition = findFigure(King.class, this.color.oppositeColor());
        if (kingPosition == null) {
            return false;
        }

        if (this.position.getX() != kingPosition.getX() && this.position.getY() != kingPosition.getY()) {
            return false;
        }

        if (this.position.getX() == kingPosition.getX()) {
            int startCol = Math.min(this.position.getY(), kingPosition.getY());
            int endCol = Math.max(this.position.getY(), kingPosition.getY());

            for (int col = startCol + 1; col < endCol; col++) {
                if (Board.game_board[this.position.getX()][col] != null) {
                    return false;
                }
            }

            Pieces finalPiece = Board.game_board[kingPosition.getX()][kingPosition.getY()];
            return finalPiece instanceof King && finalPiece.color != this.color;
        }
        if (this.position.getY() == kingPosition.getY()) {
            int startRow = Math.min(this.position.getX(), kingPosition.getX());
            int endRow = Math.max(this.position.getX(), kingPosition.getX());

            for (int row = startRow + 1; row < endRow; row++) {
                if (Board.game_board[row][this.position.getY()] != null) {
                    return false;
                }
            }

            Pieces finalPiece = Board.game_board[kingPosition.getX()][kingPosition.getY()];
            return finalPiece instanceof King && finalPiece.color != this.color;
        }

        return false;
    }
    @Override
    int calculateMoveStrength(Coordinates<Integer, Integer> move) {
        int strength = 0;
        int oldRow = this.position.getX();
        int oldCol = this.position.getY();
        if(!(move == null)) {
            this.takesList.clear();
            this.moveList.clear();
            this.legalTakes(this.position.getX(), this.position.getY());
            this.legalMoves(this.position.getX(), this.position.getY());
            if (this.takesList.contains(move) && Board.game_board[move.getX()][move.getY()] != null && Board.game_board[move.getX()][move.getY()].color != this.color) {
                strength += Board.game_board[move.getX()][move.getY()].value * 100;
                simulateMove(move);
                if (this.canBeTaken()) {
                    strength -= this.value * 100;
                }
                undoSimulateMove(move, oldRow, oldCol);
            }
            if (this.canBeTaken()) {
                strength += this.value * 100;
            }
            for(int row = 0; row<8; row++){
                if(Board.game_board[row][move.getY()] != null && Board.game_board[row][move.getY()].color == this.color && !(Board.game_board[row][move.getY()] instanceof Rook)){
                    strength += 25;
                }
            }
            for(int col = 0; col<8; col++){
                if(Board.game_board[move.getX()][col] != null && Board.game_board[move.getX()][col].color == this.color && !(Board.game_board[move.getX()][col] instanceof Rook)){
                    strength += 25;
                }
            }
            simulateMove(move);
            if (this.isChecking()) {
                strength += 30;
                if (ChessGame.checkIfGameOver(this.color.oppositeColor())) {
                    strength += 1000;
                }
            }
            if (this.canBeTaken()) {
                strength -= this.value * 100;
            }
            legalTakes(this.position.getX(), this.position.getY());
            legalMoves(this.position.getX(), this.position.getY());
            List<Coordinates<Integer, Integer>> combinedList = new ArrayList<>();
            combinedList.addAll(this.moveList);
            combinedList.addAll(this.takesList);
            if(combinedList.size() > 8){
                strength += 30;
            }
            undoSimulateMove(move, oldRow, oldCol);
        }
        return strength;
    }

}
