package pl.edu.agh.to.thumbnails.server.utils;

public class Error {

    public static Error None = new Error("None", null);

    private String _code;
    private String _description;

    public Error(String code, String description)
    {
        _code = code;
        _description = description;
    }

    public String getCode() {
        return _code;
    }

    public String getDescription() {
        return _description;
    }

}
