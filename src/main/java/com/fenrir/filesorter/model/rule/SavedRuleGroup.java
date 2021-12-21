package com.fenrir.filesorter.model.rule;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.parsers.RuleGroupParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SavedRuleGroup {
    private final Logger logger = LoggerFactory.getLogger(SavedRuleGroup.class);

    private final static String PATH = "src/main/resources/rule_group_1.json";
    private final static String RENAME_KEY = "Rename";
    private final static String SORT_KEY = "Sort";
    private final static String FILTER_KEY = "Filter";

    private JSONObject savedRuleGroup;

    public SavedRuleGroup() throws IOException {
        readRuleGroupsFromFile();
    }

    private void readRuleGroupsFromFile() throws IOException {
        try {
            String content = new String(Files.readAllBytes(Path.of(PATH)));
            savedRuleGroup = new JSONObject(content);
        } catch (IOException e) {
            logger.error("Error during reading rule_group.json: {}", e.getMessage());
            throw e;
        }
    }

    public List<String> getRuleGroupNames() {
        return savedRuleGroup.names()
                .toList()
                .stream()
                .map(Object::toString)
                .toList();
    }

    public RuleGroup getRuleGroup(String name) throws ExpressionFormatException {
        try {
            if (savedRuleGroup.has(name)) {
                JSONObject ruleGroupJSONObject = savedRuleGroup.getJSONObject(name);
                RuleGroup ruleGroup = parseJSON(ruleGroupJSONObject);
                validate(ruleGroup);
                return ruleGroup;
            } else {
                throw new IllegalArgumentException("Rule Group with this name don't exists");
            }
        } catch (ExpressionFormatException e) {
            throw new ExpressionFormatException("Loading failed. Invalid expression.");
        }
    }

    private RuleGroup parseJSON(JSONObject object) throws ExpressionFormatException {
        Rule renameRule = new Rule(object.getString(RENAME_KEY));
        Rule sortRule = new Rule(object.getString(SORT_KEY));
        List<Rule> filterRules = new ArrayList<>();
        for (String filterRule: JSONArrayToList(object.getJSONArray(FILTER_KEY))) {
            filterRules.add(new Rule(filterRule));
        }
        RuleGroup group = new RuleGroup();
        group.setRenameRule(renameRule);
        group.setSortRule(sortRule);
        group.setFilterRules(filterRules);
        return group;
    }

    private void validate(RuleGroup group) throws ExpressionFormatException {
        RuleGroupParser parser = new RuleGroupParser();
        parser.parse(group);
    }

    public void appendRuleGroup(String name, RuleGroup group) throws IllegalArgumentException {
        if (!savedRuleGroup.has(name)) {
            JSONObject ruleGroupJSONObject = parseRuleGroup(group);
            savedRuleGroup.put(name, ruleGroupJSONObject);
        } else {
            throw new IllegalArgumentException("Rule Group with this name already exists");
        }
    }

    private JSONObject parseRuleGroup(RuleGroup group) {
        JSONArray filters = new JSONArray();
        for (Rule filterRule: group.getFilterRules()) {
            filters.put(filterRule.getExpression());
        }
        String renameExpression = group.getRenameRule() != null ? group.getRenameRule().getExpression() : "";
        String sortExpression = group.getSortRule() != null ? group.getSortRule().getExpression() : "";

        JSONObject ruleGroupJSONObject = new JSONObject();
        ruleGroupJSONObject.put(RENAME_KEY, renameExpression);
        ruleGroupJSONObject.put(SORT_KEY, sortExpression);
        ruleGroupJSONObject.put(FILTER_KEY, filters);
        return ruleGroupJSONObject;
    }

    public void removeRuleGroup(String name) {
        if (savedRuleGroup.has(name)) {
            savedRuleGroup.remove(name);
        } else {
            throw new IllegalArgumentException("Rule Group with this name don't exists");
        }
    }

    public void saveRuleGroupsToFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PATH))) {
            writer.write(savedRuleGroup.toString(4));
        } catch (IOException e) {
            logger.error("Error during writing to rule_group.json file: {}", e.getMessage());
            throw e;
        }
    }

    private List<String> JSONArrayToList(JSONArray array) {
        return array.toList()
                .stream()
                .map(Objects::toString)
                .toList();
    }
}
