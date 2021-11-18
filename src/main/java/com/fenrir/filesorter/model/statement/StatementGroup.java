package com.fenrir.filesorter.model.statement;

import com.fenrir.filesorter.model.statement.operator.FilterOperatorStatement;
import com.fenrir.filesorter.model.statement.string.StringStatement;

import java.util.ArrayList;
import java.util.List;

public class StatementGroup {
    private List<StringStatement> renameStatement;
    private List<StringStatement> sortStatement;
    private List<FilterOperatorStatement<? extends Comparable<?>>> filterStatements;

    public StatementGroup() {
        this.renameStatement = new ArrayList<>();
        this.sortStatement = new ArrayList<>();
        this.filterStatements = new ArrayList<>();
    }

    public void setRenameStatement(List<StringStatement> renameStatement) {
        this.renameStatement = renameStatement;
    }

    public void setSortStatement(List<StringStatement> sortStatement) {
        this.sortStatement = sortStatement;
    }

    public void addFilterStatement(FilterOperatorStatement<? extends Comparable<?>> filterStatement) {
        filterStatements.add(filterStatement);
    }

    public List<StringStatement> getRenameStatement() {
        return renameStatement;
    }

    public List<StringStatement> getSortStatement() {
        return sortStatement;
    }

    public List<FilterOperatorStatement<? extends Comparable<?>>> getFilterStatements() {
        return filterStatements;
    }
}
