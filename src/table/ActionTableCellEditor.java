package table;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Nov 12, 2007
 * Time: 4:29:47 PM
 */

/**
 * MySwing: Advanced Swing Utilites
 * Copyright (C) 2005  Santhosh Kumar T
 * <p/>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
public abstract class ActionTableCellEditor implements TableCellEditor, ActionListener {
    //public final Icon DOTDOTDOT_ICON = new ImageIcon(getClass().getResource("dotdotdot.gif"));

    private TableCellEditor editor;
    private JButton customEditorButton = new JButton("...");

    protected JTable table;
    protected int row, column;

    public ActionTableCellEditor(TableCellEditor editor) {
        this.editor = editor;
        customEditorButton.addActionListener(this);

        // ui-tweaking
        customEditorButton.setFocusable(false);
        customEditorButton.setFocusPainted(false);
        customEditorButton.setMargin(new Insets(0, 0, 0, 0));
    }

    public Component getTableCellEditorComponent(JTable table, Object value
            , boolean isSelected, int row, int column) {

        final JComponent editorComp = (JComponent) editor.getTableCellEditorComponent(table, value, isSelected, row, column);
        JPanel panel = new JPanel(new BorderLayout()) {
            public void addNotify() {
                super.addNotify();
                editorComp.requestFocus();
            }

            protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
                InputMap map = editorComp.getInputMap(condition);
                ActionMap am = editorComp.getActionMap();

                if (map != null && am != null && isEnabled()) {
                    Object binding = map.get(ks);
                    Action action = (binding == null) ? null : am.get(binding);
                    if (action != null) {
                        return SwingUtilities.notifyAction(action, ks, e, editorComp, e.getModifiers());
                    }
                }
                return false;
            }
        };
        
        panel.setRequestFocusEnabled(true);
        panel.add(editorComp);
        panel.add(customEditorButton, BorderLayout.EAST);
        this.table = table;
        this.row = row;
        this.column = column;
        return panel;
    }

    public Object getCellEditorValue() {
        return editor.getCellEditorValue();
    }

    public boolean isCellEditable(EventObject anEvent) {
        return editor.isCellEditable(anEvent);
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return editor.shouldSelectCell(anEvent);
    }

    public boolean stopCellEditing() {
        return editor.stopCellEditing();
    }

    public void cancelCellEditing() {
        editor.cancelCellEditing();
    }

    public void addCellEditorListener(CellEditorListener l) {
        editor.addCellEditorListener(l);
    }

    public void removeCellEditorListener(CellEditorListener l) {
        editor.removeCellEditorListener(l);
    }


    public final void actionPerformed(ActionEvent e){
        Object partialValue = editor.getCellEditorValue();
        editor.cancelCellEditing();
        editCell(table, partialValue, row, column);
    }

    protected abstract void editCell(JTable table, Object partialValue, int row, int column);
}
