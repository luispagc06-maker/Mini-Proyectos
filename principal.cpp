#include <bits/stdc++.h>
using namespace std;

/*
  Programa: Colocación de barcos para Batalla Naval (Jugador).
  Tablero: 10x10 (filas A-J, columnas 1-10).
  Barcos: Carrier(5), Battleship(4), Cruiser(3), Submarine(3), Destroyer(2).
  Entrada: coordenada inicial (ej. A5) y orientación H/V.
  Muestra tablero final con:
    '.' = vacío
    '#' = barco
*/

const int N = 10;

struct Ship {
    string name;
    int size;
};

vector<Ship> ships = {
    {"Carrier", 5},
    {"Battleship", 4},
    {"Cruiser", 3},
    {"Submarine", 3},
    {"Destroyer", 2}
};

void print_board(const vector<vector<char>>& board) {
    cout << "   ";
    for (int c = 1; c <= N; ++c) {
        if (c < 10) cout << c << "  ";
        else cout << c << " ";
    }
    cout << "\n";

    for (int r = 0; r < N; ++r) {
        char rowLabel = 'A' + r;
        cout << rowLabel << "  ";
        for (int c = 0; c < N; ++c) {
            cout << board[r][c] << "  ";
        }
        cout << "\n";
    }
}

// Trim y pasar a mayúsculas
string upper_trim(const string &s) {
    string t;
    for (char ch : s) {
        if (!isspace((unsigned char)ch)) t.push_back(ch); // corregido
    }
    for (char &ch : t) ch = toupper((unsigned char)ch);
    return t;
}

// Parsear coordenada tipo "A5" o "J10" -> (row, col)
bool parse_coord(const string &inp, int &row, int &col) {
    if (inp.empty()) return false;
    string s = upper_trim(inp);

    char r = s[0];
    if (r < 'A' || r > 'J') return false;
    row = r - 'A';

    string num = s.substr(1);
    if (num.empty()) return false;
    for (char ch : num) if (!isdigit((unsigned char)ch)) return false;
    int c = stoi(num);
    if (c < 1 || c > 10) return false;
    col = c - 1;
    return true;
}

// Validar si cabe y no se superpone
bool can_place(const vector<vector<char>>& board, int row, int col, int size, char orient) {
    if (orient == 'H') {
        if (col + size - 1 >= N) return false;
        for (int j = 0; j < size; ++j)
            if (board[row][col + j] != '.') return false;
    } else { // 'V'
        if (row + size - 1 >= N) return false;
        for (int i = 0; i < size; ++i)
            if (board[row + i][col] != '.') return false;
    }
    return true;
}

// Colocar barco en tablero
void place_ship(vector<vector<char>>& board, int row, int col, int size, char orient) {
    if (orient == 'H') {
        for (int j = 0; j < size; ++j) board[row][col + j] = '#';
    } else {
        for (int i = 0; i < size; ++i) board[row + i][col] = '#';
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    vector<vector<char>> board(N, vector<char>(N, '.'));

    cout << "=== Batalla Naval: colocacion de barcos ===\n";
    cout << "Tablero 10x10. Filas A-J, columnas 1-10.\n";
    cout << "Formato coordenada: letra+numero sin espacio (ej. A5, J10).\n";
    cout << "Orientacion: H (horizontal, hacia la derecha) o V (vertical, hacia abajo).\n\n";

    for (const Ship& s : ships) {
        bool placed = false;
        while (!placed) {
            cout << "Coloque el barco " << s.name << " (tamaño " << s.size << ")\n";
            cout << ">> Coordenada inicial: ";
            string coord;
            if (!(cin >> coord)) {
                cout << "Entrada terminada inesperadamente.\n";
                return 0;
            }

            int row, col;
            if (!parse_coord(coord, row, col)) {
                cout << "!! Coordenada invalida. Use A-J y 1-10 (ej. B7). Intente de nuevo.\n";
                continue;
            }

            cout << ">> Orientacion (H/V): ";
            string ori_s;
            if (!(cin >> ori_s)) {
                cout << "Entrada terminada inesperadamente.\n";
                return 0;
            }
            ori_s = upper_trim(ori_s);
            if (ori_s.empty() || (ori_s[0] != 'H' && ori_s[0] != 'V')) {
                cout << "!! Orientacion invalida. Use H o V. Intente de nuevo.\n";
                continue;
            }
            char orient = ori_s[0];

            if (!can_place(board, row, col, s.size, orient)) {
                cout << "!! No se puede colocar el barco ahi (sale del tablero o se superpone).\n";
                print_board(board);
                continue;
            }

            place_ship(board, row, col, s.size, orient);
            cout << ">> " << s.name << " colocado correctamente.\n";
            print_board(board);
            cout << "\n";
            placed = true;
        }
    }

    cout << "=== Todos los barcos han sido colocados ===\n";
    print_board(board);
    cout << "\n'#' = barco, '.' = vacio\n";
    cout << "Fin del programa.\n";
    return 0;
}
