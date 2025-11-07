import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Grid grid = new Grid(10, 8);          // Opret et 10x8 grid
        grid.placeObjects(55, 10, 5);// Placer 55 træer, 10 sten og 5 vandfelter
        grid.chooseBurningTree(12);
        grid.print();                        // Udskriv gitteret
    }
}

// Repræsenterer én celle i gitteret
class Cell {
    private char symbol;

    public Cell() {
        this.symbol = '.'; // Standard: tom celle
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public boolean isTree() {
        return symbol == 'T';
    }

    public boolean isStone() {
        return symbol == 'S';
    }

    public boolean isWater() {
        return symbol == 'W';
    }

    public boolean isEmpty() {
        return symbol == '.';
    }
    public boolean IsBurningTree() {
        return symbol == 'B';
    }

}

// Repræsenterer hele gitteret
class Grid {
    private int rows;
    private int cols;
    private Cell[][] cells;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        cells = new Cell[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell();
            }
        }
    }

    // Placer et antal objekter tilfældigt i gitteret
    public void placeObjects(int countTree, int countStone, int countWater) {
        placeRandomObject('T', countTree);
        placeRandomObject('S', countStone);
        placeRandomObject('W', countWater);
    }

    public void placeRandomObject(char symbol, int count) {
        Random rand = new Random();
        int placedObejcts = 0;
        while (placedObejcts < count) {
            int row = rand.nextInt(rows);
            int col = rand.nextInt(cols);
            Cell cell = cells[row][col];
            if (cell.isEmpty()) {
                cell.setSymbol(symbol);
                placedObejcts++;
            }
        }
    }

    public void chooseBurningTree(int count) {
        Random rand = new Random();
        int burnedTreePlaced = 0;
        while (burnedTreePlaced < count) {
            int row = rand.nextInt(rows);
            int col = rand.nextInt(cols);
            Cell cell = cells[row][col];
            if (cell.isTree()) {
                cell.setSymbol('B');
                burnedTreePlaced++;
            }
        }

    }

    // Udskriver gitteret pænt
    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(cells[i][j].getSymbol() + " ");
            }
            System.out.println();
        }
    }
}
