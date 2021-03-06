package ee.taltech.iti0200;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StartScreen implements Screen {

    private final SpaceEscape game;
    public OrthographicCamera camera;
    public SpriteBatch batch;
    public Texture spaceEscape, background, pressEnter;

    public StartScreen(final SpaceEscape game) {
        this.game = game;

        batch = game.batch;

        spaceEscape = new Texture("space_escape.png");
        background = new Texture("menubackground.png");
        pressEnter = new Texture("press-enter.png");
        Gdx.input.setCursorCatched(true);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        batch.draw(background, (float) (-0.5 * background.getWidth()), (float) (-0.5 * background.getHeight()));
        batch.draw(spaceEscape, (float) (-0.5 * spaceEscape.getWidth()), spaceEscape.getHeight() * 4);
        batch.draw(pressEnter, (float) (-0.5 * pressEnter.getWidth()), -pressEnter.getHeight() * 6);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new MenuScreen(game));
            dispose();
        }

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
