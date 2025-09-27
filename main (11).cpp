#include <iostream>
#include <vector>
#include <string>
#include <cctype>
using namespace std;

const int N = 10;

void printBoard(const vector<vector<char>>& board) {
    cout << "   ";
    for (int c = 1; c <= N; ++c) cout << c << " ";
    cout << "\n";
    for (int r = 0; r < N; ++r) {
        char rowLabel = 'A' + r;
        cout << rowLabel << "  ";
        for (int c = 0; c < N; ++c) cout << board[r][c] << " ";
        cout << "\n";
    }
}

int main() {
    vector<vector<char>> board(N, vector<char>(N, '.'));
    printBoard(board);

    string coord;
    cout << "Ingrese una coordenada (ej: A1): ";
    cin >> coord;
    cout << "Usted ingreso: " << coord << "\n";
    return 0;
}
