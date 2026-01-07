
package com.example.dq.engine;

import com.example.dq.model.RuleSet;
import com.example.dq.model.Check;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class RuleEngine {

    private final JdbcTemplate jdbcTemplate;

    public RuleEngine(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void execute(RuleSet ruleSet) {
        for (Check check : ruleSet.checks) {
            String sql = SqlGenerator.generate(check, ruleSet.entity);
            Integer failures = jdbcTemplate.queryForObject(sql, Integer.class);
            System.out.println("Check " + check.id + " failures=" + failures);
        }
    }
}
