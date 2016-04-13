package com.wisegas.grapp.storemanagement.service_impl.factory;

import com.wisegas.grapp.storemanagement.domain.entity.Node;
import com.wisegas.grapp.storemanagement.service.dto.NodeDto;

public final class NodeDtoFactory {

   public static NodeDto createDto(Node node) {
      NodeDto nodeDto = new NodeDto();
      nodeDto.setId(node.getId().toString());
      nodeDto.setName(node.getName());
      nodeDto.setType(node.getType().name());
      nodeDto.setLocation(node.getLocation());
      return nodeDto;
   }

   private NodeDtoFactory() {

   }
}