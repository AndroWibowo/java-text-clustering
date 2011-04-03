package by.bsu.rfe.clustering.ui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import by.bsu.rfe.clustering.algorithm.KMeansHelper;
import by.bsu.rfe.clustering.algorithm.KMeansHelper.StepCompleteEvent;
import by.bsu.rfe.clustering.algorithm.KMeansHelper.StepCompleteListener;
import by.bsu.rfe.clustering.algorithm.cluster.CentroidCluster;
import by.bsu.rfe.clustering.algorithm.cluster.Cluster;
import by.bsu.rfe.clustering.algorithm.data.DataElement;
import by.bsu.rfe.clustering.algorithm.data.DataSet;
import by.bsu.rfe.clustering.algorithm.data.GenericDataSet;
import by.bsu.rfe.clustering.math.DoubleSparceVector;
import by.bsu.rfe.clustering.math.DoubleVector;

import com.google.common.collect.ImmutableList;

public class TestKMeansFrame extends JFrame {

    private static final long serialVersionUID = -8838132815992006327L;

    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;

    private final JPanel _leftPanel = new JPanel();
    private JPanel btnPanel = new JPanel();

    private JButton kMeansBtn = new JButton("Run KMeans");
    private JButton resetBtn = new JButton("Reset");
    private JButton nextBtn = new JButton("Next Step");

    public TestKMeansFrame() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        Frames.center(this);
        init();

    }

    private void init() {
        Container cont = getContentPane();
        final VisualizationPanel panel = new VisualizationPanel();
        cont.setLayout(new GridLayout(2, 1));
        cont.add(panel, BorderLayout.NORTH);
        cont.setBounds(0, 0, getWidth(), getHeight());

        panel.setBounds(0, 0, cont.getWidth(), cont.getHeight());
        panel.reset();

        kMeansBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                panel.runKMeans();
            }
        });

        resetBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                panel.reset();
                panel.repaint();
            }
        });

        nextBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panel.next();
            }
        });

        btnPanel.add(kMeansBtn, BorderLayout.CENTER);
        btnPanel.add(resetBtn, BorderLayout.CENTER);
        btnPanel.add(nextBtn, BorderLayout.CENTER);
        cont.add(btnPanel);
    }

    private class VisualizationPanel extends JPanel {

        private static final long serialVersionUID = -8972195299622403697L;

        private List<KPoint> _points = new ArrayList<KPoint>();

        Random _random = new Random();

        private int _numberOfClusters = 2;

        List<Color> _colors = ImmutableList.of(Color.red, Color.BLUE, Color.GREEN, Color.DARK_GRAY, Color.CYAN,
                Color.ORANGE);

        List<? extends Cluster<KPoint>> _cList = null;

        private KMeansHelper<KPoint, DataSet<KPoint>> kHelper;

        private DataSet<KPoint> _dataSet;

        private VisualizationPanel() {
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (_cList == null) {

                g.setColor(Color.black);
                for (KPoint p : _points) {
                    g.fillOval((int) (p.getX() * getWidth()) - 5, (int) (p.getY() * getHeight()) + 5, 10, 10);
                }
            }
            else {
                for (int i = 0; i < _cList.size(); i++) {
                    g.setColor(_colors.get(i));

                    DoubleVector center = ((CentroidCluster<KPoint>) _cList.get(i)).computeCentroid();

                    for (KPoint p : _cList.get(i).getDataElements()) {
                        g.fillOval((int) (p.getX() * getWidth()) - 5, (int) (p.getY() * getHeight()) + 5, 10, 10);
                    }
                    // g.setColor(Color.gray);
                    g.drawOval((int) (center.get(0) * getWidth()) - 5, (int) (center.get(1) * getHeight()) + 5, 10, 10);
                }
            }
        }

        void next() {
            kHelper.runNextStep();
        }

        private void reset() {

            _points.clear();
            for (int i = 0; i < _numberOfClusters * 3; i++) {
                addRandomPoint();
            }
            _dataSet = new GenericDataSet<KPoint>();
            for (KPoint p : _points) {
                _dataSet.addElement(p);
            }
            kHelper = KMeansHelper.initialize(_dataSet, _numberOfClusters);
            kHelper.addStepCompleteListener(new StepCompleteListener<KPoint>() {

                public void onStepComplete(StepCompleteEvent<KPoint> event) {
                    _cList = event.getCurrentDistribution();
                    nextBtn.setEnabled(kHelper.hasNextStep());
                    repaint();
                }
            });
            kHelper.fireEvent();

            nextBtn.setEnabled(true);
        }

        void setNumberOfClusters(int n) {
            _numberOfClusters = n;
        }

        void runKMeans() {
            while (kHelper.hasNextStep()) {
                kHelper.runNextStep();
            }
        }

        void addRandomPoint() {
            KPoint newPoint = new KPoint();
            newPoint.setX(_random.nextDouble());
            newPoint.setY(_random.nextDouble());
            _points.add(newPoint);
        }

    }

    private static class KPoint implements DataElement {

        private DoubleSparceVector _vector = new DoubleSparceVector(2);

        public KPoint() {
        }

        void setX(double x) {
            _vector.set(0, x);
        }

        void setY(double y) {
            _vector.set(1, y);
        }

        double getX() {
            return _vector.get(0);
        }

        double getY() {
            return _vector.get(1);
        }

        @Override
        public DoubleVector asVector() {
            return _vector;
        }
    }

}
