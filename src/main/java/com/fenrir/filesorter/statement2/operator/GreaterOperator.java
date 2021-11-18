package com.fenrir.filesorter.statement2.operator;

import java.util.List;

public class GreaterOperatorStatement<T extends Comparable<? super T>> implements Operator<T>{

    @Override
    public boolean execute(T o1, List<T> o2) {
        return false;
    }
}
