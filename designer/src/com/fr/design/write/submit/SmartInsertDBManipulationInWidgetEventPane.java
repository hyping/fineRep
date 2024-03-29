package com.fr.design.write.submit;

import com.fr.design.mainframe.ElementCasePane;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: zheng
 * Date: 14-4-24
 * Time: ����10:48
 */
public class SmartInsertDBManipulationInWidgetEventPane extends SmartInsertDBManipulationPane {

    public SmartInsertDBManipulationInWidgetEventPane(ElementCasePane ePane){
      super(ePane);
    }

    protected void setBorderAndLayout(JPanel jPanel){
        jPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    protected void addComponent(JPanel mainPane,JScrollPane addPane){
    }

    protected Dimension createConditionPanePreferredSize(){
        return new Dimension(454, 30);
    }

    protected Dimension createControlBtnPanePreferredSize(){
        return new Dimension(92, 20);
    }

    protected String setControlBtnPanePosition(){
        return  BorderLayout.WEST;
    }
}
