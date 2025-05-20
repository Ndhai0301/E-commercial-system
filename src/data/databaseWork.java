package data;

import java.io.*;
import java.util.*;
public class databaseWork {
    
    
    public static boolean addOneLine(String text, String filePath){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(text);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Failed to save to file: " + e.getMessage());
            return false;
        }
    }

    public static String extractField(String text, String key) {
        String pattern = "\"" + key + "\":";
        int start = text.indexOf(pattern);
        if (start == -1) return ""; 

        start += pattern.length();

        while (start < text.length() && Character.isWhitespace(text.charAt(start))) {
            start++;
        }

        if (text.charAt(start) == '"') {
            start++;
            int end = text.indexOf("\"", start);
            return (end > start) ? text.substring(start, end) : "";
        } else {

            int end = text.indexOf(",", start);
            if (end == -1) end = text.indexOf("}", start);
            return (end > start) ? text.substring(start, end).trim() : "";
        }
    }

    public static boolean checkExist(String key, String filePath){
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\""+key+"\":\"")) {
                    return true;
                }
            }
        } catch (IOException e) {}
        return false;
    }

    public static boolean updateFile(String key, String newValue, String filePath) {

        File inputFile = new File(filePath);
        File tempFile = new File("temp_" + inputFile.getName());
        boolean updated = false;

        try (
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String pattern = "\"" + key + "\":\"";
                int startIndex = line.indexOf(pattern);

                if (!updated && startIndex != -1) {
                    startIndex += pattern.length();
                    int endIndex = line.indexOf("\"", startIndex);
                    if (endIndex >= startIndex) {
                        line = line.substring(0, startIndex) + newValue + line.substring(endIndex);
                        updated = true;
                    }
                }

                writer.write(line);
                writer.newLine();
            }

            // Ghi xong -> thay thế file gốc bằng file tạm
            if (updated) {
                if (!inputFile.delete()) {
                    // System.err.println("Không thể xóa file gốc.");
                    return false;
                }
                if (!tempFile.renameTo(inputFile)) {
                    // System.err.println("Không thể đổi tên file tạm.");
                    return false;
                }
            } else {
                tempFile.delete(); 
            }

            return updated;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Map<String, String> convertLineToJsonMap(String line) {
        Map<String, String> resultMap = new HashMap<>();

        String[] pairs = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim().replace("\"", "");
                resultMap.put(key, value);
            }
        }

        return resultMap;
    }

    public static boolean deleteOneLine(String id, String filePath){
        File inputFile = new File(filePath);

        List<String> remainingLines = new ArrayList<>();
        boolean deleted = false;


        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"" + id + "\"")) {
                    deleted = true; 
                    continue;
                }
                remainingLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!deleted) {
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile, false))) {
            for (String i : remainingLines) {
                writer.write(i);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
        
    }
}
