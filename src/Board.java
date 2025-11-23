import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;


import java.io.IOException;
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
    private boolean loaded_game = false;

    public Board(ChessGame game) throws IOException, InterruptedException {
        this.game = game;
        drawBoard();
        Board.hashList.clear();
        Board.board_hash = ChessGame.hashBoardToNumber();
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
        turn.player = PieceColor.WHITE;
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



    void drawBoard() throws IOException, InterruptedException {
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
                    if(!loaded_game) {
                        game.loadGameFromFile("SavedGame.pgn");
                        loaded_game = true;
                    }
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
                            if(game.checkForPromotion(lastClickedPiece)) {
                                drawPromotionChoosingScreen((Pawn) lastClickedPiece, finalRow, finalCol);
                            }
                            try {
                                turn.changeTurn();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

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
                            if(game.checkForPromotion(lastClickedPiece)) {
                                drawPromotionChoosingScreen((Pawn) lastClickedPiece, finalRow, finalCol);
                            }
                            Board.refreshBoard();
                            try {
                                turn.changeTurn();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
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
                refreshBoard();
            }
        }
    }

    public static void refreshBoard() {
        if (Board.cells == null) return;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                if (cells[r][c] == null) continue;

                cells[r][c].getChildren().removeIf(node -> node instanceof Label);

                Pieces piece = game_board[r][c];
                if (piece != null) {
                    cells[r][c].getChildren().add(piece.label);
                }
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

    void drawPromotionChoosingScreen(Pawn pawn, int row, int col) {
        if (pawn == null) return;
        Window owner = this.getScene() != null ? this.getScene().getWindow() : null;

        Stage dialog = new Stage();
        if (owner != null) dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);

        HBox box = new HBox(12);
        box.setPadding(new Insets(12));
        box.setAlignment(Pos.CENTER);

        PieceColor color = pawn.color;
        String queenGlyph = color == PieceColor.WHITE ? "♕" : "♛";
        String rookGlyph  = color == PieceColor.WHITE ? "♖" : "♜";
        String bishopGlyph= color == PieceColor.WHITE ? "♗" : "♝";
        String knightGlyph= color == PieceColor.WHITE ? "♘" : "♞";

        Button bQueen = createPromoButton(queenGlyph, 48);
        Button bRook  = createPromoButton(rookGlyph, 48);
        Button bBishop= createPromoButton(bishopGlyph, 48);
        Button bKnight= createPromoButton(knightGlyph, 48);

        bQueen.setOnAction(e -> {
            game.promotion(new Queen(color, getCellLabel(row, col)), row, col, pawn);
            dialog.close();
        });
        bRook.setOnAction(e -> {
            game.promotion(new Rook(color, getCellLabel(row, col)), row, col, pawn);
            dialog.close();
        });
        bBishop.setOnAction(e -> {
            game.promotion(new Bishop(color, getCellLabel(row, col)), row, col, pawn);
            dialog.close();
        });
        bKnight.setOnAction(e -> {
            game.promotion(new Knight(color, getCellLabel(row, col)), row, col, pawn);
            dialog.close();
        });
        Scene scene = new Scene(box);
        scene.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.F4 && ke.isAltDown()) {
                game.promotion(new Queen(color, getCellLabel(row, col)), row, col, pawn);
                dialog.close();
                ke.consume();
            }
        });
        dialog.setOnCloseRequest(ev -> {
            game.promotion(new Queen(color, getCellLabel(row, col)), row, col, pawn);
        });


        box.getChildren().addAll(bQueen, bRook, bBishop, bKnight);
        dialog.setScene(scene);
        dialog.setTitle("Promocja pionka");
        dialog.setResizable(false);
        dialog.showAndWait();
    }
    private Button createPromoButton(String glyph, int fontSize) {
        Button b = new Button(glyph);
        b.setStyle("-fx-font-size: " + fontSize + "px; -fx-background-color: transparent;");
        b.setMinSize(64, 64);
        return b;
    }
    private Label getCellLabel(int row, int col) {
        StackPane cell = cells[row][col];
        for (javafx.scene.Node n : cell.getChildren()) {
            if (n instanceof Label) return (Label) n;
        }
        Label l = new Label();
        l.setStyle("-fx-font-size: 36px;");
        l.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        l.setAlignment(Pos.CENTER);
        GridPane.setHalignment(l, HPos.CENTER);
        GridPane.setValignment(l, VPos.CENTER);
        cell.getChildren().add(l);
        return l;
    }
}