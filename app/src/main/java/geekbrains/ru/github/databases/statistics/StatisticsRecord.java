package geekbrains.ru.github.databases.statistics;

import java.util.Date;
import java.util.Locale;

public class StatisticsRecord {
    Long time;
    Integer recordsCount;

    public StatisticsRecord(Date start, Date finish, Integer recordsCount) {
        this.time = finish.getTime() - start.getTime();
        this.recordsCount = recordsCount;
    }

    public Long getTime() {
        return time;
    }

    public Integer getRecordsCount() {
        return recordsCount;
    }

    public String getResult() {
        return String.format(Locale.getDefault(), "Количество = %d\n милисекунд = %d", recordsCount, time);
    }
}
