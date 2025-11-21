import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Turn {
    PieceColor player;
    GameState whitePlayer =  new GameState();
    GameState blackPlayer =  new GameState();
    public Turn(){
        this.player = PieceColor.WHITE;
        Board.current = whitePlayer;
    }
    void changeTurn() throws IOException, InterruptedException {
        if(this.player == PieceColor.WHITE){
            this.player = PieceColor.BLACK;
            Board.current = blackPlayer;
        }else{
            ChessGame.full_turns++;
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
        System.out.println(ChessGame.no_progress_moves);
        Board.whole_board.getBoard(Board.game_board);
        Board.whole_board.saveToFile();
    }


}
