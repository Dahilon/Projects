import java.io.FileNotFoundException;

public class MazeSolver {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("error  no file provieded.");
            return;
        }

        String filename = args[0];

        try {
            Maze maze = new Maze(filename);
            System.out.println("Original Maze:");
            maze.printMaze();

            System.out.println("\nSolving Maze:");
            if (!maze.solveMaze()) {
                System.out.println("Maze could not be solved.");
            } else {
                maze.printMaze();
            }
        } catch (FileNotFoundException e) {
            System.out.println("error File not found - " + filename);
        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
        }
    }
}
