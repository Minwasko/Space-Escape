package ee.taltech.iti0200;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.iti0200.entities.Entity;
import ee.taltech.iti0200.entities.PlayerType;
import ee.taltech.iti0200.world.GameMap;
import ee.taltech.iti0200.world.TiledGameMap;
import org.w3c.dom.Text;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen implements Screen {
    private SpaceEscape game;


    // stage
    public Stage stage;
    public float deltaTime, currentTime, xCoolDownPoint, cCoolDownPoint, vCoolDownPoint;
    public String timeStr = "0", xLabelText, cLabelText, vLabelText;
    public BitmapFont font;
    public Label.LabelStyle labelStyle;
    public Label label, cLabel, vLabel, xLabel, zLabel;
    public Image xSkill, cSkill, vSkill, zSkill;
    public Texture xSkillTexture, cSkillTexture, vSkillTexture, zSkillTexture, corpseTexture;
    public int xCooldownTime, cCooldownTime, vCooldownTime, shakeIntensityRange = 30, shakeIntensityBuffer = 15;
    public boolean shaking, gonnaShake;
    public Random random;
    public double shakingTime = 0.15;
    public List<Entity> dead;




    // cooldowns
//    public float currentTime;
//    public float xCooldownTime = 4;
//    public float xCoolDownPoint = 0;
//    public String xLabelText = "";
//    public int cCooldownTime = 3;
//    public float cCoolDownPoint;
//    public String cLabelText = "";
//    public int vCooldownTime = 5;
//    public float vCoolDownPoint;
//    public String vLabelText = "";
//    public int zCooldownTime = 4;
//    public float zCoolDownPoint;
//    public String zLabelText = "";


    public OrthographicCamera camera;

    GameMap gameMap;

    public GameScreen(SpaceEscape game, PlayerType playerType) {


        gameMap = new TiledGameMap();
        gameMap.addPlayer(playerType);


        this.game = game;


        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label.LabelStyle labelStyleTwo = new Label.LabelStyle(font, Color.RED);

        label = new Label(timeStr, labelStyle);
        label.setPosition(Gdx.graphics.getWidth() - 60, Gdx.graphics.getHeight() - 50);
        stage.addActor(label);

        corpseTexture = new Texture("corpse.png");

        // --------------   DRAWING  ------------

        // Screen size
        int screenCenterX = Gdx.graphics.getWidth() / 2;
        int screenCenterY = Gdx.graphics.getHeight() / 2;

        // abilities
        if (playerType == PlayerType.PLAYER0) {
            zSkillTexture = new Texture("PlayerAbilities/Player0/zSkill.png");
            xSkillTexture = new Texture("PlayerAbilities/Player0/xSkill.png");
            cSkillTexture = new Texture("PlayerAbilities/Player0/cSkill.png");
            vSkillTexture = new Texture("PlayerAbilities/Player0/vSkill.png");

            xCooldownTime = 4;
            xLabelText = "";
            cCooldownTime = 3;
            cLabelText = "";
            vCooldownTime = 5;
            vLabelText = "";
        } else if (playerType == PlayerType.PLAYER1) {
            xSkillTexture = new Texture("PlayerAbilities/Player1/zSkill.png");
            cSkillTexture = new Texture("PlayerAbilities/Player1/zSkill.png");
            vSkillTexture = new Texture("PlayerAbilities/Player1/zSkill.png");
            zSkillTexture = new Texture("PlayerAbilities/Player1/zSkill.png");

            xCooldownTime = 4;
            xLabelText = "";
            cCooldownTime = 3;
            cLabelText = "";
            vCooldownTime = 5;
            vLabelText = "";
        } else if (playerType == PlayerType.PLAYER2) {
            xSkillTexture = new Texture("PlayerAbilities/Player2/cSkill.png");
            cSkillTexture = new Texture("PlayerAbilities/Player2/cSkill.png");
            vSkillTexture = new Texture("PlayerAbilities/Player2/cSkill.png");
            zSkillTexture = new Texture("PlayerAbilities/Player2/cSkill.png");

            xCooldownTime = 4;
            xLabelText = "";
            cCooldownTime = 3;
            cLabelText = "";
            vCooldownTime = 5;
            vLabelText = "";
        } else {
            xSkillTexture = new Texture("PlayerAbilities/Player3/vSkill.png");
            cSkillTexture = new Texture("PlayerAbilities/Player3/vSkill.png");
            vSkillTexture = new Texture("PlayerAbilities/Player3/vSkill.png");
            zSkillTexture = new Texture("PlayerAbilities/Player3/vSkill.png");

            xCooldownTime = 4;
            xLabelText = "";
            cCooldownTime = 3;
            cLabelText = "";
            vCooldownTime = 5;
            vLabelText = "";
        }


        zSkill = new Image(zSkillTexture);
        zSkill.setSize(75, 75);
        zSkill.setPosition(screenCenterX - 260, screenCenterY - 500);
//        zLabel = new Label(zLabelText, labelStyleTwo);
//        zLabel.setPosition(screenCenterX - 240, screenCenterY - 460);
//        zLabel.addAction(Actions.alpha(0));
//        zLabel.setFontScale(5f);


        xSkill = new Image(xSkillTexture);
        xSkill.setSize(75, 75);
        xSkill.setPosition(screenCenterX - 150, screenCenterY - 500);
        xLabel = new Label(xLabelText, labelStyleTwo);
        xLabel.setPosition(screenCenterX - 130, screenCenterY - 460);
        xLabel.addAction(Actions.alpha(0));
        xLabel.setFontScale(5f);

        cSkill = new Image(cSkillTexture);
        cSkill.setSize(75, 75);
        cSkill.setPosition(screenCenterX - 40, screenCenterY - 500);
        cLabel = new Label(cLabelText, labelStyleTwo);
        cLabel.setPosition(screenCenterX - 20, screenCenterY - 460);
        cLabel.addAction(Actions.alpha(0));
        cLabel.setFontScale(5f);

        vSkill = new Image(vSkillTexture);
        vSkill.setSize(75, 75);
        vSkill.setPosition(screenCenterX + 70, screenCenterY - 500);
        vLabel = new Label(vLabelText, labelStyleTwo);
        vLabel.setPosition(screenCenterX + 90, screenCenterY - 460);
        vLabel.addAction(Actions.alpha(0));
        vLabel.setFontScale(5f);


        stage.addActor(zSkill);
//        stage.addActor(zLabel);
        stage.addActor(xSkill);
        stage.addActor(xLabel);
        stage.addActor(cSkill);
        stage.addActor(cLabel);
        stage.addActor(vSkill);
        stage.addActor(vLabel);


        // ------------------- FINISHED DRAWING -------------


        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        random = new Random();
        dead = new ArrayList<>();



    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(35 / 255f, 34 / 255f, 47 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // current time
        deltaTime += Gdx.graphics.getDeltaTime();
        currentTime = System.currentTimeMillis();


        camera.update();
        gameMap.update(Gdx.graphics.getDeltaTime());
        gameMap.render(camera, game.batch);


        // stage
        stage.act(Gdx.graphics.getDeltaTime());


        timeStr = Double.toString(Math.round(deltaTime * 100.0) / 100.0);
        label.setText(timeStr);

//        zLabelText = String.valueOf(Math.round(zCoolDownPoint + zCooldownTime - deltaTime));
//        zLabel.setText(zLabelText);
//        if (Integer.parseInt(zLabelText) < 0) {
//            zLabel.addAction(Actions.alpha(0));
//        }
//        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
//            if (deltaTime > zCoolDownPoint + zCooldownTime) {
//                zLabel.addAction(Actions.alpha(1));
//                zSkill.addAction(Actions.alpha(0));
//                zSkill.addAction(Actions.fadeIn(5f));
//                zCoolDownPoint = deltaTime;
//            }
//        }

        xLabelText = String.valueOf(Math.round(xCoolDownPoint + xCooldownTime - deltaTime));
        xLabel.setText(xLabelText);
        if (Integer.parseInt(xLabelText) < 0) {
            xLabel.addAction(Actions.alpha(0));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if (deltaTime > xCoolDownPoint + xCooldownTime) {
                xLabel.addAction(Actions.alpha(1));
                xSkill.addAction(Actions.alpha(0));
                xSkill.addAction(Actions.fadeIn(4f));
                xCoolDownPoint = deltaTime;
                gonnaShake = true;
            }
        }
        if (gonnaShake) {
            if (deltaTime >= xCoolDownPoint + 2) {
                shaking = true;
            }
        }

        cLabelText = String.valueOf(Math.round(cCoolDownPoint + cCooldownTime - deltaTime));
        cLabel.setText(cLabelText);
        if (Integer.parseInt(cLabelText) < 0) {
            cLabel.addAction(Actions.alpha(0));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            if (deltaTime > cCoolDownPoint + cCooldownTime) {
                cLabel.addAction(Actions.alpha(1));
                cSkill.addAction(Actions.alpha(0));
                cSkill.addAction(Actions.fadeIn(3f));
                cCoolDownPoint = deltaTime;
            }
        }


        vLabelText = String.valueOf(Math.round(vCoolDownPoint + vCooldownTime - deltaTime));
        vLabel.setText(vLabelText);
        if (Integer.parseInt(vLabelText) < 0) {
            vLabel.addAction(Actions.alpha(0));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            if (deltaTime > vCoolDownPoint + vCooldownTime) {
                vLabel.addAction(Actions.alpha(1));
                vSkill.addAction(Actions.alpha(0));
                vSkill.addAction(Actions.fadeIn(5f));
                vCoolDownPoint = deltaTime;
            }
        }



        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        stage.draw();



        // CAMERA SHAKING OR STATIC
        if (shaking) {
            camera.position.x = Math.round(gameMap.getPlayer().getX()
                    + (random.nextInt(shakeIntensityRange) - shakeIntensityBuffer));
            camera.position.y = Math.round(gameMap.getPlayer().getY()
                    + (random.nextInt(shakeIntensityRange) - shakeIntensityBuffer));
            if (deltaTime >= xCoolDownPoint + 2 + shakingTime) {
                gonnaShake = false;
                shaking = false;
                System.out.println("STOP IT");
            }
        } else {
        camera.position.x = Math.round(gameMap.getPlayer().getX());
        camera.position.y = Math.round(gameMap.getPlayer().getY());
        }
        camera.update();



//        // REMOVING THE DEAD
//        for (Entity entity : gameMap.getEntities()) {
//            if (entity.getLives() <= 0) dead.add(entity);
//        }
//        System.out.println(dead);
//        for (Entity corpse : dead) {
//            game.batch.draw(corpseTexture, corpse.getX(), corpse.getY());
//        }
//        for (Entity deadEntity : dead) {
//            gameMap.removeEntity(deadEntity);
//        }
//        dead.clear();
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
        stage.dispose();
    }
}
