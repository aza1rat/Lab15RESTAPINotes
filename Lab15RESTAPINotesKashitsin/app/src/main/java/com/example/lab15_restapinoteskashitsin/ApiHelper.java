package com.example.lab15_restapinoteskashitsin;

import android.app.Activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHelper
{
    Activity ctx;

    public ApiHelper(Activity ctx)
    {
        this.ctx = ctx;
    }

    public void on_ready(String res)
    {

    }

    public void on_fail()//Кашицын,393
    {
    }

    String http_get(String req, String payload) throws IOException
    {
        URL url = new URL(req);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        byte[] outmsg = payload.getBytes("utf-8");

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Content-Length", String.valueOf(outmsg.length));
        con.setDoOutput(true);
        con.setDoInput(true);

        BufferedOutputStream out = new BufferedOutputStream(con.getOutputStream());
        out.write(outmsg);
        out.close();

        BufferedInputStream inp = new BufferedInputStream(con.getInputStream());

        byte[] buf = new byte[512];
        String res = "";

        while (true)
        {
            int num = inp.read(buf);
            if (num < 0) break;

            res += new String(buf, 0, num);
        }

        con.disconnect();

        return res;
    }

    public class NetOp implements Runnable //Кашицын,393
    {
        public String req, payload;

        public void run()
        {
            try
            {
                final String res = http_get(req, payload);

                ctx.runOnUiThread(() -> {on_ready(res);});
            }
            catch (Exception ex)
            {
                on_fail();
            }
        }
    }

    public class NetOpWithStop implements Runnable
    {
        public String req, payload;

        public void run()
        {
            try
            {
                final String res = http_get(req, payload);
                on_ready(res);
            }
            catch (Exception ex)
            {
                on_fail();
            }
        }
    }

    public void send(String req, String payload)//Кашицын,393
    {
        NetOp nop = new NetOp();
        nop.req = req;
        nop.payload = payload;

        Thread th = new Thread(nop);
        th.start();

    }

    public void sendWithStop(String req, String payload)
    {
        NetOpWithStop nop = new NetOpWithStop();
        nop.req = req;
        nop.payload = payload;

        Thread th2 = new Thread(nop);
        th2.start();
    }



}