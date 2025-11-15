import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GameState {
    List<Pair<Pieces, Coordinates>> piecesList = new ArrayList<>();
    int material;


    void getBoard(Pieces[][] board){
        this.piecesList.clear();
        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < board[row].length; col++){
                Pieces piece = board[row][col];
                if(piece != null){
                    Coordinates<Integer, Integer> coordinates = new Coordinates<>(row, col);
                    this.piecesList.add(new Pair<>(piece, coordinates));
                }
            }
        }
    }

    boolean queenSideCastling(PieceColor color){
        Coordinates<Integer, Integer> kingPos = Pieces.findFigure(King.class, color);
        Pieces rookQueenSide = null;
        if (kingPos == null) {
            return false;
        }
        if(color == PieceColor.WHITE){
            if(Board.game_board[0][7] != null) {
                rookQueenSide = Board.game_board[0][0];
            }
        }else{
            if(Board.game_board[7][7] != null) {
                rookQueenSide = Board.game_board[7][0];
            }
        }
        Pieces king = Board.game_board[kingPos.getX()][kingPos.getY()];
        if(rookQueenSide != null && !((King) king).moved){
            if(rookQueenSide instanceof Rook && !((Rook) rookQueenSide).moved) {
                return true;
            }
        }
        return false;
    }

    boolean kingsideCastlingRights(PieceColor color){
        Coordinates<Integer, Integer> kingPos = Pieces.findFigure(King.class, color);
        Pieces rookKingSide = null;
        if (kingPos == null) {
            return false;
        }
        if(color == PieceColor.WHITE){
            if(Board.game_board[0][7] != null) {
                rookKingSide = Board.game_board[0][7];
            }
        }else{
            if(Board.game_board[7][7] != null) {
                rookKingSide = Board.game_board[7][7];
            }
        }
        Pieces king = Board.game_board[kingPos.getX()][kingPos.getY()];
        if(rookKingSide != null && !((King) king).moved){
            if(rookKingSide instanceof Rook && !((Rook) rookKingSide).moved) {
                return true;
            }
        }
        return false;
    }

    String buildFENString() {
        StringBuilder fen = new StringBuilder();
        for (int row = 0; row < 8; row++) {
            int emptyCount = 0;
            for (int col = 0; col < 8; col++) {
                Pieces piece = Board.game_board[row][col];
                if (piece == null) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    char pieceChar = switch (piece.getClass().getSimpleName()) {
                        case "Pawn" -> 'p';
                        case "Rook" -> 'r';
                        case "Knight" -> 'n';
                        case "Bishop" -> 'b';
                        case "Queen" -> 'q';
                        case "King" -> 'k';
                        default -> ' ';
                    };
                    if (piece.color == PieceColor.WHITE) {
                        pieceChar = Character.toUpperCase(pieceChar);
                    }
                    fen.append(pieceChar);
                }
            }
            if (emptyCount > 0) {
                fen.append(emptyCount);
            }
            if (row < 7) {
                fen.append('/');
            }
        }
        fen.append(" ");
        fen.append(Board.turn.player == PieceColor.WHITE ? 'w' : 'b');
        String castling = "";
        if (kingsideCastlingRights(PieceColor.WHITE)) castling += " K";
        if (queenSideCastling(PieceColor.WHITE)) castling += "Q";
        if (kingsideCastlingRights(PieceColor.BLACK)) castling += "k";
        if (queenSideCastling(PieceColor.BLACK)) castling += "q";
        fen.append(castling);
        fen.append(" ");
        if (Pawn.epList.isEmpty()) {
            fen.append("-");
        } else {
            Coordinates<Integer, Integer> epSquare = Pawn.epList.get(Pawn.epList.size() - 1);
            char file = (char) ('a' + epSquare.getY());
            int rank = 8 - epSquare.getX();
            fen.append(file).append(rank);
        }
        fen.append(" ");
        fen.append(ChessGame.no_progress_moves);
        fen.append(" ");
        fen.append(ChessGame.full_turns);
        return fen.toString();
    }


    void saveToFile(){
        try (PrintWriter writer = new PrintWriter("SavedGame.pgn")) {
            String fenString = buildFENString();
            writer.println(fenString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
