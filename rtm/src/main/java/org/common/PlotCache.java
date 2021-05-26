package org.common;

import org.beans.PlotStatus;

import java.util.LinkedList;
import java.util.stream.Stream;

public class PlotCache {

    private final int capacity = 6;
    private final LinkedList<PlotStatus> data = new LinkedList<>();

    public void putIfValid(PlotStatus p) {
        data.push(p);
        data.sort((e1, e2) -> e1.getLastModified() > e2.getLastModified() ? 1 : -1);
        data.removeFirst();
    }

    public Stream<PlotStatus> stream() {
        return this.data.stream();
    }

    public void clear() {
        this.data.clear();
    }
}
