package mate.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mate.jdbc.exception.DataProcessingException;
import mate.jdbc.lib.Dao;
import mate.jdbc.model.Manufacturer;
import mate.jdbc.util.ConnectionUtil;

@Dao
public class ManufacturerDaoImpl implements ManufacturerDao {
    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        String insertManufacturerQuery = "INSERT INTO manufacturers (name, country) "
                + "VALUES (?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement createStatement
                     = connection.prepareStatement(insertManufacturerQuery,
                     Statement.RETURN_GENERATED_KEYS)) {
            createStatement.setString(1, manufacturer.getName());
            createStatement.setString(2, manufacturer.getCountry());
            createStatement.executeUpdate();
            ResultSet resultSet = createStatement.getGeneratedKeys();
            if (resultSet.next()) {
                manufacturer.setId(resultSet.getObject(1, Long.class));
            }
                return manufacturer;
            } catch(SQLException throwable){
                throw new DataProcessingException("Couldn't create manufacturer. " + manufacturer + " ",
                        throwable);
        }
    }

    @Override
    public Optional<Manufacturer> get(Long id) {
        String query = "SELECT * FROM manufacturers"
                + " WHERE id = (?) AND deleted = FALSE";
        String getManufacturerQuery = "SELECT * FROM manufacturers"
                + " WHERE id = (?) AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
            PreparedStatement getStatement = connection.prepareStatement(
                    getManufacturerQuery)) {
                getStatement.setLong(1, id);
                ResultSet resultSet = getStatement.executeQuery();
                Manufacturer manufacturer = null;
                if (resultSet.next()) {
                    manufacturer = getManufacturer(resultSet);
                }

                return Optional.ofNullable(manufacturer);
            } catch (SQLException throwable) {
            throw new DataProcessingException("Couldn't get manufacturer by id " + id + " ",
                    throwable);
        }

    }

    @Override
    public List<Manufacturer> getAll() {
        String getAllManufacturersQuery = "SELECT * FROM manufacturers WHERE is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
            PreparedStatement getAllStatement = connection.prepareStatement(
                getAllManufacturersQuery)) {
            List<Manufacturer> manufacturers = new ArrayList<>();
            ResultSet resultSet = getAllStatement.executeQuery();
            while (resultSet.next()) {
              manufacturers.add(getManufacturer(resultSet));
           }

           return manufacturers;
           } catch (SQLException throwable) {
                throw new DataProcessingException("Couldn't get a list of manufacturers "
                    + "from manufacturers table. ",
                    throwable);
        }
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        String updateManufacturerQuery = "UPDATE manufacturers SET name = ?, country = ?"
                + " WHERE id = ? AND is_deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection();
            PreparedStatement updateManufacturerStatement = connection.prepareStatement(
                    updateManufacturerQuery)) {
                updateManufacturerStatement.setString(1, manufacturer.getName());
                updateManufacturerStatement.setString(2, manufacturer.getCountry());
                updateManufacturerStatement.setLong(3, manufacturer.getId());
                updateManufacturerStatement.executeUpdate();
                if (updateManufacturerStatement.executeUpdate() > 0) {
                    return manufacturer;
                }
                throw new RuntimeException("Can't update. Manufacturer does not exist.");
            } catch (SQLException throwable) {
                throw new DataProcessingException("Couldn't update a manufacturer "
                        + manufacturer + " ", throwable);
        }
    }

    @Override
    public boolean delete(Long id) {
        String deleteQuery = "UPDATE manufacturers SET is_deleted = TRUE WHERE id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                deleteStatement.setLong(1, id);
                return deleteStatement.executeUpdate() > 0;
            } catch (SQLException throwable) {
                throw new DataProcessingException("Couldn't delete a manufacturer by id " + id + " ",
                        throwable);
            }
        }

    private Manufacturer getManufacturer(ResultSet resultSet) throws SQLException {
        Long newId = resultSet.getObject("id", Long.class);
        String name = resultSet.getString("name");
        String country = resultSet.getString("country");
        Manufacturer manufacturer = new Manufacturer(name, country);
        manufacturer.setId(newId);
        manufacturer = new Manufacturer(resultSet.getString("name"),
        resultSet.getString("country"));
        manufacturer.setId(resultSet.getObject("id", Long.class));
        return manufacturer;
    }

}