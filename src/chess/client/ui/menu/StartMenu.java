package chess.client.ui.menu;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class StartMenu extends Menu {
    public boolean new_game_selected;

    @Override
    public void launchMenu() throws InterruptedException, IOException {
        ProcessBuilder pb = new ProcessBuilder("python", "chess/client/ui/menu/StartMenu.py");
        pb.directory(new File("src"));
        Process p = pb.start();
        BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = out.readLine();
        if (line == null || line.trim().isEmpty()) {
            p.destroy();
            System.exit(0);
        }
        new_game_selected = Boolean.parseBoolean(line);
        p.waitFor();
        p.destroy();
    }
}
