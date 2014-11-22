package com.leaveme.ssad;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.app.ListActivity;
import android.media.MediaPlayer.OnCompletionListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.leaveme.ssad.R;


/**
 * @author romil
 *
 */
public class MainActivity extends ListActivity {
	private static final String LOG_TAG = "AudioRecordTest";
	private static String mFileName = null;
	private static String tFileName = null;
	private static String OpenFileName = null;
	private int i=0;
	private MediaPlayer   mPlayer = null;
	boolean mStartPlaying = true;
	boolean mStartRecording = false;
	boolean alwaysPlay = false;
	private static int RECORDER_SAMPLERATE = 16000;
	private static final int RECORDER_BPP = 16; //bits per sample
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	private AudioRecord recorder = null;
	private Thread recordingThread = null;
	int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
	int BytesPerElement = 2; // 2 bytes in 16bit format
	int bufferSize=0;
	BufferedReader reader;
	String[] line;
	TextView text;
	InputStream instream;
	InputStreamReader inputreader;
	BufferedReader buffreader;
	ImageView record;
	ImageView play;
	Button open;
	int j=0;
	int recordCheck=0;
	int playCheck=1;
	int n=0;
	int ab;
	private List<String> items = null;
	ArrayAdapter<String> fileList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);        
		setContentView(R.layout.activity_main); 
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
		i=pref.getInt("key",0);
		record = (ImageView)findViewById(R.id.button2);
		record.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mStartPlaying){
					if(!alwaysPlay || playCheck==1 || mStartRecording ){
						mStartRecording=!mStartRecording;
						if(mStartRecording){
							recordCheck=1;
							if(alwaysPlay)
								playCheck=0;
							/*
							mRecorder = new MediaRecorder();
							mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
							mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
							AudioRecordTest();
							mRecorder.setOutputFile(mFileName);
							mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	
							try {
								mRecorder.prepare();
							} 
							catch (IOException e) {
								Log.e(LOG_TAG, "prepare() failed");
							}
							mRecorder.start();	
							record.setImageResource(R.drawable.stop);
							mStartRecording=!mStartRecording;
							*/
							bufferSize = AudioRecord.getMinBufferSize(
									RECORDER_SAMPLERATE,
									RECORDER_CHANNELS,
									RECORDER_AUDIO_ENCODING); 
							recorder = new AudioRecord(
									MediaRecorder.AudioSource.MIC,
									RECORDER_SAMPLERATE,
									RECORDER_CHANNELS,
									RECORDER_AUDIO_ENCODING,
									bufferSize*BytesPerElement);
							record.setImageResource(R.drawable.stop);
							int vb = recorder.getState();
			                if(vb==1){
			                	recorder.startRecording();
							recordingThread = new Thread(new Runnable() {
								@Override
						        public void run() {
						            writeAudioDataToFile();
						        }
						    }, "AudioRecorder Thread");	
						    recordingThread.start();
			                }
						}
						else{
							if (null != recorder) {
								recorder.stop();
								recorder.release();
								recorder = null;
								recordingThread=null;
								copyWaveFile();
								File q = new File(tFileName);
								q.delete();
								record.setImageResource(R.drawable.record_button);
							}
						}
					}
				}
			}
		});

		play = (ImageView)findViewById(R.id.button5);
		play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!mStartRecording && recordCheck==1){
					if(mStartPlaying){
						playCheck=1;
						mPlayer = new MediaPlayer();
						try {
							mStartPlaying=!mStartPlaying;
							mPlayer.setDataSource(mFileName);
							mPlayer.prepare();
							mPlayer.start();
							play.setImageResource(R.drawable.stop);
							mPlayer.setOnCompletionListener(new OnCompletionListener() {

					            public void onCompletion(MediaPlayer mPlayer) {
					            	stopPlaying();
					            }
					        });
						} catch (IOException e) {
							Log.e(LOG_TAG, "prepare() failed");
						}
						
						/*File file12 = new File(mFileName);
						  
				        int shortSizeInBytes = Short.SIZE/Byte.SIZE;
				  
				        int bufferSizeInBytes = (int)(file12.length()/shortSizeInBytes);
				        short[] audioData = new short[bufferSizeInBytes];
				  
				        try {
				        	InputStream inputS = new FileInputStream(file12);
				        	BufferedInputStream bufferedInputS = new BufferedInputStream(inputS);
				        	DataInputStream dataInputS = new DataInputStream(bufferedInputS);
				   
				        	int ee = 0;
				        	while(dataInputS.available() > 0){
				        		audioData[ee] = dataInputS.readShort();
				        		ee++;
				        	}
				   
				        	dataInputS.close();
				   
				        	AudioTrack audioTrack = new AudioTrack(
				        			AudioManager.STREAM_MUSIC,
				        			RECORDER_SAMPLERATE,
				        			AudioFormat.CHANNEL_OUT_MONO,
				        			AudioFormat.ENCODING_PCM_16BIT,
				        			bufferSize,
				        			AudioTrack.MODE_STREAM);
				        	play.setImageResource(R.drawable.stop);
				        	audioTrack.play();
				        	audioTrack.write(audioData, 0, bufferSize);
				        	stopPlaying();
				   
				        } 
				        catch (FileNotFoundException e) 
				        {
				        	e.printStackTrace();
				        } 
				        catch (IOException e) 
				        {
				        	e.printStackTrace();
				        }
					*/
						
					}
					else{
						stopPlaying();
					}
				}
			}
		});
		open = (Button)findViewById(R.id.button1);
		open.setOnClickListener(new View.OnClickListener() {
			@Override
			
			public void onClick(View v) {
				OpenFileName=null;
				getFiles(new File("/storage/").listFiles());
				text = (TextView) findViewById(R.id.fileArea);
				j=0;
				line = new String[100];
			}
		});	
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
	
		 
	public void AudioRecordTest() {
		File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SSAD19");
		boolean success = true;

		if (!folder.exists()) {
			i=0;
			success = folder.mkdir();
			if(success){
				mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
				mFileName +="/SSAD19/" +i+ "audiorecordtest.wav";
				i+=1;
			}
			else{
				mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
				mFileName +="/" +i+ "audiorecordtest.wav";
				i+=1;
			}
		}
		else{
			mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
			mFileName +="/SSAD19/" +i+ "audiorecordtest.wav";
			i+=1;
		}
	}
	public void AudioRecordtemp() {
		File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SSAD19");
		boolean success = true;

		if (!folder.exists()) {
			success = folder.mkdir();
			if(success){
				tFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
				tFileName +="/SSAD19/audiorecordtest.raw";
			}
			else{
				tFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
				tFileName +="/audiorecordtest.raw";
			}
		}
		else{
			tFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
			tFileName +="/SSAD19/audiorecordtest.raw";
		}
	}
	

	private void writeAudioDataToFile() {
	    // Write the output audio in byte
		AudioRecordtemp();		
		AudioRecordTest();
	    short data[] = new short[bufferSize];

	    FileOutputStream os = null;
	    try {
	        os = new FileOutputStream(tFileName);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    
	    if(null!=os){
	    while (mStartRecording) {
	        // gets the voice output from microphone to byte format
	        ab=recorder.read(data, 0, bufferSize);	
	        if(AudioRecord.ERROR_INVALID_OPERATION != ab){
                try {
                	 byte bData[] = short2byte(data);
                        os.write(bData,0,bufferSize*BytesPerElement);
                	} 
                catch (IOException e) {
                        e.printStackTrace();
                }
	        }
	    }

	    try {
	    	os.close();
	    } 
	    catch (IOException e) {
	    	e.printStackTrace();
	    }
	    }
	}
	private byte[] short2byte(short[] sData) {
	    int shortArrsize = sData.length;
	    byte[] bytes = new byte[shortArrsize * 2];
	    for (int i = 0; i < shortArrsize; i++) {
	        bytes[i * 2] = (byte) (sData[i] & 0x00FF);
	        bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
	        sData[i] = 0;
	    }
	    return bytes;

	}
	void stopPlaying(){
		 mPlayer.release();
		 mPlayer=null;
		 play.setImageResource(R.drawable.play_button);
		 mStartPlaying=!mStartPlaying;
	}
	private void copyWaveFile(){
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = RECORDER_SAMPLERATE;
        int channels = 1; // use 2 if doesn't work
        long byteRate = (RECORDER_BPP * RECORDER_SAMPLERATE * channels)/8;
        
        byte[] data = new byte[bufferSize];
        
        try {
                in = new FileInputStream(tFileName);
                out = new FileOutputStream(mFileName);
                totalAudioLen = in.getChannel().size();
                totalDataLen = totalAudioLen + 36;
                
                //AppLog.logString("File size: " + totalDataLen);
                
                WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                                longSampleRate, channels, byteRate);
                
                while(in.read(data) != -1){
                        out.write(data);
                }
                
                in.close();
                out.close();
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
}

private void WriteWaveFileHeader(
                FileOutputStream out, long totalAudioLen,
                long totalDataLen, long longSampleRate, int channels,
                long byteRate) throws IOException {
        
        byte[] header = new byte[44];
        
        header[0] = 'R';  // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';  // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;  // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8);  // block align
        header[33] = 0;
        header[34] = RECORDER_BPP;  // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
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
					text.setText(line[0]);
				}
				catch (Exception e) {
					text.setText(e.getMessage());
				}
				while (line[j] != null) {
					j+=1;
					line[j] = buffreader.readLine(); 
				}
				n=j;
				j=0;

				Button next = (Button) findViewById(R.id.button6);
				next.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(j<n-1){
							j+=1;
							text.setText(line[j]);
						}	        			
					}
				});
				Button previous = (Button) findViewById(R.id.button4);
				previous.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {			
						if(j>0){
							j=j-1;
							text.setText(line[j]);
						}
					}
				});


			}
			else{
				text.setText("file can not be opened");
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
protected void onListItemClick(ListView l, View v, int position, long id){
    int selectedRow = (int)id;
    if(selectedRow == 0){
        getFiles(new File("/storage/").listFiles());
    }else{
        File file = new File(items.get(selectedRow));
        if(file.isDirectory()){
            getFiles(file.listFiles());
        }else{
        	OpenFileName=items.get(selectedRow);
        	filereaderfunction();
        }
    }
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
	@Override
	/*public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		return;
	}*/
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
 