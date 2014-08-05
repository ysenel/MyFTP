package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import file.FileBrowser;
import file.MyFtpFile;


public class ComputerFileBrowser extends JPanel
{
	/* Temp sizes, waiting for a better solution. */
	private int window_height = 600;
	private int window_width = 400;
	
	private Dimension window_dim = 
			new Dimension(window_width, window_height);
	
	private Dimension list_dim = 
			new Dimension(window_width, window_height/2);
	
	public static final ImageIcon ICON_FOLDER = 
			new ImageIcon("ressources/images/folder.png");
	
	public static final ImageIcon ICON_FOLDER_REDIM =
			new ImageIcon(ICON_FOLDER.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT));
	
	public static final ImageIcon ICON_FILE = 
			new ImageIcon("ressources/images/file_png.png");
	
	public static final ImageIcon ICON_FILE_REDIM =
			new ImageIcon(ICON_FILE.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT));
	
	private JLabel currentPath;
	
	private JTree fileTree;
	private DefaultTreeModel fileTreeModel;
	
	private JList fileList;
	private DefaultListModel fileListModel;
	private ListSelectionModel fileListSelectionModel;
	
	private FileBrowser fileBrowser;
	private Path homePath;
	
	
	public ComputerFileBrowser()
	{
		homePath = new File(System.getProperty("user.home")).toPath();
		
		currentPath = new JLabel(); 
		fileBrowser = new FileBrowser();
		
		//this.setPreferredSize(window_dim);
		this.setLayout(new BorderLayout());
		
		this.initFileTree();
		this.initFileList();
		
        JScrollPane listScrollPane = new JScrollPane(fileList);
        //listScrollPane.setPreferredSize(list_dim);
        this.add(listScrollPane, BorderLayout.PAGE_END);
        
		JScrollPane treeScroll = new JScrollPane(fileTree);
		this.add(treeScroll, BorderLayout.CENTER);
		
		this.add(currentPath, BorderLayout.PAGE_START);
		currentPath.setOpaque(true);
		currentPath.setBackground(Color.GRAY);
		currentPath.setForeground(Color.WHITE);
	}
	
	private void initFileTree()
	{
		ArrayList<MyFtpFile> roots = fileBrowser.getRoots();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		
		for(MyFtpFile myFile : roots)
		{
			DefaultMutableTreeNode myFtpFileNode = 
					new DefaultMutableTreeNode(myFile);
			
			root.add(myFtpFileNode);
			
			ArrayList<MyFtpFile> files = 
					fileBrowser.scanDirectory(myFile.getPath());
			
			for(MyFtpFile mySubFile : files)
				myFtpFileNode.add(new DefaultMutableTreeNode(mySubFile));
				
		}
		
        TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse)
            {
            	System.out.println("Value changed");
                DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
                loadChildren(node);
            }
        };
		
		fileTreeModel = new DefaultTreeModel(root);
		fileTree = new JTree(fileTreeModel);
		
		fileTree.setRootVisible(false);
		//fileTree.expandPath(path)
        
        fileTree.addTreeSelectionListener(treeSelectionListener);
        fileTree.setCellRenderer(new FileTreeCellRenderer());
        
        /* Should not be in this method. */
        currentPath.setText("currently selected: " + roots.get(0).getDisplayName());
	}
	
	private void initFileList()
	{
		ArrayList<MyFtpFile> roots = fileBrowser.getRoots();
		ArrayList<MyFtpFile> files = 
				fileBrowser.scanDirectory(roots.get(0).getPath());
		
		Collections.sort(files);
		
		fileListModel = new DefaultListModel<File>();
	    fileList = new JList<File>(fileListModel);
	    
	    fileList.setCellRenderer(new FileListCellRenderer());
	    
	    loadList(files);
	}
	
	private void loadList(final ArrayList<MyFtpFile> files)
	{
        SwingUtilities.invokeLater(new Runnable() {
            public void run() 
            {
            	fileListModel.clear();
            	for(MyFtpFile file : files)
            		fileListModel.add(0, file);
            }
        });
	}
	
    private void loadChildren(final DefaultMutableTreeNode node) 
    {
        fileTree.setEnabled(false);

        SwingWorker<Void, MyFtpFile> worker = new SwingWorker<Void, MyFtpFile>() {
        	
            @Override
            public Void doInBackground() 
            {
            	MyFtpFile file = (MyFtpFile)node.getUserObject();
                
                if (file.isDir()) 
                {
                	System.out.println("fetching...");
                	currentPath.setText(" Selected file: " + file.toString());
                    
                	ArrayList<MyFtpFile> files = 
                    		fileBrowser.scanDirectory(file.getPath());
                	
                	Collections.sort(files);
                    
                    if (node.isLeaf()) 
                    {
                        for (MyFtpFile child : files)
                        	if (child.isDir())
                        		publish(child);
                    }
                    
                    loadList(files);
                }
                return null;
            }

            @Override
            protected void process(List<MyFtpFile> chunks) 
            {
                for (MyFtpFile child : chunks) 
                {
                    node.add(new DefaultMutableTreeNode(child));
                }
            }

            @Override
            protected void done() 
            {
                fileTree.setEnabled(true);
            }
        };
        worker.execute();
    }

}

/* TODO: make the same format as FileTreeCellRenderer. */

class FileListCellRenderer extends JLabel implements ListCellRenderer
{
	public FileListCellRenderer()
	{
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}


	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus)
	{
		MyFtpFile file = (MyFtpFile)value;
		
		if(file.isDir())
			this.setIcon(ComputerFileBrowser.ICON_FOLDER_REDIM);
		else
			this.setIcon(ComputerFileBrowser.ICON_FILE_REDIM);
		
		this.setAlignmentX((float)0.0);
		
		this.setText(file.getDisplayName());

		if (isSelected)
		{
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} 
		else
		{
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		return this;
	}
}


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
