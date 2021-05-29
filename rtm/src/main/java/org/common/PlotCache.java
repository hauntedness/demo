package org.common;

import org.beans.PlotSlice;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.stream.Stream;

public class PlotCache {

    private final int capacity = 6;
    private int counted = 0;
    private int totalSeconds = 0;
    private final LinkedList<PlotSlice> data = new LinkedList<>();

    public String predictPercentage(float seconds) {
        float avg;
        if (counted == 0) {
            avg = 10 * 60 * 60;
        } else {
            avg = totalSeconds / counted;
        }
        while (seconds > avg) {
            avg += 100;
        }
        return new DecimalFormat("0.00%").format(seconds / avg);
    }

    public void putIfValid(PlotSlice p) {
        if (p.getCopyTime() > 0 && p.getTotalTime() > 0) {
            totalSeconds += p.getTotalTime() + p.getCopyTime();
            counted++;
        }
        data.push(p);
        data.sort((e1, e2) -> e1.getLastModified() > e2.getLastModified() ? 1 : -1);
        while (data.size() > capacity) {
            data.removeFirst();
        }
    }

    public Stream<PlotSlice> stream() {
        return this.data.stream();
    }

    public void clear() {
        this.data.clear();
    }
}
