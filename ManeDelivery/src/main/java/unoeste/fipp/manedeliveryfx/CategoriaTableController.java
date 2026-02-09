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
import unoeste.fipp.manedeliveryfx.db.dals.CategoriaDAL;
import unoeste.fipp.manedeliveryfx.db.dals.ProdutoDAL;
import unoeste.fipp.manedeliveryfx.db.entidades.Categoria;
import unoeste.fipp.manedeliveryfx.db.entidades.TipoPagamento;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CategoriaTableController implements Initializable {

    @FXML
    private TableColumn<Categoria, Integer> coId;

    @FXML
    private TableColumn<Categoria, String> coNome;

    @FXML
    private TableView<Categoria> tableView;

    @FXML
    private TextField tfPesquisar;

    static public Categoria categoriaSelecionada=null;

    @FXML
    void onPesquisar(KeyEvent event){
        carregarTabela("upper(cat_nome) LIKE '%"+tfPesquisar.getText().toUpperCase()+"%'");
    }

    @FXML
    void onFechar(ActionEvent event){
        tfPesquisar.getScene().getWindow().hide();
    }

    @FXML
    void onNovoCategoria(ActionEvent event){
        try{
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("Categoria-form-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Cadastro de Categorias");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
            carregarTabela(tfPesquisar.getText());
        }catch (Exception e){}
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        coId.setCellValueFactory(new PropertyValueFactory<>("id"));
        coNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        carregarTabela("");
    }

    private void carregarTabela(String filtro){
        CategoriaDAL dal = new CategoriaDAL();
        List<Categoria> lista = dal.get(filtro);
        tableView.setItems(FXCollections.observableArrayList(lista));
    }

    public void onAlterar(ActionEvent actionEvent){
        CategoriaDAL dal=new CategoriaDAL();
        if(tableView.getSelectionModel().getSelectedItem()!=null){
            categoriaSelecionada=tableView.getSelectionModel().getSelectedItem();
            try{
                Stage stage=new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("Categoria-form-view.fxml"));
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
        CategoriaDAL dal=new CategoriaDAL();
        if(tableView.getSelectionModel().getSelectedItem()!=null){
            dal.apagar(tableView.getSelectionModel().getSelectedItem());
            carregarTabela("");
        }
    }
}
