package ru.alexlen.trance;

import org.jetbrains.annotations.Nullable;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author Almazko
 */
public class DictionaryTableModel extends AbstractTableModel {

    Dictionary        mDic;
    ArrayList<String> columns;
    ArrayList<String> rows;


    public DictionaryTableModel(Dictionary dictionary)
    {
        mDic = dictionary;

        columns = new ArrayList<>(mDic.languages.size() + 1);

        columns.add("Source");

        for (String lang : mDic.languages) {
            columns.add(lang);
        }

        rows = new ArrayList<>(mDic.data.size());

        for (String s : mDic.data.keySet()) {
            rows.add(s);
        }


    }

    @Override
    public String getColumnName(int index)
    {
        return columns.get(index);
    }

    @Override
    public int getRowCount()
    {
        return mDic.data.size();
    }

    @Override
    public int getColumnCount()
    {
        return columns.size();
    }

    @Override
    public Object getValueAt(int row, int column)
    {
        if (column == 0) {

            return rows.get(row);
        } else {
            TreeMap<String, Unit> variants = mDic.data.get(rows.get(row));
            if (variants != null) {
                Unit unit = variants.get(columns.get(column));
                if (unit != null) return unit.target;
            }

        }

        return null;
    }

    @Nullable
    public Unit getUnit(int row, int column)
    {

        if (column == 0) return null;

        TreeMap<String, Unit> variants = mDic.data.get(rows.get(row));
        if (variants != null) {
            return variants.get(columns.get(column));

        }

        return null;
    }
}
