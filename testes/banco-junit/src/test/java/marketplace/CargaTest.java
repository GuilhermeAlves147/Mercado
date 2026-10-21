package marketplace;

import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

class CargaTest {
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

    long escalar(String sql) throws Exception {
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getLong(1);
        }
    }

    void verificaContagem(String tabela, long esperado) throws Exception {
        assertEquals(esperado, escalar("SELECT COUNT(*) FROM " + tabela), tabela);
    }

    // CARGA-U-01: quantidade de registros inseridos em cada tabela
    @Test
    void quantidadeDeRegistros() throws Exception {
        verificaContagem("tb_user", 45);
        verificaContagem("tb_seller", 20);
        verificaContagem("tb_client", 30);
        verificaContagem("tb_category", 14);
        verificaContagem("tb_supplier", 16);
        verificaContagem("tb_product", 55);
        verificaContagem("tb_product_supplier", 57);
        verificaContagem("tb_cart", 45);
        verificaContagem("tb_buyItem", 74);
        verificaContagem("tb_order", 50);
        verificaContagem("tb_orderItem", 151);
        verificaContagem("tb_order_status", 163);
    }

    // CARGA-U-02: RN13, total do pedido = soma de quantity * unit_value dos itens
    @Test
    void totalDoPedidoBateComItens() throws Exception {
        assertEquals(0, escalar(
                "SELECT COUNT(*) FROM tb_order o WHERE o.totalvalue <> " +
                        "(SELECT COALESCE(SUM(i.quantity * i.unit_value),0) FROM tb_orderItem i WHERE i.id_order = o.id_order)"));
    }

    // CARGA-U-03: total e quantidade do carrinho batem com os itens
    @Test
    void totalDoCarrinhoBateComItens() throws Exception {
        assertEquals(0, escalar(
                "SELECT COUNT(*) FROM tb_cart c WHERE " +
                        "c.totalvalue <> (SELECT COALESCE(SUM(b.quantity * b.unit_value),0) FROM tb_buyItem b WHERE b.id_cart = c.id_cart) " +
                        "OR c.itemquantity <> (SELECT COALESCE(SUM(b.quantity),0) FROM tb_buyItem b WHERE b.id_cart = c.id_cart)"));
    }

    // CARGA-U-04: RN11, unit_value dos itens igual ao preço do produto
    @Test
    void valorUnitarioBateComProduto() throws Exception {
        assertEquals(0, escalar(
                "SELECT COUNT(*) FROM tb_orderItem i JOIN tb_product p ON p.id_product = i.id_product " +
                        "WHERE i.unit_value <> p.value"));
    }

    // CARGA-U-05: RN17, nenhum pedido com data futura
    @Test
    void nenhumPedidoComDataFutura() throws Exception {
        assertEquals(0, escalar("SELECT COUNT(*) FROM tb_order WHERE date_order > NOW()"));
    }

    // CARGA-U-06: RN14, todo pedido tem ao menos um status
    @Test
    void todoPedidoTemStatus() throws Exception {
        assertEquals(0, escalar(
                "SELECT COUNT(*) FROM tb_order o WHERE NOT EXISTS " +
                        "(SELECT 1 FROM tb_order_status s WHERE s.id_order = o.id_order)"));
    }

    // CARGA-U-07: RN01, 5 usuários são vendedor e cliente ao mesmo tempo
    @Test
    void usuariosComDoisPapeis() throws Exception {
        assertEquals(5, escalar(
                "SELECT COUNT(*) FROM tb_user u WHERE EXISTS (SELECT 1 FROM tb_seller s WHERE s.id_seller = u.id_user) " +
                        "AND EXISTS (SELECT 1 FROM tb_client c WHERE c.id_client = u.id_user)"));
    }

    // CARGA-U-08: nenhum produto sem vendedor ou sem categoria (RN02, RN04)
    @Test
    void nenhumProdutoOrfao() throws Exception {
        assertEquals(0, escalar(
                "SELECT COUNT(*) FROM tb_product p " +
                        "LEFT JOIN tb_seller s ON s.id_seller = p.id_seller " +
                        "LEFT JOIN tb_category c ON c.id_category = p.id_category " +
                        "WHERE s.id_seller IS NULL OR c.id_category IS NULL"));
    }

    // CARGA-U-09: casos de contorno propositais da carga
    @Test
    void casosDeContorno() throws Exception {
        assertEquals(5, escalar(
                "SELECT COUNT(*) FROM tb_product p WHERE NOT EXISTS " +
                        "(SELECT 1 FROM tb_product_supplier ps WHERE ps.id_product = p.id_product)"));   // produtos 11,22,33,44,55
        assertEquals(1, escalar(
                "SELECT COUNT(*) FROM tb_cart c WHERE NOT EXISTS " +
                        "(SELECT 1 FROM tb_buyItem b WHERE b.id_cart = c.id_cart)"));                    // carrinho 45 vazio
        assertEquals(4, escalar("SELECT COUNT(*) FROM tb_product WHERE quantity = 0"));      // estoque zerado
    }
}