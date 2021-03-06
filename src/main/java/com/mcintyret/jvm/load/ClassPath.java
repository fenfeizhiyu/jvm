package com.mcintyret.jvm.load;

import java.util.Iterator;

import com.google.common.collect.Iterators;

public interface ClassPath extends Iterable<ClassFileResource> {

    default Iterator<ClassFileResource> classFileFilteringIterator(Iterator<ClassFileResource> it) {
        return Iterators.filter(it, classFileResource -> classFileResource.getName().endsWith(".class"));
    }

}
