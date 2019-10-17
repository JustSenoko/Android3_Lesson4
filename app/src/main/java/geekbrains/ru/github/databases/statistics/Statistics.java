package geekbrains.ru.github.databases.statistics;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

public class Statistics {
    private List<StatisticsRecord> roomStatistics = new ArrayList<>();
    private List<StatisticsRecord> sugarStatistics = new ArrayList<>();

    private List<StatisticsRecord> getStatistics(String dbName) {
        if (dbName.equals("sugar")) {
            return sugarStatistics;
        } else {
            return roomStatistics;
        }
    }

    public void addRecord(String dbName, StatisticsRecord record) {
        List<StatisticsRecord> statisticsRecordList = getStatistics(dbName);
        statisticsRecordList.add(record);
    }

    @SuppressLint("DefaultLocale")
    public String getResult(String dbName) {
        List<StatisticsRecord> statisticsRecordList = getStatistics(dbName);
        if (statisticsRecordList.size() == 0) {
            return "";
        }
        StatisticsRecord lastEntry = statisticsRecordList.get(statisticsRecordList.size() - 1);
        return String.format("количество = %d\nмилисекунд = %d\nсреднее = %d",
                lastEntry.recordsCount, lastEntry.time, getTimeAvg(statisticsRecordList));
    }

    private Long getTimeAvg(List<StatisticsRecord> statisticsRecordList) {
        int size = statisticsRecordList.size();
        if (size == 0) {
            return 0L;
        }
        Long sum = 0L;
        for (int i = 0; i < size; i++) {
            sum += statisticsRecordList.get(i).time;
        }
        return sum / size;
    }
}
