package com.star.downloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DownloaderActivity extends AppCompatActivity {

    private static final String DOMAIN = "http://www.martystepp.com/files/";

    private List<String> mLinksList;
    private ArrayAdapter<String> mArrayAdapter;

    private EditText mUrlEditText;

    private ListView mLinksListView;
    private Button mGoButton;

    private DownloaderReceiver mDownloaderReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloader);

        mUrlEditText = (EditText) findViewById(R.id.the_url);

        mLinksList = new ArrayList<>();
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mLinksList);

        mLinksListView = (ListView) findViewById(R.id.links_list);
        mLinksListView.setAdapter(mArrayAdapter);

        mLinksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String domain = mUrlEditText.getText().toString();
                String url = domain + mLinksList.get(position);

                Intent intent = new Intent(DownloaderActivity.this, DownloaderService.class);
                intent.putExtra(DownloaderService.URL, url);
                intent.setAction(DownloaderService.ACTION_DOWNLOAD);
                startService(intent);
            }
        });

        mGoButton = (Button) findViewById(R.id.go_button);
        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scanner scanner = new Scanner(getResources().openRawResource(R.raw.filelist));
                while (scanner.hasNext()) {
                    mLinksList.add(scanner.next());
                }

                mArrayAdapter.notifyDataSetChanged();
            }
        });

        mDownloaderReceiver = new DownloaderReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloaderService.ACTION_DOWNLOAD_COMPLETED);

        registerReceiver(mDownloaderReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mDownloaderReceiver);
    }

    private class DownloaderReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (action.equals(DownloaderService.ACTION_DOWNLOAD_COMPLETED)) {
                String url = intent.getStringExtra(DownloaderService.URL);
                Toast.makeText(DownloaderActivity.this, "done downloading " + url,
                        Toast.LENGTH_SHORT).show();
            }

        }
    }
}
