package gr.evansp.momento.bean;

import java.util.Map;

public record ConstraintViolationExceptionMessage(Map<String, String> messages) {
}
