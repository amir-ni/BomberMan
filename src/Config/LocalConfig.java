package Config;

import Frames.GameFrame;
import Frames.ChatFrame;
import Utilies.GetUpdatesThread;
import Utilies.Lock;
import Utilies.MapPanel;
import Views.Drawable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class LocalConfig {
    public static int fullFrameWidth;
    public static int fullFrameHeight;
    public static String GameTitle;
    public static int cellCountX;
    public static int cellCountY;
    public static int cellSize;
    public static Color BackColor = Color.BLACK;
    public static String playerName;
    public static GameFrame gameFrame;
    public static final Lock LOCK = new Lock();
    public static ArrayList<Drawable> drawables;
    public static boolean isPaused;
    public static int serverPort;
    public static String serverAddress;
    public static int bomberManId = -1;
    public static int gameId = -1;
    public static int getUpdatesWaitTime = 100;
    public static Map<Long, JLabel> usersChats;
    public static ChatFrame chatFrame;
    public static int gameFrameBorderSize = 20;
    public static boolean isGameFrameFocused;
    public static boolean isScoreFrameFocused;
    public static GetUpdatesThread getUpdatesThread;
    public static int interruptionState;
    public static int level;

    public static String gameFontLocation = "src/resources/font.ttf";
    public static String gameSoundLocation = "src/resources/gameSound.wav";
    public static String bombSoundLocation = "src/resources/bombSound.wav";
    public static String btnPauseImageLocation = "src/resources/btnPause.png";
    public static String bomberManLogoImageLocation = "src/resources/bomberManLogo.png";
    public static String btnExitImageLocation = "src/resources/btnExit.png";
    public static String abstractBombImageLocation = "src/resources/abstractBomb.gif";
    public static String bombImageLocation = "src/resources/bomb.gif";
    public static Image bombBufferedImage;
    public static String bombFirstAnimationImageLocation = "src/resources/fire1.png";
    public static BufferedImage bombFirstAnimationBufferedImage;
    public static String bombSecondAnimationImageLocation = "src/resources/fire2.png";
    public static BufferedImage bombSecondAnimationBufferedImage;
    public static String grassImageLocation = "src/resources/grass.png";
    public static Image grassBufferedImage;
    public static String wallImageLocation = "src/resources/wall.png";
    public static BufferedImage pauseBufferedImage;
    public static String pauseImageLocation = "src/resources/pause.png";
    public static BufferedImage doorBufferedImage;
    public static String doorImageLocation = "src/resources/door.png";
    public static Image wallBufferedImage;
    public static String obstacleImageLocation = "src/resources/obstacle.png";
    public static Image obstacleBufferedImage;
    public static String[][][] bomberGuyImageLocation = {
        {
            {"src/resources/bombers/1/10.png" , "src/resources/bombers/1/11.png" , "src/resources/bombers/1/12.png" , "src/resources/bombers/1/13.png"},
            {"src/resources/bombers/1/20.png" , "src/resources/bombers/1/21.png" , "src/resources/bombers/1/22.png" , "src/resources/bombers/1/23.png"},
            {"src/resources/bombers/1/30.png" , "src/resources/bombers/1/31.png" , "src/resources/bombers/1/32.png" , "src/resources/bombers/1/33.png"},
            {"src/resources/bombers/1/40.png" , "src/resources/bombers/1/41.png" , "src/resources/bombers/1/42.png" , "src/resources/bombers/1/43.png"}
        },
        {
            {"src/resources/bombers/2/10.png" , "src/resources/bombers/2/11.png" , "src/resources/bombers/2/12.png" , "src/resources/bombers/2/13.png"},
            {"src/resources/bombers/2/20.png" , "src/resources/bombers/2/21.png" , "src/resources/bombers/2/22.png" , "src/resources/bombers/2/23.png"},
            {"src/resources/bombers/2/30.png" , "src/resources/bombers/2/31.png" , "src/resources/bombers/2/32.png" , "src/resources/bombers/2/33.png"},
            {"src/resources/bombers/2/40.png" , "src/resources/bombers/2/41.png" , "src/resources/bombers/2/42.png" , "src/resources/bombers/2/43.png"}
        },
        {
            {"src/resources/bombers/3/10.png" , "src/resources/bombers/3/11.png" , "src/resources/bombers/3/12.png" , "src/resources/bombers/3/13.png"},
            {"src/resources/bombers/3/20.png" , "src/resources/bombers/3/21.png" , "src/resources/bombers/3/22.png" , "src/resources/bombers/3/23.png"},
            {"src/resources/bombers/3/30.png" , "src/resources/bombers/3/31.png" , "src/resources/bombers/3/32.png" , "src/resources/bombers/3/33.png"},
            {"src/resources/bombers/3/40.png" , "src/resources/bombers/3/41.png" , "src/resources/bombers/3/42.png" , "src/resources/bombers/3/43.png"}
        }
    };
    public static Image[][][] hunterBufferedImage;

    public static String[][][] hunterImageLocation = {
            {
                    {"src/resources/hunters/1/10.png" , "src/resources/hunters/1/11.png"},
                    {"src/resources/hunters/1/20.png" , "src/resources/hunters/1/21.png"},
                    {"src/resources/hunters/1/30.png" , "src/resources/hunters/1/31.png"},
                    {"src/resources/hunters/1/40.png" , "src/resources/hunters/1/41.png"}
            },
            {
                    {"src/resources/hunters/2/10.png" , "src/resources/hunters/2/11.png"},
                    {"src/resources/hunters/2/20.png" , "src/resources/hunters/2/21.png"},
                    {"src/resources/hunters/2/30.png" , "src/resources/hunters/2/31.png"},
                    {"src/resources/hunters/2/40.png" , "src/resources/hunters/2/41.png"}
            },
            {
                    {"src/resources/hunters/3/10.png" , "src/resources/hunters/3/11.png"},
                    {"src/resources/hunters/3/20.png" , "src/resources/hunters/3/21.png"},
                    {"src/resources/hunters/3/30.png" , "src/resources/hunters/3/31.png"},
                    {"src/resources/hunters/3/40.png" , "src/resources/hunters/3/41.png"}
            },
            {
                    {"src/resources/hunters/4/10.png" , "src/resources/hunters/4/11.png"},
                    {"src/resources/hunters/4/20.png" , "src/resources/hunters/4/21.png"},
                    {"src/resources/hunters/4/30.png" , "src/resources/hunters/4/31.png"},
                    {"src/resources/hunters/4/40.png" , "src/resources/hunters/4/41.png"}
            },
            {
                    {"src/resources/hunters/5/10.png" , "src/resources/hunters/5/11.png"},
                    {"src/resources/hunters/5/20.png" , "src/resources/hunters/5/21.png"},
                    {"src/resources/hunters/5/30.png" , "src/resources/hunters/5/31.png"},
                    {"src/resources/hunters/5/40.png" , "src/resources/hunters/5/41.png"}
            }
    };
    public static BufferedImage[][][] bomberGuyBufferedImage;

    public static MapPanel mapPanel;

    public static long latestUpdateId = 0;

    public static LocalDateTime lastKeyPressSent;


}
