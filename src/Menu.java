import java.io.IOException;

public abstract class Menu {
    public abstract void launchMenu() throws InterruptedException, IOException;

    public void launchMenu(String winner) throws IOException, InterruptedException {
        throw new UnsupportedOperationException();
    }
}
