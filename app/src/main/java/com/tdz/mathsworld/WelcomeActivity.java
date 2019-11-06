package com.tdz.mathsworld;

        import android.content.Context;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import java.io.BufferedReader;
        import java.io.BufferedWriter;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStream;
        import java.io.OutputStreamWriter;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.net.URLEncoder;


public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etID, etPassword;
    public Button btnLogin;// btnLoginHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        etID = findViewById(R.id.etID);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        //btnLoginHide = findViewById(R.id.btnLoginHide);


        // btnLoginHide.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        // btnLoginHide.setEnabled(false);
    }

    @Override
    public void onClick(View view) {


        String child_mail = etID.getText().toString();
        String password = etPassword.getText().toString();
        String type = "login";
        ChildLoginCheck childLoginCheck = new ChildLoginCheck(this);
        childLoginCheck.execute(type, child_mail, password);


    }


    private class ChildLoginCheck extends AsyncTask<String,Void,String> {

        AlertDialog alertDialog;
        Context context;

        private ChildLoginCheck(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        protected String doInBackground(String... voids) {
            String type = voids[0];
            String child_login_url = "http://192.168.43.208/cts/child/login.php";

            if(type.equals("login")){
                System.out.println("I'm in doInBackground ");
                try {
                    String child_mail = voids[1];
                    String password = voids[2];
                    URL url = new URL(child_login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String post_data = URLEncoder.encode("mail","UTF-8")+"="+
                            URLEncoder.encode(child_mail,"UTF-8")+"&"+
                            URLEncoder.encode("pass","UTF-8")+"="+
                            URLEncoder.encode(password,"UTF-8");

                    System.out.println("POST_DATA " + post_data + " POST_DATA");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();

                    bufferedWriter.close(); //write is close
                    outputStream.close();  // output stream close

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

                    String result = "";
                    String line = "";

                    while((line = bufferedReader.readLine()) != null){
                        result += line;
                        System.out.println(" > "+line + " <= is LINE ");
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    System.out.println(" > "+result + " <= is RESULT");
                    return result;

                } catch (MalformedURLException e) {
                    System.out.println("URL Error  ");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("I/O Error in URL ");
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            //alertDialog.setTitle("Alert");
            System.out.println("I'm onPreExecute() ");
        }

        @Override
        protected void onPostExecute(String result) {
            //alertDialog.setMessage(result);
            //alertDialog.show();



            if(result.equals("Csuccess")) {
                //Intent i = new Intent(MainActivity.this,ResultActivity2.class);
                // i.putExtra("number",num);
                //startActivityForResult(i,10);

                //LayoutInflater inflater = LayoutInflater.from(context);
                // View yourView = inflater.inflate(R.layout.activity_main, null, false);
                // then bring it to front
                //yourView.bringToFront();


                Intent i = new Intent();
                i.setClass(getApplicationContext(), FirstActivity.class);
                i.putExtra("child_mail",etID.getText().toString());
                startActivity(i);

                // Intent intent_name = new Intent();
                //intent_name.setClass(loginChildActivity.this,MainActivity.class);
                //startActivity(intent_name);
                //Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();
                //context.startActivity(new Intent(context, MainActivity.class));


                //super.setContentView(R.layout.activity_main);

                //MainActivity.testMethod();

                //(new MainActivity()).execute();

                // btnLogin.setEnabled(false);
                //btnLoginHide.setEnabled(true);
                //loginChildActivity.setContentView(R.layout.activity_main);

            }
            else{
                Toast.makeText(getApplicationContext(),"User ID or Password Error",Toast.LENGTH_LONG).show();
            }
            //loginChildActivity.setContentView(R.layout.activity_main);
            System.out.println("I'm onPostExecute() ");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            System.out.println("I'm onProgressUpdate() ");
        }
    }
}