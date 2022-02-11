package com.fenrir.filesorter.model;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.exceptions.SortConfigurationException;
import com.fenrir.filesorter.model.rule.Rule;
import com.fenrir.filesorter.model.rule.RuleGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {

    @Test
    public void addSourcePathsShouldAddPathToList() {
        Configuration configuration = new Configuration();
        Path path1 = Path.of("/home/user/Documents");
        Path path2 = Path.of("/home/user/Downloads");
        Path path3 = Path.of("/home/user/Desktop");
        configuration.addSourcePaths(List.of(path1));
        configuration.addSourcePaths(List.of(path2, path3));
        assertEquals(List.of(path1, path2, path3), configuration.getSourcePaths());
    }

    @Test
    public void addSourcePathsShouldNormalizePath() {
        Configuration configuration = new Configuration();
        configuration.addSourcePaths(List.of(Path.of("/home/../home/user/.//Documents/Dir/../")));
        assertEquals(List.of(Path.of("/home/user/Documents")), configuration.getSourcePaths());
    }

    @Test
    public void addSourcePathsShouldAddOnlyUniqPath() {
        Configuration configuration = new Configuration();
        Path path1 = Path.of("/home/user/Documents");
        Path path2 = Path.of("/home/../home/user/.//Documents/Dir/../"); //same as /home/user/Documents
        configuration.addSourcePaths(List.of(path1, path1, path2));
        assertEquals(List.of(path1), configuration.getSourcePaths());
    }

    @Test
    public void getRuleGroupsShouldReturnEmptyListWhenNoRuleGroupExists() {
        Configuration configuration = new Configuration();
        assertTrue(configuration.getRuleGroups().isEmpty());
    }

    @Test
    public void getRuleGroupsNamesShouldReturnEmptyListWhenNoRuleGroupExists() {
        Configuration configuration = new Configuration();
        assertTrue(configuration.getNamedRuleGroups().isEmpty());
    }

    @Nested
    class TestForConfigurationObjectWithData {
        private Configuration configuration;
        private Path targetPath;
        private Path sourcePath1;
        private Path sourcePath2;
        private Path sourcePath3;
        private String ruleGroup1Name;
        private RuleGroup ruleGroup1;
        private String ruleGroup2Name;
        private RuleGroup ruleGroup2;

        @BeforeEach
        public void initConfigurationObject() throws ExpressionFormatException {
            targetPath = Path.of("/home/user/Document/Target");

            sourcePath1 = Path.of("/home/user/Documents");
            sourcePath2 = Path.of("/home/user/Downloads");
            sourcePath3 = Path.of("/home/user/Desktop");
            List<Path> sourcePaths = List.of(sourcePath1, sourcePath2, sourcePath3);

            ruleGroup1Name = "Rule Group 1";
            ruleGroup1 = new RuleGroup();
            ruleGroup1.setSortRule(new Rule("%(DIM)"));
            ruleGroup1.setRenameRule(new Rule("%(FIX)"));

            ruleGroup2Name = "Rule Group 2";
            ruleGroup2 = new RuleGroup();
            ruleGroup2.setSortRule(new Rule("%(DIM)%(/)%(CAT)"));
            ruleGroup2.setRenameRule(new Rule("%(FIX)"));

            configuration = new Configuration();
            configuration.setSortAction(Sorter.Action.COPY);
            configuration.setTargetRootDir(targetPath);
            configuration.addSourcePaths(sourcePaths);
            configuration.addNamedRuleGroup(ruleGroup1Name, ruleGroup1);
            configuration.addNamedRuleGroup(ruleGroup2Name, ruleGroup2);
        }

        @Test
        public void removeSourcePathsShouldRemoveAllPathsFromList() {
            List<Path> pathsToRemove = List.of(sourcePath1, sourcePath2);
            configuration.removeSourcePaths(pathsToRemove);
            assertEquals(List.of(sourcePath3), configuration.getSourcePaths());
        }

        @Test
        public void removeSourcePathsShouldIgnorePathThatNotExistInSourcePathList() {
            List<Path> pathsToRemove = List.of(sourcePath1, sourcePath2, Path.of("/home/user/diff/path"));
            configuration.removeSourcePaths(pathsToRemove);
            assertEquals(List.of(sourcePath3), configuration.getSourcePaths());
        }

        @Test
        public void addNamedRuleGroupShouldAddRuleGroup() {
            String nameOfNewRuleGroup = "New Rule Group";
            RuleGroup newRuleGroup = new RuleGroup();
            configuration.addNamedRuleGroup(nameOfNewRuleGroup, newRuleGroup);

            ObservableList<Pair<String, RuleGroup>> expectedRuleGroups = FXCollections.observableArrayList(
                    new Pair<>(ruleGroup1Name, ruleGroup1),
                    new Pair<>(ruleGroup2Name, ruleGroup2),
                    new Pair<>(nameOfNewRuleGroup, newRuleGroup)
            );
            assertEquals(expectedRuleGroups, configuration.getNamedRuleGroups());
        }

        @Test
        public void addNamedRuleGroupShouldNotAddRuleGroupWithNameThatAlreadyExistsInsideConfiguration() {
            RuleGroup newRuleGroup = new RuleGroup();
            configuration.addNamedRuleGroup(ruleGroup1Name, newRuleGroup);

            ObservableList<Pair<String, RuleGroup>> expectedRuleGroups = FXCollections.observableArrayList(
                    new Pair<>(ruleGroup1Name, ruleGroup1),
                    new Pair<>(ruleGroup2Name, ruleGroup2)
            );
            assertEquals(expectedRuleGroups, configuration.getNamedRuleGroups());
        }

        @Test
        public void removeRuleGroupShouldRemoveGivenNamedRuleGroup() {
            Pair<String, RuleGroup> namedRuleGroupToRemove = new Pair<>(ruleGroup1Name, ruleGroup1);
            configuration.removeRuleGroup(namedRuleGroupToRemove);

            ObservableList<Pair<String, RuleGroup>> expectedRuleGroups = FXCollections.observableArrayList(
                    new Pair<>(ruleGroup2Name, ruleGroup2)
            );
            assertEquals(expectedRuleGroups, configuration.getNamedRuleGroups());
        }

        @Test
        public void removeRuleGroupShouldNotRemoveAnyRuleGroupWhenRuleGroupWithGivenNameNotExists() {
            Pair<String, RuleGroup> namedRuleGroupToRemove = new Pair<>("Incorrect name", ruleGroup1);
            configuration.removeRuleGroup(namedRuleGroupToRemove);

            ObservableList<Pair<String, RuleGroup>> expectedRuleGroups = FXCollections.observableArrayList(
                    new Pair<>(ruleGroup1Name, ruleGroup1),
                    new Pair<>(ruleGroup2Name, ruleGroup2)
            );
            assertEquals(expectedRuleGroups, configuration.getNamedRuleGroups());
        }

        @Test
        public void removeRuleGroupShouldNotRemoveAnyRuleGroupWhenGivenRuleGroupNotExists() {
            Pair<String, RuleGroup> namedRuleGroupToRemove = new Pair<>(ruleGroup1Name, new RuleGroup());
            configuration.removeRuleGroup(namedRuleGroupToRemove);

            ObservableList<Pair<String, RuleGroup>> expectedRuleGroups = FXCollections.observableArrayList(
                    new Pair<>(ruleGroup1Name, ruleGroup1),
                    new Pair<>(ruleGroup2Name, ruleGroup2)
            );
            assertEquals(expectedRuleGroups, configuration.getNamedRuleGroups());
        }

        @Test
        public void getRuleGroupsShouldReturnRuleGroupWithoutTheirNames() {
            List<RuleGroup> expectedRuleGroup = List.of(ruleGroup1, ruleGroup2);
            assertEquals(expectedRuleGroup, configuration.getRuleGroups());
        }

        @Test
        public void getRuleGroupsNamesShouldReturnNamesOfRuleGroups() {
            List<String> expectedRuleGroupNames = List.of(ruleGroup1Name, ruleGroup2Name);
            assertEquals(expectedRuleGroupNames, configuration.getRuleGroupsNames());
        }

        @Test
        public void getRuleGroupShouldReturnRuleGroupWithGivenName() {
            Optional<RuleGroup> actualRuleGroup = configuration.getRuleGroup(ruleGroup1Name);
            assertTrue(actualRuleGroup.isPresent());
            assertEquals(ruleGroup1, actualRuleGroup.get());
        }

        @Test
        public void getRuleGroupShouldReturnEmptyOptionalType() {
            Optional<RuleGroup> actualRuleGroup = configuration.getRuleGroup("Incorrect name");
            assertTrue(actualRuleGroup.isEmpty());
        }
    }

    @Nested
    class TestValidate {
        @TempDir
        Path tempDir;

        private Path targetDirPath;
        private Path sourceDirPath;

        private String ruleGroupName;
        private RuleGroup ruleGroup;

        @BeforeEach
        public void init() throws ExpressionFormatException {
            targetDirPath = FileUtils.createDirectory(tempDir, "target");
            sourceDirPath = FileUtils.createDirectory(tempDir, "source");

            ruleGroupName = "Rule Group 1";
            ruleGroup = new RuleGroup();
            ruleGroup.setSortRule(new Rule("%(DIM)"));
            ruleGroup.setRenameRule(new Rule("%(FIX)"));
        }

        @Test
        public void validateShouldThrowSortConfigurationExceptionIfNoTargetDirectoryHasBeenSpecified() {
            Configuration configuration = new Configuration();
            configuration.addSourcePaths(List.of(sourceDirPath));
            configuration.addNamedRuleGroup(ruleGroupName, ruleGroup);
            configuration.setSortAction(Sorter.Action.COPY);

            assertThrows(
                    SortConfigurationException.class,
                    configuration::validate
            );
        }

        @Test
        public void validateShouldThrowSortConfigurationExceptionIfGivenPathToTargetDirectoryIsNotDirectory() {
            Configuration configuration = new Configuration();
            configuration.addSourcePaths(List.of(sourceDirPath));
            configuration.addNamedRuleGroup(ruleGroupName, ruleGroup);
            configuration.setSortAction(Sorter.Action.COPY);

            Path invalidTargetDirectoryPath = FileUtils.createFile(tempDir, "file.txt");
            configuration.setTargetRootDir(invalidTargetDirectoryPath);

            assertThrows(
                    SortConfigurationException.class,
                    configuration::validate
            );
        }

        @Test
        public void validateShouldThrowSortConfigurationExceptionIfTargetDirectoryNotExists() {
            Configuration configuration = new Configuration();
            configuration.addSourcePaths(List.of(sourceDirPath));
            configuration.addNamedRuleGroup(ruleGroupName, ruleGroup);
            configuration.setSortAction(Sorter.Action.COPY);

            configuration.setTargetRootDir(Path.of("Incorrect"));

            assertThrows(
                    SortConfigurationException.class,
                    configuration::validate
            );
        }

        @Test
        public void validateShouldThrowSortConfigurationExceptionIfTargetDirectoryIsNotEmpty() {
            Configuration configuration = new Configuration();
            configuration.setTargetRootDir(targetDirPath);
            configuration.addSourcePaths(List.of(sourceDirPath));
            configuration.addNamedRuleGroup(ruleGroupName, ruleGroup);
            configuration.setSortAction(Sorter.Action.COPY);

            FileUtils.createFile(targetDirPath, "file.txt");

            assertThrows(
                    SortConfigurationException.class,
                    configuration::validate
            );
        }

        @Test
        public void validateShouldThrowSortConfigurationExceptionIfNoSourcePathWasGiven() {
            Configuration configuration = new Configuration();
            configuration.setTargetRootDir(targetDirPath);
            configuration.addNamedRuleGroup(ruleGroupName, ruleGroup);
            configuration.setSortAction(Sorter.Action.COPY);

            assertThrows(
                    SortConfigurationException.class,
                    configuration::validate
            );
        }

        @Test
        public void validateShouldThrowSortConfigurationExceptionIfNoRuleGroupWasGiven() {
            Configuration configuration = new Configuration();
            configuration.setTargetRootDir(targetDirPath);
            configuration.addSourcePaths(List.of(sourceDirPath));
            configuration.setSortAction(Sorter.Action.COPY);

            assertThrows(
                    SortConfigurationException.class,
                    configuration::validate
            );
        }

        @Test
        public void validateShouldThrowSortConfigurationExceptionIfGivenRuleGroupIsEmpty() {
            Configuration configuration = new Configuration();
            configuration.setTargetRootDir(targetDirPath);
            configuration.addSourcePaths(List.of(sourceDirPath));
            configuration.setSortAction(Sorter.Action.COPY);

            configuration.addNamedRuleGroup("Empty rule group", new RuleGroup());

            assertThrows(
                    SortConfigurationException.class,
                    configuration::validate
            );
        }

        @Test
        public void validateShouldThrowSortConfigurationExceptionIfNoSortActionWasGiven() {
            Configuration configuration = new Configuration();
            configuration.setTargetRootDir(targetDirPath);
            configuration.addSourcePaths(List.of(sourceDirPath));
            configuration.addNamedRuleGroup(ruleGroupName, ruleGroup);

            assertThrows(
                    SortConfigurationException.class,
                    configuration::validate
            );
        }

        @Test
        public void validateShouldPassIfAllParametersHasBeenSpecified() {
            Configuration configuration = new Configuration();
            configuration.setTargetRootDir(targetDirPath);
            configuration.addSourcePaths(List.of(sourceDirPath));
            configuration.addNamedRuleGroup(ruleGroupName, ruleGroup);
            configuration.setSortAction(Sorter.Action.COPY);

            assertDoesNotThrow(configuration::validate);
        }
    }
}