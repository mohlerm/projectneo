package ch.baws.projectneo;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.audiofx.AudioEffect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import ch.baws.projectneo.effects.*;
import ch.baws.projectneo.effects.Effect.DialogOptions;
import ch.baws.projectneo.frameGenerator.Frame;
import ch.baws.projectneo.sendService.SendService;

public class TrinityActivity extends Activity{
	final static boolean D = false;
	final static String TAG = "TrinityActivity";
	public void setEffectsInList(){
		effects = new ArrayList<Class>();	
	//+++++++++++++++++++ List all available Effects ++++++++++++++++++
		effects.add(AudioVisualizer.class);
		effects.add(Text.class);
		effects.add(Buttons.class);
		effects.add(Matrix.class);
		effects.add(Nexus.class);
		effects.add(GameOfLife.class);
		effects.add(StarSky.class);
		effects.add(BinaryClock.class); 	
		effects.add(Tetris.class);
		effects.add(HumanSnakePlayer.class);
		effects.add(RainbowEffect.class);
		effects.add(Wave.class);
		effects.add(Colorfield.class);
		
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
	}
	
	//==== List Adapter
	private class LAdapter extends ArrayAdapter<Class> implements OnClickListener{

		private List<Class> items;

		public LAdapter(Context context, int textViewResourceId, List<Class> effects) {
			super(context, textViewResourceId, effects);
			this.items = effects;
		}

		/**
		 * This gets called whenever a new ListItem is created from the m_orders set.
		 * @param position the position of the listitem at which it is created
		 * @param convertView the View object of the ListItem.
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.item_effect, null);
			}
			//Fetch item
			Class effectClass = items.get(position);
			Effect effect;
			try{
				effect = (Effect) effectClass.newInstance();
				if(D) Log.d(TAG, "Effect instantiated");
				// fill View with info
				//Log.d("QAct","adding TU to list: "+o.toString());
				
				// TextView und ImageView
				TextView title = (TextView) v.findViewById(R.id.item_title);
				TextView description = (TextView) v.findViewById(R.id.item_description);
				
				ImageView icon = (ImageView) v.findViewById(R.id.item_icon);
				icon.setImageResource(effect.getIcon());
				// convert Time Data etc.
				
				title.setText(effect.TITLE);
				description.setText("Author: " + effect.AUTHOR);
				//icon.setImageDrawable(R.drawable);
				
				v.setTag(effectClass);
			} catch (InstantiationException e) {
				Log.e(TAG, "InstantiationException");
			} catch (IllegalAccessException e) {
				if(D) Log.e(TAG, "IllegalAccessException");
			}
//			} catch () {
//				Toast.makeText(getApplicationContext(), "Oh noes! Invalid Effect!", Toast.LENGTH_SHORT).show();
//			}
			return v;
		}

		@Override
		public void onClick(View v) {
//			Class c = (Class) v.getTag();
//			Effect e = null;
//			try {
//				e = (Effect) c.newInstance();
//				application.setEffect(e);
//				Toast.makeText(getApplicationContext(), e.TITLE + " AdapterOnClick", Toast.LENGTH_SHORT).show();
//			} catch (InstantiationException e1) {
//			} catch (IllegalAccessException e1) {
//			}
			
		}
	}
	
	
	 private class StartService extends AsyncTask<Void, Integer, Integer> {
		 private static final String TAG = "StartServiceTask";
		 @Override
	     protected Integer doInBackground(Void... args) {
			int error_code = 0;
			int progress;
			if(!application.isServiceRunning()){ //service running? > start it
				if(D) Log.d(TAG, "Starting Service...");
				progress = 8;
				publishProgress(progress);
				// Bluetooth active?
				if(!BluetoothUtils.active()) { // request popup if BT isnt activated
			        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			        startActivityForResult(enableBtIntent, 17);
			    }
				startService(new Intent(getApplicationContext(), SendService.class));
				for(int i=0;i<90 && !application.isServiceRunning();i++){
					progress++;
					publishProgress(progress);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						if(D) Log.d(TAG,"Got Interrupted...");
					}
					
				}
				publishProgress(100);
			}else{ //stop service
				progress = 100;
				publishProgress(progress);
				if(D) Log.d(TAG, "Stopping Service...");
				if(!application.isServiceRunning()) return 1;
				stopService(new Intent(getApplicationContext(), SendService.class));
				//Toast.makeText(this, "stopping Service...", Toast.LENGTH_SHORT).show();
				for(int i=0;i<95 && application.isServiceRunning();i++){
					progress--;
					publishProgress(progress);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						if(D) Log.d(TAG,"Got Interrupted...");
					}
				}
				publishProgress(0);
			}
	         
	         return error_code;
	    }
		 
		@Override
		protected void onProgressUpdate(Integer... values) {
			progress_bar.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Integer result) {
	    	 if(D) Log.d(TAG,"+onPostExecute+");
	    	 TrinityActivity.this.invalidateOptionsMenu();
	    	 //Toast.makeText(TrinityActivity.this, "Service started.", Toast.LENGTH_SHORT).show();
		} 
	 }
	
	
	
	
	
	//====================================== actual class...
	private static boolean BT_ON = false;
	
	private ListView effects_list;
	private ProjectMORPHEUS application;
	private ProgressBar progress_bar;
	
	private LAdapter effects_adapter;
	
	private static List<Class> effects;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);   
		setContentView(R.layout.main);
		
		//ActionBar ab = getActionBar();
//		getActionBar().setDisplayShowTitleEnabled(false);
		
		setEffectsInList();
		
		//find all Views
		application = (ProjectMORPHEUS) getApplication();
		progress_bar = (ProgressBar) findViewById(R.id.progbar_connect);
		progress_bar.setMax(100); // but is actually already set in layout, just be sure^^
		
		progress_bar.setInterpolator(this, android.R.anim.accelerate_decelerate_interpolator);
		//Interpolator ipol = new AnticipateInterpolator(2f);
		//progress_bar.setInterpolator(ipol);
		//---- load List and its adapter
		effects_list = (ListView) findViewById(R.id.effect_list);
		effects_list.setEmptyView((TextView)findViewById(R.id.no_effects));
		
	
		effects_adapter = new LAdapter(this, R.layout.item_effect, effects);
		
		effects_list.setAdapter(effects_adapter);
		effects_list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,long id){
				Class c = (Class) v.getTag();
				Effect e = null;
				try {
					e = (Effect) c.newInstance();
					application.setEffect(e);
					if(D) Toast.makeText(TrinityActivity.this, e.TITLE + " running", Toast.LENGTH_SHORT).show();
					if(e.hasAnActivity()){
						final Intent intent = new Intent(TrinityActivity.this,e.getActivity());
						startActivity(intent);
					}else if(e.hasOnClickOptions()){
						AlertDialog.Builder builder = new AlertDialog.Builder(TrinityActivity.this);
						DialogOptions opt = e.getOnClickDialogOptions();
						builder.setTitle(opt.title);
						builder.setItems(opt.options, new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog, int item) {
						    	application.getEffect().setOnClickOption(item);
						    }
						});
						AlertDialog alert = builder.create();
						alert.show();						
					}
				} catch (InstantiationException e1) {
					Log.e(TAG, "Caught strange InstantiationException");
				} catch (IllegalAccessException e1) {
					Log.e(TAG, "Caught strange IllegalAccessException");
				}
			}
		
		});
		
		effects_list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,int pos, long id) {
				Class c = (Class) v.getTag();
				Effect e = null;
				try {
					e = (Effect) c.newInstance();
					if(D) Toast.makeText(TrinityActivity.this, e.TITLE + " running", Toast.LENGTH_SHORT).show();
					if(e.hasOnLongClickOptions()){
						application.setEffect(e);
						AlertDialog.Builder builder = new AlertDialog.Builder(TrinityActivity.this);
						DialogOptions opt = e.getOnLongClickDialogOptions();
						builder.setTitle(opt.title);
						builder.setItems(opt.options, new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog, int item) {
						    	application.getEffect().setOnLongClickOption(item);
						    }
						});
						AlertDialog alert = builder.create();
						alert.show();	
						return true;
					}
				} catch (InstantiationException e1) {
					Log.e(TAG, "Caught strange InstantiationException");
				} catch (IllegalAccessException e1) {
					Log.e(TAG, "Caught strange IllegalAccessException");
				}
				return false;
			}
		});
   	
		// Bluetooth active?
    	if (!BluetoothUtils.active()) { // request popup if BT isnt activated
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 17);
        }
	}
	

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode==17){
			if (resultCode == RESULT_OK) {
			}else{
				if(D) Log.d(TAG, "Result NO, stop service");
            	if(!application.isServiceRunning()) if(D) Log.d(TAG, "Service not even running"); 
            	stopService(new Intent(this, SendService.class));
            	Log.d(TAG, "stopped service"); 
            	application.setServiceRunning(false); // -> onResume misses it the other way
            }
		}
	}

	
	//==== Popup
	@Override
    protected Dialog onCreateDialog(int id) {
		if(D) Log.d(TAG, "onCreateDialog");
		switch(id){
		case 0:
			if(D) Log.d(TAG, "About Dialog");
			AlertDialog Credits = new AlertDialog.Builder(this).create();
			String version;
			try {
				version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				version = "n/a";
			}
			Credits.setTitle("ProjectNEO::TRINITY " + version);
			Credits.setMessage(getResources().getText(R.string.about));
			Credits.setIcon(R.drawable.ic_app);
			Credits.setButton("Done", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
					// do nothing.
			   }
			});
			return Credits;
		case 1:
			if(D) Log.d(TAG, "About Dialog");
			AlertDialog BugRep = new AlertDialog.Builder(this).create();
			BugRep.setTitle("ProjectNEO::TRINITY");
			BugRep.setMessage(getResources().getText(R.string.bugreport));
			BugRep.setIcon(R.drawable.ic_app);
			BugRep.setButton("Done", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
					// do nothing.
			   }
			});
			return BugRep;
		}
		
		if(D) Log.e(TAG, "Unknown Dialog ID!");
		return null;
		
	}
	
	// Action bar stuff
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(D) Log.d(TAG,"onCreateOptionsMenu");
	    MenuInflater inflater = getMenuInflater(); 
	    inflater.inflate(R.menu.actionbar, menu);
	    return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		if(D) Log.d(TAG, "onPrepareOptionsMenu > updateServiceButton");
		if(application.isServiceRunning()){
			menu.getItem(0).setIcon(android.R.drawable.ic_media_pause);
			progress_bar.setProgress(100);
		}else{
			menu.getItem(0).setIcon(android.R.drawable.ic_media_play);
			progress_bar.setProgress(0);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		if(D) Log.d(TAG, "+onOptionsItemSelected+");
	    switch (item.getItemId()) {
	        case R.id.itm_service:
	            new StartService().execute();
	            return true;
	        case R.id.itm_preferences:
				if(D) Log.d(TAG, "Preferences Button Clicked");
				showDialog(1);
	            return true;
	        case R.id.itm_about:
	        	if(D) Log.d(TAG, "About Button Clicked");
				showDialog(0);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
