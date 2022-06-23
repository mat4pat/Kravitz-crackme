package root.postlogin.puzzle.memory;

import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import root.postlogin.puzzle.Binary;

public class MemoryTable {

    private static TableView<Binary> tableView;
    public static TableView<Binary> getTableView() { return tableView; }

    private static boolean hasBeenSetup=false;

    public static void setupTableView() {
        tableView = new TableView();
        TableColumn<Binary, Integer> indexColumn = new TableColumn<>();
        TableColumn<Binary, Integer> firstColumn = new TableColumn<>();
        TableColumn<Binary, Integer> secondColumn = new TableColumn<>();
        TableColumn<Binary, Integer> thirdColumn = new TableColumn<>();
        TableColumn<Binary, Integer> fourthColumn = new TableColumn<>();
        TableColumn<Binary, Integer> fifthColumn = new TableColumn<>();
        TableColumn<Binary, Integer> sixthColumn = new TableColumn<>();
        TableColumn<Binary, Integer> seventhColumn = new TableColumn<>();
        TableColumn<Binary, Integer> eighthColumn = new TableColumn<>();

        indexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("a"));
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("b"));
        thirdColumn.setCellValueFactory(new PropertyValueFactory<>("c"));
        fourthColumn.setCellValueFactory(new PropertyValueFactory<>("d"));
        fifthColumn.setCellValueFactory(new PropertyValueFactory<>("e"));
        sixthColumn.setCellValueFactory(new PropertyValueFactory<>("f"));
        seventhColumn.setCellValueFactory(new PropertyValueFactory<>("g"));
        eighthColumn.setCellValueFactory(new PropertyValueFactory<>("h"));

        indexColumn.setId("indexcolumn");

        indexColumn.setPrefWidth(66.66);
        firstColumn.setPrefWidth(66.66);
        secondColumn.setPrefWidth(66.66);
        thirdColumn.setPrefWidth(66.66);
        fourthColumn.setPrefWidth(66.66);
        fifthColumn.setPrefWidth(66.66);
        sixthColumn.setPrefWidth(66.66);
        seventhColumn.setPrefWidth(66.66);
        eighthColumn.setPrefWidth(66.66);

        indexColumn.setResizable(false);
        firstColumn.setResizable(false);
        secondColumn.setResizable(false);
        thirdColumn.setResizable(false);
        fourthColumn.setResizable(false);
        fifthColumn.setResizable(false);
        sixthColumn.setResizable(false);
        seventhColumn.setResizable(false);
        eighthColumn.setResizable(false);


        tableView.getColumns().addAll(indexColumn, firstColumn, secondColumn, thirdColumn, fourthColumn, fifthColumn, sixthColumn, seventhColumn, eighthColumn);

        Binary[] binaryList = new Binary[10];
        binaryList[0] = new Binary(0,0,1,1,0,1,0,1,1);
        binaryList[1] = new Binary(1,1,0,0,0,0,0,0,0);
        binaryList[2] = new Binary(2,1,0,0,1,1,0,1,1);
        binaryList[3] = new Binary(3,0,1,1,1,0,0,1,0);
        binaryList[4] = new Binary(4,1,1,1,0,1,1,1,0);
        binaryList[5] = new Binary(5,0,0,1,1,1,1,1,1);
        binaryList[6] = new Binary(6,1,1,1,0,0,0,1,1);
        binaryList[7] = new Binary(7,0,1,0,1,1,0,1,0);
        binaryList[8] = new Binary(8,1,1,0,0,1,1,0,1);
        binaryList[9] = new Binary(9,0,1,1,0,1,1,0,1);

        for (Binary binaryItem : binaryList) {
            tableView.getItems().add(binaryItem);
        }

        hasBeenSetup=true;
    }

    private static Stage memoryTableWindow;

    public static Stage getMemoryTableWindow() {
        return memoryTableWindow; // to close when time isn't correct.
    }

    private static StackPane stackPane;
    private static Scene scene;

    public static void displayMemoryTable() {

        if (!hasBeenSetup) {

            memoryTableWindow = new Stage();

            stackPane = new StackPane();
            MemoryTable.setupTableView();
            stackPane.getChildren().add(tableView);

            scene = new Scene(stackPane, 605, 243);

            scene.getStylesheets().add("root/postlogin/puzzle/memory/table.css");
            tableView.getStyleClass().add("noheader");

            memoryTableWindow.setTitle("Computer Memory");
            memoryTableWindow.setScene(scene);
            memoryTableWindow.setResizable(false);
        } else {
            // do nothing, it's already setup
        }

        if (!memoryTableWindow.isShowing()) {
            memoryTableWindow.show();
        } else {
            memoryTableWindow.requestFocus();
        }

    }

}
