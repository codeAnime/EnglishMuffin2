package com.example.jonathan.englishmuffin;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;

public class Assignment extends MainActivity {
    //new vars
    public MainActivity assign = new MainActivity();
    TextView displayWord;
    TextView displayDefinition;
    Button getDef;
    Button getVocals;

    //recycled vars
    public XMLParser obj;
    String wav;                                 //will get the wav file for sounds
    Uri uri;
    //private GoogleApiClient client;             //Google client

    ArrayList<String> wordList = new ArrayList<String>();
    LayoutInflater inflater=null;

    ArrayList<String> list ;
    ListView listView;

    protected void onCreate(Bundle savedInstanceState2) {
        super.onCreate(savedInstanceState2);
        setContentView(R.layout.assignment_list);

        displayWord = (TextView) findViewById(R.id.display);
        displayDefinition = (TextView)findViewById(R.id.Definition);
        getDef = (Button) findViewById(R.id.def);
        getVocals = (Button) findViewById(R.id.vocals);

        //displayDefinition.setMovementMethod(new ScrollingMovementMethod());
        String[] wordList = {"cake","obsolete","dog","hand","cook","draw",
                "remote","televsision","electricity","wonderful","computer"};

        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(new MyListAdapter(Assignment.this, wordList, new Button(this)));

    }//end of onCreate

    public void geDefinition(TextView word, TextView def)
    {
        String word1 = word.getText().toString();
        url = urlStart + word1 + "?" + key;
        obj = new XMLParser(url);
        obj.fetchXML();

        LOOP:    while (obj.parsingComplete)
        {
            input.setText(word1);
            def.setText(obj.getDef());
            if (def.getText().toString().equals("loading..."))
            {
                continue LOOP;
            }
            wav = obj.getWav();
        }
    }//end of getDefinition

    public void getSound(Uri uri)
    {
        MediaPlayer player = new MediaPlayer();

        //createUri();
        try {
            player.setDataSource(Assignment.this, uri);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();
    }

    public void myClickHandler(View v){
        ListView lvItems = (ListView) findViewById(R.id.listView);
        for (int i=0; i<lvItems.getChildCount(); i++)
        {
            lvItems.getChildAt(i).setBackgroundColor(Color.BLUE);
        }
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent();

        TextView child = (TextView)vwParentRow.getChildAt(2);
        Button btnChild = (Button)vwParentRow.getChildAt(0);
        btnChild.setText(child.getText());
        btnChild.setText("I've been clicked!");

        int c = Color.CYAN;

        vwParentRow.setBackgroundColor(c);
        vwParentRow.refreshDrawableState();
    }

    public class BaseCell{
        TextView word;
        TextView def;
        Button getDef;
        Button getSound;
    public BaseCell()
    {}

    public BaseCell(Context c, String word)
    {
        this.word = new TextView(c);
        this.getDef = new Button(c);
        this.getSound = new Button(c);
    }
}

    public class MyListAdapter extends BaseAdapter{
        ArrayList<BaseCell> list;
        Context context;
        String[] wordList;
        Button getDef;

        MyListAdapter(Context c, String words[], Button button){
            this.context = c;
            list = new ArrayList<BaseCell>();
            Resources res = c.getResources();
            Uri uri;

            this.wordList = words;
            this.getDef=button;

            for(int i =0; i < wordList.length; i++)
            {
               list.add(new BaseCell(Assignment.this, wordList[i]));
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.singlerow, viewGroup, false);

            final TextView word = (TextView) row.findViewById(R.id.display);
            final TextView def = (TextView)row.findViewById(R.id.Definition);
            Button getDef = (Button)row.findViewById(R.id.def);
            Button getSound=(Button)row.findViewById(R.id.vocals);
            uri = createUri();

            word.setText(wordList[i].substring(0,1).toUpperCase() + wordList[i].substring(1));
            def.setText("Definition: ");


            getDef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    geDefinition(word, def);
                }
            });
            getSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSound(uri);
                }
            });

            return row;
        }
        public Uri createUri() {
            try {
                String folder = wav.substring(0, 1);
                String soundFile = soundFile1 + folder + "/" + wav;
                uri = Uri.parse(soundFile);
            }catch(NullPointerException e){
                e.getMessage();
            }return uri;
        }
    }

}
