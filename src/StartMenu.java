import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class StartMenu {
    public boolean new_game_selected;
    void launchMenu() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("python", "StartMenu.py");
        pb.directory(new File("src"));
        Process p = pb.start();
        BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = out.readLine();
        new_game_selected = Boolean.parseBoolean(line);

        p.waitFor();
        p.destroy();
    }

}
