package ee.taltech.iti0200.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import ee.taltech.iti0200.entities.Entity;
import ee.taltech.iti0200.server.packets.*;
import ee.taltech.iti0200.world.GameMap;
import ee.taltech.iti0200.world.TiledGameMap;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameServer {
    Server server;
    Map<Connection, Player> players;
    List<Enemy> enemies;
    Connection firstConnection;

    public GameServer() throws IOException {
        players = new HashMap<>();
        enemies = new ArrayList<>();

        Enemy enemy = new Enemy();
        enemy.enemyType = "enemy0";
        enemy.id = "0";
        enemy.lives = 50;
        enemy.x = 1000;
        enemy.y = 600;

        enemies.add(enemy);

        //Enemy enemy2 = new Enemy();
        //enemy2.enemyType = "enemy1";
        //enemy2.id = "1";
        //enemy2.lives = 50;
        //enemy2.x = 2000;
        //enemy2.y = 700;

        //enemies.add(enemy2);
        //enemies.add(enemy1);

        Server server = new Server();
        server.start();
        server.bind(54556, 54778);
        Kryo kryoServer = server.getKryo();
        kryoServer.register(Register.class);
        kryoServer.register(Move.class);
        kryoServer.register(LivesLost.class);
        kryoServer.register(Player.class);
        kryoServer.register(Player[].class);
        kryoServer.register(Gun.class);
        kryoServer.register(Enemy.class);
        kryoServer.register(MoveEnemy.class);



        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof Register) {

                    if (players.isEmpty()) {
                        firstConnection = connection;
                    }

                    for (Player value : players.values()) {
                        connection.sendTCP(value);
                    }

                    for (Enemy enemy1 : enemies) {
                        connection.sendTCP(enemy1);
                    }

                    Player player = new Player();
                    player.id = ((Register) object).id;
                    player.playerType = ((Register) object).playerType;
                    player.x = 800;
                    player.y = 600;
                    player.lives = 500;

                    for (Connection c : players.keySet()) {
                        c.sendTCP(player);
                    }
                    players.put(connection, player);
                }

                if (object instanceof Move) {
                    for (Connection c : players.keySet()) {
                        if (c.equals(connection)) {
                            players.get(c).x = ((Move) object).x;
                            players.get(c).y = ((Move) object).y;
                        }
                        c.sendTCP(object);
                    }
                }

                if (object instanceof Gun) {
                    for (Connection c : players.keySet()) {
                        if (!c.equals(connection)) {
                            c.sendTCP(object);
                        }
                    }
                }

                if (object instanceof LivesLost) {
                    for (Connection c : players.keySet()) {
                        if (players.get(c).id.equals(((LivesLost) object).id)) {
                            players.get(c).lives = ((LivesLost) object).lives;
                        }
                        c.sendTCP(object);
                    }
                }

                if (object instanceof MoveEnemy) {
                    if (connection.equals(firstConnection)) {
                        for (Connection c : players.keySet()) {
                            if (!c.equals(connection)) {
                                c.sendTCP(object);
                            }
                        }
                    }
                    for (Enemy enemy1 : enemies) {
                        if (enemy1.id.equals(((MoveEnemy) object).id)) {
                            enemy1.x = ((MoveEnemy) object).x;
                            enemy1.y = ((MoveEnemy) object).y;
                        }
                    }
                }

            }
        });
    }
}