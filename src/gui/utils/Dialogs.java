/**
 *
 */
package gui.utils;

import gui.application.EtoATool;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author AAG
 *
 */
public class Dialogs
{
  private static final String DEFAULT_DIALOG_CSS = "dialogs.css";
  private static final String DEFAULT_DIALOG_STYLE = "dialog";

  private static final String DIALOG_CSS = EtoATool.getCSSFolder() + DEFAULT_DIALOG_CSS;

  public static TextInputDialog getNameDialog(final String itemType)
  {
    final TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("EtoA-Tool");
    dialog.setHeaderText("Neues " + itemType + " hinzufügen?");
    dialog.setContentText("Name:");
    final DialogPane dialogPane = dialog.getDialogPane();
    styleDialog(dialogPane);
    final TextField content = (TextField) ((GridPane) dialogPane.getContent()).getChildren().get(1);
    final Button btOk = (Button) dialogPane.lookupButton(ButtonType.OK);
    btOk.addEventFilter(ActionEvent.ACTION, e ->
    {
      if (content.getText().isEmpty())
      {
        if (!content.getStyleClass().contains("error"))
        {
          content.getStyleClass().add("error");
        }
        e.consume();
      }
    });
    return dialog;
  }

  public static Alert buildAlert(final AlertType alertType, final String title, final String headerText, final String contentText)
  {
    final Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(headerText);
    alert.setContentText(contentText);
    final DialogPane dialogPane = alert.getDialogPane();
    styleDialog(dialogPane);
    return alert;
  }

  public static void styleDialog(final DialogPane dialogPane)
  {
    dialogPane.getStylesheets().add(Dialogs.class.getResource(DIALOG_CSS).toExternalForm());
    dialogPane.getStyleClass().addAll(DEFAULT_DIALOG_STYLE);
    ((Stage) dialogPane.getScene().getWindow()).getIcons().add(new Image(EtoATool.getGameIconPath()));
  }
}
