package com.saucedemo.helperutilities.file;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FileUtils {
    private static final Logger log = LogManager.getLogger(FileUtils.class);


    public static void deleteFiles(String dirPath, String ext) {
        log.info(String.valueOf("clearing down all the downloaded reports"));
        File dir = new File(dirPath);
        if (!dir.exists()) return;
        File[] fList = dir.listFiles();
        assert fList != null;
        for (File f : fList) {
            if (f.getName().endsWith(ext)) f.delete();
        }
    }


    public static void compareCsvFiles() throws Exception {

    }

    public static Map<String, String> getMapFromCSV(final String filePath, String headerKey) throws IOException, IOException {
        Stream<String> lines = Files.lines(Paths.get(filePath));
        Map<String, String> resultMap = lines.map(line -> line.split(",")).collect(Collectors.toMap(line -> line[0], line -> line[1]));
        lines.close();
        resultMap.remove(headerKey);
        return resultMap;
    }


    public static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }


    public static void writeByteArrayToFile(File file, byte[] screenshot) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(screenshot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
