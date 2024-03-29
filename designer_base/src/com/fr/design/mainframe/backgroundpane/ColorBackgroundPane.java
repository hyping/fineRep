package com.fr.design.mainframe.backgroundpane;

import com.fr.base.background.ColorBackground;
import com.fr.design.event.UIObserverListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.design.style.color.NewColorSelectPane;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * @author zhou
 * @since 2012-5-29下午1:12:14
 */
public class ColorBackgroundPane extends BackgroundSettingPane {

	private NewColorSelectPane detailColorSelectPane;

	public ColorBackgroundPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());

		detailColorSelectPane = new NewColorSelectPane();
		this.add(detailColorSelectPane, BorderLayout.NORTH);
	}

	public void populateBean(Background background) {
		ColorBackground colorBackgroud = (ColorBackground) background;
		this.detailColorSelectPane.setColor(colorBackgroud.getColor());
	}

	public Background updateBean() {
		this.detailColorSelectPane.updateUsedColor();
		return ColorBackground.getInstance(this.detailColorSelectPane.getNotNoneColor());
	}

	/**
	 * 给组件登记一个观察者监听事件
	 *
	 * @param listener 观察者监听事件
	 */
	public void registerChangeListener(final UIObserverListener listener) {
		detailColorSelectPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				listener.doChange();
			}
		});
	}

	@Override
	/**
	 * 是否为ColorBackground 类型
	 * 
	 * @param background 背景
	 * @return 同上
	 * 
	 */
	public boolean accept(Background background) {
		return background instanceof ColorBackground;
	}

	@Override
	/**
	 * 窗口名称
	 * @return 同上
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("Color");
	}
}
