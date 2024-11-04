import java.io.*;
import java.util.*;

// Interface for electronic devices
interface Device {
    String getCategory();
    String getName();
    double getPrice();
    int getQuantity();
    void setName(String name);
    void setPrice(double price);
    void setQuantity(int quantity);
}

// Class representing an electronic device
class ElectronicDevice implements Device {
    private String category;
    private String name;
    private double price;
    private int quantity;

    public ElectronicDevice(String category, String name, double price, int quantity) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Category: " + category + ", Name: " + name + ", Price: $" + price + ", Quantity: " + quantity;
    }
}

// Class representing the inventory management system
class Inventory {
    private LinkedList<ArrayList<Device>> inventoryList;

    public Inventory() {
        inventoryList = new LinkedList<>();
    }

    public void addDevice(Device device) {
        ArrayList<Device> categoryList = findCategoryList(device.getCategory());
        if (categoryList == null) {
            categoryList = new ArrayList<>();
            inventoryList.add(categoryList);
        }
        categoryList.add(device);
    }

    public void removeDevice(Device device) {
        ArrayList<Device> categoryList = findCategoryList(device.getCategory());
        if (categoryList != null) {
            categoryList.remove(device);
            if (categoryList.isEmpty()) {
                inventoryList.remove(categoryList);
            }
        }
    }

    public void updateDevice(Device oldDevice, Device newDevice) {
        ArrayList<Device> categoryList = findCategoryList(oldDevice.getCategory());
        if (categoryList != null && categoryList.contains(oldDevice)) {
            int index = categoryList.indexOf(oldDevice);
            categoryList.set(index, newDevice);
        }
    }

    public void displayDevices() {
        for (ArrayList<Device> categoryList : inventoryList) {
            for (Device device : categoryList) {
                System.out.println(device);
            }
        }
    }

    public Device findMinimumPriceDevice() {
        Device minPriceDevice = null;
        double minPrice = Double.MAX_VALUE;
        for (ArrayList<Device> categoryList : inventoryList) {
            for (Device device : categoryList) {
                if (device.getPrice() < minPrice) {
                    minPrice = device.getPrice();
                    minPriceDevice = device;
                }
            }
        }
        return minPriceDevice;
    }

    public ArrayList<Device> findCategoryList(String category) {
        for (ArrayList<Device> categoryList : inventoryList) {
            if (!categoryList.isEmpty() && categoryList.get(0).getCategory().equals(category)) {
                return categoryList;
            }
        }
        return null;
    }

    public void sortDevicesByPrice() {
        for (ArrayList<Device> categoryList : inventoryList) {
            categoryList.sort(Comparator.comparingDouble(Device::getPrice));
        }
    }

    public double calculateInventoryValue() {
        double totalValue = 0.0;
        for (ArrayList<Device> categoryList : inventoryList) {
            for (Device device : categoryList) {
                totalValue += device.getPrice() * device.getQuantity();
            }
        }
        return totalValue;
    }

    public void restockDevice(Device device, int quantityToAdd) {
        ArrayList<Device> categoryList = findCategoryList(device.getCategory());
        if (categoryList != null && categoryList.contains(device)) {
            int index = categoryList.indexOf(device);
            device.setQuantity(device.getQuantity() + quantityToAdd);
            categoryList.set(index, device);
        }
    }

    public void exportInventoryToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (ArrayList<Device> categoryList : inventoryList) {
                for (Device device : categoryList) {
                    writer.println(device.getCategory() + "," + device.getName() + "," + device.getPrice() + "," + device.getQuantity());
                }
            }
            System.out.println("Inventory exported to file: " + filename);
        } catch (IOException e) {
            System.err.println("Error exporting inventory to file: " + e.getMessage());
        }
    }
}

public class Myinventory {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();

        // Adding sample devices to inventory
        inventory.addDevice(new ElectronicDevice("TV", "Samsung 4K TV", 899.99, 5));
        inventory.addDevice(new ElectronicDevice("Smartphone", "iPhone 15", 999.99, 10));
        inventory.addDevice(new ElectronicDevice("Laptop", "MSI  ", 1499.99, 3));
        inventory.addDevice(new ElectronicDevice("Tablet", "iPad Air", 599.99, 7));
        inventory.addDevice(new ElectronicDevice("Headphones", "Sony    ", 349.99, 15));

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n========== Inventory Management System ==========");
            System.out.println("1. Add Device");
            System.out.println("2. Remove Device");
            System.out.println("3. Update Device Details");
            System.out.println("4. Display Devices");
            System.out.println("5. Identify Device with Minimum Price");
            System.out.println("6. Sort Devices by Price");
            System.out.println("7. Calculate Total Inventory Value");
            System.out.println("8. Restock Device");
            System.out.println("9. Export Inventory to File");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    inventory.addDevice(new ElectronicDevice(category, name, price, quantity));
                    System.out.println("Device added successfully!");
                    break;
                case 2:
                    System.out.print("Enter category of device to remove: ");
                    String categoryToRemove = scanner.nextLine();
                    System.out.print("Enter name of device to remove: ");
                    String nameToRemove = scanner.nextLine();
                    ArrayList<Device> devicesToRemove = inventory.findCategoryList(categoryToRemove);
                    if (devicesToRemove != null) {
                        Device deviceToRemove = devicesToRemove.stream()
                                .filter(device -> device.getName().equals(nameToRemove))
                                .findFirst().orElse(null);
                        if (deviceToRemove != null) {
                            inventory.removeDevice(deviceToRemove);
                            System.out.println("Device removed successfully!");
                        } else {
                            System.out.println("Device not found in the inventory.");
                        }
                    } else {
                        System.out.println("Category not found in the inventory.");
                    }
                    break;
                case 3:
                    System.out.print("Enter category of device to update: ");
                    String categoryToUpdate = scanner.nextLine();
                    System.out.print("Enter name of device to update: ");
                    String nameToUpdate = scanner.nextLine();
                    ArrayList<Device> devicesToUpdate = inventory.findCategoryList(categoryToUpdate);
                    if (devicesToUpdate != null) {
                        Device oldDevice = devicesToUpdate.stream()
                                .filter(device -> device.getName().equals(nameToUpdate))
                                .findFirst().orElse(null);
                        if (oldDevice != null) {
                            System.out.print("Enter new name: ");
                            String newName = scanner.nextLine();
                            System.out.print("Enter new price: ");
                            double newPrice = scanner.nextDouble();
                            System.out.print("Enter new quantity: ");
                            int newQuantity = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            Device newDevice = new ElectronicDevice(categoryToUpdate, newName, newPrice, newQuantity);
                            inventory.updateDevice(oldDevice, newDevice);
                            System.out.println("Device details updated successfully!");
                        } else {
                            System.out.println("Device not found in the inventory.");
                        }
                    } else {
                        System.out.println("Category not found in the inventory.");
                    }
                    break;
                case 4:
                    System.out.println("========== All Devices in Inventory ==========");
                    inventory.displayDevices();
                    break;
                case 5:
                    Device minPriceDevice = inventory.findMinimumPriceDevice();
                    if (minPriceDevice != null) {
                        System.out.println("Device with minimum price: " + minPriceDevice);
                    } else {
                        System.out.println("No devices in inventory.");
                    }
                    break;
                case 6:
                    inventory.sortDevicesByPrice();
                    System.out.println("Devices sorted by price.");
                    break;
                case 7:
                    double totalValue = inventory.calculateInventoryValue();
                    System.out.println("Total inventory value: $" + totalValue);
                    break;
                case 8:
                    System.out.print("Enter category of device to restock: ");
                    String categoryToRestock = scanner.nextLine();
                    System.out.print("Enter name of device to restock: ");
                    String nameToRestock = scanner.nextLine();
                    ArrayList<Device> devicesToRestock = inventory.findCategoryList(categoryToRestock);
                    if (devicesToRestock != null) {
                        Device deviceToRestock = devicesToRestock.stream()
                                .filter(device -> device.getName().equals(nameToRestock))
                                .findFirst().orElse(null);
                        if (deviceToRestock != null) {
                            System.out.print("Enter quantity to add: ");
                            int quantityToAdd = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            inventory.restockDevice(deviceToRestock, quantityToAdd);
                            System.out.println("Device restocked successfully!");
                        } else {
                            System.out.println("Device not found in the inventory.");
                        }
                    } else {
                        System.out.println("Category not found in the inventory.");
                    }
                    break;
                case 9:
                    System.out.print("Enter filename to export inventory (e.g., inventory.txt): ");
                    String filename = scanner.nextLine();
                    inventory.exportInventoryToFile(filename);
                    break;
                case 0:
                    System.out.println("Exiting the inventory management system.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }
}
