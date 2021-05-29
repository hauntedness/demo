package org.test;

import org.beans.PlotSlice;

import javax.management.relation.InvalidRelationIdException;
import java.util.ArrayList;
import java.util.Optional;

public class Any {
    public static void main(String[] args) {
        /*
         *
         * Forward propagation table time: 1723.315 seconds
         * Forward propagation table time: 2556.550 seconds.
         * Forward propagation table time: 2645.032 seconds
         * Forward propagation table time: 2490.009 seconds
         * Forward propagation table time: 2384.411 seconds
         * Forward propagation table time: 1691.618 seconds
         * Time for phase 1 = 13872.198 seconds , ignore propagation seconds
         * Time for phase 2 = 5550.739 seconds.
         * Total compress table time: 2027.138 seconds
         * Total compress table time: 2244.129 seconds
         * Total compress table time: 2248.440 seconds
         * Total compress table time: 2306.803 seconds
         * Total compress table time: 2409.666 seconds
         * Total compress table time: 2684.468 seconds
         * Time for phase 3 = 13920.699 seconds
         * Time for phase 4 = 958.017 seconds
         * Total time = 34301.656 seconds
         * Copy time = 2307.775 seconds
         *
         * */
        String[] lines = new String[]{
                "Forward propagation table time: 1723.315 seconds",
                "Forward propagation table time: 2556.550 seconds.",
                "Forward propagation table time: 2645.032 seconds",
                "Forward propagation table time: 2490.009 seconds",
                "Forward propagation table time: 2384.411 seconds",
                "Forward propagation table time: 1691.618 seconds",
                //  "Time for phase 1 = 13872.198 seconds , ignore propagation seconds",
                //"Time for phase 2 = 5550.739 seconds.",
                "Total compress table time: 2027.138 seconds",
                "Total compress table time: 2244.129 seconds",
                "Total compress table time: 2248.440 seconds",
                "Total compress table time: 2306.803 seconds",
                "Total compress table time: 2409.666 seconds",
                //    "Total compress table time: 2684.468 seconds",
                //  "Time for phase 3 = 13920.699 seconds",
                "Time for phase 4 = 958.017 seconds",
                "Total time = 34301.656 seconds",
                "Copy time = 2307.775 seconds"
        };
        ArrayList<PlotSlice> slices = new ArrayList<>();
        for (String line : lines) {
            slices.add(new PlotSlice("1", line));
        }
        Optional<PlotSlice> reduce = slices.stream().reduce((s1, s2) -> {
            try {
                return s1.merge(s2, PlotSlice.MergeFunctions.anyPositive);
            } catch (InvalidRelationIdException e) {
                e.printStackTrace();
            }
            return s1;
        });
        PlotSlice s = reduce.orElse(null);
        System.out.println(s.valuate());
        System.out.println(s);
    }
}
