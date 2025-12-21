import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Pieces{
    Pawn(PieceColor color, Label label){
        this.drawPiece(color, label);
        this.label = label;
        this.color = color;
        this.value = 1;
        this.movedByTwo = false;
        this.did_ep = false;
    }
    public boolean did_ep;
    public boolean movedByTwo;
    public static List<Coordinates<Integer, Integer>> epList = new ArrayList<>();
    @Override
    public void legalMoves(int row, int col) {
        if(this.color == Board.player_on_bottom){
            if(!isOutOfBoard(row-1, col)) {
                if (Board.game_board[row - 1][col] == null) {
                    this.moveList.add(new Coordinates<>(row - 1, col));
                    if (!isOutOfBoard(row-2, col) && Board.game_board[row - 2][col] == null && row == 6) {
                        this.moveList.add(new Coordinates<>(row - 2, col));
                    }
                }
            }
        }else{
            if(!isOutOfBoard(row+1, col)) {
                if (Board.game_board[row + 1][col] == null) {
                    this.moveList.add(new Coordinates<>(row + 1, col));
                    if (!isOutOfBoard(row+2, col) && Board.game_board[row + 2][col] == null && row == 1) {
                        this.moveList.add(new Coordinates<>(row + 2, col));
                    }
                }
            }
        }
        filterMovesLeadingToCheck(this.moveList);
    }

    @Override
    void legalTakes(int row, int col) {
        enPassant(row, col);
        if(this.color == Board.player_on_bottom){
            if(!isOutOfBoard(row-1, col+1) && !(Board.game_board[row-1][col+1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row-1, col+1));
            }
            if(!isOutOfBoard(row-1, col-1) && !(Board.game_board[row-1][col-1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row-1, col-1));
            }
        }else{
            if(!isOutOfBoard(row+1, col+1) && !(Board.game_board[row+1][col+1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row+1, col+1));
            }
            if(!isOutOfBoard(row+1, col-1) && !(Board.game_board[row+1][col-1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row+1, col-1));
            }
        }
        filterMovesLeadingToCheck(this.takesList);
    }

    @Override
    void drawPiece(PieceColor color, Label label) {
        if(color == PieceColor.WHITE) {
            label.setText("♙");
        }else{
            label.setText("♟");
        }
    }

    @Override
    boolean isChecking() {
        Coordinates king_position;
        king_position = findFigure(King.class, this.color.oppositeColor());
        if (king_position == null) return false;
        if(this.color == Board.player_on_bottom){
            if((king_position.getX() == this.position.getX() - 1 && king_position.getY() == this.position.getY()-1) || (king_position.getX() == this.position.getX()-1 && king_position.getY() == this.position.getY()+1)){
                return true;
            }
        }else{
            if((king_position.getX() == this.position.getX() + 1 && king_position.getY() == this.position.getY()-1) || (king_position.getX() == this.position.getX()+1 && king_position.getY() == this.position.getY()+1)){
                return true;
            }
        }
        return false;
    }

    @Override
    int calculateMoveStrength(Coordinates<Integer, Integer> move) {
        int strength = 0;
        if(!(move == null)) {
            if (this.color == Board.player_on_bottom) {
                strength += (7 - move.getX());
            } else {
                strength += move.getX();
            }
            if (this.takesList.contains(move) && Board.game_board[move.getX()][move.getY()] != null && Board.game_board[move.getX()][move.getY()].color != this.color) {
                strength += Board.game_board[move.getX()][move.getY()].value;
                for (int row = 0; row < 8; row++) {
                    if (Board.game_board[row][move.getY()] != null && Board.game_board[row][move.getY()] instanceof Pawn && Board.game_board[row][move.getY()].color == this.color) {
                        strength -= 1;
                    }
                }
            }
            int oldRow = this.position.getX();
            int oldCol = this.position.getY();
            simulateMove(move);
            if (this.isChecking()) {
                strength += 5;
                if (ChessGame.checkIfGameOver(this.color.oppositeColor())) {
                    strength += 1000;
                }
            }
            if (this.canBeTaken()) {
                strength -= 3;
            }
            legalTakes(this.position.getX(), this.position.getY());
            for (Coordinates<Integer, Integer> take : this.takesList) {
                if (take != null && Board.game_board[take.getX()][take.getY()] != null && Board.game_board[take.getX()][take.getY()].color != this.color) {
                    strength += Board.game_board[take.getX()][take.getY()].value;
                }
            }
            undoSimulateMove(move, oldRow, oldCol);
        }
        return strength;
    }

    void enPassant(int row, int col){
        if(!isOutOfBoard(row, col+1) && Board.game_board[row][col+1] != null && Board.game_board[row][col+1] instanceof Pawn && Board.game_board[row][col+1].color != this.color && ((Pawn) Board.game_board[row][col+1]).movedByTwo == true){
            if(this.color == Board.player_on_bottom){
                epList.add(new Coordinates<>(row-1, col+1));
                this.did_ep = true;
            }else{
                epList.add(new Coordinates<>(row+1, col+1));
                this.did_ep = true;
            }
        }
        if(!isOutOfBoard(row, col-1) && Board.game_board[row][col-1] != null && Board.game_board[row][col-1] instanceof Pawn && Board.game_board[row][col-1].color != this.color && ((Pawn) Board.game_board[row][col-1]).movedByTwo == true){
            if(this.color == Board.player_on_bottom){
                epList.add(new Coordinates<>(row-1, col-1));
                this.did_ep = true;
            }else{
                epList.add(new Coordinates<>(row+1, col-1));
                this.did_ep = true;
            }
        }
        filterMovesLeadingToCheck(this.epList);
    }

}
