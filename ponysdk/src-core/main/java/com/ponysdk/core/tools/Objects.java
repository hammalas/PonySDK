
package com.ponysdk.core.tools;

import javax.annotation.Nullable;

public class Objects {

    public static boolean equals(@Nullable final Object a, @Nullable final Object b) {
        if (a == null && b == null) return true;
        if (a != null && b != null) return a.equals(b);
        return false;
    }
    
    public static boolean quals(final String s1, final String s2) {
        if (s1 != null) {
            return s1.equals(s2);
        } else {
            return s2 == null;
        }
    }

    public static boolean equals(final boolean a, final boolean b) {
        return a == b;
    }

    public static boolean equals(final int a, final int b) {
        return a == b;
    }

    public static boolean equals(final char a, final char b) {
        return a == b;
    }

    public static boolean equals(final byte a, final byte b) {
        return a == b;
    }
}
