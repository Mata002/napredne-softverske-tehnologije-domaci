import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class DataRepository {

    private final Connection connection;
    private final Properties mapping = new Properties();

    public DataRepository(Connection connection) throws Exception {
        this.connection = connection;

        // ucitavamo mapiranje iz fajla
        try (InputStream in = new FileInputStream("mapping.properties")) {
            mapping.load(in);
        }
    }

    public void save(Object object) throws Exception {

        Class<?> klass = object.getClass();
        String className = klass.getSimpleName();

        // ime tabele dolazi iz fajla
        String tableName = mapping.getProperty(className + ".table");
        if (tableName == null) {
            throw new Exception("Nema mapiranja za klasu " + className);
        }

        // ime polja koje je ID (njega preskacemo)
        String id = mapping.getProperty(className + ".id");

        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Field field : klass.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (field.getName().equals(id)) {
                continue;
            }

            // ime kolone dolazi iz fajla: "Klasa.polje"
            String columnName = mapping.getProperty(className + "." + field.getName());
            if (columnName == null) {
                continue; // polje nije mapirano, ne cuvamo ga
            }

            field.setAccessible(true);
            columns.add(columnName);
            values.add(field.get(object));
        }

        String columnNames = String.join(", ", columns);
        String placeholders = String.join(", ", Collections.nCopies(columns.size(), "?"));

        String sql = "INSERT INTO `" + tableName + "`("
                + columnNames + ") VALUES ("
                + placeholders + ")";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }
            statement.executeUpdate();
        }
    }
}