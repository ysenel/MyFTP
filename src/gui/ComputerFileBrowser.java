package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import file.FileBrowser;
import file.MyFtpFile;


public class ComputerFileBrowser extends JPanel
{
	/* For capturing simple and double clicks. */
	private final static int clickInterval = 
			(Integer)Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
	private boolean wasDoubleClick = false;
	private Timer clickTimer;
	
	/* Temp sizes, waiting for a better solution. */
	private int window_height = 600;
	private int window_width = 400;
	
	private Dimension window_dim = 
			new Dimension(window_width, window_height);
	
	private Dimension list_dim = 
			new Dimension(window_width, window_height/2);
	
	private static int icon_width = 25;
	private static int icon_height = 25;
	private static int icon_margin = 6;
	
	public static final ImageIcon ICON_FOLDER = 
			new ImageIcon("ressources/images/folder.png");
	
	public static final ImageIcon ICON_FOLDER_REDIM =
			new ImageIcon(ICON_FOLDER.getImage().getScaledInstance(icon_width, icon_height, Image.SCALE_DEFAULT));
	
	public static final ImageIcon ICON_FILE = 
			new ImageIcon("ressources/images/file_png.png");
	
	public static final ImageIcon ICON_FILE_REDIM =
			new ImageIcon(ICON_FILE.getImage().getScaledInstance(icon_width, icon_height, Image.SCALE_DEFAULT));
	
	private JLabel currentPath;
	
	private JTree fileTree;
	private DefaultTreeModel fileTreeModel;
	
	/*private JList fileList;
	private DefaultListModel fileListModel;
	private ListSelectionModel fileListSelectionModel;*/
	
	private FileBrowser fileBrowser;
	private Path homePath;
	
	/* JTable showing the files */
	
	private JTable fileTable;
	private FileTableModel fileTableModel;
    private ListSelectionListener listSelectionListener;
    private MouseAdapter tableMouseAdapter;
	
	
	public ComputerFileBrowser()
	{
		homePath = new File(System.getProperty("user.home")).toPath();
		/*System.out.println("path: " + homePath.toString());
		System.out.println("namecount: " + homePath.getNameCount());
		System.out.println("getName(0): " + homePath.getName(0));
		System.out.println("getName(1): " + homePath.getName(1));*/
		
		currentPath = new JLabel(); 
		fileBrowser = new FileBrowser();
		
		//this.setPreferredSize(window_dim);
		this.setLayout(new BorderLayout());
		
		this.initFileTree();
		//this.initFileList();
		this.initFileTable();
		
		JScrollPane tableScroll = new JScrollPane(fileTable);
		this.add(tableScroll, BorderLayout.PAGE_END);
		
        //JScrollPane listScrollPane = new JScrollPane(fileList);
        //listScrollPane.setPreferredSize(list_dim);
        //this.add(listScrollPane, BorderLayout.PAGE_END);
        
		JScrollPane treeScroll = new JScrollPane(fileTree);
		this.add(treeScroll, BorderLayout.CENTER);
		
		this.add(currentPath, BorderLayout.PAGE_START);
		currentPath.setOpaque(true);
		currentPath.setBackground(Color.GRAY);
		currentPath.setForeground(Color.WHITE);
	}
	
	private void initFileTable()
	{
		ArrayList<MyFtpFile> roots = fileBrowser.getRoots();
		ArrayList<MyFtpFile> files = 
				fileBrowser.scanDirectory(roots.get(0).getPath());
		
		Collections.sort(files);
		
		fileTable = new JTable();
		fileTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileTable.setAutoCreateRowSorter(true);
		
        /*listSelectionListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) 
            {
            	ListSelectionModel model = fileTable.getSelectionModel();
            	if(!model.getValueIsAdjusting())
            	{
            		int row = model.getLeadSelectionIndex();
            		System.out.println("Row whatsit: " + row);
            	}
                //setFileDetails( ((FileTableModel)fileTable.getModel()).getFile(row) );
            }
        };*/
        
        tableMouseAdapter = new MouseAdapter() {
            public void mouseClicked(final MouseEvent me) 
            {
            	JTable table =(JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                currentPath.setText(((FileTableModel)fileTable.getModel()).getFile(row).getPath().toString());
                
            	if(me.getClickCount() == 2)
            	{
            		System.out.println("Double clicked.");
            		wasDoubleClick = true;
            	}
            	else
            	{
            		currentPath.setText(((FileTableModel)fileTable.getModel()).getFile(row).getPath().toString());
            		clickTimer = new Timer(clickInterval, new ActionListener()
            		{
            			public void actionPerformed(ActionEvent evt) 
            			{
            				/*JTable table =(JTable) me.getSource();
                            Point p = me.getPoint();
                            int row = table.rowAtPoint(p);*/
                            
                            if (wasDoubleClick)
                                wasDoubleClick = false;
                            else 
                            {
                            	/* simple click. */
                            	System.out.println("Simple click");
                            	changeDirectory();
                            }
                        }
            		});
            		
            		clickTimer.setRepeats(false);
            		clickTimer.start();
            	}
            }
        };
        
        fileTable.addMouseListener(tableMouseAdapter);
        //fileTable.getSelectionModel().addListSelectionListener(listSelectionListener);
        fileTableModel = new FileTableModel();
        fileTable.setModel(fileTableModel);
        fileTable.setShowGrid(false);
        
        loadTableData(files);
	}
	
	private void changeDirectory()
	{
		//loadChildren();
		
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
        
        fileTree.addTreeSelectionListener(treeSelectionListener);
        fileTree.setCellRenderer(new FileTreeCellRenderer());
        
        /* Should not be in this method. */
        currentPath.setText("currently selected: " + roots.get(0).getDisplayName());
        
	}
	
	private void loadTableData(final ArrayList<MyFtpFile> files)
	{
        SwingUtilities.invokeLater(new Runnable() {
            public void run() 
            {
                //fileTable.getSelectionModel().removeListSelectionListener(listSelectionListener);
                fileTableModel.setFiles(files);
                //fileTable.getSelectionModel().addListSelectionListener(listSelectionListener);

                fileTable.setRowHeight(icon_width);
                setTableColumnWidth(0);
            }
        });
		
	}
	
	private void setTableColumnWidth(int column)
	{
		TableColumn tableColumn = fileTable.getColumnModel().getColumn(column);
		
		if(column == 0)
		{
			tableColumn.setPreferredWidth(this.icon_width + this.icon_margin);
			tableColumn.setMinWidth(this.icon_width + this.icon_margin);
			tableColumn.setMaxWidth(this.icon_width + this.icon_margin + 10);
		}
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
                	currentPath.setText(" Selected file: " + file.getPath().toString());
                    
                	ArrayList<MyFtpFile> files = 
                    		fileBrowser.scanDirectory(file.getPath());
                	
                	Collections.sort(files);
                    
                    if (node.isLeaf()) 
                    {
                        for (MyFtpFile child : files)
                        	if (child.isDir())
                        		publish(child);
                    }
                    
                    //loadList(files);
                    loadTableData(files);
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
    
    public Path getCurrentPath()
    {
    	int row = 0;
    	ListSelectionModel model = fileTable.getSelectionModel();
    	if(!model.getValueIsAdjusting())
    		row = model.getLeadSelectionIndex();
    	
    	return fileTableModel.getFile(row).getPath();
    }

}





