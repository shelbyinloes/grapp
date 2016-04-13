package com.wisegas.grapp.storemanagement.domain.entity;

import com.wisegas.common.lang.value.GeoPoint;
import com.wisegas.common.lang.value.GeoPolygon;
import com.wisegas.common.persistence.jpa.converter.GeoPolygonConverter;
import com.wisegas.common.persistence.jpa.entity.SimpleEntity;
import com.wisegas.grapp.storemanagement.domain.value.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Layout extends SimpleEntity<LayoutId> {
   @EmbeddedId
   private LayoutId id;

   @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, optional = false)
   private Store store;

   @Column(length = 2047)
   @Convert(converter = GeoPolygonConverter.class)
   private GeoPolygon outerOutline;

   @Column(length = 2047)
   @Convert(converter = GeoPolygonConverter.class)
   private GeoPolygon innerOutline;

   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "layout", orphanRemoval = true)
   @MapKey(name = "id")
   private Map<FeatureId, Feature> features = new HashMap<>();

   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "layout", orphanRemoval = true)
   @MapKey(name = "id")
   private Map<NodeId, Node> nodes = new HashMap<>();

   public Layout(Store store) {
      id = LayoutId.generate();
      setStore(store);
   }

   protected Layout() {

   }

   @Override
   public LayoutId getId() {
      return id;
   }

   public Store getStore() {
      return store;
   }

   public GeoPolygon getOuterOutline() {
      return outerOutline;
   }

   public void setOuterOutline(GeoPolygon outerPolygon) {
      this.outerOutline = outerPolygon;
   }

   public GeoPolygon getInnerOutline() {
      return innerOutline;
   }

   public void setInnerOutline(GeoPolygon innerPolygon) {
      this.innerOutline = innerPolygon;
   }

   public Collection<Feature> getFeatures() {
      return features.values();
   }

   public Feature reshapeFeature(FeatureId featureId, GeoPolygon polygon) {
      Feature feature = requireFeatureExistence(featureId);
      feature.setPolygon(polygon);
      return feature;
   }

   public Feature addFeature(GeoPolygon polygon) {
      Feature feature = new Feature(this, polygon);
      features.put(feature.getId(), feature);
      return feature;
   }

   public void removeFeature(FeatureId featureId) {
      requireFeatureExistence(featureId);
      features.remove(featureId);
   }

   public Node getNode(NodeId nodeId) {
      return nodes.get(nodeId);
   }

   public Collection<Node> getNodes() {
      return nodes.values();
   }

   public Node moveNode(NodeId nodeId, GeoPoint location) {
      Node node = requireNodeExistence(nodeId);
      node.setLocation(location);
      return node;
   }

   public Node addNode(NodeType type, GeoPoint location) {
      if (type.isSingleton()) {
         ensureNoNodesOfType(type);
      }
      Node node = new Node(this, "Node #" + nodes.size(), type, location);
      nodes.put(node.getId(), node);
      return node;
   }

   public void removeNode(NodeId nodeId) {
      requireNodeExistence(nodeId);
      nodes.remove(nodeId);
   }

   public NodeItem addNodeItem(NodeId nodeId, Item item) {
      Node node = requireNodeExistence(nodeId);
      ensureNodeItemUniqueness(node, item);
      return node.addItem(item);
   }

   private Feature requireFeatureExistence(FeatureId featureId) {
      return Objects.requireNonNull(features.get(featureId), () -> String.format("Feature (%s) not found in Layout (%s).", featureId, getId()));
   }

   private Node requireNodeExistence(NodeId nodeId) {
      return Objects.requireNonNull(nodes.get(nodeId), () -> String.format("Node (%s) not found in Layout (%s).", nodeId, getId()));
   }

   private void ensureNoNodesOfType(NodeType type) {
      for (Node node : nodes.values()) {
         if (node.getType() == type) {
            node.setType(NodeType.defaultNonSingleton());
         }
      }
   }

   private void ensureNodeItemUniqueness(Node node, Item item) {
      Optional<Node> foundNode = findNodeByItem(item);
      if (foundNode.isPresent() && !Objects.equals(node, foundNode.get())) {
         foundNode.get().removeItem(item);
      }
   }

   private Optional<Node> findNodeByItem(Item item) {
      return nodes.values().stream().filter(node -> node.containsItem(item)).findAny();
   }

   private void setStore(Store store) {
      this.store = store;
   }
}
