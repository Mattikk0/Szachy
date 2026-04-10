package chess.client.bot;

import chess.client.ui.board.Board;
import chess.shared.piece.Pieces;
import chess.shared.piece.Queen;
import javafx.scene.control.Label;

import chess.shared.enums.PieceColor;
import chess.shared.piece.*;
import chess.shared.util.Coordinates;
import chess.shared.util.Pair;
import java.util.*;

public class ChessBotLvl1 extends ChessBot {
    private Pieces chosenPiece;
    private Coordinates<Integer, Integer> chosenMove;
    private Map<Integer, List<Pair<Pieces, Coordinates<Integer, Integer>>>> movesMap = new HashMap<>();
    List<Coordinates<Integer, Integer>> filteredTakesList = new ArrayList<>();
    List<Coordinates<Integer, Integer>> mergedList = new ArrayList<>();
    private final int MAX_DEPTH = 2;
    public ChessBotLvl1() {
        super(1);
    }

    @Override
    public Pair<Coordinates<Integer, Integer>, Coordinates<Integer, Integer>> setMove() {
        if (!Board.game_over) {
            int max_strength = Integer.MIN_VALUE;
            movesMap.clear();
            for (Pieces[] row : Board.game_board) {
                for (Pieces piece : row) {
                    filteredTakesList.clear();
                    mergedList.clear();
                    if (piece != null && piece.color == Board.turn.player) {
                        piece.moveList.clear();
                        piece.takesList.clear();
                        piece.legalMoves(piece.position.getX(), piece.position.getY());
                        piece.legalTakes(piece.position.getX(), piece.position.getY());
                        for (Coordinates<Integer, Integer> take : piece.takesList) {
                            if (Board.game_board[take.getX()][take.getY()] != null && Board.game_board[take.getX()][take.getY()].color != piece.color) {
                                filteredTakesList.add(take);
                            }
                        }
                        mergedList.addAll(piece.moveList);
                        mergedList.addAll(filteredTakesList);
                        for (Coordinates move : mergedList) {
                            int move_strength = piece.calculateMoveStrength(move);
                            movesMap.computeIfAbsent(move_strength, k -> new ArrayList<>()).add(new Pair<>(piece, move));
                            if (max_strength < move_strength) {
                                max_strength = move_strength;
                            }
                        }
                    }
                }
            }
            if (movesMap.isEmpty()) {
                return null;
            }
            List<Pair<Pieces, Coordinates<Integer, Integer>>> bestList = movesMap.get(max_strength);
            if (bestList == null || bestList.isEmpty()) {
                return null;
            }
            if (bestList.size() > 1) {
                Random rand = new Random();
                int randomIndex = rand.nextInt(bestList.size());
                chosenPiece = bestList.get(randomIndex).first();
                chosenMove = bestList.get(randomIndex).second();
            } else {
                chosenPiece = bestList.get(0).first();
                chosenMove = bestList.get(0).second();
            }
            Pair<Coordinates<Integer, Integer>, Coordinates<Integer, Integer>> chosenMovePair = new Pair<>(new Coordinates<>(chosenPiece.position.getX(), chosenPiece.position.getY()), new Coordinates<>(chosenMove.getX(), chosenMove.getY()));
            return chosenMovePair;
        }else{
            Board.turn = null;
            return null;
        }
    }

    @Override
    public Pieces getPromotionPiece(PieceColor color, Label label) {
        return new Queen(color, label);
    }


}