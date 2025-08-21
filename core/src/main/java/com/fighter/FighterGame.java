package com.fighter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fighter.animations.Drawable;

public class FighterGame extends ApplicationAdapter {
    public static final int V_WIDTH = 800;
    public static final int V_HEIGHT = 480;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private TiledMap tileMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    private final Array<Drawable> objects = new Array<>();

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(V_WIDTH, V_HEIGHT, camera);
        camera.position.set(V_WIDTH / 2f, V_HEIGHT / 2f, 0);
        System.out.println("create");
        // Add your game objects here
        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        tileMap = new TmxMapLoader(new InternalFileHandleResolver())
            .load("FighterGameMap.tmx", params);
        mapRenderer = new OrthogonalTiledMapRenderer(tileMap, 1f);
        Player player = new Player("FighterSprites.png", 100, 100);
        objects.add(player);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        // --- Update ---
        for (Drawable d : objects) d.update(dt);

        // (Optional) follow player: center camera on player x/y
        // if (objects.peek() instanceof Player p) camera.position.set(p.getX(), p.getY(), 0);

        camera.update();

        // --- Draw ---
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 2) Render tilemap first (background)
        mapRenderer.setView(camera);
        mapRenderer.render();              // or render(new int[]{0,1}) for specific layers

        // 3) Render sprites on top
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Drawable d : objects) d.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        for (Drawable d : objects) d.dispose();
        // 3) dispose map-related resources
        mapRenderer.dispose();
        tileMap.dispose();
        batch.dispose();
    }
}
