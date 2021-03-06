package com.mcintyret.jvm.core.thread;

import java.util.IdentityHashMap;
import java.util.Map;

import com.mcintyret.jvm.core.oop.OopClass;

/**
 * User: tommcintyre
 * Date: 5/26/14
 */
public class Threads {

    private static final Map<OopClass, Thread> THREADS = new IdentityHashMap<>();

    public static Thread get(OopClass oopThread) {
        return THREADS.get(oopThread);
    }

    public static void register(Thread thread) {
        if (THREADS.put(thread.getThisThread(), thread) != null) {
//            throw new AssertionError("Multiple live threads with id " + thread.getId());
        }
    }

    public static void deregister(Thread thread) {
        if (THREADS.remove(thread.getThisThread()) != thread) {
//            throw new AssertionError("Thread with id '" + thread.getId() + "' not registered");
        }
    }

}
