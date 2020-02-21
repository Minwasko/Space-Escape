package ee.taltech.iti0200;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor {
    private Texture characterImage;
    private Actor object;

    public Player(FileHandle characterImage, long height, long width) {
        this.object = new Actor();
        this.characterImage = new Texture(characterImage);
        this.object.setHeight(height);
        this.object.setWidth(width);
        this.object.setX(20);
        this.object.setY(20);
    }

    public Actor getObject() {
        return object;
    }

    public Texture getCharacterImage() {
        return characterImage;
    }

    public float getXPosition() {
        return this.object.getX();
    }

    public float getYPosition() {
        return this.object.getY();
    }
}
