package com.mcintyret.jvm.core.opcode.nuu;

import com.mcintyret.jvm.core.clazz.ClassObject;
import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.opcode.OperationContext;

class ANewArray extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ClassObject clazz = (ClassObject) ctx.getConstantPool().get(ctx.getByteIterator().nextShort());

        ctx.getStack().push(Heap.allocate(Utils.newArray(clazz.getType(), ctx.getStack().pop())));
    }

    @Override
    public byte getByte() {
        return (byte) 0xBD;
    }
}
