package com.example.jonathan.englishmuffin;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public TextView input;
    public TextView n;
    Button audio;
    public EditText voc;
    public String url = "";
    public String urlStart = "http://www.dictionaryapi.com/api/v1/references/collegiate/xml/";
    // add this:
    public String word = "";
    public String key = "key=c5b57b25-3976-4e02-8775-0222211778c2";
    public XMLParser obj;
    public String soundFile1 = "http://media.merriam-webster.com/soundc11/";
    public String wav;
    Button b1;
    Uri uri;
    Button def;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        voc = (EditText) findViewById(R.id.editText);
        b1 = (Button) findViewById(R.id.button);
        audio = (Button) findViewById(R.id.play);
        input = (TextView) findViewById(R.id.textView);
        n = (TextView) findViewById(R.id.textView2);
        def = (Button) findViewById(R.id.def);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geDefinition();
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer player = new MediaPlayer();

                createUri();
                try {
                    player.setDataSource(MainActivity.this, uri);
                    player.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                    player.start();

            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        def.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Assignment.class);
                startActivity(intent);
            }
        });
    }

    public void createUri() {
        String folder = wav.substring(0, 1);
        String soundFile = soundFile1 + folder + "/" + wav;
        uri = Uri.parse(soundFile);
    }

    public void geDefinition()
    {
        this.word = voc.getText().toString();
        url = urlStart + this.word + "?" + key;
        obj = new XMLParser(url);
        obj.fetchXML();

        LOOP:
        while (obj.parsingComplete)
        {
            input.setText(this.word);
            n.setText(obj.getDef());
            if (n.getText().toString().equals("loading...")) {
                continue LOOP;
            }
            wav = obj.getWav();
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.jonathan.englishmuffin/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.jonathan.englishmuffin/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


}
























