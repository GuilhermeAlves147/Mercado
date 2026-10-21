package marketplace;

import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

class DdlTest {
    static Connection conn;

    @BeforeAll
    static void setup() throws Exception {
        conn = DbHelper.conectar();
        DbHelper.executarScript(conn, "01_ddl.sql");
    }

    @AfterAll
    static void fim() throws Exception {
        conn.close();
    }

    @BeforeEach
    void limpar() throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("SET FOREIGN_KEY_CHECKS=0");
            for (String t : new String[]{"tb_product_supplier", "tb_order_status", "tb_orderItem",
                    "tb_order", "tb_buyItem", "tb_cart", "tb_supplier", "tb_product",
                    "tb_category", "tb_client", "tb_seller", "tb_user"}) {
                st.execute("TRUNCATE TABLE marketplace_db." + t);
            }
            st.execute("SET FOREIGN_KEY_CHECKS=1");
        }
    }

    // DDL-U-01: e-mail duplicado deve ser rejeitado (uq_user_email)
    @Test
    void rejeitaEmailDuplicado() throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("INSERT INTO marketplace_db.tb_user(user,password,email) VALUES('a','h','x@x.com')");
            assertThrows(SQLIntegrityConstraintViolationException.class, () ->
                    st.execute("INSERT INTO marketplace_db.tb_user(user,password,email) VALUES('b','h','x@x.com')"));
        }
    }

    // DDL-U-02: valor do produto deve ser > 0 (ck_product_value_positive)
    @Test
    void rejeitaProdutoComValorZero() throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("INSERT INTO marketplace_db.tb_user(id_user,user,password,email) VALUES(1,'v','h','v@v.com')");
            st.execute("INSERT INTO marketplace_db.tb_seller VALUES(1,'12345678000199')");
            st.execute("INSERT INTO marketplace_db.tb_category(id_category,category) VALUES(1,'Geral')");
            assertThrows(SQLException.class, () ->
                    st.execute("INSERT INTO marketplace_db.tb_product(name,value,quantity,id_seller,id_category) VALUES('P',0,1,1,1)"));
        }
    }

    // DDL-U-03: não excluir vendedor que possui produtos (RN02, ON DELETE RESTRICT)
    @Test
    void naoExcluiVendedorComProdutos() throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("INSERT INTO marketplace_db.tb_user(id_user,user,password,email) VALUES(1,'v','h','v@v.com')");
            st.execute("INSERT INTO marketplace_db.tb_seller VALUES(1,'12345678000199')");
            st.execute("INSERT INTO marketplace_db.tb_category(id_category,category) VALUES(1,'Geral')");
            st.execute("INSERT INTO marketplace_db.tb_product(name,value,quantity,id_seller,id_category) VALUES('P',10,1,1,1)");
            assertThrows(SQLException.class, () ->
                    st.execute("DELETE FROM marketplace_db.tb_seller WHERE id_seller=1"));
        }
    }

    // DDL-U-04: status inválido rejeitado (RN15, ck_orderstatus_domain)
    @Test
    void rejeitaStatusInvalido() throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("INSERT INTO marketplace_db.tb_user(id_user,user,password,email) VALUES(1,'c','h','c@c.com')");
            st.execute("INSERT INTO marketplace_db.tb_client(id_client) VALUES(1)");
            st.execute("INSERT INTO marketplace_db.tb_order(id_order,totalvalue,id_client) VALUES(1,10,1)");
            assertThrows(SQLException.class, () ->
                    st.execute("INSERT INTO marketplace_db.tb_order_status(id_order,status) VALUES(1,'xyz')"));
        }
    }

    // DDL-U-05: ON DELETE CASCADE, excluir usuário remove o cliente
    @Test
    void excluirUsuarioRemoveCliente() throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("INSERT INTO marketplace_db.tb_user(id_user,user,password,email) VALUES(1,'c','h','c@c.com')");
            st.execute("INSERT INTO marketplace_db.tb_client(id_client) VALUES(1)");
            st.execute("DELETE FROM marketplace_db.tb_user WHERE id_user=1");
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM marketplace_db.tb_client");
            rs.next();
            assertEquals(0, rs.getInt(1));
        }
    }
}