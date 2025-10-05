#include <iostream>
#include <vector>
#include <ctime>
#include <cstdlib>
using namespace std;

const int TAM = 5;
const char AGUA = '~';
const char BARCO = 'B';
const char TOCADO = 'X';
const char FALLO = 'O';

struct Tablero {
    vector<vector<char>> celdas;
    Tablero() : celdas(TAM, vector<char>(TAM, AGUA)) {}
};

void mostrarTablero(const Tablero &t, bool ocultar) {
    cout << "  ";
    for (int i = 0; i < TAM; ++i) cout << i + 1 << " ";
    cout << "\n";
    for (int i = 0; i < TAM; ++i) {
        cout << i + 1 << " ";
        for (int j = 0; j < TAM; ++j) {
            if (ocultar && t.celdas[i][j] == BARCO)
                cout << AGUA << " ";
            else
                cout << t.celdas[i][j] << " ";
        }
        cout << "\n";
    }
}

void colocarBarcosJugador(Tablero &t, int n) {
    cout << "\nColoca tus " << n << " barcos (coordenadas entre 1 y " << TAM << "):\n";
    int x, y;
    for (int i = 0; i < n; ++i) {
        while (true) {
            cout << "Barco #" << i + 1 << " - Fila: ";
            cin >> x;
            cout << "Columna: ";
            cin >> y;
            if (x >= 1 && x <= TAM && y >= 1 && y <= TAM && t.celdas[x - 1][y - 1] == AGUA) {
                t.celdas[x - 1][y - 1] = BARCO;
                break;
            } else {
                cout << "Posición inválida o ya ocupada. Intenta otra.\n";
            }
        }
    }
}

void colocarBarcosComputadora(Tablero &t, int n) {
    srand(time(0));
    int colocados = 0;
    while (colocados < n) {
        int x = rand() % TAM;
        int y = rand() % TAM;
        if (t.celdas[x][y] == AGUA) {
            t.celdas[x][y] = BARCO;
            colocados++;
        }
    }
}

bool disparar(Tablero &t, int x, int y) {
    if (t.celdas[x][y] == BARCO) {
        t.celdas[x][y] = TOCADO;
        return true;
    } else if (t.celdas[x][y] == AGUA) {
        t.celdas[x][y] = FALLO;
    }
    return false;
}

bool todosHundidos(const Tablero &t) {
    for (int i = 0; i < TAM; ++i)
        for (int j = 0; j < TAM; ++j)
            if (t.celdas[i][j] == BARCO)
                return false;
    return true;
}

int main() {
    Tablero jugador, cpu;
    int numBarcos = 3;

    cout << "=== BATALLA NAVAL 5x5 ===\n";
    cout << "Tienes que hundir los barcos enemigos antes que ellos hundan los tuyos.\n";

    colocarBarcosJugador(jugador, numBarcos);
    colocarBarcosComputadora(cpu, numBarcos);

    cout << "\n¡Empieza la batalla!\n";

    bool turnoJugador = true;
    srand(time(0));

    while (true) {
        if (turnoJugador) {
            cout << "\n--- Tu turno ---\n";
            mostrarTablero(cpu, true);
            int x, y;
            cout << "Elige fila (1-5): ";
            cin >> x;
            cout << "Elige columna (1-5): ";
            cin >> y;
            if (x < 1 || x > TAM || y < 1 || y > TAM) {
                cout << "Fuera de rango. Intenta otra vez.\n";
                continue;
            }
            if (cpu.celdas[x - 1][y - 1] == TOCADO || cpu.celdas[x - 1][y - 1] == FALLO) {
                cout << "Ya disparaste ahí.\n";
                continue;
            }

            bool acierto = disparar(cpu, x - 1, y - 1);
            if (acierto) cout << "💥 ¡Le diste a un barco enemigo!\n";
            else cout << "🌊 Fallaste.\n";

            if (todosHundidos(cpu)) {
                cout << "\n¡GANASTE! Hundiste todos los barcos enemigos.\n";
                break;
            }
        } else {
            cout << "\n--- Turno de la computadora ---\n";
            int x = rand() % TAM;
            int y = rand() % TAM;
            cout << "La computadora dispara a (" << x + 1 << "," << y + 1 << ")\n";
            bool acierto = disparar(jugador, x, y);
            if (acierto) cout << "🔥 ¡La computadora acertó!\n";
            else cout << "💨 La computadora falló.\n";

            if (todosHundidos(jugador)) {
                cout << "\n💀 La computadora hundió todos tus barcos. PERDISTE.\n";
                break;
            }
        }
        turnoJugador = !turnoJugador;
    }

    cout << "\n--- Tu tablero final ---\n";
    mostrarTablero(jugador, false);
    cout << "\n--- Tablero enemigo (real) ---\n";
    mostrarTablero(cpu, false);
    cout << "\nFin del juego.\n";
    return 0;
}
