package def.statix.rendering;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import def.statix.R;
import def.statix.construction.Beam;

/**
 * Created by Lux on 20.02.14.
 */
public class SceneController {
    private ArrayList<Renderable> sceneObjects; // data model.
    private RenderingSurface renderingSurface;
    private Map<String, Bitmap> bitmaps;

    //used to easily locate the objects in the bitmaps hashmap;
    private final static String BEAM = "beam";
    private final static String BINDING_MOVABLE = "bindmov";
    private final static String BINDING_FIXED   = "bindfix";
    private final static String BINDING_STATIC  = "bindstc";

    public SceneController(Context context) {
        sceneObjects = new ArrayList<>();
        renderingSurface = new RenderingSurface(context);
        preloadBitmaps(context);
    }

    public RenderingSurface getSurface(){
        return renderingSurface;
    }

    public void addRenderableObject(Renderable object) {
        sceneObjects.add(object);
    }

    private void preloadBitmaps(Context context) {
        bitmaps = new HashMap<>();
        bitmaps.put(BEAM, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
    }
}
