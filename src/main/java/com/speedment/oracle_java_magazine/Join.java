/**
 *
 * Copyright (c) 2006-2017, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.oracle_java_magazine;

import com.company.sakila.SakilaApplication;
import com.company.sakila.db0.sakila.film.Film;
import com.company.sakila.db0.sakila.film.FilmManager;
import com.company.sakila.db0.sakila.language.Language;
import com.company.sakila.db0.sakila.language.LanguageManager;
import com.speedment.common.tuple.Tuples;
import com.speedment.oracle_java_magazine.util.ExampleUtil;
import static com.speedment.oracle_java_magazine.util.ExampleUtil.buildApplication;

/**
 *
 * @author Per Minborg
 */
public class Join {

    private final SakilaApplication app;
    private final FilmManager films;
    private final LanguageManager languages;

    public Join() {
        app = buildApplication();
        films = app.getOrThrow(FilmManager.class);
        languages = app.getOrThrow(LanguageManager.class);
    }

    public static void main(String[] args) {
        new Join().run();
    }

    private void run() {
        oneToMany();
        manyToMany();
        join();
    }

    private void oneToMany() {
        ExampleUtil.log("oneToMany");

        languages.stream()
            .filter(Language.NAME.equal("English"))
            .flatMap(films.finderBackwardsBy(Film.LANGUAGE_ID))
            .forEach(System.out::println);

    }
    
    private void manyToMany() {
        ExampleUtil.log("manyToMany");

        languages.stream()
            .filter(Language.NAME.equal("English"))
            .flatMap(films.finderBackwardsBy(Film.LANGUAGE_ID))
            .forEach(System.out::println);

    }

    private void join() {
        ExampleUtil.log("join");

         films.stream()
            .filter(Film.RATING.in("G", "PG"))
            .map(f -> Tuples.of(f, languages.findBy(Film.LANGUAGE_ID, f)))
            .map(t2 -> String.format("The film '%s' is in %s", t2.get0().getTitle(), t2.get1().getName()))
            .forEachOrdered(System.out::println);

    }

}
