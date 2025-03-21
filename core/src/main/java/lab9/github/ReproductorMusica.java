/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9.github;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 *
 * @author Maria Gabriela
 */
public class ReproductorMusica extends Game {

    public SpriteBatch batch;
    public Skin skin;
    public Stage stage;

    public ReproductorMusica() {
        // Empty constructor
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Create a skin programmatically
        skin = createBasicSkin();

        // Create the main stage with a ScreenViewport
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        setScreen(new MainPage(this));
    }

    private Skin createBasicSkin() {
        Skin skin = new Skin();

        // Generate a 1x1 white pixel texture and store it in the skin
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap.dispose();

        // Generate a 1x1 black pixel texture
        pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        skin.add("black", new Texture(pixmap));
        pixmap.dispose();

        // Generate a 1x1 gray pixel texture
        pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.5f, 0.5f, 0.5f, 1);
        pixmap.fill();
        skin.add("gray", new Texture(pixmap));
        pixmap.dispose();

        // Store a default font
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        // Configure a TextButtonStyle
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default-font");
        skin.add("default", textButtonStyle);

        // Configure a LabelStyle
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        // Create a white label style
        LabelStyle whiteLabelStyle = new LabelStyle();
        whiteLabelStyle.font = skin.getFont("default-font");
        whiteLabelStyle.fontColor = Color.WHITE;
        skin.add("white", whiteLabelStyle);

        // Create a ScrollPaneStyle
        ScrollPaneStyle scrollPaneStyle = new ScrollPaneStyle();
        scrollPaneStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        skin.add("default", scrollPaneStyle);

        // Create a ProgressBarStyle
        ProgressBarStyle progressBarStyle = new ProgressBarStyle();
        progressBarStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        progressBarStyle.knob = skin.newDrawable("white", Color.LIGHT_GRAY);
        progressBarStyle.knobBefore = skin.newDrawable("white", Color.LIGHT_GRAY);
        skin.add("default-horizontal", progressBarStyle);

        // Add "default-pane" drawable
        TextureRegionDrawable paneDrawable = new TextureRegionDrawable(new TextureRegion(skin.getRegion("white")));
        paneDrawable.setMinSize(1, 1);
        skin.add("default-pane", paneDrawable);

        // Add "default-pane-noborder" drawable
        TextureRegionDrawable paneNoBorderDrawable = new TextureRegionDrawable(new TextureRegion(skin.getRegion("gray")));
        paneNoBorderDrawable.setMinSize(1, 1);
        skin.add("default-pane-noborder", paneNoBorderDrawable);

        return skin;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        super.render();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Update the stage's viewport when the window is resized
        stage.getViewport().update(width, height, true);
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        skin.dispose();
        stage.dispose();
        super.dispose();
    }
}
