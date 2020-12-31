package nu.olivertwistor.todolisttools.models;

import nu.olivertwistor.todolisttools.util.CsvConfig;
import org.hamcrest.CoreMatchers;
import org.jetbrains.annotations.NonNls;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@link Task} class.
 *
 * @since 0.1.0
 */
@SuppressWarnings({"StringConcatenation", "HardcodedFileSeparator"})
public final class TaskTest
{
    /**
     * Asserts that when creating a task setting all class members, the correct
     * smart add string is constructed.
     *
     * @since 0.1.0
     */
    @SuppressWarnings({"HardCodedStringLiteral", "HardcodedFileSeparator"})
    @Test
    public void When_SmartAddTasksUsingAllSetters_Then_CorrectStringIsReturned()
    {
        final Task task = new Task("Buy milk");
        task.setUrl("https://grocerystore.com");
        task.setStart("Tomorrow");
        task.setDue("Saturday");
        task.setRepeat("After 1 week");
        task.setLocation("Work");
        task.setPriority("2");
        task.setList("Household");
        task.addTag("car");
        task.addTag("money");
        task.setTimeEstimate("30 minutes");
        task.setComments("Pick low fat if available.");

        // We need to create two targets, each with a permutation of the tags
        // because the SmartAddBuilder uses a Set for tags, and Sets are
        // unordered.

        final String[] targets = {
                "Buy milk https://grocerystore.com ~Tomorrow ^Saturday " +
                        "*After 1 week @Work !2 #Household #car #money " +
                        "=30 minutes //Pick low fat if available.",
                "Buy milk https://grocerystore.com ~Tomorrow ^Saturday " +
                        "*After 1 week @Work !2 #Household #money #car " +
                        "=30 minutes //Pick low fat if available."
        };
        Assert.assertThat(
                "Parameterized task creation should equal smart add string.",
                task.toSmartAdd(),
                CoreMatchers.anyOf(
                        CoreMatchers.is(targets[0]),
                        CoreMatchers.is(targets[1])));
    }

    /**
     * Asserts that when creating a task setting some class members, the
     * correct smart add string is constructed.
     *
     * @since 0.1.0
     */
    @Test
    public void When_SmartAddTasksUsingSomeSetters_Then_CorrectStringIsReturned()
    {
        final Task task = new Task("Buy milk"); //NON-NLS
        task.setUrl("https://grocerystore.com");
        task.setDue("4 days"); //NON-NLS
        task.setPriority("2");
        task.setList("Household"); //NON-NLS
        task.setTimeEstimate("30 minutes"); //NON-NLS

        @NonNls final String target = "Buy milk https://grocerystore.com " +
                "^4 days !2 #Household =30 minutes";
        Assert.assertThat(
                "Parameterized task creation should equal smart add string.",
                task.toSmartAdd(), CoreMatchers.is(target));
    }
}
