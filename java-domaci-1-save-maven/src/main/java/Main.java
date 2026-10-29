
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        try (Connection connection = DatabaseConnection.getConnection()) {

            DataRepository repository = new DataRepository(connection);
            System.out.println("Baza je povezana");

            repository.save(new User("Mateja", "12345"));
            repository.save(new Subject("Matematika", 6));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}