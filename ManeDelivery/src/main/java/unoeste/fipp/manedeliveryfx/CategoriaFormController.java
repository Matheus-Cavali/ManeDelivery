package unoeste.fipp.manedeliveryfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import unoeste.fipp.manedeliveryfx.db.dals.CategoriaDAL;
import unoeste.fipp.manedeliveryfx.db.entidades.Categoria;
import unoeste.fipp.manedeliveryfx.db.util.SingletonDB;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoriaFormController implements Initializable {

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
        CategoriaDAL dal = new CategoriaDAL();
        Categoria categoria = new Categoria();

        categoria.setNome(tfNome.getText());

        if(tfId.getText().isEmpty()){
            if(!dal.gravar(categoria)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao cadastrar\n" + SingletonDB.getConexao().getMensagemErro());
                alert.showAndWait();
            }
            else{
                tfNome.getScene().getWindow().hide();
            }
        }
        else{
            categoria.setId(Integer.parseInt(tfId.getText()));
            if(!dal.alterar(categoria)){
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

        if(CategoriaTableController.categoriaSelecionada != null){
            Categoria aux = CategoriaTableController.categoriaSelecionada;
            tfId.setEditable(true);
            tfId.setText("" + aux.getId());
            tfId.setEditable(false);
            tfNome.setText(aux.getNome());
        }
    }
}
