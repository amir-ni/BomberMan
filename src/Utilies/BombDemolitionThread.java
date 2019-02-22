package Utilies;

import Config.ServerConfig;
import Models.*;

import static Utilies.ServerEngine.addUpdate;
import static Utilies.ServerEngine.findBombById;

public class BombDemolitionThread extends Thread {

    private long id;

    BombDemolitionThread(long id) {
        this.id = id;
    }

    public void run() {
        try {
            synchronized (this) {
                Bomb bomb = findBombById(id);
                assert bomb != null;
                bomb.setDestructionState(1);
                boolean temp;
                addUpdate("Bomb", id, "destructionState", "1");
                for (int i = bomb.getBombExplodingRange(); i >= ((-1) * bomb.getBombExplodingRange()); i--) {
                    for (Bomb otherBomb : ServerConfig.Games.get(ServerConfig.currentGameId).bombs) {
                        if (((otherBomb.getCellX() == (bomb.getCellX() + i)
                                && otherBomb.getCellY() == (bomb.getCellY()))
                                || (otherBomb.getCellX() == (bomb.getCellX())
                                        && otherBomb.getCellY() == (bomb.getCellY() + i)))
                                && otherBomb.getDestructionState() == 0) {
                            (new BombDemolitionThread(otherBomb.getId())).start();
                        }
                    }
                    temp = false;
                    for (Obstacle obstacle : ServerConfig.Games.get(ServerConfig.currentGameId).obstacles) {
                        if ((obstacle.getCellX() == (bomb.getCellX() + i) && obstacle.getCellY() == (bomb.getCellY()))
                                || (obstacle.getCellX() == (bomb.getCellX())
                                        && obstacle.getCellY() == (bomb.getCellY() + i))) {
                            obstacle.destruct();
                            ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[obstacle.getCellX()][obstacle
                                    .getCellY()] = BlocksState.Grass;

                            addUpdate("Obstacle", obstacle.getId(), "destructed",
                                    String.valueOf(obstacle.isDestructed()));
                            ServerEngine.increaseScore(bomb.getOwner(),
                                    ServerConfig.Games.get(ServerConfig.currentGameId).defaultObstacleDestructionPoint);
                            temp = true;
                            break;
                        }
                    }
                    for (Hunter hunter : ServerConfig.Games.get(ServerConfig.currentGameId).hunters) {
                        if (!hunter.isAlive())
                            continue;
                        if ((((hunter.getLocation().getX())
                                / ServerConfig.Games.get(ServerConfig.currentGameId).cellSize) == (bomb.getCellX() + i)
                                && (hunter.getLocation().getY()
                                        / ServerConfig.Games.get(ServerConfig.currentGameId).cellSize) == (bomb
                                                .getCellY()))
                                || ((hunter.getLocation().getX()
                                        / ServerConfig.Games.get(ServerConfig.currentGameId).cellSize) == (bomb
                                                .getCellX())
                                        && ((hunter.getLocation().getY())
                                                / ServerConfig.Games.get(ServerConfig.currentGameId).cellSize) == (bomb
                                                        .getCellY() + i))) {
                            ServerEngine.hunterManDie(hunter);
                            ServerEngine.increaseScore(bomb.getOwner(),
                                    ServerConfig.Games.get(ServerConfig.currentGameId).defaultHunterDestructionPoint);
                        }
                    }
                    if (temp) {
                        for (PowerUp powerUp : ServerConfig.Games.get(ServerConfig.currentGameId).powerUps) {
                            if ((powerUp.getCellX() == (bomb.getCellX() + i) && powerUp.getCellY() == (bomb.getCellY()))
                                    || (powerUp.getCellX() == (bomb.getCellX())
                                            && powerUp.getCellY() == (bomb.getCellY() + i))) {
                                ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[powerUp
                                        .getCellX()][powerUp.getCellY()] = BlocksState.PowerUp;
                                addUpdate("PowerUp", powerUp.getId(), "hidden", String.valueOf(false));
                                break;
                            }
                        }
                        if ((ServerConfig.Games.get(ServerConfig.currentGameId).door.getCellX() == (bomb.getCellX() + i)
                                && ServerConfig.Games.get(ServerConfig.currentGameId).door
                                        .getCellY() == (bomb.getCellY()))
                                || (ServerConfig.Games.get(ServerConfig.currentGameId).door
                                        .getCellX() == (bomb.getCellX())
                                        && ServerConfig.Games.get(ServerConfig.currentGameId).door
                                                .getCellY() == (bomb.getCellY() + i))) {
                            ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[ServerConfig.Games
                                    .get(ServerConfig.currentGameId).door
                                            .getCellX()][ServerConfig.Games.get(ServerConfig.currentGameId).door
                                                    .getCellY()] = BlocksState.Door;
                            addUpdate("Door", 0, "hidden", String.valueOf(false));

                        }
                        continue;
                    }

                    for (BomberGuy bomberGuy : ServerConfig.Games.get(ServerConfig.currentGameId).bomberGuys) {
                        if (!bomberGuy.isAlive())
                            continue;
                        if ((((bomberGuy.getLocation().getX())
                                / ServerConfig.Games.get(ServerConfig.currentGameId).cellSize) == (bomb.getCellX() + i)
                                && (bomberGuy.getLocation().getY()
                                        / ServerConfig.Games.get(ServerConfig.currentGameId).cellSize) == (bomb
                                                .getCellY()))
                                || ((bomberGuy.getLocation().getX()
                                        / ServerConfig.Games.get(ServerConfig.currentGameId).cellSize) == (bomb
                                                .getCellX())
                                        && ((bomberGuy.getLocation().getY() + bomberGuy.getBomberHeight())
                                                / ServerConfig.Games.get(ServerConfig.currentGameId).cellSize) == (bomb
                                                        .getCellY() + i))) {
                            ServerEngine.bomberManDie(bomberGuy);
                            if (bomberGuy.getId() != bomb.getOwner().getId())
                                ServerEngine.bomberManKill(bomb.getOwner());
                        }
                    }
                    for (PowerUp powerUp : ServerConfig.Games.get(ServerConfig.currentGameId).powerUps) {
                        if ((powerUp.getCellX() == (bomb.getCellX() + i) && powerUp.getCellY() == (bomb.getCellY()))
                                || (powerUp.getCellX() == (bomb.getCellX())
                                        && powerUp.getCellY() == (bomb.getCellY() + i))) {
                            powerUp.setUsed(true);
                            addUpdate("PowerUp", powerUp.getId(), "isUsed", String.valueOf(powerUp.isUsed()));
                        }
                    }

                }
                wait(ServerConfig.Games.get(ServerConfig.currentGameId).defaultBombDestructionAnimationTime);
                addUpdate("Bomb", id, "destructionState", "2");
                bomb.setDestructionState(2);
                wait(ServerConfig.Games.get(ServerConfig.currentGameId).defaultBombDestructionAnimationTime);
                addUpdate("Bomb", id, "destructionState", "3");
                bomb.setDestructionState(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
