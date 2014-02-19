package def.statix.rendering;

import android.content.Context;

/**
 * Created by Lux on 19.02.14.
 */
public class Renderer {
    private RenderingSurface renderingSurface;

    public Renderer(Context context) {
        renderingSurface = new RenderingSurface(context);
    }

    public RenderingSurface getRenderingSurface() {
        return renderingSurface;
    }
}
