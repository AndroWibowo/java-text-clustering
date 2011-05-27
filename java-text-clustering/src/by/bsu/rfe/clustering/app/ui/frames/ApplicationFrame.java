package by.bsu.rfe.clustering.app.ui.frames;

import by.bsu.rfe.clustering.algorithm.FlatClustering;
import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.app.InputCallback;
import by.bsu.rfe.clustering.app.InputValidator;
import by.bsu.rfe.clustering.math.ComputationException;
import by.bsu.rfe.clustering.nlp.WordList;
import by.bsu.rfe.clustering.text.algorithm.TextKMeansClustering;
import by.bsu.rfe.clustering.text.data.DocumentDataElement;
import by.bsu.rfe.clustering.text.data.DocumentDataSet;
import by.bsu.rfe.clustering.text.ir.Document;
import by.bsu.rfe.clustering.text.ir.DocumentCollection;
import by.bsu.rfe.clustering.text.ir.DocumentCollectionReader;
import by.bsu.rfe.clustering.text.ir.FileSystemDocumentCollectionReader;
import by.bsu.rfe.clustering.text.ir.InformationRetrievalException;
import by.bsu.rfe.clustering.text.vsm.DocumentVSMGenerator;
import by.bsu.rfe.clustering.text.vsm.NormalizedTFIDF;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ApplicationFrame extends JFrame {

  private static final long serialVersionUID = -3331823388635050038L;

  private static final int DEFAULT_WIDTH = 800;
  private static final int DEFAULT_HEIGHT = 600;

  private final JPanel _mainPanel = new JPanel();

  private final JMenuBar _menuBar = new JMenuBar();

  private final JMenu _menuFile = new JMenu("File");
  private final JMenuItem _menuItemLoad = new JMenuItem("Load Folder");

  private final JMenu _menuTools = new JMenu("Tools");
  private final JMenuItem _menuItemCluster = new JMenuItem("Clustering");

  private final JTree _clusterTree = new JTree((TreeModel) null);
  private final JScrollPane _treePanel = new JScrollPane(_clusterTree);

  private final JPanel _leftPanel = new JPanel();
  private final JPanel _rightPanel = new JPanel();

  private final JSplitPane _splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, _leftPanel, _rightPanel);

  private final JTextArea _documentViewArea = new JTextArea();

  // private JToolBar _toolbar = new JToolBar(JToolBar.TOP);

  // private JButton _buttonCluster = new JButton("Cluster");

  private DocumentCollection _currentDocCollection;
  private int _currentNumberOfClusters;

  public ApplicationFrame() {
    setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    Frames.center(this);
    initLayout();
    initActionHandlers();
  }

  private void initLayout() {
    _clusterTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

    _mainPanel.setLayout(new BorderLayout());

    _splitPane.setBorder(BorderFactory.createLineBorder(Color.black));
    _splitPane.setDividerSize(5);

    // _leftPanel.setSize(100, _leftPanel.getHeight());

    _leftPanel.setLayout(new BorderLayout());
    _leftPanel.add(_treePanel, BorderLayout.WEST);

    _treePanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    _treePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    // _buttonCluster.setEnabled(false);
    //    
    // _toolbar.setFloatable(false);
    // _toolbar.add(_buttonCluster);

    getContentPane().add(_mainPanel);

    _mainPanel.add(_splitPane, BorderLayout.CENTER);
    // _mainPanel.add(_toolbar, BorderLayout.NORTH);

    setJMenuBar(_menuBar);

    _menuBar.add(_menuFile);
    _menuBar.add(_menuTools);

    _menuFile.add(_menuItemLoad);

    _menuItemCluster.setEnabled(false);
    _menuTools.add(_menuItemCluster);

    _documentViewArea.setEditable(false);

    _rightPanel.setLayout(new BorderLayout(10, 10));
    _rightPanel.add(_documentViewArea, BorderLayout.CENTER);

    _documentViewArea.setVisible(false);
    _documentViewArea.setLineWrap(true);
    _documentViewArea.setWrapStyleWord(true);
    _documentViewArea.setAutoscrolls(true);
  }

  private void initActionHandlers() {
    _menuItemLoad.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        loadDocumentCollection();
      }
    });

    _leftPanel.addComponentListener(new ComponentAdapter() {

      @Override
      public void componentResized(ComponentEvent e) {
        _clusterTree.setSize(_leftPanel.getWidth(), _clusterTree.getHeight());
      }
    });

    _clusterTree.addTreeSelectionListener(new TreeSelectionListener() {

      @Override
      public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) _clusterTree.getLastSelectedPathComponent();

        if (node != null) {
          Object nodeDataObject = node.getUserObject();

          if (nodeDataObject instanceof Document) {
            Document doc = (Document) nodeDataObject;
            _documentViewArea.setVisible(true);
            _documentViewArea.setText(doc.getOriginalText());
          }
          else {
            _documentViewArea.setText("");
            _documentViewArea.setVisible(false);
          }
        }
      }
    });

    _menuItemCluster.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // clusterDocuments();
        // JOptionPane.showInputDialog("Number of clusters");

        final ValidatableInputDialog inputDialog = new ValidatableInputDialog(ApplicationFrame.this,
            "Input number of clusters", new InputValidator() {

              @Override
              public boolean validate(String value) {
                if ((value == null) || !value.matches("[\\d]+")) {
                  return false;
                }

                Integer numOfClusters = Integer.valueOf(value);
                return numOfClusters <= _currentDocCollection.size();
              }

              @Override
              public String getErrorMessage() {
                return "Input a positive number less or equal to " + _currentDocCollection.size();
              }
            }, new InputCallback() {

              @Override
              public void inputInvalid(String value) {

              }

              @Override
              public void inputValid(String input) {
                _currentNumberOfClusters = Integer.valueOf(input);
                clusterDocuments();
              }

              @Override
              public void inputCancelled() {
                // do nothing
              }
            });

        inputDialog.pack();
        inputDialog.setResizable(false);
        inputDialog.setLocationRelativeTo(ApplicationFrame.this);
        inputDialog.setVisible(true);
      }
    });

  }

  private void loadDocumentCollection() {
    // Choose a Folder with text documents
    JFileChooser fileChooser = new JFileChooser(new File("."));

    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.setDialogTitle("Choose a folder");

    fileChooser.setFileFilter(new FileFilter() {
      @Override
      public boolean accept(File f) {
        return f.isDirectory();
      }

      @Override
      public String getDescription() {
        return "Folder with text documents";
      }
    });

    int dialogOption = fileChooser.showOpenDialog(this);

    if (dialogOption == JFileChooser.APPROVE_OPTION) {
      File folder = fileChooser.getSelectedFile();

      if (folder != null) {
        beforeDocumentLoad();

        File stopWords = new File("dictionary\\stopwords.txt");
        WordList stopWordList = null;
        try {
          stopWordList = WordList.load(stopWords);
        }
        catch (IOException e) {
          // TODO: handle exception properly
        }

        DocumentCollectionReader reader = new FileSystemDocumentCollectionReader(folder, stopWordList);

        try {
          _currentDocCollection = reader.readDocuments();
          displayDocumentCollection(_currentDocCollection);

          afterDocumentLoad();
        }
        catch (InformationRetrievalException e) {
          // TODO handle exception
        }
      }

    }

  }

  private void clusterDocuments() {
    assert (_currentDocCollection != null) : "Document Collection is null";
    
    System.out.println("1");

    new Thread(new Runnable() {

      @Override
      public void run() {
        
        System.out.println("run()");

        ProgressBarDialog progressDialog = new ProgressBarDialog(ApplicationFrame.this);

        progressDialog.setLocationRelativeTo(progressDialog.getParent());
        progressDialog.setSize(200, 100);
        progressDialog.setResizable(false);
        
        System.out.println("run(1)");
        
        progressDialog.setVisible(true);
        
        System.out.println("run(2)");

        DocumentVSMGenerator vsmGen = new NormalizedTFIDF();
        DocumentDataSet dataSet = vsmGen.createVSM(_currentDocCollection);

        FlatClustering<DocumentDataElement, Cluster<DocumentDataElement>, DocumentDataSet> clustering = new TextKMeansClustering(
            _currentNumberOfClusters);

        System.out.println("2");
        
        try {
          List<Cluster<DocumentDataElement>> clusters = clustering.cluster(dataSet);
          System.out.println("ololo");
          progressDialog.dispose();
          displayClusters(clusters);
        }
        catch (ComputationException e) {
          progressDialog.dispose();
          JOptionPane.showMessageDialog(ApplicationFrame.this, "Computation error", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    }).start();
    
    System.out.println("started");
  }

  private void beforeDocumentLoad() {
    _leftPanel.removeAll();

    _leftPanel.add(new JLabel("Loading..."), BorderLayout.CENTER);
    _mainPanel.revalidate();
  }

  private void afterDocumentLoad() {
    if ((_currentDocCollection != null) && (_currentDocCollection.size() > 0)) {
      _menuItemCluster.setEnabled(true);
    }
  }

  private void displayDocumentCollection(DocumentCollection docCollection) {
    _leftPanel.removeAll();
    _clusterTree.setModel(createDocumentTree(docCollection));
    _leftPanel.add(_treePanel, BorderLayout.WEST);
    _leftPanel.revalidate();
  }

  private void displayClusters(List<Cluster<DocumentDataElement>> clusters) {
    _leftPanel.removeAll();
    _clusterTree.setModel(createClusterTreeModel(clusters));
    _leftPanel.add(_treePanel, BorderLayout.WEST);
    _leftPanel.revalidate();
  }

  private TreeModel createDocumentTree(DocumentCollection docCollection) {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Corpus");

    for (Document doc : docCollection) {
      DefaultMutableTreeNode documentNode = new DefaultMutableTreeNode(doc);
      root.add(documentNode);
    }

    return new DefaultTreeModel(root);
  }

  private TreeModel createClusterTreeModel(List<Cluster<DocumentDataElement>> clusters) {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Dataset");

    for (Cluster<DocumentDataElement> c : clusters) {
      DefaultMutableTreeNode clusterNode = new DefaultMutableTreeNode(c.getLabel());
      root.add(clusterNode);

      for (DocumentDataElement elem : c.getDataElements()) {
        Document doc = elem.getDocument();

        DefaultMutableTreeNode documentNode = new DefaultMutableTreeNode(doc);

        clusterNode.add(documentNode);
      }
    }

    return new DefaultTreeModel(root);
  }

}
