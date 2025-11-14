import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Grid grid = new Grid(10, 8);          // Opret et 10x8 grid
        grid.placeObjects(55, 10, 5);// Placer 55 træer, 10 sten og 5 vandfelter
        grid.chooseBurningTree(12);
        grid.print();                       // Udskriv gitteret



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

    public boolean wall(int row, int col){

        return row  >= 0 && row <= 9 && col >= 0 && col <= 7;

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
    public void placeWater(int row,  int col) {

        if(!(wall(row, col))) {
            System.out.println("Outside of grid please enter a valid row and collom");
            return;
        }

        if(cells[row][col].isStone()) {
            System.out.println("Placed on a cell containing a stone, nothing happens. so try another cell");
            return;
        }

        if(cells[row][col].IsBurningTree()) {
            cells[row][col].setSymbol('W');
        }
        waterSpreading(row, col,-1, 0);
        waterSpreading(row, col,1, 0);
        waterSpreading(row, col, 0, 1);
        waterSpreading(row, col, 0, -1);

    }

    public void waterSpreading(int row, int col, int drow, int dcol) {

         while (wall(row, col)) {
             if (cells[row][col].isStone()) {
                 return;
             }
             if (cells[row][col].IsBurningTree()) {
                 cells[row][col].setSymbol('W');
             }
             row += drow;
             col += dcol;
         }
    }

    public void fireUpdate(){
        final double chance =0.75;
        Random rand = new Random();
        boolean[][] nextFire = new boolean[rows][cols];
        int firespreading = 0;

        for(int  r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                if(cells[r][c].isTree() && fireNeighbour(r, c)) {
                    if(rand.nextDouble() < chance){
                        nextFire[r][c] = true;
                        firespreading++;
                    }
                }
            }
        }
        System.out.println("There are " + firespreading + " fire that had spread");

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if(cells[r][c].IsBurningTree()) {
                    cells[r][c].setSymbol('.');
                }
                if(nextFire[r][c]) {
                    cells[r][c].setSymbol('B');
                }
            }

        }


    }

    public boolean fireNeighbour(int row, int col) {

        //north
        if(wall(row-1, col) && cells[row-1][col].IsBurningTree()) {
            return true;
        }
        //west
        if(wall(row, col+1) && cells[row][col+1].IsBurningTree() ) {
            return true;
        }
        //south
        if(wall(row+1, col) && cells[row+1][col].IsBurningTree()) {
            return true;
        }
        //east
        if(wall(row, col-1) && cells[row][col-1].IsBurningTree()) {
            return true;
        }
        else return false;

    }

    public boolean hasfire(){
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                if(cells[r][c].IsBurningTree()) {
                    return true;

                }
            }
        }
        return false;
    }

    public void treeCount(){
        int treeCount = 0;
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                if(cells[r][c].isTree()) {
                    treeCount++;
                }
            }
        }
        System.out.println("There are " + treeCount + " trees in the grid");

    }
    public void fireCount(){
        int fireCount = 0;
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                if(cells[r][c].IsBurningTree()) {
                    fireCount++;
                }
            }
        }
        System.out.println("There are " + fireCount + " fires in the grid");
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
