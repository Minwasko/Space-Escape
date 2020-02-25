package ee.taltech.iti0200;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class GameScreen implements Screen {
    private SpaceEscape game;
    private Player player;
    private Physics physics;
    private World world;

    private OrthographicCamera camera;

    public GameScreen(SpaceEscape game) {
        this.game = game;
        this.physics = new Physics();

        this.player = new Player(Gdx.files.internal("box.jpg"), 50, 50);
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 1800, 900);
    }

    @Override
    public void render(float delta) {
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        physics.move(player);
        player.boundsLeftAndRight(camera);
        player.boundsUpAndDown(camera);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(player.getCharacterImage(), player.getObject().getX(), player.getObject().getY());
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        player.getCharacterImage().dispose();
    }
}
