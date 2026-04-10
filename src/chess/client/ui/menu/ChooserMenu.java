package chess.client.ui.menu;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChooserMenu extends Menu {
    public String chosenColor;
    public String opponent;
    public int botLevel;
    @Override
    public void launchMenu() throws InterruptedException, IOException {
        ProcessBuilder pb = new ProcessBuilder("python", "chess/client/ui/menu/ChooserMenu.py");
        pb.directory(new File("src"));
        Process p = pb.start();
        BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = out.readLine();
        if (line == null || line.trim().isEmpty()) {
            p.destroy();
            System.exit(0);
        }
        String[] parts = line.split(" ");
        chosenColor = parts[0];
        opponent = parts[1];
        if(parts[2].equals("none")){
            botLevel = -1;
        }else{
            botLevel = Integer.parseInt(parts[2]);
        }

        p.waitFor();
        p.destroy();
    }

}
