package com.example.trabalhocontroleremedio;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.example.trabalhocontroleremedio.banco.IdosoDAO;
import com.example.trabalhocontroleremedio.enumeracao.Sexo;
import com.example.trabalhocontroleremedio.excecao.CampoVazioExcecao;
import com.example.trabalhocontroleremedio.modelo.Idoso;
import com.example.trabalhocontroleremedio.modelo.Login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class CadastrarIdosoController {

    private Idoso idoso;
    private IdosoDAO jpa;
    private ObservableList<String> listaSexo;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button alterar;

    @FXML
    private Button buscar;

    @FXML
    private Button cadastrar;

    @FXML
    private Button excluir;

    @FXML
    private DatePicker nascimento;

    @FXML
    private TextField nome;

    @FXML
    private ChoiceBox<String> sexo;

    @FXML
    private Label excecao;

    @FXML
    void alterar(ActionEvent event) { // Realiza a alteração do Idoso
        desativarExcecao();
        try{
            if(nome.getText().equals("")){
                throw new CampoVazioExcecao();
            }
            idoso.setNome(nome.getText());
            idoso.setNascimento(nascimento.getValue().toString());
            idoso.setSexo(sexo.getValue());
            jpa.editar(idoso);
            novaExcecao("Idoso alteraddo com sucesso!", Color.GREEN);
            HelloApplication.escreverLog(Login.getLogin() + " alterou idoso " + this.nome.getText());
        }catch(CampoVazioExcecao CVE){
            System.out.println("Necessário fornecer um nome!");
            novaExcecao("Necessário fornecer um nome!", Color.RED);
        }catch(Exception ex){
            System.out.println(ex);
            novaExcecao("Impossível alterar idoso!", Color.RED);
        }
    }

    @FXML
    void buscar(ActionEvent event) { // Realiza a busca de Idoso
        desativarExcecao();
        try{
            if(nome.getText().equals("")){
                throw new CampoVazioExcecao();
            }
            idoso = new Idoso();
            idoso.setNome(nome.getText());
            idoso = jpa.buscarNome(idoso);
            if(idoso != null){
                sexo.setValue(idoso.getSexo());
                nascimento.setValue(LocalDate.parse(idoso.getNascimento()));
            }else{
                novaExcecao("Idoso não encontrado", Color.RED);
            }
        }catch(CampoVazioExcecao CVE){
            System.out.println("Necessário fornecer um nome");
            novaExcecao("Necessário fornecer um nome!", Color.RED);
        }catch(NullPointerException NPE){
            System.out.println(NPE);
        }
    }

    @FXML
    void cadastrar(ActionEvent event) { // Cadastra o Idoso
        desativarExcecao();
        try{
            if(nome.getText().equals("") || nascimento.getValue().equals(null)){
                throw new CampoVazioExcecao();
            }
            idoso = new Idoso();
            int totalIdoso = 0;
            for (Idoso idoso : jpa.buscarTodos()) {
                if(idoso != null){
                    totalIdoso = idoso.getIdIdoso();
                }
            }
            idoso.setIdIdoso(totalIdoso+1);
            idoso.setNome(nome.getText());
            idoso.setNascimento(nascimento.getValue().toString());
            idoso.setSexo(sexo.getValue());
            jpa.cadastrar(idoso);
            novaExcecao("Idoso criado com sucesso!", Color.GREEN);
            HelloApplication.escreverLog(Login.getLogin() + " cadastrou idoso " + this.nome.getText());
        }catch(CampoVazioExcecao CVE){
            System.out.println("Campos vazios!");
            novaExcecao("Campos vazios encontrados!", Color.RED);
        }catch(Exception ex){
            System.out.println(ex);
            novaExcecao("Impossível cadastrar idoso!", Color.RED);
        }
    }

    @FXML
    void excluir(ActionEvent event) { // Exclui o Idoso
        desativarExcecao();
        try{
            if(nome.getText().equals("")){
                throw new CampoVazioExcecao();
            }
            idoso = new Idoso();
            idoso.setNome(nome.getText());
            idoso = jpa.buscarNome(idoso);
            jpa.excluir(idoso);
            nome.setText("");
            sexo.setValue("Masculino");
            nascimento.setValue(null);
            novaExcecao("Idoso excluido com sucesso!", Color.GREEN);
            HelloApplication.escreverLog(Login.getLogin() + " excluiu idoso " + this.nome.getText());
        }catch(CampoVazioExcecao CVE){
            System.out.println("Necessário nome para exclusão!");
            novaExcecao("Necessário fornecer um nome!", Color.RED);
        }catch(Exception ex){
            System.out.println(ex);
            novaExcecao("Impossível realizar a exclusão!", Color.RED);
        }
    }

    private void desativarExcecao(){
        excecao.setVisible(false);
        excecao.textFillProperty().set(Color.RED);
    }

    private void novaExcecao(String text, Color color){
        excecao.setText(text);
        excecao.textFillProperty().set(color);
        excecao.setVisible(true);
    }

    @FXML
    void initialize() {
        listaSexo = FXCollections.observableArrayList();
        for(Sexo listaSexo: Sexo.values()){
            this.listaSexo.add(listaSexo.getSexo());
        }
        sexo.setItems(listaSexo);
        sexo.setValue("Masculino");
        jpa = new IdosoDAO();
    }

}
