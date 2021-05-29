package org.beans;

import org.utils.Logger;
import org.utils.Regexp;

import javax.management.relation.InvalidRelationIdException;
import java.io.Serializable;

public class PlotSlice implements Serializable {

    private String plotID;
    private long lastModified;
    private Float forwardPropagationTableTime;
    private Float timeForPhase1;
    private Float timeForPhase2;
    private Float timeForPhase3;
    private Float timeForPhase4;
    private Float totalTime;
    private Float totalCompressTableTime;
    private Float copyTime;
    private String error;


    public Float getTotalTime() {
        return totalTime;
    }

    public Float getCopyTime() {
        return copyTime;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getPlotID() {
        return plotID;
    }

    public String getError() {
        return error;
    }

    public void setPlotID(String plotID) {
        this.plotID = plotID;
    }

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
    public PlotSlice(String plotID, String line) {
        this.plotID = plotID;
        Regexp regexp = Regexp.with(line);
        if (regexp.find("error|exception|fail")) {
            error = line;
        } else {
            error = "";
        }
        String timeLike = "(\\d+\\.\\d*) seconds";
        forwardPropagationTableTime = parseFloat(regexp.findFirst("Forward propagation table time: " + timeLike));
        timeForPhase1 = parseFloat(regexp.findFirst("Time for phase 1 = " + timeLike));
        timeForPhase2 = parseFloat(regexp.findFirst("Time for phase 2 = " + timeLike));
        timeForPhase3 = parseFloat(regexp.findFirst("Time for phase 3 = " + timeLike));
        timeForPhase4 = parseFloat(regexp.findFirst("Time for phase 4 = " + timeLike));
        totalTime = parseFloat(regexp.findFirst("Total time = " + timeLike));
        totalCompressTableTime = parseFloat(regexp.findFirst("Total compress table time: " + timeLike));
        copyTime = parseFloat(regexp.findFirst("Copy time = " + timeLike));
    }

    private Float parseFloat(String value) {
        float number = 0f;
        try {
            number = Float.parseFloat(value);
        } catch (NumberFormatException e) {
            Logger.logger.error(e.getMessage());
        }
        return number;
    }


    @FunctionalInterface
    public interface MergeFunction<T> {
        T merge(T n1, T n2);
    }

    public static class MergeFunctions {
        public static MergeFunction<Float> anyPositive = (n1, n2) -> n2 > 0 ? n2 : n1;
    }

    public PlotSlice merge(PlotSlice other, MergeFunction<Float> function) throws InvalidRelationIdException {
        if (function == null) {
            function = MergeFunctions.anyPositive;
        }
        if (!this.plotID.equals(other.plotID)) {
            throw new InvalidRelationIdException();
        }
        other.plotID = this.plotID;
        other.lastModified = Math.max(this.lastModified, other.lastModified);
        other.totalTime = function.merge(this.totalTime, other.totalTime);
        other.copyTime = function.merge(this.copyTime, other.copyTime);
        other.timeForPhase1 = function.merge(this.timeForPhase1, other.timeForPhase1);
        other.timeForPhase2 = function.merge(this.timeForPhase2, other.timeForPhase2);
        other.timeForPhase3 = function.merge(this.timeForPhase3, other.timeForPhase3);
        other.timeForPhase4 = function.merge(this.timeForPhase4, other.timeForPhase4);
        other.totalCompressTableTime = this.totalCompressTableTime + other.totalCompressTableTime;
        other.forwardPropagationTableTime = this.forwardPropagationTableTime + other.forwardPropagationTableTime;
        other.error = this.error == null ? other.error : "";
        return other;
    }

    public Float valuate() {
        float seconds = this.copyTime;
        if (this.totalTime > 0) {
            seconds += totalTime;
        } else {
            if (this.timeForPhase1 == 0) {
                seconds += this.forwardPropagationTableTime;
            } else {
                seconds += this.timeForPhase1;
            }
            seconds += this.timeForPhase2;
            if (this.timeForPhase3 == 0) {
                seconds += this.totalCompressTableTime;
            } else {
                seconds += this.timeForPhase3;
            }
            seconds += this.timeForPhase4;
        }
        return seconds;
    }


    @Override
    public String toString() {
        return "PlotSlice{" +
                "plotID='" + plotID + '\'' +
                ", lastModified=" + lastModified +
                ", forwardPropagationTableTime=" + forwardPropagationTableTime +
                ", timeForPhase1=" + timeForPhase1 +
                ", timeForPhase2=" + timeForPhase2 +
                ", timeForPhase3=" + timeForPhase3 +
                ", timeForPhase4=" + timeForPhase4 +
                ", totalTime=" + totalTime +
                ", totalCompressTableTime=" + totalCompressTableTime +
                ", copyTime=" + copyTime +
                ", error='" + error + '\'' +
                '}';
    }
}
