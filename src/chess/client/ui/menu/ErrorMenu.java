package chess.client.ui.menu;

import chess.client.game.ChessGame;
import chess.client.ui.board.Board;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ErrorMenu extends Menu {
    public boolean can_load;
    public boolean load_finished_game;
    @Override
    public void launchMenu() throws InterruptedException, IOException {}

    @Override
    public void launchMenu(String error) throws InterruptedException, IOException {
        ProcessBuilder pb = new ProcessBuilder("python", "chess/client/ui/menu/ErrorMenu.py", error);
        pb.directory(new File("src"));
        Process p = pb.start();
        if(error.equals("game_over")){
            BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = out.readLine();
            load_finished_game = Boolean.parseBoolean(line);
        }
        p.waitFor();
        p.destroy();
    }

    public void launchStartMenu() throws InterruptedException, IOException {
        Board.combinations_table = ChessGame.allCombinations();
        ChessGame game = new ChessGame();
        StartMenu startMenu = new StartMenu();
        startMenu.launchMenu();
        if(startMenu.new_game_selected){
            game.new_game = true;
        }else{
            game.new_game = false;
        }
        Board board = new Board(game);
    }

}
