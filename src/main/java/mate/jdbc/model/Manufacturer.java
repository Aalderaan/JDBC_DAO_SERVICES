package mate.jdbc.model;

import java.util.Objects;

public class Manufacturer {
    private String name;
    private String country;
    private Long id;

    public Manufacturer() {
    }

    public Manufacturer(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Manufacturer manufacturer = (Manufacturer) o;
        return Objects.equals(name, manufacturer.name) && Objects.equals(country, manufacturer.country)
                && Objects.equals(id, manufacturer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, country, id);
    }

    @Override
    public String toString() {
        return "Manufacturer {"
                + "name= " + name + '\''
                + ", country='" + country + '\''
                + ", id='" + id + '\''
                + '}';
    }
}
