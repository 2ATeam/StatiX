package def.statix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import def.statix.rendering.RenderingSurface;

public class ActivityMain extends ActionBarActivity {

    RenderingSurface surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        surface.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        surface.resume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment implements View.OnTouchListener {
        //test
        private Bitmap balka;

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            //View rootView = inflater.inflate(R.layout.fragment_activity_main, container, false);
            surface = new RenderingSurface(this.getActivity());

            //test demo
                balka = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                surface.setBalka(balka);
            //
            surface.setOnTouchListener(this);
            return surface;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN ||
               motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                //test
                    surface.moveBalka(motionEvent.getX(), motionEvent.getY());
            }
            return true;
        }
    }

}
