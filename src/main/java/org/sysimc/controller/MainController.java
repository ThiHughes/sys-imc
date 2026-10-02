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
        lerFormulario();
        this.pessoa.classificacaoIMC();
        exibirClassificacaoIMC();
        exibirClassificacaoIMC();
        System.out.println(this.pessoa.toString());
    }
    @FXML
    public void onClickSalvarIMC(){
        lerFormulario();
        this.pessoa.classificacaoIMC();
        this.listaPessoas.add(this.pessoa);
        atualizarTableView();
        this.pessoa = new Pessoa();
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
        this.pessoa.setNome( txtNome.getText() );
        this.pessoa.setPeso( Float.parseFloat(txtPeso.getText()) );
        this.pessoa.setAltura( Float.parseFloat(txtAltura.getText()) );

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