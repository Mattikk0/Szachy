package chess.shared.model;

import chess.client.game.ChessGame;
import chess.client.ui.board.Board;
import chess.shared.enums.PieceColor;
import chess.shared.piece.King;
import chess.shared.piece.Pieces;

import java.io.IOException;

public class Turn {
    public PieceColor player;
    public GameState whitePlayer =  GameState.getWhiteInstance();
    public GameState blackPlayer =  GameState.getBlackInstance();
    public Turn(){
        this.player = PieceColor.WHITE;
        Board.current = whitePlayer;
        GameState.is_bot_static.set(Board.current.is_bot);
    }
    public void changeTurn() throws IOException, InterruptedException {
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
            Board.game_over = true;
            clearTurn();
        } else if (ChessGame.checkIfStalemate(this.player)) {
            Board.game_over = true;
            clearTurn();
        }
        GameState.is_bot_static.set(Board.current.is_bot);
        Board.board_hash = ChessGame.hashBoardToNumber();
        Board.whole_board.getBoard(Board.game_board);
        Board.whole_board.saveToFile();
    }
    public void clearTurn(){
        this.player = null;
    }

}
