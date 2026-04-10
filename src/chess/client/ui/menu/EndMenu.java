package chess.client.ui.menu;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class EndMenu extends Menu {
    public boolean restart = false;

    @Override
    public void launchMenu() {
        throw new UnsupportedOperationException("Wywołaj launchMenu(String winner)");
    }

    @Override
    public void launchMenu(String winner) throws InterruptedException, IOException, IOException {
        ProcessBuilder pb = new ProcessBuilder("python", "chess/client/ui/menu/EndMenu.py", winner);
        pb.directory(new File("src"));
        Process p = pb.start();
        BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = out.readLine();
        restart = Boolean.parseBoolean(line);

        p.waitFor();
        p.destroy();
    }




}
