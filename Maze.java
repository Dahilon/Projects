// Maze solver work
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
//Class declaration
// This block defines the maze class and declers the 2D array to represent the maze also including the coordinatesfor the starting postions
public class Maze {
    char[][] maze;
    int startRow = -1, startCol = -1, endRow = -1, endCol = -1;
// Constructer intializes maze
    public Maze(String filename) throws FileNotFoundException {
        loadMaze(filename);
    }
//this method ads the maze file then builds the 2D maze array. it initializes the scanner to read the file.
    void loadMaze(String filename) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(new File(filename));
        StringBuilder mazeBuilder = new StringBuilder();
        int row = 0, col = -1;
        int startCount = 0, endCount = 0;
//the loop reads each line of the maze then checking if the mazs shape is rectangular
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (col == -1) col = line.length();
            if (line.length() != col) {
                System.out.println("Error not rectangular.");
                return;
            }
//found the " if ("SEO- ".indexOf(c) == -1)" on stack flows
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                if ("SEO- ".indexOf(c) == -1) {
                    System.out.println("error invalid '" + c + "' in maze.");
                    return;
                }
                if (c == 'S') startCount++;
                if (c == 'E') endCount++;
            }
            mazeBuilder.append(line).append("\n");
            row++;
        }
//building the maze array convert from stringbuilder to 2d array also identfies and stores the strat and end of the cordinates
        if (startCount != 1 || endCount != 1) {
            System.out.println("error:No S or E detected in maze");
            return;
        }

        String[] lines = mazeBuilder.toString().split("\n");
        maze = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                maze[i][j] = lines[i].charAt(j);
                if (maze[i][j] == 'S') {
                    startRow = i;
                    startCol = j;
                }
                if (maze[i][j] == 'E') {
                    endRow = i;
                    endCol = j;
                }
            }
        }
    }

    void printMaze() {
        for (char[] row : maze) {
            System.out.println(new String(row));
        }
    }
// this method solves for the maze starting from the start postions nd checks all four directions and moves
// towards them accordiangly once it reaches the end =point it returns true or contiues
    boolean solveMaze() {
        int row = startRow;
        int col = startCol;
        maze[row][col] = 'X';

        while (true) {
            boolean moved = false;

            // Check all four directions
            if (tMove(row, col, row + 1, col)) { moved = true; row++; }
            else if (tMove(row, col, row - 1, col)) { moved = true; row--; }
            else if (tMove(row, col, row, col + 1)) { moved = true; col++; }
            else if (tMove(row, col, row, col - 1)) { moved = true; col--; }

            if (moved) {
                if (maze[row][col] == 'E') return true;
                maze[row][col] = 'X';
            } else {
                break;  // No more moves available
            }
        }

        return false;
    }
// this tries to move into another postion in the maze it chekcs is the move is possible and will mark that postion
    boolean tMove(int currentRow, int currentCol, int newRow, int newCol) {
        if (validmove(newRow, newCol)) {
            if (maze[newRow][newCol] == 'E') {
                return true; // Exit found
            }
            maze[newRow][newCol] = 'X';
            return true;
        }
        return false;
    }
//this checks if the move is valid in the given postion being s and o
    boolean validmove(int row, int col) {
        return row >= 0 && row < maze.length &&
                col >= 0 && col < maze[0].length &&
                (maze[row][col] == 'O' || maze[row][col] == 'E');
    }
// main method which runs the program
    public static void main(String[] args) throws FileNotFoundException {
        Maze maze = new Maze("maze.txt");
        if (maze.solveMaze()) {
            System.out.println("Maze solved:");
            maze.printMaze();
        } else {
            System.out.println("No solution ");
        }
    }
}
