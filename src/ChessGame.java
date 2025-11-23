import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static java.lang.System.exit;

public class ChessGame {
    boolean new_game;
    static String winner;
    public static int no_progress_moves = 0;
    public static int full_turns = 0;
    void showLegalMoves(Pieces piece, int row, int col) {
        piece.moveList.clear();
        showLegalTakes(piece, row, col);
        piece.legalMoves(row, col);
    }

    void showLegalTakes(Pieces playerPiece, int row, int col) {
        if (playerPiece instanceof Pawn) {
            ((Pawn) playerPiece).epList.clear();
        }
        playerPiece.takesList.clear();
        playerPiece.legalTakes(row, col);
    }

    void move(int row, int col, Pieces piece, int prev_row, int prev_col, PieceColor color, GameState player) {
        ChessGame.no_progress_moves++;
        Pieces target = Board.game_board[row][col];

        if (target != null && !target.color.equals(piece.color)) {
            ChessGame.no_progress_moves = 0;
            player.material += target.value;
            Board.cells[row][col].getChildren().remove(target.label);
        }
        if (target == null && piece instanceof Pawn && ((Pawn) piece).did_ep) {
            ChessGame.no_progress_moves = 0;
            player.material += 1;
            if (piece.color == PieceColor.WHITE) {
                Board.cells[row + 1][col].getChildren().remove(Board.game_board[row + 1][col].label);
                Board.game_board[row + 1][col] = null;
            } else {
                Board.cells[row - 1][col].getChildren().remove(Board.game_board[row - 1][col].label);
                Board.game_board[row - 1][col] = null;
            }
            ((Pawn) piece).did_ep = false;
        }
        if(target == null && piece instanceof Pawn && !((Pawn) piece).did_ep){
            ChessGame.no_progress_moves = 0;
        }
        if (piece instanceof Pawn && Math.abs(prev_row - row) == 2) {
            ChessGame.no_progress_moves = 0;
            ((Pawn) piece).movedByTwo = true;
        }
        if (piece instanceof Rook) {
            ((Rook) piece).moved = true;
        }
        if (piece instanceof King) {
            ((King) piece).moved = true;
            if (prev_col - col == 2) {
                if (piece.color == PieceColor.WHITE) {
                    Board.game_board[7][0].setPosition(7, 3);
                } else {
                    Board.game_board[0][0].setPosition(0, 3);
                }
            }
            if (prev_col - col == -2) {
                if (piece.color == PieceColor.WHITE) {
                    Board.game_board[7][7].setPosition(7, 5);
                } else {
                    Board.game_board[0][7].setPosition(0, 5);
                }
            }
        }
        Board.game_board[row][col] = piece;
        Board.game_board[prev_row][prev_col] = null;
        Board.cells[prev_row][prev_col].getChildren().remove(piece.label);
        Board.cells[row][col].getChildren().add(piece.label);
        piece.drawPiece(color, piece.label);
        piece.position = null;
        piece.position = new Coordinates<>(row, col);
        piece.moveList.clear();
        piece.takesList.clear();
    }

    static boolean checkIfGameOver(PieceColor king_color) {
        for (Pieces[] row : Board.game_board) {
            for (Pieces piece : row) {
                if (piece != null && piece.color == king_color) {
                    piece.legalMoves(piece.position.getX(), piece.position.getY());
                    piece.legalTakes(piece.position.getX(), piece.position.getY());
                    Iterator<Coordinates<Integer, Integer>> it = piece.takesList.iterator();
                    while (it.hasNext()) {
                        Coordinates<Integer, Integer> p = it.next();
                        if (Board.game_board[p.getX()][p.getY()] == null || Board.game_board[p.getX()][p.getY()].color.equals(piece.color)) {
                            it.remove();
                        }
                    }
                    if (!piece.moveList.isEmpty() || !piece.takesList.isEmpty()) {
                        piece.moveList.clear();
                        piece.takesList.clear();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    static boolean checkIfCheckmate(PieceColor king_color) throws IOException, InterruptedException {
        King king = (King) Board.game_board[Pieces.findFigure(King.class, king_color).getX()][Pieces.findFigure(King.class, king_color).getY()];
        if (king.isChecked && checkIfGameOver(king_color)) {
            winner = king_color.oppositeColor().toString();
            java.util.concurrent.Callable<Void> task = () -> {
                try {
                    //Thread.sleep(1000);
                    EndMenu endMenu = new EndMenu();
                    endMenu.launchMenu(winner);
                    if (endMenu.restart) {
                        endMenu.restartGame();
                    } else {
                        exit(0);
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
                return null;
            };
            java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
            executor.submit(task);
            executor.shutdown();
            return true;
        }
        return false;
    }

    static boolean checkIfStalemate(PieceColor king_color) throws IOException, InterruptedException {
        King king = (King) Board.game_board[Pieces.findFigure(King.class, king_color).getX()][Pieces.findFigure(King.class, king_color).getY()];
        if (!king.isChecked && (checkIfGameOver(king_color) || noProgressStalemate() || repetitionStalemate() || insufficientMaterialStalemate())) {
            winner = "null";
            java.util.concurrent.Callable<Void> task = () -> {
                try {
                    //Thread.sleep(1000);
                    EndMenu endMenu = new EndMenu();
                    endMenu.launchMenu(winner);
                    if (endMenu.restart) {
                        endMenu.restartGame();
                    } else {
                        exit(0);
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
                return null;
            };
            java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
            executor.submit(task);
            executor.shutdown();
            return true;
        }
        return false;
    }

    static boolean noProgressStalemate(){
        return no_progress_moves >= 50;
    }

    static boolean repetitionStalemate(){
        for(Long hash : Board.hashList){
            if(Board.hashList.stream().filter(h -> h.equals(hash)).count() >= 3){
                return true;
            }
        }
        return false;
    }

    static boolean insufficientMaterialStalemate(){
        int piecesCount = 0;
        int bishopsWhite = 0;
        int bishopsBlack = 0;
        int knightsWhite = 0;
        int knightsBlack = 0;
        for (Pieces[] row : Board.game_board) {
            for (Pieces piece : row) {
                if (piece != null) {
                    piecesCount++;
                    if(piece instanceof Bishop){
                        if(piece.color == PieceColor.WHITE){
                            bishopsWhite++;
                        }else{
                            bishopsBlack++;
                        }
                    }
                    if(piece instanceof Knight){
                        if(piece.color == PieceColor.WHITE){
                            knightsWhite++;
                        }else{
                            knightsBlack++;
                        }
                    }
                }
            }
        }
        return piecesCount == 2 || (piecesCount == 3 && (bishopsWhite == 1 || bishopsBlack == 1)) || (piecesCount == 4 && bishopsWhite == 1 && bishopsBlack == 1) || (piecesCount == 3 && (knightsWhite == 1 || knightsBlack == 1)) || (piecesCount == 4 && knightsWhite == 1 && knightsBlack == 1) || (piecesCount == 4 && ((knightsWhite == 1 && bishopsWhite == 1) || (knightsBlack == 1 && bishopsBlack == 1)));
    }

    static long[][][] allCombinations() {
        long[][][] table = new long[64][6][2];
        Random r = new Random();
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 6; j++) {
                for (int k = 0; k < 2; k++) {
                    table[i][j][k] = r.nextLong();
                }
            }
        }
        return table;
    }

    static int pieceHashValue(Pieces piece) {
        switch (piece.getClass().getSimpleName()) {
            case "Pawn" -> {
                return 0;
            }
            case "Knight" -> {
                return 1;
            }
            case "Bishop" -> {
                return 2;
            }
            case "Rook" -> {
                return 3;
            }
            case "Queen" -> {
                return 4;
            }
            case "King" -> {
                return 5;
            }
        }
        return -1;
    }

    static long hashBoardToNumber() {
        int pieceValue = 0;
        int squareIndex = 0;
        long hash = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (Board.game_board[i][j] != null) {
                    pieceValue = pieceHashValue(Board.game_board[i][j]);
                    squareIndex = i * 8 + j;
                    if (Board.game_board[i][j].color == PieceColor.WHITE) {
                        hash += Board.combinations_table[squareIndex][pieceValue][0];
                    } else {
                        hash += Board.combinations_table[squareIndex][pieceValue][1];
                    }
                }
            }
        }
        updateHashList(hash);
        return hash;
    }


    void loadGameFromFile(String file_name) throws IOException, InterruptedException {
        ErrorMenu errorMenu = new ErrorMenu();
        File file = new File(file_name);
        errorMenu.can_load = file.exists();
        if(!errorMenu.can_load || file.length() == 0){
            errorMenu.launchMenu("no_file");
            errorMenu.launchStartMenu();
        }else {

            try {
                Scanner scanner = new Scanner(new File(file_name));
                String fenString = scanner.nextLine().trim();
                scanner.close();
                String[] fenParts = fenString.split(" ");
                if (fenParts.length < 6) {
                    throw new IOException("Invalid FEN string: insufficient parts");
                }

                setTurn(fenParts[1]);
                if(Board.turn.player == null){
                    errorMenu.launchMenu("game_over");
                    if(!errorMenu.load_finished_game){
                        errorMenu.launchStartMenu();
                        return;
                    }
                }
                parseBoardFromFEN(fenParts[0]);
                setCastlingRights(fenParts[2]);
                setEnPassantSquare(fenParts[3], Board.turn.player);
                try {
                    ChessGame.no_progress_moves = Integer.parseInt(fenParts[4]);
                    ChessGame.full_turns = Integer.parseInt(fenParts[5]);
                } catch (NumberFormatException e) {
                    throw new IOException("Invalid numeric part in FEN string: " + e.getMessage());
                }
            } catch (IOException e) {
                System.err.println("Error loading game: " + e.getMessage());
            }
        }
    }

    private void parseBoardFromFEN(String fenPart0) {
        String[] ranks = fenPart0.split("/");
        for (int r = 0; r < 8 && r < ranks.length; r++) {
            String rank = ranks[r];
            int col = 0;
            int row = r;

            for (int i = 0; i < rank.length(); i++) {
                char c = rank.charAt(i);
                if (Character.isDigit(c)) {
                    int count = Character.getNumericValue(c);
                    for (int j = 0; j < count && col < 8; j++) {
                        Board.game_board[row][col++] = null;
                    }
                } else {
                    PieceColor color = (Character.isLowerCase(c)) ? PieceColor.BLACK : PieceColor.WHITE;
                    Label label = new Label();
                    label.setStyle("-fx-font-size: 36px;");
                    label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    label.setAlignment(Pos.CENTER);
                    GridPane.setHalignment(label, HPos.CENTER);
                    GridPane.setValignment(label, VPos.CENTER);
                    char pieceChar = Character.toLowerCase(c);
                    Pieces piece = null;
                    switch (pieceChar) {
                        case 'p' -> piece = new Pawn(color, label);
                        case 'r' -> piece = new Rook(color, label);
                        case 'n' -> piece = new Knight(color, label);
                        case 'b' -> piece = new Bishop(color, label);
                        case 'q' -> piece = new Queen(color, label);
                        case 'k' -> piece = new King(color, label);
                    }
                    if (piece != null) {
                        Board.game_board[row][col] = piece;
                        piece.position = new Coordinates<>(row, col);;
                        col++;
                    }
                }
            }
        }
    }


    private void setTurn(String turnStr) {
        if ("w".equals(turnStr)) {
            Board.turn.player = PieceColor.WHITE;
        } else if ("b".equals(turnStr)) {
            Board.turn.player = PieceColor.BLACK;
        } else if("null".equals(turnStr)){
            Board.turn.player = null;
        } else {
            throw new RuntimeException("Invalid turn in FEN string: " + turnStr);
        }
    }

    private void setCastlingRights(String castlingStr) {
        for (int colorIdx = 0; colorIdx < 2; colorIdx++) {
            PieceColor color = (colorIdx == 0) ? PieceColor.WHITE : PieceColor.BLACK;
            int kingRow = (color == PieceColor.WHITE) ? 7 : 0;
            Pieces king = Board.game_board[kingRow][4];
            if (king instanceof King) ((King) king).moved = true;
            Pieces rookK = Board.game_board[kingRow][7];
            if (rookK instanceof Rook) ((Rook) rookK).moved = true;
            Pieces rookQ = Board.game_board[kingRow][0];
            if (rookQ instanceof Rook) ((Rook) rookQ).moved = true;
        }
        if (castlingStr.contains("K")) handleCastlingKingside(PieceColor.WHITE);
        if (castlingStr.contains("Q")) handleCastlingQueenside(PieceColor.WHITE);
        if (castlingStr.contains("k")) handleCastlingKingside(PieceColor.BLACK);
        if (castlingStr.contains("q")) handleCastlingQueenside(PieceColor.BLACK);
    }

    private void handleCastlingKingside(PieceColor color) {
        int kingRow = (color == PieceColor.WHITE) ? 7 : 0;
        Pieces king = Board.game_board[kingRow][4];
        Pieces rook = Board.game_board[kingRow][7];
        if (king instanceof King && king.color == color) ((King) king).moved = false;
        if (rook instanceof Rook && rook.color == color) ((Rook) rook).moved = false;
    }

    private void handleCastlingQueenside(PieceColor color) {
        int kingRow = (color == PieceColor.WHITE) ? 7 : 0;
        Pieces king = Board.game_board[kingRow][4];
        Pieces rook = Board.game_board[kingRow][0];
        if (king instanceof King && king.color == color) ((King) king).moved = false;
        if (rook instanceof Rook && rook.color == color) ((Rook) rook).moved = false;
    }

    private void setEnPassantSquare(String epStr, PieceColor currentTurn) {
        Pawn.epList.clear();
        if (!"-".equals(epStr) && epStr.length() == 2) {
            char file = epStr.charAt(0);
            int rank = Character.getNumericValue(epStr.charAt(1));
            int row = 8 - rank;
            int col = file - 'a';
            if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                Pawn.epList.add(new Coordinates<>(row, col));
            }
        }
        if(currentTurn == PieceColor.WHITE){
            if(!Pawn.epList.isEmpty()){
                if(!Pieces.isOutOfBoard(Pawn.epList.get(0).getX()-1, Pawn.epList.get(0).getY()-1) && Board.game_board[Pawn.epList.get(0).getX()-1][Pawn.epList.get(0).getY()-1] != null && Board.game_board[Pawn.epList.get(0).getX()-1][Pawn.epList.get(0).getY()-1] instanceof Pawn){
                    Pawn pawn = (Pawn) Board.game_board[Pawn.epList.get(0).getX()-1][Pawn.epList.get(0).getY() - 1];
                    Pawn opp_pawn = (Pawn) Board.game_board[Pawn.epList.get(0).getX() + 1][Pawn.epList.get(0).getY()];
                    opp_pawn.movedByTwo = true;
                    pawn.did_ep = false;
                } else if(!Pieces.isOutOfBoard(Pawn.epList.get(0).getX()-1, Pawn.epList.get(0).getY()+1) && Board.game_board[Pawn.epList.get(0).getX()-1][Pawn.epList.get(0).getY()+1] != null && Board.game_board[Pawn.epList.get(0).getX()-1][Pawn.epList.get(0).getY()+1] instanceof Pawn){
                    Pawn pawn = (Pawn) Board.game_board[Pawn.epList.get(0).getX()-1][Pawn.epList.get(0).getY() + 1];
                    Pawn opp_pawn = (Pawn) Board.game_board[Pawn.epList.get(0).getX() + 1][Pawn.epList.get(0).getY()];
                    opp_pawn.movedByTwo = true;
                    pawn.did_ep = false;
                }
            }
        }else {
            if(!Pawn.epList.isEmpty()){
                if(!Pieces.isOutOfBoard(Pawn.epList.get(0).getX()+1, Pawn.epList.get(0).getY()-1)&& Board.game_board[Pawn.epList.get(0).getX()+1][Pawn.epList.get(0).getY()-1] != null && Board.game_board[Pawn.epList.get(0).getX()+1][Pawn.epList.get(0).getY()-1] instanceof Pawn){
                    Pawn pawn = (Pawn) Board.game_board[Pawn.epList.get(0).getX()+1][Pawn.epList.get(0).getY() - 1];
                    Pawn opp_pawn = (Pawn) Board.game_board[Pawn.epList.get(0).getX() - 1][Pawn.epList.get(0).getY()];
                    opp_pawn.movedByTwo = true;
                    pawn.did_ep = false;
                } else if(!Pieces.isOutOfBoard(Pawn.epList.get(0).getX()+1, Pawn.epList.get(0).getY()+1) && Board.game_board[Pawn.epList.get(0).getX()+1][Pawn.epList.get(0).getY()+1] != null && Board.game_board[Pawn.epList.get(0).getX()+1][Pawn.epList.get(0).getY()+1] instanceof Pawn){
                    Pawn pawn = (Pawn) Board.game_board[Pawn.epList.get(0).getX()+1][Pawn.epList.get(0).getY() + 1];
                    Pawn opp_pawn = (Pawn) Board.game_board[Pawn.epList.get(0).getX() - 1][Pawn.epList.get(0).getY()];
                    opp_pawn.movedByTwo = true;
                    pawn.did_ep = false;
                }
            }
        }
    }


    static void updateHashList(Long hash){
        if(Board.hashList.size()>9){
            Board.hashList.remove(0);
        }
        Board.hashList.add(hash);
    }

    void promotion(Pieces newPiece, int row, int col, Pawn oldPawn) {
        Board.game_board[row][col] = newPiece;
        newPiece.position = new Coordinates<>(row, col);
        newPiece.setPosition(row, col);
        Board.cells[row][col].getChildren().remove(oldPawn.label);
        Board.cells[row][col].getChildren().add(newPiece.label);
        System.out.println(newPiece.position);
    }

    boolean checkForPromotion(Pieces piece){
        if(piece instanceof Pawn){
            if(piece.color == PieceColor.WHITE && piece.position.getX() == 0){
                return true;
            }
            if(piece.color == PieceColor.BLACK && piece.position.getX() == 7){
                return true;
            }
        }
        return false;
    }


}
