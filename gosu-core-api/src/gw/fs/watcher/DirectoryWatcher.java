/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs.watcher;

import gw.lang.UnstableAPI;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@UnstableAPI
public class DirectoryWatcher {

  private WatchService _watchService;
  private Map<WatchKey, Path> _watchedDirectories;

  public DirectoryWatcher() {
    try {
      _watchService = FileSystems.getDefault().newWatchService();
      _watchedDirectories = new HashMap<WatchKey, Path>();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void watchDirectoryTree(Path dir) {
    try {
      if (Files.exists(dir)) {
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
          @Override
          public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            watchDirectory(dir);
            return FileVisitResult.CONTINUE;
          }
        });
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void watchDirectory(Path dir) throws IOException {
    try {
      WatchKey key = dir.register(_watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
      _watchedDirectories.put(key, dir);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static enum FileEvent { CREATE, MODIFY, DELETE }

  public Map<Path, FileEvent> getChangesSinceLastTime() {
    Map<Path, FileEvent> events = new HashMap<Path, FileEvent>();
    while (true) {
      WatchKey key = _watchService.poll();
      if (key == null) {
        break;
      }

      Path dir = _watchedDirectories.get(key);
      if (dir == null) {
        throw new IllegalStateException("Got a WatchKey for a Path that we didn't watch");
      }

      for (WatchEvent event : key.pollEvents()) {
        Object context = event.context();
        if (context instanceof Path) {
          Path changedFile = dir.resolve((Path) context);

          WatchEvent.Kind eventKind = event.kind();
          if (eventKind == StandardWatchEventKinds.ENTRY_CREATE) {
            // If a create follows a delete, it means the editor deleted and re-created the file, and
            // who knows what the contents are now, so it should be treated as a modification
            if (events.get(changedFile) == FileEvent.DELETE) {
              events.put(changedFile, FileEvent.MODIFY);
            } else {
              events.put(changedFile, FileEvent.CREATE);
            }
          } else if (eventKind == StandardWatchEventKinds.ENTRY_MODIFY) {
            // If a file is already marked as CREATE, leave it as CREATE; if it's marked as DELETE, that's totally
            // invalid, so just ignore that.  If it's marked as MODIFY, no need to change it.  So only put something
            // in the map if it's not already in there
            if (!events.containsKey(changedFile)) {
              events.put(changedFile, FileEvent.MODIFY);
            }
          } else if (eventKind == StandardWatchEventKinds.ENTRY_DELETE) {
            if (events.get(changedFile) == FileEvent.CREATE) {
              // If the file was created, then deleted, then just ignore it entirely and remove the event
              events.remove(changedFile);
            } else {
              events.put(changedFile, FileEvent.DELETE);
            }
          }
        }
      }

      if (!key.reset()) {
        _watchedDirectories.remove(key);
      }
    }

    // TODO - AHK - Watch any newly-created directories

    // TODO - AHK - Probably Coerce this into some more meaningful format, such that it takes into account
    // the root directory that the thing falls under, or something?
    return events;
  }
}
