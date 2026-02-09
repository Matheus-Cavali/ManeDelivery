package unoeste.fipp.manedeliveryfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unoeste.fipp.manedeliveryfx.db.util.SingletonDB;

import javax.swing.*;
import java.io.IOException;

public class ManeDeliveryFX extends Application {
    @Override
    public void start(Stage stage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mané Delivery FX");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args){
        if(SingletonDB.conectar())
            launch();
        else {
            JOptionPane.showMessageDialog(null,"Erro ao conectar o banco\n"+
                    SingletonDB.getConexao().getMensagemErro());
            Platform.exit();
        }
    }
}