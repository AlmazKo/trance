package ru.alexlen.trance;

import com.intellij.ui.CollectionListModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * @author Almazko
 */
public class TransEditor {
    private JTable               mTable;
    private JPanel               panel1;
    private JEditorPane          editor;
    private JTextField           editorLabel;
    private JTextField           textField1;
    private JList<TransResource> listDic;


    final static int SOURCE_COL = 0;
    private HashMap<String, Dictionary> mData;
    private Writer                      mWriter;
    private MouseAdapter                mTableListener;
    private String                      mLang;
    private DocumentListener            mEditListener;

    private int        mRow;
    private int        mCol;
    private Unit       mUnit;
    private Dictionary mDic;

    public TransEditor(HashMap<String, Dictionary> data, Writer writer)
    {
        mData = data;
        mWriter = writer;

        ListModel<TransResource> list = new CollectionListModel<>(data.keySet());
        listDic.setModel(list);

        listDic.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                JList list = (JList) e.getSource();

                String name = (String) list.getSelectedValue();


                mDic = mData.get(name);
                showDictionary(mDic);

               // mLang = file;
            }

        });


    }


    void showDictionary(final Dictionary dic)
    {
        Log.i("Open file" + dic.getResName());
        final TransModel model = new TransModel(dic);

        mTable.setModel(model);

        TableRowSorter<TransModel> sorter = new TableRowSorter<>(model);
        mTable.setRowSorter(sorter);

        if (mTableListener != null) {
            mTable.removeMouseListener(mTableListener);
        }

        mTableListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                JTable table = (JTable) e.getSource();

                mRow = table.getSelectedRow();
                mCol = table.getSelectedColumn();

                mUnit = model.getUnit(mRow, mCol);

                if (mCol == SOURCE_COL) mCol = 1;
                mLang = model.getColumnName(mCol);

                Log.i(dic.getResName() + " col: " + mCol + ", row: " + mRow);

                editorLabel.setText((String) model.getValueAt(mRow, SOURCE_COL));
                editor.setText((String) model.getValueAt(mRow, mCol));

            }
        };


        mTable.addMouseListener(mTableListener);


        if (mEditListener != null) {
            editor.getDocument().removeDocumentListener(mEditListener);
        }

        mEditListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                updateLog(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                updateLog(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                updateLog(e);
            }
        };

        editor.getDocument().addDocumentListener(mEditListener);

    }

    public void updateLog(DocumentEvent e)
    {

        if (mUnit == null) return;

        Document doc = e.getDocument();


        try {
            String text = doc.getText(0, doc.getLength());
            mWriter.writeTarget(mDic.resources.get(mLang), mUnit.id, text);

        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    public JComponent createComponent()
    {
        return panel1;
    }
}
