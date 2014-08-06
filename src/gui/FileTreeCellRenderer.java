package gui;

import file.MyFtpFile;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

class FileTreeCellRenderer extends DefaultTreeCellRenderer 
{
    private JLabel label;

    FileTreeCellRenderer() 
    {
        label = new JLabel();
        label.setOpaque(true);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, 
    	Object value, boolean selected, boolean expanded, 
    	boolean leaf, int row, boolean hasFocus) 
    {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        MyFtpFile file = (MyFtpFile)node.getUserObject();
        
        label.setIcon(ComputerFileBrowser.ICON_FOLDER_REDIM);
        label.setText(file.getDisplayName());

        if (selected) 
        {
            label.setBackground(backgroundSelectionColor);
            label.setForeground(textSelectionColor);
        } else 
        {
            label.setBackground(backgroundNonSelectionColor);
            label.setForeground(textNonSelectionColor);
        }

        return label;
    }
}
