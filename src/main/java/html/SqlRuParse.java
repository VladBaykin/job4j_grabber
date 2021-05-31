package html;

import grabber.Parse;
import grabber.Post;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import utils.DateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
    private final DateTimeParser parser;

    public SqlRuParse(DateTimeParser parser) {
        this.parser = parser;
    }

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

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(link).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td: row) {
                Element href = td.child(0);
                String attrUrl = href.attr("href");
                posts.add(detail(attrUrl));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post detail(String url) {
        Post post = new Post();
        post.setLink(url);
        try {
            Document doc = Jsoup.connect(url).get();
            List<TextNode> name = doc.select(".messageHeader").get(0).textNodes();
            post.setName(name.get(0).text().trim());
            Elements desc = doc.select(".msgBody");
            post.setDescription(desc.get(1).text());
            Elements date = doc.select(".msgFooter");
            String fromFooter = date.get(0).text();
            post.setCreated(parser.parse(fromFooter.substring(0, fromFooter.indexOf("[") - 1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }
}
