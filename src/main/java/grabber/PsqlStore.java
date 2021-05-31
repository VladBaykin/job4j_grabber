package grabber;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {
    private Connection connection;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            connection = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void save(Post post) {
        String sqlInsert = "insert into post(name, text, link, created) values (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getLink());
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> result = new ArrayList<>();
        String sqlSelect = "select * from post";
        try (PreparedStatement ps = connection.prepareStatement(sqlSelect)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Post post = new Post(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("text"),
                        rs.getString("link"),
                        rs.getTimestamp("created").toLocalDateTime()
                );
                result.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Post findById(String id) {
        Post resultPost = null;
        String sqlSelect = "select * from post where id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlSelect)) {
            ps.setInt(1, Integer.parseInt(id));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultPost = new Post();
                resultPost.setId(rs.getInt("id"));
                resultPost.setName(rs.getString("name"));
                resultPost.setDescription(rs.getString("text"));
                resultPost.setLink(rs.getString("link"));
                resultPost.setCreated(rs.getTimestamp("created").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultPost;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) {
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("grabber.properties")) {
            Properties prop = new Properties();
            prop.load(in);
            PsqlStore psqlStore = new PsqlStore(prop);
            Post postToAdd = new Post();
            postToAdd.setName("java");
            postToAdd.setDescription("description");
            postToAdd.setLink("https://test.test");
            postToAdd.setCreated(LocalDateTime.now());
            psqlStore.save(postToAdd);
            List<Post> posts = psqlStore.getAll();
            System.out.println(posts);
            Post postToFind = psqlStore.findById("2");
            System.out.println(postToFind);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
