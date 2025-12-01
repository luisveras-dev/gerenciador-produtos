package com.veras.produto.view;

import com.veras.produto.model.Produto;
import com.veras.produto.service.ProdutoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class ProdutoView extends JFrame {

    private ProdutoService service;
    private JTextField txtNome, txtPreco, txtQuantidade, txtId;
    private JTable tabelaProdutos;
    private DefaultTableModel tableModel;

    public ProdutoView() {
        service = new ProdutoService();
        initComponents();
        atualizarTabela();
    }

    private void initComponents() {
        setTitle("Gerenciador de Estoque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));

        txtId = new JTextField();
        txtId.setEditable(false);
        txtNome = new JTextField();
        txtPreco = new JTextField();
        txtQuantidade = new JTextField();

        panelForm.add(new JLabel("ID:"));
        panelForm.add(txtId);
        panelForm.add(new JLabel("Nome:"));
        panelForm.add(txtNome);
        panelForm.add(new JLabel("Preço (R$):"));
        panelForm.add(txtPreco);
        panelForm.add(new JLabel("Quantidade:"));
        panelForm.add(txtQuantidade);

        add(panelForm, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "Preço", "Quantidade"};
        tableModel = new DefaultTableModel(colunas, 0);
        tabelaProdutos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);

        tabelaProdutos.getSelectionModel().addListSelectionListener(e -> preencherFormulario());

        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel(new FlowLayout());

        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnRemover = new JButton("Remover");
        JButton btnLimpar = new JButton("Limpar");

        JTextField txtBusca = new JTextField(15);
        txtBusca.setBorder(BorderFactory.createTitledBorder("Buscar por Nome"));
        txtBusca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrarTabela(txtBusca.getText());
            }
        });

        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnAtualizar.addActionListener(e -> atualizarProduto());
        btnRemover.addActionListener(e -> removerProduto());
        btnLimpar.addActionListener(e -> limparCampos());

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnRemover);
        panelBotoes.add(btnLimpar);
        panelBotoes.add(txtBusca);

        add(panelBotoes, BorderLayout.SOUTH);
    }

    private void adicionarProduto() {
        try {
            String nome = txtNome.getText();
            double preco = Double.parseDouble(txtPreco.getText());
            int qtd = Integer.parseInt(txtQuantidade.getText());

            service.adicionar(nome, preco, qtd);
            atualizarTabela();
            limparCampos();
            JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro: Verifique se preço e quantidade são números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarProduto() {
        try {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
                return;
            }
            int id = Integer.parseInt(txtId.getText());
            String nome = txtNome.getText();
            double preco = Double.parseDouble(txtPreco.getText());
            int qtd = Integer.parseInt(txtQuantidade.getText());

            if (service.atualizar(id, nome, preco, qtd)) {
                atualizarTabela();
                limparCampos();
                JOptionPane.showMessageDialog(this, "Produto atualizado!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar.");
        }
    }

    private void removerProduto() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para remover.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            service.remover(id);
            atualizarTabela();
            limparCampos();
        }
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Produto> produtos = service.listar();
        for (Produto p : produtos) {
            tableModel.addRow(new Object[]{p.getId(), p.getNome(), p.getPreco(), p.getQuantidade()});
        }
    }

    private void filtrarTabela(String termo) {
        tableModel.setRowCount(0);
        List<Produto> produtos = service.buscarPorNome(termo);
        for (Produto p : produtos) {
            tableModel.addRow(new Object[]{p.getId(), p.getNome(), p.getPreco(), p.getQuantidade()});
        }
    }

    private void preencherFormulario() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            txtId.setText(tabelaProdutos.getValueAt(linhaSelecionada, 0).toString());
            txtNome.setText(tabelaProdutos.getValueAt(linhaSelecionada, 1).toString());
            txtPreco.setText(tabelaProdutos.getValueAt(linhaSelecionada, 2).toString());
            txtQuantidade.setText(tabelaProdutos.getValueAt(linhaSelecionada, 3).toString());
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtPreco.setText("");
        txtQuantidade.setText("");
        tabelaProdutos.clearSelection();
    }
}