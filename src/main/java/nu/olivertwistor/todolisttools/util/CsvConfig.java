package nu.olivertwistor.todolisttools.util;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Configuration values from a CSV parsing config file are read via this class.
 *
 * @since  0.1.0
 */
@SuppressWarnings({"ClassWithoutLogger", "PublicMethodWithoutLogging"})
public final class CsvConfig extends Config
{
    public CsvConfig(final URL url)
            throws InvalidFileFormatException, IOException, URISyntaxException
    {
        super(CsvConfig.class.getResource("/csv.defaults.cfg"));
    }

    public CsvConfig(final File file) throws IOException
    {
        super(file);
    }

    public String getDelimiter()
    {
        return this.get("csv", "delimiter");
    }

    /**
     *
     * @return
     *
     * @throws NumberFormatException
     */
    @SuppressWarnings("JavaDoc")
    public int getSkipLines()
    {
        final String rawString = this.get("csv", "skip.lines");
        return Integer.parseInt(rawString);
    }

    @SuppressWarnings("JavaDoc")
    public int getColumnIndexName()
    {
        final String rawString = this.get("task", "column.index.name");
        return Integer.parseInt(rawString);
    }

    public String getDefaultValueName()
    {
        return this.get("task", "default.value.name");
    }

    @SuppressWarnings("JavaDoc")
    public int getColumnIndexUrl()
    {
        final String rawString = this.get("task", "column.index.url");
        return Integer.parseInt(rawString);
    }

    public String getDefaultValueUrl()
    {
        return this.get("task", "default.value.url");
    }

    @SuppressWarnings("JavaDoc")
    public int getColumnIndexStart()
    {
        final String rawString = this.get("task", "column.index.start");
        return Integer.parseInt(rawString);
    }

    public String getDefaultValueStart()
    {
        return this.get("task", "default.value.start");
    }

    @SuppressWarnings("JavaDoc")
    public int getColumnIndexDue()
    {
        final String rawString = this.get("task", "column.index.due");
        return Integer.parseInt(rawString);
    }

    public String getDefaultValueDue()
    {
        return this.get("task", "default.value.due");
    }

    @SuppressWarnings("JavaDoc")
    public int getColumnIndexRepeat()
    {
        final String rawString = this.get("task", "column.index.repeat");
        return Integer.parseInt(rawString);
    }

    public String getDefaultValueRepeat()
    {
        return this.get("task", "default.value.repeat");
    }

    @SuppressWarnings("JavaDoc")
    public int getColumnIndexLocation()
    {
        final String rawString = this.get("task", "column.index.location");
        return Integer.parseInt(rawString);
    }

    public String getDefaultValueLocation()
    {
        return this.get("task", "default.value.location");
    }

    @SuppressWarnings("JavaDoc")
    public int getColumnIndexPriority()
    {
        final String rawString = this.get("task", "column.index.priority");
        return Integer.parseInt(rawString);
    }

    public String getDefaultValuePriority()
    {
        return this.get("task", "default.value.priority");
    }

    @SuppressWarnings("JavaDoc")
    public int getColumnIndexList()
    {
        final String rawString = this.get("task", "column.index.list");
        return Integer.parseInt(rawString);
    }

    public String getDefaultValueList()
    {
        return this.get("task", "default.value.list");
    }

    @SuppressWarnings("JavaDoc")
    public int getColumnIndexTags()
    {
        final String rawString = this.get("task", "column.index.tags");
        return Integer.parseInt(rawString);
    }

    public String[] getDefaultValueTags()
    {
        final String allTags = this.get("task", "default.value.tags");
        return allTags.split(",", -1);
    }

    @SuppressWarnings("JavaDoc")
    public int getColumnIndexTimeEstimate()
    {
        final String rawString = this.get("task", "column.index.time.estimate");
        return Integer.parseInt(rawString);
    }

    public String getDefaultValueTimeEstimate()
    {
        return this.get("task", "default.value.time.estimate");
    }

    @SuppressWarnings("JavaDoc")
    public int getColumnIndexComments()
    {
        final String rawString = this.get("task", "column.index.comments");
        return Integer.parseInt(rawString);
    }

    public String getDefaultValueComments()
    {
        return this.get("task", "default.value.comments");
    }
}
