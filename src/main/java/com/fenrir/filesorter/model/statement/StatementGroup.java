package com.fenrir.filesorter.model.statement;

import com.fenrir.filesorter.model.statement.predicate.Predicate;
import com.fenrir.filesorter.model.statement.provider.Provider;

import java.util.ArrayList;
import java.util.List;

public class StatementGroup {
    private List<Provider<?>> renameStatement;
    private List<Provider<?>> sortStatement;
    private List<Predicate<? extends Comparable<?>>> filterStatements;

    public StatementGroup() {
        this.renameStatement = new ArrayList<>();
        this.sortStatement = new ArrayList<>();
        this.filterStatements = new ArrayList<>();
    }

    public void setRenameStatement(List<Provider<?>> renameStatement) {
        this.renameStatement = renameStatement;
    }

    public void setSortStatement(List<Provider<?>> sortStatement) {
        this.sortStatement = sortStatement;
    }

    public void addFilterStatement(Predicate<? extends Comparable<?>> filterStatement) {
        filterStatements.add(filterStatement);
    }

    public List<Provider<?>> getRenameStatement() {
        return renameStatement;
    }

    public List<Provider<?>> getSortStatement() {
        return sortStatement;
    }

    public List<Predicate<? extends Comparable<?>>> getFilterStatements() {
        return filterStatements;
    }
}
