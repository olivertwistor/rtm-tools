package nu.olivertwistor.todolisttools.models;

import ch.rfin.util.Pair;
import nu.olivertwistor.todolisttools.util.CsvConfig;
import org.jetbrains.annotations.NonNls;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class models the task in Remember The Milk. It has a name, URL, start
 * date, due date, repeat interval, location, priority, list, tag(s), time
 * estimate and comments. The method {@link #toSmartAdd()} constructs a smart
 * add string representing this task. For example "Buy milk ^Tomorrow" for a
 * task with name "Buy milk" and due date "Tomorrow".
 *
 * @since 0.1.0
 */
@SuppressWarnings({"NonFinalFieldReferenceInEquals", "NonFinalFieldReferencedInHashCode", "ClassWithoutLogger", "PublicMethodWithoutLogging"})
public final class Task
{
    private static final Pattern REMOVE_EXTRA_SPACES = Pattern.compile("  +");

    private final Pair<String, String> name;
    private Pair<String, String> url = Pair.of("", "");
    private Pair<String, String> start = Pair.of("", "");
    private Pair<String, String> due = Pair.of("", "");
    private Pair<String, String> repeat = Pair.of("", "");
    private Pair<String, String> location = Pair.of("", "");
    private Pair<String, String> priority = Pair.of("", "");
    private Pair<String, String> list = Pair.of("", "");
    private final Set<Pair<String, String>> tags = new HashSet<>();
    private Pair<String, String> timeEstimate = Pair.of("", "");
    private Pair<String, String> comments = Pair.of("", "");

    /**
     * Creates a new Task object with the only required attribute: name.
     *
     * @param name task name, for example "Buy milk"
     *
     * @since 0.1.0
     */
    public Task(final String name)
    {
        this.name = Pair.of(SmartAddPrefixes.NAME.getPrefix(), name);
    }

    /**
     * Creates a new Task object based on a string array with data and a
     * configuration object, detailing which array elements that correspond
     * with which Task properties.
     *
     * @param data   string array of data to fill into the Task properties
     * @param config configuration object detailing which array elements that
     *               correspond to which Task properties
     *
     * @throws IndexOutOfBoundsException if the configuration object points to
     *                                   any array element that doesn't exist
     *                                   in the data string array
     *
     * @since 0.1.0
     */
    @SuppressWarnings({"OverlyComplexMethod", "OverlyLongMethod"})
    public Task(final String[] data, final CsvConfig config)
    {
        final int columnIndexName = config.getColumnIndexName();
        this.name = Pair.of(SmartAddPrefixes.NAME.getPrefix(),
                data[columnIndexName]);

        final int columnIndexUrl = config.getColumnIndexUrl();
        if (columnIndexUrl > 0)
        {
            final String urlData = data[columnIndexUrl];
            this.url = Pair.of(SmartAddPrefixes.URL.getPrefix(), urlData);
        }

        final int columnIndexStart = config.getColumnIndexStart();
        if (columnIndexStart > 0)
        {
            final String startData = data[columnIndexStart];
            this.start = Pair.of(SmartAddPrefixes.START.getPrefix(), startData);
        }

        final int columnIndexDue = config.getColumnIndexDue();
        if (columnIndexDue > 0)
        {
            this.due = Pair.of(SmartAddPrefixes.DUE.getPrefix(),
                    data[columnIndexDue]);
        }

        final int columnIndexRepeat = config.getColumnIndexRepeat();
        if (columnIndexRepeat > 0)
        {
            this.repeat = Pair.of(SmartAddPrefixes.REPEAT.getPrefix(),
                    data[columnIndexRepeat]);
        }

        final int columnIndexLocation = config.getColumnIndexLocation();
        if (columnIndexLocation > 0)
        {
            this.location = Pair.of(SmartAddPrefixes.LOCATION.getPrefix(),
                    data[columnIndexLocation]);
        }

        final int columnIndexPriority = config.getColumnIndexPriority();
        if (columnIndexPriority > 0)
        {
            this.priority = Pair.of(SmartAddPrefixes.PRIORITY.getPrefix(),
                    data[columnIndexPriority]);
        }

        final int columnIndexList = config.getColumnIndexList();
        if (columnIndexList > 0)
        {
            this.list = Pair.of(SmartAddPrefixes.LIST.getPrefix(),
                    data[columnIndexList]);
        }

        final int columnIndexTags = config.getColumnIndexTags();
        if (columnIndexTags > 0)
        {
            final String[] tags = data[columnIndexTags].split(",", -1);
            for (final String tag : tags)
            {
                this.tags.add(Pair.of(SmartAddPrefixes.TAG.getPrefix(), tag));
            }
        }

        final int columnIndexTimeEstimate = config.getColumnIndexTimeEstimate();
        if (columnIndexTimeEstimate > 0)
        {
            this.timeEstimate = Pair.of(
                    SmartAddPrefixes.TIME_ESTIMATE.getPrefix(),
                    data[columnIndexTimeEstimate]);
        }

        final int columnIndexComments = config.getColumnIndexComments();
        if (columnIndexComments > 0)
        {
            this.comments = Pair.of(SmartAddPrefixes.COMMENTS.getPrefix(),
                    data[columnIndexComments]);
        }
    }

    void setUrl(final String url)
    {
        this.url = Pair.of(SmartAddPrefixes.URL.getPrefix(), url);
    }

    void setStart(final String start)
    {
        this.start = Pair.of(SmartAddPrefixes.START.getPrefix(), start);
    }

    void setDue(final String due)
    {
        this.due = Pair.of(SmartAddPrefixes.DUE.getPrefix(), due);
    }

    void setRepeat(final String repeat)
    {
        this.repeat = Pair.of(SmartAddPrefixes.REPEAT.getPrefix(), repeat);
    }

    void setLocation(final String location)
    {
        this.location = Pair.of(
                SmartAddPrefixes.LOCATION.getPrefix(), location);
    }

    void setPriority(final String priority)
    {
        this.priority = Pair.of(
                SmartAddPrefixes.PRIORITY.getPrefix(), priority);
    }

    public void setList(final String list)
    {
        this.list = Pair.of(SmartAddPrefixes.LIST.getPrefix(), list);
    }

    /**
     * Adds a tag to the task.
     *
     * @param tag tag
     *
     * @since 0.1.0
     */
    void addTag(final String tag)
    {
        this.tags.add(Pair.of(SmartAddPrefixes.TAG.getPrefix(), tag));
    }

    /**
     * Removes a tag.
     *
     * @param tag the tag to remove
     *
     * @since 0.1.0
     */
    @SuppressWarnings("unused")
    public void removeTag(final String tag)
    {
        this.tags.remove(Pair.of(SmartAddPrefixes.TAG.getPrefix(), tag));
    }

    void setTimeEstimate(final String timeEstimate)
    {
        this.timeEstimate = Pair.of(
                SmartAddPrefixes.TIME_ESTIMATE.getPrefix(), timeEstimate);
    }

    void setComments(final String comments)
    {
        this.comments = Pair.of(
                SmartAddPrefixes.COMMENTS.getPrefix(), comments);
    }

    /**
     * Constructs a smart add string from all the task properties, for example
     * "Buy milk ^Tomorrow @Work" for a task with name "Buy milk", due date
     * "Tomorrow" and location "Work". The prefixes ^ and @ is added after
     * setting the various class members; they are not in the class members
     * themselves.
     *
     * Note that the constructed string will have two or more spaces converted
     * to one.
     *
     * @return A smart add string.
     *
     * @since 0.1.0
     */
    public String toSmartAdd()
    {
        final StringBuilder stringBuilder = new StringBuilder()
                .append(this.name._1).append(this.name._2).append(' ')
                .append(this.url._1).append(this.url._2).append(' ')
                .append(this.start._1).append(this.start._2).append(' ')
                .append(this.due._1).append(this.due._2).append(' ')
                .append(this.repeat._1).append(this.repeat._2).append(' ')
                .append(this.location._1).append(this.location._2).append(' ')
                .append(this.priority._1).append(this.priority._2).append(' ')
                .append(this.list._1).append(this.list._2).append(' ');
        for (final Pair<String, String> tag : this.tags)
        {
            stringBuilder.append(tag._1).append(tag._2).append(' ');
        }
        stringBuilder.append(this.timeEstimate._1).append(this.timeEstimate._2)
                .append(' ')
                .append(this.comments._1).append(this.comments._2);

        final String beforeTrim = stringBuilder.toString();
        final String afterTrim = beforeTrim.trim();
        final Matcher extraSpaces = Task.REMOVE_EXTRA_SPACES.matcher(afterTrim);
        final String afterReplace = extraSpaces.replaceAll(" ");

        return afterReplace;
    }

    @Override
    public @NonNls String toString()
    {
        return "Task{" +
                "name=" + this.name +
                ", url=" + this.url +
                ", start=" + this.start +
                ", due=" + this.due +
                ", repeat=" + this.repeat +
                ", location=" + this.location +
                ", priority=" + this.priority +
                ", list=" + this.list +
                ", tags=" + this.tags +
                ", timeEstimate=" + this.timeEstimate +
                ", comments=" + this.comments +
                '}';
    }

    @SuppressWarnings("OverlyComplexMethod")
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if ((obj == null) || (this.getClass() != obj.getClass()))
        {
            return false;
        }
        final Task task = (Task) obj;
        return this.name.equals(task.name) &&
                Objects.equals(this.url, task.url) &&
                Objects.equals(this.start, task.start) &&
                Objects.equals(this.due, task.due) &&
                Objects.equals(this.repeat, task.repeat) &&
                Objects.equals(this.location, task.location) &&
                Objects.equals(this.priority, task.priority) &&
                Objects.equals(this.list, task.list) &&
                this.tags.equals(task.tags) &&
                Objects.equals(this.timeEstimate, task.timeEstimate) &&
                Objects.equals(this.comments, task.comments);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.name, this.url, this.start, this.due,
                this.repeat, this.location, this.priority, this.list,
                this.tags, this.timeEstimate, this.comments);
    }
}
