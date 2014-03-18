package def.statix;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import def.statix.fragments.RenderingSurfaceFragment;
import def.statix.fragments.StatusFragment;
import def.statix.fragments.ToolboxFragment;
import def.statix.utils.drag.DragController;
import def.statix.utils.ui.editors.UnitEditorManager;

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
        if (savedInstanceState == null) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
