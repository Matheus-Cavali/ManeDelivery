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
import unoeste.fipp.manedeliveryfx.db.dals.MarcaDAL;
import unoeste.fipp.manedeliveryfx.db.dals.ProdutoDAL;
import unoeste.fipp.manedeliveryfx.db.entidades.Marca;
import unoeste.fipp.manedeliveryfx.db.entidades.Produto;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MarcaTableController implements Initializable {

    @FXML
    private TableColumn<Marca, Integer> coId;

    @FXML
    private TableColumn<Marca, String> coNome;

    @FXML
    private TableView<Marca> tableView;

    @FXML
    private TextField tfPesquisar;

    static public Marca marcaSelecionada=null;

    @FXML
    void onPesquisar(KeyEvent event){
        carregarTabela("upper(mar_nome) LIKE '%"+tfPesquisar.getText().toUpperCase()+"%'");
    }

    @FXML
    void onFechar(ActionEvent event){
        tfPesquisar.getScene().getWindow().hide();
    }

    @FXML
    void onNovoMarca(ActionEvent event){
        try{
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("marca-form-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Cadastro de Marcas");
            stage.setScene(scene);
            stage.initOwner(((javafx.scene.Node)event.getSource()).getScene().getWindow());
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
        MarcaDAL dal = new MarcaDAL();
        List<Marca> lista = dal.get(filtro);
        tableView.setItems(FXCollections.observableArrayList(lista));
    }

    public void onAlterar(ActionEvent actionEvent){
        MarcaDAL dal=new MarcaDAL();
        if(tableView.getSelectionModel().getSelectedItem()!=null){
            marcaSelecionada=tableView.getSelectionModel().getSelectedItem();
            try{
                Stage stage=new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("marca-form-view.fxml"));
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
        MarcaDAL dal=new MarcaDAL();
        if(tableView.getSelectionModel().getSelectedItem()!=null){
            dal.apagar(tableView.getSelectionModel().getSelectedItem());
            carregarTabela("");
        }
    }
}
