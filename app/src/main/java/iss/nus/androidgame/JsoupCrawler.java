package iss.nus.androidgame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

// Using Jsoup library
public final class JsoupCrawler {

    public static ArrayList<String> crawImageUrl(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        ArrayList<String> imagesList = new ArrayList<>();
        Elements images = doc.getElementsByTag("img");
        for(Element element: images) {
            String src = element.attr("src");
            imagesList.add(src);
        }

        return imagesList;
    }
}
