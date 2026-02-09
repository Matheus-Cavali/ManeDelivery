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
import unoeste.fipp.manedeliveryfx.db.dals.TipoPagamentoDAL;
import unoeste.fipp.manedeliveryfx.db.entidades.Produto;
import unoeste.fipp.manedeliveryfx.db.entidades.TipoPagamento;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TiposPagamentoTableController implements Initializable {

    @FXML
    private TableColumn<TipoPagamento, Integer> coId;

    @FXML
    private TableColumn<TipoPagamento, String> coNome;

    @FXML
    private TableView<TipoPagamento> tableView;

    @FXML
    private TextField tfPesquisar;

    static public TipoPagamento tpgSelecionado=null;

    @FXML
    void onPesquisar(KeyEvent event){
        carregarTabela("upper(tpg_nome) LIKE '%"+tfPesquisar.getText().toUpperCase()+"%'");
    }

    @FXML
    void onFechar(ActionEvent event){
        tfPesquisar.getScene().getWindow().hide();
    }

    @FXML
    void onNovoPagamento(ActionEvent event){
        try{
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("tipos-pagamentos-form-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Cadastro de Tipos de Pagamento");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        }catch (Exception e){}
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        coId.setCellValueFactory(new PropertyValueFactory<>("id"));
        coNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        carregarTabela("");
    }

    private void carregarTabela(String filtro){
        TipoPagamentoDAL dal = new TipoPagamentoDAL();
        List<TipoPagamento> lista = dal.get(filtro);
        tableView.setItems(FXCollections.observableArrayList(lista));
    }

    public void onAlterar(ActionEvent actionEvent){
        TipoPagamentoDAL dal=new TipoPagamentoDAL();
        if(tableView.getSelectionModel().getSelectedItem()!=null){
            tpgSelecionado=tableView.getSelectionModel().getSelectedItem();
            try{
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("tipos-pagamentos-form-view.fxml"));
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
        TipoPagamentoDAL dal = new TipoPagamentoDAL();
        if(tableView.getSelectionModel().getSelectedItem()!=null){
            dal.apagar(tableView.getSelectionModel().getSelectedItem());
            carregarTabela("");
        }
    }
}
