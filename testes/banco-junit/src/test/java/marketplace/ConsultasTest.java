package marketplace;

import org.junit.jupiter.api.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ConsultasTest {
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

    /** Executa a consulta de número n do arquivo 03_consultas.sql. */
    static List<List<String>> consulta(int n) throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/sql/03_consultas.sql"));
        StringBuilder sb = new StringBuilder();
        for (String linha : sql.split("\n")) {
            if (!linha.trim().startsWith("--")) sb.append(linha).append("\n");
        }
        List<String> stmts = new ArrayList<>();
        for (String cmd : sb.toString().split(";")) {
            if (!cmd.isBlank() && !cmd.trim().toUpperCase().startsWith("USE")) stmts.add(cmd.trim());
        }
        List<List<String>> linhas = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(stmts.get(n - 1))) {
            int cols = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                List<String> l = new ArrayList<>();
                for (int i = 1; i <= cols; i++) l.add(rs.getString(i));
                linhas.add(l);
            }
        }
        return linhas;
    }

    // CONS-U-01: lista todos os produtos
    @Test
    void consulta01_todosProdutos() throws Exception {
        assertEquals(55, consulta(1).size());
    }

    // CONS-U-02: produtos acima de R$100, do mais caro ao mais barato
    @Test
    void consulta02_acimaDe100OrdenadoDesc() throws Exception {
        List<List<String>> r = consulta(2);
        assertEquals(37, r.size());
        assertEquals("Notebook Acer Aspire 5 Pro", r.get(0).get(0));
        double anterior = Double.MAX_VALUE;
        for (List<String> linha : r) {
            double v = Double.parseDouble(linha.get(1));
            assertTrue(v > 100, "valor deveria ser > 100");
            assertTrue(v <= anterior, "ordem deveria ser decrescente");
            anterior = v;
        }
    }

    // CONS-U-03: produtos com "Celular" no nome
    @Test
    void consulta03_nomeContemCelular() throws Exception {
        List<List<String>> r = consulta(3);
        assertEquals(6, r.size());
        for (List<String> linha : r) assertTrue(linha.get(0).contains("Celular"));
    }

    // CONS-U-04: valor entre 100 e 2000 nas categorias 3 ou 4
    // As categorias 3 e 4 são categorias raiz (Alimentos, Vestuário) e os produtos
    // estão nas subcategorias (5 a 14), então a consulta retorna vazio.
    @Test
    void consulta04_categorias3e4() throws Exception {
        assertEquals(0, consulta(4).size());
    }

    // CONS-U-05: fornecedores sem telefone
    @Test
    void consulta05_fornecedoresSemTelefone() throws Exception {
        List<List<String>> r = consulta(5);
        Set<String> nomes = new HashSet<>();
        for (List<String> linha : r) nomes.add(linha.get(0));
        assertEquals(Set.of("Alimentos Brasil", "Utilcasa Distribuicao", "Norte Suprimentos"), nomes);
    }

    // CONS-U-06: categoria de cada produto (todo produto tem categoria, RN04)
    @Test
    void consulta06_categoriaDeCadaProduto() throws Exception {
        assertEquals(55, consulta(6).size());
    }

    // CONS-U-07: produto e usuário vendedor (todo produto tem vendedor, RN02)
    @Test
    void consulta07_produtoEVendedor() throws Exception {
        assertEquals(55, consulta(7).size());
    }

    // CONS-U-08: LEFT JOIN, 151 linhas de pedido + 35 produtos sem pedido
    @Test
    void consulta08_produtosComOuSemPedido() throws Exception {
        List<List<String>> r = consulta(8);
        assertEquals(186, r.size());
        long semPedido = r.stream().filter(l -> l.get(1) == null).count();
        assertEquals(35, semPedido);
    }

    // CONS-U-09: pedidos com mais de um status (50 pedidos, 5 só com "aberto")
    @Test
    void consulta09_pedidosComMaisDeUmStatus() throws Exception {
        List<List<String>> r = consulta(9);
        assertEquals(45, r.size());
        for (List<String> linha : r) assertTrue(Integer.parseInt(linha.get(1)) > 1);
    }

    // CONS-U-10: produtos por fornecedor (inclui fornecedores sem produto)
    @Test
    void consulta10_produtosPorFornecedor() throws Exception {
        List<List<String>> r = consulta(10);
        assertEquals(16, r.size());
        Map<String, Integer> m = new HashMap<>();
        for (List<String> linha : r) m.put(linha.get(0), Integer.parseInt(linha.get(1)));
        assertEquals(17, m.get("Felipe Distribuidora"));
        assertEquals(17, m.get("Bebidas do Vale"));
        assertEquals(16, m.get("Grao Dourado Alimentos"));
        assertEquals(2, m.get("Casa Boa Suprimentos"));
        assertEquals(0, m.get("Elias Comercio"));
    }

    // CONS-U-11: produtos acima da média (média = 821,57)
    @Test
    void consulta11_acimaDaMedia() throws Exception {
        List<List<String>> r = consulta(11);
        assertEquals(17, r.size());
        for (List<String> linha : r) assertTrue(Double.parseDouble(linha.get(1)) > 821.57);
    }

    // CONS-U-12: clientes que já fizeram pedido
    @Test
    void consulta12_clientesComPedido() throws Exception {
        List<List<String>> r = consulta(12);
        Set<String> nomes = new HashSet<>();
        for (List<String> linha : r) nomes.add(linha.get(0));
        assertEquals(Set.of("gabriela.nascimento", "vinicius.teixeira", "sabrina.santos",
                "mariana.gomes", "bruno.campos", "fabio.nascimento"), nomes);
    }

    // CONS-U-13: produtos nunca comprados
    @Test
    void consulta13_produtosNuncaComprados() throws Exception {
        assertEquals(35, consulta(13).size());
    }

    // CONS-U-14: pedido de maior valor
    @Test
    void consulta14_pedidoMaiorValor() throws Exception {
        List<List<String>> r = consulta(14);
        assertEquals(1, r.size());
        assertEquals("4", r.get(0).get(0));
        assertEquals(19476.86, Double.parseDouble(r.get(0).get(1)), 0.001);
        assertEquals("36", r.get(0).get(2));
    }

    // CONS-U-15: status mais recente de cada pedido
    @Test
    void consulta15_statusMaisRecente() throws Exception {
        List<List<String>> r = consulta(15);
        assertEquals(50, r.size());
        Map<String, String> statusPorPedido = new HashMap<>();
        for (List<String> linha : r) statusPorPedido.put(linha.get(0), linha.get(2));
        assertEquals("entregue", statusPorPedido.get("1"));
        assertEquals("cancelado", statusPorPedido.get("17"));
        assertEquals("aberto", statusPorPedido.get("9"));
    }
}