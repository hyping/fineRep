package com.fr.design.chart.gui.active.action;

import java.awt.event.ActionEvent;

import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.general.Inter;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-22
 * Time   : ����4:49
 */
public class SetDataSheetAction extends ChartComponentAction {

    private static final long serialVersionUID = -4763886493273213850L;

    public SetDataSheetAction(ChartComponent chartComponent) {
        super(chartComponent);
        this.setName(Inter.getLocText("Chart_Set_Data_Sheet"));
    }

    public void actionPerformed(ActionEvent e) {
        showDataSheetStylePane();
    }

    public void showDataSheetStylePane() {
    	ChartEditPane.getInstance().GoToPane(PaneTitleConstants.CHART_STYLE_TITLE, PaneTitleConstants.CHART_STYLE_DATA_TITLE);
    }
}
