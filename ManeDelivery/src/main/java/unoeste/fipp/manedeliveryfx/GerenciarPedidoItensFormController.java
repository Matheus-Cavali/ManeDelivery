package unoeste.fipp.manedeliveryfx;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import unoeste.fipp.manedeliveryfx.db.dals.PedidoDAL;
import unoeste.fipp.manedeliveryfx.db.dals.ProdutoDAL;
import unoeste.fipp.manedeliveryfx.db.entidades.Pedido;
import unoeste.fipp.manedeliveryfx.db.entidades.Produto;
import unoeste.fipp.manedeliveryfx.util.ModalTable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GerenciarPedidoItensFormController implements Initializable {

    @FXML private Button btProduto;
    @FXML private Spinner<Integer> spQuant;
    @FXML private TableView<Pedido.Item> tableView;
    @FXML private TableColumn<Pedido.Item, String> coQuant;
    @FXML private TableColumn<Pedido.Item, String> coProduto;
    @FXML private TableColumn<Pedido.Item, String> coValor;
    @FXML private Label lbTotal;

    private Produto produto = null;
    private double total = 0.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        coProduto.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().produto().getNome()));
        coQuant.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().quantidade())));
        coValor.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f", c.getValue().valor())));
        spQuant.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1));

        Pedido p = GerenciarPedidoTableController.pedidoSelecionado;
        if(p != null && p.getItens() != null){
            tableView.setItems(FXCollections.observableArrayList(p.getItens()));
            total = p.getItens().stream().mapToDouble(Pedido.Item::valor).sum();
            lbTotal.setText(String.format("%.2f", total));
        }
        Platform.runLater(() -> btProduto.requestFocus());
    }

    @FXML
    void onCancelar(ActionEvent event){
        btProduto.getScene().getWindow().hide();
    }

    @FXML
    void onConfirmar(ActionEvent event){
        Pedido p = GerenciarPedidoTableController.pedidoSelecionado;
        if(p != null){
            List<Pedido.Item> itensPedido = p.getItens();
            itensPedido.clear();
            for(Pedido.Item i : tableView.getItems()){
                p.addItem(i.produto(), i.quantidade());
            }
            p.setTotal(total);
            new PedidoDAL().alterar(p);
        }
        btProduto.getScene().getWindow().hide();
    }

    @FXML
    void onSelProduto(ActionEvent event){
        ModalTable mt = new ModalTable(new ProdutoDAL().get(""),
                new String[]{"id","nome","preco","marca"},"nome");
        Stage stage = new Stage();
        stage.setScene(new Scene(mt));
        stage.setWidth(1024);
        stage.setHeight(600);
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();

        produto = (Produto) mt.getSelecionado();
        if(produto != null)
            btProduto.setText(produto.getNome());
    }

    @FXML
    void onAdicItem(ActionEvent event){
        int q = spQuant.getValue();
        var itens = tableView.getItems();

        boolean achou = false;
        for(int i = 0; i < itens.size(); i++){
            Pedido.Item atual = itens.get(i);

            if(!achou && atual.produto().getId() == produto.getId()){
                int novaQtd = atual.quantidade() + q;
                double novoValor = novaQtd * produto.getPreco();

                Pedido.Item at = new Pedido.Item(produto, novaQtd, novoValor);
                itens.set(i, at);

                achou = true;
            }
        }
        if(!achou){
            double valor = produto.getPreco() * q;
            Pedido.Item item = new Pedido.Item(produto, q, valor);
            itens.add(item);
        }
        total = itens.stream().mapToDouble(Pedido.Item::valor).sum();
        lbTotal.setText(String.format("%.2f", total));
    }


    @FXML
    void onDelItem(ActionEvent event){
        Pedido.Item item = tableView.getSelectionModel().getSelectedItem();
        if(item != null){
            tableView.getItems().remove(item);
            total -= item.valor();
            lbTotal.setText(String.format("%.2f", total));
        }
    }
}
