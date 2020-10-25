package entity;

/**
 * Class for Object Vaccine
 * Contains vaccine's name, detail, number of months till it expiry, and boolean var to indicate if this vaccine is only required once
 */
public class Park {
    private String name;
    private String detail;

    public Park() {
    }

    public Park(String name, String detail) {
        this.name = name;
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}