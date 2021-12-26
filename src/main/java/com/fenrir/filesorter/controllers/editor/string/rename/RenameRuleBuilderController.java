package com.fenrir.filesorter.controllers.editor.string.rename;

import com.fenrir.filesorter.controllers.editor.string.StringRuleBuilderController;
import com.fenrir.filesorter.model.enums.Scope;
import com.fenrir.filesorter.model.rule.Iterator;
import com.fenrir.filesorter.model.rule.Rule;
import com.fenrir.filesorter.model.rule.Token;
import com.fenrir.filesorter.model.statement.types.ProviderType;
import java.util.*;

public class RenameRuleBuilderController extends StringRuleBuilderController {

    @Override
    protected List<ProviderType> getProviderList() {
        ProviderType[] types = ProviderType.values();
        List<ProviderType> providerRenameTypes = new ArrayList<>();
        for (ProviderType type: types) {
            if (isProviderInRenameScope(type)) {
                providerRenameTypes.add(type);
            }
        }
        return providerRenameTypes;
    }

    private boolean isProviderInRenameScope(ProviderType type) {
        return Arrays.asList(type.getScope()).contains(Scope.RENAME);
    }

    @Override
    protected List<ProviderArgPair> parseRule(Rule rule) {
        List<ProviderArgPair> providerArgPairs = new ArrayList<>();
        Iterator<Token> iterator = rule.getTokenIterator();
        while (iterator.hasNext()) {
            Token token = iterator.next();
            ProviderType providerType = ProviderType.getType(token.symbol(), Scope.RENAME);
            String args = token.args() != null ? String.join(",", token.args()) : null;
            ProviderArgPair pair = new ProviderArgPair(providerType, args);
            providerArgPairs.add(pair);
        }
        return providerArgPairs;
    }
}
