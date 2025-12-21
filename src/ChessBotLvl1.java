import javafx.scene.control.Label;

import java.util.*;

public class ChessBotLvl1 extends ChessBot{
    private Pieces chosenPiece;
    private Coordinates<Integer, Integer> chosenMove;
    private Map<Integer, List<Pair<Pieces, Coordinates<Integer, Integer>>>> movesMap = new HashMap<>();
    private Coordinates<Integer, Integer> checkedMove;
    List<Coordinates<Integer, Integer>> filteredTakesList = new ArrayList<>();
    List<Coordinates<Integer, Integer>> mergedList = new ArrayList<>();
    private final int MAX_DEPTH = 2;
    public ChessBotLvl1() {
        super(1);
    }

    @Override
    public Pair<Coordinates<Integer, Integer>, Coordinates<Integer, Integer>> setMove() {
        movesMap.clear();
        int max_strength = minimax(0, true, Board.current, Board.turn.player);
        if(movesMap.isEmpty()){
            return null;
        }
        List<Pair<Pieces, Coordinates<Integer, Integer>>> bestList = movesMap.get(max_strength);
        if(bestList == null || bestList.isEmpty()){
            return null;
        }
        Pair<Pieces, Coordinates<Integer, Integer>> chosen = bestList.get(0);
        chosenPiece = movesMap.get(max_strength).get(0).first();
        chosenMove = movesMap.get(max_strength).get(0).second();
        Pair<Coordinates<Integer, Integer>, Coordinates<Integer, Integer>> chosenMovePair = new Pair<>(new Coordinates<>(chosenPiece.position.getX(), chosenPiece.position.getY()), new Coordinates<>(chosenMove.getX(), chosenMove.getY()));;
        return chosenMovePair;
    }

    @Override
    public Pieces getPromotionPiece(PieceColor color, Label label) {
        return null;
    }

    private int evaluateBoard(GameState state, boolean isMinimalizing) {
        int score = 0;
        for (Pair<Pieces, Coordinates> p : state.piecesList) {
            score = p.first().calculateMoveStrength(checkedMove);
        }
        if(isMinimalizing){
            score = -score;
        }
        return score;
    }

    private int minimax(int depth, boolean botMove, GameState current_state, PieceColor current_turn){
        filteredTakesList.clear();
        mergedList.clear();
        if (depth >= MAX_DEPTH) {
            return evaluateBoard(current_state, false);
        }
        if(Board.game_over){
            Board.turn.player = null;
            if(ChessGame.winner == null){
                return 0;
            }else if(ChessGame.winner.equals("WHITE")){
                return 10 - depth;
            }else if(ChessGame.winner.equals("BLACK")){
                return depth - 10;
            }
        }else {
            if (botMove) {
                PieceColor opp_turn = current_turn.oppositeColor();
                GameState opp_state;
                if(Board.turn.player == PieceColor.WHITE){
                    opp_state = GameState.getBlackInstance();
                }else{
                    opp_state = GameState.getWhiteInstance();
                }
                int max_move_strength = Integer.MIN_VALUE;
                current_state.getBoard(Board.game_board);
                for(Pair<Pieces, Coordinates> piece : new ArrayList<>(current_state.piecesList)){
                    if(piece.first().color == current_turn){
                        Coordinates<Integer, Integer> coords = piece.second();
                        piece.first().moveList.clear();
                        piece.first().takesList.clear();
                        piece.first().legalMoves(coords.getX(), coords.getY());
                        piece.first().legalTakes(coords.getX(), coords.getY());
                        for (Coordinates<Integer, Integer> take : piece.first().takesList) {
                            if (Board.game_board[take.getX()][take.getY()] != null && Board.game_board[take.getX()][take.getY()].color != Board.turn.player) {
                                filteredTakesList.add(take);
                            }
                        }
                        mergedList.addAll(piece.first().moveList);
                        mergedList.addAll(filteredTakesList);
                        for(Coordinates<Integer, Integer> move : new ArrayList<>(mergedList)){
                            Pieces originalTarget = Board.game_board[move.getX()][move.getY()];
                            Coordinates<Integer, Integer> originalPosition = piece.second();
                            Board.game_board[move.getX()][move.getY()] = piece.first();
                            Board.game_board[originalPosition.getX()][originalPosition.getY()] = null;
                            piece.first().position = move;
                            int move_strength = minimax(depth + 1, false, opp_state, opp_turn);
                            if(move_strength > max_move_strength){
                                max_move_strength = move_strength;
                            }
                            if(depth == 0){
                                movesMap.computeIfAbsent(move_strength, k -> new ArrayList<>()).add(new Pair<>(piece.first(), move));
                                checkedMove = move;
                            }
                            Board.game_board[originalPosition.getX()][originalPosition.getY()] = piece.first();
                            Board.game_board[move.getX()][move.getY()] = originalTarget;
                            piece.first().position = originalPosition;
                        }
                    }
                }
                return (max_move_strength == Integer.MIN_VALUE) ? evaluateBoard(current_state, false) : max_move_strength;
            }else{
                PieceColor bot_turn = current_turn.oppositeColor();
                GameState bot_state;
                if(current_turn == PieceColor.WHITE){
                    bot_state = GameState.getBlackInstance();
                }else{
                    bot_state = GameState.getWhiteInstance();
                }
                current_turn = current_turn.oppositeColor();
                int min_move_strength = Integer.MAX_VALUE;
                current_state.getBoard(Board.game_board);
                for(Pair<Pieces, Coordinates> piece : new ArrayList<>(current_state.piecesList)){
                    if(piece.first().color == current_turn){
                        Coordinates<Integer, Integer> coords = piece.second();
                        piece.first().moveList.clear();
                        piece.first().takesList.clear();
                        piece.first().legalMoves(coords.getX(), coords.getY());
                        piece.first().legalTakes(coords.getX(), coords.getY());
                        for (Coordinates<Integer, Integer> take : piece.first().takesList) {
                            if (Board.game_board[take.getX()][take.getY()] != null && Board.game_board[take.getX()][take.getY()].color != Board.turn.player) {
                                filteredTakesList.add(take);
                            }
                        }
                        mergedList.addAll(piece.first().moveList);
                        mergedList.addAll(filteredTakesList);
                        for(Coordinates<Integer, Integer> move : new ArrayList<>(mergedList)){
                            Pieces originalTarget = Board.game_board[move.getX()][move.getY()];
                            Coordinates<Integer, Integer> originalPosition = piece.second();
                            Board.game_board[move.getX()][move.getY()] = piece.first();
                            Board.game_board[originalPosition.getX()][originalPosition.getY()] = null;
                            piece.first().position = move;
                            int move_strength = minimax(depth + 1, true, bot_state, bot_turn);
                            if(move_strength < min_move_strength){
                                min_move_strength = move_strength;
                            }
                            Board.game_board[originalPosition.getX()][originalPosition.getY()] = piece.first();
                            Board.game_board[move.getX()][move.getY()] = originalTarget;
                            piece.first().position = originalPosition;
                        }
                    }
                }
                return (min_move_strength == Integer.MAX_VALUE) ? evaluateBoard(current_state, true) : min_move_strength;
            }
        }
        return 0;
    }
}