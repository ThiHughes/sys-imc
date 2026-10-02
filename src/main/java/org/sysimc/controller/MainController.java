package org.sysimc.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.sysimc.model.Pessoa;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import org.sysimc.utils.ArquivoUtil;
import java.io.IOException;

public class MainController implements Initializable {
    // VARIAVEIS CAIXA DE TEXTO TEXTFIELD
    @FXML
    protected TextField txtNome;
    @FXML
    protected TextField txtPeso;
    @FXML
    protected TextField txtAltura;
    // VARIAVEIS LABEL
    @FXML
    protected Label lbIMC;
    @FXML
    protected Label lbClassificacao;

    // VARIAVEIS TABELA
    @FXML
    TableView<Pessoa> tbPessoas;
    @FXML
    TableColumn<Pessoa, Integer> colId;
    @FXML
    TableColumn<Pessoa, String> colNome;
    @FXML
    TableColumn<Pessoa, Float> colPeso;
    @FXML
    TableColumn<Pessoa, Float> colAltura;
    @FXML
    TableColumn<Pessoa, Float> colIMC;

    // Objeto Modelo
    Pessoa pessoa;
    List<Pessoa> listaPessoas;
    ObservableList<Pessoa> observableListPessoas;

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.pessoa = new Pessoa();
        this.listaPessoas = new ArrayList<>();
        iniciarGUI();
    }

    public void iniciarGUI(){
        // VINCULAR AS CADA CELULA DA LINHA DA TABELA COM O ATRIBUTO DO OBJETO PESSOA
        this.colId.setCellValueFactory( new PropertyValueFactory<>("id"));
        this.colNome.setCellValueFactory( new PropertyValueFactory<>("nome"));
        this.colPeso.setCellValueFactory( new PropertyValueFactory<>("peso"));
        this.colAltura.setCellValueFactory( new PropertyValueFactory<>("altura"));
        this.colIMC.setCellValueFactory( new PropertyValueFactory<>("imc"));
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // EVENTOS
    @FXML
    public void onClickCalcularIMC(){
        try {
            lerFormulario();
            this.pessoa.classificacaoIMC();
            exibirClassificacaoIMC();
        } catch (NumberFormatException e) {
            mostrarErro("Peso e altura devem ser números válidos (ex.: 70 e 1.75).");
        } catch (IllegalArgumentException e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    public void onClickSalvarIMC(){
        try {
            lerFormulario();
            this.pessoa.classificacaoIMC();
            int maiorId = this.listaPessoas.stream().mapToInt(Pessoa::getId).max().orElse(0);
            this.pessoa.setId(maiorId + 1);
            this.listaPessoas.add(this.pessoa);

            ArquivoUtil.salvar(this.listaPessoas);
            atualizarTableView();

            this.pessoa = new Pessoa();
        } catch (NumberFormatException e) {
            mostrarErro("Peso e altura devem ser números válidos (ex.: 70 e 1.75).");
        } catch (IllegalArgumentException e) {
            mostrarErro(e.getMessage());
        } catch (IOException e) {
            mostrarErro("Não foi possível salvar o arquivo: " + e.getMessage());
        }
    }

    @FXML
    public void onClickCarregar(){
        try {
            this.listaPessoas = ArquivoUtil.carregar();

            int maiorId = this.listaPessoas.stream().mapToInt(Pessoa::getId).max().orElse(0);
            Pessoa.setProximoId(maiorId + 1);   // próximos IDs continuam depois do último
            this.pessoa = new Pessoa();

            atualizarTableView();
        } catch (IOException e) {
            mostrarErro("Não foi possível ler o arquivo: " + e.getMessage());
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    @FXML
    public void onClickNovo(){

        this.pessoa = new Pessoa();
        txtNome.setText("");
        txtPeso.setText("");
        txtAltura.setText("");
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // FORMULARIO
    public void lerFormulario(){
        String nome = txtNome.getText().trim();
        float peso = Float.parseFloat(txtPeso.getText().trim().replace(",", "."));
        float altura = Float.parseFloat(txtAltura.getText().trim().replace(",", "."));

        if (altura > 3) {
            throw new IllegalArgumentException("Informe a altura em metros (ex.: 1.75).");
        }
        if (nome.isEmpty()) {
            throw new IllegalArgumentException("Informe o nome.");
        }
        if (peso <= 0 || altura <= 0) {
            throw new IllegalArgumentException("Peso e altura devem ser maiores que zero.");
        }

        this.pessoa.setNome(nome);
        this.pessoa.setPeso(peso);
        this.pessoa.setAltura(altura);
    }

    public void exibirClassificacaoIMC(){
        DecimalFormat df = new DecimalFormat("#0.00");
        lbIMC.setText(df.format(this.pessoa.getImc()));
        lbClassificacao.setText(this.pessoa.getClassificacao() );
    }
    public void atualizarTableView(){

        this.listaPessoas.forEach( obj -> System.out.printf(obj.getNome() +", " + obj.getPeso() +", " + obj.getAltura() +"\n"));
        this.observableListPessoas = FXCollections.observableList(this.listaPessoas);
        this.tbPessoas.setItems(this.observableListPessoas);

    }


}