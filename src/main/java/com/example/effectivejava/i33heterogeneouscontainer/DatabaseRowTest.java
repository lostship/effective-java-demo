package com.example.effectivejava.i33heterogeneouscontainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class DatabaseRowTest {
    private final Map<Column<?>, Object> row = new HashMap<>();

    public <T> void put(Column<?> column, T value) {
        row.put(Objects.requireNonNull(column), column.type.cast(value));
    }

    public <T> T get(Column<T> column) {
        return column.type.cast(row.get(column));
    }

    public static final class Column<T> {
        private final String name;
        private final Class<T> type;

        public static <T> Column<T> from(String name, Class<T> type) {
            return new Column<>(name, type);
        }

        private Column(String name, Class<T> type) {
            this.name = name;
            this.type = type;
        }
    }

    public static void main(String[] args) {
        Map<String, Column<?>> columns = Map.of(
                "id", Column.from("id", Integer.class),
                "name", Column.from("name", String.class));

        List<DatabaseRowTest> rows = new ArrayList<>();
        DatabaseRowTest row1 = new DatabaseRowTest();
        row1.put(columns.get("id"), 1);
        row1.put(columns.get("name"), "Tom");
        rows.add(row1);
        DatabaseRowTest row2 = new DatabaseRowTest();
        row2.put(columns.get("id"), 2);
        row2.put(columns.get("name"), "Jerry");
        rows.add(row2);

        for (DatabaseRowTest row : rows) {
            StringJoiner sj = new StringJoiner(", ");
            for (Column<?> column : columns.values()) {
                sj.add(column.name + " = " + row.get(column) + " (" + column.type.getSimpleName() + ")");
            }
            System.out.println(sj.toString());
        }
    }
}
