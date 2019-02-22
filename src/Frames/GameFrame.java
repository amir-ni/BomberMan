package Frames;

import Config.LocalConfig;
import Utilies.ContainerPanel;
import Utilies.LocalEngine;
import Utilies.MapPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class GameFrame extends JFrame  {
    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;
    public void end(){
        System.exit(0);
    }
    public GameFrame()  {

        setTitle(LocalConfig.GameTitle);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        setResizable(false);
        setSize(LocalConfig.fullFrameWidth + LocalConfig.gameFrameBorderSize*2, LocalConfig.fullFrameHeight + LocalConfig.gameFrameBorderSize*2);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, LocalConfig.fullFrameWidth + LocalConfig.gameFrameBorderSize*2, LocalConfig.fullFrameHeight + LocalConfig.gameFrameBorderSize*2, 50, 50));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ContainerPanel containerPanel = new ContainerPanel();
        LocalConfig.mapPanel = new MapPanel();
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        jPanel.setBackground(new Color(32, 34, 38));
        jPanel.setBounds(0,0,LocalConfig.fullFrameWidth,LocalConfig.fullFrameHeight);
        setContentPane(jPanel);
        jPanel.add(containerPanel);


        LocalConfig.chatFrame.setVisible(true);

        containerPanel.add(LocalConfig.mapPanel);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(LocalConfig.playerName.equals("Server"))
                    return;
                LocalConfig.isGameFrameFocused = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(LocalConfig.playerName.equals("Server"))
                    return;
                LocalConfig.isGameFrameFocused = false;
                new java.util.Timer().schedule(
                        new java.util.TimerTask(){
                            @Override
                            public void run() {
                                if(!LocalConfig.isGameFrameFocused && !LocalConfig.isScoreFrameFocused){
                                    //LocalEngine.keyPressed("Pause");
                                }
                            }
                        }
                        ,1000);
            }
        });


        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    if(LocalConfig.playerName.equals("Server"))
                        return;
                    switch (e.getKeyCode()) {
                        case 32:
                            LocalEngine.keyPressed("Explode");
                            break;
                        case 37:
                            LocalEngine.keyPressed("Left");
                            break;
                        case 38:
                            LocalEngine.keyPressed("Up");
                            break;
                        case 39:
                            LocalEngine.keyPressed("Right");
                            break;
                        case 40:
                            LocalEngine.keyPressed("Down");
                            break;
                        case 66:
                            LocalEngine.keyPressed("Plant");
                            break;
                        case 67:
                            LocalEngine.sendChat();
                            break;
                        case 72:
                            if((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)
                                LocalEngine.keyPressed("newHunter");
                            break;
                        case 79:
                            if((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)
                                LocalEngine.keyPressed("Load");
                            break;
                        case 80:
                            LocalEngine.keyPressed("PauseOrResume");
                            break;
                        case 81:
                            LocalEngine.keyPressed("Quit");
                            new java.util.Timer().schedule(
                                    new java.util.TimerTask(){
                                        @Override
                                        public void run() {
                                            end();
                                        }
                                    }
                                    ,300);
                            break;
                        case 83:
                            if((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)
                                LocalEngine.keyPressed("Save");
                            break;
                        case 85:
                            if((e.getModifiers() & KeyEvent.ALT_MASK) != 0)
                                LocalEngine.keyPressed("KillAllHunters");
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

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
        Utilies.MusicPlayer.play(LocalConfig.gameSoundLocation,true);

    }
}
