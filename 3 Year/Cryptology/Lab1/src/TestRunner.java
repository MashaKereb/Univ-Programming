/**
 * Created by Masha Kereb on 20-Dec-16.
 */
import org.junit.runner.*;
import org.junit.runner.notification.Failure;
public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(JunitTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }
}

