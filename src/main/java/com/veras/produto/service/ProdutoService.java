package com.veras.produto.service;

import com.veras.produto.model.Produto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoService {

    private List<Produto> listaProdutos;
    private int proximoId;

    public ProdutoService() {
        this.listaProdutos = new ArrayList<>();
        this.proximoId = 1;
    }

    public void adicionar(String nome, double preco, int quantidade) {
        Produto novo = new Produto(proximoId++, nome, preco, quantidade);
        listaProdutos.add(novo);
    }

    public List<Produto> listar() {
        return listaProdutos;
    }

    public List<Produto> buscarPorNome(String termo) {
        return listaProdutos.stream()
                .filter(p -> p.getNome().toLowerCase().contains(termo.toLowerCase()))
                .collect(Collectors.toList());
    }

    public boolean atualizar(int id, String nome, double preco, int quantidade) {
        for (Produto p : listaProdutos) {
            if (p.getId() == id) {
                p.setNome(nome);
                p.setPreco(preco);
                p.setQuantidade(quantidade);
                return true;
            }
        }
        return false;
    }

    public boolean remover(int id) {
        return listaProdutos.removeIf(p -> p.getId() == id);
    }
}