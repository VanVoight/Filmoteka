package pl.edu.pb.filmoteka.Models;

import java.util.Objects;

public class Category {
    private String name;
    private Integer id;
    public Category(String name, Integer id) {
        this.name = name;
        this.id =id;
    }

    public String getName() {
        return name;
    }
    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
