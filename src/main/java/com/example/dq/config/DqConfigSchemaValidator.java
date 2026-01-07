
package com.example.dq.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.*;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Set;

@Component
public class DqConfigSchemaValidator {

    private final ObjectMapper mapper = new ObjectMapper();

    public void validate(InputStream configStream) {
        try {
            JsonSchemaFactory factory =
                JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

            InputStream schemaStream =
                getClass().getResourceAsStream("/schema/dq-config.schema.json");

            JsonSchema schema = factory.getSchema(schemaStream);
            JsonNode configNode = mapper.readTree(configStream);

            Set<ValidationMessage> errors = schema.validate(configNode);

            if (!errors.isEmpty()) {
                StringBuilder sb = new StringBuilder("DQ Config validation failed:\n");
                for (ValidationMessage msg : errors) {
                    sb.append("- ").append(msg.getMessage()).append("\n");
                }
                throw new IllegalStateException(sb.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid dq-config.json", e);
        }
    }
}
