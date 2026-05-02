import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class MergeFiles {
    public static void main(String[] args) {
        String outputFile = "merged_code.txt";
        String rootDir = "."; // текущая директория
        List<String> extensions = List.of(".java", ".html", ".xml", ".properties", ".txt");

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile))) {
            Files.walk(Paths.get(rootDir))
                    .filter(Files::isRegularFile)
                    .filter(path -> extensions.stream().anyMatch(ext -> path.toString().endsWith(ext)))
                    .forEach(path -> {
                        try {
                            // Записать заголовок с именем файла
                            writer.write("=".repeat(50));
                            writer.newLine();
                            writer.write("Файл: " + path.toAbsolutePath());
                            writer.newLine();
                            writer.write("=".repeat(50));
                            writer.newLine();

                            // Записать содержимое
                            Files.lines(path).forEach(line -> {
                                try {
                                    writer.write(line);
                                    writer.newLine();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            // Две пустые строки между файлами
                            writer.newLine();
                            writer.newLine();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            System.out.println("Готово! Все файлы объединены в " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}