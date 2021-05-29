package html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SqlParse {
    public static void main(String[] args) throws Exception {
        String url = "https://www.sql.ru/forum/job-offers";
        for (int i = 1; i < 6; i++) {
            parsePage(url + "/" + i);
        }
    }

    public static void parsePage(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            Element date = td.parent().child(5);
            System.out.println(date.text());
        }
    }
}
