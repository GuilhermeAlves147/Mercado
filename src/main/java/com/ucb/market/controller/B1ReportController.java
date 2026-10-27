package com.ucb.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/b1")
@RequiredArgsConstructor
public class B1ReportController {
    private final NamedParameterJdbcTemplate jdbc;

    /** Filtro opcional de categoria/nome, com JOIN e agregação por categoria. */
    @GetMapping("/consultas/estoque")
    public List<Map<String,Object>> estoque(@RequestParam(required=false) Integer categoriaId,
                                             @RequestParam(required=false) String nome) {
        String sql = "SELECT c.id_category AS categoriaId, c.category AS categoria, " +
            "COUNT(p.id_product) AS totalProdutos, COALESCE(SUM(p.quantity),0) AS unidades, " +
            "COALESCE(SUM(p.quantity*p.value),0) AS valorEstoque " +
            "FROM tb_category c JOIN tb_product p ON p.id_category=c.id_category " +
            "WHERE (:categoria IS NULL OR c.id_category=:categoria) " +
            "AND (:nome IS NULL OR LOWER(p.name) LIKE CONCAT('%',LOWER(:nome),'%')) " +
            "GROUP BY c.id_category,c.category ORDER BY c.category";
        Map<String,Object> args=new HashMap<>(); args.put("categoria",categoriaId);
        args.put("nome",nome==null||nome.isBlank()?null:nome.trim());
        return jdbc.queryForList(sql,args);
    }

    /** Filtro de intervalo e vendedor, com JOIN e agregação de itens vendidos. */
    @GetMapping("/consultas/vendas")
    public List<Map<String,Object>> vendas(@RequestParam(required=false) Integer vendedorId,
                                            @RequestParam(required=false) LocalDate inicio,
                                            @RequestParam(required=false) LocalDate fim) {
        if(inicio!=null&&fim!=null&&inicio.isAfter(fim))
            throw new com.ucb.market.exception.BusinessException("A data inicial não pode ser posterior à data final.");
        StringBuilder sql = new StringBuilder(
            "SELECT p.id_seller AS vendedorId, u.user AS vendedor, " +
            "COUNT(DISTINCT o.id_order) AS pedidos, COALESCE(SUM(oi.quantity),0) AS unidades, " +
            "COALESCE(SUM(oi.quantity*oi.unit_value),0) AS faturamento " +
            "FROM tb_order o JOIN tb_orderItem oi ON oi.id_order=o.id_order " +
            "JOIN tb_product p ON p.id_product=oi.id_product " +
            "JOIN tb_seller s ON s.id_seller=p.id_seller " +
            "JOIN tb_user u ON u.id_user=s.id_seller WHERE 1=1");
        Map<String,Object> args = new HashMap<>();
        if (vendedorId != null) {
            sql.append(" AND p.id_seller=:vendedor");
            args.put("vendedor", vendedorId);
        }
        if (inicio != null) {
            sql.append(" AND o.date_order>=:inicio");
            args.put("inicio", inicio.atStartOfDay());
        }
        if (fim != null) {
            sql.append(" AND o.date_order<:fimExclusivo");
            args.put("fimExclusivo", fim.plusDays(1).atStartOfDay());
        }
        sql.append(" GROUP BY p.id_seller,u.user ORDER BY faturamento DESC");
        return jdbc.queryForList(sql.toString(),args);
    }

    /** Depende da VIEW criada por sql/04_b1_view.sql. */
    @GetMapping("/relatorios/categorias")
    public List<Map<String,Object>> relatorio() {
        return jdbc.queryForList("SELECT * FROM vw_b1_resumo_categorias ORDER BY categoria", Map.of());
    }
}
