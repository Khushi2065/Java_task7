import java.sql.*;
import java.util.Scanner;

public class StaffManager {

    private static final String URL = "jdbc:mysql://localhost:3306/company";
    private static final String USER = "root";
    private static final String PASSWORD = "Khushi@25";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (Exception e) {
            System.out.println("Driver error: " + e.getMessage());
        }

        Scanner input = new Scanner(System.in);
        boolean active = true;

        while (active) {
            System.out.println("\n=== Staff Management System ===");
            System.out.println("1. Register Staff");
            System.out.println("2. Show All Staff");
            System.out.println("3. Edit Staff Info");
            System.out.println("4. Remove Staff");
            System.out.println("5. Quit");
            System.out.print("Select option: ");
            int choice = Integer.parseInt(input.nextLine());

            switch (choice) {
                case 1 -> registerStaff(input);
                case 2 -> listStaff();
                case 3 -> editStaff(input);
                case 4 -> removeStaff(input);
                case 5 -> {
                    active = false;
                    System.out.println("Program ended.");
                }
                default -> System.out.println(" Invalid option. Try again.");
            }
        }
        input.close();
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void registerStaff(Scanner input) {
        try (Connection conn = connect()) {
            System.out.print("Enter name: ");
            String name = input.nextLine();
            System.out.print("Enter division: ");
            String division = input.nextLine();
            System.out.print("Enter wage: ");
            double wage = Double.parseDouble(input.nextLine());

            String sql = "INSERT INTO employees (name, department, salary) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, division);
            ps.setDouble(3, wage);

            int rows = ps.executeUpdate();
            System.out.println("✅ " + rows + " staff registered.");
        } catch (Exception e) {
            System.out.println("Error while registering: " + e.getMessage());
        }
    }

    private static void listStaff() {
        try (Connection conn = connect()) {
            String sql = "SELECT * FROM employees";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n--- Staff List ---");
            System.out.printf("%-5s %-20s %-15s %-10s%n", "ID", "Name", "Division", "Wage");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-15s ₹%.2f%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"));
            }
        } catch (Exception e) {
            System.out.println("Error fetching staff: " + e.getMessage());
        }
    }

    private static void editStaff(Scanner input) {
        try (Connection conn = connect()) {
            System.out.print("Enter Staff ID to edit: ");
            int id = Integer.parseInt(input.nextLine());

            System.out.print("New name: ");
            String newName = input.nextLine();
            System.out.print("New division: ");
            String newDiv = input.nextLine();
            System.out.print("New wage: ");
            double newWage = Double.parseDouble(input.nextLine());

            String sql = "UPDATE employees SET name=?, department=?, salary=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newName);
            ps.setString(2, newDiv);
            ps.setDouble(3, newWage);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();
            System.out.println("✅ " + rows + " staff record(s) updated.");
        } catch (Exception e) {
            System.out.println("Error while editing: " + e.getMessage());
        }
    }

    private static void removeStaff(Scanner input) {
        try (Connection conn = connect()) {
            System.out.print("Enter Staff ID to remove: ");
            int id = Integer.parseInt(input.nextLine());

            String sql = "DELETE FROM employees WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            System.out.println("✅ " + rows + " staff removed.");
        } catch (Exception e) {
            System.out.println("Error while removing: " + e.getMessage());
        }
    }
}
