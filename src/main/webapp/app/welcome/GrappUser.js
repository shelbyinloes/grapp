(function() {
   "use strict";

   angular.module("Grapp")
      .service("GrappUser", GrappUser);

   GrappUser.$inject = ["Root"];
   function GrappUser(Root) {
      var self = this;
      self.loadByID = loadByID;
      self.load = load;

      ////////////////////

      function loadByID(grappUserID) {
         return Root.loadResourceModelByID("user", grappUserID, createModel);
      }

      function load(grappUserRsc) {
         return Root.mergeResourceIntoModel(grappUserRsc, createModel(grappUserRsc));
      }

      function createModel(grappUserRsc) {
         return new GrappUserModel(grappUserRsc);
      }

      function GrappUserModel(grappUserRsc) {
         var self = this;
         self.updateDisplayName = updateDisplayName;

         ////////////////////

         function updateDisplayName(displayName) {
            return grappUserRsc.$put("self", {name: displayName})
               .then(function() { self.name = displayName; });
         }
      }
   }
})();