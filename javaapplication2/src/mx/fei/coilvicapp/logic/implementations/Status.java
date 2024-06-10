package mx.fei.coilvicapp.logic.implementations;

/**
 *
 * @author ivanr
 */
public enum Status {
    SUCCESS("100"),
    WARNING("200"),
    ERROR("300"),
    FATAL("400");

    private final String code;

    private Status(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
