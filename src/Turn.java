public class Turn {
    PieceColor player;
    GameState whitePlayer =  new GameState(Board.game_board, PieceColor.WHITE);
    GameState blackPlayer =  new GameState(Board.game_board, PieceColor.BLACK);
    public Turn(){
        this.player = PieceColor.WHITE;
        Board.current = whitePlayer;
    }
    void changeTurn(){
        if(this.player == PieceColor.WHITE){
            this.player = PieceColor.BLACK;
            Board.current = blackPlayer;
        }else{
            this.player = PieceColor.WHITE;
            Board.current = whitePlayer;
        }
    }
}
