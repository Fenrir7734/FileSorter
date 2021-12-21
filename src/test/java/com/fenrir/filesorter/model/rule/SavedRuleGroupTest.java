package com.fenrir.filesorter.model.rule;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SavedRuleGroupTest {
    @TempDir
    Path tempDir;
    Path filePath;

    @Test
    public void constructorShouldThrowIOExceptionIfFileWasNotFound() {
        assertThrows(
                IOException.class,
                () -> new SavedRuleGroup("Invalid path")
        );
    }

    @Nested
    class TestForCorrectData {

        @BeforeEach
        public void initFile() throws IOException, ExpressionFormatException {
            JSONObject root = new JSONObject();

            JSONObject child1 = new JSONObject();
            JSONArray filterArray1 = new JSONArray();
            filterArray1.put("%(INC)%(FIN)%(NCO:HD)");
            filterArray1.put("%(INC)%(FIN)%(==:abcd)");
            child1.put("Sort", "%(DIM)");
            child1.put("Rename", "%(FIX)");
            child1.put("Filter", filterArray1);
            root.put("Rule group 1", child1);

            JSONObject child2 = new JSONObject();
            JSONArray filterArray2 = new JSONArray();
            filterArray2.put("%(INC)%(DIM)%(==:1920x1080)");
            filterArray2.put("%(INC)%(FIN)%(SW:HD)");
            child2.put("Sort", "%(DIM)");
            child2.put("Rename", "%(FIN)");
            child2.put("Filter", filterArray2);
            root.put("Rule group 2", child2);

            filePath = tempDir.resolve("test.json");
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath.toString()))) {
                writer.write(root.toString(4));
            }
        }

        @Test
        public void constructorShouldReturnNewInstanceForCorrectFilePath() throws IOException {
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            assertNotNull(savedRuleGroup);
        }

        @Test
        public void getRuleGroupNamesShouldReturnListOfRuleGroupNames() throws IOException {
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            List<String> actualRuleGroupNames = savedRuleGroup.getRuleGroupNames();
            List<String> expectedRuleGroupNames = List.of("Rule group 2", "Rule group 1");
            assertEquals(expectedRuleGroupNames, actualRuleGroupNames);
        }

        @Test
        public void getRuleGroupShouldReturnRuleGroupForExistingName() throws IOException, ExpressionFormatException {
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            RuleGroup ruleGroup = savedRuleGroup.getRuleGroup("Rule group 1");
            assertEquals("%(DIM)", ruleGroup.getSortRule().toString());
            assertEquals("%(FIX)", ruleGroup.getRenameRule().toString());
            assertEquals(2, ruleGroup.getFilterRules().size());
            assertEquals("%(INC)%(FIN)%(NCO:HD)", ruleGroup.getFilterRules().get(0).toString());
            assertEquals("%(INC)%(FIN)%(==:abcd)", ruleGroup.getFilterRules().get(1).toString());
        }

        @Test
        public void getRuleGroupShouldThrowIllegalArgumentExceptionForNotExistingName() throws IOException {
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> savedRuleGroup.getRuleGroup("Invalid")
            );
            assertEquals("Rule Group with this name don't exists", exception.getMessage());
        }

        @Test
        public void appendRuleGroupShouldAddRuleGroupToJSONObjectForCorrectRuleGroup() throws IOException, ExpressionFormatException {
            String name = "test";
            RuleGroup expectedRuleGroup = new RuleGroup();
            expectedRuleGroup.setRenameRule(new Rule("%(DIN)"));
            expectedRuleGroup.setSortRule(new Rule("%(EXT)"));
            expectedRuleGroup.addFilterRule(new Rule("%(INC)%(EXT)%(==:jpg)"));
            expectedRuleGroup.addFilterRule(new Rule("%(EXC)%(WID)%(==:1920,1080)"));
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            savedRuleGroup.appendRuleGroup(name, expectedRuleGroup);
            assertEquals(List.of(name, "Rule group 2", "Rule group 1"), savedRuleGroup.getRuleGroupNames());
            RuleGroup actualRuleGroup = savedRuleGroup.getRuleGroup(name);
            assertEquals(expectedRuleGroup.getSortRule().toString(), actualRuleGroup.getSortRule().toString());
            assertEquals(expectedRuleGroup.getRenameRule().toString(), actualRuleGroup.getRenameRule().toString());
            assertEquals(expectedRuleGroup.getFilterRules().size(), actualRuleGroup.getFilterRules().size());
            assertEquals(
                    expectedRuleGroup.getFilterRules().get(0).toString(),
                    actualRuleGroup.getFilterRules().get(0).toString()
            );
            assertEquals(
                    expectedRuleGroup.getFilterRules().get(1).toString(),
                    actualRuleGroup.getFilterRules().get(1).toString()
            );
        }

        @Test
        public void appendRuleGroupShouldAddRuleGroupToJSONObjectForRuleGroupWithoutOneOfTheRule()
                throws IOException, ExpressionFormatException {
            String name = "test";
            RuleGroup expectedRuleGroup = new RuleGroup();
            expectedRuleGroup.setRenameRule(new Rule("%(DIN)"));
            expectedRuleGroup.addFilterRule(new Rule("%(INC)%(EXT)%(==:jpg)"));
            expectedRuleGroup.addFilterRule(new Rule("%(EXC)%(WID)%(==:1920,1080)"));
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            savedRuleGroup.appendRuleGroup(name, expectedRuleGroup);
            assertEquals(List.of(name, "Rule group 2", "Rule group 1"), savedRuleGroup.getRuleGroupNames());
            RuleGroup actualRuleGroup = savedRuleGroup.getRuleGroup(name);
            assertEquals("", actualRuleGroup.getSortRule().toString());
            assertEquals(expectedRuleGroup.getRenameRule().toString(), actualRuleGroup.getRenameRule().toString());
            assertEquals(expectedRuleGroup.getFilterRules().size(), actualRuleGroup.getFilterRules().size());
            assertEquals(
                    expectedRuleGroup.getFilterRules().get(0).toString(),
                    actualRuleGroup.getFilterRules().get(0).toString()
            );
            assertEquals(
                    expectedRuleGroup.getFilterRules().get(1).toString(),
                    actualRuleGroup.getFilterRules().get(1).toString()
            );
        }

        @Test
        public void appendRuleGroupShouldAddRuleGroupToJSONObjectForEmptyRuleGroup()
                throws IOException, ExpressionFormatException {
            String name = "test";
            RuleGroup expectedRuleGroup = new RuleGroup();
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            savedRuleGroup.appendRuleGroup(name, expectedRuleGroup);
            assertEquals(List.of(name, "Rule group 2", "Rule group 1"), savedRuleGroup.getRuleGroupNames());
            RuleGroup actualRuleGroup = savedRuleGroup.getRuleGroup(name);
            assertEquals("", actualRuleGroup.getSortRule().toString());
            assertEquals("", actualRuleGroup.getRenameRule().toString());
            assertTrue(actualRuleGroup.getFilterRules().isEmpty());
        }

        @Test
        public void appendRuleGroupShouldThrowIllegalArgumentExceptionForAlreadyExistingName()
                throws IOException {
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> savedRuleGroup.appendRuleGroup("Rule group 1", new RuleGroup())
            );
            assertEquals("Rule Group with this name already exists", exception.getMessage());
        }

        @Test
        public void saveRuleGroupToFileShouldSaveAppendedRuleGroupToFile()
                throws IOException, ExpressionFormatException {
            String name = "test";
            RuleGroup expectedRuleGroup = new RuleGroup();
            expectedRuleGroup.setRenameRule(new Rule("%(DIN)"));
            expectedRuleGroup.addFilterRule(new Rule("%(INC)%(EXT)%(==:jpg)"));
            expectedRuleGroup.addFilterRule(new Rule("%(EXC)%(WID)%(==:1920,1080)"));
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            savedRuleGroup.appendRuleGroup(name, expectedRuleGroup);
            savedRuleGroup.saveRuleGroupsToFile();

            String content = new String(Files.readAllBytes(filePath));

        }
    }
}