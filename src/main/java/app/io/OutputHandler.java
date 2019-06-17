package app.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;

public class OutputHandler {
    private Path filePath; // = Paths.get("./output.txt");
    private LinkedList<String> lines = new LinkedList<>();
    public OutputHandler(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    public void addLine(String line) {
        this.lines.add(line);
    }

    public void writeAllLines() {
        Charset charset = Charset.forName("UTF-8");
        try (BufferedWriter writer = Files.newBufferedWriter(this.filePath, charset, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (String line : lines) {
                writer.write(line, 0, line.length());
                writer.newLine();
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }        
    }

    public void printAllLines() {
        for (String line : lines) {
            System.out.println(line);
        }
    }

}