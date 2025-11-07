import java.util.Random;

public class FK1af6 {
    public static void main(String[] args) {
        Grid grid = new Grid(9);   // Opret et 9x9 grid
        grid.placeObjects(10);     // Placer 10 tilfældige 'o'
        grid.print();              // Udskriv gitteret
        grid.printDistances();     // Beregn og udskriv afstande
    }
}

// Repræsenterer én celle i gitteret
class Cell {
    private char symbol;

    public Cell() {
        this.symbol = '.';
    }
    
    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public boolean isObject() {
        return symbol == 'o';
    }

    public boolean isEmpty() {
        return symbol == '.';
    }
}

// Repræsenterer hele gitteret (men som ét array af Cell-objekter)
class Grid {
    private Cell[] cells;
    private int size;
    private int centerRow;
    private int centerCol;

    public Grid(int size) {
        this.size = size;
        this.centerRow = size / 2;
        this.centerCol = size / 2;
        this.cells = new Cell[size * size];

        // Opret alle Cell-objekter
        for (int i = 0; i < cells.length; i++) {
            cells[i] = new Cell();
        }

        // Marker midtercellen
        getCell(centerRow, centerCol).setSymbol('X');
    }

    // Beregner index i arrayet ud fra række og kolonne
    private int index(int row, int col) {
        return row * size + col;
    }

    // Finder en specifik celle
    private Cell getCell(int row, int col) {
        return cells[index(row, col)];
    }

    // Placer et antal objekter tilfældigt i gitteret
    public void placeObjects(int count) {
        Random rand = new Random();

        for (int i = 0; i < count; i++) {
            int r = rand.nextInt(size);
            int c = rand.nextInt(size);

            Cell cell = getCell(r, c);
            if ((r == centerRow && c == centerCol) || !cell.isEmpty()) {
                continue; 
                }// ikke ovenpå X eller object 
            cell.setSymbol('o');
        }
    }

    // Udskriver gitteret pænt
    public void print() {
        for (int i = 0; i < cells.length; i++) {
            System.out.print(cells[i].getSymbol() + " ");
            if ((i + 1) % size == 0) System.out.println();
        }
    }

    // Finder og udskriver afstandene fra midten i fire retninger
    public void printDistances() {
        System.out.println("Distance NORTH = " + distance(-1, 0));
        System.out.println("Distance EAST  = " + distance(0, 1));
        System.out.println("Distance SOUTH = " + distance(1, 0));
        System.out.println("Distance WEST  = " + distance(0, -1));
    }

    // Beregner afstand i én retning (bruger offset dRow, dCol)
    private int distance(int dRow, int dCol) {
        int row = centerRow + dRow;
        int col = centerCol + dCol;
        int dist = 0;

        while (row >= 0 && row < size && col >= 0 && col < size) {
            dist++;
            if (getCell(row, col).isObject()) {
                return dist;
            }
            row += dRow;
            col += dCol;
        }
        return -1; // hvis ingen objekt i den retning
    }
}
