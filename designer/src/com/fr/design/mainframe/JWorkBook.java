package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Parameter;
import com.fr.design.DesignModelAdapter;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.AllowAuthorityEditAction;
import com.fr.design.actions.ExitAuthorityEditAction;
import com.fr.design.actions.file.WebPreviewUtils;
import com.fr.design.actions.file.export.*;
import com.fr.design.actions.report.ReportExportAttrAction;
import com.fr.design.actions.report.ReportParameterAction;
import com.fr.design.actions.report.ReportWebAttrAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.designer.TargetComponent;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.fun.PreviewProvider;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIModeControlContainer;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.mainframe.cell.QuickEditorRegion;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.*;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.parameter.ParameterDefinitePane;
import com.fr.design.parameter.ParameterInputPane;
import com.fr.design.preview.PagePreview;
import com.fr.design.preview.ViewPreview;
import com.fr.design.preview.WritePreview;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.selection.QuickEditor;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.design.write.submit.SmartInsertDBManipulationInWidgetEventPane;
import com.fr.design.write.submit.SmartInsertDBManipulationPane;
import com.fr.env.RemoteEnv;
import com.fr.file.FILE;
import com.fr.file.FileNodeFILE;
import com.fr.file.filetree.FileNode;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.general.ModuleContext;
import com.fr.general.web.ParameterConsts;
import com.fr.io.exporter.EmbeddedTableDataExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.WorkBook;
import com.fr.main.parameter.ReportParameterAttr;
import com.fr.poly.PolyDesigner;
import com.fr.privilege.finegrain.WorkSheetPrivilegeControl;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.module.Module;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JWorkBook used to edit WorkBook.
 */
public class JWorkBook extends JTemplate<WorkBook, WorkBookUndoState> {

    private static final String SHARE_SUFFIX = "_share";
    private static final String SHARE_FOLDER = "share";
    private static final int TOOLBARPANEDIMHEIGHT = 26;

    private UIModeControlContainer centerPane;
    private ReportComponentComposite reportComposite;
    private ParameterDefinitePane parameterPane;

    public JWorkBook() {
        super(new WorkBook(new WorkSheet()), "WorkBook");
        populateReportParameterAttr();
    }

    public JWorkBook(WorkBook workBook, String fileName) {
        super(workBook, fileName);
        populateReportParameterAttr();
    }

    public JWorkBook(WorkBook workBook, FILE file) {
        super(workBook, file);
        populateReportParameterAttr();
    }

    @Override
    protected UIModeControlContainer createCenterPane() {
        parameterPane = ModuleContext.isModuleStarted(Module.FORM_MODULE) ? new ParameterDefinitePane() : null;
        centerPane = new UIModeControlContainer(parameterPane, reportComposite = new ReportComponentComposite(this)) {
            @Override
            protected void onModeChanged() {
                refreshToolArea();
            }

            @Override
            protected void onResize(int distance) {
                if (hasParameterPane()) {
                    parameterPane.setDesignHeight(distance);
                    fireTargetModified();
                }
            }
        };

        reportComposite.addTargetModifiedListener(new TargetModifiedListener() {

            @Override
            public void targetModified(TargetModifiedEvent e) {
                JWorkBook.this.fireTargetModified();
            }
        });

        reportComposite.setParentContainer(centerPane);
        return centerPane;
    }

    /**
     * 判断sheet权限
     *
     * @param rolsName 角色
     */
    public void judgeSheetAuthority(String rolsName) {
        boolean isCovered = reportComposite.getEditingTemplateReport().getWorkSheetPrivilegeControl().checkInvisible(rolsName);
        centerPane.setSheeetCovered(isCovered);
        centerPane.refreshContainer();
    }

    /**
     * 在编辑的面板是被参考的面板时，取消格式刷
     */
    public void doConditionCancelFormat() {
        if (ComparatorUtils.equals(reportComposite.centerCardPane.editingComponet.elementCasePane, DesignerContext.getReferencedElementCasePane())) {
            cancelFormat();
        }
    }

    /**
     * 无条件取消格式刷
     */
    public void cancelFormat() {
        DesignerContext.setFormatState(DesignerContext.FORMAT_STATE_NULL);
        reportComposite.centerCardPane.editingComponet.elementCasePane.getGrid().setCursor(UIConstants.CELL_DEFAULT_CURSOR);
        ((ElementCasePane) DesignerContext.getReferencedElementCasePane()).getGrid().setCursor(UIConstants.CELL_DEFAULT_CURSOR);
        ((ElementCasePane) DesignerContext.getReferencedElementCasePane()).getGrid().setNotShowingTableSelectPane(true);
        DesignerContext.setReferencedElementCasePane(null);
        DesignerContext.setReferencedIndex(0);
        this.repaint();
    }

    public int getEditingReportIndex() {
        return reportComposite.getEditingIndex();
    }

    /**
     * 创建权限细粒度面板
     *
     * @return 返回权限细粒度面板
     */
    public AuthorityEditPane createAuthorityEditPane() {
        if (centerPane.isUpEditMode()) {
            return parameterPane.getParaDesigner().getAuthorityEditPane();
        } else {
            WorkSheetPrivilegeControl workSheetPrivilegeControl = reportComposite.getEditingTemplateReport().getWorkSheetPrivilegeControl();
            if (workSheetPrivilegeControl.checkInvisible(ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName())) {
                SheetAuthorityEditPane sheetEditedPane = new SheetAuthorityEditPane(reportComposite.getEditingWorkBook(), this.getEditingReportIndex());
                sheetEditedPane.populateDetials();
                return sheetEditedPane;
            }
            return reportComposite.getEditingReportComponent().createAuthorityEditPane();
        }

    }

    public ToolBarMenuDockPlus getToolBarMenuDockPlus() {
        if (this.getEditingElementCasePane() == null) {
            return JWorkBook.this;
        }
        this.getEditingElementCasePane().getGrid().setEditable(!BaseUtils.isAuthorityEditing());
        centerPane.needToShowCoverAndHidPane();
        if (centerPane.isUpEditMode()) {
            return parameterPane;
        } else {
            return JWorkBook.this;
        }
    }

    private boolean hasParameterPane() {
        return parameterPane != null;
    }

    /**
     *
     */
    public void setAutoHeightForCenterPane() {
        centerPane.setUpPaneHeight(hasParameterPane() ? parameterPane.getPreferredSize().height : 0);
    }

    @Override
    /**
     *
     */
    public void setComposite() {
        super.setComposite();
        reportComposite.setComponents();
    }

    public JPanel getEastUpPane() {
        if (BaseUtils.isAuthorityEditing()) {
            return allowAuthorityUpPane();
        } else {
            return exitEastUpPane();
        }
    }

    private JPanel allowAuthorityUpPane() {
        //初始时显示不支持权限编辑的情况
        //1.编辑参数面板，参数面板什么也没有选中
        //2.在报表主体选中的聚合块不是报表聚合块，是图标聚合块
        boolean isParameterNotSuppportAuthority = centerPane.isUpEditMode() && !parameterPane.getParaDesigner().isSupportAuthority();
        boolean isReportNotSupportAuthority = reportComposite.getEditingReportComponent() instanceof PolyDesigner
                && !((PolyDesigner) reportComposite.getEditingReportComponent()).isSelectedECBolck();
        WorkSheetPrivilegeControl workSheetPrivilegeControl = reportComposite.getEditingTemplateReport().getWorkSheetPrivilegeControl();
        if (!centerPane.isUpEditMode() && workSheetPrivilegeControl.checkInvisible(ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName())) {
            AuthoritySheetEditedPane sheetEditedPane = new AuthoritySheetEditedPane(reportComposite.getEditingWorkBook(), this.getEditingReportIndex());
            sheetEditedPane.populate();
            return sheetEditedPane;
        }
        boolean isSelectedDownSupport = !centerPane.isUpEditMode() && isReportNotSupportAuthority;
        if (isParameterNotSuppportAuthority || isSelectedDownSupport) {
            return new NoSupportAuthorityEdit();
        }

        return new AuthorityPropertyPane(this);
    }

    private JPanel exitEastUpPane() {
        if (centerPane.isUpEditMode()) {
            return parameterPane.getParaDesigner().getEastUpPane();
        }
        if (delegate4ToolbarMenuAdapter() instanceof PolyDesigner) {
            return ((PolyDesigner) delegate4ToolbarMenuAdapter()).getEastUpPane();
        } else {
            ElementCasePane casePane = ((ReportComponent) delegate4ToolbarMenuAdapter()).elementCasePane;
            if (casePane != null) {
                return casePane.getEastUpPane();
            }
        }
        return new JPanel();
    }

    public JPanel getEastDownPane() {
        if (centerPane.isUpEditMode()) {
            return parameterPane.getParaDesigner().getEastDownPane();
        }
        if (delegate4ToolbarMenuAdapter() instanceof PolyDesigner) {
            if (((PolyDesigner) delegate4ToolbarMenuAdapter()).getSelectionType() == PolyDesigner.SelectionType.NONE) {
                return new JPanel();
            } else {
                return ((PolyDesigner) delegate4ToolbarMenuAdapter()).getEastDownPane();
            }
        } else {
            ElementCasePane casePane = ((ReportComponent) delegate4ToolbarMenuAdapter()).elementCasePane;
            if (casePane != null) {
                return casePane.getEastDownPane();
            }
        }
        return new JPanel();
    }

    /**
     * 移除选择
     */
    public void removeTemplateSelection() {
        this.reportComposite.removeSelection();
    }

    public void setSheetCovered(boolean isCovered) {
        centerPane.setSheeetCovered(isCovered);
    }

    /**
     * 刷新容器
     */
    public void refreshContainer() {
        centerPane.refreshContainer();
    }

    /**
     * 移除参数面板选择
     */
    public void removeParameterPaneSelection() {
        parameterPane.getParaDesigner().removeSelection();
    }

    public int getToolBarHeight() {
        return TOOLBARPANEDIMHEIGHT;
    }

    /**
     * 更新报表参数属性
     */
    public void populateReportParameterAttr() {
        if (hasParameterPane()) {
            parameterPane.populate(this);
            setAutoHeightForCenterPane();
        }
    }

    /**
     * 更新ReportParameterAttr
     */
    public void updateReportParameterAttr() {
        if (hasParameterPane()) {
            ReportParameterAttr rpt = this.parameterPane.update(this.getTarget().getReportParameterAttr());
            this.getTarget().setReportParameterAttr(rpt);
        }
    }

    /**
     * 检查提交按钮
     */
    public void checkHasSubmitButton() {
        if (parameterPane != null) {
            parameterPane.checkSubmitButton();
        }
    }

    // ////////////////////////////////////////////////////////
    // //////////////////////OLD BELOW/////////////////////////
    // ////////////////////////////////////////////////////////

    @Override
    /**
     * set target
     */
    public void setTarget(WorkBook book) {
        if (book == null) {
            return;
        }

        if (book.getReportCount() == 0) {
            book.addReport(new WorkSheet());
        }

        super.setTarget(book);
    }

    private TargetComponent delegate4ToolbarMenuAdapter() {
        return this.reportComposite.getEditingReportComponent();
    }

    /**
     * 复制
     */
    public void copy() {
        this.delegate4ToolbarMenuAdapter().copy();
    }

    /**
     * 剪切
     *
     * @return 剪切成功返回true
     */
    public boolean cut() {
        return this.delegate4ToolbarMenuAdapter().cut();
    }

    /**
     * 黏贴
     *
     * @return 黏贴成功返回true
     */
    public boolean paste() {
        return this.delegate4ToolbarMenuAdapter().paste();
    }

    /**
     * 停止编辑
     */
    public void stopEditing() {
        reportComposite.stopEditing();
        if (!this.isSaved()) {
            this.updateReportParameterAttr();
            this.delegate4ToolbarMenuAdapter().stopEditing();
        }
    }

    /**
     * 后缀
     *
     * @return 后缀的字符串
     */
    public String suffix() {
        return ".cpt";
    }


    // ////////////////////////////////////////////////////////////////////
    // ////////////////for toolbarMenuAdapter//////////////////////////////
    // ////////////////////////////////////////////////////////////////////

    /**
     * 文件菜单的子菜单
     *
     * @return 子菜单
     */
    public ShortCut[] shortcut4FileMenu() {
        return (ShortCut[]) ArrayUtils.addAll(
                super.shortcut4FileMenu(),
                BaseUtils.isAuthorityEditing() || (FRContext.getCurrentEnv() instanceof RemoteEnv) ? new ShortCut[0] : new ShortCut[]{this.createWorkBookExportMenu()}
        );
    }

    /**
     * 目标的菜单
     *
     * @return 菜单
     */
    public MenuDef[] menus4Target() {
        return (MenuDef[]) ArrayUtils.addAll(
                super.menus4Target(), this.delegate4ToolbarMenuAdapter().menus4Target()
        );
    }

    public int getMenuState() {
        return this.delegate4ToolbarMenuAdapter().getMenuState();
    }

    private MenuDef createWorkBookExportMenu() {
        MenuDef excelExportMenuDef = new MenuDef(KeySetUtils.EXCEL_EXPORT.getMenuKeySetName(), KeySetUtils.EXCEL_EXPORT.getMnemonic());
        excelExportMenuDef.setIconPath("/com/fr/design/images/m_file/excel.png");
        excelExportMenuDef
                .addShortCut(new PageExcelExportAction(this), new ExcelExportAction(this), new PageToSheetExcelExportAction(this));
        // Export - MenuDef
        MenuDef exportMenuDef = new MenuDef(KeySetUtils.EXPORT.getMenuName());
        exportMenuDef.setIconPath("/com/fr/design/images/m_file/export.png");

        exportMenuDef.addShortCut(excelExportMenuDef, new PDFExportAction(this), new WordExportAction(this), new SVGExportAction(this),
                new CSVExportAction(this), new TextExportAction(this), new EmbeddedExportExportAction(this));

        return exportMenuDef;
    }

    /**
     * 权限细粒度情况下的子菜单
     *
     * @return 子菜单
     */
    public ShortCut[] shortCuts4Authority() {
        return new ShortCut[]{
                new NameSeparator(Inter.getLocText(new String[]{"DashBoard-Potence", "Edit"})),
                BaseUtils.isAuthorityEditing() ? new ExitAuthorityEditAction(this) : new AllowAuthorityEditAction(this),
        };

    }

    /**
     * 模板的子菜单
     *
     * @return 子菜单
     */
    public ShortCut[] shortcut4TemplateMenu() {
        return (ShortCut[]) ArrayUtils.addAll(new ShortCut[]{
                new ReportWebAttrAction(this),
                new ReportExportAttrAction(this),
                new ReportParameterAction(this),
                new NameSeparator(Inter.getLocText("Utils-Current_Sheet")),
        }, this.reportComposite.getEditingReportComponent().shortcut4TemplateMenu());
    }

    /**
     * 模板的工具
     *
     * @return 工具
     */
    public ToolBarDef[] toolbars4Target() {
        return this.delegate4ToolbarMenuAdapter().toolbars4Target();
    }

    @Override
    protected WorkBookUndoState createUndoState() {
        return new WorkBookUndoState(
                this,
                this.reportComposite.getSelectedIndex(),
                this.reportComposite.getEditingReportComponent().createEditingState()
        );
    }

    @Override
    protected void applyUndoState(WorkBookUndoState u) {
        try {
            this.setTarget((WorkBook) u.getWorkBook().clone());
            if (!BaseUtils.isAuthorityEditing()) {
                if (u.getAuthorityType() != BaseUndoState.NORMAL_STATE) {
                    applyAll(u);
                    this.undoState = u;
                    return;
                }
                if (centerPane.isUpEditMode()) {
                    if (hasParameterPane()) {
                        parameterPane.populate(u.getApplyTarget());
                        DesignModuleFactory.getFormHierarchyPane().refreshRoot();
                    }
                } else {
                    reportComposite.setSelectedIndex(u.getSelectedReportIndex());
                    u.getSelectedEditingState().revert();
                    TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter()).refreshDockingView();
                }
                this.undoState = u;
            } else {
                //参数面板
                applyAll(u);
                this.authorityUndoState = u;
            }

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void applyAll(WorkBookUndoState u) {
        if (hasParameterPane()) {
            parameterPane.populate(u.getApplyTarget());
            DesignModuleFactory.getFormHierarchyPane().refreshRoot();
        }
        //报表主体
        reportComposite.setSelectedIndex(u.getSelectedReportIndex());
        u.getSelectedEditingState().revert();
        TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter()).refreshDockingView();
        //如果是在权限编辑状态下，则有工具栏
        DesignerContext.getDesignerFrame().resetToolkitByPlus(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate());
    }

    /**
     * 请求焦点
     */
    public void requestFocus() {
        super.requestFocus();
        ReportComponent reportComponent = reportComposite.getEditingReportComponent();
        reportComponent.requestFocus();
    }

    /**
     * @return
     */
    public final TemplateElementCase getEditingElementCase() {
        return this.reportComposite.getEditingReportComponent().getEditingElementCasePane().getEditingElementCase();
    }

    /**
     * 获取当前workBook中的正在编辑的component对应的elementCasePane
     *
     * @return
     */
    public final ElementCasePane getEditingElementCasePane() {
        return this.reportComposite.getEditingReportComponent().getEditingElementCasePane();
    }

    /**
     * 刷新所有的控件
     */
    public void refreshAllNameWidgets() {
        if (parameterPane != null) {
            parameterPane.refreshAllNameWidgets();
        }
    }

    /**
     * 为数据集刷新参数面板
     *
     * @param oldName 旧名字
     * @param newName 新名字
     */
    public void refreshParameterPane4TableData(String oldName, String newName) {
        if (parameterPane != null) {
            parameterPane.refresh4TableData(oldName, newName);
        }
    }

    /**
     * 恢复
     */
    public void revert() {
        ElementCasePane epane = reportComposite.getEditingReportComponent().elementCasePane;
        if (epane == null) {
            return;
        }
        if (delegate4ToolbarMenuAdapter() instanceof PolyDesigner) {
            PolyDesigner polyDesigner = (PolyDesigner) delegate4ToolbarMenuAdapter();
            if (polyDesigner.getSelectionType() == PolyDesigner.SelectionType.NONE || polyDesigner.getSelection() == null) {
                QuickEditorRegion.getInstance().populate(QuickEditor.DEFAULT_EDITOR);
            } else {
                QuickEditorRegion.getInstance().populate(epane.getCurrentEditor());
            }
        } else {
            QuickEditorRegion.getInstance().populate(epane.getCurrentEditor());
        }
        CellElementPropertyPane.getInstance().populate(epane);
    }

    @Override
    protected WorkBookModelAdapter createDesignModel() {
        return new WorkBookModelAdapter(this);
    }

    /**
     * 表单的工具栏
     *
     * @return 表单工具栏
     */
    public JPanel[] toolbarPanes4Form() {
        if (centerPane.isUpEditMode() && hasParameterPane()) {
            return parameterPane.toolbarPanes4Form();
        }
        return new JPanel[0];
    }

    /**
     * 表单的工具按钮
     *
     * @return 工具按钮
     */
    public JComponent[] toolBarButton4Form() {
        centerPane.needToShowCoverAndHidPane();
        if (centerPane.isUpEditMode() && hasParameterPane()) {
            return parameterPane.toolBarButton4Form();
        } else {
            return this.delegate4ToolbarMenuAdapter().toolBarButton4Form();
        }
    }

    /**
     * 权限细粒度状态下的工具面板
     *
     * @return 工具面板
     */
    public JComponent toolBar4Authority() {
        return new AuthorityToolBarPane();
    }

    /**
     * 是否支持预览
     *
     * @return 预览接口
     *
     */
    public PreviewProvider[] supportPreview() {
        return (PreviewProvider[])ArrayUtils.addAll(new PreviewProvider[]{
                new PagePreview(), new WritePreview(), new ViewPreview()
        }, ExtraDesignClassManager.getInstance().getPreviewProviders());
    }

    /**
     * 预览菜单项
     *
     * @return 预览菜单项
     */
    public UIMenuItem[] createMenuItem4Preview() {
        List<UIMenuItem> menuItems = new ArrayList<UIMenuItem>();
        PreviewProvider[] previewProviders = supportPreview();
        for (final PreviewProvider provider : previewProviders) {
            UIMenuItem item = new UIMenuItem(provider.nameForPopupItem(), BaseUtils.readIcon(provider.iconPathForPopupItem()));
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    provider.onClick(JWorkBook.this);
                }
            });
            menuItems.add(item);
        }
        return menuItems.toArray(new UIMenuItem[menuItems.size()]);
    }

    /**
     * 预览按钮点击事件
     *
     * @param provider 预览接口
     */
    public void previewMenuActionPerformed(PreviewProvider provider) {
        setPreviewType(provider);
        WebPreviewUtils.actionPerformed(this, provider.parametersForPreview(), ParameterConsts.REPORTLET);
    }

    /**
     * 是不是模板
     *
     * @return 是则返回true
     */
    public boolean isJWorkBook() {
        return true;
    }

    public void setAuthorityMode(boolean isUpMode) {
        centerPane.setAuthorityMode(isUpMode);
    }

    /**
     * 是不是正在编辑参数面板
     *
     * @return 是则返回true
     */
    public boolean isUpMode() {
        return centerPane.isUpEditMode();
    }

    /**
     * 刷新参数和工具区域
     */
    public void refreshToolArea() {
        populateReportParameterAttr();
        if (centerPane.isUpEditMode()) {
            if (hasParameterPane()) {
                DesignerContext.getDesignerFrame().resetToolkitByPlus(parameterPane);
                parameterPane.initBeforeUpEdit();
            }
        } else {
            DesignerContext.getDesignerFrame().resetToolkitByPlus(JWorkBook.this);
            if (delegate4ToolbarMenuAdapter() instanceof PolyDesigner) {
                PolyDesigner polyDesigner = (PolyDesigner) delegate4ToolbarMenuAdapter();
                if (polyDesigner.getSelectionType() == PolyDesigner.SelectionType.NONE || polyDesigner.getSelection() == null) {
                    EastRegionContainerPane.getInstance().replaceDownPane(new JPanel());
                    QuickEditorRegion.getInstance().populate(QuickEditor.DEFAULT_EDITOR);
                } else {
                    EastRegionContainerPane.getInstance().replaceDownPane(CellElementPropertyPane.getInstance());
                }
                EastRegionContainerPane.getInstance().replaceUpPane(QuickEditorRegion.getInstance());
            } else {
                ElementCasePane casePane = ((ReportComponent) delegate4ToolbarMenuAdapter()).elementCasePane;
                if (casePane != null) {
                    casePane.fireSelectionChangeListener();
                }
            }
        }
        if (BaseUtils.isAuthorityEditing()) {
            EastRegionContainerPane.getInstance().replaceUpPane(allowAuthorityUpPane());
            EastRegionContainerPane.getInstance().replaceDownPane(RolesAlreadyEditedPane.getInstance());
        }
        centerPane.needToShowCoverAndHidPane();
    }

    @Override
    /**
     *
     */
    public Icon getPreviewLargeIcon() {
        PreviewProvider provider = getPreviewType();
        String iconPath = provider.iconPathForLarge();
        return BaseUtils.readIcon(iconPath);
    }

    /**
     * 获取当前workBook的参数及默认值
     * 同名参数的话模板参数覆盖全局参数
     *
     * @return
     */
    public Parameter[] getParameters() {
        Parameter[] ps = this.parameterPane.getParameterArray();
        Parameter[] curPs = this.parameterPane.getAllParameters();
        for (int i = 0; i < ps.length; i++) {
            for (int j = 0; j < curPs.length; j++) {
                if (ComparatorUtils.equals(ps[i].getName(), curPs[j].getName())) {
                    ps[i].setValue(curPs[j].getValue());
                }
            }
        }
        return ps;
    }

    /**
     * 请求单元格区域的焦点
     */
    public void requestGridFocus() {
        reportComposite.centerCardPane.requestGrifFocus();
    }


    /**
     * 创建内置sql提交的pane
     *
     * @return 内置sql提交的pane
     * @date 2014-10-14-下午7:39:27
     */
    public DBManipulationPane createDBManipulationPane() {
        ElementCasePane<TemplateElementCase> epane = this.getEditingElementCasePane();
        return new SmartInsertDBManipulationPane(epane);
    }

    /**
     * 创建控件事件里内置sql提交的pane
     *
     * @return 内置sql提交的pane
     * @date 2014-10-14-下午7:39:27
     */
    public DBManipulationPane createDBManipulationPaneInWidget() {
        ElementCasePane<TemplateElementCase> epane = this.getEditingElementCasePane();
        return new SmartInsertDBManipulationInWidgetEventPane(epane);
    }

    public Icon getIcon() {
        return BaseUtils.readIcon("/com/fr/design/images/buttonicon/newcpts.png");
    }

    /**
     * 创建sheet名称tab面板
     *
     * @param reportCompositeX 当前组件对象
     * @return sheet名称tab面板
     * @date 2015-2-5-上午11:42:12
     */
    public SheetNameTabPane createSheetNameTabPane(ReportComponentComposite reportCompositeX) {
        return new SheetNameTabPane(reportCompositeX);
    }

    /**
     * 将模板另存为可以分享出去的混淆后内置数据集模板
     *
     * @return 是否另存成功
     */
    public boolean saveShareFile() {
        FILE newFile = createNewEmptyFile();
        //如果文件已经打开, 那么就覆盖关闭掉他
        MutilTempalteTabPane.getInstance().closeFileTemplate(newFile);
        final TemplateWorkBook tpl = this.getTarget();
        // 弹出输入参数
        java.util.Map<String, Object> parameterMap = inputParameters(tpl);

        try {
            String fullPath = StableUtils.pathJoin(FRContext.getCurrentEnv().getPath(), newFile.getPath());
            FileOutputStream fileOutputStream = new FileOutputStream(fullPath);
            EmbeddedTableDataExporter exporter = new EmbeddedTableDataExporter();
            exporter.export(fileOutputStream, (WorkBook) tpl, parameterMap);
        } catch (Exception e1) {
            FRContext.getLogger().error(e1.getMessage());
        }

        //打开导出的内置模板
        DesignerContext.getDesignerFrame().openTemplate(newFile);
        return true;
    }

    //创建新的空白模板
    private FILE createNewEmptyFile() {
        String oldName = this.getEditingFILE().getName();
        oldName = oldName.replaceAll(ProjectConstants.CPT_SUFFIX, StringUtils.EMPTY);
        String shareFileName = oldName + SHARE_SUFFIX;
        String newFilePath = StableUtils.pathJoin(ProjectConstants.REPORTLETS_NAME, SHARE_FOLDER, shareFileName, shareFileName + ProjectConstants.CPT_SUFFIX);
        FileNode node = new FileNode(newFilePath, false);

        FileNodeFILE newFile = new FileNodeFILE(node);
        mkNewFile(newFile);

        return newFile;
    }

    //输入导出内置数据集需要的参数
    private Map<String, Object> inputParameters(final TemplateWorkBook tpl) {
        final java.util.Map<String, Object> parameterMap = new java.util.HashMap<String, Object>();
        DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
        Parameter[] parameters = tpl.getParameters();
        if (!ArrayUtils.isEmpty(parameters)) {// 检查Parameter.
            final ParameterInputPane pPane = new ParameterInputPane(
                    parameters);
            pPane.showSmallWindow(designerFrame, new DialogActionAdapter() {

                @Override
                public void doOk() {
                    parameterMap.putAll(pPane.update());
                }
            }).setVisible(true);
        }

        return parameterMap;
    }

    /**
     * 创建分享模板的按钮, 目前只有jworkbook实现了
     *
     * @return 分享模板按钮
     */
    public UIButton[] createShareButton() {
        return new UIButton[0];
        //产品想要重新设计下, 1现在的分享多列数据集很麻烦, 2想做成自动上传附件.
//        return new UIButton[]{new ShareButton()};
    }

}
