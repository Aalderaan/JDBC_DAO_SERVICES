package mate.jdbc;

import mate.jdbc.lib.Injector;
import mate.jdbc.model.Driver;
import mate.jdbc.model.Manufacturer;
import mate.jdbc.service.DriverService;
import mate.jdbc.service.ManufacturerService;

public class Main {

    private static final Injector injector = Injector.getInstance("mate.jdbc");

    public static void main(String[] args) {
        Driver john = new Driver("John", "Joni182");
        Driver bob = new Driver("Bob", "bib123");
        DriverService manager = (DriverService) injector.getInstance(DriverService.class);
        manager.create(john);
        manager.create(bob);
        System.out.println("List of all drivers:");
        manager.getAll().forEach(System.out::println);

        john.setName("Jonni");
        john.setLicenseNumber("Jonni234");
        manager.update(john);
        System.out.println("Find John " + manager.get(john.getId()));
        System.out.println("Delete John: " + manager.delete(john.getId()));
        System.out.println(System.lineSeparator() + "Info in table drivers:");
        manager.getAll().forEach(System.out::println);
        manager.getAll().forEach(d -> manager.delete(d.getId()));
        System.out.println(System.lineSeparator());

        Manufacturer bmw = new Manufacturer("BMW", "Germany");
        Manufacturer ford = new Manufacturer("Ford", "USA");
        ManufacturerService factory = (ManufacturerService)
                injector.getInstance(ManufacturerService.class);
        factory.create(bmw);
        factory.create(ford);
        System.out.println("List of all cars:");
        factory.getAll().forEach(System.out::println);
        bmw.setCountry("Ukraine");
        bmw.setName("BMWUHA");
        factory.update(bmw);
        System.out.println("Find BMWUHA: " + factory.get(bmw.getId()));
        System.out.println("Delete BMWUHA: " + factory.delete(bmw.getId()));
        System.out.println(System.lineSeparator() + "Info in table manufacturers:");
        factory.getAll().forEach(System.out::println);
        factory.getAll().forEach(car -> factory.delete(car.getId()));
    }
}
