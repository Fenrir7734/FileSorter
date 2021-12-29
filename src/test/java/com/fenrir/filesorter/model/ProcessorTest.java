package com.fenrir.filesorter.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorTest {
    @TempDir
    Path tempDir;
    List<Path> fileStructure;

    @BeforeEach
    public void init() {

    }
}