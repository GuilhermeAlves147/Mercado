package marketplace;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbHelper {
    static final String URL = "jdbc:mysql://localhost:3306/?allowMultiQueries=true";
    static final String USER = "root";
    static final String PASS = "19112004";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void executarScript(Connection c, String arquivo) throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/sql/" + arquivo));
        StringBuilder sb = new StringBuilder();
        for (String linha : sql.split("\n")) {
            if (!linha.trim().startsWith("--")) sb.append(linha).append("\n");
        }
        try (Statement st = c.createStatement()) {
            for (String cmd : sb.toString().split(";")) {
                if (!cmd.isBlank()) st.execute(cmd);
            }
        }
    }

    public static void recriarBanco(Connection c) throws Exception {
        executarScript(c, "01_ddl.sql");
        executarScript(c, "02_carga.sql");
    }
}