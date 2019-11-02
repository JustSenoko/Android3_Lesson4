package geekbrains.ru.github.tests;

import java.util.List;

import geekbrains.ru.github.retrofit.RetrofitModel;

public interface DBOperations<T> {
    List<T> getAll();
    void saveAll(List<RetrofitModel> modelList);
    void deleteAll();
}
