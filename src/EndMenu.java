import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class EndMenu {
    public boolean restart;
    void launchMenu(String winner) throws InterruptedException, IOException {
        ProcessBuilder pb = new ProcessBuilder("python", "EndMenu.py", winner);
        pb.directory(new File("src"));
        Process p = pb.start();
        BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = out.readLine();
        restart = Boolean.parseBoolean(line);

        p.waitFor();
        p.destroy();
    }
}
