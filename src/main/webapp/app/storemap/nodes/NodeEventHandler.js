(function() {
   "use strict";

   angular.module("Grapp")
      .factory("NodeEventHandler", NodeEventHandler);

   NodeEventHandler.$inject = ["BaseEventHandler"];
   function NodeEventHandler(BaseEventHandler) {
      return function(mapControl, grappStoreLayout, nodeSelector, grappStoreNodeType) {
         var self = angular.extend(this, new BaseEventHandler(mapControl, grappStoreLayout));
         self.finish = finish;
         self.mapClicked = mapClicked;
         self.markerClicked = markerClicked;
         self.markerRightClicked = markerRightClicked;
         self.setNodeType = setNodeType;

         var nodeType = grappStoreNodeType;

         ////////////////////

         function finish() {
            nodeSelector.deselect()
         }

         function mapClicked(modelId, map, mouseEvent) {
            grappStoreLayout.addNode(nodeType, _.convertPositionToLocation(mouseEvent.latLng))
               .then(function(result) {
                  mapControl.addNode(result.node.id, createGMapMarker(map.markerOptions, mouseEvent.latLng));
                  if (result.affectedNodes) {
                     result.affectedNodes.forEach(function(affectedNode) {
                        mapControl.setNodeOptions(affectedNode.id, {
                           icon: affectedNode.type.iconUrl
                        });
                     });
                  }
               });
         }

         function markerClicked(modelId, gMapMarker, mouseEvent) {
            nodeSelector.select(grappStoreLayout.getNodeById(modelId));
         }

         function markerRightClicked(modelId, gMapMarker, mouseEvent) {
            grappStoreLayout.removeNodeById(modelId);
            mapControl.removeNodeById(modelId);
         }

         function setNodeType(grappStoreNodeType) {
            nodeType = grappStoreNodeType;
         }

         function createGMapMarker(options, position) {
            return new google.maps.Marker(_.merge({position: position, icon: nodeType.iconUrl}, options));
         }
      };
   }
})();