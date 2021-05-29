package org.beans;

import org.utils.Logger;
import org.utils.Regexp;

public class PlotStatusMapper {

    public static final String StartingPhase = "Starting phase (\\d)/4";
    public static final String ComputingTable = "(?<value>Computing table (?<order>\\d))";
    public static final String TimeForPhase = "(?<value>Time for phase (?<order>\\d) = (?<seconds>\\d+\\.?\\d+) seconds)";
    public static final String TotalTime = "(?<value>Total time = (?<seconds>\\d+\\.?\\d+ seconds))";
    public static final String BackPropagating = "(?<value>Backpropagating on table (?<order>7))";
    public static final String CompressingTables = "(?<value>Compressing tables \\d and (?<order>\\d))";
    public static final String Error = "error|fail|exception";

    public PlotStatus map(String path, String line) {
        PlotStatus plotStatus = new PlotStatus();
        Regexp regexp = Regexp.with(line);
        if (regexp.find(StartingPhase)) {
            String anInt = Regexp.with(line).findFirst(StartingPhase);
            plotStatus.setPhaseOrder(this.parseInt(anInt));
            plotStatus.setStageOder(0);
            plotStatus.setStageValue("Started");
        } else if (regexp.find(ComputingTable)) {
            plotStatus.setPhaseOrder(1);
            String anInt = Regexp.with(line).findGroup(ComputingTable, "order");
            plotStatus.setStageOder(this.parseInt(anInt));
            plotStatus.setStageValue(Regexp.with(line).findGroup(ComputingTable, "value"));
        } else if (regexp.find(BackPropagating)) {
            plotStatus.setPhaseOrder(2);
            String anInt = Regexp.with(line).findGroup(BackPropagating, "order");
            plotStatus.setStageOder(7 - this.parseInt(anInt));
            plotStatus.setStageValue(Regexp.with(line).findGroup(BackPropagating, "value"));
        } else if (regexp.find(CompressingTables)) {
            plotStatus.setPhaseOrder(3);
            String anInt = Regexp.with(line).findGroup(CompressingTables, "order");
            plotStatus.setStageOder(this.parseInt(anInt));
            plotStatus.setStageValue(Regexp.with(line).findGroup(CompressingTables, "value"));
        } else if (regexp.find(TimeForPhase)) {
            String anInt = regexp.findGroup(TimeForPhase, "order");
            plotStatus.setPhaseOrder(this.parseInt(anInt));
            plotStatus.setStageOder(8);
            String value = Regexp.with(line).findGroup(TimeForPhase, "value");
            plotStatus.setStageValue("Ended: " + value);
        } else if (regexp.find(TotalTime)) {
            plotStatus.setPhaseOrder(5);
            plotStatus.setStageOder(1);
            plotStatus.setStageValue(Regexp.with(line).findGroup(TotalTime, "value"));
        } else if (regexp.find(Error)) {
            plotStatus.setPhaseOrder(6);
            plotStatus.setStageOder(999);
            plotStatus.setStageValue(line);
        }
        if (plotStatus.getPhaseOrder() == 1) {
            float order = plotStatus.getStageOder();
            float progress = order / 8 * 40;
            plotStatus.setProgress(progress);
            plotStatus.setPhaseName("Forward Propagation");
        } else if (plotStatus.getPhaseOrder() == 2) {
            float order = plotStatus.getStageOder();
            float progress = 40 + order / 8 * 16;
            plotStatus.setProgress(progress);
            plotStatus.setPhaseName("Backward propagation");
        } else if (plotStatus.getPhaseOrder() == 3) {
            float order = plotStatus.getStageOder();
            float progress = 56 + order / 8 * 40;
            plotStatus.setProgress(progress);
            plotStatus.setPhaseName("Compression tables");
        } else if (plotStatus.getPhaseOrder() == 4) {
            float progress = 96;
            plotStatus.setProgress(progress);
            plotStatus.setPhaseName("Write Checkpoint tables");
        } else if (plotStatus.getPhaseOrder() == 5) {
            plotStatus.setProgress(100);
            plotStatus.setPhaseName("Generate Final File");
        } else if (plotStatus.getPhaseOrder() == 6) {
            plotStatus.setPhaseName("Error occurred");
        }
        plotStatus.setPlotID(path);
        String plotID = (("" + plotStatus.getPlotID()).length() > 14 ?
                plotStatus.getPlotID().substring(plotStatus.getPlotID().length() - 14) : "");
        Logger.logger.info(plotID + ": " + plotStatus);
        return plotStatus;
    }

    public boolean match(String line) {
        Regexp regexp = Regexp.with(line);
        if (regexp.find(StartingPhase)) {
            return true;
        } else if (regexp.find(ComputingTable)) {
            return true;
        } else if (regexp.find(BackPropagating)) {
            return true;
        } else if (regexp.find(CompressingTables)) {
            return true;
        } else if (regexp.find(TimeForPhase)) {
            return true;
        } else if (regexp.find(TotalTime)) {
            return true;
        } else return regexp.find(Error);
    }

    private int parseInt(String value) {
        int number = -1;
        try {
            number = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Logger.logger.error(e.getMessage());
        }
        return number;
    }

}
