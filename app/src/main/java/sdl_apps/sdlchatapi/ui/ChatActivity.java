package sdl_apps.sdlchatapi.ui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.bassaer.chatmessageview.model.ChatUser;
import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sdl_apps.sdlchatapi.R;
import sdl_apps.sdlchatapi.utils.Constants;

public class ChatActivity extends AppCompatActivity {
    private ChatView mChatView;
    ProgressDialog pd;
    ChatUser bot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_chat );
        String username;
        if (getIntent().hasExtra( "Username" )){
            username = getIntent().getStringExtra("Username");
        }else{
            username = "User";
        }
        createChatUsers(username);

    }

    private void createChatUsers(String username) {
        //User id
        int myId = 0;
        //User icon
        Bitmap myIcon = BitmapFactory.decodeResource(getResources(), R.drawable.face_2);
        //User name
        final ChatUser me = new ChatUser(myId, username, myIcon);

        bot = new ChatUser(1, "ChatBot", BitmapFactory.decodeResource(getResources(), R.drawable.face_1));

        mChatView = findViewById(R.id.my_chat_view);

        mChatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500));
        mChatView.setLeftBubbleColor(ContextCompat.getColor(this, R.color.blueGray500));
        mChatView.setBackgroundColor(Color.WHITE);
        mChatView.setSendButtonColor(ContextCompat.getColor(this, R.color.cyan900));
        mChatView.setSendIcon(R.drawable.ic_action_send);
        mChatView.setRightMessageTextColor(Color.WHITE);
        mChatView.setLeftMessageTextColor(Color.WHITE);
        mChatView.setUsernameTextColor(Color.BLACK);
        mChatView.setSendTimeTextColor(Color.BLACK);
        mChatView.setDateSeparatorColor(Color.BLACK);
        mChatView.setInputTextHint("new message...");
        mChatView.setMessageMarginTop(5);
        mChatView.setMessageMarginBottom(5);

        final String Token = "Bearer 2d3b646176f94c0389baf5234adba9cc";

        final String url = "https://api.api.ai/v1/query?v=20150910&lang=en&sessionId=" + generateNumber() + "&query=";

        //Click Send Button
        mChatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new message
                Message message = new Message.Builder()
                        .setUser(me)
                        .setRight(true)
                        .setText(mChatView.getInputText())
                        .hideIcon(true)
                        .build();
                //Set to chat view
                String text = mChatView.getInputText();
                mChatView.send(message);
                new JsonTask().execute(url + text, Token);
                //Reset edit text
                mChatView.setInputText("");
            }
        });

    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ChatActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty ("Authorization", params[1]);
                connection.connect();

                int status = connection.getResponseCode();
                System.out.println("status= " + status );
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);
                }
                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            try {
                JSONObject json = new JSONObject(result);
                JSONObject jsonresult = json.getJSONObject("result");
                JSONObject fulfillment = jsonresult.getJSONObject("fulfillment");
                String speech = fulfillment.getString("speech");

                if (speech != null) {
                    mChatView = findViewById(R.id.my_chat_view);
                    Message recieved_message = new Message.Builder()
                            .setUser(bot)
                            .setRight(false)
                            .setText(speech)
                            .hideIcon(true)
                            .build();

                    mChatView.send(recieved_message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public long generateNumber()
    {
        return 1234560000 + Constants.user.getPk();
    }
}
