package quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
    private Properties config;

    public AlertRabbit(Properties config) {
        this.config = config;
    }

    public static void main(String[] args) {
        AlertRabbit alertRabbit = new AlertRabbit(getProperties("rabbit.properties"));
        try (Connection cn = alertRabbit.init(alertRabbit.config)) {
            alertRabbit.executeScheduler(cn, alertRabbit.config);
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    private static Properties getProperties(String path) {
        Properties config = new Properties();
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream(path)) {
            config.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    private Connection init(Properties config) throws SQLException, ClassNotFoundException {
        Class.forName(config.getProperty("driver-class-name"));
        return DriverManager.getConnection(
                config.getProperty("url"),
                config.getProperty("username"),
                config.getProperty("password")
        );
    }

    public void executeScheduler(Connection connection, Properties properties)
            throws SchedulerException, InterruptedException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        scheduler.scheduleJob(getJob(connection), getTrigger(properties));
        Thread.sleep(10000);
        scheduler.shutdown();
    }

    private JobDetail getJob(Connection connection) {
        JobDataMap data = new JobDataMap();
        data.put("store", connection);
        return newJob(Rabbit.class)
                .usingJobData(data)
                .build();
    }

    private Trigger getTrigger(Properties config) {
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(config.getProperty("rabbit.interval")))
                .repeatForever();
        return newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
            Connection cn = (Connection) context.getJobDetail().getJobDataMap().get("store");
            try (PreparedStatement statement =
                         cn.prepareStatement("insert into rabbit(created_date) values (?)")) {
                statement.setLong(1,  System.currentTimeMillis());
                statement.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
