import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Grid grid = new Grid(10, 8);          // creating a 10x8 grid
        grid.placeObjects(55, 10, 5);         // Place 55 trees, 10 stones and 5 water tiles
        grid.chooseBurningTree(12);           // the number of burning trees out of the total number of trees, in this case 12 out of 55 are burning.
        grid.print();                         // prints the grid.

        /* this while loop takes user input, updates the row and column, and spreads the water horizontally and vertically.
           After that it updates the spreading of fire, prints the output, and then displays how many trees and fires there are.
           This while loop runs until there are no more fires in the grid. */
        while (grid.hasfire()) {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter number of row (0-9): ");
            int row = input.nextInt();
            System.out.print("Enter number of column (0-7): ");
            int col = input.nextInt();
            grid.placeWater(row, col);
            grid.print();
            System.out.println("Water has spread to the cell (" + row + "," + col + ")");
            grid.fireUpdate();
            grid.print();
            grid.fireCount();
            grid.treeCount();
        }
    }
}

/* This class represents the cells in the grid and fills them with characters corresponding to whether it is stone, fire, etc. */
class Cell {
    private char symbol;

    public Cell() {
        this.symbol = '.'; // Default: empty cell, so-called constructor method
    }

    // getter: a specific method that returns the symbol of the cell
    public char getSymbol() {
        return symbol;
    }

    // setter: makes it possible to set a cell to contain a specific character
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    // this method tests if the symbol in the cell is a capital letter T, which represents a tree;
    // similar logic applies to the other booleans in this class
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

// this class represents the grid with cells, like before with symbols;
// in main it is determined how many cells/symbols there are in the grid
class Grid {
    // these private variables are instance variables, so not directly changeable from outside
    private int rows;
    private int cols;
    private Cell[][] cells;

    // this sets up the grid to contain a certain number of rows and columns
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

    // this method helps the fire method to not check array positions outside the indexes
    public boolean ingrid(int row, int col) {
        return row >= 0 && row <= rows && col >= 0 && col <= cols;
    }

    // this method changes the symbols in cells from empty to a corresponding symbol:
    // either stone, tree or water, for a specific amount set in Main
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

    // this method replaces the tree symbol with the symbol B, as a burning tree,
    // in a random cell containing a tree symbol, a set number of times
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

    /* this method handles where water is placed and chooses the direction for the water so it spreads horizontally and vertically.
       It cannot be placed on stone, and it changes the symbol from burning tree to water. */
    public void placeWater(int row, int col) {
        if (!(ingrid(row, col))) {
            System.out.println("Outside of grid, please enter a valid row and column");
            return;
        }
        if (cells[row][col].isStone() || cells[row][col].isWater()) {
            System.out.println("Placed on a cell containing a stone or water, nothing happens. Try another cell");
            return;
        }
        if (cells[row][col].IsBurningTree()) {
            cells[row][col].setSymbol('W');
        }
        // this corresponds to the direction of water spreading: north, east, south and west
        waterSpreading(row, col, -1, 0);
        waterSpreading(row, col, 1, 0);
        waterSpreading(row, col, 0, 1);
        waterSpreading(row, col, 0, -1);
    }

    /*
       this method handles water spreading so it spreads horizontally and vertically,
       changes the symbols if the water hits a cell containing a burning tree,
       and stops spreading if the cell contains stone or water
    */
    public void waterSpreading(int row, int col, int drow, int dcol) {
        while (ingrid(row, col)) {
            if (cells[row][col].isStone() || cells[row][col].isWater()) {
                return;
            }
            if (cells[row][col].IsBurningTree()) {
                cells[row][col].setSymbol('W');
            }
            row += drow;
            col += dcol;
        }
    }

    /**
     * Simulates one time step of fire spread across the grid and updates cell states accordingly.
     */
    public void fireUpdate() {
        final double chance = 0.75;
        Random rand = new Random();
        boolean[][] nextFire = new boolean[rows][cols];
        int firespreading = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (cells[r][c].isTree() && fireNeighbour(r, c)) {
                    if (rand.nextDouble() < chance) {
                        nextFire[r][c] = true;
                        firespreading++;
                    }
                }
            }
        }
        System.out.println("There are " + firespreading + " fires that have spread");
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (cells[r][c].IsBurningTree()) {
                    cells[r][c].setSymbol('.');
                }
                if (nextFire[r][c]) {
                    cells[r][c].setSymbol('B');
                }
            }
        }
    }

    /**
     * Returns whether any of the four orthogonally adjacent cells to the given position contains a burning tree.
     */
    public boolean fireNeighbour(int row, int col) {
        // north
        if (ingrid(row - 1, col) && cells[row - 1][col].IsBurningTree()) {
            return true;
        }
        // west
        if (ingrid(row, col + 1) && cells[row][col + 1].IsBurningTree()) {
            return true;
        }
        // south
        if (ingrid(row + 1, col) && cells[row + 1][col].IsBurningTree()) {
            return true;
        }
        // east
        if (ingrid(row, col - 1) && cells[row][col - 1].IsBurningTree()) {
            return true;
        } else return false;
    }
// this method checks if there are any fires left in the grid
    public boolean hasfire() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (cells[r][c].IsBurningTree()) {
                    return true;
                }
            }
        }
        return false;
    }

    // this method displays the count of trees as text
    public void treeCount() {
        int treeCount = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (cells[r][c].isTree()) {
                    treeCount++;
                }
            }
        }
        System.out.println("There are " + treeCount + " trees in the grid");
    }

    // this method displays the count of fires as text
    public void fireCount() {
        int fireCount = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (cells[r][c].IsBurningTree()) {
                    fireCount++;
                }
            }
        }
        System.out.println("There are " + fireCount + " fires in the grid");
    }

    // this method handles the grid and displays it
    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(cells[i][j].getSymbol() + " ");
            }
            System.out.println();
        }
    }
}
