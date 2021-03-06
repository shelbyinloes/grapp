(function() {
   "use strict";

   angular.module("App")
      .service("ShoppingNode", ShoppingNode);

   ShoppingNode.$inject = ["NodeType", "ShoppingItemType"];
   function ShoppingNode(NodeType, ShoppingItemType) {
      var self = this;
      self.load = load;

      ////////////////////

      function load(shoppingNode) {
         return _.mergeLeft(new ShoppingNodeModel(shoppingNode), shoppingNode);
      }

      function ShoppingNodeModel(shoppingNode) {
         var self = this;
         self.type = NodeType[shoppingNode.type];
         self.items = _.fromPairs(shoppingNode.items.map(function(shoppingItem) { return [shoppingItem.item.code, createModelForShoppingItem(shoppingItem)]; }));
         self.getItems = function() { return _.values(self.items); };
         self.hasItems = function() { return shoppingNode.items.length > 0; };

         ////////////////////

         function createModelForShoppingItem(shoppingItem) {
            return {
               item: shoppingItem.item,
               type: ShoppingItemType[shoppingItem.type],
               obtained: false
            };
         }
      }
   }
})();