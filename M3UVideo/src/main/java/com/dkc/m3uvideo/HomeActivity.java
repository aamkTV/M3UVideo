package com.dkc.m3uvideo;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class HomeActivity extends ListActivity {
    public static final  String PLAYLIST_URI="PLAYLIST_URI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getListView().setFastScrollEnabled(true);
        getListView().setItemsCanFocus(true);

        handlePlaylistIntent();
    }

    private void handlePlaylistIntent() {
        Uri dataUri = getIntent().getData();
        if (dataUri != null) {
            SettingsUtil.setOption(getApplicationContext(), PLAYLIST_URI, dataUri.getPath());
            loadPlaylist(dataUri.getPath());
        }
        else{
            loadPlaylist(SettingsUtil.getStringOption(getApplicationContext(),PLAYLIST_URI,""));
        }
    }

    private void loadPlaylist(String path) {
        if(path!=null&&path.length()>0){
            ArrayList<VideoStream> videos =  new M3UParser().parse(path);
            displayPlaylist(videos);

        }
    }

    protected void displayPlaylist(ArrayList<VideoStream> videos){
        StreamsAdapter mAdapter = new StreamsAdapter(this,R.layout.list_item,videos);
        setListAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_openfile:
                selectPlaylist();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectPlaylist() {
        FileDialog fileDialog = new FileDialog(this, Environment.getExternalStorageDirectory());
        fileDialog.setFileEndsWith(".m3u");
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
                SettingsUtil.setOption(getApplicationContext(), PLAYLIST_URI, file.getAbsolutePath());
                loadPlaylist(file.getAbsolutePath());
                Log.d(getClass().getName(), "selected file " + file.toString());
            }
        });
        fileDialog.showDialog();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        VideoStream vid = (VideoStream) getListAdapter().getItem(position);

        playVideo(vid);

    }

    private void playVideo(VideoStream vid) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        Uri videoUri =Uri.parse(vid.getPath());
        intent.setDataAndType(videoUri, vid.getMime_type());

        //problems with titles in DLNA
        if(!vid.getMime_type().contains("vnd.dlna")){
            String title=vid.getTitle();
            if(title!=null&&title.length()>0){
                intent.putExtra("title",title);
                intent.putExtra("displayName",title);
                intent.putExtra("forcename",title);
            }
        }
        if(!startActivityWCheck(intent)){

            //start activity without mime-type
            intent.setData(videoUri);
            if(!startActivityWCheck(intent)){
                showNoPlayerFoundMsg();
            }
        }
    }

    private void showNoPlayerFoundMsg() {
        Toast toast = Toast.makeText(this, R.string.no_players_associated, Toast.LENGTH_LONG);
        toast.show();
    }

    private boolean startActivityWCheck(Intent intent){
        try{
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
                return true;
            }
        }
        catch (Exception ex){
            return false;
        }
        return false;
    }
}
