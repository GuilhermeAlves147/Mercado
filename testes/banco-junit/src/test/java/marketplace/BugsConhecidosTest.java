package marketplace;

import org.junit.jupiter.api.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

class BugsConhecidosTest {
    static Connection conn;

    @BeforeAll
    static void setup() throws Exception {
        conn = DbHelper.conectar();
        DbHelper.recriarBanco(conn);
    }

    @AfterAll
    static void fim() throws Exception {
        conn.close();
    }

    // cada teste roda numa transação desfeita no final, para não sujar o banco
    @BeforeEach
    void iniciarTransacao() throws Exception {
        conn.setAutoCommit(false);
    }

    @AfterEach
    void desfazer() throws Exception {
        conn.rollback();
        conn.setAutoCommit(true);
    }

    /** Conta as linhas devolvidas pela consulta de número n do 03_consultas.sql. */
    static int contarLinhas(int n) throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/sql/03_consultas.sql"));
        StringBuilder sb = new StringBuilder();
        for (String linha : sql.split("\n")) {
            if (!linha.trim().startsWith("--")) sb.append(linha).append("\n");
        }
        java.util.List<String> stmts = new java.util.ArrayList<>();
        for (String cmd : sb.toString().split(";")) {
            if (!cmd.isBlank() && !cmd.trim().toUpperCase().startsWith("USE")) stmts.add(cmd.trim());
        }
        int total = 0;
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(stmts.get(n - 1))) {
            while (rs.next()) total++;
        }
        return total;
    }

    // BUG-U-01 (achado B1): a consulta 4 deveria devolver produtos
    @Test
    void consulta04_deveRetornarProdutos() throws Exception {
        assertTrue(contarLinhas(4) > 0,
                "B1: a consulta 4 deveria retornar produtos entre R$100 e R$2000, mas retornou 0 linhas");
    }

    // BUG-U-02 (achado B4): RN17, o banco deveria rejeitar pedido com data futura
    @Test
    void bancoDeveRejeitarPedidoComDataFutura() throws Exception {
        try (Statement st = conn.createStatement()) {
            assertThrows(SQLException.class, () -> st.execute(
                            "INSERT INTO tb_order(date_order,totalvalue,id_client) " +
                                    "VALUES (DATE_ADD(NOW(), INTERVAL 1 YEAR), 10, 16)"),
                    "RN17: o banco aceitou um pedido com data futura");
        }
    }

    // BUG-U-03 (achado B4): categoria não pode ser pai dela mesma
    @Test
    void bancoDeveRejeitarCategoriaPaiDeSiMesma() throws Exception {
        try (Statement st = conn.createStatement()) {
            assertThrows(SQLException.class, () -> st.execute(
                            "INSERT INTO tb_category(id_category, category, id_category_parent) VALUES (500,'Auto',500)"),
                    "RN05: o banco aceitou uma categoria ligada a ela mesma");
        }
    }

    // BUG-U-04 (achado B4): RN13, total do pedido deve bater com a soma dos itens
    @Test
    void totalDoPedidoDeveBaterComItens() throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("INSERT INTO tb_order(id_order,totalvalue,id_client) VALUES (900, 1.00, 16)");
            st.execute("INSERT INTO tb_orderItem VALUES (900, 1, 2, 100.00)");
            ResultSet rs = st.executeQuery(
                    "SELECT COUNT(*) FROM tb_order o WHERE o.totalvalue <> " +
                            "(SELECT COALESCE(SUM(i.quantity * i.unit_value),0) FROM tb_orderItem i WHERE i.id_order = o.id_order)");
            rs.next();
            assertEquals(0, rs.getInt(1),
                    "RN13: o banco aceitou um pedido de total R$1,00 com itens somando R$200,00");
        }
    }
}