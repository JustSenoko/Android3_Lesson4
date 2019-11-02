package geekbrains.ru.github.dagger;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
@interface GetRoom {}

@Qualifier
@Retention(RUNTIME)
@interface SaveRoom {}

@Qualifier
@Retention(RUNTIME)
@interface DeleteRoom {}

@Qualifier
@Retention(RUNTIME)
@interface GetSugar {}

@Qualifier
@Retention(RUNTIME)
@interface SaveSugar {}

@Qualifier
@Retention(RUNTIME)
@interface DeleteSugar {}
