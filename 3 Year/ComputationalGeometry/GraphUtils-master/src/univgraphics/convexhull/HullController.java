package univgraphics.convexhull;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import univgraphics.common.GraphController;
import univgraphics.common.generators.PointGenerator;
import univgraphics.convexhull.hullers.*;
import univgraphics.common.generators.SimpleGenerator;

public class HullController extends GraphController{
    @FXML
    private Button generateSimplePolygonBtn;

    @FXML
    private Button generatePointsBtn;

    @FXML
    private Button preparataBtn;

    @FXML
    private Button simplePolygonHullBtn;

    @FXML
    private Canvas drawCanvas;

    @FXML
    private Button divideAndConquerBtn;

    @FXML
    private Button quickHullBtn;

    private boolean isSimplePolygon = false;

    @FXML
    void onGeneratePointsBtnClicked(ActionEvent event) {
        generator = new PointGenerator(0, 0,
                (int)drawCanvas.getWidth(), (int)drawCanvas.getHeight());
        generateAndDraw();
        isSimplePolygon = false;
    }

    @FXML
    void onGenerateSimplePolygonBtnClicked(ActionEvent event) {
        generator = new SimpleGenerator(0, 0,
                (int)drawCanvas.getWidth(), (int)drawCanvas.getHeight());
        generateAndDraw();
        isSimplePolygon = true;
    }

    @FXML
    void onQuickHullBtnClicked(ActionEvent event) {
        if (graph == null) return;
        gc.clearRect(0, 0, (int)drawCanvas.getWidth(), (int)drawCanvas.getHeight());
        drawGraph();
        drawRegion((new QuickHull(graph)).getRegion());
    }

    @FXML
    void onDivideAndConquerBtnClicked(ActionEvent event) {
        if (graph == null) return;
        gc.clearRect(0, 0, (int)drawCanvas.getWidth(), (int)drawCanvas.getHeight());
        drawGraph();
        drawRegion((new DivideAndConquer(graph)).getRegion());
    }

    @FXML
    void onSimplePolygonHullBtnClicked(ActionEvent event) {
        if (graph == null) return;
        if (isSimplePolygon) {
            gc.clearRect(0, 0, (int)drawCanvas.getWidth(), (int)drawCanvas.getHeight());
            drawGraph();
            drawRegion((new SimplePolygonHull(graph)).getRegion());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "There is no simple polygon",
                    ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    void onPreparataBtnClicked(ActionEvent event) {
        if (graph == null) return;
        gc.clearRect(0, 0, (int)drawCanvas.getWidth(), (int)drawCanvas.getHeight());
        drawGraph();
        drawRegion((new Preparata(graph)).getRegion());
    }
}
