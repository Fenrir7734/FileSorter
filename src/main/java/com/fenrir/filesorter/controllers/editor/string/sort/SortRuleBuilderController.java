package com.fenrir.filesorter.controllers.editor.string.sort;

import com.fenrir.filesorter.controllers.editor.string.StringRuleBuilderController;
import com.fenrir.filesorter.model.statement.types.enums.Scope;
import com.fenrir.filesorter.model.rule.Iterator;
import com.fenrir.filesorter.model.rule.Rule;
import com.fenrir.filesorter.model.rule.Token;
import com.fenrir.filesorter.model.statement.types.ProviderType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortRuleBuilderController extends StringRuleBuilderController {
    @Override
    protected List<ProviderType> getProviderList() {
        ProviderType[] types = ProviderType.values();
        List<ProviderType> providerRenameTypes = new ArrayList<>();
        for (ProviderType type: types) {
            if (isProviderInSortScope(type)) {
                providerRenameTypes.add(type);
            }
        }
        return providerRenameTypes;
    }

    private boolean isProviderInSortScope(ProviderType type) {
        return Arrays.asList(type.getScope()).contains(Scope.SORT);
    }

    @Override
    protected List<ProviderArgPair> parseRule(Rule rule) {
        List<ProviderArgPair> providerArgPairs = new ArrayList<>();
        Iterator<Token> iterator = rule.getTokenIterator();
        while (iterator.hasNext()) {
            Token token = iterator.next();
            ProviderType providerType = ProviderType.getType(token.symbol(), Scope.SORT);
            String args = token.args() != null ? String.join(",", token.args()) : null;
            ProviderArgPair pair = new ProviderArgPair(providerType, args);
            providerArgPairs.add(pair);
        }
        return providerArgPairs;
    }
}
