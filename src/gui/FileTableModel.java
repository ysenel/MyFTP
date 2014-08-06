package gui;

import file.MyFtpFile;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

class FileTableModel extends AbstractTableModel 
{
	private ArrayList<MyFtpFile> files;
    private String[] columns = {"", "Path/name", };

    FileTableModel()
    {
        this(new ArrayList<MyFtpFile>());
    }

    FileTableModel(ArrayList<MyFtpFile> files) {
        this.files = files;
    }

    public Object getValueAt(int row, int column) 
    {
    	MyFtpFile file = files.get(row);
    	
    	if(column == 0)
    		if(file.isDir())
    			return ComputerFileBrowser.ICON_FOLDER_REDIM;
    		else
    			return ComputerFileBrowser.ICON_FILE_REDIM;
    	else if(column == 1)
    		return "  " + file.getDisplayName();
    	
    	
        return "WTF unknow suff";
    }

    public int getColumnCount() 
    {
        return columns.length;
    }

    public Class<?> getColumnClass(int column) 
    {
    	if(column == 0)
    		return ImageIcon.class;

        return String.class;
    }

    public String getColumnName(int column) {
        return columns[column];
    }

    public int getRowCount() {
        return files.size();
    }

    public MyFtpFile getFile(int row) {
        return files.get(row);
    }

    public void setFiles(ArrayList<MyFtpFile> files) 
    {
        this.files = files;
        fireTableDataChanged();
    }
}
