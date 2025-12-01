package com.veras.produto.app;

import com.veras.produto.view.ProdutoView;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProdutoView tela = new ProdutoView();
            tela.setVisible(true);
        });
    }
}