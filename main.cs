using System;

class Program {
    static void Main() {
        const int size = 10;
        char[,] tablero = new char[size, size];
        Random rnd = new Random();
        
        // Initialize the board with '.'
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                tablero[i, j] = '.';
        
        // Place 5 traps ('T')
        for (int i = 0; i < 5; i++) {
            int x = rnd.Next(size);
            int y = rnd.Next(size);
            tablero[x, y] = 'T';
        }

        // Place 3 treasures ('$')
        for (int i = 0; i < 3; i++) {
            int x = rnd.Next(size);
            int y = rnd.Next(size);
            if (tablero[x, y] == '.') 
                tablero[x, y] = '$';
        }

        int px = 0, py = 0;  // Player's initial position
        tablero[px, py] = 'J';

        while (true) {
            Console.Clear(); // Clear the console screen
            
            // Display the board
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++)
                    Console.Write(tablero[i, j] + " ");
                Console.WriteLine();
            }

            Console.WriteLine("\nUsa W, A, S, D para moverte. Q para salir.");
            char tecla = char.ToUpper(Console.ReadKey(true).KeyChar);

            int nuevoX = px, nuevoY = py;
            if (tecla == 'W' && px > 0) nuevoX--;
            else if (tecla == 'S' && px < size - 1) nuevoX++;
            else if (tecla == 'A' && py > 0) nuevoY--; // Corrected from px > 0 to py > 0
            else if (tecla == 'D' && py < size - 1) nuevoY++; // Corrected from px < size - 1 to py < size - 1
            else if (tecla == 'Q') break;

            if (tablero[nuevoX, nuevoY] == 'T') {
                Console.Clear();
                Console.WriteLine("¡Caíste en una trampa!");
                Console.ReadKey();
            } else if (tablero[nuevoX, nuevoY] == '$') {
                Console.Clear();
                Console.WriteLine("¡Encontraste un tesoro!");
                Console.ReadKey();
            }

            // Update the player's position
            tablero[px, py] = '.';
            px = nuevoX; 
            py = nuevoY;
            tablero[px, py] = 'J';
        }
        
        Console.WriteLine("\nJuego terminado.");
    }
}
