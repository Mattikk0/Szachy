import javafx.scene.control.Label;

import java.util.Objects;

public class ChessGame {
    void showLegalMoves(Pieces piece, int row, int col){
        Pieces.moveList.clear();
        piece.legalMoves(row, col);
        showLegalTakes(piece, row, col);
    }
    void showLegalTakes(Pieces playerPiece, int row, int col){
        Pieces.takesList.clear();
        playerPiece.legalTakes(row, col);
    }
    void move(int row, int col, Pieces piece, int prev_row, int prev_col, String color, GameState player) {
        Pieces target = Board.game_board[row][col];

        if (target != null && !target.color.equals(piece.color)) {
            player.material += target.value;
            Board.cells[row][col].getChildren().remove(target.label);
        }
        if(target==null && piece instanceof Pawn && ((Pawn) piece).did_ep){
            player.material += 1;
            if(piece.color.equals("white")){
                Board.cells[row+1][col].getChildren().remove(Board.game_board[row+1][col].label);
                Board.game_board[row+1][col] = null;
            }else{
                Board.cells[row-1][col].getChildren().remove(Board.game_board[row-1][col].label);
                Board.game_board[row-1][col] = null;
            }
            ((Pawn) piece).did_ep = false;
        }
        if(piece instanceof Pawn && Math.abs(prev_row-row) == 2){
            ((Pawn) piece).movedByTwo = true;
        }
        if(piece instanceof Rook){
            ((Rook) piece).moved = true;
        }
        if(piece instanceof King){
            ((King) piece).moved = true;
            if(prev_col - col == 2){
                if(piece.color.equals("white")){
                    Board.game_board[7][0].setPosition(7, 3);
                }else{
                    Board.game_board[0][0].setPosition(0, 3);
                }
            }
            if(prev_col - col == -2){
                if(piece.color.equals("white")){
                    Board.game_board[7][7].setPosition(7, 5);
                }else{
                    Board.game_board[0][7].setPosition(0, 5);
                }
            }
        }
        Board.game_board[row][col] = piece;
        Board.game_board[prev_row][prev_col] = null;
        Board.cells[prev_row][prev_col].getChildren().remove(piece.label);
        Board.cells[row][col].getChildren().add(piece.label);
        piece.drawPiece(color, piece.label);


        Pieces.moveList.clear();
        Pieces.takesList.clear();
    }
}
