package Frames;

import Utilies.LocalEngine;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.*;
import javax.swing.*;

public class GameSetting extends JFrame {
    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;

    GameSetting() {
        initComponents();
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

        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        TexBoxHeight = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TexBoxWidth = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        TexBoxCellSize = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        TexGameFrameWidth = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        TexBoxBomberManHeight = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        TexBoxBomberManWidth = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        TexBoxBomberManSpeed = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        TexBoxMaxBombCount = new javax.swing.JTextField();
        TexBoxBombExplosionRadius = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        TexBoxBombTimer = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        TexBoxRespawnTime = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        TexBoxPauseTime = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        TexBoxGameName = new javax.swing.JTextField();
        TexGameFrameHeight = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(255, 255, 255));
        setName("GameSetting");
        setUndecorated(true);
        setOpacity(0.9F);
        setShape(new RoundRectangle2D.Double(60, 60, 600, 380, 60, 60));

        jPanel5.setBackground(new java.awt.Color(204, 255, 204));

        jButton1.setText("شروع بازی جدید");
        jButton1.addActionListener(evt -> jButton1ActionPerformed());

        jButton2.setText("خروج");
        jButton2.addActionListener(evt -> jButton2ActionPerformed());

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("تعداد ستون ها :");

        TexBoxHeight.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TexBoxHeight.setText("21");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("تعداد سطر ها :");

        TexBoxWidth.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TexBoxWidth.setText("21");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("سایز خانه ها :");

        TexBoxCellSize.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TexBoxCellSize.setText("45");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("عرض صفحه بازی :");

        TexGameFrameWidth.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TexGameFrameWidth.setText("800");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("ارتفاع بمبرمن :");
        jLabel8.setToolTipText("");

        TexBoxBomberManHeight.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TexBoxBomberManHeight.setText("32");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("عرض بمبرمن :");

        TexBoxBomberManWidth.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TexBoxBomberManWidth.setText("20");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("سرعت بمبرمن :");

        TexBoxBomberManSpeed.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TexBoxBomberManSpeed.setText("5");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("حداکثر تعداد بمب :");

        TexBoxMaxBombCount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TexBoxMaxBombCount.setText("1");

        TexBoxBombExplosionRadius.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TexBoxBombExplosionRadius.setText("1");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("شعاع انفجار بمب :");

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("زمان انفجار بمب :");

        TexBoxBombTimer.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TexBoxBombTimer.setText("5");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("مدت مرگ بمبرمن (ms) :");

        TexBoxRespawnTime.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TexBoxRespawnTime.setText("5000");

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("حداقل مدت توقف :");

        TexBoxPauseTime.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TexBoxPauseTime.setText("5");

        jLabel16.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 24));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("تنظیمات");

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("ارتفاع صفحه بازی :");

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("نام بازی :");

        TexBoxGameName.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TexBoxGameName.setText("BomberMan");

        TexGameFrameHeight.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TexGameFrameHeight.setText("800");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(125, 125, 125)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(TexBoxBomberManSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(TexBoxMaxBombCount, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addComponent(jButton1)
                                                                .addGap(37, 37, 37)
                                                                .addComponent(jButton2))
                                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addComponent(TexBoxBombExplosionRadius, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addComponent(TexBoxBombTimer, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addComponent(TexBoxRespawnTime, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addComponent(TexBoxPauseTime, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addComponent(TexBoxBomberManHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addComponent(TexBoxBomberManWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(43, 43, 43)
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addComponent(TexBoxGameName, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addComponent(TexGameFrameHeight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addComponent(TexGameFrameWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addGroup(jPanel5Layout.createSequentialGroup()
                                                                        .addComponent(TexBoxHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(jPanel5Layout.createSequentialGroup()
                                                                        .addComponent(TexBoxWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addComponent(TexBoxCellSize, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap(142, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(TexBoxHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12)
                                        .addComponent(TexBoxBombExplosionRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(TexBoxWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13)
                                        .addComponent(TexBoxBombTimer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(TexBoxCellSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel14)
                                        .addComponent(TexBoxRespawnTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(TexGameFrameWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel15)
                                        .addComponent(TexBoxPauseTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(TexBoxBomberManHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel17)
                                        .addComponent(TexGameFrameHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(TexBoxBomberManWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel18)
                                        .addComponent(TexBoxGameName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel10)
                                        .addComponent(TexBoxBomberManSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11)
                                        .addComponent(TexBoxMaxBombCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton2)
                                        .addComponent(jButton1))
                                .addContainerGap(73, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }

    private void jButton2ActionPerformed() {
        System.exit(0);
    }

    private void jButton1ActionPerformed() {
        int cellCountY = 0;
        int cellCountX = 0;
        int BomberManWidth=0, BombExplosionRadius=0, BombTimer=0, BomberManHeight=0, BomberManSpeed=0, GameFrameWidth=0,GameFrameHeight=0, CellSize=0, MaxBombCount=0, PauseTime=0, RespawnTime=0;
        String GameName;
        try{
            cellCountY = Integer.parseInt(TexBoxHeight.getText());
            TexBoxHeight.setBackground(Color.white);
        }catch (Exception e){
            TexBoxHeight.setBackground(Color.red);
        }

        try{
            cellCountX = Integer.parseInt(TexBoxWidth.getText());
            TexBoxWidth.setBackground(Color.white);
        }catch (Exception e){
            TexBoxWidth.setBackground(Color.red);
        }

        if(cellCountX < 3){
            JOptionPane.showMessageDialog(null, "تعداد سطر ها باید حداقل 3 باشد", "خطا" , JOptionPane.ERROR_MESSAGE);
            TexBoxWidth.setBackground(Color.red);
            return;
        }else{
            TexBoxWidth.setBackground(Color.white);
        }
        if(cellCountY < 3){
            JOptionPane.showMessageDialog(null, "تعداد ستون ها باید حداقل 3 باشد", "خطا" , JOptionPane.ERROR_MESSAGE);
            TexBoxHeight.setBackground(Color.red);
            return;
        }else{
            TexBoxHeight.setBackground(Color.white);
        }

        try {
            BomberManWidth = Integer.parseInt(TexBoxBomberManWidth.getText());
            TexBoxBomberManWidth.setBackground(Color.WHITE);
        }catch (Exception e){
            TexBoxBomberManWidth.setBackground(Color.red);
        }
        try {
            BombExplosionRadius = Integer.parseInt(TexBoxBombExplosionRadius.getText());
            TexBoxBombExplosionRadius.setBackground(Color.WHITE);
        }catch (Exception e){
            TexBoxBombExplosionRadius.setBackground(Color.red);
        }
        try {
            BombTimer = Integer.parseInt(TexBoxBombTimer.getText());
            TexBoxBombTimer.setBackground(Color.WHITE);
        }catch (Exception e){
            TexBoxBombTimer.setBackground(Color.red);
        }
        try {
            BomberManHeight = Integer.parseInt(TexBoxBomberManHeight.getText());
            TexBoxBomberManHeight.setBackground(Color.WHITE);
        }catch (Exception e){
            TexBoxBomberManHeight.setBackground(Color.red);
        }
        try {
            BomberManSpeed = Integer.parseInt(TexBoxBomberManSpeed.getText());
            TexBoxBomberManSpeed.setBackground(Color.WHITE);
        }catch (Exception e){
            TexBoxBomberManSpeed.setBackground(Color.red);
        }
        try {
            CellSize = Integer.parseInt(TexBoxCellSize.getText());
            TexBoxCellSize.setBackground(Color.WHITE);
        }catch (Exception e){
            TexBoxCellSize.setBackground(Color.red);
        }
        try {
            GameFrameWidth = Integer.parseInt(TexGameFrameWidth.getText());
            TexGameFrameWidth.setBackground(Color.WHITE);
            if(GameFrameWidth > CellSize * cellCountX){
                TexGameFrameWidth.setBackground(Color.red);
            }
        }catch (Exception e){
            TexGameFrameWidth.setBackground(Color.red);
        }
        try {
            GameFrameHeight = Integer.parseInt(TexGameFrameHeight.getText());
            TexGameFrameHeight.setBackground(Color.WHITE);
            if(GameFrameHeight > CellSize * cellCountY){
                TexGameFrameHeight.setBackground(Color.red);
            }
        }catch (Exception e){
            TexGameFrameHeight.setBackground(Color.red);
        }
        try {
            MaxBombCount = Integer.parseInt(TexBoxMaxBombCount.getText());
            TexBoxMaxBombCount.setBackground(Color.WHITE);
        }catch (Exception e){
            TexBoxMaxBombCount.setBackground(Color.red);
        }
        try {
            PauseTime = Integer.parseInt(TexBoxPauseTime.getText());
            TexBoxPauseTime.setBackground(Color.WHITE);
        }catch (Exception e){
            TexBoxPauseTime.setBackground(Color.red);
        }
        try {
            RespawnTime = Integer.parseInt(TexBoxRespawnTime.getText());
            TexBoxRespawnTime.setBackground(Color.WHITE);
        }catch (Exception e){
            TexBoxRespawnTime.setBackground(Color.red);
        }
        GameName = TexBoxGameName.getText();

        if(TexBoxWidth.getBackground() == Color.red || TexBoxHeight.getBackground() == Color.red || TexBoxWidth.getBackground() == Color.red || TexBoxBombExplosionRadius.getBackground() == Color.red ||
                TexBoxBombTimer.getBackground() == Color.red || TexBoxBomberManHeight.getBackground() == Color.red || TexBoxBomberManSpeed.getBackground() == Color.red ||
                TexGameFrameWidth.getBackground() == Color.red || TexBoxCellSize.getBackground() == Color.red || TexBoxMaxBombCount.getBackground() == Color.red ||
                TexBoxPauseTime.getBackground() == Color.red || TexBoxRespawnTime.getBackground() == Color.red || TexGameFrameHeight.getBackground() == Color.red  ){
            JOptionPane.showMessageDialog(null, "لطفا مقادیر را تصحیح کنید", "خطا" , JOptionPane.ERROR_MESSAGE);
            return;
        }
        LocalEngine.CreateNewSoloGame(cellCountX,cellCountY,BombExplosionRadius,BombTimer,BomberManSpeed,CellSize,MaxBombCount,PauseTime,RespawnTime,GameName,GameFrameHeight,GameFrameWidth,1);
        this.dispose();

    }

    private JTextField TexBoxBomberManWidth;
    private JTextField TexBoxBombExplosionRadius;
    private JTextField TexBoxBombTimer;
    private JTextField TexBoxBomberManHeight;
    private JTextField TexBoxBomberManSpeed;
    private JTextField TexGameFrameWidth;
    private JTextField TexBoxCellSize;
    private JTextField TexBoxGameName;
    private JTextField TexBoxHeight;
    private JTextField TexBoxMaxBombCount;
    private JTextField TexBoxPauseTime;
    private JTextField TexBoxRespawnTime;
    private JTextField TexBoxWidth;
    private JButton jButton1;
    private JButton jButton2;
    private JTextField TexGameFrameHeight;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel14;
    private JLabel jLabel15;
    private JLabel jLabel16;
    private JLabel jLabel17;
    private JLabel jLabel18;
    private JLabel jLabel3;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JPanel jPanel5;
}
