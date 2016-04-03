package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Override;
import java.lang.String;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private TextView wordFragment;
    private TextView gameStatus;
    InputStream input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        wordFragment = (TextView) findViewById(R.id.ghostText);
        gameStatus = (TextView) findViewById(R.id.gameStatus);

        try {
            input = getAssets().open("words.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dictionary = new SimpleDictionary(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        onStart(null);
    }

    @Override
    public boolean onKeyUp(int keyCode,KeyEvent event)
    {
        String word = null;

        word = wordFragment.getText()+""+((char)event.getUnicodeChar());
        wordFragment.setText(word);

        if(dictionary.isWord(word))
        {
            gameStatus.setText("Valid word");
        }
        return super.onKeyUp(keyCode,event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        String word=(String) wordFragment.getText();
        String anyWord=null;
        if (dictionary.isWord(word))
        {
            gameStatus.setText("Valid word! Computer won!");
        }
        else {
            anyWord=dictionary.getAnyWordStartingWith(word);
        }
        if(anyWord == null){
            onChallenge(null);
        }
        else
        {
            int len = word.length();
            String s = anyWord.substring(len, len +1);
            wordFragment.setText(word + s);
        }
        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        }
        else
        {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
    public void onChallenge(View view) {
        if (!userTurn)
            declareResult(userTurn);
        else {
            String word = (String) wordFragment.getText();
            if (dictionary.isWord(word))
                declareResult(userTurn);
            else if (dictionary.getAnyWordStartingWith(word) == null) {
                declareResult(userTurn);
            }
            else     declareResult(!userTurn);
        }
    }

    private void declareResult(boolean userTurn)
    {
        if(userTurn)
            gameStatus.setText("User Won!");
        else
            gameStatus.setText("Computer Won!");
    }
}
