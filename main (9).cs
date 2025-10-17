using System;
using System.Collections.Generic;

class Program
{
    static void Main()
    {
        Juego juego = new Juego();
        juego.Iniciar();
    }
}

class Juego
{
    int size = 10;
    char[,] tablero;
    Random rnd = new Random();
    int px, py;
    int vidas = 3;
    int energia = 4;
    int tesorosRecolectados = 0;
    int nivel = 1;
    int puntaje = 0;

    public void Iniciar()
    {
        Console.Title = "Juego de Tesoros - C#";
        while (true)
        {
            ConfigurarNivel();
            JugarNivel();

            if (vidas <= 0)
            {
                Console.Clear();
                Console.WriteLine("💀 Has perdido todas tus vidas.");
                break;
            }

            if (nivel == 3)
            {
                Console.Clear();
                Console.WriteLine("🎉 ¡Felicidades! Completaste todos los niveles.");
                break;
            }

            nivel++;
        }

        Console.WriteLine($"\n🏁 Puntuación final: {puntaje}");
    }

    void ConfigurarNivel()
    {
        tablero = new char[size, size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                tablero[i, j] = '.';

        for (int i = 0; i < 6 + nivel; i++)
        {
            int x, y;
            do
            {
                x = rnd.Next(size);
                y = rnd.Next(size);
            } while (tablero[x, y] != '.');
            tablero[x, y] = 'T';
        }

        for (int i = 0; i < 4 + nivel; i++)
        {
            int x, y;
            do
            {
                x = rnd.Next(size);
                y = rnd.Next(size);
            } while (tablero[x, y] != '.');
            tablero[x, y] = '$';
        }

        px = 0; py = 0;
        tablero[px, py] = 'J';
    }

    void JugarNivel()
    {
        int pasosSinChocar = 0;

        while (true)
        {
            Console.Clear();
            MostrarTablero();
            Console.WriteLine($"\nNivel: {nivel} | Vidas: {vidas} | Energía: {energia} | Tesoros: {tesorosRecolectados} | Puntaje: {puntaje}");
            Console.WriteLine("Usa W/A/S/D para moverte, Q para salir.");

            char tecla = Char.ToUpper(Console.ReadKey(true).KeyChar);
            if (tecla == 'Q') Environment.Exit(0);

            int nuevoX = px, nuevoY = py;
            if (tecla == 'W' && px > 0) nuevoX--;
            else if (tecla == 'S' && px < size - 1) nuevoX++;
            else if (tecla == 'A' && py > 0) nuevoY--;
            else if (tecla == 'D' && py < size - 1) nuevoY++;
            else continue;

            if (tablero[nuevoX, nuevoY] == 'T')
            {
                Console.Clear();
                Console.WriteLine("💀 ¡Caíste en una trampa! -1 vida");
                vidas--;
                energia = Math.Max(0, energia - 1);
                puntaje -= 10 * nivel;
                pasosSinChocar = 0;
                Console.ReadKey();
            }
            else if (tablero[nuevoX, nuevoY] == '$')
            {
                Console.Clear();
                Console.WriteLine("💎 ¡Encontraste un tesoro! +10 puntos");
                tesorosRecolectados++;
                puntaje += 10 * nivel;
                pasosSinChocar++;

                if (tesorosRecolectados % 3 == 0)
                {
                    vidas++;
                    Console.WriteLine("❤️ ¡Ganaste una vida extra!");
                }
                Console.ReadKey();
            }
            else
            {
                pasosSinChocar++;
                if (pasosSinChocar >= 3)
                {
                    energia++;
                    pasosSinChocar = 0;
                    Console.WriteLine("⚡ +1 punto de energía por moverte sin chocar.");
                    Console.ReadKey();
                }
            }

            tablero[px, py] = '.';
            px = nuevoX; py = nuevoY;
            tablero[px, py] = 'J';

            if (tesorosRecolectados >= 5 * nivel)
            {
                Console.Clear();
                Console.WriteLine($"🎯 ¡Completaste el nivel {nivel}!");
                puntaje += 50 * nivel;
                Console.ReadKey();
                break;
            }

            if (vidas <= 0)
                break;
        }
    }

    void MostrarTablero()
    {
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
                Console.Write(tablero[i, j] + " ");
            Console.WriteLine();
        }
    }
}
