package com.budgeez.model.entities.external;

import com.budgeez.model.entities.dao.ChartSelection;

import javax.validation.constraints.NotNull;

public class ChartRequestWrapper {

    @NotNull(message = "error.chartselection.notblank")
    private ChartSelection chartSelection;
    private DatePicker datePicker;

    public ChartSelection getChartSelection() {
        return chartSelection;
    }

    public void setChartSelection(ChartSelection chartSelection) {
        this.chartSelection = chartSelection;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(DatePicker datePicker) {
        this.datePicker = datePicker;
    }

    @Override
    public String toString() {
        return "ChartRequestWrapper{" +
                "chartSelection=" + chartSelection.toString() +
                ", datePicker=" + datePicker +
                '}';
    }
}
