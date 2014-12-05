package com.mcintyret.jvm.core.exec;

import java.util.NoSuchElementException;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ValueReceiver;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.util.Utils;

public class VariableStackImpl implements ValueReceiver, VariableStack {

    private static final int DEFAULT_SIZE = 10;

    private Variables stack;

    private int head = 0;

    public VariableStackImpl(int size) {
        stack = new Variables(size);
    }

    public VariableStackImpl() {
        this(DEFAULT_SIZE);
    }

    public void push(int val, SimpleType type) {
        if (head == stack.length()) {
            resize();
        }
        int pos = head++;
        stack.put(pos, type, val);
    }

    public int popRaw() {
        return popInternal(null, false);
    }

    public int popInternal(SimpleType type, boolean checkType) {
        if (head == 0) {
            throw new NoSuchElementException();
        }
        int pos = --head;
        int ret = checkType ? stack.getCheckedValue(pos, type) : stack.getRawValue(pos);
        stack.clear(pos);
        return ret;
    }

    @Override
    public int popChecked(SimpleType type) {
        return popInternal(type, true);
    }

    @Override
    public Variable pop() {
        if (head == 0) {
            throw new NoSuchElementException();
        }
        int pos = --head;
        Variable ret = stack.get(pos);
        stack.clear(pos);
        return ret;
    }

    @Override
    public int popInt() {
        return popChecked(SimpleType.INT);
    }

    @Override
    public float popFloat() {
        return Float.intBitsToFloat(popChecked(SimpleType.FLOAT));
    }

    @Override
    public long popLong() {
        return popDoubleWidth(SimpleType.LONG);
    }

    @Override
    public double popDouble() {
        return Double.longBitsToDouble(popDoubleWidth(SimpleType.DOUBLE));
    }

    private long popDoubleWidth(SimpleType type) {
        if (head <= 1) {
            throw new NoSuchElementException();
        }
        long val = Utils.toLong(stack.getCheckedValue(head - 2, type), stack.getCheckedValue(head - 1, type));
        head -= 2;
        return val;
    }

    @Override
    public <O extends Oop> O popOop() {
        return (O) Heap.getOop(popChecked(SimpleType.REF));
    }

    @Override
    public void pushChecked(int val, SimpleType type) {
        if (head >= stack.length()) {
            resize();
        }
        stack.put(head++, type, val);
    }

    @Override
    public void pushInt(int val) {
        pushChecked(val, SimpleType.INT);
    }

    @Override
    public void pushLong(int l, int r) {
        pushDoubleWidth(l, r, SimpleType.LONG);
    }

    @Override
    public void pushLong(long val) {
        pushDoubleWidth(val, SimpleType.LONG);
    }

    @Override
    public void pushFloat(float f) {
        pushChecked(Float.floatToIntBits(f), SimpleType.FLOAT);
    }

    @Override
    public void pushDouble(double d) {
        pushDoubleWidth(Double.doubleToLongBits(d), SimpleType.DOUBLE);
    }

    @Override
    public void pushDoubleWidth(int l, int r, SimpleType type) {
        if (head >= stack.length() - 1) {
            resize();
        }
        stack.put(head++, type, l);
        stack.put(head++, type, r);
    }

    @Override
    public void pushDoubleWidth(long val, SimpleType type) {
        int l = (int) (val >> 32);
        int r = (int) val;
        pushDoubleWidth(l, r, type);
    }

    @Override
    public void pushByte(byte b) {
        pushChecked(b, SimpleType.BYTE);
    }

    @Override
    public void pushShort(short s) {
        pushChecked(s, SimpleType.SHORT);
    }

    @Override
    public void pushOop(Oop oop) {
        pushChecked(oop.getAddress(), SimpleType.REF);
    }

    @Override
    public void pushNull() {
        pushChecked(Heap.NULL_POINTER, SimpleType.REF);
    }

    @Override
    public void push(Variable v) {
        pushChecked(v.getValue(), v.getType());
    }

    @Override
    public void clear() {
        makeNewStack(stack.length(), false);
    }

    private void resize() {
        makeNewStack(stack.length() * 2, true);
    }

    private void makeNewStack(int newSize, boolean copy) {
        Variables newStack = new Variables(newSize);
        if (copy) {
            int lim = Math.min(newSize, stack.length());
            System.arraycopy(stack.getRawValues(), 0, newStack.getRawValues(), 0, lim);
            System.arraycopy(stack.getTypes(), 0, newStack.getTypes(), 0, lim);
        }
        stack = newStack;
    }
}
