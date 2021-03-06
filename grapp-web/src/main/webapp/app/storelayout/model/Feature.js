(function() {
   "use strict";

   angular.module("App")
      .service("Feature", Feature);

   Feature.$inject = ["StoresRoot"];
   function Feature(StoresRoot) {
      var self = this;
      self.load = load;

      ////////////////////

      function load(storeLayoutRsc, feature) {
         return _.mergeLeft(new FeatureModel(storeLayoutRsc, feature), feature);
      }

      function FeatureModel(storeLayoutRsc, feature) {
         var self = this;
         self.setVertices = setVertices;
         self.delete = del;

         ////////////////////

         function setVertices(vertices) {
            return storeLayoutRsc.$put("reshapeFeature", {featureId: feature.id, polygon: _.stringifyVerticesIntoPolygon(vertices)})
               .then(function() {
                  self.polygon.vertices = vertices;
               });
         }

         function del() {
            return StoresRoot.deleteResource("feature", feature.id);
         }
      }
   }
})();