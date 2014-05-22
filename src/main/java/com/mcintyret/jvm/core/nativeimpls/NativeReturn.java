package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.WordStack;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
public interface NativeReturn {

    static final NativeReturn FOR_VOID = stack -> {};

    static final NativeReturn FOR_NULL = forInt(Heap.NULL_POINTER);

    public static NativeReturn forInt(int i) {
        return stack -> {
            stack.push(i);
        };
    }

    public static NativeReturn forLong(long l) {
        return stack -> {
            stack.push(l);
        };
    }

    public static NativeReturn forNull() {
        return FOR_NULL;
    }

    public static NativeReturn forVoid() {
        return FOR_VOID;
    }



    void applyToStack(WordStack stack);

}