/**
 * Project name: Duke
 * @author Tan Jun Da A023489eU
 */

package seedu.duke;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import seedu.duke.task.Deadline;
import seedu.duke.task.Event;
import seedu.duke.task.Task;
import seedu.duke.task.Todo;

/**
 * Represents the storage for the Duke program.
 * A <code>Storage</code> object corresponds to
 * the filepath of the text file.
 */
public class Storage {

    protected String filePath;

    /**
     * Constructor for the Storage class.
     *
     * @param filePath The filepath to the storage text file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns a List of tasks.
     * If file not found, throw DukeException.
     *
     * @return The List of tasks.
     * @throws DukeException If file not found.
     */
    public List<Task> load() throws DukeException {
        try {
            return readAddFileContents(filePath);
        } catch (FileNotFoundException e) {
            throw new DukeException("File not found");
        }
    }

    /**
     * Write to the storage text file.
     *
     * @param tasks The tasks that need to be written.
     * @throws DukeException If no such text file is found.
     */
    public void write(TaskList tasks) throws DukeException {
        try {
            writeToFile(tasks.tasksList, "data/duke.txt");
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    /**
     * Writes to text file.
     * If file not found, throw IOException.
     *
     * @param storage
     * @param filePath
     * @throws IOException If no such text file is found.
     */
    public void writeToFile(List<Task> storage, String filePath) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        for (Task element : storage) {
            String mark = "0";
            if (element.getStatusIcon().equals("X")) {
                mark = "1";
            }
            if (element instanceof Todo) {
                fw.write("T | " + mark + " | " + element.getDescription());
            }
            if (element instanceof Deadline) {
                fw.write("D | " + mark + " | " + element.getDescription() + " | " + ((Deadline) element).getBy());
            }
            if (element instanceof Event) {
                fw.write("E | " + mark + " | " + element.getDescription() + " | "
                        + ((Event) element).getFrom() + " | " + ((Event) element).getTo());
            }
            fw.write(System.lineSeparator());
        }
        fw.close();
    }

    /**
     * Returns the List of tasks added from the text file.
     * If file not found, throw FileNotFoundException.
     *
     * @param filePath The filepath for the text file.
     * @return The List of tasks.
     * @throws FileNotFoundException If file not found.
     */
    public List<Task> readAddFileContents(String filePath) throws FileNotFoundException {
        File f = new File(filePath);
        List<Task> storage = new ArrayList<>();
        Scanner fileScanner = new Scanner(f);
        while (fileScanner.hasNext()) {
            String[] currArray = fileScanner.nextLine().split("\\|");
            boolean mark = false;
            if (currArray[1].trim().equals("1")) {
                mark = true;
            }
            if (currArray[0].trim().equals("T")) {
                Todo t = new Todo(currArray[2].trim());
                if (mark) {
                    t.mark();
                }
                storage.add(t);
            } else if (currArray[0].trim().equals("D")) {
                Deadline d = new Deadline(currArray[2], currArray[3]);
                if (mark) {
                    d.mark();
                }
                storage.add(d);
            } else {
                Event e = new Event(currArray[2], currArray[3], currArray[4]);
                if (mark) {
                    e.mark();
                }
                storage.add(e);
            }
        }
        return storage;
    }
}
