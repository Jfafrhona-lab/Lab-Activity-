import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FileSystemTree {

    private Node root;

    public FileSystemTree() {
        root = new Node("/", null);
    }

    public static void main(String[] args) {
        FileSystemTree fileSystem = new FileSystemTree();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(fileSystem.getCurrentDirectory() + "> ");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("exit")) {
                break;
            } else if (command.startsWith("mkdir ")) {
                fileSystem.createDirectory(command.substring(6));
            } else if (command.equalsIgnoreCase("ls")) {
                fileSystem.listDirectory();
            } else if (command.startsWith("cd ")) {
                fileSystem.changeDirectory(command.substring(3));
            } else {
                System.out.println("Invalid command. Please try again.");
            }
        }
        scanner.close();
    }

    private String getCurrentDirectory() {
        return root.getCurrentPath();
    }

    private void createDirectory(String directoryName) {
        Node currentDirectory = root.findNode(getCurrentDirectory());
        if (currentDirectory.hasChild(directoryName)) {
            System.out.println("Directory already exists.");
        } else {
            currentDirectory.addChild(new Node(directoryName, currentDirectory));
            System.out.println("Directory " + directoryName + " created successfully.");
        }
    }

    private void listDirectory() {
        Node currentDirectory = root.findNode(getCurrentDirectory());
        List<String> directoryContents = currentDirectory.getChildrenNames();
        if (directoryContents.isEmpty()) {
            System.out.println("Directory is empty.");
        } else {
            for (String content : directoryContents) {
                System.out.println(content);
            }
        }
    }

    private void changeDirectory(String directoryName) {
        Node currentDirectory = root.findNode(getCurrentDirectory());
        if (directoryName.equals("..")) {
            if (currentDirectory.getParent() != null) {
                root = currentDirectory.getParent();
                System.out.println("Changed directory to " + getCurrentDirectory());
            } else {
                System.out.println("Already at the root directory.");
            }
        } else {
            if (currentDirectory.hasChild(directoryName)) {
                root = currentDirectory.getChild(directoryName);
                System.out.println("Changed directory to " + getCurrentDirectory());
            } else {
                System.out.println("Directory not found.");
            }
        }
    }

    private static class Node {
        private String name;
        private Node parent;
        private Map<String, Node> children;

        public Node(String name, Node parent) {
            this.name = name;
            this.parent = parent;
            this.children = new HashMap<>();
        }

        public String getName() {
            return name;
        }

        public Node getParent() {
            return parent;
        }

        public void addChild(Node child) {
            children.put(child.getName(), child);
        }

        public Node getChild(String name) {
            return children.get(name);
        }

        public boolean hasChild(String name) {
            return children.containsKey(name);
        }

        public List<String> getChildrenNames() {
            return new ArrayList<>(children.keySet());
        }

        public String getCurrentPath() {
            if (parent == null) {
                return name;
            } else {
                return parent.getCurrentPath() + "/" + name;
            }
        }

        public Node findNode(String path) {
            if (path.equals(getCurrentPath())) {
                return this;
            } else {
                for (Node child : children.values()) {
                    Node foundNode = child.findNode(path);
                    if (foundNode != null) {
                        return foundNode;
                    }
                }
                return null;
            }
        }
    }
}
