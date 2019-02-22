package Utilies;

import Config.LocalConfig;

import javax.swing.*;

public class ContainerPanel extends JPanel {
    public ContainerPanel() {
        setLayout(null);
        setBackground(LocalConfig.BackColor);
        setBounds(LocalConfig.gameFrameBorderSize, LocalConfig.gameFrameBorderSize,
                LocalConfig.fullFrameWidth, LocalConfig.fullFrameHeight);
    }

}
