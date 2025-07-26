public class Turn {
    String player;
    GameState whitePlayer =  new GameState(Board.game_board, "white");
    GameState blackPlayer =  new GameState(Board.game_board, "black");
    public Turn(){
        this.player = "white";
        Board.current = whitePlayer;
    }
    void changeTurn(){
        if(this.player.equals("white")){
            this.player = "black";
            Board.current = blackPlayer;
        }else{
            this.player = "white";
            Board.current = whitePlayer;
        }
    }
}
