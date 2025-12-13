import javafx.scene.control.Label;

public abstract class ChessBot{
    int level;
    public ChessBot(int level){
        this.level = level;
    }
    public abstract Pair<Coordinates<Integer, Integer>, Coordinates<Integer, Integer>> setMove();
    public abstract Pieces getPromotionPiece(PieceColor color, Label label);

}
