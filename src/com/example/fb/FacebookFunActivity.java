package com.example.fb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.facebook.android.*;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.*;

public class FacebookFunActivity extends Activity {
    /** Called when the activity is first created. */
	
	Facebook facebook = new Facebook( "160529117367232" );
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        System.out.println( "testing methods with access token");
        
        mPrefs = getPreferences( MODE_PRIVATE );
        String access_token = mPrefs.getString( "access_token", null );
        long expires = mPrefs.getLong( "access_expires", 0 );
        if ( access_token != null ) {
        	facebook.setAccessToken( access_token );
        	System.out.println( "got access token " + access_token );
        }
        if ( expires != 0 ) {
        	facebook.setAccessExpires( expires );
        }
        
        if ( !facebook.isSessionValid() ) {
            facebook.authorize(this, new DialogListener() {
                @Override
                public void onComplete(Bundle values) {
                	SharedPreferences.Editor editor = mPrefs.edit();
                	editor.putString( "access_token", facebook.getAccessToken() );
                	editor.putLong( "access_expries", facebook.getAccessExpires() );
                	editor.commit();
                	
                	System.out.println( "just got new access token " + facebook.getAccessToken() );
                	
                }

                @Override
                public void onFacebookError(FacebookError error) {}

                @Override
                public void onError(DialogError e) {}

                @Override
                public void onCancel() {}
            });
        }
        
        if ( facebook != null ) {
        	AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner( facebook );
        	mAsyncRunner.request( "me", new RequestListener() {

				@Override
				public void onComplete(String response, Object state) {
					System.out.println( "just got request from facebook.. " + response );
					
				}

				@Override
				public void onIOException(IOException e, Object state) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onFileNotFoundException(FileNotFoundException e,
						Object state) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onMalformedURLException(MalformedURLException e,
						Object state) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onFacebookError(FacebookError e, Object state) {
					// TODO Auto-generated method stub
					
				}
        		
        	} );
        }
  

    }
    
    public void logout() {
    	
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
}