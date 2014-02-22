package def.statix.rendering;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Lux on 20.02.14.
 */
public class SceneController {
    private ArrayList<Renderable> sceneObjects; // data model.
    private RenderingSurface renderingSurface;

    public SceneController(Context context) {
        sceneObjects = new ArrayList<>();
        renderingSurface = new RenderingSurface(context);
    }

    public void addRenderableObject(Renderable object) {
        sceneObjects.add(object);
    }

    public RenderingSurface getSurface(){
        return renderingSurface;
    }
}
