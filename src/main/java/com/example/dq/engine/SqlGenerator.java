
package com.example.dq.engine;

import com.example.dq.model.Check;
import com.example.dq.model.RuleSet;

public class SqlGenerator {

    public static String generate(Check check, RuleSet.Entity entity) {
        String table = entity.name;

        return switch (check.type) {
            case "not_null" -> "SELECT COUNT(*) FROM " + table + " WHERE " + check.column + " IS NULL";
            case "range" -> "SELECT COUNT(*) FROM " + table + " WHERE " + check.column +
                    " < " + check.params.get("min") + " OR " + check.column +
                    " > " + check.params.get("max");
            case "uniqueness" -> "SELECT COUNT(*) FROM (SELECT " + check.column +
                    " FROM " + table + " GROUP BY " + check.column + " HAVING COUNT(*) > 1) t";
            case "row_count" -> "SELECT COUNT(*) FROM " + table;
            case "custom_sql" -> (String) check.params.get("sql");
            default -> throw new IllegalArgumentException("Unsupported check: " + check.type);
        };
    }
}
