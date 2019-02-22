import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import Config.LocalConfig;
import Config.ServerConfig;
import Frames.ServerFrame;
import Utilies.ServerEngine;
import org.json.*;

import javax.swing.*;

public class ServerRunner {

    private static void getPort(){
        try {
            ServerConfig.serverPort = Integer.parseInt((String) JOptionPane.showInputDialog(null, "Which port you want to use? ",
                    "Port", JOptionPane.QUESTION_MESSAGE,null,null,"8642"));
        }catch (Exception e){
            getPort();
        }
    }

    private void init() {
        getPort();
        LocalConfig.serverAddress = "127.0.0.1";
        LocalConfig.serverPort = ServerConfig.serverPort;
        LocalConfig.playerName = "Server";
        new Thread(ServerFrame::new).start();
        try {
            ServerSocket serverSocket = new ServerSocket(ServerConfig.serverPort);
            while(!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                InputStream fromClientStream = client.getInputStream();
                OutputStream toClientStream = client.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fromClientStream)));
                PrintWriter writer = new PrintWriter(toClientStream, true);
                JSONObject request = new JSONObject(reader.readLine());
                ServerEngine.startDbCon();
                int requestSender = Integer.parseInt(request.get("Sender").toString());
                String requestType = request.get("Type").toString();
                JSONObject response = new JSONObject();
                ServerConfig.currentGameId  = Integer.parseInt(request.get("GameId").toString());
                switch (requestType){
                    case "getScores":
                        response = ServerEngine.getScores();
                        break;
                    case "EndGame":
                        ServerEngine.endGame();
                        break;
                    case "Initialize":
                        response.put("GameId",ServerEngine.init(request));
                        response.put("bomberManId", ServerEngine.addBomberGuy(request.get("Name").toString()));
                        break;
                    case "InitializeSolo":
                        response.put("GameId",ServerEngine.initSolo(request));
                        break;
                    case "addBomberMan":
                        response.put("bomberManId", ServerEngine.addBomberGuy(request.get("Name").toString()));
                        break;
                    case "Connect":
                        response = ServerEngine.ConnectSolo(request);
                        break;
                    case "NewMonster":
                        ServerEngine.newMonster(request);
                        break;
                    case "GetUpdates":
                        ServerEngine.checkForBombs();
                        synchronized (ServerConfig.Games.get(ServerConfig.currentGameId).updates) {
                            response.put("Updates", new JSONArray(ServerConfig.Games.get(ServerConfig.currentGameId).updates.subList(Integer.parseInt(request.get("LatestUpdateId").toString()), ServerConfig.Games.get(ServerConfig.currentGameId).updates.size())));
                        }
                        break;
                    case "GamesDetail":
                        response.put("GamesDetail",ServerEngine.getGames());
                        break;
                    case "KeyPressed":
                        response.put("Accept",ServerEngine.keyPressed(request,requestSender));
                        break;
                    case "Chat":
                        response.put("Accept",ServerEngine.sentChat(request,requestSender));
                        break;
                }
                assert response != null;
                writer.println(response.toString());
            }
        }catch (BindException e){
            init();
        }catch (IOException | JSONException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ServerRunner().init();
    }

}