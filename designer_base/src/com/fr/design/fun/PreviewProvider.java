package com.fr.design.fun;

import com.fr.design.mainframe.JTemplate;

import java.util.Map;

/**
 * @author richie
 * @date 2015-03-19
 * @since 8.0
 * 自定义预览方式接口
 */
public interface PreviewProvider {

    public static final String MARK_STRING = "PreviewProvider";

    /**
     * 下拉弹出菜单的名字
     * @return 弹出菜单名字
     */
    public String nameForPopupItem();

    /**
     * 下拉弹出菜单的图标路径
     * @return 图标路径
     */
    public String iconPathForPopupItem();

    /**
     * 大图标路径
     * @return 大图标路径
     */
    public String iconPathForLarge();

    /**
     * 点击下拉菜单时触发的事件
     * @param jt 当前的模板对象
     */
    public void onClick(JTemplate<?, ?> jt);

    /**
     * 用于标记预览类型的整数
     * @return 预览类型
     */
    public int previewTypeCode();

    /**
     * 该种预览方式所携带的默认参数集合
     * @return 参数集合
     */
    public Map<String, Object> parametersForPreview();

}
