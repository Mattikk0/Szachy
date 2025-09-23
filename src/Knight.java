 import javafx.scene.control.Label;

 import java.util.List;

 public class Knight extends Pieces{
    public Knight(PieceColor color, Label label) {
        this.drawPiece(color, label);
        this.label = label;
        this.color = color;
        this.value = 3;
    }

    void moveUpRight(int row, int col){
        if (!isOutOfBoard(row - 2, col + 1) && Board.game_board[row - 2][col + 1] == null) {
            this.moveList.add(new Coordinates<>(row - 2, col + 1));
        }
    }
    void moveUpLeft(int row, int col){
        if (!isOutOfBoard(row - 2, col - 1) && Board.game_board[row - 2][col - 1] == null) {
            this.moveList.add(new Coordinates<>(row - 2, col - 1));
        }
    }
    void moveDownLeft(int row, int col){
        if (!isOutOfBoard(row + 2, col - 1) && Board.game_board[row + 2][col - 1] == null) {
            this.moveList.add(new Coordinates<>(row + 2, col - 1));
        }
    }
    void moveDownRight(int row, int col){
        if (!isOutOfBoard(row + 2, col + 1) && Board.game_board[row + 2][col + 1] == null) {
            this.moveList.add(new Coordinates<>(row + 2, col + 1));
        }
    }
    void moveRightUp(int row, int col){
        if (!isOutOfBoard(row - 1, col + 2) && Board.game_board[row - 1][col + 2] == null) {
            this.moveList.add(new Coordinates<>(row - 1, col + 2));
        }
    }
    void moveRightDown(int row, int col){
        if (!isOutOfBoard(row + 1, col + 2) && Board.game_board[row + 1][col + 2] == null) {
            this.moveList.add(new Coordinates<>(row + 1, col + 2));
        }
    }
    void moveLeftDown(int row, int col){
        if (!isOutOfBoard(row + 1, col - 2) && Board.game_board[row + 1][col - 2] == null) {
            this.moveList.add(new Coordinates<>(row + 1, col - 2));
        }
    }
    void moveLeftUp(int row, int col){
        if (!isOutOfBoard(row - 1, col - 2) && Board.game_board[row - 1][col - 2] == null) {
            this.moveList.add(new Coordinates<>(row - 1, col - 2));

        }
    }
    void takeLeftUp(int row, int col){
        if (!isOutOfBoard(row -1, col - 2)) {
            if (Board.game_board[row - 1][col - 2] != null && !(Board.game_board[row - 1][col - 2].color.equals(this.color)) && !(Board.game_board[row - 1][col - 2] instanceof King)) {
                this.takesList.add(new Coordinates<>(row - 1, col - 2));
            }
        }
    }
    void takeRightUp(int row, int col){
        if (!isOutOfBoard(row -1, col + 2)) {
            if (Board.game_board[row-1][col + 2] != null && !(Board.game_board[row-1][col + 2].color.equals(this.color)) && !(Board.game_board[row-1][col + 2] instanceof King)) {
                this.takesList.add(new Coordinates<>(row-1, col + 2));
            }
        }
    }
    void takeRightDown(int row, int col){
        if (!isOutOfBoard(row +1, col + 2)) {
            if (Board.game_board[row+1][col + 2] != null && !(Board.game_board[row+1][col + 2].color.equals(this.color)) && !(Board.game_board[row+1][col + 2] instanceof King)) {
                this.takesList.add(new Coordinates<>(row+1, col + 2));
            }
        }
    }
    void takeLeftDown(int row, int col){
        if (!isOutOfBoard(row +1, col - 2)) {
            if (Board.game_board[row+1][col - 2] != null && !(Board.game_board[row+1][col - 2].color.equals(this.color)) && !(Board.game_board[row+1][col - 2] instanceof King)) {
                this.takesList.add(new Coordinates<>(row+1, col - 2));
            }
        }
    }
    void takeDownLeft(int row, int col){
        if (!isOutOfBoard(row +2, col - 1)) {
            if (Board.game_board[row+2][col - 1] != null && !(Board.game_board[row+2][col - 1].color.equals(this.color)) && !(Board.game_board[row+2][col - 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row+2, col - 1));
            }
        }
    }
    void takeUpLeft(int row, int col){
        if (!isOutOfBoard(row -2, col - 1)) {
            if (Board.game_board[row-2][col - 1] != null && !(Board.game_board[row-2][col - 1].color.equals(this.color)) && !(Board.game_board[row-2][col - 1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row-2, col - 1));
            }
        }
    }
    void takeDownRight(int row, int col){
        if (!isOutOfBoard(row +2, col +1)) {
            if (Board.game_board[row+2][col +1] != null && !(Board.game_board[row+2][col+1].color.equals(this.color)) && !(Board.game_board[row+2][col+1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row+2, col+1));
            }
        }
    }
    void takeUpRight(int row, int col){
        if (!isOutOfBoard(row -2, col +1)) {
            if (Board.game_board[row-2][col+1] != null && !(Board.game_board[row-2][col+1].color.equals(this.color)) && !(Board.game_board[row-2][col+1] instanceof King)) {
                this.takesList.add(new Coordinates<>(row-2, col+1));
            }
        }
    }

    @Override
    void legalMoves(int row, int col) {
        moveLeftUp(row, col);
        moveLeftDown(row, col);
        moveRightUp(row, col);
        moveRightDown(row, col);
        moveUpLeft(row, col);
        moveUpRight(row, col);
        moveDownLeft(row, col);
        moveDownRight(row, col);
        filterMovesLeadingToCheck(this.moveList);
    }

    @Override
    void legalTakes(int row, int col) {
        takeDownRight(row, col);
        takeDownLeft(row, col);
        takeLeftDown(row, col);
        takeRightDown(row, col);
        takeLeftUp(row, col);
        takeRightUp(row, col);
        takeUpRight(row, col);
        takeUpLeft(row, col);
        filterMovesLeadingToCheck(this.takesList);
    }

    @Override
    void drawPiece(PieceColor color, Label label) {
        if(color == PieceColor.WHITE) {
            label.setText("♘");
        }else{
            label.setText("♞");
        }
    }

     @Override
     boolean isChecking() {
        Coordinates king_position = findFigure(King.class, this.color.oppositeColor());
        return (king_position != null) && (
                (Math.abs(king_position.getX() - this.position.getX()) == 2 && Math.abs(king_position.getY() - this.position.getY()) == 1) ||
                        (Math.abs(king_position.getX() - this.position.getX()) == 1 && Math.abs(king_position.getY() - this.position.getY()) == 2)
        );
     }
 }
