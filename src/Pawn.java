import javafx.scene.control.Label;

public class Pawn extends Pieces{
    Pawn(String color, Label label){
        this.drawPiece(color, label);
        this.label = label;
        this.color = color;
        this.value = 1;
        this.movedByTwo = false;
        this.did_ep = false;
    }
    public boolean did_ep;
    public boolean movedByTwo;
    @Override
    public void legalMoves(int row, int col) {
        if(this.color.equals("white")){
            if(!isOutOfBoard(row-1, col)) {
                if (Board.game_board[row - 1][col] == null) {
                    moveList.add(new Coordinates<>(row - 1, col));
                    if (!isOutOfBoard(row-2, col) && Board.game_board[row - 2][col] == null && row == 6) {
                        moveList.add(new Coordinates<>(row - 2, col));
                    }
                }
            }
        }else{
            if(!isOutOfBoard(row+1, col)) {
                if (Board.game_board[row + 1][col] == null) {
                    moveList.add(new Coordinates<>(row + 1, col));
                    if (!isOutOfBoard(row+2, col) && Board.game_board[row + 2][col] == null && row == 1) {
                        moveList.add(new Coordinates<>(row + 2, col));
                    }
                }
            }
        }
    }

    @Override
    void legalTakes(int row, int col) {
        enPassant(row, col);
        if(this.color.equals("white") ){
            if(!isOutOfBoard(row-1, col+1) && Board.game_board[row-1][col+1]!=null && !(Board.game_board[row-1][col+1].color.equals(this.color)) && !(Board.game_board[row-1][col+1] instanceof King)) {
                takesList.add(new Coordinates<>(row-1, col+1));
            }
            if(!isOutOfBoard(row-1, col-1) && Board.game_board[row-1][col-1]!=null && !(Board.game_board[row-1][col-1].color.equals(this.color)) && !(Board.game_board[row-1][col-1] instanceof King)) {
                takesList.add(new Coordinates<>(row-1, col-1));
            }
        }else{
            if(!isOutOfBoard(row+1, col+1) && Board.game_board[row+1][col+1]!=null && !(Board.game_board[row+1][col+1].color.equals(this.color)) && !(Board.game_board[row+1][col+1] instanceof King)) {
                takesList.add(new Coordinates<>(row+1, col+1));
            }
            if(!isOutOfBoard(row+1, col-1) && Board.game_board[row+1][col-1]!=null && !(Board.game_board[row+1][col-1].color.equals(this.color)) && !(Board.game_board[row+1][col-1] instanceof King)) {
                takesList.add(new Coordinates<>(row+1, col-1));
            }
        }
    }

    @Override
    void drawPiece(String color, Label label) {
        if(color.equals("white")) {
            label.setText("♙");
        }else{
            label.setText("♟");
        }
    }
    void enPassant(int row, int col){
        if(!isOutOfBoard(row, col+1) && Board.game_board[row][col+1] != null && Board.game_board[row][col+1] instanceof Pawn && Board.game_board[row][col+1].color != this.color && ((Pawn) Board.game_board[row][col+1]).movedByTwo == true){
            if(this.color.equals("white")){
                takesList.add(new Coordinates<>(row-1, col+1));
                this.did_ep = true;
            }else{
                takesList.add(new Coordinates<>(row+1, col+1));
                this.did_ep = true;
            }
        }
        if(!isOutOfBoard(row, col-1) && Board.game_board[row][col-1] != null && Board.game_board[row][col-1] instanceof Pawn && Board.game_board[row][col-1].color != this.color && ((Pawn) Board.game_board[row][col-1]).movedByTwo == true){
            if(this.color.equals("white")){
                takesList.add(new Coordinates<>(row-1, col-1));
                this.did_ep = true;
            }else{
                takesList.add(new Coordinates<>(row+1, col-1));
                this.did_ep = true;
            }
        }
    }

}
