package org.common;

import org.beans.PlotStatus;

import java.util.LinkedList;
import java.util.stream.Stream;

public class PlotCache {

    private final int capacity = 6;
    private final LinkedList<PlotStatus> data = new LinkedList<>();

    public boolean putIfValid(PlotStatus p) {
        // keep newest but not duplicated
        int toBeDeleted = -1;
        int toBeAdded = -1;
        for (int i = 0; i < 6; i++) {
            if (data.size() < i + 1) {
                toBeAdded = i;
                break;
            } else {
                PlotStatus ith = data.get(i);
                if (p.getPlotID().equals(ith.getPlotID()) && p.getLastModified() >= ith.getLastModified()) {
                    toBeDeleted = i;
                }
                if (p.getLastModified() >= ith.getLastModified()) {
                    toBeAdded = i;
                    break;
                } else {
                    continue;
                }
            }
        }
        if (toBeDeleted >= 0) {
            data.remove(toBeDeleted);
        }
        if (toBeAdded >= 0) {
            data.add(toBeAdded, p);
        }
        // keep 6
        if (data.size() > capacity) {
            data.removeLast();
            return true;
        } else {
            return false;
        }
    }

    public void sort() {
        data.sort(PlotStatus::compareTo);
    }

    public Stream<PlotStatus> stream() {
        return this.data.stream();
    }

    public void clear() {
        this.data.clear();
    }
}
