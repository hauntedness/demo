package org.beans;

public class PlotStatus implements Comparable<PlotStatus> {


    private String plotID;

    private int stageOder;

    private String stageValue;

    private int phaseOrder;

    private String phaseName;

    private String progress;

    private long lastModified;

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getStageValue() {
        return stageValue;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
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
