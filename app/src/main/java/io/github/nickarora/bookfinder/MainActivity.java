package io.github.nickarora.bookfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;


public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "name";
    SharedPreferences mSharedPreferences;

    TextView mainTextView;
    protected ListView mainListView;
    protected ArrayAdapter myArrayAdapter;
    protected ArrayList mNameList = new ArrayList();
    ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mainButton = (Button) findViewById(R.id.button);
        mainButton.setOnClickListener(this);

        mainTextView = (TextView) findViewById(R.id.mainText);

        mainListView = (ListView) findViewById(R.id.main_listview);
        myArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mNameList);

        mainListView.setAdapter(myArrayAdapter);
        mainListView.setOnItemClickListener(this);

        displayWelcome();
    }

    public void displayWelcome(){
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        String name = mSharedPreferences.getString(PREF_NAME, "");
        if (name.length() > 0){
            Toast.makeText(this, "Welcome back, " + name + "!", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Hello!");
            alert.setMessage("What is your name?");

            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int whichButton) {
                    String inputName = input.getText().toString();
                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putString(PREF_NAME, inputName);
                    e.commit();

                    Toast.makeText(getApplicationContext(),
                            "Welcome, " + inputName + "!",
                            Toast.LENGTH_LONG).show();
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int whichButton) {

                }
            });

            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem shareItem = menu.findItem(R.id.menu_item_share);

        if (shareItem != null) {
            mShareActionProvider = (ShareActionProvider) shareItem.getActionProvider();
        }

        setShareIntent();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_share) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        EditText mainEditText = (EditText) findViewById(R.id.main_editText);
        mNameList.add(mainEditText.getText().toString());
        myArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d("BookFinder", "position: " + mNameList.get(position));
    }

    private void setShareIntent() {
        if (mShareActionProvider != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Development");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mainTextView.getText());
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
