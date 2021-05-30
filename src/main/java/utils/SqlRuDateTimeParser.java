package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class SqlRuDateTimeParser implements DateTimeParser {
    private HashMap<String, Integer> months;

    public SqlRuDateTimeParser() {
        init();
    }

    private void init() {
        months = new HashMap<>();
        months.put("янв", 1);
        months.put("фев", 2);
        months.put("мар", 3);
        months.put("апр", 4);
        months.put("май", 5);
        months.put("июн", 6);
        months.put("июл", 7);
        months.put("авг", 8);
        months.put("сен", 9);
        months.put("окт", 10);
        months.put("ноя", 11);
        months.put("дек", 12);
    }

    @Override
    public LocalDateTime parse(String parse) {
        String[] parts = parse.split(",");
        StringBuilder dateBuilder = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
        dateBuilder.append(getDate(parts[0]).format(formatter));
        formatter = DateTimeFormatter.ofPattern("dd-MMM-yy kk:mm");
        dateBuilder.append(parts[1]);
        return LocalDateTime.parse(dateBuilder, formatter);
    }

    private LocalDate getDate(String date) {
        if (date.equals("сегодня")) {
            return LocalDate.now();
        }
        if (date.equals("вчера")) {
            return LocalDate.now().minusDays(1);
        }
        String[] parts = date.split(" ");
        int day = Integer.parseInt(parts[0]);
        int month = months.get(parts[1]);
        int year = Integer.parseInt(parts[2]) + 2000;
        return LocalDate.of(year, month, day);
    }
}
