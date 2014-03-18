package def.statix.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import def.statix.ActivityMain;
import def.statix.R;
import def.statix.construction.unittypes.BindingType;
import def.statix.construction.unittypes.ConstructionUnitType;
import def.statix.construction.unittypes.ForceType;
import def.statix.construction.unittypes.PlankType;
import def.statix.utils.drag.DragMode;
import def.statix.utils.drag.DragableToolbox;
import def.statix.utils.ui.GestureHandler;
import def.statix.utils.ui.editors.UnitEditor;
import def.statix.utils.ui.editors.UnitEditorManager;

/**
 * Created by AdYa on 16.03.14.
 */
public class ToolboxFragment extends Fragment implements View.OnTouchListener {

    private DragableToolbox frame;
    private GestureHandler gestureHandler;
    private static int DRAG_SENSIVITY = 20;

    private static int[] TOOLS = {R.drawable.plank_icon,
            R.drawable.binding_fixed, R.drawable.binding_movalbe, R.drawable.binding_static,
            R.drawable.force_concentrated, R.drawable.force_distributed, R.drawable.force_moment};

    private ConstructionUnitType getUnitTypeFromRes(int res) {
        switch (res) {
            case R.drawable.plank_icon:
                return PlankType.PLANK;
            case R.drawable.binding_fixed:
                return BindingType.FIXED;
            case R.drawable.binding_movalbe:
                return BindingType.MOVABLE;
            case R.drawable.binding_static:
                return BindingType.STATIC;
            case R.drawable.force_concentrated:
                return ForceType.CONCENTRATED;
            case R.drawable.force_distributed:
                return ForceType.DISTRIBUTED;
            case R.drawable.force_moment:
                return ForceType.MOMENT;
            default:
                return null;
        }
    }

    private void populateTools() {
        DragableToolbox.LayoutParams lp = new DragableToolbox.LayoutParams(DragableToolbox.LayoutParams.MATCH_PARENT, DragableToolbox.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        for (int icon : TOOLS) {
            ImageView tool = new ImageView(this.getActivity());
            tool.setLayoutParams(lp);
            tool.setImageResource(icon);
            tool.setOnTouchListener(this);
            tool.setId(icon);
            frame.addView(tool);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        frame = (DragableToolbox) inflater.inflate(R.layout.toolbox_fragment, container, false);
        initDragger();
        populateTools();
        gestureHandler = new GestureHandler();
        return frame;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gestureHandler.onTouchEvent(motionEvent);

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ConstructionUnitType type = getUnitTypeFromRes(view.getId());
            UnitEditor editor = UnitEditorManager.getInstance().getEditorForUnitType(type);
            if (type instanceof PlankType) {
                ((ActivityMain) getActivity()).getSurfaceFragment().startCreatingPlank();
            } else {
                ((ActivityMain) getActivity()).getSurfaceFragment().stopCreatingPlank();
            }
            UnitEditorManager.getInstance().showEditor(editor);
            gestureHandler.reset();
        } else if (!frame.getDragController().isDragging() && (gestureHandler.getDX() > DRAG_SENSIVITY || gestureHandler.getDY() > DRAG_SENSIVITY)) {
            frame.getDragController().startDrag(view, frame, getUnitTypeFromRes(view.getId()), DragMode.DRAG_MODE_MOVE);
            UnitEditorManager.getInstance().hideActiveEditor();
        }
        return true;
    }

    private void initDragger() {
        ActivityMain activity = (ActivityMain) getActivity();
        assert (activity != null);
        frame.setDragController(activity.getDragController());
    }
}
