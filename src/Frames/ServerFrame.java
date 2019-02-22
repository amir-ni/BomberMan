package Frames;

import Config.LocalConfig;
import Config.ServerConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;

public class ServerFrame extends JFrame {
    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;

    public ServerFrame() {
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon("src/resources/back.png")));
        setLayout(new FlowLayout());

        setShape(new RoundRectangle2D.Double(0, 0, 750, 750, 30, 30));

        setSize(750, 750);

        JLabel btnExit = new JLabel(new ImageIcon("src/resources/btnExit2.png"));
        btnExit.setBounds(261, 445, 228, 40);
        btnExit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        add(btnExit);

        JLabel btnOptions = new JLabel(new ImageIcon("src/resources/btnOptions.png"));
        btnOptions.setBounds(261, 370, 228, 40);
        btnOptions.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }
        });
        add(btnOptions);

        JLabel btnNew = new JLabel(new ImageIcon("src/resources/btnNew.png"));
        btnNew.setBounds(261, 220, 228, 40);
        btnNew.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                LocalConfig.serverAddress = "127.0.0.1";
                LocalConfig.serverPort = ServerConfig.serverPort;
                java.awt.EventQueue.invokeLater(() -> new Frames.GameSetting().setVisible(true));
            }
        });
        add(btnNew);

        JLabel btnLoad = new JLabel(new ImageIcon("src/resources/btnLoad.png"));
        btnLoad.setBounds(261, 295, 228, 40);
        btnLoad.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                LocalConfig.serverAddress = "127.0.0.1";
                LocalConfig.serverPort = ServerConfig.serverPort;
                java.awt.EventQueue.invokeLater(() -> new Frames.ServerLoadGame().setVisible(true));
            }
        });
        add(btnLoad);

        repaint();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

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
}