package org.sysimc.utils;

import org.sysimc.model.Pessoa;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ArquivoUtil {
    private static final String ARQUIVO = "dados_pessoas.txt";

    // SALVAR
    public static void salvar(List<Pessoa> pessoas) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO, StandardCharsets.UTF_8))) {
            for (Pessoa p : pessoas) {
                bw.write(p.getId() + ","
                        + p.getNome().replace(",", " ") + ","
                        + p.getPeso() + ","
                        + p.getAltura() + ","
                        + p.getImc());
                bw.newLine();
            }
        }
    }

    // CARREGAR
    public static List<Pessoa> carregar() throws IOException {
        List<Pessoa> lista = new ArrayList<>();
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return lista;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo, StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.isBlank()) continue;
                try {
                    String[] c = linha.split(",");
                    Pessoa p = new Pessoa();
                    p.setId(Integer.parseInt(c[0].trim()));
                    p.setNome(c[1].trim());
                    p.setPeso(Float.parseFloat(c[2].trim()));
                    p.setAltura(Float.parseFloat(c[3].trim()));
                    p.classificacaoIMC();
                    lista.add(p);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Linha ignorada (formato inválido): " + linha);
                }
            }
        }
        return lista;
    }
}