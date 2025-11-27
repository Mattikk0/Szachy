public abstract class ChessBot{
    private int level;
    public ChessBot(int level){
        this.level = level;
    }
    public abstract Pair<Coordinates<Integer, Integer>, Coordinates<Integer, Integer>> setMove();

}
