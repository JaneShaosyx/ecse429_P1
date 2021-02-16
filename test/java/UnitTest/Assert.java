package UnitTest;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.ComparisonFailure;

import java.util.Arrays;
import java.util.Collection;

/**
 * External for {@code org.junit.Assert}.
 *
 * @author Haohang Xia (created on 2021-02-14)
 * @version 1.0
 * @see org.junit.Assert
 */
abstract class Assert extends org.junit.Assert {

    /**
     * Assert equals anyone of array elements
     */
    static void assertAnyEquals(String message, String[] expected, String actual) {
        if (ArrayUtils.contains(expected, actual)) return;
        throw new ComparisonFailure(message, Arrays.toString(expected), actual);
    }

    /**
     * Assert collection not empty.
     */
    static void assertNotEmpty(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            throw new AssertionError("Collection is empty");
        }
    }
}
