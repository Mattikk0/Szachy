package chess.client.bot;

import javafx.scene.control.Label;
import chess.shared.enums.PieceColor;
import chess.shared.piece.Pieces;
import chess.shared.util.Coordinates;
import chess.shared.util.Pair;

public abstract class ChessBot{
    public int level;
    public ChessBot(int level){
        this.level = level;
    }
    public abstract Pair<Coordinates<Integer, Integer>, Coordinates<Integer, Integer>> setMove();
    public abstract Pieces getPromotionPiece(PieceColor color, Label label);

}
