package unoeste.fipp.manedeliveryfx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import unoeste.fipp.manedeliveryfx.db.dals.PedidoDAL;
import unoeste.fipp.manedeliveryfx.db.dals.TipoPagamentoDAL;
import unoeste.fipp.manedeliveryfx.db.entidades.Pedido;
import unoeste.fipp.manedeliveryfx.db.entidades.TipoPagamento;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class GerenciarPedidoFormController implements Initializable {

    @FXML private TextField tfId;
    @FXML private TextField tfCliente;
    @FXML private TextField tfTelefone;
    @FXML private TextField tfEndereco;
    @FXML private TextField tfNumero;
    @FXML private ComboBox<TipoPagamento> cbTipoPagamento;
    @FXML private ComboBox<String> cbStatus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        List<TipoPagamento> tps = new TipoPagamentoDAL().get("");
        cbTipoPagamento.setItems(FXCollections.observableArrayList(tps));
        cbStatus.setItems(FXCollections.observableArrayList("S","N"));

        if(GerenciarPedidoTableController.pedidoSelecionado != null){
            Pedido p = GerenciarPedidoTableController.pedidoSelecionado;
            tfId.setText(""+p.getId());
            tfCliente.setText(p.getNomeCliente());
            tfTelefone.setText(p.getFoneCliente());
            tfEndereco.setText(p.getLocal());
            tfNumero.setText(p.getNumero());
            cbTipoPagamento.getSelectionModel().select(p.getTipoPagamento());
            cbStatus.getSelectionModel().select(p.getEntregue());
        }
        Platform.runLater(() -> tfCliente.requestFocus());
    }

    @FXML
    void onCancelar(ActionEvent event){
        tfCliente.getScene().getWindow().hide();
    }

    @FXML
    void onConfirmar(ActionEvent event){
        PedidoDAL dal = new PedidoDAL();
        Pedido p = (GerenciarPedidoTableController.pedidoSelecionado == null)
                ? new Pedido() : GerenciarPedidoTableController.pedidoSelecionado;

        p.setData(LocalDate.now());
        p.setNomeCliente(tfCliente.getText());
        p.setFoneCliente(tfTelefone.getText());
        p.setLocal(tfEndereco.getText());
        p.setNumero(tfNumero.getText());
        p.setTipoPagamento(cbTipoPagamento.getValue());
        p.setEntregue(cbStatus.getValue() == null ? "N" : cbStatus.getValue());

        if(GerenciarPedidoTableController.pedidoSelecionado == null){
            dal.gravar(p);
        }
        else{
            dal.alterar(p);
        }
        tfCliente.getScene().getWindow().hide();
    }

    @FXML
    void onGerenciarItens(ActionEvent event){
        try {
            Stage stage = new Stage();
            FXMLLoader fx = new FXMLLoader(ManeDeliveryFX.class.getResource("gerenciar-pedido-itens-form.fxml"));
            Scene sc = new Scene(fx.load());
            stage.setTitle("Itens do Pedido");
            stage.setScene(sc);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tfCliente.getScene().getWindow());
            stage.showAndWait();
        }catch (Exception e){}
    }
}
