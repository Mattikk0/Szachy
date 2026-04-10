package chess.shared.util;

public record Pair<X, Y>(X first, Y second) {
    public Pair(X first, Y second) {
        this.first = first;
        this.second = second;
    }
}