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
        for(Pieces[] row : Board.game_board) {
            for (Pieces king : row) {
                if(king instanceof King){
                    ((King)king).setCheckStatus();
                    ((King) king).zone.clear();
                    ((King)king).updateZone();
                }
            }
        }
        if(ChessGame.checkIfCheckmate(this.player)){
            System.out.println("Checkmate! " + (this.player == PieceColor.WHITE ? "Black" : "White") + " wins!");
        } else if (ChessGame.checkIfStalemate(this.player)) {
            System.out.println("Stalemate!");
        }
        Board.board_hash = ChessGame.hashBoardToNumber();
        System.out.println("Current board hash: " + Board.board_hash);
    }
}
