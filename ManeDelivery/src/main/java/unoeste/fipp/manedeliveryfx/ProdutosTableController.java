package unoeste.fipp.manedeliveryfx;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import unoeste.fipp.manedeliveryfx.db.dals.ProdutoDAL;
import unoeste.fipp.manedeliveryfx.db.entidades.Categoria;
import unoeste.fipp.manedeliveryfx.db.entidades.Marca;
import unoeste.fipp.manedeliveryfx.db.entidades.Produto;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProdutosTableController implements Initializable {

    @FXML
    private TableColumn<Produto, Categoria> coCategoria;

    @FXML
    private TableColumn<Produto, Integer> coId;

    @FXML
    private TableColumn<Produto, Marca> coMarca;

    @FXML
    private TableColumn<Produto, String> coNome;

    @FXML
    private TableView<Produto> tableView;

    @FXML
    private TextField tfPesquisar;

    static public Produto produtoSelecionado=null;

    @FXML
    void onFechar(ActionEvent event){
        tfPesquisar.getScene().getWindow().hide();
    }

    @FXML
    void onNovoProduto(ActionEvent event){
        try{
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("produto-form-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Cadastro de Produtos");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        }catch (Exception e){}
    }

    @FXML
    void onPesquisar(KeyEvent event){
        carregarTabela("upper(pro_nome) LIKE '%"+tfPesquisar.getText().toUpperCase()+"%'");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        coId.setCellValueFactory(new PropertyValueFactory<>("id"));
        coNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        coMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        coCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        carregarTabela("");
    }

    private void carregarTabela(String filtro){
        ProdutoDAL dal = new ProdutoDAL();
        List<Produto> produtoList=dal.get(filtro);
        tableView.setItems(FXCollections.observableArrayList(produtoList));
    }

    public void onAlterar(ActionEvent actionEvent){
        ProdutoDAL dal = new ProdutoDAL();
        if(tableView.getSelectionModel().getSelectedItem()!=null){
            produtoSelecionado=tableView.getSelectionModel().getSelectedItem();
            try{
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("produto-form-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Alteração de Produtos");
                stage.setScene(scene);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.showAndWait();
                carregarTabela("");
            }catch (Exception e){}
        }
    }

    public void onApagar(ActionEvent actionEvent){
        ProdutoDAL dal = new ProdutoDAL();
        if(tableView.getSelectionModel().getSelectedItem()!=null){
            dal.apagar(tableView.getSelectionModel().getSelectedItem());
            carregarTabela("");
        }
    }
}
