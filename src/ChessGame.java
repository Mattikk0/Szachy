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
        System.out.println(Pieces.takesList);
    }
    void move(int row, int col, Pieces piece, int prev_row, int prev_col, String color) {
        Pieces target = Board.game_board[row][col];

        if (target != null && !target.color.equals(piece.color)) {
            GameState player = piece.convertToGS(row, col);
            player.material += target.value;
            Board.cells[row][col].getChildren().remove(target.label);
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
