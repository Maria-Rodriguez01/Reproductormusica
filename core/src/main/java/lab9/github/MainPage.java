/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9.github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Maria Gabriela
 */
public class MainPage implements Screen {

    private final ReproductorMusica game;
    private Music currentMusic;
    private Cancion currentSong;
    private boolean isPaused = false;
    private boolean isPlaying = false;

    // UI Elements
    private Image albumImage;
    private Label titleLabel;
    private Label artistLabel;
    private Label genreLabel;
    private Label durationLabel;
    private Label currentTimeLabel;
    private ProgressBar progressBar;
    private Table songListTable;
    private ScrollPane songListScroll;
    private Table rightPanel;
    private Table mainTable;

    private SongList songList;

    private Texture defaultAlbumCover;
    private Map<String, Texture> customAlbumCovers = new HashMap<>();

    private float elapsedTime = 0f;

    public MainPage(ReproductorMusica game) {
        this.game = game;
        initializeDefaultCover(); // Asegurarte de que no sea nulo
        songList = new SongList();
        createUI();
    }
    
    private void createUI() {
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.padLeft(10).padRight(10).padTop(10).padBottom(10);

        Table leftPanel = new Table();
        if (game.skin.has("default-pane", Drawable.class)) {
            leftPanel.setBackground(game.skin.getDrawable("default-pane"));
        } else {
            // Fallback: Create a simple gray background
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.GRAY);
            pixmap.fill();
            Texture texture = new Texture(pixmap);
            pixmap.dispose();
            leftPanel.setBackground(new TextureRegionDrawable(new TextureRegion(texture)));
        }

        // Create a label style with proper font
        Label.LabelStyle labelStyle;
        if (game.skin.has("default-font", BitmapFont.class)) {
            labelStyle = new Label.LabelStyle(game.skin.getFont("default-font"), Color.WHITE);
        } else {
            // Fallback: Create a default font
            labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        }
        
        if (game.skin.has("white", Color.class)) {
            labelStyle.fontColor = game.skin.getColor("white");
        }
        
        Label playlistTitle = new Label("Lista de Reproducción", labelStyle);
        playlistTitle.setAlignment(Align.center);

        rightPanel = new Table();

        songListTable = new Table();
        if (game.skin.has("default-pane", Drawable.class)) {
            songListTable.setBackground(game.skin.getDrawable("default-pane"));
        }

        songListScroll = new ScrollPane(songListTable, game.skin);
        songListScroll.setFadeScrollBars(false);

        // Playlist buttons
        Table playlistButtonsTable = new Table();
        TextButton addButton = new TextButton("Agregar", game.skin);
        TextButton selectButton = new TextButton("Seleccionar", game.skin);
        TextButton removeButton = new TextButton("Eliminar", game.skin);

        playlistButtonsTable.add(addButton).padRight(5);
        playlistButtonsTable.add(selectButton).padRight(5);
        playlistButtonsTable.add(removeButton);

        leftPanel.add(playlistTitle).padTop(10).padBottom(10).expandX().fillX().row();
        leftPanel.add(songListScroll).expand().fill().row();
        leftPanel.add(playlistButtonsTable).padTop(10).padBottom(10).row();

        albumImage = new Image(new TextureRegionDrawable(new TextureRegion(defaultAlbumCover)));
        albumImage.setSize(200, 200);

        // Make sure we have a proper label style with font for all labels
        Label.LabelStyle defaultLabelStyle = createDefaultLabelStyle();
        
        titleLabel = new Label("Sin reproducción", defaultLabelStyle);
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(1.5f);

        artistLabel = new Label("", defaultLabelStyle);
        artistLabel.setAlignment(Align.center);

        genreLabel = new Label("", defaultLabelStyle);
        genreLabel.setAlignment(Align.center);

        Table progressTable = new Table();
        currentTimeLabel = new Label("0:00", defaultLabelStyle);
        durationLabel = new Label("0:00", defaultLabelStyle);
        
        // Create a progress bar with proper style
        ProgressBar.ProgressBarStyle progressBarStyle;
        if (game.skin.has("default-horizontal", ProgressBar.ProgressBarStyle.class)) {
            progressBarStyle = game.skin.get("default-horizontal", ProgressBar.ProgressBarStyle.class);
        } else {
            // Create a fallback progress bar style
            progressBarStyle = new ProgressBar.ProgressBarStyle();
            
            // Create a background drawable
            Pixmap bgPixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
            bgPixmap.setColor(Color.DARK_GRAY);
            bgPixmap.fill();
            Texture bgTexture = new Texture(bgPixmap);
            bgPixmap.dispose();
            progressBarStyle.background = new TextureRegionDrawable(new TextureRegion(bgTexture));
            
            // Create a knob drawable
            Pixmap knobPixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
            knobPixmap.setColor(Color.LIGHT_GRAY);
            knobPixmap.fill();
            Texture knobTexture = new Texture(knobPixmap);
            knobPixmap.dispose();
            progressBarStyle.knob = new TextureRegionDrawable(new TextureRegion(knobTexture));
            
            // Create a knobBefore drawable (the filled part of the progress bar)
            Pixmap knobBeforePixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
            knobBeforePixmap.setColor(Color.LIGHT_GRAY);
            knobBeforePixmap.fill();
            Texture knobBeforeTexture = new Texture(knobBeforePixmap);
            knobBeforePixmap.dispose();
            progressBarStyle.knobBefore = new TextureRegionDrawable(new TextureRegion(knobBeforeTexture));
        }
        
        progressBar = new ProgressBar(0, 1, 0.01f, false, progressBarStyle);

        progressTable.add(currentTimeLabel).padRight(5);
        progressTable.add(progressBar).expandX().fillX();
        progressTable.add(durationLabel).padLeft(5);

        Table controlButtonsTable = new Table();
        TextButton playButton = new TextButton("Play", game.skin);
        TextButton pauseButton = new TextButton("Pause", game.skin);
        TextButton stopButton = new TextButton("Stop", game.skin);

        TextButton customAlbumButton = new TextButton("Cambiar Portada", game.skin);

        controlButtonsTable.add(playButton).padRight(5);
        controlButtonsTable.add(pauseButton).padRight(5);
        controlButtonsTable.add(stopButton);

        rightPanel.add(albumImage).size(200, 200).padTop(20).row();
        rightPanel.add(titleLabel).padTop(20).expandX().fillX().row();
        rightPanel.add(artistLabel).padTop(5).row();
        rightPanel.add(genreLabel).padTop(5).row();
        rightPanel.add(progressTable).padTop(20).expandX().fillX().row();
        rightPanel.add(controlButtonsTable).padTop(20).row();
        rightPanel.add(customAlbumButton).padTop(10).row();

        mainTable.add(leftPanel).width(300).fillY().expandY();
        mainTable.add(rightPanel).expand().fill();

        game.stage.addActor(mainTable);

        addButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addSong();
            }
        });

        selectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectSong();
            }
        });

        removeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                removeSong();
            }
        });

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                play();
            }
        });

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pause();
            }
        });

        stopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stop();
            }
        });

        customAlbumButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentSong != null) {
                    selectCustomAlbumCover();
                }
            }
        });
    }
    
    private Label.LabelStyle createDefaultLabelStyle() {
        Label.LabelStyle style;
        if (game.skin.has("default-font", BitmapFont.class)) {
            style = new Label.LabelStyle(game.skin.getFont("default-font"), Color.WHITE);
        } else {
            style = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        }
        
        if (game.skin.has("white", Color.class)) {
            style.fontColor = game.skin.getColor("white");
        }
        
        return style;
    }

    private void initializeDefaultCover() {
        if (defaultAlbumCover == null) {
            try {
                defaultAlbumCover = new Texture(Gdx.files.internal("Smileface.png"));
            } catch (GdxRuntimeException e) {
                System.err.println("Error al cargar la portada predeterminada: " + e.getMessage());
                // Create a blank texture as a fallback
                Pixmap blankPixmap = new Pixmap(200, 200, Pixmap.Format.RGBA8888);
                blankPixmap.setColor(Color.GRAY);
                blankPixmap.fill();
                defaultAlbumCover = new Texture(blankPixmap);
                blankPixmap.dispose();
            }
        }
    }

    private void selectCustomAlbumCover() {
        if (currentSong == null || currentSong.getSongPath() == null) {
            return;
        }

        new Thread(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar imagen de portada");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes (*.png, *.jpg, *.jpeg)", "png", "jpg", "jpeg"));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();

                Gdx.app.postRunnable(() -> {
                    try {
                        FileHandle fileHandle = Gdx.files.absolute(filePath);

                        if (customAlbumCovers.containsKey(currentSong.getSongPath())) {
                            customAlbumCovers.get(currentSong.getSongPath()).dispose();
                        }

                        Texture newAlbumTexture = new Texture(fileHandle);
                        customAlbumCovers.put(currentSong.getSongPath(), newAlbumTexture);

                        currentSong.setAlbumCover(newAlbumTexture);
                        updateSongInfo();
                    } catch (Exception e) {
                        System.err.println("Error al cargar la imagen: " + e.getMessage());
                    }
                });
            }
        }).start();
    }

    private void addSong() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar canción");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de Audio (*.mp3, *.wav)", "mp3", "wav"));

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            // Extraer información de la canción (en un caso real podríamos usar una librería para metadata)
            String fileName = selectedFile.getName();
            String songName = fileName.substring(0, fileName.lastIndexOf('.'));
            String artist = "Artista Desconocido";
            String genre = "Género Desconocido";
            String duration = "3:45";

            Texture albumCover = defaultAlbumCover;

            Cancion newSong = new Cancion(filePath, songName, artist, duration, albumCover, genre);
            songList.add(newSong);

            updateSongList();

            if (songList.getSize() == 1) {
                currentSong = newSong;
                updateSongInfo();
            }
        }
    }

    private void selectSong() {
        if (songList.getSize() > 0) {
            currentSong = songList.getFirstSong();
            updateSongInfo();
            play();
        }
    }

    private void removeSong() {
        if (songList.getSize() > 0) {
            Cancion songToRemove = songList.getFirstSong();

            if (currentSong == songToRemove) {
                stop();
                currentSong = null;
                updateSongInfo();
            }

            // Remove custom album cover if exists
            if (customAlbumCovers.containsKey(songToRemove.getSongPath())) {
                customAlbumCovers.get(songToRemove.getSongPath()).dispose();
                customAlbumCovers.remove(songToRemove.getSongPath());
            }

            songList.remove(0);
            updateSongList();
        }
    }

    private void play() {
        if (currentSong != null) {
            if (!isPlaying) {
                try {
                    if (currentMusic == null || !isPaused) {
                        if (currentMusic != null) {
                            currentMusic.dispose();
                        }

                        FileHandle musicFile = Gdx.files.absolute(currentSong.getSongPath());
                        currentMusic = Gdx.audio.newMusic(musicFile);
                        currentMusic.setOnCompletionListener(music -> {
                            isPlaying = false;
                            elapsedTime = 0f;
                        });
                    }

                    currentMusic.play();
                    isPlaying = true;
                    isPaused = false;
                } catch (GdxRuntimeException e) {
                    System.err.println("Error al reproducir la música: " + e.getMessage());
                    // Handle the error (e.g., show a message to the user)
                }
            } else if (isPaused) {
                currentMusic.play();
                isPaused = false;
            }
        }
    }

    @Override
    public void pause() {
        if (isPlaying && !isPaused && currentMusic != null) {
            currentMusic.pause();
            isPaused = true;
        }
    }

    private void stop() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
            isPlaying = false;
            isPaused = false;
            elapsedTime = 0f;
            progressBar.setValue(0);
            currentTimeLabel.setText("0:00");
        }
    }

    private void updateSongList() {
        songListTable.clear();
        
        Label.LabelStyle headerStyle = createDefaultLabelStyle();
        
        songListTable.add(new Label("Nombre", headerStyle)).expandX().fillX();
        songListTable.add(new Label("Artista", headerStyle)).expandX().fillX();
        songListTable.add(new Label("Género", headerStyle)).expandX().fillX();
        songListTable.add(new Label("Duración", headerStyle)).expandX().fillX().row();

        CancionNodo current = songList.getHead();
        int index = 0;
        
        Label.LabelStyle rowStyle = createDefaultLabelStyle();

        while (current != null) {
            final int songIndex = index;
            final Cancion song = current.getSong();

            Table row = new Table();
            Drawable rowBackground;
            
            if (index % 2 == 0 && game.skin.has("default-pane", Drawable.class)) {
                rowBackground = game.skin.getDrawable("default-pane");
            } else if (game.skin.has("default-pane-noborder", Drawable.class)) {
                rowBackground = game.skin.getDrawable("default-pane-noborder");
            } else {
                // Create a fallback background
                Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                pixmap.setColor(index % 2 == 0 ? new Color(0.2f, 0.2f, 0.2f, 1) : new Color(0.25f, 0.25f, 0.25f, 1));
                pixmap.fill();
                Texture texture = new Texture(pixmap);
                pixmap.dispose();
                rowBackground = new TextureRegionDrawable(new TextureRegion(texture));
            }
            
            row.setBackground(rowBackground);

            row.add(new Label(song.getSongName(), rowStyle)).expandX().fillX();
            row.add(new Label(song.getArtist(), rowStyle)).expandX().fillX();
            row.add(new Label(song.getMusicGenre(), rowStyle)).expandX().fillX();
            row.add(new Label(song.getDuration(), rowStyle)).expandX().fillX();

            row.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentSong = song;
                    updateSongInfo();
                    play();
                }
            });

            songListTable.add(row).expandX().fillX().height(40).colspan(4).row();

            current = current.getNext();
            index++;
        }
    }

    private void updateSongInfo() {
        if (defaultAlbumCover != null) {
            albumImage.setDrawable(new TextureRegionDrawable(new TextureRegion(defaultAlbumCover)));
        }

        if (currentSong != null) {
            titleLabel.setText(currentSong.getSongName());
            artistLabel.setText(currentSong.getArtist());
            genreLabel.setText(currentSong.getMusicGenre());
            durationLabel.setText(currentSong.getDuration());

            if (customAlbumCovers.containsKey(currentSong.getSongPath())) {
                albumImage.setDrawable(new TextureRegionDrawable(new TextureRegion(customAlbumCovers.get(currentSong.getSongPath()))));
            } else if (currentSong.getAlbumCover() != null && currentSong.getAlbumCover() != defaultAlbumCover) {
                albumImage.setDrawable(new TextureRegionDrawable(new TextureRegion(currentSong.getAlbumCover())));
            } else {
                albumImage.setDrawable(new TextureRegionDrawable(new TextureRegion(defaultAlbumCover)));
            }
        } else {
            titleLabel.setText("Sin reproducción");
            artistLabel.setText("");
            genreLabel.setText("");
            durationLabel.setText("0:00");
            currentTimeLabel.setText("0:00");
            progressBar.setValue(0);
            albumImage.setDrawable(new TextureRegionDrawable(new TextureRegion(defaultAlbumCover)));
        }
    }

    private void updateProgress(float delta) {
        if (isPlaying && !isPaused && currentMusic != null) {
            elapsedTime += delta;

            float position = currentMusic.getPosition();

            progressBar.setValue(position);

            int minutes = (int) (position / 60);
            int seconds = (int) (position % 60);
            currentTimeLabel.setText(String.format("%d:%02d", minutes, seconds));
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        updateProgress(delta);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        mainTable.remove();
    }

    @Override
    public void dispose() {
        if (currentMusic != null) {
            currentMusic.dispose();
        }
        defaultAlbumCover.dispose();

        // Dispose all custom album covers
        for (Texture texture : customAlbumCovers.values()) {
            if (texture != null) {
                texture.dispose();
            }
        }
    }
}
