import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class King extends Pieces{
    public boolean moved;
    public boolean isChecked;
    public List<Coordinates<Integer, Integer>> zone = new ArrayList<>();
    public King(PieceColor color, Label label) {
        this.drawPiece(color, label);
        this.color = color;
        this.label = label;
        this.moved = false;
        this.isChecked = false;
    }
    private void moveUpRight(int row, int col){
        if (!isOutOfBoard(row - 1, col + 1) && Board.game_board[row - 1][col + 1] == null) {
            this.moveList.add(new Coordinates<>(row - 1, col + 1));
        }
    }
    private void moveDownRight(int row, int col){
        if (!isOutOfBoard(row + 1, col + 1) && Board.game_board[row + 1][col + 1] == null) {
            this.moveList.add(new Coordinates<>(row + 1, col + 1));
        }
    }
    private void moveUpLeft(int row, int col){
        if(!isOutOfBoard(row - 1, col - 1) && Board.game_board[row - 1][col - 1] == null) {
            this.moveList.add(new Coordinates<>(row - 1, col - 1));
        }
    }
    private void moveDownLeft(int row, int col){
        if (!isOutOfBoard(row + 1, col - 1) && Board.game_board[row + 1][col - 1] == null) {
            this.moveList.add(new Coordinates<>(row + 1, col - 1));
        }
    }
    private void takeDownLeft(int row, int col){
        if (!isOutOfBoard(row + 1, col - 1)) {
            if (!(Board.game_board[row + 1][col - 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row + 1, col - 1));
            }
        }
    }
    private void takeDownRight(int row, int col){
        if (!isOutOfBoard(row + 1, col + 1)) {
            if (!(Board.game_board[row + 1][col + 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row + 1, col + 1));
            }
        }
    }
    private void takeUpRight(int row, int col){
        if (!isOutOfBoard(row - 1, col + 1)) {
            if (!(Board.game_board[row - 1][col + 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row - 1, col + 1));
            }
        }
    }
    private void takeUpLeft(int row, int col){
        if (!isOutOfBoard(row - 1, col - 1)) {
            if (!(Board.game_board[row - 1][col - 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row - 1, col - 1));
            }
        }
    }
    private void moveUp(int row, int col){
        if (!isOutOfBoard(row - 1, col)) {
            if (Board.game_board[row - 1][col] == null) {
                this.moveList.add(new Coordinates<>(row - 1, col));
            }
        }
    }
    private void moveRight(int row, int col){
        if (!isOutOfBoard(row, col + 1)) {
            if (Board.game_board[row][col + 1] == null) {
                this.moveList.add(new Coordinates<>(row, col + 1));
            }
        }
    }
    private void moveLeft(int row, int col){
        if (!isOutOfBoard(row, col - 1)) {
            if (Board.game_board[row][col - 1] == null) {
                this.moveList.add(new Coordinates<>(row, col - 1));
            }
        }
    }
    private void moveDown(int row, int col){
        if (!isOutOfBoard(row + 1, col)) {
            if (Board.game_board[row + 1][col] == null) {
                this.moveList.add(new Coordinates<>(row + 1, col));
            }
        }
    }
    private void takeUp(int row, int col) {
        if (!isOutOfBoard(row - 1, col)) {
            if (!(Board.game_board[row - 1][col] instanceof King)) {
                this.takesList.add(new Coordinates<>(row - 1, col));
            }
        }
    }
    private void takeDown(int row, int col) {
        if (!isOutOfBoard(row + 1, col)) {
            if (!(Board.game_board[row + 1][col] instanceof King)) {
                this.takesList.add(new Coordinates<>(row + 1, col));
            }
        }
    }
    private void takeRight(int row, int col){
        if (!isOutOfBoard(row, col + 1)) {
            if (!(Board.game_board[row][col + 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row, col + 1));
            }
        }
    }
    private void takeLeft(int row, int col){
        if (!isOutOfBoard(row, col - 1)) {
            if (!(Board.game_board[row][col - 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row, col - 1));
            }
        }
    }
    private void castling(Pieces leftRook, Pieces rightRook) {
        if (leftRook instanceof Rook) {
            if (leftRook != null && !this.moved && !((Rook) leftRook).moved) {
                boolean canCastleQueenside = true;
                int startCol = this.color == PieceColor.BLACK ? 1 : 1;
                int endCol = this.color == PieceColor.BLACK ? 3 : 3;

                for (int c = startCol; c <= endCol; c++) {
                    if (Board.game_board[this.position.getX()][c] != null) {
                        canCastleQueenside = false;
                        break;
                    }
                }

                if (canCastleQueenside) {
                    this.moveList.add(this.color == PieceColor.BLACK ? new Coordinates<>(0, 2) : new Coordinates<>(7, 2));
                }
            }
        }

        if (rightRook instanceof Rook) {
            if (rightRook != null && !this.moved && !((Rook) rightRook).moved) {
                boolean canCastleKingside = true;
                int startCol = this.color == PieceColor.BLACK ? 5 : 5;
                int endCol = this.color == PieceColor.BLACK ? 6 : 6;

                for (int c = startCol; c <= endCol; c++) {
                    if (Board.game_board[this.position.getX()][c] != null) {
                        canCastleKingside = false;
                        break;
                    }
                }

                if (canCastleKingside) {
                    this.moveList.add(this.color == PieceColor.BLACK ? new Coordinates<>(0, 6) : new Coordinates<>(7, 6));
                }
            }
        }
    }

    void avoidOppKing(List<Coordinates<Integer, Integer>> filteredList) {
        Coordinates opp_king_position = findFigure(King.class, this.color.oppositeColor());
        for(Coordinates field : this.zone){
            if(((King)Board.game_board[opp_king_position.getX()][opp_king_position.getY()]).zone.contains(field)){
                filteredList.remove(field);
            }
        }
    }

    void updateZone() {
        zone.clear();
        int row = this.position.getX();
        int col = this.position.getY();

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;

                int newRow = row + dr;
                int newCol = col + dc;

                if (!isOutOfBoard(newRow, newCol)) {
                    zone.add(new Coordinates<>(newRow, newCol));
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
        avoidOppKing(this.moveList);
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
        avoidOppKing(this.takesList);
        filterMovesLeadingToCheck(this.takesList);
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
    boolean isChecking() {
        return false;
    }
}
