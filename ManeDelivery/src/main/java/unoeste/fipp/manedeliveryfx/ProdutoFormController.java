package unoeste.fipp.manedeliveryfx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import unoeste.fipp.manedeliveryfx.db.dals.CategoriaDAL;
import unoeste.fipp.manedeliveryfx.db.dals.MarcaDAL;
import unoeste.fipp.manedeliveryfx.db.dals.ProdutoDAL;
import unoeste.fipp.manedeliveryfx.db.entidades.Categoria;
import unoeste.fipp.manedeliveryfx.db.entidades.Marca;
import unoeste.fipp.manedeliveryfx.db.entidades.Produto;
import unoeste.fipp.manedeliveryfx.db.util.SingletonDB;
import unoeste.fipp.manedeliveryfx.util.MaskFieldUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProdutoFormController implements Initializable {

    @FXML
    private ComboBox<Categoria> cbCategoria;

    @FXML
    private ComboBox<Marca> cbMarca;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfPreco;

    @FXML
    private TextField tfVolume;

    @FXML
    void onCancelar(ActionEvent event){
        tfVolume.getScene().getWindow().hide();
    }

    @FXML
    void onConfirmar(ActionEvent event){
        ProdutoDAL dal=new ProdutoDAL();
        Produto produto=new Produto();
        // valide se há dados digitados
        produto.setNome(tfNome.getText());
        produto.setPreco(Double.parseDouble(tfPreco.getText().replace(",",".")));
        produto.setVolume(Integer.parseInt(tfVolume.getText()));
        produto.setMarca(cbMarca.getSelectionModel().getSelectedItem());
        produto.setCategoria(cbCategoria.getValue());
        if(tfId.getText().isEmpty()){
            if(!dal.gravar(produto)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao cadastrar\n" + SingletonDB.getConexao().getMensagemErro());
            }
            else
                tfVolume.getScene().getWindow().hide();
        }
        else{
            produto.setId(Integer.parseInt(tfId.getText()));
            if(!dal.alterar(produto)){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro ao alterar\n"+ SingletonDB.getConexao().getMensagemErro());
            }
            else
                tfVolume.getScene().getWindow().hide();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        Platform.runLater(()->{tfNome.requestFocus();});
        MaskFieldUtil.monetaryField(tfPreco);
        MaskFieldUtil.numericField(tfVolume);
        carregarComboBox();

        if(ProdutosTableController.produtoSelecionado!=null){
            Produto aux=ProdutosTableController.produtoSelecionado;
            tfId.setEditable(true);
            tfId.setText(""+aux.getId());
            tfId.setEditable(false);
            tfNome.setText(aux.getNome());
            tfPreco.setText(String.format("%.2f",aux.getPreco()));
            tfVolume.setText(""+aux.getVolume());
            cbMarca.getSelectionModel().select(aux.getMarca());
            cbCategoria.getSelectionModel().select(aux.getCategoria());
        }
    }

    private void carregarComboBox(){
        MarcaDAL marcaDAL = new MarcaDAL();
        CategoriaDAL categoriaDAL = new CategoriaDAL();
        List<Marca> marcaList = marcaDAL.get("");
        List<Categoria> categoriaList = categoriaDAL.get("");

        cbMarca.setItems(FXCollections.observableArrayList(marcaList));
        cbCategoria.setItems(FXCollections.observableArrayList(categoriaList));
    }
}
