import org.junit.Assert;
import org.junit.Test;
import java.nio.file.*;
import java.io.IOException;
import java.util.stream.IntStream;

import static java.nio.file.StandardWatchEventKinds.*;

public class NIOFileAPITest {
    private  final String HOME = System.getProperty("user.home");
    private  final String PLAY_WITH_NIO = "TempPlayGround";

    @Test
    public void givenPathWhenCheckedThenConfirm() throws IOException {
        Path homePath = Paths.get(HOME);
        Assert.assertTrue(Files.exists(homePath));

        Path playPath = Paths.get(HOME, PLAY_WITH_NIO);
        if (Files.exists(playPath)) FileUtils.deleteFiles(playPath.toFile());
        Assert.assertTrue(Files.notExists(playPath));

        Files.createDirectory(playPath);
        Assert.assertTrue(Files.exists(playPath));

        IntStream.range(1, 10).forEach(counter -> {
            Path tempFile = Paths.get(playPath.toString(), "temp" + counter);
            Assert.assertTrue(Files.notExists(tempFile));
            try {
                Files.createFile(tempFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Assert.assertTrue(Files.exists(tempFile));
        });

        Files.list(playPath).filter(Files::isRegularFile).forEach(System.out::println);
        Files.newDirectoryStream(playPath).forEach(System.out::println);
    }

    @Test
    public void givenADirectoryWhenWatchedListsAllTheActivities() throws IOException {
        Path dir = Paths.get(HOME+ "/"+ PLAY_WITH_NIO);
        Files.list(dir).filter(Files::isRegularFile).forEach(System.out::println);
        new Java8WatchServiceExample(dir).processEvents();
    }
}
