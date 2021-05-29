package org.beans;

import javax.management.relation.InvalidRelationIdException;
import java.io.Serializable;

public class PlotStatus implements Comparable<PlotStatus>, Serializable {

    private String plotID;
    private int stageOder;
    private String stageValue;
    private int phaseOrder;
    private String phaseName;
    private float progress;
    private int elapsedSeconds;
    private int phaseSeconds;
    private int stageSeconds;
    private long lastModified;
    private int forwardPropagationTableTime;
    private int timeForPhase1;
    private int timeForPhase2;
    private int timeForPhase3;
    private int timeForPhase4;
    private int totalTime;
    private int totalCompressTableTime;
    private int copyTime;

    public int getStageSeconds() {
        return stageSeconds;
    }

    public void setStageSeconds(int stageSeconds) {
        this.stageSeconds = stageSeconds;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(int elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public int getPhaseSeconds() {
        return phaseSeconds;
    }

    public void setPhaseSeconds(int phaseSeconds) {
        this.phaseSeconds = phaseSeconds;
    }

    public long getLastModified() {
        return lastModified;
    }


    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getStageValue() {
        return stageValue;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public String getPlotID() {
        return plotID;
    }

    public void setPlotID(String plotID) {
        this.plotID = plotID;
    }

    public int getStageOder() {
        return stageOder;
    }

    public void setStageOder(int stageOder) {
        this.stageOder = stageOder;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public void setStageValue(String stageValue) {
        this.stageValue = stageValue;
    }

    public int getPhaseOrder() {
        return phaseOrder;
    }

    public void setPhaseOrder(int phaseOrder) {
        this.phaseOrder = phaseOrder;
    }


    public PlotStatus merge(PlotStatus other) throws InvalidRelationIdException {
        if (!this.getPlotID().equals(other.getPlotID())) {
            throw new InvalidRelationIdException();
        }
        PlotStatus plotStatus = new PlotStatus();
        plotStatus.setPlotID(this.getPlotID());
        if (this.getPhaseOrder() > other.getPhaseOrder()) {
            plotStatus.setPhaseOrder(this.getPhaseOrder());
            plotStatus.setPhaseName(this.getPhaseName());
            plotStatus.setProgress(this.getProgress());
            /*
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
             */


        } else if (this.getPhaseOrder() == other.getPhaseOrder()) {

        } else {

        }
        return plotStatus;
    }

    @Override
    public String toString() {
        return "phaseName=" + phaseName + ",progress=" + progress + ",stageOder=" + stageOder + ", stageValue=" + stageValue;
    }

    @Override
    public int compareTo(PlotStatus o) {
        if (this.getPhaseOrder() < o.getPhaseOrder()) {
            return -1;
        } else if (this.getPhaseOrder() > o.getPhaseOrder()) {
            return 1;
        } else if (this.getStageOder() < o.getStageOder()) {
            return -1;
        } else if (this.getStageOder() > o.getStageOder()) {
            return 1;
        }
        return 0;
    }
}
