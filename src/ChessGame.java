import javafx.scene.control.Label;

import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

public class ChessGame {
    boolean new_game = false;

    void showLegalMoves(Pieces piece, int row, int col){
        piece.moveList.clear();
        showLegalTakes(piece, row, col);
        piece.legalMoves(row, col);
    }
    void showLegalTakes(Pieces playerPiece, int row, int col){
        if(playerPiece instanceof Pawn){
            ((Pawn) playerPiece).epList.clear();
        }
        playerPiece.takesList.clear();
        playerPiece.legalTakes(row, col);
    }

    void move(int row, int col, Pieces piece, int prev_row, int prev_col, PieceColor color, GameState player) {
        Pieces target = Board.game_board[row][col];

        if (target != null && !target.color.equals(piece.color)) {
            player.material += target.value;
            Board.cells[row][col].getChildren().remove(target.label);
        }
        if(target==null && piece instanceof Pawn && ((Pawn) piece).did_ep){
            player.material += 1;
            if(piece.color == PieceColor.WHITE){
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
                if(piece.color == PieceColor.WHITE){
                    Board.game_board[7][0].setPosition(7, 3);
                }else{
                    Board.game_board[0][0].setPosition(0, 3);
                }
            }
            if(prev_col - col == -2){
                if(piece.color == PieceColor.WHITE){
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
        piece.position = null;
        piece.position = new Coordinates<>(row, col);
        piece.moveList.clear();
        piece.takesList.clear();
    }

    static boolean checkIfGameOver(PieceColor king_color) {
        for (Pieces[] row : Board.game_board) {
            for (Pieces piece : row) {
                if (piece != null && piece.color == king_color) {
                    piece.legalMoves(piece.position.getX(), piece.position.getY());
                    piece.legalTakes(piece.position.getX(), piece.position.getY());
                    Iterator<Coordinates<Integer, Integer>> it = piece.takesList.iterator();
                    while (it.hasNext()) {
                        Coordinates<Integer, Integer> p = it.next();
                        if (Board.game_board[p.getX()][p.getY()] == null || Board.game_board[p.getX()][p.getY()].color.equals(piece.color)) {
                            it.remove();
                        }
                    }
                    if (!piece.moveList.isEmpty() || !piece.takesList.isEmpty()) {
                        piece.moveList.clear();
                        piece.takesList.clear();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    static boolean checkIfCheckmate(PieceColor king_color) {
        King king = (King) Board.game_board[Pieces.findFigure(King.class, king_color).getX()][Pieces.findFigure(King.class, king_color).getY()];
        if(king.isChecked &&checkIfGameOver(king_color)) {
                    return true;
        }
        return false;
    }

    static boolean checkIfStalemate(PieceColor king_color) {
        King king = (King) Board.game_board[Pieces.findFigure(King.class, king_color).getX()][Pieces.findFigure(King.class, king_color).getY()];
        if(!king.isChecked && checkIfGameOver(king_color)){
            return true;
        }
        return false;
    }

    static long[][][] allCombinations(){
        long[][][] table = new long[64][6][2];
        Random r = new Random();
        for(int i=0; i<64; i++){
            for(int j=0; j<6; j++){
                for(int k=0; k<2; k++){
                    table[i][j][k] = r.nextLong();
                }
            }
        }
        return table;
    }

    static int pieceHashValue(Pieces piece){
        switch(piece.getClass().getSimpleName()){
            case "Pawn" -> {return 0;}
            case "Knight" -> {return 1;}
            case "Bishop" -> {return 2;}
            case "Rook" -> {return 3;}
            case "Queen" -> {return 4;}
            case "King" -> {return 5;}
        }
        return -1;
    }

    static long hashBoardToNumber() {
        int pieceValue = 0;
        int squareIndex = 0;
        long hash = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (Board.game_board[i][j] != null) {
                    pieceValue = pieceHashValue(Board.game_board[i][j]);
                    squareIndex = i * 8 + j;
                    if (Board.game_board[i][j].color == PieceColor.WHITE) {
                        hash += Board.combinations_table[squareIndex][pieceValue][0];
                    } else {
                        hash += Board.combinations_table[squareIndex][pieceValue][1];
                    }
                }
            }
        }
        return hash;
    }



}
