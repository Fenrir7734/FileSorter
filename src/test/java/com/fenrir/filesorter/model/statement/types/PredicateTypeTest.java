package com.fenrir.filesorter.model.statement.types;

import com.fenrir.filesorter.model.statement.types.enums.ArgumentNumber;
import com.fenrir.filesorter.model.statement.types.enums.ReturnType;
import com.fenrir.filesorter.model.exceptions.ExpressionFormatException;
import com.fenrir.filesorter.model.statement.predicate.*;
import com.fenrir.filesorter.model.statement.provider.FileNameProvider;
import com.fenrir.filesorter.model.statement.provider.FileSizeProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.fenrir.filesorter.model.statement.types.enums.ReturnType.*;
import static com.fenrir.filesorter.model.statement.types.enums.ReturnType.DATE;
import static org.junit.jupiter.api.Assertions.*;

class PredicateTypeTest {
    @Test
    public void getTypeShouldReturnPredicateTypeForValidToken() {
        PredicateType type = PredicateType.getType("EW");
        assertNotNull(type);
        assertEquals(PredicateType.ENDS_WITH, type);
    }

    @Test
    public void getTypeShouldReturnNullForInvalidToken() {
        PredicateType type = PredicateType.getType("Invalid token");
        assertNull(type);
    }

    @Test
    public void getPredicatesForReturnTypeShouldReturnListOfAllPredicatesForGivenReturnType() {
        List<PredicateType> predicateTypes = PredicateType.getPredicatesForReturnType(DATE);
        assertEquals(
                List.of(
                        PredicateType.EQUAL,
                        PredicateType.NOT_EQUAL,
                        PredicateType.GREATER,
                        PredicateType.GREATER_EQUAL,
                        PredicateType.SMALLER,
                        PredicateType.SMALLER_EQUAL
                ),
                predicateTypes
        );
    }

    @Test
    public void getTokenShouldReturnTokenFor() {
        String token = PredicateType.EQUAL.getToken();
        assertEquals("==", token);
    }

    @Test
    public void getArgumentNumberShouldReturnArgumentNumber() {
        ArgumentNumber argumentNumber = PredicateType.EQUAL.getArgumentNumber();
        assertEquals(argumentNumber, ArgumentNumber.MULTIPLE);
    }

    @Test
    public void getTypesShouldReturnReturnTypes() {
        ReturnType[] returnTypes = PredicateType.EQUAL.getTypes();
        assertArrayEquals(new ReturnType[]{NUMBER, PATH, STRING, EXACT_STRING, DATE}, returnTypes);
    }

    @Test
    public void getPredicateShouldReturnPredicate() throws ExpressionFormatException {
        PredicateOperands<?> operands = new PredicateOperands<>(
                new FileNameProvider(null),
                List.of("abc")
        );
        Predicate<?> predicate = PredicateType.EQUAL.getPredicate(operands, false);
        assertNotNull(predicate);
        assertTrue(predicate instanceof EqualPredicate);
    }

    @Test
    public void getPredicateShouldThrowExpressionFormatExceptionForIncorrectTypeOfOperand() {
        PredicateOperands<Long> operands = new PredicateOperands<>(
                new FileSizeProvider(null),
                List.of(12L)
        );
        PredicateType type = PredicateType.CONTAINS;
        assertThrows(
                ExpressionFormatException.class,
                () -> type.getPredicate(operands, false),
                "Invalid type of operand for given operator"
        );
    }
}