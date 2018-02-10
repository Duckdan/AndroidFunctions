package study.com.androidfunctions.model;

/**
 * 例：实体类
 */
public class PersonInfo {

    private String name;

    public PersonInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
