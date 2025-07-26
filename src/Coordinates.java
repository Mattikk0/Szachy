public record Coordinates<X, Y>(X first, Y second) {
    public Coordinates(X first, Y second) {
        this.first = first;
        this.second = second;
    }
    public int getX(){
        return (int) this.first;
    }
    public int getY(){
        return (int) this.second;
    }
}
