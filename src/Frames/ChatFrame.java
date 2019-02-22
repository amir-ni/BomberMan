package Frames;

import Config.LocalConfig;
import Utilies.LocalEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatFrame extends JFrame {

    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;


    public ChatFrame() {
        initComponents();
        setFocusable(true);
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                LocalConfig.isScoreFrameFocused = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                LocalConfig.isScoreFrameFocused = false;
                new java.util.Timer().schedule(
                        new java.util.TimerTask(){
                            @Override
                            public void run() {
                                if(!LocalConfig.isGameFrameFocused && !LocalConfig.isScoreFrameFocused){
                                    LocalEngine.keyPressed("Pause");
                                }
                            }
                        }
                        ,1000);
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getXOnScreen() - screenX;
                int deltaY = e.getYOnScreen() - screenY;

                setLocation(myX + deltaX, myY + deltaY);
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();

                myX = getX();
                myY = getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        buttonExit = new javax.swing.JButton();
        buttonPause = new javax.swing.JButton();
        labelCurrentGameName = new javax.swing.JLabel();
        myBomberGuyPanel = new javax.swing.JPanel();
        labelBomberManIcon = new javax.swing.JLabel();
        labelCurrentBomberGuyName = new javax.swing.JLabel();
        labelCurrentBomberGuyScore = new javax.swing.JLabel();
        ScrollPane = new javax.swing.JScrollPane();
        panelScrollable = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        labelCurrentBomberGuyScoreLabel = new JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(20, 20, 20));
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(20, 20, 20));
        jPanel1.setForeground(new java.awt.Color(20, 20, 20));

        buttonExit.setBackground(new java.awt.Color(76, 76, 76));
        buttonExit.setForeground(new java.awt.Color(76, 76, 76));
        buttonExit.setIcon(new javax.swing.ImageIcon(LocalConfig.btnExitImageLocation));
        buttonExit.setMaximumSize(new java.awt.Dimension(50, 50));
        buttonExit.setMinimumSize(new java.awt.Dimension(50, 50));
        buttonExit.setPreferredSize(new java.awt.Dimension(50, 50));
        buttonExit.addActionListener(evt -> buttonExitActionPerformed());
        buttonExit.setToolTipText("خروج");

        buttonPause.setBackground(new java.awt.Color(76, 76, 76));
        buttonPause.setForeground(new java.awt.Color(76, 76, 76));
        buttonPause.setIcon(new javax.swing.ImageIcon(LocalConfig.btnPauseImageLocation));
        buttonPause.setMaximumSize(new java.awt.Dimension(50, 50));
        buttonPause.setMinimumSize(new java.awt.Dimension(50, 50));
        buttonPause.setPreferredSize(new java.awt.Dimension(50, 50));
        buttonPause.addActionListener(evt -> buttonPauseActionPerformed());
        buttonPause.setToolTipText("توقف");

        labelCurrentGameName.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        labelCurrentGameName.setForeground(new java.awt.Color(80, 150, 180));
        labelCurrentGameName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelCurrentGameName.setText(LocalConfig.GameTitle);
        labelCurrentGameName.setToolTipText(LocalConfig.serverAddress + ":" + LocalConfig.serverPort);

        myBomberGuyPanel.setBackground(new java.awt.Color(20, 20, 20));
        myBomberGuyPanel.setForeground(new java.awt.Color(20, 20, 20));
        myBomberGuyPanel.setMinimumSize(new java.awt.Dimension(300, 50));
        myBomberGuyPanel.setName("");
        myBomberGuyPanel.setPreferredSize(new java.awt.Dimension(300, 50));

        labelBomberManIcon.setIcon(new javax.swing.ImageIcon(LocalConfig.bomberManLogoImageLocation));

        labelCurrentBomberGuyName.setFont(new java.awt.Font("Arial", Font.BOLD, 20));
        labelCurrentBomberGuyName.setForeground(new java.awt.Color(80, 150, 180));
        labelCurrentBomberGuyName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelCurrentBomberGuyName.setText("");

        labelCurrentBomberGuyScore.setFont(new java.awt.Font("Arial", Font.BOLD, 20));
        labelCurrentBomberGuyScore.setForeground(new java.awt.Color(80, 150, 180));
        labelCurrentBomberGuyScore.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelCurrentBomberGuyScore.setText("0");

        labelCurrentBomberGuyScoreLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 20));
        labelCurrentBomberGuyScoreLabel.setForeground(new java.awt.Color(80, 150, 180));
        labelCurrentBomberGuyScoreLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelCurrentBomberGuyScoreLabel.setText("امتیاز");

        javax.swing.GroupLayout labelBomberManIconLayout = new javax.swing.GroupLayout(myBomberGuyPanel);
        myBomberGuyPanel.setLayout(labelBomberManIconLayout);
        labelBomberManIconLayout.setHorizontalGroup(
                labelBomberManIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(labelBomberManIconLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(labelCurrentBomberGuyScoreLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelCurrentBomberGuyScore, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelCurrentBomberGuyName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(labelBomberManIcon)
                                .addContainerGap())
        );
        labelBomberManIconLayout.setVerticalGroup(
                labelBomberManIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, labelBomberManIconLayout.createSequentialGroup()
                                .addGroup(labelBomberManIconLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(labelCurrentBomberGuyScoreLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(labelCurrentBomberGuyScore, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(labelCurrentBomberGuyName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(labelBomberManIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        ScrollPane.setBackground(new java.awt.Color(32, 32, 32));
        ScrollPane.setForeground(new java.awt.Color(32, 32, 32));
        ScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        ScrollPane.setMaximumSize(new java.awt.Dimension(290, 410));
        ScrollPane.setMinimumSize(new java.awt.Dimension(290, 410));
        ScrollPane.setPreferredSize(new java.awt.Dimension(290, 410));

        panelScrollable.setLayout(new BoxLayout(panelScrollable, BoxLayout.Y_AXIS));


        ScrollPane.setViewportView(panelScrollable);
        ScrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(buttonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(buttonPause, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(labelCurrentGameName, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(myBomberGuyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(buttonExit, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(buttonPause, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(labelCurrentGameName, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(myBomberGuyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void buttonExitActionPerformed() {
        System.exit(0);
    }

    private void buttonPauseActionPerformed() {
        //Do Pause
    }

    public void newChatAdded(String text){
        JLabel jLabel1 = new JLabel();
        jLabel1.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText(text);
        jLabel1.setMaximumSize(new Dimension(280, 30));
        jLabel1.setMinimumSize(new Dimension(280, 30));
        jLabel1.setPreferredSize(new Dimension(280, 30));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setMinimumSize(new Dimension(280, 30));
        panel.setPreferredSize(new Dimension(280, 30));
        panel.setMaximumSize(new Dimension(280, 30));
        panel.setSize(new Dimension(280, 30));
        panel.add(jLabel1);
        jLabel1.setText("1");
        panelScrollable.add(panel);
        jLabel1.setText("2");
        panelScrollable.repaint();
        jLabel1.setText(text);
        ScrollPane.repaint();

    }

    private javax.swing.JButton buttonExit;
    private javax.swing.JButton buttonPause;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane ScrollPane;
    public javax.swing.JLabel labelCurrentBomberGuyName;
    private javax.swing.JLabel labelBomberManIcon;
    private javax.swing.JPanel myBomberGuyPanel;
    private javax.swing.JPanel panelScrollable;
    public javax.swing.JLabel labelCurrentBomberGuyScore;
    private javax.swing.JLabel labelCurrentBomberGuyScoreLabel;
    private javax.swing.JLabel labelCurrentGameName;

}
