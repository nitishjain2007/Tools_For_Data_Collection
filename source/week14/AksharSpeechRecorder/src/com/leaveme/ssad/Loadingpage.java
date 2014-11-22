
package com.leaveme.ssad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author romilpc
 *
 */
public class Loadingpage extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);        
		setContentView(R.layout.frontpage);
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(2000);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
				finally{
					Intent openStartingPoint=new Intent("android.intent.action.M");
					startActivity(openStartingPoint);
				}
			}
		};
		timer.start();
	}
}
