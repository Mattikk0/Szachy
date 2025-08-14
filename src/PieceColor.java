public enum PieceColor {
    WHITE,
    BLACK;
    public PieceColor oppositeColor(){
        return this == WHITE ? BLACK : WHITE;
    }
}
