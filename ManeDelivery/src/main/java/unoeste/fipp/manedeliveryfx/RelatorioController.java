package unoeste.fipp.manedeliveryfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RelatorioController {

    @FXML
    private DatePicker dpDataInicio;

    @FXML
    private DatePicker dpDataFim;

    private boolean confirmado = false;

    @FXML
    private void initialize(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        StringConverter<LocalDate> converter = new StringConverter<>(){
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return formatter.format(date);
                }
                return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if(string == null || string.trim().isEmpty()) {
                    return null;
                }
                try {
                    return LocalDate.parse(string.trim(), formatter);
                }catch (DateTimeParseException e) {
                    return null;
                }
            }
        };

        dpDataInicio.setConverter(converter);
        dpDataFim.setConverter(converter);

        dpDataInicio.setPromptText("dd/mm/aaaa");
        dpDataFim.setPromptText("dd/mm/aaaa");
    }

    private void commitDatePicker(DatePicker dp){
        String txt = dp.getEditor().getText();
        if(txt != null && !txt.trim().isEmpty()){
            LocalDate data = dp.getConverter().fromString(txt.trim());
            dp.setValue(data);
        }
        else{
            dp.setValue(null);
        }
    }

    @FXML
    private void onConfirmar(ActionEvent event){
        commitDatePicker(dpDataInicio);
        commitDatePicker(dpDataFim);

        LocalDate ini = dpDataInicio.getValue();
        LocalDate fim = dpDataFim.getValue();

        boolean ok = true;
        String msgErro = null;

        if(ini == null || fim == null){
            msgErro = "Informe a data inicial e a data final no formato dd/mm/aaaa.";
            ok = false;
        }
        else if(fim.isBefore(ini)){
            msgErro = "A data final não pode ser menor que a data inicial.";
            ok = false;
        }

        if(!ok){
            new Alert(Alert.AlertType.WARNING, msgErro, ButtonType.OK).showAndWait();
            confirmado = false;
        }
        else{
            confirmado = true;
            fecharJanela(event);
        }
    }

    @FXML
    private void onCancelar(ActionEvent event){
        confirmado = false;
        fecharJanela(event);
    }

    private void fecharJanela(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public boolean isConfirmado(){
        return confirmado;
    }

    public LocalDate getDataInicio(){
        return dpDataInicio.getValue();
    }

    public LocalDate getDataFim(){
        return dpDataFim.getValue();
    }
}