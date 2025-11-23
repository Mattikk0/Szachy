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
            clearTurn();
        } else if (ChessGame.checkIfStalemate(this.player)) {
            clearTurn();
        }
        Board.board_hash = ChessGame.hashBoardToNumber();
        Board.whole_board.getBoard(Board.game_board);
        Board.whole_board.saveToFile();
    }
    public void clearTurn(){
        this.player = null;
    }

}
