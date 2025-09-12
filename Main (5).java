import java.util.Scanner;

public class TicTacToe {
    private static final char EMPTY = ' ';
    private static final char X = 'X';
    private static final char O = 'O';

    private char[][] board = new char[3][3];
    private Scanner scanner = new Scanner(System.in);

    public TicTacToe() {
        clearBoard();
    }

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        System.out.println("=== Juego del Gato (Tic-Tac-Toe) ===");
        System.out.println("1) 2 jugadores");
        System.out.println("2) Jugar contra la CPU (IA, nivel óptimo)");
        System.out.print("Elige modo (1 o 2): ");
        int modo = game.readIntInRange(1, 2);
        if (modo == 1) {
            game.playTwoPlayers();
        } else {
            game.playVsCPU();
        }
    }

    /* ---------- Juego y flujo ---------- */
    private void playTwoPlayers() {
        char current = X;
        while (true) {
            printBoard();
            System.out.println("Turno de " + current);
            makeHumanMove(current);
            if (checkWin(current)) {
                printBoard();
                System.out.println("¡El jugador " + current + " gana!");
                break;
            }
            if (isFull()) {
                printBoard();
                System.out.println("Empate.");
                break;
            }
            current = (current == X) ? O : X;
        }
    }

    private void playVsCPU() {
        System.out.println("Eres X (comienzas). CPU es O.");
        char current = X; // humano empieza
        while (true) {
            printBoard();
            if (current == X) {
                System.out.println("Tu turno (X)");
                makeHumanMove(X);
            } else {
                System.out.println("Turno de la CPU (O) — pensando...");
                Move best = findBestMove(O);
                board[best.row][best.col] = O;
            }
            if (checkWin(current)) {
                printBoard();
                if (current == X) System.out.println("¡Felicidades! Ganaste.");
                else System.out.println("La CPU gana. Mejor suerte la próxima.");
                break;
            }
            if (isFull()) {
                printBoard();
                System.out.println("Empate.");
                break;
            }
            current = (current == X) ? O : X;
        }
    }

    /* ---------- Movimiento humano ---------- */
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

    /* ---------- Utilidades del tablero ---------- */
    private void clearBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = EMPTY;
    }

    private void printBoard() {
        System.out.println("\n   1   2   3 ");
        for (int i = 0; i < 3; i++) {
            System.out.print((i+1) + " ");
            for (int j = 0; j < 3; j++) {
                System.out.print(" " + (board[i][j] == EMPTY ? ' ' : board[i][j]) + " ");
                if (j < 2) System.out.print("|");
            }
            System.out.println();
            if (i < 2) System.out.println("  ---+---+---");
        }
        System.out.println();
    }

    private boolean isFull() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == EMPTY) return false;
        return true;
    }

    private boolean checkWin(char player) {
        // filas y columnas
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        // diagonales
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;
        return false;
    }

    /* ---------- IA: Minimax ---------- */
    private static class Move {
        int row, col;
        Move(int r, int c) { row = r; col = c; }
    }

    private Move findBestMove(char aiPlayer) {
        int bestVal = Integer.MIN_VALUE;
        Move bestMove = new Move(-1, -1);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = aiPlayer;
                    int moveVal = minimax(0, false, aiPlayer);
                    board[i][j] = EMPTY;
                    if (moveVal > bestVal) {
                        bestMove.row = i;
                        bestMove.col = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return bestMove;
    }

    // retorna +10 si aiPlayer gana, -10 si oponente gana, 0 empate
    private int evaluate(char aiPlayer) {
        if (checkWin(aiPlayer)) return +10;
        char human = (aiPlayer == X) ? O : X;
        if (checkWin(human)) return -10;
        return 0;
    }

    private int minimax(int depth, boolean isMax, char aiPlayer) {
        int score = evaluate(aiPlayer);
        if (score == 10) return score - depth; // preferir victoria rápida
        if (score == -10) return score + depth; // evitar derrota tardía
        if (isFull()) return 0;

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = aiPlayer;
                        best = Math.max(best, minimax(depth + 1, !isMax, aiPlayer));
                        board[i][j] = EMPTY;
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            char human = (aiPlayer == X) ? O : X;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = human;
                        best = Math.min(best, minimax(depth + 1, !isMax, aiPlayer));
                        board[i][j] = EMPTY;
                    }
                }
            }
            return best;
        }
    }

    /* ---------- Lectura segura de enteros ---------- */
    private int readIntInRange(int min, int max) {
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