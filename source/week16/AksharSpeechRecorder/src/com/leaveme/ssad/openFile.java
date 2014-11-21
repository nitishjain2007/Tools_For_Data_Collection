package com.leaveme.ssad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class openFile extends ListActivity{
	private static String OpenFileName = null;
	private int i=0;
	private MediaPlayer   mPlayer = null;
	boolean mStartPlaying = true;
	boolean mStartRecording = false;
	boolean alwaysPlay = false;
	private static int RECORDER_SAMPLERATE = 16000;
	private AudioRecord recorder = null;
	int bufferSize=0;
	BufferedReader reader=null;
	String[] line=null;
	TextView text=null;
	InputStream instream=null;
	InputStreamReader inputreader=null;
	BufferedReader buffreader=null;
	Button open=null;
	int j=0;
	int recordCheck=0;
	int playCheck=1;
	int n=0;
	int ab=0;
	int ne=0;
	private List<String> items = null;
	ArrayAdapter<String> fileList=null;
	ListView list=null;
	String UserName=null;
	public static File currentFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);        
		setContentView(R.layout.open_file); 
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
		i=pref.getInt("key",0);
		final EditText ET = (EditText) findViewById(R.id.edittext);
		open = (Button)findViewById(R.id.button1);
		open.setOnClickListener(new View.OnClickListener() {
			@Override
			
			public void onClick(View v) {
				OpenFileName=null;
				currentFile = new File("/storage/");
				UserName=ET.getText().toString().trim();
				getFiles(currentFile.listFiles());
				j=0;
				line = new String[100];
			}
		});	
	}
	private void getFiles(File[] files){
	    items = new ArrayList<String>();
	    items.add(getString(R.string.goto_root));
	    for(File file : files){
	        items.add(file.getPath());
	    }
	    fileList = new ArrayAdapter<String>(this,R.layout.file_list_row, items);
	    setListAdapter(fileList);
	}
	protected void onListItemClick(ListView list, View v, int position, long id){
	    int selectedRow = (int)id;
	    if(selectedRow == 0){
	    	if(!currentFile.getAbsolutePath().equals("/"))
	    		currentFile = currentFile.getParentFile();
	    	else
	    		currentFile = new File("/storage/");
	        getFiles(currentFile.listFiles());
	    }else{
	        currentFile = new File(items.get(selectedRow));
	        if(currentFile.isDirectory()){
	            getFiles(currentFile.listFiles());
	        }else{
	        	OpenFileName=items.get(selectedRow);
	        	StringTokenizer tokens = new StringTokenizer(OpenFileName, ".");
				String first = tokens.nextToken();
				String second = tokens.nextToken();
				if(second.equals("txt"))
					filereaderfunction();
				else{
					Toast.makeText(getApplicationContext(), "Wrong file format",
							   Toast.LENGTH_LONG).show();
				}
	        }
	    }
	}
	public void filereaderfunction(){
		if(OpenFileName != null){
			try {
				// open the file for reading
				instream = new FileInputStream(OpenFileName);

				// if file the available for reading
				if (instream != null) 
				{
					// prepare the file for reading
					inputreader = new InputStreamReader(instream);
					buffreader = new BufferedReader(inputreader);
					try {
						line[j] = buffreader.readLine();						
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					while (line[j] != null) {
						j+=1;
						line[j] = buffreader.readLine(); 
					}
					n=j;
					j=0;
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			finally{
				if(UserName != null){
					Intent openStartingPoint=new Intent("android.intent.action.M");
					openStartingPoint.putExtra("first_value", j);
					openStartingPoint.putExtra("second_value", n);
					openStartingPoint.putExtra("third_value", line);
					openStartingPoint.putExtra("forth_value", OpenFileName);
					openStartingPoint.putExtra("fifth_value", UserName);
					startActivity(openStartingPoint);
				}
				else{
					Toast.makeText(getApplicationContext(), "First enter a UserName",
							Toast.LENGTH_SHORT).show();
				}					
			}
		}
	}
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			//super.onBackPressed();
			return;
		}
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			int id = item.getItemId();
			if (id == R.id.action_samplerate) {
				final CharSequence rates[] = new CharSequence[] {"8000", "16000", "22050", "32000"};
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Pick SampleRate");
				builder.setItems(rates, new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				    	if(rates[which]=="8000"){RECORDER_SAMPLERATE =8000;}
				    	else if(rates[which]=="16000"){RECORDER_SAMPLERATE =16000;}
				    	else if(rates[which]=="22050"){RECORDER_SAMPLERATE =22050;}
				    	else if(rates[which]=="32000"){RECORDER_SAMPLERATE =32000;}
				    }
				});
				builder.show();
				return true;
			}
			if (id == R.id.action_alwaysplay){
				final CharSequence[] aPlay = {"Yes","No"};
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Always Play file Before recording next file");
				alert.setSingleChoiceItems(aPlay,-1, new 
				DialogInterface.OnClickListener()
				{
					@Override
				    public void onClick(DialogInterface dialog, int which) 
				    {
				        if(aPlay[which]=="Yes")
				        {
				            alwaysPlay=true;
				        }
				        else if (aPlay[which]=="No")
				        {
				            alwaysPlay=false;
				        }
				    }
				});
				alert.show();
			}
			return super.onOptionsItemSelected(item);
		}
		@Override
		protected void onStop(){
			super.onStop();
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE); 
			Editor editor = pref.edit();
			editor.putInt("key", i);
			editor.commit();
		}

		@Override
		public void onPause() {
			super.onPause();
			if (recorder != null) {
				recorder.release();
				recorder = null;
			}

			if (mPlayer != null) {
				mPlayer.release();
				mPlayer = null;
			}
		}
}
