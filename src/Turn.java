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
        System.out.println(Board.board_hash);
        Board.whole_board.getBoard(Board.game_board);
        Board.whole_board.saveToFile();
        saveAdditionalInfo();
    }


    void saveAdditionalInfo(){
        try (PrintWriter writer = new PrintWriter("AdditionalInfo.txt")) {
            writer.println(whitePlayer.material + " " + blackPlayer.material + " " + this.player + " " + ChessGame.no_progress_moves);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadAdditionalInfo(){
        try (Scanner scanner = new Scanner(new File("AdditionalInfo.txt"))) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                if (parts.length < 4) return;

                Board.current = parts[2].equals("WHITE") ? whitePlayer : blackPlayer;
                ChessGame.no_progress_moves = Integer.parseInt(parts[3]);
                this.player = parts[2].equals("WHITE") ? PieceColor.WHITE : PieceColor.BLACK;
                whitePlayer.material = Integer.parseInt(parts[0]);
                blackPlayer.material = Integer.parseInt(parts[1]);
            }
        } catch (FileNotFoundException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

}
