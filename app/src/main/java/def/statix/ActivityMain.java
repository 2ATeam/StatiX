package def.statix;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import def.statix.editors.UnitEditorManager;
import def.statix.fragments.RenderingSurfaceFragment;
import def.statix.fragments.StatusFragment;
import def.statix.fragments.ToolboxFragment;
import def.statix.utils.drag.DragController;

public class ActivityMain extends FragmentActivity {

    private DragController dragController;

    public RenderingSurfaceFragment getSurfaceFragment() {
        return surfaceFragment;
    }

    private RenderingSurfaceFragment surfaceFragment;

    public DragController getDragController() {
        return dragController;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dragController = new DragController(this);
        UnitEditorManager.getInstance().prepare(this, R.id.editor_container);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.toolbox_container, new ToolboxFragment())
                .commit();
        surfaceFragment = new RenderingSurfaceFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.surface_container, surfaceFragment)
                .commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.status_container, new StatusFragment())
                .commit();
    }
}
