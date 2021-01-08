package iss.nus.androidgame;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static java.lang.String.valueOf;

// Using Jsoup library
public final class JsoupCrawler extends Service {

    private Thread myThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        String action = intent.getAction();
        System.out.println("hello from jsoup");
        if (action.compareToIgnoreCase("download") == 0)
        {
            myThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = intent.getStringExtra("URL");
                    try
                    {
                        crawImageUrl(url);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            myThread.start();
        }

        return START_NOT_STICKY;
    }

    public ArrayList<Drawable> crawImageUrl(String url) throws IOException, InterruptedException {
        Document doc = Jsoup.connect(url).get();
        ArrayList<String> imagesList = new ArrayList<>();
        Elements images = doc.getElementsByTag("img");
        for(Element element: images) {
            String src = element.attr("src");
            int srcLength = src.length();
            String filetype = src.substring(srcLength - 3);
            if (filetype.equals("svg") == false) {
                imagesList.add(src);
            }
        }

        ArrayList<Drawable> drawableList = new ArrayList<>();
        System.out.println("before dwltoSave");

        Intent intent1 = new Intent();
        intent1.setAction("DOWNLOAD_START");
        sendBroadcast(intent1);

        int n = Math.min(images.size(), 20);

        for (int i = 0; i < n; i++)
        {
            System.out.println(imagesList.get(i));
            downloadToSave(imagesList.get(i), "pic" + valueOf(i));

//            Thread.sleep(1000);

            Intent intent2 = new Intent();
            intent2.setAction("DOWNLOAD_ONGOING");
            intent2.putExtra("Download Progress", i);
            intent2.putExtra("picName", "pic" + valueOf(i));
            sendBroadcast(intent2);
        }

        Intent intent3 = new Intent();
        intent3.setAction("DOWNLOAD_COMPLETE");
        sendBroadcast(intent3);

        //Maybe can return list of ids instead
        return drawableList;
    }

    public boolean downloadToSave(String where, String filename) {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(dir, filename);

        try {
            URL url = new URL(where);
            URLConnection conn = url.openConnection();

            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(file);

            byte[] buf = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = in.read(buf)) != -1)
                out.write(buf, 0, bytesRead);

            out.close();
            in.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        myThread.interrupt();

        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(JsoupCrawler.this,
                        "Download ended",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
