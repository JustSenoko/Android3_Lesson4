package geekbrains.ru.github.databases;

import java.util.Date;

public class Statistics {
    public Long time;
    public Integer recordsCount;

    public Statistics(Date start, Date finish, Integer recordsCount) {
        this.time = finish.getTime() - start.getTime();
        this.recordsCount = recordsCount;
    }
}
