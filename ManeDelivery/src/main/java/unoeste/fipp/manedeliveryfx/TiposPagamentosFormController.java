package unoeste.fipp.manedeliveryfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import unoeste.fipp.manedeliveryfx.db.dals.TipoPagamentoDAL;
import unoeste.fipp.manedeliveryfx.db.entidades.TipoPagamento;
import unoeste.fipp.manedeliveryfx.db.util.SingletonDB;

import java.net.URL;
import java.util.ResourceBundle;

public class TiposPagamentosFormController implements Initializable {

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfNome;

    @FXML
    void onCancelar(ActionEvent event){
        tfNome.getScene().getWindow().hide();
    }

    @FXML
    void onConfirmar(ActionEvent event){
        TipoPagamentoDAL dal = new TipoPagamentoDAL();
        TipoPagamento tp = new TipoPagamento();

        tp.setNome(tfNome.getText());

        if(tfId.getText().isEmpty()){
            if(!dal.gravar(tp)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao cadastrar\n" + SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            }
            else{
                tfNome.getScene().getWindow().hide();
            }
        }
        else{
            tp.setId(Integer.parseInt(tfId.getText()));
            if(!dal.alterar(tp)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao alterar\n" + SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            }
            else{
                tfNome.getScene().getWindow().hide();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        Platform.runLater(() -> tfNome.requestFocus());

        if(TiposPagamentoTableController.tpgSelecionado != null){
            TipoPagamento aux = TiposPagamentoTableController.tpgSelecionado;
            tfId.setEditable(true);
            tfId.setText("" + aux.getId());
            tfId.setEditable(false);
            tfNome.setText(aux.getNome());
        }
    }
}
