package nu.olivertwistor.todolisttools.menus;

import nu.olivertwistor.java.tui.Terminal;
import nu.olivertwistor.todolisttools.models.Task;
import nu.olivertwistor.todolisttools.rtmapi.rest.AddTask;
import nu.olivertwistor.todolisttools.rtmapi.rest.CreateTimeline;
import nu.olivertwistor.todolisttools.util.AppConfig;
import nu.olivertwistor.todolisttools.util.CsvConfig;
import nu.olivertwistor.todolisttools.util.Session;
import org.dom4j.DocumentException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Adds tasks to Remember The Milk based on a user supplied CSV file.
 *
 * @since 0.1.0
 */
@SuppressWarnings(
        {"PublicMethodWithoutLogging", "HardCodedStringLiteral",
                "DuplicateStringLiteralInspection", "StringConcatenation",
                "ClassWithoutLogger", "MultiCatchCanBeSplit"})
public final class CsvAddTasksAction implements MenuAction
{
    private static final int SECONDS_PER_REQUEST = 1_500;

    @SuppressWarnings("OverlyLongMethod")
    @Override
    public boolean execute(final AppConfig config, final Session session)
    {
        try
        {
            CsvAddTasksAction.createTimeline(config, session);
        }
        catch (final DocumentException | NoSuchAlgorithmException |
                IOException e)
        {
            System.out.println("Failed to create a new timeline.");
            return false;
        }

        final String inputCsvDataFile;
        final String inputCsvConfigFile;
        try
        {
            inputCsvDataFile = Terminal.readString(
                    "Path to CSV file containing tasks: ");
            inputCsvConfigFile = Terminal.readString(
                    "Path to CSV configuration file: ");
        }
        catch (final IOException e)
        {
            System.out.println("Failed to read user input.");
            return false;
        }

        List<Task> parsedDataFile = null;
        try
        {
            final Path dataFilePath = Paths.get(inputCsvDataFile);
            final File dataFile = dataFilePath.toFile();
            final Path configFilePath = Paths.get(inputCsvConfigFile);
            final File configFile = configFilePath.toFile();

            parsedDataFile = CsvAddTasksAction.parseCsvFile(
                    dataFile, configFile);
        }
        catch (final InvalidPathException | IOException e)
        {
            System.out.println("Failed to parse the CSV file.");
        }

        if (parsedDataFile == null)
        {
            System.out.println("Failed to parse the CSV file into tasks.");
            return false;
        }

        for (final Task task : parsedDataFile)
        {
            final String smartAdd = task.toSmartAdd();
            try
            {
                final AddTask addTask = new AddTask(config, session, smartAdd);
                if (addTask.isResponseSuccess())
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
                System.out.println(
                        "Failed to make an add task request to the RTM API.");
            }

            try
            {
                // Wait a moment until adding the next task to make sure we
                // adhere to the API rate limit.
                Thread.sleep(CsvAddTasksAction.SECONDS_PER_REQUEST);
            }
            catch (final InterruptedException e)
            {
                System.out.println("Current thread was interrupted.");
            }
        }

        return false;
    }

    /**
     * Parses a CSV file into a list of Task objects, using a configuration
     * detailing how to parse the CSV file.
     *
     * @param dataFile   the CSV file to parse
     * @param configFile file containing the configuration for parsing the CSV
     *                   file
     *
     * @return A list of Task objects.
     *
     * @throws IOException if any of the files couldn't be read
     *
     * @since 0.1.0
     */
    static List<Task> parseCsvFile(final File dataFile, final File configFile)
            throws IOException
    {
        final CsvConfig config = new CsvConfig(configFile);

        // Read the file line by line and split each line into columns.
        final Path dataFilePath = dataFile.toPath();
        try (final BufferedReader br = Files.newBufferedReader(
                dataFilePath, StandardCharsets.UTF_8))
        {
            // Skip first few lines if the config says so.
            final int linesToSkip = config.getSkipLines();
            final int nSkippedLines =
                    CsvAddTasksAction.skipFirstLines(linesToSkip, br);

            int nReadLines = 0;
            final List<Task> tasks = new ArrayList<>();
            if (nSkippedLines == linesToSkip)
            {
                System.out.println("Skipped lines: " + nSkippedLines);

                final String delimiter = config.getDelimiter();

                for (String line = br.readLine(); line != null;
                     line = br.readLine())
                {
                    nReadLines++;

                    final String[] columns = line.split(delimiter, -1);
                    final Task task = new Task(columns, config);
                    tasks.add(task);
                }
            }

            System.out.println("Read " + nReadLines + " lines.");
            return tasks;
        }
    }

    /**
     * Skips over a number of lines in the beginning of the input of a
     * BufferedReader.
     *
     * @param nLines the number of lines to skip over
     * @param br     BufferedReader to read input from
     *
     * @return The number of lines that were skipped over.
     *
     * @throws IOException if the BufferedReader couldn't read lines
     *
     * @since 0.1.0
     */
    private static int skipFirstLines(final int nLines,
                                      final BufferedReader br)
            throws IOException
    {
        int nSkipped = 0;

        for (int line = 0; line < nLines; line++)
        {
            br.readLine();
            nSkipped++;
        }

        return nSkipped;
    }

    /**
     * Creates a new timeline by requesting one from the Remember The Milk API,
     * and stores it within this session object.
     *
     * Note that this action will invalidate the last timeline, making all
     * actions made before calling this method undoable.
     *
     * @since 0.1.0
     */
    @SuppressWarnings({"JavaDoc", "MethodWithTooExceptionsDeclared"})
    private static void createTimeline(final AppConfig config,
                                       final Session session)
            throws DocumentException, NoSuchAlgorithmException,
            MalformedURLException, IOException
    {
        if (!session.hasTimeline())
        {
            final CreateTimeline createTimeline = new CreateTimeline(config);
            final String timeline = createTimeline.getTimeline();
            session.setTimeline(timeline);
        }
    }
}
