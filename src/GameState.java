import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GameState{
    private static final GameState WHITE_INSTANCE = new GameState();
    private static final GameState BLACK_INSTANCE = new GameState();
    List<Pair<Pieces, Coordinates>> piecesList = new ArrayList<>();
    int material;
    public boolean is_bot;
    public static BooleanProperty  is_bot_static = new SimpleBooleanProperty();

    public static GameState getWhiteInstance() {
        return WHITE_INSTANCE;
    }

    public static GameState getBlackInstance() {
        return BLACK_INSTANCE;
    }


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
        if (kingPos == null) {
            return false;
        }
        int row = (color == PieceColor.WHITE) ? 7 : 0;
        Pieces rookQueenSide = Board.game_board[row][0];
        Pieces king = Board.game_board[kingPos.getX()][kingPos.getY()];
        if (rookQueenSide != null && king instanceof King && !((King) king).moved){
            if(rookQueenSide instanceof Rook && !((Rook) rookQueenSide).moved) {
                return true;
            }
        }
        return false;
    }

    boolean kingsideCastlingRights(PieceColor color){
        Coordinates<Integer, Integer> kingPos = Pieces.findFigure(King.class, color);
        if (kingPos == null) {
            return false;
        }
        int row = (color == PieceColor.WHITE) ? 7 : 0;
        Pieces rookKingSide = Board.game_board[row][7];
        Pieces king = Board.game_board[kingPos.getX()][kingPos.getY()];
        if (rookKingSide != null && king instanceof King && !((King) king).moved){
            if(rookKingSide instanceof Rook && !((Rook) rookKingSide).moved) {
                return true;
            }
        }
        return false;
    }

    String buildFENString() {
        StringBuilder fen = new StringBuilder();
        fen.append("[");
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
        fen.append(Board.turn.player == PieceColor.WHITE ? 'w' : Board.turn.player == PieceColor.BLACK ? 'b' : "null");
        String castling = " ";
        if (kingsideCastlingRights(PieceColor.WHITE)) castling += "K";
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
        fen.append("]");
        fen.append("\n");
        fen.append(Board.player_on_bottom == PieceColor.WHITE ? "[WHITE: " : "[white: ");
        fen.append(GameState.WHITE_INSTANCE.is_bot  ? "BotLevel" + Board.opponent_bot.level + "]" : "Player]");
        fen.append("\n");
        fen.append(Board.player_on_bottom == PieceColor.BLACK ? "[BLACK: " : "[black: ");
        fen.append(GameState.BLACK_INSTANCE.is_bot  ? "BotLevel" + Board.opponent_bot.level + "]" : "Player]");

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
