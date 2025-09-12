import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.start();
    }
}

class TicTacToe {
    private static final char EMPTY = ' ';
    private static final char X = 'X';
    private static final char O = 'O';

    private char[][] board = new char[3][3];
    private Scanner scanner = new Scanner(System.in);

    public TicTacToe() {
        initBoard();
    }

    // Inicia el juego (menú)
    public void start() {
        System.out.println("=== Juego del Gato (Tic-Tac-Toe) ===");
        System.out.println("1) 2 jugadores");
        System.out.println("2) Jugar contra la CPU (IA óptima - Minimax)");
        System.out.print("Elige modo (1 o 2): ");
        int modo = readIntInRange(1, 2);
        if (modo == 1) playTwoPlayers();
        else playVsCPU();
    }

    private void initBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = EMPTY;
    }

    private void playTwoPlayers() {
        char current = X;
        while (true) {
            printBoard();
            System.out.println("Turno de " + current);
            makeHumanMove(current);
            char winner = checkWinner();
            if (winner != EMPTY) {
                printBoard();
                System.out.println("¡El jugador " + winner + " gana!");
                break;
            }
            if (isBoardFull()) {
                printBoard();
                System.out.println("Empate.");
                break;
            }
            current = (current == X) ? O : X;
        }
    }

    private void playVsCPU() {
        System.out.print("¿Quieres ser X o O? (X empieza). Presiona Enter para X: ");
        String in = scanner.nextLine().trim().toUpperCase();
        char human = in.equals("O") ? O : X;
        char ai = (human == X) ? O : X;
        boolean humanTurn = (human == X); // X empieza siempre

        System.out.println("Tú eres " + human + ", la CPU es " + ai + ".");
        while (true) {
            printBoard();
            if (humanTurn) {
                System.out.println("Tu turno (" + human + ")");
                makeHumanMove(human);
            } else {
                System.out.println("Turno de la CPU (" + ai + ") — pensando...");
                Move m = findBestMove(ai);
                if (m.row != -1) board[m.row][m.col] = ai;
            }

            char winner = checkWinner();
            if (winner != EMPTY) {
                printBoard();
                if (winner == human) System.out.println("¡Felicidades! Ganaste.");
                else System.out.println("La CPU gana. Intenta otra vez.");
                break;
            }
            if (isBoardFull()) {
                printBoard();
                System.out.println("Empate.");
                break;
            }
            humanTurn = !humanTurn;
        }
    }

    private void makeHumanMove(char player) {
        while (true) {
            System.out.print("Ingresa fila (1-3): ");
            int r = readIntInRange(1, 3) - 1;
            System.out.print("Ingresa columna (1-3): ");
            int c = readIntInRange(1, 3) - 1;
            if (board[r][c] == EMPTY) {
                board[r][c] = player;
                break;
            } else {
                System.out.println("Esa casilla ya está ocupada. Intenta otra.");
            }
        }
    }

    private void printBoard() {
        System.out.println();
        System.out.println("   1   2   3");
        for (int i = 0; i < 3; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 3; j++) {
                System.out.print(" " + (board[i][j] == EMPTY ? ' ' : board[i][j]) + " ");
                if (j < 2) System.out.print("|");
            }
            System.out.println();
            if (i < 2) System.out.println("  ---+---+---");
        }
        System.out.println();
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == EMPTY) return false;
        return true;
    }

    private char checkWinner() {
        // Filas y columnas
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != EMPTY && board[i][0] == board[i][1] && board[i][1] == board[i][2])
                return board[i][0];
            if (board[0][i] != EMPTY && board[0][i] == board[1][i] && board[1][i] == board[2][i])
                return board[0][i];
        }
        // Diagonales
        if (board[0][0] != EMPTY && board[0][0] == board[1][1] && board[1][1] == board[2][2])
            return board[0][0];
        if (board[0][2] != EMPTY && board[0][2] == board[1][1] && board[1][1] == board[2][0])
            return board[0][2];
        return EMPTY;
    }

    private static class Move {
        int row, col;
        Move(int r, int c) { row = r; col = c; }
    }

    private Move findBestMove(char aiSymbol) {
        int bestVal = Integer.MIN_VALUE;
        Move bestMove = new Move(-1, -1);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = aiSymbol;
                    int moveVal = minimax(0, false, aiSymbol);
                    board[i][j] = EMPTY;
                    if (moveVal > bestVal) {
                        bestVal = moveVal;
                        bestMove.row = i;
                        bestMove.col = j;
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, boolean isMax, char aiSymbol) {
        char winner = checkWinner();
        char opponent = (aiSymbol == X) ? O : X;
        if (winner == aiSymbol) return 10 - depth;
        if (winner == opponent) return depth - 10;
        if (isBoardFull()) return 0;

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = aiSymbol;
                        best = Math.max(best, minimax(depth + 1, false, aiSymbol));
                        board[i][j] = EMPTY;
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = opponent;
                        best = Math.min(best, minimax(depth + 1, true, aiSymbol));
                        board[i][j] = EMPTY;
                    }
                }
            }
            return best;
        }
    }

    // Lectura segura de enteros (usa scanner.nextLine internamente)
    public int readIntInRange(int min, int max) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v < min || v > max) {
                    System.out.print("Ingresa un número entre " + min + " y " + max + ": ");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Ingresa un número: ");
            }
        }
    }
}
