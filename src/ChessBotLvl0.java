import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChessBotLvl0 extends ChessBot {
    List<Coordinates<Integer, Integer>> filteredTakesList = new ArrayList<>();
    List<Coordinates<Integer, Integer>> mergedList = new ArrayList<>();

    public ChessBotLvl0() {
        super(0);
    }

    @Override
    public Pair<Coordinates<Integer, Integer>, Coordinates<Integer, Integer>> setMove() {
        if (!Board.game_over) {
            Random random_piece = new Random();
            filteredTakesList.clear();
            mergedList.clear();
            int piece_row = random_piece.nextInt(8);
            int piece_col = random_piece.nextInt(8);
            if (Board.game_board[piece_row][piece_col] != null && Board.game_board[piece_row][piece_col].color == Board.turn.player) {
                Pieces piece = Board.game_board[piece_row][piece_col];
                piece.moveList.clear();
                piece.takesList.clear();
                piece.legalMoves(piece_row, piece_col);
                piece.legalTakes(piece_row, piece_col);
                for (Coordinates<Integer, Integer> take : piece.takesList) {
                    if (Board.game_board[take.getX()][take.getY()] != null && Board.game_board[take.getX()][take.getY()].color != Board.turn.player) {
                        filteredTakesList.add(take);
                    }
                }
                Random random_move = new Random();
                mergedList.addAll(piece.moveList);
                mergedList.addAll(filteredTakesList);
                if (mergedList.isEmpty()) {
                    return setMove();
                } else {
                    int move_index = random_move.nextInt(mergedList.size());
                    Coordinates<Integer, Integer> move = mergedList.get(move_index);
                    return new Pair<>(new Coordinates<>(piece_row, piece_col), move);
                }

            } else {
                return setMove();
            }
        }
        Board.turn.player = null;
        return null;
    }

    @Override
    public Pieces getPromotionPiece(PieceColor color, Label label) {
        Random random_piece = new Random();
        int piece_index = random_piece.nextInt(4);
        return switch (piece_index) {
            case 0 -> new Knight(color, label);
            case 1 -> new Bishop(color, label);
            case 2 -> new Rook(color, label);
            case 3 -> new Queen(color, label);
            default -> null;
        };
    }



}

