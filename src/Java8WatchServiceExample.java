import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

public class Java8WatchServiceExample {
    private final WatchService watcher;
    private final Map<WatchKey, Path> dirWatchers;

    public Java8WatchServiceExample(Path dir) throws IOException {
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            throw new IOException("Directory does not exist or is inaccessible: " + dir);
        }
        this.watcher = FileSystems.getDefault().newWatchService();
        this.dirWatchers = new HashMap<>();
        scanAndRegisterDirectories(dir);
    }

    private void registerDirWatchers(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        dirWatchers.put(key, dir);
    }

    private void scanAndRegisterDirectories(final Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                registerDirWatchers(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void processEvents() {
        while (true) {
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                Thread.currentThread().interrupt();
                return;
            }

            Path dir = dirWatchers.get(key);
            if (dir == null) continue;

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (event.context() instanceof Path) {
                    Path name = (Path) event.context();
                    Path child = dir.resolve(name);
                    System.out.format("%s: %s\n", kind.name(), child);

                    if (kind == ENTRY_CREATE) {
                        try {
                            if (Files.isDirectory(child)) scanAndRegisterDirectories(child);
                        } catch (IOException x) {
                            System.err.println("Error during directory scanning: " + x.getMessage());
                        }
                    } else if (kind == ENTRY_DELETE) {
                        dirWatchers.remove(key);
                    }
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                dirWatchers.remove(key);
                if (dirWatchers.isEmpty()) break;
            }
        }
    }
}
