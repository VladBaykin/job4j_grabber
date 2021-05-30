package html;

import grabber.Post;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.util.List;

public class SqlRuParse {
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

    public static Post load(String url) throws IOException {
        Post post = new Post();
        post.setLink(url);
        Document doc = Jsoup.connect(url).get();
        List<TextNode> name = doc.select(".messageHeader").get(0).textNodes();
        post.setName(name.get(0).text().trim());
        Elements desc = doc.select(".msgBody");
        post.setDescription(desc.get(1).text());
        Elements date = doc.select(".msgFooter");
        String fromFooter = date.get(0).text();
        post.setCreated(new SqlRuDateTimeParser().parse(fromFooter.substring(0, fromFooter.indexOf("[") - 1)));
        return post;
    }
}
