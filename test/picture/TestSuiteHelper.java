package picture;

import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.util.Arrays;

public class TestSuiteHelper {

  public static Picture runMain(TemporaryFolder folder, String... argumentList) throws IOException {

    String outputFile = folder.newFile("out.png").getAbsolutePath();

    PictureProcessor.main(appendTo(argumentList, outputFile));

    return new Picture(outputFile);
  }

  private static String[] appendTo(String[] argumentList, String outputFile) {
    String[] arguments = Arrays.copyOf(argumentList, argumentList.length + 1);
    arguments[arguments.length - 1] = outputFile;
    return arguments;
  }
}
