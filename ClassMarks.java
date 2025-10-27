import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * ClassMarks Program
 * ------------------
 * Reads a list of students and assignments from text files,
 * generates random marks for each student (mean = 75, SD = 10),
 * and saves the results to a CSV file.
 *
 * @author Beni Nkongolo
 * @version 1.0
 * @since 2025-10-26
 */
public final class ClassMarks {

    private ClassMarks() { } // Prevent instantiation
/**
     * Calculates class average.
     * @param args Command-line arguments (not used).
     */
    public static void main(final String[] args) {
        try {
            // === Step 1: Read students from file ===
            String[] students = readFileToArray("Unit2-07-students.txt");
            String[] assignments = readFileToArray("Unit2-07-assignment.txt");

            // === Step 2: Generate marks ===
            String[][] marksTable = generateMarks(students, assignments);

            // === Step 3: Write results to marks.csv ===
            writeCSV(marksTable, "marks.csv");

            System.out.println(
                "✅ Marks have been generated and saved to marks.csv");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Reads a text file and returns its contents as a String array.
     *
     * @param fileName the name of the text file
     * @return array of lines from the file
     * @throws IOException if file is not found or cannot be read
     */
    private static String[] readFileToArray(
        final String fileName) throws IOException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        // Count number of lines first
        int count = 0;
        while (scanner.hasNextLine()) {
            count++;
            scanner.nextLine();
        }
        scanner.close();

        // Read lines into array
        String[] lines = new String[count];
        scanner = new Scanner(file);
        int i = 0;
        while (scanner.hasNextLine()) {
            lines[i++] = scanner.nextLine().trim();
        }
        scanner.close();

        return lines;
    }

    /**
     * Generates random marks for each student based on assignments.
     * Marks follow a normal distribution (mean = 75, SD = 10).
     *
     * @param studentArr array of student names
     * @param assignmentArr array of assignment names
     * @return a 2D String array containing the table of marks
     */
    private static String[][] generateMarks(
        final String[] studentArr, final String[] assignmentArr) {
        Random random = new Random();

        // +1 for header row
        String[][] table = new String[
            studentArr.length + 1][assignmentArr.length + 1];

        // === Header Row ===
        table[0][0] = "Student";
        for (int i = 0; i < assignmentArr.length; i++) {
            table[0][i + 1] = assignmentArr[i];
        }

        // === Generate rows for each student ===
        for (int i = 0; i < studentArr.length; i++) {
            table[i + 1][0] = studentArr[i]; // first column is student name

            for (int j = 0; j < assignmentArr.length; j++) {
                // Generate Gaussian random number (mean = 75, sd = 10)
                int mark = (int) Math.round(random.nextGaussian() * 10 + 75);

                // Clamp to 0–100 range
                if (mark < 0) {
                    mark = 0;
                }
                if (mark > 100) {
                    mark = 100;
                }

                table[i + 1][j + 1] = String.valueOf(mark);
            }
        }

        return table;
    }

    /**
     * Writes a 2D array to a CSV file.
     *
     * @param data 2D array to write
     * @param fileName output file name
     * @throws IOException if an error occurs during writing
     */
    private static void writeCSV(
        final String[][] data, final String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);

        for (String[] row : data) {
            writer.write(String.join(",", row));
            writer.write("\n");
        }

        writer.close();
    }
}
