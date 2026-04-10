package chess.client.ui.board;

import chess.client.bot.ChessBot;
import chess.client.game.ChessGame;
import chess.client.ui.menu.ChooserMenu;
import chess.shared.piece.*;
import javafx.application.Platform;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import chess.shared.enums.PieceColor;
import chess.shared.model.GameState;
import chess.shared.model.Turn;
import chess.shared.piece.*;
import chess.shared.util.Coordinates;
import chess.shared.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Board extends GridPane {
    private final ChessGame game;
    public static long board_hash;
    public static List<Long> hashList = new ArrayList<>();
    public static long[][][] combinations_table;
    public static Pieces[][] game_board = new Pieces[8][8];
    public static StackPane[][] cells = new StackPane[8][8];
    Pieces lastClickedPiece;
    private static final int TILE_SIZE = 100;
    private static final int ROWS = 8;
    private static final int COLS = 8;
    int tempRow, tempCol;
    public static Turn turn = new Turn();
    public static GameState current = new GameState();
    public static GameState whole_board = new GameState();
    private boolean loaded_game = false;
    public static boolean game_over = false;
    public boolean plays_white;
    public static boolean opponent_is_bot;
    public static ChessBot opponent_bot;
    public static PieceColor player_on_bottom;

    public Board(ChessGame game) throws IOException, InterruptedException {
        this.game = game;
        drawBoard();
        Board.hashList.clear();
        Board.board_hash = ChessGame.hashBoardToNumber();
    }

    static void drawPiecesNewGame(Label label, int row, int col, boolean white) {
        int black_pawn_row;
        int black_piece_row;
        int white_pawn_row;
        int white_piece_row;
        if(white){
            player_on_bottom = PieceColor.WHITE;
            black_pawn_row = 1;
            black_piece_row = 0;
            white_pawn_row = 6;
            white_piece_row = 7;
        }else{
            player_on_bottom = PieceColor.BLACK;
            black_pawn_row = 6;
            black_piece_row = 7;
            white_pawn_row = 1;
            white_piece_row = 0;
        }
        if (row == white_pawn_row) {
            Pawn white_pawn = new Pawn(PieceColor.WHITE, label);
            game_board[white_pawn_row][col] = white_pawn;
            white_pawn.position = new Coordinates<>(white_pawn_row, col);
        }
        if (row == black_pawn_row) {
            Pawn black_pawn = new Pawn(PieceColor.BLACK, label);
            game_board[black_pawn_row][col] = black_pawn;
            black_pawn.position = new Coordinates<>(black_pawn_row, col);
        }
        if (row == black_piece_row) {
            switch (col) {
                case 0, 7:
                    Rook black_rook = new Rook(PieceColor.BLACK, label);
                    game_board[black_piece_row][col] = black_rook;
                    black_rook.position = new Coordinates<>(black_piece_row, col);
                    break;
                case 1, 6:
                    Knight black_knight = new Knight(PieceColor.BLACK, label);
                    game_board[black_piece_row][col] = black_knight;
                    black_knight.position = new Coordinates<>(black_piece_row, col);
                    break;
                case 2, 5:
                    Bishop black_bishop = new Bishop(PieceColor.BLACK, label);
                    game_board[black_piece_row][col] = black_bishop;
                    black_bishop.position = new Coordinates<>(black_piece_row, col);
                    break;
                case 3:
                    if(black_piece_row==0){
                        Queen black_queen = new Queen(PieceColor.BLACK, label);
                        game_board[black_piece_row][col] = black_queen;
                        black_queen.position = new Coordinates<>(black_piece_row , col);
                    }else{
                        King black_king = new King(PieceColor.BLACK, label);
                        game_board[black_piece_row][col] = black_king;
                        black_king.position = new Coordinates<>(black_piece_row, col);
                        black_king.updateZone();
                    }
                    break;
                case 4:
                    if(black_piece_row==0) {
                        King black_king = new King(PieceColor.BLACK, label);
                        game_board[black_piece_row][col] = black_king;
                        black_king.position = new Coordinates<>(black_piece_row, col);
                        black_king.updateZone();
                    }else{
                        Queen black_queen = new Queen(PieceColor.BLACK, label);
                        game_board[black_piece_row][col] = black_queen;
                        black_queen.position = new Coordinates<>(black_piece_row , col);
                    }
                    break;
            }
        }
        if (row == white_piece_row) {
            switch (col) {
                case 0, 7:
                    Rook white_rook = new Rook(PieceColor.WHITE, label);
                    game_board[white_piece_row][col] = white_rook;
                    white_rook.position = new Coordinates<>(white_piece_row, col);
                    break;
                case 1, 6:
                    Knight white_knight = new Knight(PieceColor.WHITE, label);
                    game_board[white_piece_row][col] = white_knight;
                    white_knight.position = new Coordinates<>(white_piece_row, col);
                    break;
                case 2, 5:
                    Bishop white_bishop = new Bishop(PieceColor.WHITE, label);
                    game_board[white_piece_row][col] = white_bishop;
                    white_bishop.position = new Coordinates<>(white_piece_row, col);
                    break;
                case 3:
                    if(white_piece_row == 7){
                        Queen white_queen = new Queen(PieceColor.WHITE, label);
                        game_board[white_piece_row][col] = white_queen;
                        white_queen.position = new Coordinates<>(white_piece_row, col);
                    }else{
                        King white_king = new King(PieceColor.WHITE, label);
                        game_board[white_piece_row][col] = white_king;
                        white_king.position = new Coordinates<>(white_piece_row, col);
                        white_king.updateZone();
                    }
                    break;
                case 4:
                    if(white_piece_row == 7) {
                        King white_king = new King(PieceColor.WHITE, label);
                        game_board[white_piece_row][col] = white_king;
                        white_king.position = new Coordinates<>(white_piece_row, col);
                        white_king.updateZone();
                    }else{
                        Queen white_queen = new Queen(PieceColor.WHITE, label);
                        game_board[white_piece_row][col] = white_queen;
                        white_queen.position = new Coordinates<>(white_piece_row, col);
                    }
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
        if(this.game.new_game) {
            ChooserMenu cm = new ChooserMenu();
            cm.launchMenu();
            plays_white = cm.chosenColor.equals("white");
            opponent_is_bot = cm.opponent.equals("bot");
            if (opponent_is_bot) {
                opponent_bot = game.getBot(cm.botLevel);
            }
            if (plays_white && opponent_is_bot) {
                turn.blackPlayer.is_bot = true;
            } else if (!plays_white && opponent_is_bot) {
                GameState.is_bot_static.set(true);
                turn.whitePlayer.is_bot = true;
            }
            turn.player = PieceColor.WHITE;
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Label label = new Label();
                label.setStyle("-fx-font-size: 36px;");
                label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                label.setAlignment(Pos.CENTER);
                GridPane.setHalignment(label, HPos.CENTER);
                GridPane.setValignment(label, VPos.CENTER);

                if (this.game.new_game) {
                    drawPiecesNewGame(label, row, col, plays_white);
                } else {
                    if (!loaded_game) {
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
                cells[finalRow][finalCol] = cell;
                if(!current.is_bot) {
                    cell.setOnMouseClicked((MouseEvent event) -> {
                        removeDots();
                        Pieces clickedPiece = game_board[finalRow][finalCol];
                        if (current.is_bot) {
                            return;
                        }
                        if (clickedPiece != null && clickedPiece.color.equals(turn.player)) {
                            lastClickedPiece = clickedPiece;
                            tempRow = finalRow;
                            tempCol = finalCol;
                            game.showLegalMoves(clickedPiece, finalRow, finalCol);
                            drawMoves(lastClickedPiece);
                            drawTakes(lastClickedPiece);
                            if (lastClickedPiece instanceof Pawn) {
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
                                if (game.checkForPromotion(lastClickedPiece)) {
                                    drawPromotionChoosingScreen((Pawn) lastClickedPiece, finalRow, finalCol);
                                }
                                try {
                                    turn.changeTurn();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                boolean isCurrentPlayerBot = (turn.player == PieceColor.WHITE && turn.whitePlayer.is_bot) ||
                                        (turn.player == PieceColor.BLACK && turn.blackPlayer.is_bot);
                                if (isCurrentPlayerBot && !game_over && opponent_is_bot) {
                                    Platform.runLater(() -> {
                                        Pair<Coordinates<Integer, Integer>, Coordinates<Integer, Integer>> move = opponent_bot.setMove();
                                        if (move != null) {
                                            int pieceRow = move.first().getX();
                                            int pieceCol = move.first().getY();
                                            int moveRow = move.second().getX();
                                            int moveCol = move.second().getY();
                                            game.move(moveRow, moveCol, game_board[pieceRow][pieceCol], pieceRow, pieceCol, game_board[pieceRow][pieceCol].color, current);
                                            if (game.checkForPromotion(Board.game_board[moveRow][moveCol])) {
                                                game.promotion(opponent_bot.getPromotionPiece(turn.player, getCellLabel(moveRow, moveCol)), moveRow, moveCol, (Pawn)Board.game_board[moveRow][moveCol]);
                                            }
                                            try {
                                                turn.changeTurn();
                                            } catch (IOException | InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                                return;
                            }
                        }

                        for (Coordinates<Integer, Integer> coord : lastClickedPiece.takesList) {
                            if (coord.getX() == finalRow && coord.getY() == finalCol) {
                                game.move(finalRow, finalCol, lastClickedPiece, tempRow, tempCol, lastClickedPiece.color, current);
                                if (game.checkForPromotion(lastClickedPiece)) {
                                    drawPromotionChoosingScreen((Pawn) lastClickedPiece, finalRow, finalCol);
                                }
                                try {
                                    turn.changeTurn();
                                    refreshBoard();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                // Sprawdź czy bot ma turę po ruchu człowieka
                                boolean isCurrentPlayerBot = (turn.player == PieceColor.WHITE && turn.whitePlayer.is_bot) ||
                                        (turn.player == PieceColor.BLACK && turn.blackPlayer.is_bot);
                                if (isCurrentPlayerBot && !game_over && opponent_is_bot) {
                                    Platform.runLater(() -> {
                                        Pair<Coordinates<Integer, Integer>, Coordinates<Integer, Integer>> move = opponent_bot.setMove();
                                        if (move != null) {
                                            int pieceRow = move.first().getX();
                                            int pieceCol = move.first().getY();
                                            int moveRow = move.second().getX();
                                            int moveCol = move.second().getY();
                                            game.move(moveRow, moveCol, game_board[pieceRow][pieceCol], pieceRow, pieceCol, game_board[pieceRow][pieceCol].color, current);
                                            if (game.checkForPromotion(Board.game_board[moveRow][moveCol])) {
                                                game.promotion(opponent_bot.getPromotionPiece(turn.player, getCellLabel(moveRow, moveCol)), moveRow, moveCol, (Pawn)Board.game_board[moveRow][moveCol]);
                                            }
                                            try {
                                                turn.changeTurn();
                                            } catch (IOException | InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                                return;
                            }
                        }
                    });
                }

                cell.getChildren().addAll(square, label);
                this.add(cell, col, row);
                refreshBoard();
            }
        }

        boolean isWhiteBot = turn.whitePlayer.is_bot;
        if (GameState.is_bot_static.get() && !game_over && opponent_is_bot && isWhiteBot) {
            Pair<Coordinates<Integer, Integer>, Coordinates<Integer, Integer>> initialMove = opponent_bot.setMove();
            if (initialMove != null) {
                int pieceRow = initialMove.first().getX();
                int pieceCol = initialMove.first().getY();
                int moveRow = initialMove.second().getX();
                int moveCol = initialMove.second().getY();
                game.move(moveRow, moveCol, game_board[pieceRow][pieceCol], pieceRow, pieceCol, game_board[pieceRow][pieceCol].color, current);
                if (game.checkForPromotion(Board.game_board[moveRow][moveCol])) {
                    game.promotion(opponent_bot.getPromotionPiece(turn.player, getCellLabel(moveRow, moveCol)), moveRow, moveCol, (Pawn)Board.game_board[moveRow][moveCol]);
                }
                try {
                    turn.changeTurn();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
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
