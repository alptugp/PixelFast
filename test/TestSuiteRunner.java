import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import picture.PictureProcessorTest;

public class TestSuiteRunner {

  public static void main(String[] args) {
    Result result = JUnitCore.runClasses(PictureProcessorTest.class);
    for (Failure failure : result.getFailures()) {
      System.out.println(failure.toString());
    }
  }
}
