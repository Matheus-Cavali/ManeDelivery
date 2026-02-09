package unoeste.fipp.manedeliveryfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import unoeste.fipp.manedeliveryfx.db.dals.PedidoDAL;
import unoeste.fipp.manedeliveryfx.db.entidades.Pedido;
import unoeste.fipp.manedeliveryfx.reports.PedidoReports;

import java.time.LocalDate;
import java.util.List;

public class MenuController {

    @FXML
    void onCadCategorias(ActionEvent event){
        try{
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("Categoria-table-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Cadastro de Categorias");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        }catch (Exception e){}
    }

    @FXML
    void onCadMarcas(ActionEvent event){
        try{
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("marca-table-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Cadastro de Marcas");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        }catch (Exception e){}
    }

    @FXML
    void onCadProdutos(ActionEvent event){
       try{
           Stage stage = new Stage();
           FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("produtos-table-view.fxml"));
           Scene scene = new Scene(fxmlLoader.load());
           stage.setTitle("Cadastro de Produtos");
           stage.setScene(scene);
           stage.initModality(Modality.WINDOW_MODAL);
           stage.showAndWait();
       }catch (Exception e){}
    }

    @FXML
    void onCadTiposPagamento(ActionEvent event){
        try{
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("tipos-pagamento-table-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Tipos de Pagamento");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();
        }catch (Exception e){}
    }

    @FXML
    void onFechar(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Deseja finalizar a aplicação?");
        if(alert.showAndWait().get() == ButtonType.OK)
            Platform.exit();
    }

    @FXML
    void onGerenciarPedido(ActionEvent event){
        try{
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("gerenciar-pedido-table-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Gerenciamento dos Pedidos");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }catch (Exception e){}
    }

    @FXML
    void onNovoPedido(ActionEvent event){
        try{
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("novo-pedido-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Novo Pedido");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }catch (Exception e){}
    }

    @FXML
    void onSobre(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Desenvolvido por Matheus Cavali\nCom a ajuda do professor");
        alert.setHeaderText("Mane Delivery");
        alert.showAndWait();
    }

    public void onRelatórioPedido(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Relatorio-data-form.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Período do relatório de pedidos");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));

            RelatorioController controller = loader.getController();

            stage.showAndWait();

            if (controller.isConfirmado()) {
                LocalDate dataIni = controller.getDataInicio();
                LocalDate dataFim = controller.getDataFim();

                String filtro = String.format(
                        "ped_data BETWEEN '%s' AND '%s' ORDER BY ped_data", dataIni.toString(), dataFim.toString());
                PedidoDAL dal = new PedidoDAL();
                List<Pedido> pedidoList = dal.get(filtro);
                PedidoReports.relPedidos(pedidoList);
            }
        } catch (Exception e) {}
    }

}
