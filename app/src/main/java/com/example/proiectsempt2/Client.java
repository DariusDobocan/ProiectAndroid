package com.example.proiectsempt2;

import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.ThemedSpinnerAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client extends Thread {
    private String ipAddress;
    private int port;
    public String sendMsg;
    String auxMsg;
    String[] split;

    public Client(String ipAddress, int port, String sendMsg) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.sendMsg = sendMsg;

    }

    @Override
    public void run() {
            try {
                Socket clientSocket = new Socket(ipAddress, port);

                PrintWriter printWriter = Helper.getWriter(clientSocket);

                BufferedReader bufferedReader = Helper.getReader(clientSocket);

                while(true){
                    try {
                        Thread.sleep(1000);

                        if(sendMsg != null)
                            if(auxMsg != sendMsg)
                            { printWriter.println(sendMsg);

                                MapsActivity.latLong = bufferedReader.readLine();

                                split = MapsActivity.latLong.split(" ");


                                MapsActivity.x = Double.parseDouble(split[0]);
                                MapsActivity.y = Double.parseDouble(split[1]);
                                auxMsg = sendMsg;
                            }



                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}