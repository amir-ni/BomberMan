package Utilies;

import Config.LocalConfig;
import Frames.ChatFrame;
import Views.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.util.HashMap;

public class LocalEngine {

    public static Bomb findBombById(long id){
        synchronized (LocalConfig.LOCK) {
            for (Drawable drawable : LocalConfig.drawables) {
                if (drawable.getId() == id && drawable instanceof Bomb) {
                    return (Bomb) drawable;
                }
            }
        }
        return null;
    }

    public static void endGame(){
        try {
            JSONObject request = (new JSONObject()).put("Type", "EndGame");
            doSocket(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static BomberGuy findBomberGuyById(long id){
        synchronized (LocalConfig.LOCK) {
            for (Drawable drawable : LocalConfig.drawables) {
                if (drawable.getId() == id && drawable instanceof BomberGuy) {
                    return (BomberGuy) drawable;
                }
            }
        }
        return null;
    }

    public static Obstacle findObstacleById(long id){
        synchronized (LocalConfig.LOCK) {
            for (Drawable drawable : LocalConfig.drawables) {
                if (drawable.getId() == id && drawable instanceof Obstacle) {
                    return (Obstacle) drawable;
                }
            }
        }
        return null;
    }

    public static Door findDoor(){
        synchronized (LocalConfig.LOCK) {
            for (Drawable drawable : LocalConfig.drawables) {
                if (drawable instanceof Door) {
                    return (Door) drawable;
                }
            }
        }
        return null;
    }

    public static PowerUp findPowerUpById(long id){
        synchronized (LocalConfig.LOCK) {
            for (Drawable drawable : LocalConfig.drawables) {
                if (drawable.getId() == id && drawable instanceof PowerUp) {
                    return (PowerUp) drawable;
                }
            }
        }
        return null;
    }

    public static Hunter findHunterById(long id){
        synchronized (LocalConfig.LOCK) {
            for (Drawable drawable : LocalConfig.drawables) {
                if (drawable.getId() == id ) {
                    if(drawable instanceof Hunter) {
                        return (Hunter) drawable;
                    }
                }
            }
        }
        return null;
    }

    public static void UpdateHandler(JSONObject jsonResponse) throws JSONException {
        JSONArray jsonArray = (JSONArray)jsonResponse.get("Updates");
        JSONObject jsonObject;
        for(int i=0;i<jsonArray.length();i++){
            jsonObject = ((JSONObject)(jsonArray.get(i)));
            switch (jsonObject.get("updatedItemType").toString()){
                case "Bomb":
                    if(jsonObject.get("updatedField").toString().equals("Add")){
                        synchronized (LocalConfig.LOCK) {
                            LocalConfig.drawables.add(new Bomb(Integer.parseInt(jsonObject.get("updatedItemId").toString())));
                        }
                    }else {
                        Bomb bomb = findBombById(Integer.parseInt(jsonObject.get("updatedItemId").toString()));
                        switch (jsonObject.get("updatedField").toString()){
                            case "cellX":
                                bomb.cellX = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "cellY":
                                bomb.cellY = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "destructionTime":
                                bomb.destructionTime = ZonedDateTime.parse(jsonObject.get("updatedFieldValue").toString()).toLocalDateTime();
                                break;
                            case "destructionState":
                                bomb.destructionState = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                if(bomb.destructionState == 1){
                                    Utilies.MusicPlayer.play(LocalConfig.bombSoundLocation,false);
                                }
                                break;
                            case "bombExplodingRange":
                                bomb.bombExplodingRange = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                break;
                        }
                    }
                    break;
                case "BomberGuy":
                    if(jsonObject.get("updatedField").toString().equals("Add")){
                        synchronized (LocalConfig.LOCK) {
                            LocalConfig.drawables.add(new BomberGuy(Integer.parseInt(jsonObject.get("updatedItemId").toString())));
                        }
                    }else {
                        BomberGuy bomberGuy = findBomberGuyById(Integer.parseInt(jsonObject.get("updatedItemId").toString()));
                        switch (jsonObject.get("updatedField").toString()){
                            case "isAlive":
                                bomberGuy.isAlive= Boolean.parseBoolean(jsonObject.get("updatedFieldValue").toString().split(",")[0]);
                                break;
                            case "name":
                                bomberGuy.name = jsonObject.get("updatedFieldValue").toString();
                                if(bomberGuy.getId()==LocalConfig.bomberManId){
                                    LocalConfig.chatFrame.labelCurrentBomberGuyName.setText(bomberGuy.name);
                                }
                                /*if(bomberGuy.getId()!=LocalConfig.bomberManId)
                                    LocalConfig.chatFrame.newUserAdded(bomberGuy.getId(),bomberGuy.name);
                                else {
                                    LocalConfig.usersChats.put(bomberGuy.getId(), LocalConfig.chatFrame.labelCurrentBomberGuyScore);
                                    LocalConfig.chatFrame.labelCurrentBomberGuyName.setText(bomberGuy.name);
                                }*/
                                break;
                            case "currentDirection":
                                bomberGuy.currentDirection = Direction.ParseDirection(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "hunterViewModelId":
                                bomberGuy.bomberGuyViewModelId = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "score":
                                if(bomberGuy.getId()==LocalConfig.bomberManId){
                                    LocalConfig.chatFrame.labelCurrentBomberGuyScore.setText(jsonObject.get("updatedFieldValue").toString());
                                }
                                //LocalConfig.usersChats.get(bomberGuy.getId()).setText(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "moveAnimationState":
                                bomberGuy.moveAnimationState = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "bomberHeight":
                                bomberGuy.bomberHeight = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "bomberWidth":
                                bomberGuy.bomberWidth = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "location":
                                bomberGuy.location.setX(Integer.parseInt(jsonObject.get("updatedFieldValue").toString().split(",")[0]));
                                bomberGuy.location.setY(Integer.parseInt(jsonObject.get("updatedFieldValue").toString().split(",")[1]));
                                if(bomberGuy.getId() == LocalConfig.bomberManId){
                                    MapPanel.myBomberGuyX = bomberGuy.location.getX();
                                    MapPanel.myBomberGuyY = bomberGuy.location.getY();
                                }
                                break;
                        }
                    }
                    break;
                case "Obstacle":
                    if(jsonObject.get("updatedField").toString().equals("Add")){
                        synchronized (LocalConfig.LOCK) {
                            LocalConfig.drawables.add(new Obstacle(Integer.parseInt(jsonObject.get("updatedItemId").toString())));
                        }
                    }else {
                        Obstacle obstacle = findObstacleById(Integer.parseInt(jsonObject.get("updatedItemId").toString()));
                        switch (jsonObject.get("updatedField").toString()){
                            case "cellX":
                                obstacle.cellX = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "cellY":
                                obstacle.cellY = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "destructed":
                                obstacle.destructed = Boolean.parseBoolean(jsonObject.get("updatedFieldValue").toString());
                                break;
                        }
                    }
                    break;
                case "Door":
                    if(jsonObject.get("updatedField").toString().equals("Add")){
                        synchronized (LocalConfig.LOCK) {
                            LocalConfig.drawables.add(new Door(0));
                        }
                    }else {
                        Door door = findDoor();
                        switch (jsonObject.get("updatedField").toString()){
                            case "cellX":
                                door.cellX = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "cellY":
                                door.cellY = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "hidden":
                                door.hidden = Boolean.parseBoolean(jsonObject.get("updatedFieldValue").toString());
                                break;
                        }
                    }
                    break;
                case "Chat":
                    if(jsonObject.get("updatedField").toString().equals("Text")){
                        LocalConfig.chatFrame.newChatAdded(jsonObject.get("updatedFieldValue").toString());
                    }
                    break;
                case "PowerUp":
                    if(jsonObject.get("updatedField").toString().equals("Add")){
                        synchronized (LocalConfig.LOCK) {
                            LocalConfig.drawables.add(new PowerUp(Integer.parseInt(jsonObject.get("updatedItemId").toString())));
                        }
                    }else {
                        PowerUp powerUp = findPowerUpById(Integer.parseInt(jsonObject.get("updatedItemId").toString()));
                        switch (jsonObject.get("updatedField").toString()){
                            case "cellX":
                                powerUp.cellX = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "cellY":
                                powerUp.cellY = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "isUsed":
                                powerUp.isUsed = Boolean.parseBoolean(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "hidden":
                                powerUp.hidden = Boolean.parseBoolean(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "typeName":
                                try {powerUp.setDrawingImage(ImageIO.read(new File("src/resources/PowerUp"+ jsonObject.get("updatedFieldValue").toString() +".png").toURI().toURL())); powerUp.typeName = jsonObject.get("updatedFieldValue").toString(); }
                                catch (Exception e){ e.printStackTrace(); }
                                break;
                        }
                    }
                    break;
                case "Hunter":
                    if(jsonObject.get("updatedField").toString().equals("Add")){
                        synchronized (LocalConfig.LOCK) {
                            LocalConfig.drawables.add(new Hunter(Integer.parseInt(jsonObject.get("updatedItemId").toString())));
                        }
                    }else {
                        Hunter hunter = findHunterById(Integer.parseInt(jsonObject.get("updatedItemId").toString()));
                        switch (jsonObject.get("updatedField").toString()){
                            case "isAlive":
                                hunter.isAlive = Boolean.parseBoolean(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "currentDirection":
                                hunter.currentDirection = Direction.ParseDirection(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "moveAnimationState":
                                hunter.moveAnimationState = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "location":
                                hunter.location.setX(Integer.parseInt(jsonObject.get("updatedFieldValue").toString().split(",")[0]));
                                hunter.location.setY(Integer.parseInt(jsonObject.get("updatedFieldValue").toString().split(",")[1]));
                                break;
                            case "hunterViewModelId":
                                hunter.hunterViewModelId = Integer.parseInt(jsonObject.get("updatedFieldValue").toString());
                                break;
                            case "isGhost":
                                hunter.isGhost = Boolean.parseBoolean(jsonObject.get("updatedFieldValue").toString());
                                break;
                        }
                    }
                    break;
                case "Game":
                    switch (jsonObject.get("updatedField").toString()){
                        case "isPaused":
                            LocalConfig.isPaused =  Boolean.parseBoolean(jsonObject.get("updatedFieldValue").toString());
                            break;
                        case "End":
                            System.exit(0);
                            break;
                        case "levelUp":
                            LocalConfig.latestUpdateId = Integer.parseInt( jsonObject.get("Id").toString() );
                            LocalConfig.gameFrame.dispose();
                            LocalConfig.chatFrame.dispose();
                            LocalConfig.interruptionState = 1;
                            LocalEngine.ConnectToSolo(LocalConfig.gameId);

                            break;
                    }
                    break;

            }
            LocalConfig.latestUpdateId = Integer.parseInt( jsonObject.get("Id").toString() );
        }
        LocalConfig.mapPanel.repaint();
    }

    public static void setLookAndFill(){
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void CreateNewGame(int cellCountX,int cellCountY,int BombExplosionRadius,int BombTimer,int BomberManSpeed,int CellSize,int MaxBombCount,int PauseTime,int RespawnTime,String GameName,int GameFrameHeight,int GameFrameWidth){
        try {
            //setLookAndFill();
            LocalConfig.cellCountX = cellCountX;
            LocalConfig.cellCountY = cellCountY;
            LocalConfig.isPaused = false;
            LocalConfig.BackColor = Color.BLACK;
            GifUtility.resize(LocalConfig.abstractBombImageLocation,LocalConfig.bombImageLocation,CellSize,CellSize,250);
            LocalConfig.bombBufferedImage =  (new ImageIcon(LocalConfig.bombImageLocation).getImage());
            LocalConfig.bombFirstAnimationBufferedImage = ImageIO.read(new File(LocalConfig.bombFirstAnimationImageLocation).toURI().toURL());
            LocalConfig.bombSecondAnimationBufferedImage = ImageIO.read(new File(LocalConfig.bombSecondAnimationImageLocation).toURI().toURL());
            LocalConfig.grassBufferedImage = ImageIO.read(new File(LocalConfig.grassImageLocation).toURI().toURL());
            LocalConfig.wallBufferedImage = ImageIO.read(new File(LocalConfig.wallImageLocation).toURI().toURL());
            LocalConfig.pauseBufferedImage = ImageIO.read(new File(LocalConfig.pauseImageLocation).toURI().toURL());
            LocalConfig.obstacleBufferedImage = ImageIO.read(new File(LocalConfig.obstacleImageLocation).toURI().toURL()).getScaledInstance(LocalConfig.cellSize, LocalConfig.cellSize, Image.SCALE_SMOOTH);
            LocalConfig.doorBufferedImage = ImageIO.read(new File(LocalConfig.doorImageLocation).toURI().toURL());
            LocalConfig.drawables = new ArrayList<>();
            LocalConfig.usersChats = new HashMap<Long, JLabel>();
            LocalConfig.bomberGuyBufferedImage = new BufferedImage[4][4][4];
            for(int i=0;i<4;i++)
                for(int j=0;j<4;j++)
                    for(int k=0;k<3;k++)
                        LocalConfig.bomberGuyBufferedImage[k][i][j] = ImageIO.read(new File(LocalConfig.bomberGuyImageLocation[k][i][j]).toURI().toURL());
            if(!Initialize(BombExplosionRadius,BombTimer,BomberManSpeed,CellSize,MaxBombCount,PauseTime,RespawnTime,GameName,GameFrameHeight,GameFrameWidth))
            return;
            LocalConfig.chatFrame = new ChatFrame();
            (new GetUpdatesThread()).start();
             new Frames.GameFrame().setVisible(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void CreateNewSoloGame(int cellCountX,int cellCountY,int BombExplosionRadius,int BombTimer,int BomberManSpeed,int CellSize,int MaxBombCount,int PauseTime,int RespawnTime,String GameName,int GameFrameHeight,int GameFrameWidth,int level){
        try {
            //setLookAndFill();
            LocalConfig.cellCountX = cellCountX;
            LocalConfig.cellCountY = cellCountY;
            LocalConfig.isPaused = false;
            LocalConfig.BackColor = Color.BLACK;
            GifUtility.resize(LocalConfig.abstractBombImageLocation,LocalConfig.bombImageLocation,CellSize,CellSize,250);
            LocalConfig.bombBufferedImage =  (new ImageIcon(LocalConfig.bombImageLocation).getImage());
            LocalConfig.bombFirstAnimationBufferedImage = ImageIO.read(new File(LocalConfig.bombFirstAnimationImageLocation).toURI().toURL());
            LocalConfig.bombSecondAnimationBufferedImage = ImageIO.read(new File(LocalConfig.bombSecondAnimationImageLocation).toURI().toURL());
            LocalConfig.grassBufferedImage = ImageIO.read(new File(LocalConfig.grassImageLocation).toURI().toURL()).getScaledInstance(CellSize, CellSize, Image.SCALE_SMOOTH);
            LocalConfig.wallBufferedImage = ImageIO.read(new File(LocalConfig.wallImageLocation).toURI().toURL()).getScaledInstance(CellSize, CellSize, Image.SCALE_SMOOTH);
            LocalConfig.pauseBufferedImage = ImageIO.read(new File(LocalConfig.pauseImageLocation).toURI().toURL());
            LocalConfig.obstacleBufferedImage = ImageIO.read(new File(LocalConfig.obstacleImageLocation).toURI().toURL()).getScaledInstance(CellSize, CellSize, Image.SCALE_SMOOTH);
            LocalConfig.doorBufferedImage = ImageIO.read(new File(LocalConfig.doorImageLocation).toURI().toURL());
            LocalConfig.drawables = new ArrayList<>();
            LocalConfig.usersChats = new HashMap<Long, JLabel>();
            LocalConfig.bomberGuyBufferedImage = new BufferedImage[3][4][4];
            LocalConfig.level = level;
            for(int i=0;i<4;i++)
                for(int j=0;j<4;j++)
                    for(int k=0;k<3;k++)
                        LocalConfig.bomberGuyBufferedImage[k][i][j] = ImageIO.read(new File(LocalConfig.bomberGuyImageLocation[k][i][j]).toURI().toURL());
            LocalConfig.hunterBufferedImage = new Image[5][4][4];
            for(int i=0;i<4;i++)
                for(int j=0;j<2;j++)
                    for(int k=0;k<5;k++)
                        LocalConfig.hunterBufferedImage[k][i][j] = ImageIO.read(new File(LocalConfig.hunterImageLocation[k][i][j]).toURI().toURL()).getScaledInstance((CellSize*2)/3, (CellSize*2)/3, Image.SCALE_SMOOTH);
            if(!InitializeSolo(BombExplosionRadius,BombTimer,BomberManSpeed,CellSize,MaxBombCount,PauseTime,RespawnTime,GameName,GameFrameHeight,GameFrameWidth,level))
                return;
            LocalConfig.chatFrame = new ChatFrame();
            LocalConfig.interruptionState = 0;
            LocalConfig.getUpdatesThread = new GetUpdatesThread();
            LocalConfig.getUpdatesThread.start();
            LocalConfig.gameFrame = new Frames.GameFrame();
            LocalConfig.gameFrame.setVisible(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void ConnectToSolo(int gameId){

        try {
            LocalConfig.drawables = new ArrayList<>();
            LocalConfig.gameId = gameId;
            JSONObject request = (new JSONObject()).put("Name",LocalConfig.playerName).put("Type","Connect");
            JSONObject response = doSocket(request);
            //setLookAndFill();
            LocalConfig.cellCountX = Integer.parseInt(response.get("cellCountX").toString());
            LocalConfig.cellCountY = Integer.parseInt(response.get("cellCountY").toString());
            LocalConfig.isPaused = Boolean.parseBoolean(response.get("isPaused").toString());
            LocalConfig.BackColor = Color.decode(response.get("backColor").toString());
            LocalConfig.cellSize = Integer.parseInt(response.get("cellSize").toString());
            LocalConfig.fullFrameWidth = Integer.parseInt(response.get("fullFrameWidth").toString());
            LocalConfig.fullFrameHeight = Integer.parseInt(response.get("fullFrameHeight").toString());
            LocalConfig.GameTitle = response.get("gameTitle").toString();
            LocalConfig.level = Integer.parseInt(response.get("level").toString());
            if(!LocalConfig.playerName.equals("Server")) {
                request = (new JSONObject()).put("Type", "addBomberMan").put("GameId", LocalConfig.gameId).put("Name", LocalConfig.playerName);
                response = doSocket(request);
                LocalConfig.bomberManId = Integer.parseInt(response.get("bomberManId").toString());
            }
            LocalConfig.lastKeyPressSent = LocalDateTime.now();
            GifUtility.resize(LocalConfig.abstractBombImageLocation,LocalConfig.bombImageLocation,LocalConfig.cellSize,LocalConfig.cellSize,250);
            LocalConfig.bombBufferedImage =  (new ImageIcon(LocalConfig.bombImageLocation).getImage());
            LocalConfig.bombFirstAnimationBufferedImage = ImageIO.read(new File(LocalConfig.bombFirstAnimationImageLocation).toURI().toURL());
            LocalConfig.bombSecondAnimationBufferedImage = ImageIO.read(new File(LocalConfig.bombSecondAnimationImageLocation).toURI().toURL());
            LocalConfig.grassBufferedImage = ImageIO.read(new File(LocalConfig.grassImageLocation).toURI().toURL()).getScaledInstance(LocalConfig.cellSize, LocalConfig.cellSize, Image.SCALE_SMOOTH);
            LocalConfig.wallBufferedImage = ImageIO.read(new File(LocalConfig.wallImageLocation).toURI().toURL()).getScaledInstance(LocalConfig.cellSize, LocalConfig.cellSize, Image.SCALE_SMOOTH);
            LocalConfig.pauseBufferedImage = ImageIO.read(new File(LocalConfig.pauseImageLocation).toURI().toURL());
            LocalConfig.obstacleBufferedImage = ImageIO.read(new File(LocalConfig.obstacleImageLocation).toURI().toURL()).getScaledInstance(LocalConfig.cellSize, LocalConfig.cellSize, Image.SCALE_SMOOTH);
            LocalConfig.doorBufferedImage = ImageIO.read(new File(LocalConfig.doorImageLocation).toURI().toURL());
            LocalConfig.drawables = new ArrayList<>();
            LocalConfig.usersChats = new HashMap<Long, JLabel>();
            LocalConfig.bomberGuyBufferedImage = new BufferedImage[3][4][4];
            for(int i=0;i<4;i++)
                for(int j=0;j<4;j++)
                    for(int k=0;k<3;k++)
                        LocalConfig.bomberGuyBufferedImage[k][i][j] = ImageIO.read(new File(LocalConfig.bomberGuyImageLocation[k][i][j]).toURI().toURL());
            LocalConfig.hunterBufferedImage = new Image[5][4][4];
            for(int i=0;i<4;i++)
                for(int j=0;j<2;j++)
                    for(int k=0;k<5;k++)
                        LocalConfig.hunterBufferedImage[k][i][j] = ImageIO.read(new File(LocalConfig.hunterImageLocation[k][i][j]).toURI().toURL()).getScaledInstance((LocalConfig.cellSize*2)/3, (LocalConfig.cellSize*2)/3, Image.SCALE_SMOOTH);
            LocalConfig.chatFrame = new ChatFrame();
            LocalConfig.interruptionState = 0;
            LocalConfig.getUpdatesThread = new GetUpdatesThread();
            LocalConfig.getUpdatesThread.start();
            LocalConfig.gameFrame = new Frames.GameFrame();
            LocalConfig.gameFrame.setVisible(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean ConnectToGame(int gameId){
        try {
            //setLookAndFill();
            LocalConfig.BackColor = Color.BLACK;
            LocalConfig.bombFirstAnimationBufferedImage = ImageIO.read(new File(LocalConfig.bombFirstAnimationImageLocation).toURI().toURL());
            LocalConfig.bombSecondAnimationBufferedImage = ImageIO.read(new File(LocalConfig.bombSecondAnimationImageLocation).toURI().toURL());
            LocalConfig.grassBufferedImage = ImageIO.read(new File(LocalConfig.grassImageLocation).toURI().toURL());
            LocalConfig.wallBufferedImage = ImageIO.read(new File(LocalConfig.wallImageLocation).toURI().toURL());
            LocalConfig.pauseBufferedImage = ImageIO.read(new File(LocalConfig.pauseImageLocation).toURI().toURL());
            LocalConfig.obstacleBufferedImage = ImageIO.read(new File(LocalConfig.obstacleImageLocation).toURI().toURL());
            LocalConfig.drawables = new ArrayList<>();
            LocalConfig.usersChats = new HashMap<Long, JLabel>();
            LocalConfig.bomberGuyBufferedImage = new BufferedImage[4][4][4];
            for(int i=0;i<4;i++)
                for(int j=0;j<4;j++)
                    for (int k=0;k<4;k++)
                        LocalConfig.bomberGuyBufferedImage[k][i][j] = ImageIO.read(new File(LocalConfig.bomberGuyImageLocation[k][i][j]).toURI().toURL());
            LocalConfig.gameId = gameId;
            Conectialize();
            GifUtility.resize(LocalConfig.abstractBombImageLocation,LocalConfig.bombImageLocation,LocalConfig.cellSize,LocalConfig.cellSize,250);
            LocalConfig.bombBufferedImage =  (new ImageIcon(LocalConfig.bombImageLocation).getImage());
            LocalConfig.chatFrame = new ChatFrame();
            (new GetUpdatesThread()).start();
            new Frames.GameFrame().setVisible(true);
            return true;
        }catch (IOException e){
            return false;
        }
    }

    public static boolean Conectialize(){
        try {
            JSONObject request = (new JSONObject()).put("Name",LocalConfig.playerName).put("Type","Connect");
            JSONObject response = doSocket(request);
            LocalConfig.cellCountX = Integer.parseInt(response.get("CellCountX").toString());
            LocalConfig.cellCountY = Integer.parseInt(response.get("CellCountY").toString());
            LocalConfig.isPaused = Boolean.parseBoolean(response.get("IsPaused").toString());
            LocalConfig.bomberManId = Integer.parseInt(response.get("bomberManId").toString());
            LocalConfig.cellSize = Integer.parseInt(response.get("CellSize").toString());
            LocalConfig.fullFrameWidth = Integer.parseInt(response.get("FullFrameWidth").toString());
            LocalConfig.fullFrameHeight = Integer.parseInt(response.get("FullFrameHeight").toString());
            LocalConfig.GameTitle = response.get("GameTitle").toString();
            LocalConfig.BackColor = Color.decode(response.get("BackColor").toString());
            LocalConfig.lastKeyPressSent = LocalDateTime.now();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static JSONObject doSocket(JSONObject request) throws Exception{
        try {
            request.put("Sender",LocalConfig.bomberManId);
            request.put("GameId",LocalConfig.gameId);
            Socket Socket = new Socket(LocalConfig.serverAddress, LocalConfig.serverPort);
            BufferedInputStream inputStream = new BufferedInputStream(Socket.getInputStream());
            BufferedOutputStream outputStream = new BufferedOutputStream(Socket.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(inputStream)));
            PrintWriter printWriter = new PrintWriter(outputStream, true);
            printWriter.println(request.toString());
            JSONObject response = new JSONObject(bufferedReader.readLine());
            Socket.close();
            return response;
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "خطا در ارتباط با سرور", "خطا" , JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }

    public static boolean Initialize(int BombExplosionRadius,int BombTimer,int BomberManSpeed,int CellSize,int MaxBombCount,int PauseTime,int RespawnTime,String GameName ,int GameFrameHeight,int GameFrameWidth) {
        try {
            JSONObject request = (new JSONObject()).put("Name", LocalConfig.playerName).put("Type", "Initialize").put("CellCountX", LocalConfig.cellCountX).put("CellCountY", LocalConfig.cellCountY);
            request.put("GameFrameHeight", GameFrameHeight);
            request.put("GameFrameWidth", GameFrameWidth);
            request.put("BombExplosionRadius", BombExplosionRadius);
            request.put("BombTimer", BombTimer);
            request.put("BomberManSpeed", BomberManSpeed);
            request.put("CellSize", CellSize);
            request.put("GameName", GameName);
            request.put("MaxBombCount", MaxBombCount);
            request.put("PauseTime", PauseTime);
            request.put("RespawnTime", RespawnTime);
            request.put("BackColor","#" + Integer.toHexString(LocalConfig.BackColor.getRGB()).substring(2));

            JSONObject response = doSocket(request);

            LocalConfig.bomberManId = Integer.parseInt(response.get("bomberManId").toString());
            LocalConfig.cellSize = CellSize;
            LocalConfig.fullFrameWidth = GameFrameWidth;
            LocalConfig.fullFrameHeight = GameFrameHeight;
            LocalConfig.gameId = Integer.parseInt(response.get("GameId").toString());
            LocalConfig.GameTitle = GameName;
            LocalConfig.lastKeyPressSent = LocalDateTime.now();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean InitializeSolo(int BombExplosionRadius,int BombTimer,int BomberManSpeed,int CellSize,int MaxBombCount,int PauseTime,int RespawnTime,String GameName ,int GameFrameHeight,int GameFrameWidth,int level) {
        try {
            JSONObject request = (new JSONObject()).put("Name", LocalConfig.playerName).put("Type", "InitializeSolo").put("CellCountX", LocalConfig.cellCountX).put("CellCountY", LocalConfig.cellCountY);
            request.put("GameFrameHeight", GameFrameHeight);
            request.put("GameFrameWidth", GameFrameWidth);
            request.put("BombExplosionRadius", BombExplosionRadius);
            request.put("BombTimer", BombTimer);
            request.put("BomberManSpeed", BomberManSpeed);
            request.put("CellSize", CellSize);
            request.put("GameName", GameName);
            request.put("MaxBombCount", MaxBombCount);
            request.put("PauseTime", PauseTime);
            request.put("RespawnTime", RespawnTime);
            request.put("level", level);
            request.put("BackColor","#" + Integer.toHexString(LocalConfig.BackColor.getRGB()).substring(2));

            JSONObject response = doSocket(request);

            LocalConfig.cellSize = CellSize;
            LocalConfig.fullFrameWidth = GameFrameWidth;
            LocalConfig.fullFrameHeight = GameFrameHeight;
            LocalConfig.gameId = Integer.parseInt(response.get("GameId").toString());
            LocalConfig.GameTitle = GameName;
            LocalConfig.lastKeyPressSent = LocalDateTime.now();

            if(!LocalConfig.playerName.equals("Server")) {
                request = (new JSONObject()).put("Type", "addBomberMan").put("GameId", LocalConfig.gameId).put("Name", LocalConfig.playerName);
                response = doSocket(request);
                LocalConfig.bomberManId = Integer.parseInt(response.get("bomberManId").toString());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<String> getGames(){
        try {
            JSONObject request = (new JSONObject()).put("Type", "GamesDetail");
            return new ArrayList<>(Arrays.asList(doSocket(request).get("GamesDetail").toString().split(",")));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public synchronized static boolean sendChat(){
        try {
            String text = (String) JOptionPane.showInputDialog(null, "What do you want to say?",
                    "Port", JOptionPane.INFORMATION_MESSAGE,null,null,"");
            JSONObject request = (new JSONObject()).put("Type", "Chat").put("Text", text).put("Time",LocalDateTime.now().toString());
            doSocket(request);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public synchronized static boolean keyPressed(String key){
        try {
            if(LocalConfig.playerName.equals("Server"))
                return false;
            if(LocalConfig.lastKeyPressSent.isBefore(LocalDateTime.now().minusNanos(150000000))) {
                JSONObject request = (new JSONObject()).put("Type", "KeyPressed").put("Key", key);
                if(key.equals("Save")){
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new java.io.File("."));
                    chooser.setDialogTitle("انتخاب محل ذخیره");
                    chooser.setAcceptAllFileFilterUsed(false);
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Save Data", "save");
                    chooser.setFileFilter(filter);
                    if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION){
                        return false;
                    }
                    request.put("Path", chooser.getSelectedFile()+".save");
                }else if(key.equals("newHunter")){
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "CLASS files", "class");
                    chooser.setFileFilter(filter);
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int returnVal = chooser.showOpenDialog(null);
                    if(returnVal == JFileChooser.APPROVE_OPTION) {
                        request = (new JSONObject()).put("Type", "NewMonster").put("PATH",chooser.getSelectedFile().getPath());
                    }
                }
                doSocket(request);
                LocalConfig.lastKeyPressSent = LocalDateTime.now();
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
