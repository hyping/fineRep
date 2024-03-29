package com.fr.design.mainframe.chart.gui.type;

import java.util.ArrayList;
import java.util.List;

import com.fr.chart.base.AttrAlpha;
import com.fr.chart.chartattr.Area3DPlot;
import com.fr.chart.chartattr.AreaPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.charttypes.AreaIndependentChart;
import com.fr.general.Inter;

/**
 * 面积图 属性表 选择类型 布局 界面.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2012-12-25 下午06:57:36
 */
public class AreaPlotPane extends AbstractChartTypePane{

	private static final int STACK_AREA_CHART = 0;
	private static final int PERCENT_AREA_LINE_CHART = 1;
	private static final int STACK_3D_AREA_CHART = 2;
	private static final int PERCENT_3D_AREA_LINE_CHART = 3;

	@Override
	protected List<ChartImagePane> initDemoList() {
		List <ChartImagePane> demoList = new ArrayList<ChartImagePane>();
		
		String area = Inter.getLocText("FR-Chart-Type_Area");
		String stack = Inter.getLocText("FR-Chart-Type_Stacked");
		String percent = Inter.getLocText("FR-Chart-Use_Percent");
        String[] iconPaths = getTypeIconPath();

		ChartImagePane pane = new ChartImagePane(iconPaths[0], stack + area);
		pane.isPressing = true;
		demoList.add(pane);
		demoList.add(new ChartImagePane(iconPaths[1], percent + stack + area));
		
		String td = Inter.getLocText("FR-Chart-Chart_3D");
		demoList.add(new ChartImagePane(iconPaths[2], td + stack + area));
		demoList.add(new ChartImagePane(iconPaths[3], td + percent + stack + area, true));

		return demoList;
	}

	@Override
	protected List<ChartImagePane> initStyleList() {
		return initNormalStyleList();
	}

	@Override
	protected String[] getTypeIconPath() {
        return new String[]{"/com/fr/design/images/chart/AreaPlot/type/0.png",
                "/com/fr/design/images/chart/AreaPlot/type/1.png",
                "/com/fr/design/images/chart/AreaPlot/type/2.png",
                "/com/fr/design/images/chart/AreaPlot/type/3.png",
                "/com/fr/design/images/chart/AreaPlot/type/4.png",
                "/com/fr/design/images/chart/AreaPlot/type/5.png"};
	}

	@Override
	protected String[] getTypeLayoutPath() {
		return new String[]{"/com/fr/design/images/chart/AreaPlot/layout/0.png",
                "/com/fr/design/images/chart/AreaPlot/layout/1.png",
                "/com/fr/design/images/chart/AreaPlot/layout/2.png",
                "/com/fr/design/images/chart/AreaPlot/layout/3.png",
        };
	}

	/**
	 * 更新界面 内容
	 */
	public void populateBean(Chart chart) {
		super.populateBean(chart);
		Plot plot = chart.getPlot();
		if(plot instanceof AreaPlot) {
			AreaPlot area = (AreaPlot)plot;
			if(area.isStacked()) {
				if(area.getyAxis().isPercentage()) {
					typeDemo.get(PERCENT_AREA_LINE_CHART).isPressing = true;
                    lastTypeIndex = PERCENT_AREA_LINE_CHART;
				} else {
					typeDemo.get(STACK_AREA_CHART).isPressing = true;
                    lastTypeIndex = STACK_AREA_CHART;
				}
			} 
		} else if(plot instanceof Area3DPlot) {
			Area3DPlot threeDPlot = (Area3DPlot)plot;
			if(threeDPlot.isStacked()) {
				if(threeDPlot.getyAxis().isPercentage()) {
					typeDemo.get(PERCENT_3D_AREA_LINE_CHART).isPressing = true;
                    lastTypeIndex = PERCENT_3D_AREA_LINE_CHART;
				} else {
					typeDemo.get(STACK_3D_AREA_CHART).isPressing = true;
                    lastTypeIndex = STACK_3D_AREA_CHART;
				}
			}
		}
		
		checkDemosBackground();
	}

    protected Plot getSelectedClonedPlot(){
        Plot plot = null;
        if(typeDemo.get(STACK_AREA_CHART).isPressing) {
            plot = new AreaPlot();
            ((AreaPlot)plot).setStacked(true);
        }
        else if(typeDemo.get(PERCENT_AREA_LINE_CHART).isPressing) {
            plot = new AreaPlot();
            ((AreaPlot)plot).setStacked(true);
            ((AreaPlot)plot).getyAxis().setPercentage(true);
        }
        else if(typeDemo.get(STACK_3D_AREA_CHART).isPressing) {
            plot = new Area3DPlot();
            ((Area3DPlot)plot).setStacked(true);
        }
        else if(typeDemo.get(PERCENT_3D_AREA_LINE_CHART).isPressing) {
            plot = new Area3DPlot();
            ((Area3DPlot)plot).setStacked(true);
            ((Area3DPlot)plot).getyAxis().setPercentage(true);
        }
        createAreaCondition(plot);
        return plot;
    }

	/**
	 * 保存界面属性 
	 */
	public void updateBean(Chart chart) {
		chart.switchPlot(getSelectedClonedPlot());
		super.updateBean(chart);
	}
	
	private void createAreaCondition(Plot plot) {
		ConditionCollection collection = plot.getConditionCollection();
		AttrAlpha alpha = (AttrAlpha) collection.getDefaultAttr().getExisted(AttrAlpha.class);
		if (alpha == null) {
			alpha = new AttrAlpha();
			collection.getDefaultAttr().addDataSeriesCondition(alpha);
		}
		alpha.setAlpha(0.7f);
	}

	/**
	 * 界面标题
     * @return  界面标题
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Type_Area");
	}

    /**
     * 判断界面是否为Chart 传入
     * @param ob 对象是否为chart
     * @return 是否是chart对象
     */
	public boolean accept(Object ob) {
		if(!super.accept(ob)) {
			return false;
		}
		Chart chart = (Chart) ob;
		Plot plot = chart.getPlot();
		return plot instanceof AreaPlot || plot instanceof Area3DPlot;
	}

    public Chart getDefaultChart() {
        return AreaIndependentChart.areaChartTypes[0];
    }
}
