
import java.util.*;

public class FileSys {
// The file node class
    // Inner class to represent Files and Directories got info about the inner classes from stack flow
    static class FileNode {
        String name;
        boolean isdirectory;
        String content;
        Map<String, FileNode> child;
        FileNode parent; // The parent diretory

        // Constructor for initializing  both files or directories file if the node is an directory
        public FileNode(String name, boolean isdirectory, FileNode parent) {
            this.name = name;
            this.isdirectory = isdirectory;
            this.content = isdirectory ? null : "";
            this.child = isdirectory ? new TreeMap<>() : null;
            this.parent = parent;
        }
    }

    // Root and current directory and the pointers
    private FileNode root;
    private FileNode current;

    // This construtor intilizes the file system
    public FileSys() {
        root = new FileNode("/", true, null);  // Root does not have an parent
        current = root;
    }
// This methiod is where the filesys is run in a loop acceping  user input the switch commands handles the diffrent commands
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter prompt> ");
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.print("Enter prompt> ");
                continue;
            }
            String[] args = input.split("\\s+", 2);
            String command = args[0];
            String argument = args.length > 1 ? args[1] : null;

            try {
                switch (command) {
                    case "create":
                        createFile(argument);
                        break;
                    case "cat":
                        catFile(argument);
                        break;
                    case "rm":
                        removeFile(argument);
                        break;
                    case "mkdir":
                        makeDirectory(argument);
                        break;
                    case "rmdir":
                        removeDirectory(argument);
                        break;
                    case "cd":
                        changeDirectory(argument);
                        break;
                    case "ls":
                        listDirectory();
                        break;
                    case "du":
                        diskUsage(current);
                        break;
                    case "pwd":
                        printWorkingDirectory();
                        break;
                    case "find":
                        find(argument, current, "");
                        break;
                    case "exit":
                        System.out.println("Exiting");
                        scanner.close();
                        return;
                    default:
                        throw new IllegalArgumentException("Error invalid commaand");
                }
            } catch (Exception e) {
                System.out.println("Error " + e.getMessage());
            }
            System.out.print(" Enter prompt> ");
        }
    }
    // Command methods
    // command to create a file this method allow useres to create a new file in the current directory it checks if the
    // file name is valid and if it already exit  got some code from stack flows
    private void createFile(String fileName) {
        if (fileName == null) throw new IllegalArgumentException("File name needed ");
        if (current.child.containsKey(fileName))
            throw new IllegalArgumentException("File or directory already exist.");

        //Creating an new FileNode for the file with current as its parent
        FileNode file = new FileNode(fileName, false, current);
        System.out.println("Enter file content end with ~ ");
        Scanner scanner = new Scanner(System.in);
        StringBuilder content = new StringBuilder();
        String line;
// Loops reading the file content lione by line until the user Types ~
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.contains("~")) {
                content.append(line.substring(0, line.indexOf('~')));
                break;
            }
            content.append(line).append("\n");
        }
        //Adding file to the current directory's children
        file.content = content.toString();
        current.child.put(fileName, file);
    }

    // Command to display the content of a file it checks if the  file exists and whether it's a file (not a directory).
    private void catFile(String fileName) {
        if (fileName == null) throw new IllegalArgumentException("Filename required.");
        FileNode file = current.child.get(fileName);
        if (file == null || file.isdirectory)
            throw new IllegalArgumentException("File not found or is a directory.");
        System.out.println(file.content);
    }

    // Command to remove a file
    private void removeFile(String fileName) {
        if (fileName == null) throw new IllegalArgumentException("Filename required.");
        FileNode file = current.child.get(fileName);
        if (file == null || file.isdirectory)
            throw new IllegalArgumentException("File not found or is a directory.");
        current.child.remove(fileName);
    }

    // Command to create a directory
    private void makeDirectory(String dirName) {
        if (dirName == null) throw new IllegalArgumentException("Directory name required.");
        if (current.child.containsKey(dirName))
            throw new IllegalArgumentException("File or directory already exists.");
        current.child.put(dirName, new FileNode(dirName, true, current)); // Parent is current directory
    }

    // Command to remove a directory
    private void removeDirectory(String dirName) {
        if (dirName == null) throw new IllegalArgumentException("Directory name required.");
        FileNode dir = current.child.get(dirName);
        if (dir == null || !dir.isdirectory)
            throw new IllegalArgumentException("Directory not found or is a file.");
        current.child.remove(dirName);
    }

    // Command to change directory had help from stack flow
    private void changeDirectory(String path) {
        if (path == null) throw new IllegalArgumentException("Directory name required.");

        if (path.equals("/")) {
            current = root;
            return;
        }
        if (path.equals("..")) {
            if (current == root) return;
            current = current.parent;
            return;
        }

        // Otherwise, navigate through the parts of the path this method changes directory based on the path provided
        // and depding if its / or .. the cruurent irectory will be set to root or navigates to parent directory
        String[] parts = path.split("/");
        FileNode temp = current;

        for (String part : parts) {
            if (part.isEmpty()) continue;  // Skip empty parts (like in `cd /` or `cd ..`)
            if (!temp.child.containsKey(part) || !temp.child.get(part).isdirectory)
                throw new IllegalArgumentException("Invalid path.");
            temp = temp.child.get(part);  // Move to the next directory
        }
        current = temp;  // Update the current directory to the final directory in the path
    }

    // This methods list all the files and directories in the current directory
    private void listDirectory() {
        for (Map.Entry<String, FileNode> entry : current.child.entrySet()) {
            System.out.println(entry.getKey() + (entry.getValue().isdirectory ? " (*)" : ""));
        }
    }
    // Command to display current working directory
    private void printWorkingDirectory() {
        if (current == null) {
            System.out.println("Error current directory is null.");
            return;
        }
        List<String> path = new ArrayList<>();
        FileNode temp = current;
        // goes thoguh the path from current to root from stackflows
        while (temp != root) {
            path.add(temp.name);
            temp = temp.parent;
        }
        Collections.reverse(path);
        System.out.println("/" + String.join("/", path));
    }

    // Recursive command to calculate disk usage oof the directory
    private int diskUsage(FileNode node) {
        if (!node.isdirectory) return node.content.length();
        int size = 0;
        for (FileNode child : node.child.values()) {
            size += diskUsage(child);
        }
        System.out.println("Disk usage is  " + size + " characters.");
        return size;
    }

    // Recursive  method to do the command to find a file or directory by name
    private void find(String name, FileNode node, String path) {
        path = path + "/" + node.name;
        if (node.name.equals(name)) {
            System.out.println(path);
        }
        if (node.isdirectory) {
            for (FileNode child : node.child.values()) {
                find(name, child, path);
            }
        }
    }

    public static void main(String[] args) {
        new FileSys().run();
    }
}
