package nu.olivertwistor.todolisttools.menus;

import ch.rfin.util.Pair;
import nu.olivertwistor.java.tui.Terminal;
import nu.olivertwistor.todolisttools.Session;
import nu.olivertwistor.todolisttools.models.Task;
import nu.olivertwistor.todolisttools.rtmapi.Response;
import nu.olivertwistor.todolisttools.rtmapi.methods.AddTask;
import nu.olivertwistor.todolisttools.util.Config;
import org.dom4j.DocumentException;
import sun.text.resources.cldr.fo.FormatData_fo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class CsvAddTasksAction implements MenuAction
{
    private static final int seconds_per_request = 1500;

    private final Config config;
    private final Session session;

    public CsvAddTasksAction(final Config config, final Session session)
    {
        this.config = config;
        this.session = session;
        if (!this.session.hasTimeline())
        {
            try
            {
                this.session.createTimeline();
            }
            catch (final DocumentException | NoSuchAlgorithmException |
                    IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void execute()
    {
        System.out.println(String.join(System.lineSeparator(),
                "Here, you can specify a CSV file containing tasks to add to ",
                "Remember The Milk. Please note that the first row is ",
                "considered headings, and the only task property currently ",
                "implemented is name and it must be in the first column. All ",
                "other columns are ignored. The tasks will be added to ",
                "Inbox, and have the default due date and neither tags nor ",
                "time estimates."));
        System.out.println();

        String csvFileInput = null;
        String csvDelimiter = null;
        try
        {
            csvFileInput = Terminal.readString(
                    "Path to the CSV file to load: ");
            csvDelimiter = Terminal.readString(
                    "By which character is the columns separated? ");
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        if (csvFileInput == null)
        {
            System.out.println("No filename entered.");
            return;
        }

        List<Task> parsedFile = null;
        try
        {
            final Path filePath = Paths.get(csvFileInput);
            final File file = filePath.toFile();

            parsedFile = this.parseCsvFile(file, csvDelimiter);
        }
        catch (final InvalidPathException | IOException | URISyntaxException e)
        {
            e.printStackTrace();
        }

        if (parsedFile == null)
        {
            System.out.println("Failed to parse the CSV file into tasks.");
            return;
        }

        for (final Task task : parsedFile)
        {
            final String smartAdd = task.toSmartAdd();
            try
            {
                final AddTask addTask = new AddTask(
                        this.config, this.session, smartAdd);
                final Response response = addTask.getResponse();
                if (response.isResponseSuccess())
                {
                    System.out.println("Added task to RTM: " + smartAdd);
                }
                else
                {
                    System.out.println(
                            "Failed to add task tag to RTM: " + smartAdd);
                }
            }
            catch (final DocumentException | NoSuchAlgorithmException |
                    IOException e)
            {
                e.printStackTrace();
            }

            try
            {
                // Wait a moment until adding the next task to make sure we
                // adhere to the API rate limit.
                Thread.sleep(seconds_per_request);
            }
            catch (final InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    List<Task> parseCsvFile(final File file, final String delimiter)
            throws IOException, URISyntaxException
    {
        final List<Task> tasks = new ArrayList<>();

        // Read the file line by line and split each line into columns.
        try (final BufferedReader br = Files.newBufferedReader(
                file.toPath(), StandardCharsets.UTF_8))
        {
            // Skip the first line.
            final String skippedLine = br.readLine();
            int nReadLines = 0;
            if (skippedLine != null)
            {
                System.out.println("Skipped line: " + skippedLine);

                for (String line = br.readLine(); line != null;
                     line = br.readLine())
                {
                    nReadLines++;

                    final String[] columns = line.split(delimiter, -1);
                    final Task task = new Task(columns[0]);
                    tasks.add(task);
                }
            }

            System.out.println("Read " + nReadLines + " lines.");
        }

        return tasks;
    }
}
