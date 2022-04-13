package com.example.socket_client_test_updating_loop;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.io.PrintWriter;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity
{
    Thread Thread1 = null;
    TextView displayData;
    TextView cpuModel;
    Button buttonConnect;
    String SERVER_IP;
    int SERVER_PORT;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayData = findViewById(R.id.dataText);
        buttonConnect = findViewById(R.id.connect_button);
        cpuModel = findViewById(R.id.CpuModel);

        buttonConnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SERVER_IP = "192.168.0.19";
                SERVER_PORT = 5555;
                Thread1 = new Thread(new Thread1());
                Thread1.start();
            }

        });
    }
    private PrintWriter output;
    private BufferedReader input;
    class Thread1 implements Runnable
    {
        public void run()
        {
            Socket socket;
            {
                try
                {
                    socket = new Socket(SERVER_IP, SERVER_PORT);
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    output = new PrintWriter(socket.getOutputStream());
                    new Thread (new Thread2()).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class Thread2 implements Runnable
    {
        @Override
        public void run()
        {
            String core1Temp = "NOT SET";
            String core2Temp = "NOT SET";
            String core3Temp = "NOT SET";
            String core4Temp = "NOT SET";
            String modelName = "";


            while (true)
            {
                try {
                    String dataMessage = input.readLine();
                    if (dataMessage != null) {
                        if (dataMessage.length() > 0)
                        {
                            switch (dataMessage.substring(5, 6))
                            {
                                case "0":
                                    core1Temp = dataMessage;
                                    break;
                                case "1":
                                    core2Temp = dataMessage;
                                    break;
                                case "2":
                                    core3Temp = dataMessage;
                                    break;
                                case "3":
                                    core4Temp = dataMessage;
                                    break;
                            }
                            if(dataMessage.substring(0,1).equals("I"))
                            {
                                modelName = dataMessage;
                            }
                            String finalCore1Temp = core1Temp;
                            String finalCore2Temp = core2Temp;
                            String finalCore3Temp = core3Temp;
                            String finalCore4Temp = core4Temp;
                            String finalModelName = modelName;
                            runOnUiThread(new Runnable()
                                          {
                                              @Override
                                              public void run()
                                              {
                                                  cpuModel.setText(finalModelName);
                                                  displayData.setText(finalCore1Temp + System.getProperty("line.separator")+ System.getProperty("line.separator")
                                                          + finalCore2Temp + System.getProperty("line.separator")+ System.getProperty("line.separator")
                                                          + finalCore3Temp + System.getProperty("line.separator")+ System.getProperty("line.separator")
                                                          + finalCore4Temp + System.getProperty("line.separator"));
                                              }
                                          }
                            );
                            new Thread(new Thread3()).start();
                        }
                        else
                        {
                            //Thread1 = new Thread(new Thread1());
                            //Thread1.start();
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    class Thread3 implements Runnable
    {
        @Override
        public void run()
        {
            output.write("0");
            output.flush();
        }

    }

}