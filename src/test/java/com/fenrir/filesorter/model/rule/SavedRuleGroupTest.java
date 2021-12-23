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
    class TestForCorrectDataInFile {
        String name1;
        RuleGroup ruleGroup1;
        JSONObject ruleGroup1JSONObject;
        String name2;
        RuleGroup ruleGroup2;
        JSONObject ruleGroup2JSONObject;

        @BeforeEach
        public void initFile() throws IOException, ExpressionFormatException {
            JSONObject root = new JSONObject();

            name1 = "Rule group 1";
            ruleGroup1 = new RuleGroup();
            ruleGroup1.setSortRule(new Rule("%(DIM)"));
            ruleGroup1.setRenameRule(new Rule("%(FIX)"));
            ruleGroup1.addFilterRule(new Rule("%(INC)%(DIM)%(==:1920x1080)"));
            ruleGroup1.addFilterRule(new Rule("%(INC)%(FIN)%(SW:HD)"));
            JSONArray jsonArray1 = new JSONArray();
            jsonArray1.put(ruleGroup1.getFilterRules().get(0).getExpression());
            jsonArray1.put(ruleGroup1.getFilterRules().get(1).getExpression());
            ruleGroup1JSONObject = new JSONObject();
            ruleGroup1JSONObject.put("Sort", ruleGroup1.getSortRule().getExpression());
            ruleGroup1JSONObject.put("Rename", ruleGroup1.getRenameRule().getExpression());
            ruleGroup1JSONObject.put("Filter", jsonArray1);
            root.put(name1, ruleGroup1JSONObject);

            name2 = "Rule group 2";
            ruleGroup2 = new RuleGroup();
            ruleGroup2.setSortRule(new Rule("%(DIM)"));
            ruleGroup2.setRenameRule(new Rule("%(FIN)"));
            ruleGroup2.addFilterRule(new Rule("%(INC)%(FIN)%(NCO:HD)"));
            ruleGroup2.addFilterRule(new Rule("%(INC)%(FIN)%(==:abcd)"));
            JSONArray jsonArray2 = new JSONArray();
            jsonArray2.put(ruleGroup2.getFilterRules().get(0).getExpression());
            jsonArray2.put(ruleGroup2.getFilterRules().get(1).getExpression());
            ruleGroup2JSONObject = new JSONObject();
            ruleGroup2JSONObject.put("Sort", ruleGroup2.getSortRule().getExpression());
            ruleGroup2JSONObject.put("Rename", ruleGroup2.getRenameRule().getExpression());
            ruleGroup2JSONObject.put("Filter", jsonArray2);
            root.put(name2, ruleGroup2JSONObject);

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
            List<String> expectedRuleGroupNames = List.of(name2, name1);
            assertEquals(expectedRuleGroupNames, actualRuleGroupNames);
        }

        @Test
        public void getRuleGroupShouldReturnRuleGroupForExistingName() throws IOException, ExpressionFormatException {
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            RuleGroup actualRuleGroup = savedRuleGroup.getRuleGroup(name1);
            assertEquals(ruleGroup1, actualRuleGroup);
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
            String nameOfNewRuleGroup = "test";
            RuleGroup newRuleGroup = new RuleGroup();
            newRuleGroup.setRenameRule(new Rule("%(DIN)"));
            newRuleGroup.setSortRule(new Rule("%(EXT)"));
            newRuleGroup.addFilterRule(new Rule("%(INC)%(EXT)%(==:jpg)"));
            newRuleGroup.addFilterRule(new Rule("%(EXC)%(WID)%(==:1920,1080)"));
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            savedRuleGroup.appendRuleGroup(nameOfNewRuleGroup, newRuleGroup);
            assertEquals(List.of(nameOfNewRuleGroup, name2, name1), savedRuleGroup.getRuleGroupNames());
            assertEquals(ruleGroup1, savedRuleGroup.getRuleGroup(name1));
            assertEquals(ruleGroup2, savedRuleGroup.getRuleGroup(name2));
            assertEquals(newRuleGroup, savedRuleGroup.getRuleGroup(nameOfNewRuleGroup));
        }

        @Test
        public void appendRuleGroupShouldAddRuleGroupToJSONObjectForRuleGroupWithoutOneOfTheRule()
                throws IOException, ExpressionFormatException {
            String nameOfNewRuleGroup = "test";
            RuleGroup newRuleGroup = new RuleGroup();
            newRuleGroup.setRenameRule(new Rule("%(DIN)"));
            newRuleGroup.addFilterRule(new Rule("%(INC)%(EXT)%(==:jpg)"));
            newRuleGroup.addFilterRule(new Rule("%(EXC)%(WID)%(==:1920,1080)"));
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            savedRuleGroup.appendRuleGroup(nameOfNewRuleGroup, newRuleGroup);
            assertEquals(List.of(nameOfNewRuleGroup, name2, name1), savedRuleGroup.getRuleGroupNames());
            assertEquals(ruleGroup1, savedRuleGroup.getRuleGroup(name1));
            assertEquals(ruleGroup2, savedRuleGroup.getRuleGroup(name2));
            RuleGroup actualRuleGroup = savedRuleGroup.getRuleGroup(nameOfNewRuleGroup);
            assertEquals("", actualRuleGroup.getSortRule().toString());
            assertEquals(newRuleGroup.getRenameRule().toString(), actualRuleGroup.getRenameRule().toString());
            assertEquals(newRuleGroup.getFilterRules().size(), actualRuleGroup.getFilterRules().size());
            assertEquals(
                    newRuleGroup.getFilterRules().get(0).toString(),
                    actualRuleGroup.getFilterRules().get(0).toString()
            );
            assertEquals(
                    newRuleGroup.getFilterRules().get(1).toString(),
                    actualRuleGroup.getFilterRules().get(1).toString()
            );
        }

        @Test
        public void appendRuleGroupShouldAddRuleGroupToJSONObjectForEmptyRuleGroup()
                throws IOException, ExpressionFormatException {
            String nameOfNewRuleGroup = "test";
            RuleGroup newRuleGroup = new RuleGroup();
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            savedRuleGroup.appendRuleGroup(nameOfNewRuleGroup, newRuleGroup);
            assertEquals(List.of(nameOfNewRuleGroup, name2, name1), savedRuleGroup.getRuleGroupNames());
            assertEquals(ruleGroup1, savedRuleGroup.getRuleGroup(name1));
            assertEquals(ruleGroup2, savedRuleGroup.getRuleGroup(name2));
            RuleGroup actualRuleGroup = savedRuleGroup.getRuleGroup(nameOfNewRuleGroup);
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
        public void saveRuleGroupToFileShouldSaveRuleGroupsToFileWithAppendedNewRuleGroup()
                throws IOException, ExpressionFormatException {
            String nameOfNewRuleGroup = "test";
            RuleGroup newRuleGroup = new RuleGroup();
            newRuleGroup.setRenameRule(new Rule("%(DIN)"));
            newRuleGroup.setSortRule(new Rule("%(EXT)"));
            newRuleGroup.addFilterRule(new Rule("%(INC)%(EXT)%(==:jpg)"));
            newRuleGroup.addFilterRule(new Rule("%(EXC)%(WID)%(==:1920,1080)"));
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            savedRuleGroup.appendRuleGroup(nameOfNewRuleGroup, newRuleGroup);
            savedRuleGroup.saveRuleGroupsToFile();
            String content = new String(Files.readAllBytes(filePath));
            JSONObject actualObject = new JSONObject(content);
            assertEquals(List.of(nameOfNewRuleGroup, name2, name1), actualObject.names().toList());
            assertEquals(ruleGroup1JSONObject.toString(), actualObject.getJSONObject(name1).toString());
            assertEquals(ruleGroup2JSONObject.toString(), actualObject.getJSONObject(name2).toString());
            JSONObject actualNewRuleGroup = actualObject.getJSONObject(nameOfNewRuleGroup);
            assertEquals(newRuleGroup.getSortRule().toString(), actualNewRuleGroup.getString("Sort"));
            assertEquals(newRuleGroup.getRenameRule().toString(), actualNewRuleGroup.getString("Rename"));
            JSONArray actualFilterRuleOfNewRuleGroup = actualNewRuleGroup.getJSONArray("Filter");
            assertEquals(newRuleGroup.getFilterRules().size(), actualFilterRuleOfNewRuleGroup.length());
            assertEquals(
                    newRuleGroup.getFilterRules().get(0).toString(),
                    actualFilterRuleOfNewRuleGroup.get(0)
            );
            assertEquals(
                    newRuleGroup.getFilterRules().get(1).toString(),
                    actualFilterRuleOfNewRuleGroup.get(1)
            );
        }

        @Test
        public void saveRuleGroupShouldSaveRuleGroupsToFileWithRemovedRuleGroup()
                throws IOException {
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            savedRuleGroup.removeRuleGroup(name1);
            savedRuleGroup.saveRuleGroupsToFile();
            String content = new String(Files.readAllBytes(filePath));
            JSONObject actualObject = new JSONObject(content);
            assertEquals(List.of(name2), actualObject.names().toList());
            assertEquals(ruleGroup2JSONObject.toString(), actualObject.getJSONObject(name2).toString());
        }
    }

    @Nested
    class TestForIncorrectData {
        String name;
        RuleGroup ruleGroup;
        JSONObject ruleGroupJSONObject;

        @BeforeEach
        public void initFile() throws IOException, ExpressionFormatException {
            JSONObject root = new JSONObject();

            name = "Rule group 1";
            ruleGroup = new RuleGroup();
            ruleGroup.setSortRule(new Rule("%(Incorrect)"));
            ruleGroup.setRenameRule(new Rule("%(FIX)"));
            ruleGroup.addFilterRule(new Rule("%(INC)%(DIM)%(==:1920x1080)"));
            ruleGroup.addFilterRule(new Rule("%(INC)%(FIN)%(SW:HD)"));
            JSONArray jsonArray1 = new JSONArray();
            jsonArray1.put(ruleGroup.getFilterRules().get(0).getExpression());
            jsonArray1.put(ruleGroup.getFilterRules().get(1).getExpression());
            ruleGroupJSONObject = new JSONObject();
            ruleGroupJSONObject.put("Sort", ruleGroup.getSortRule().getExpression());
            ruleGroupJSONObject.put("Rename", ruleGroup.getRenameRule().getExpression());
            ruleGroupJSONObject.put("Filter", jsonArray1);
            root.put(name, ruleGroupJSONObject);

            filePath = tempDir.resolve("test.json");
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath.toString()))) {
                writer.write(root.toString(4));
            }
        }

        @Test
        public void getRuleGroupShouldThrowExpressionFormatExceptionForIncorrectRuleGroupInFile()
                throws IOException {
            SavedRuleGroup savedRuleGroup = new SavedRuleGroup(filePath.toString());
            assertThrows(
                    ExpressionFormatException.class,
                    () -> savedRuleGroup.getRuleGroup(name)
            );
        }
    }
}