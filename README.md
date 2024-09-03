# File Synchronizer

## Overview
File Synchronizer is a Java-based tool that allows for asynchronous copying of files from a source directory to a destination directory. It includes functionality to ignore certain files and directories based on a .gitignore-like file.

## Features
- Asynchronous file copying
- Directory structure preservation
- File comparison to avoid unnecessary copying
- Ignore files and directories based on a specified ignore file
- Synchronization reporting

## Project Structure

***src/main/java/com/ebblius/FileCopier.java*** --> Handles the copying of individual files. 

***src/main/java/com/ebblius/AsyncFileSynchronizer.java*** --> Manages the asynchronous synchronization of files. 

***src/main/java/com/ebblius/FileComparator.java*** --> Compares files to avoid unnecessary copying. 

***src/main/java/com/ebblius/SyncReport.java*** --> Generates synchronization reports. 

***src/main/java/com/ebblius/FileSynchronizer.java*** --> Manages the synchronous synchronization of files. 

***src/main/java/com/ebblius/Scheduler.java*** --> Schedules synchronization tasks. 

***src/main/java/com/ebblius/Logger.java*** --> Handles logging. 

***src/main/java/com/ebblius/IgnoreFileParser.java*** --> Parses the ignore file. 

## Prerequisites
- Java 11 or higher
- Maven 3.6.0 or higher

## Example
To synchronize files from the current directory to __/home/oem/Desktop/destination__ while ignoring files specified in __.gitignore__:

```
    AsyncFileSynchronizer synchronizer = new AsyncFileSynchronizer(
            Paths.get(""),
            Paths.get("/home/oem/Desktop/destination"),
            Paths.get(".gitignore")
    );
    synchronizer.startAsyncSync();
```

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
