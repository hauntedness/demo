package test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class FilesAndPathsNio {


    public static void main(String[] args) throws IOException {
        String plotterLogPath = "C:\\\\Users\\\\Administrator\\\\.chia\\\\mainnet\\\\plotter";
        Stream<Path> list = Files.list(Paths.get(plotterLogPath));
        list.filter(path1 -> path1.toFile().lastModified() > System.currentTimeMillis() - 1000 * 60 * 60 * 2)
                .forEach(
                        path -> {
                            try {
                                Optional<String> max = Files.lines(path, Charset.forName("Cp1252")).max(Comparator.naturalOrder());
                                if (max.isPresent()) {
                                    System.out.println(max.get());
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
    }
}
