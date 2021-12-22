package com.fenrir.filesorter.model.rule;

import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RuleGroupTest {
    @Test
    public void equalShouldReturnTrueIfObjectAreTheSame() throws ExpressionFormatException {
        RuleGroup ruleGroup1 = new RuleGroup();
        ruleGroup1.setRenameRule(new Rule("%(FIX)"));
        ruleGroup1.setSortRule(new Rule("%(DIM)"));
        ruleGroup1.addFilterRule(new Rule("%(INC)%(DIM)%(==:1920x1080)"));
        ruleGroup1.addFilterRule(new Rule("%(INC)%(FIN)%(SW:HD)"));
        RuleGroup ruleGroup2 = new RuleGroup();
        ruleGroup2.setRenameRule(new Rule("%(FIX)"));
        ruleGroup2.setSortRule(new Rule("%(DIM)"));
        ruleGroup2.addFilterRule(new Rule("%(INC)%(DIM)%(==:1920x1080)"));
        ruleGroup2.addFilterRule(new Rule("%(INC)%(FIN)%(SW:HD)"));
        assertEquals(ruleGroup1, ruleGroup2);
    }

    @Test
    public void equalShouldReturnFalseIfRenameRuleAreDifferent() throws ExpressionFormatException {
        RuleGroup ruleGroup1 = new RuleGroup();
        ruleGroup1.setRenameRule(new Rule("%(FIX)"));
        ruleGroup1.setSortRule(new Rule("%(DIM)"));
        ruleGroup1.addFilterRule(new Rule("%(INC)%(DIM)%(==:1920x1080)"));
        ruleGroup1.addFilterRule(new Rule("%(INC)%(FIN)%(SW:HD)"));
        RuleGroup ruleGroup2 = new RuleGroup();
        ruleGroup2.setRenameRule(new Rule("%(DIM)"));
        ruleGroup2.setSortRule(new Rule("%(DIM)"));
        ruleGroup2.addFilterRule(new Rule("%(INC)%(DIM)%(==:1920x1080)"));
        ruleGroup2.addFilterRule(new Rule("%(INC)%(FIN)%(SW:HD)"));
        assertNotEquals(ruleGroup1, ruleGroup2);
    }

    @Test
    public void equalShouldReturnFalseIfSortRuleAreDifferent() throws ExpressionFormatException {
        RuleGroup ruleGroup1 = new RuleGroup();
        ruleGroup1.setRenameRule(new Rule("%(FIX)"));
        ruleGroup1.setSortRule(new Rule("%(DIM)"));
        ruleGroup1.addFilterRule(new Rule("%(INC)%(DIM)%(==:1920x1080)"));
        ruleGroup1.addFilterRule(new Rule("%(INC)%(FIN)%(SW:HD)"));
        RuleGroup ruleGroup2 = new RuleGroup();
        ruleGroup2.setRenameRule(new Rule("%(FIX)"));
        ruleGroup2.setSortRule(new Rule("%(FIX)"));
        ruleGroup2.addFilterRule(new Rule("%(INC)%(DIM)%(==:1920x1080)"));
        ruleGroup2.addFilterRule(new Rule("%(INC)%(FIN)%(SW:HD)"));
        assertNotEquals(ruleGroup1, ruleGroup2);
    }

    @Test
    public void equalShouldReturnFalseIfFilterRuleAreDifferent() throws ExpressionFormatException {
        RuleGroup ruleGroup1 = new RuleGroup();
        ruleGroup1.setRenameRule(new Rule("%(FIX)"));
        ruleGroup1.setSortRule(new Rule("%(DIM)"));
        ruleGroup1.addFilterRule(new Rule("%(INC)%(DIM)%(==:1920x1080)"));
        ruleGroup1.addFilterRule(new Rule("%(INC)%(FIN)%(SW:HD)"));
        RuleGroup ruleGroup2 = new RuleGroup();
        ruleGroup2.setRenameRule(new Rule("%(FIX)"));
        ruleGroup2.setSortRule(new Rule("%(DIM)"));
        ruleGroup2.addFilterRule(new Rule("%(EXC)%(DIM)%(==:1920x1080)"));
        ruleGroup2.addFilterRule(new Rule("%(INC)%(FIN)%(SW:HD)"));
        assertNotEquals(ruleGroup1, ruleGroup2);
    }

    @Test
    public void equalShouldReturnFalseIfFilterRuleAreInDifferentOrder() throws ExpressionFormatException {
        RuleGroup ruleGroup1 = new RuleGroup();
        ruleGroup1.setRenameRule(new Rule("%(FIX)"));
        ruleGroup1.setSortRule(new Rule("%(DIM)"));
        ruleGroup1.addFilterRule(new Rule("%(INC)%(DIM)%(==:1920x1080)"));
        ruleGroup1.addFilterRule(new Rule("%(INC)%(FIN)%(SW:HD)"));
        RuleGroup ruleGroup2 = new RuleGroup();
        ruleGroup2.setRenameRule(new Rule("%(FIX)"));
        ruleGroup2.setSortRule(new Rule("%(DIM)"));
        ruleGroup2.addFilterRule(new Rule("%(INC)%(FIN)%(SW:HD)"));
        ruleGroup2.addFilterRule(new Rule("%(INC)%(DIM)%(==:1920x1080)"));
        assertNotEquals(ruleGroup1, ruleGroup2);
    }

    @Test
    public void equalShouldReturnFalseIfOneObjectIsEqualNull() throws ExpressionFormatException {
        RuleGroup ruleGroup1 = new RuleGroup();
        ruleGroup1.setRenameRule(new Rule("%(FIX)"));
        ruleGroup1.setSortRule(new Rule("%(DIM)"));
        ruleGroup1.addFilterRule(new Rule("%(INC)%(DIM)%(==:1920x1080)"));
        ruleGroup1.addFilterRule(new Rule("%(INC)%(FIN)%(SW:HD)"));
        assertNotEquals(ruleGroup1, null);
    }

    @Test
    public void equalShouldReturnFalseForObjectsOfADifferentType() throws ExpressionFormatException {
        RuleGroup ruleGroup1 = new RuleGroup();
        ruleGroup1.setRenameRule(new Rule("%(FIX)"));
        ruleGroup1.setSortRule(new Rule("%(DIM)"));
        ruleGroup1.addFilterRule(new Rule("%(INC)%(DIM)%(==:1920x1080)"));
        ruleGroup1.addFilterRule(new Rule("%(INC)%(FIN)%(SW:HD)"));
        assertNotEquals(ruleGroup1, new Object());
    }
}
