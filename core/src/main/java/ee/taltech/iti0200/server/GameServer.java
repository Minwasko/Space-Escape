package ee.taltech.iti0200.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import ee.taltech.iti0200.server.packets.*;

import java.io.IOException;
import java.util.*;

public class GameServer {
    public Server server;
    public Map<Connection, Player> players;
    public List<Enemy> enemies;
    public List<Connection> firstConnection;
    public List<Integer> enemyX = new ArrayList<>(Arrays.asList(1430, 1730, 1470, 2008, 1100, 2743, 2995, 3230));
    public List<Integer> enemyY = new ArrayList<>(Arrays.asList(560, 450, 1040, 770, 1825, 1875, 625, 800));
    public List<Integer> playerX = new ArrayList<>(Arrays.asList(800, 1026, 1400, 1024, 1678, 2337, 2892));
    public List<Integer> playerY = new ArrayList<>(Arrays.asList(450, 580, 465, 865, 945, 1475, 1140));

    public GameServer() throws IOException {
        players = new HashMap<>();
        enemies = new ArrayList<>();
        firstConnection = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Enemy enemy = new Enemy();
            enemy.enemyType = "enemy0";
            enemy.id = "" + i;
            enemy.lives = 10;
            int coordinates = (int) (Math.random() * (7));
            enemy.x = enemyX.get(coordinates);
            enemy.y = enemyY.get(coordinates);
            enemies.add(enemy);
        }
        for (int i = 4; i < 8; i++) {
            Enemy enemy = new Enemy();
            enemy.enemyType = "enemy1";
            enemy.id = "" + i;
            enemy.lives = 10;
            int coordinates = (int) (Math.random() * (7));
            enemy.x = enemyX.get(coordinates);
            enemy.y = enemyY.get(coordinates);
            enemies.add(enemy);
        }
        for (int i = 8; i < 12; i++) {
            Enemy enemy = new Enemy();
            enemy.enemyType = "enemy2";
            enemy.id = "" + i;
            enemy.lives = 5;
            int coordinates = (int) (Math.random() * (7));
            enemy.x = enemyX.get(coordinates);
            enemy.y = enemyY.get(coordinates);
            enemies.add(enemy);
        }

        server = new Server();
        server.start();
        server.bind(5200, 5201);
        Kryo kryoServer = server.getKryo();
        kryoServer.register(Register.class);
        kryoServer.register(Move.class);
        kryoServer.register(LivesLost.class);
        kryoServer.register(Player.class);
        kryoServer.register(Player[].class);
        kryoServer.register(Gun.class);
        kryoServer.register(Enemy.class);
        kryoServer.register(MoveEnemy.class);
        kryoServer.register(Death.class);
        kryoServer.register(Ability.class);
        kryoServer.register(Drone.class);
        kryoServer.register(SmallDrone.class);
        kryoServer.setRegistrationRequired(true);

        Log.set(Log.LEVEL_TRACE);


        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {

                if (object instanceof Register) {

                    for (Player value : players.values()) {
                        connection.sendTCP(value);
                    }

                    for (Enemy enemy1 : enemies) {
                        connection.sendTCP(enemy1);
                    }

                    Player player = new Player();
                    player.id = ((Register) object).id;
                    player.playerType = ((Register) object).playerType;
                    player.lives = 500;
                    int coordinates = (int) (Math.random() * (6));
                    player.x = playerX.get(coordinates);
                    player.y = playerY.get(coordinates);

                    connection.sendTCP(player);

                    server.sendToAllTCP(player);
                    players.put(connection, player);
                    firstConnection.add(connection);
                }

                if (object instanceof Move) {
                    for (Connection c : players.keySet()) {
                        if (c.equals(connection)) {
                            players.get(c).x = ((Move) object).x;
                            players.get(c).y = ((Move) object).y;
                        }
                    }
                    server.sendToAllUDP(object);
                }

                if (object instanceof Gun) {
                    for (Connection c : players.keySet()) {
                        if (!c.equals(connection)) {
                            c.sendUDP(object);
                        }
                    }
                }

                if (object instanceof Ability) {
                    for (Connection c : players.keySet()) {
                        if (!c.equals(connection)) {
                            c.sendUDP(object);
                        }
                    }
                }

                if (object instanceof Drone) {
                    for (Connection c : players.keySet()) {
                        if (!c.equals(connection)) {
                            c.sendUDP(object);
                        }
                    }
                }

                if (object instanceof SmallDrone) {
                    for (Connection c : players.keySet()) {
                        if (!c.equals(connection)) {
                            c.sendUDP(object);
                        }
                    }
                }

                if (object instanceof LivesLost) {
                    for (Connection c : players.keySet()) {
                        if (players.get(c).id.equals(((LivesLost) object).id)) {
                            players.get(c).lives = ((LivesLost) object).lives;
                            break;
                        }
                    }
                    server.sendToAllTCP(object);
                }

                if (object instanceof MoveEnemy) {
                    if (connection.equals(firstConnection.get(0))) {
                        for (Connection c : players.keySet()) {
                            if (!c.equals(connection)) {
                                c.sendUDP(object);
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

                if (object instanceof Death) {
                    server.sendToAllTCP(object);
                    for (Enemy enemy : enemies) {
                        if (enemy.id.equals(((Death) object).id)) {
                            enemy.lives = 50;
                            int coordinates = (int) (Math.random() * (7));
                            enemy.x = enemyX.get(coordinates);
                            enemy.y = enemyY.get(coordinates);
                            server.sendToAllTCP(enemy);
                            break;
                        }
                    }
                    for (Connection connection1 : players.keySet()) {
                        if (players.get(connection1).id.equals(((Death) object).id)) {
                            players.remove(connection1);
                            break;
                        }
                    }

                }

            }

            public void disconnected(Connection c) {
                if (players.containsKey(c)) {
                    Death death = new Death();
                    death.id = players.get(c).id;
                    firstConnection.remove(c);
                    players.remove(c);
                    server.sendToAllTCP(death);
                }
            }
        });
    }
}
