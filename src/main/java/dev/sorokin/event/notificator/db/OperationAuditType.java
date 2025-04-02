package dev.sorokin.event.notificator.db;

public enum OperationAuditType {
    CREATE("create"), UPDATE("update"), REMOVE("remove");

    private final String name;

    private OperationAuditType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
