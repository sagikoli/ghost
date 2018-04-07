/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private FastDictionary fastDictionary;
    private TextView ghostText,gameStatus;
    private String fragment="";
    Button challenge,restart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try{
            InputStream inputStream = assetManager.open("words.txt");
            fastDictionary=new FastDictionary(inputStream);
            }
        catch (IOException e){
            Toast.makeText(this,"Something went Wrong",Toast.LENGTH_SHORT).show();
        }
        onStart(null);
    }
    public void onRestart(View v)
    {
                onStart(null);
    }
   public void onChallenge(View v){
        String challengeStr=ghostText.getText().toString();
        if(challengeStr.length()>=4 && fastDictionary.isWord(challengeStr))
        {
            challengeStr=challengeStr+" is valid word";
            ghostText.setText(challengeStr);
            gameStatus.setText("You win");
            onRestart();
        }
        else {
            gameStatus.setText("Computer wins \n The suitable word can be: "+challengeStr);
            onRestart();
        }
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

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        challenge=(Button)findViewById(R.id.challenge);
        restart=(Button)findViewById(R.id.restart);
        ghostText=(TextView)findViewById(R.id.ghostText);
        gameStatus=(TextView)findViewById(R.id.gameStatus);
        ghostText.setText("");
        fragment=ghostText.getText().toString();
        if (userTurn) {
            gameStatus.setText(USER_TURN);
        } else {
            gameStatus.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
       // TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
      String compStr=ghostText.getText().toString();
      if(compStr.length()>=4 && fastDictionary.isWord(compStr))
      {
          gameStatus.setText("You Lost");
          onRestart();
      }
      String possibleWord=fastDictionary.getAnyWordStartingWith(compStr);
      if(possibleWord==null)
      {
                  gameStatus.setText("NO such Word\nYou Lost");
                  onRestart();
      }
      else {
          if(fragment.equals(possibleWord)){
              gameStatus.setText("You Lost");
                      onRestart();
          }
          else {
              fragment=fragment+possibleWord.substring(fragment.length(),fragment.length()+1);
              ghostText.setText(fragment);
              userTurn=true;
              gameStatus.setText(USER_TURN);
          }
      }
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
       char c=(char)event.getUnicodeChar();
       c=Character.toLowerCase(c);
       if(event.getUnicodeChar()<97 && event.getUnicodeChar()>122)
       {
           gameStatus.setText("Invalid Key");
           return super.onKeyUp(keyCode, event);
       }
       else {
           gameStatus.setText("Valid Key");
           fragment=fragment+(char)event.getUnicodeChar();
           ghostText.setText(fragment);
           userTurn=false;
           gameStatus.setText(COMPUTER_TURN);
                   computerTurn();
           return true;
       }
    }
}
