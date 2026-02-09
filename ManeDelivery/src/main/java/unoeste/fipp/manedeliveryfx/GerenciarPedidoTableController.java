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
import unoeste.fipp.manedeliveryfx.db.dals.PedidoDAL;
import unoeste.fipp.manedeliveryfx.db.entidades.Pedido;

import javafx.beans.property.SimpleStringProperty;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GerenciarPedidoTableController implements Initializable {

    @FXML
    private TableColumn<Pedido, String> coNome;
    @FXML
    private TableColumn<Pedido, String> coEndereco;
    @FXML
    private TableColumn<Pedido, String> coPreco;
    @FXML
    private TableColumn<Pedido, String> coStatus;

    @FXML
    private TableView<Pedido> tableView;

    @FXML
    private TextField tfPesquisar;

    public static Pedido pedidoSelecionado = null;

    @FXML
    void onFechar(ActionEvent event){
        tfPesquisar.getScene().getWindow().hide();
    }

    @FXML
    void onPesquisar(KeyEvent event){
        carregarTabela("upper(ped_clinome) LIKE '%" + tfPesquisar.getText().toUpperCase() + "%'");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        coNome.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));
        coStatus.setCellValueFactory(new PropertyValueFactory<>("entregue"));

        coEndereco.setCellValueFactory(p ->
                new SimpleStringProperty(
                        (p.getValue().getLocal()  == null ? "" : p.getValue().getLocal()) + ((p.getValue().getNumero() == null || p.getValue().getNumero().isEmpty()) ? "" : ", " + p.getValue().getNumero())));

        coPreco.setCellValueFactory(p ->
                new SimpleStringProperty(String.format("%.2f", p.getValue().getTotal())));

        carregarTabela("");
    }

    private void carregarTabela(String filtroExtra){
        PedidoDAL dal = new PedidoDAL();

        String filtro = "ped_entregue = 'N'";

        if(filtroExtra != null && !filtroExtra.isEmpty()){
            filtro += " AND " + filtroExtra;
        }

        List<Pedido> lista = dal.get(filtro);
        tableView.setItems(FXCollections.observableArrayList(lista));
    }

    public void onAlterar(ActionEvent actionEvent){
        if(tableView.getSelectionModel().getSelectedItem() != null) {
            pedidoSelecionado = tableView.getSelectionModel().getSelectedItem();
            try {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("gerenciar-pedido-form-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("Alteração de Pedido");
                stage.setScene(scene);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.showAndWait();
                carregarTabela("");
            }catch (Exception e) {}
        }
    }

    public void onApagar(ActionEvent actionEvent){
        if(tableView.getSelectionModel().getSelectedItem() != null){
            new PedidoDAL().apagar(tableView.getSelectionModel().getSelectedItem());
            carregarTabela("");
        }
    }
}
