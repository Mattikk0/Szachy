import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class Board extends GridPane {
    private final ChessGame game;
    static long board_hash;
    static List<Long> hashList = new ArrayList<>();
    static long[][][] combinations_table;
    public static Pieces[][] game_board = new Pieces[8][8];
    static StackPane[][] cells = new StackPane[8][8];
    Pieces lastClickedPiece;
    private static final int TILE_SIZE = 100;
    private static final int ROWS = 8;
    private static final int COLS = 8;
    int tempRow, tempCol;
    static Turn turn = new Turn();
    static GameState current = new GameState();
    static GameState whole_board = new GameState();
    public Board(ChessGame game) {
        this.game = game;
        drawBoard();
        Board.board_hash = ChessGame.hashBoardToNumber();
        System.out.println(Board.board_hash);
    }

    static void drawPiecesNewGame(Label label, int row, int col) {
        if (row == 6) {
            Pawn white_pawn = new Pawn(PieceColor.WHITE, label);
            game_board[6][col] = white_pawn;
            white_pawn.position = new Coordinates<>(6, col);
        }
        if (row == 1) {
            Pawn black_pawn = new Pawn(PieceColor.BLACK, label);
            game_board[1][col] = black_pawn;
            black_pawn.position = new Coordinates<>(1, col);
        }
        if (row == 0) {
            switch (col) {
                case 0, 7:
                    Rook black_rook = new Rook(PieceColor.BLACK, label);
                    game_board[0][col] = black_rook;
                    black_rook.position = new Coordinates<>(0, col);
                    break;
                case 1, 6:
                    Knight black_knight = new Knight(PieceColor.BLACK, label);
                    game_board[0][col] = black_knight;
                    black_knight.position = new Coordinates<>(0, col);
                    break;
                case 2, 5:
                    Bishop black_bishop = new Bishop(PieceColor.BLACK, label);
                    game_board[0][col] = black_bishop;
                    black_bishop.position = new Coordinates<>(0, col);
                    break;
                case 3:
                    Queen black_queen = new Queen(PieceColor.BLACK, label);
                    game_board[0][col] = black_queen;
                    black_queen.position = new Coordinates<>(0, col);
                    break;
                case 4:
                    King black_king = new King(PieceColor.BLACK, label);
                    game_board[0][col] = black_king;
                    black_king.position = new Coordinates<>(0, col);
                    black_king.updateZone();
                    break;
            }
        }
        if (row == 7) {
            switch (col) {
                case 0, 7:
                    Rook white_rook = new Rook(PieceColor.WHITE, label);
                    game_board[7][col] = white_rook;
                    white_rook.position = new Coordinates<>(7, col);
                    break;
                case 1, 6:
                    Knight white_knight = new Knight(PieceColor.WHITE, label);
                    game_board[7][col] = white_knight;
                    white_knight.position = new Coordinates<>(7, col);
                    break;
                case 2, 5:
                    Bishop white_bishop = new Bishop(PieceColor.WHITE, label);
                    game_board[7][col] = white_bishop;
                    white_bishop.position = new Coordinates<>(7, col);
                    break;
                case 3:
                    Queen white_queen = new Queen(PieceColor.WHITE, label);
                    game_board[7][col] = white_queen;
                    white_queen.position = new Coordinates<>(7, col);
                    break;
                case 4:
                    King white_king = new King(PieceColor.WHITE, label);
                    game_board[7][col] = white_king;
                    white_king.position = new Coordinates<>(7, col);
                    white_king.updateZone();
                    break;
            }
        }
    }

    static void drawMoves(Pieces piece) {
        removeDots();
        if (!piece.moveList.isEmpty()) {
            for (int i = 0; i < piece.moveList.size(); i++) {
                int x = piece.moveList.get(i).getX();
                int y = piece.moveList.get(i).getY();
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
                if(this.game.new_game) {
                    drawPiecesNewGame(label, row, col);
                }else{
                    game.loadGame("SavedGame.txt");
                    turn.loadAdditionalInfo();
                }

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
                        drawMoves(lastClickedPiece);
                        drawTakes(lastClickedPiece);
                        if(lastClickedPiece instanceof Pawn){
                            drawEnPassant((Pawn) lastClickedPiece);
                        }
                        return;
                    }
                    if (lastClickedPiece == null) {
                        return;
                    }

                    for (Coordinates<Integer, Integer> coord : lastClickedPiece.moveList) {
                        if (coord.getX() == finalRow && coord.getY() == finalCol) {
                            game.move(finalRow, finalCol, lastClickedPiece, tempRow, tempCol, lastClickedPiece.color, current);
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

                    for (Coordinates<Integer, Integer> coord : lastClickedPiece.takesList) {
                        if (coord.getX() == finalRow && coord.getY() == finalCol) {
                            game.move(finalRow, finalCol, lastClickedPiece, tempRow, tempCol, lastClickedPiece.color, current);
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

    static void drawTakes(Pieces piece) {
        if (!piece.takesList.isEmpty()) {
            Iterator<Coordinates<Integer, Integer>> it = piece.takesList.iterator();
            while (it.hasNext()) {
                Coordinates<Integer, Integer> p = it.next();
                if (Board.game_board[p.getX()][p.getY()] == null || game_board[p.getX()][p.getY()].color.equals(piece.color)) {
                    it.remove();
                }
            }
            for (int i = 0; i < piece.takesList.size(); i++) {
                int x = piece.takesList.get(i).getX();
                int y = piece.takesList.get(i).getY();
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
    static void drawEnPassant(Pawn pawn) {
        if (pawn.did_ep) {
            for (Coordinates<Integer, Integer> coord : pawn.epList) {
                int x = coord.getX();
                int y = coord.getY();
                if (game_board[x][y] == null) {
                    StackPane targetCell = cells[x][y];
                    Label epDot = new Label("•");
                    epDot.setTextFill(Color.RED);
                    epDot.setStyle("-fx-font-size: 64px;");
                    epDot.setAlignment(Pos.CENTER);
                    epDot.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    targetCell.getChildren().add(epDot);
                }
                pawn.takesList.addAll(pawn.epList);
            }
        }
    }

}