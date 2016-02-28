(function() {
   "use strict";

   angular.module("Grapp")
      .value("GrappMapControl", GrappMapControl);

   function GrappMapControl() {
      var self = this;
      self.getOutlineById = getOutlineById;
      self.setOutlinePolygon = setOutlinePolygon;
      self.addFeature = addFeature;
      self.removeFeatureById = removeFeatureById;
      self.applyToFeatures = applyToFeatures;
      self.mapClicked = mapClicked;
      self.polygonComplete = polygonComplete;
      self.polygonClicked = polygonClicked;
      self.polygonRightClicked = polygonRightClicked;
      self.polygonDragEnd = polygonDragEnd;
      self.setMapControl = setMapControl;
      self.setDrawingManagerControl = setDrawingManagerControl;
      self.setOutlineControl = setOutlineControl;
      self.setFeatureControl = setFeatureControl;
      self.setEventHandler = setEventHandler;
      self.setDrawingMode = setDrawingMode;

      var mapControl = null;
      var drawingManagerControl = null;
      var outlineControl = null;
      var featureControl = null;
      var eventHandler = null;

      ////////////////////

      function getOutlineById(outlineId) {
         return getOutlinePluralById(outlineId).gObject;
      }

      function setOutlinePolygon(outlineId, gMapPolygon) {
         var gMapPolygonPlural = getOutlinePluralById(outlineId);
         gMapPolygonPlural.gObject = gMapPolygon;
         gMapPolygon.setOptions({
            fillColor: gMapPolygonPlural.model.fill.color,
            zIndex: gMapPolygonPlural.model.zIndex,
            clickable: gMapPolygonPlural.model.clickable
         });
         gMapPolygon.setEditable(true);
         postProcessAddedGMapPolygon(outlineId, gMapPolygon);
      }

      function addFeature(featureId, gMapPolygon) {
         featureControl.getPlurals().put(featureId, {gObject: gMapPolygon});
         postProcessAddedGMapPolygon(featureId, gMapPolygon);
      }

      function removeFeatureById(featureId) {
         var featurePlurals = featureControl.getPlurals();
         var gMapPolygonPlural = featurePlurals.get(featureId);
         var gMapPolygon = gMapPolygonPlural.gObject;
         featurePlurals.remove(featureId);
         google.maps.event.clearInstanceListeners(gMapPolygon);
         gMapPolygon.setOptions({clickable: false});
         gMapPolygon.setMap(null);
      }

      function applyToFeatures(operator) {
         featureControl.getPlurals().values().forEach(function(plural) {
            operator(plural.gObject);
         });
      }

      function mapClicked(map, mouseEvent) {
         if (eventHandler && eventHandler.mapClicked) {
            eventHandler.mapClicked(map, mouseEvent);
         }
      }

      function polygonComplete(gMapPolygon) {
         // This is done to represent the fact that this
         // GMapPolygon is just a representation of something
         // to process. it is up to the handler to handle
         // its post-processing. For instance, if it just
         // needs to add this representation to the Map, it
         // should set the Map itself; however there won't be
         // any Events attached to it and no associated ID
         // for those Events. If this GMapPolygon is a
         // Feature, then it needs to be added like other
         // Features through GrappMapControl.addFeature() so
         // that Event Handlers are attached with appropriate
         // ID and it is added to the Plurals
         gMapPolygon.setMap(null);
         // -------------------------------------------------

         if (eventHandler && eventHandler.polygonComplete) {
            eventHandler.polygonComplete(gMapPolygon);
         }
      }

      function polygonClicked(modelId, gMapPolygon, polyMouseEvent) {
         if (eventHandler && eventHandler.polygonClicked) {
            eventHandler.polygonClicked(modelId, gMapPolygon, polyMouseEvent);
         }
      }

      function polygonRightClicked(modelId, gMapPolygon, polyMouseEvent) {
         if (gMapPolygon.getMap() && eventHandler && eventHandler.polygonRightClicked) {
            eventHandler.polygonRightClicked(modelId, gMapPolygon, polyMouseEvent);
         }
      }

      function polygonDragEnd(modelId, gMapPolygon, mouseEvent) {
         if (gMapPolygon.getMap() && eventHandler && eventHandler.polygonDragEnd) {
            eventHandler.polygonDragEnd(modelId, gMapPolygon, mouseEvent);
         }
      }

      function setMapControl(mc) {
         mapControl = mc;
      }

      function setDrawingManagerControl(dmc) {
         drawingManagerControl = dmc;
      }

      function setOutlineControl(oc) {
         outlineControl = oc;
      }

      function setFeatureControl(fc) {
         featureControl = fc;
      }

      function setEventHandler(eh) {
         if (eventHandler && eventHandler.finish) {
            eventHandler.finish();
         }

         eventHandler = eh;
         if (eventHandler && eventHandler.start) {
            eventHandler.start();
         }
      }

      function setDrawingMode(drawingMode) {
         var drawingManager = drawingManagerControl.getDrawingManager();
         if (drawingMode === "POLYGON") {
            drawingManager.setDrawingMode(google.maps.drawing.OverlayType.POLYGON);
         }
         else {
            drawingManager.setDrawingMode(null);
         }
      }

      function getOutlinePluralById(outlineId) {
         return outlineControl.getPlurals().get(outlineId);
      }

      function postProcessAddedGMapPolygon(modelId, gMapPolygon) {
         google.maps.event.clearInstanceListeners(gMapPolygon);
         google.maps.event.addListener(gMapPolygon, "click", function(polyMouseEvent) { self.polygonClicked(modelId, gMapPolygon, polyMouseEvent); });
         google.maps.event.addListener(gMapPolygon, "rightclick", function(polyMouseEvent) { self.polygonRightClicked(modelId, gMapPolygon, polyMouseEvent); });
         google.maps.event.addListener(gMapPolygon, "dragend", function(mouseEvent) { self.polygonDragEnd(modelId, gMapPolygon, mouseEvent); });
         gMapPolygon.setMap(mapControl.getGMap());
      }
   }
})();