/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import com.fr.base.FRContext;

/**
 * Used for JList.
 */
public class ArrayListTransferHandler extends TransferHandler {
    private DataFlavor localArrayListFlavor;
    private DataFlavor serialArrayListFlavor;
    private String localArrayListType = DataFlavor.javaJVMLocalObjectMimeType +
            ";class=java.util.ArrayList";
    private JList source = null;
    private int[] indices = null;
    private int addIndex = -1; //Location where items were added
    private int addCount = 0;  //Number of items added

    public ArrayListTransferHandler() {
        try {
            localArrayListFlavor = new DataFlavor(localArrayListType);
        } catch (ClassNotFoundException e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }

        serialArrayListFlavor = new DataFlavor(ArrayList.class, "ArrayList");
    }

    public boolean importData(JComponent c, Transferable t) {
        JList target = null;
        ArrayList alist = null;
        if (!canImport(c, t.getTransferDataFlavors())) {
            return false;
        }
        try {
            target = (JList) c;
            if (hasLocalArrayListFlavor(t.getTransferDataFlavors())) {
                alist = (ArrayList) t.getTransferData(localArrayListFlavor);
            } else if (hasSerialArrayListFlavor(t.getTransferDataFlavors())) {
                alist = (ArrayList) t.getTransferData(serialArrayListFlavor);
            } else {
                return false;
            }
        } catch (UnsupportedFlavorException ufe) {
            FRContext.getLogger().error(ufe.getMessage(), ufe);
            return false;
        } catch (IOException ioe) {
            FRContext.getLogger().error(ioe.getMessage(), ioe);
            return false;
        }

        //At this point we use the same code to retrieve the data
        //locally or serially.

        //We'll drop at the current selected index.
        int index = target.getSelectedIndex();

        //Prevent the user from dropping data back on itself.
        //For example, if the user is moving items #4,#5,#6 and #7 and
        //attempts to insert the items after item #5, this would
        //be problematic when removing the original items.
        //This is interpreted as dropping the same data on itself
        //and has no effect.
        if (source.equals(target)) {
            if (indices != null && index >= indices[0] - 1 &&
                    index <= indices[indices.length - 1]) {
                indices = null;
                return true;
            }
        }

        DefaultListModel listModel = (DefaultListModel) target.getModel();
        int max = listModel.getSize();
        if (index < 0) {
            index = max;
        } else {
            index++;
            if (index > max) {
                index = max;
            }
        }
        addIndex = index;
        addCount = alist.size();

        this.importJListData(target, addIndex, alist);
        return true;
    }

    protected void importJListData(JList targetList, int addIndex, ArrayList dataList) {
        DefaultListModel targetListModel = (DefaultListModel) targetList.getModel();

        int[] selectedIndices = new int[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            selectedIndices[i] = addIndex;
            targetListModel.add(addIndex++, dataList.get(i));
        }

        targetList.setSelectedIndices(selectedIndices);
        targetList.ensureIndexIsVisible(selectedIndices[0]);
    }

    protected void exportJListDone(JList targetList, int[] indices) {
//一般情况下，不需要删除.前一个JList当中的数据.所以这里什么也不做.

//      DefaultListModel model = (DefaultListModel) targetList.getModel();
//
//      for (int i = indices.length - 1; i >= 0; i--) {
//         model.remove(indices[i]);
//      }
    }

    protected void exportDone(JComponent c, Transferable data, int action) {
        if ((action == MOVE) && (indices != null)) {

            //If we are moving items around in the same list, we
            //need to adjust the indices accordingly since those
            //after the insertion point have moved.
            if (addCount > 0) {
                for (int i = 0; i < indices.length; i++) {
                    if (indices[i] > addIndex) {
                        indices[i] += addCount;
                    }
                }
            }

            exportJListDone((JList) c, indices);
        }

        indices = null;
        addIndex = -1;
        addCount = 0;
    }

    private boolean hasLocalArrayListFlavor(DataFlavor[] flavors) {
        if (localArrayListFlavor == null) {
            return false;
        }

        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(localArrayListFlavor)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSerialArrayListFlavor(DataFlavor[] flavors) {
        if (serialArrayListFlavor == null) {
            return false;
        }

        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(serialArrayListFlavor)) {
                return true;
            }
        }
        return false;
    }

    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        if (hasLocalArrayListFlavor(flavors)) {
            return true;
        }

        return hasSerialArrayListFlavor(flavors);
    }

    protected Transferable createTransferable(JComponent c) {
        if (c instanceof JList) {
            source = (JList) c;
            indices = source.getSelectedIndices();
            Object[] values = source.getSelectedValues();
            if (values == null || values.length == 0) {
                return null;
            }

            ArrayList alist = new ArrayList(values.length);
            for (int i = 0; i < values.length; i++) {
                Object o = values[i];
                String str = o.toString();
                if (str == null) str = "";
                alist.add(str);
            }

            return new ArrayListTransferable(alist);
        }
        return null;
    }

    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }
}
