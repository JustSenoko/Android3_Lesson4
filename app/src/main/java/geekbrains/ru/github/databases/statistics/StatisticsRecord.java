package geekbrains.ru.github.databases.statistics;

import java.util.Date;

public class StatisticsRecord {
    Long time;
    Integer recordsCount;

    public StatisticsRecord(Date start, Date finish, Integer recordsCount) {
        this.time = finish.getTime() - start.getTime();
        this.recordsCount = recordsCount;
    }
}
