package com.speechtotext;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.ArrayList;

/**
 * Created by prayuditb on 4/10/17.
 */

public class STTmodule extends ReactContextBaseJavaModule implements ActivityEventListener{

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private ReactApplicationContext mReactContext;
    private Promise mPromise;
    private Activity mCurrentActivity;

    public STTmodule (ReactApplicationContext reactContext){
        super(reactContext);
        reactContext.addActivityEventListener(this);
        mReactContext = reactContext;
    }

    @Override
    public String getName(){
        return "SpeechToText";
    }

    @ReactMethod
    public void start(Promise promise){
        openSpeechToText();
        mPromise = promise;
    }

    private void openSpeechToText(){
         mCurrentActivity = getCurrentActivity();

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");

        mCurrentActivity.startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        {
            Toast.makeText(mReactContext, R.string.SPEECH_NOT_SUPPORT,
                    Toast.LENGTH_SHORT).show();
            mPromise.reject("100", "Sorry! Your device doesn\\'t support speech input");
        }
    }


    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        ArrayList<String> result = data
                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        mPromise.resolve(result.get(0));
    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
