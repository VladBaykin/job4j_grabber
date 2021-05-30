package html;

import grabber.Post;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.*;

public class SqlRuParseTest {

    @Test
    public void load() {
        SqlRuParse parse = new SqlRuParse();
        String url =
                "https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t";
        Post post = parse.detail(url);
        assertEquals(LocalDateTime.of(2020, Month.MAY, 13, 21, 58, 0),
                post.getCreated());
        assertEquals("Лиды BE/FE/senior cистемные аналитики/QA и DevOps, Москва, до 200т.", post.getName());
        assertEquals(url, post.getLink());
    }
}