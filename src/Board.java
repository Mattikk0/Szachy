import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class Board extends GridPane {
    private final ChessGame game;
    public static Pieces[][] game_board = new Pieces[8][8];
    static StackPane[][] cells = new StackPane[8][8];
    Pieces lastClickedPiece;
    private static final int TILE_SIZE = 100;
    private static final int ROWS = 8;
    private static final int COLS = 8;
    int tempRow, tempCol;
    Turn turn = new Turn();
    static GameState current = new GameState();

    public Board(ChessGame game) {
        this.game = game;
        drawBoard();
    }

    static void drawPieces(Label label, int row, int col) {
        if (row == 6) {
            Pawn white_pawn = new Pawn("white", label);
            game_board[6][col] = white_pawn;
        }
        if (row == 1) {
            Pawn black_pawn = new Pawn("black", label);
            game_board[1][col] = black_pawn;
        }
        if (row == 0) {
            switch (col) {
                case 0, 7:
                    Rook black_rook = new Rook("black", label);
                    game_board[0][col] = black_rook;
                    break;
                case 1, 6:
                    Knight black_knight = new Knight("black", label);
                    game_board[0][col] = black_knight;
                    break;
                case 2, 5:
                    Bishop black_bishop = new Bishop("black", label);
                    game_board[0][col] = black_bishop;
                    break;
                case 3:
                    Queen black_queen = new Queen("black", label);
                    game_board[0][col] = black_queen;
                    break;
                case 4:
                    King black_king = new King("black", label);
                    game_board[0][col] = black_king;
                    break;
            }
        }
        if (row == 7) {
            switch (col) {
                case 0, 7:
                    Rook white_rook = new Rook("white", label);
                    game_board[7][col] = white_rook;
                    break;
                case 1, 6:
                    Knight white_knight = new Knight("white", label);
                    game_board[7][col] = white_knight;
                    break;
                case 2, 5:
                    Bishop white_bishop = new Bishop("white", label);
                    game_board[7][col] = white_bishop;
                    break;
                case 3:
                    Queen white_queen = new Queen("white", label);
                    game_board[7][col] = white_queen;
                    break;
                case 4:
                    King white_king = new King("white", label);
                    game_board[7][col] = white_king;
                    break;
            }
        }
    }

    static void drawMoves() {
        removeDots();
        if (!Pieces.moveList.isEmpty()) {
            for (int i = 0; i < Pieces.moveList.size(); i++) {
                int x = Pieces.moveList.get(i).getX();
                int y = Pieces.moveList.get(i).getY();
                StackPane targetCell = cells[x][y];
                Label moveDot = new Label("•");
                moveDot.setTextFill(Color.GREEN);
                moveDot.setStyle("-fx-font-size: 64px;");
                moveDot.setAlignment(Pos.CENTER);
                moveDot.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                targetCell.getChildren().add(moveDot);
            }
        }
    }

    void drawBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Label label = new Label();
                label.setStyle("-fx-font-size: 36px;");
                label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                label.setAlignment(Pos.CENTER);
                GridPane.setHalignment(label, HPos.CENTER);
                GridPane.setValignment(label, VPos.CENTER);
                drawPieces(label, row, col);

                Rectangle square = new Rectangle(TILE_SIZE, TILE_SIZE);
                boolean isLight = (row + col) % 2 == 0;
                square.setFill(isLight ? Color.BEIGE : Color.SADDLEBROWN);
                square.setStroke(Color.BLACK);
                square.setStrokeWidth(0.5);

                int finalRow = row;
                int finalCol = col;

                StackPane cell = new StackPane();
                cells[row][col] = cell;

                cell.setOnMouseClicked((MouseEvent event) -> {
                    removeDots();

                    Pieces clickedPiece = game_board[finalRow][finalCol];

                    if (clickedPiece != null && clickedPiece.color.equals(turn.player)) {
                        lastClickedPiece = clickedPiece;
                        tempRow = finalRow;
                        tempCol = finalCol;
                        game.showLegalMoves(clickedPiece, finalRow, finalCol);
                        drawMoves();
                        drawTakes();
                        return;
                    }

                    for (Coordinates<Integer, Integer> coord : Pieces.moveList) {
                        if (coord.getX() == finalRow && coord.getY() == finalCol) {
                            game.move(finalRow, finalCol, lastClickedPiece, tempRow, tempCol, lastClickedPiece.color, current);
                            current.getState(game_board, turn.toString());
                            turn.changeTurn();
                            for (Pieces[] pieces : game_board) {
                                for (Pieces piece : pieces) {
                                    if (piece instanceof Pawn && Objects.equals(piece.color, turn.player)) {
                                        ((Pawn) piece).movedByTwo = false;
                                    }
                                }
                            }
                            return;
                        }
                    }

                    for (Coordinates<Integer, Integer> coord : Pieces.takesList) {
                        if (coord.getX() == finalRow && coord.getY() == finalCol) {
                            game.move(finalRow, finalCol, lastClickedPiece, tempRow, tempCol, lastClickedPiece.color, current);
                            current.getState(game_board, turn.toString());
                            turn.changeTurn();
                            for (Pieces[] pieces : game_board) {
                                for (Pieces piece : pieces) {
                                    if (piece instanceof Pawn && Objects.equals(piece.color, turn.player)) {
                                        ((Pawn) piece).movedByTwo = false;
                                    }
                                }
                            }
                            return;
                        }
                    }
                });
                cell.getChildren().addAll(square, label);
                this.add(cell, col, row);
            }
        }
    }


    private static void removeDots() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                StackPane cell = cells[row][col];
                cell.getChildren().removeIf(node ->
                        node instanceof Label && "•".equals(((Label) node).getText())
                );
            }
        }
    }

    static void drawTakes() {
        if (!Pieces.takesList.isEmpty()) {
            for (int i = 0; i < Pieces.takesList.size(); i++) {
                int x = Pieces.takesList.get(i).getX();
                int y = Pieces.takesList.get(i).getY();
                StackPane targetCell = cells[x][y];
                Label takeDot = new Label("•");
                takeDot.setTextFill(Color.RED);
                takeDot.setStyle("-fx-font-size: 64px;");
                takeDot.setAlignment(Pos.CENTER);
                takeDot.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                targetCell.getChildren().add(takeDot);
            }
        }
    }
}