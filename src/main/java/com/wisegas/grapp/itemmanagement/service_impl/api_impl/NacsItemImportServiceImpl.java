package com.wisegas.grapp.itemmanagement.service_impl.api_impl;

import com.wisegas.common.lang.annotation.ApplicationService;
import com.wisegas.common.lang.annotation.Transactional;
import com.wisegas.grapp.itemmanagement.domain.entity.GrappItem;
import com.wisegas.grapp.itemmanagement.domain.service.GrappItemImportService;
import com.wisegas.grapp.itemmanagement.domain.value.GrappItemCode;
import com.wisegas.grapp.itemmanagement.domain.value.GrappItemCodeType;
import com.wisegas.grapp.itemmanagement.service.api.NacsItemImportService;
import com.wisegas.grapp.itemmanagement.service.dto.GrappItemDto;
import com.wisegas.grapp.itemmanagement.service_impl.factory.GrappItemDtoFactory;
import com.wisegas.grapp.itemmanagement.service_impl.util.NacsItemCsvParser;
import com.wisegas.grapp.itemmanagement.service_impl.value.NacsId;
import com.wisegas.grapp.itemmanagement.service_impl.value.NacsItem;
import com.wisegas.grapp.itemmanagement.service_impl.value.NacsItemType;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Named
@Singleton
@Transactional
@ApplicationService
public class NacsItemImportServiceImpl implements NacsItemImportService {

   private final GrappItemImportService grappItemImportService;

   @Inject
   public NacsItemImportServiceImpl(GrappItemImportService grappItemImportService) {
      this.grappItemImportService = grappItemImportService;
   }

   @Override
   public List<GrappItemDto> importCsvItems(String nacsItemCsvData) {
      Map<NacsItemType, List<NacsItem>> nacsItemsByType = NacsItemCsvParser.parse(nacsItemCsvData).stream().collect(groupingBy(NacsItem::getType));
      List<GrappItem> categoryGrappItems = importNacsItems(nacsItemsByType.getOrDefault(NacsItemType.CATEGORY, emptyList()), this::importNacsItemAsGeneralItem);
      List<GrappItem> subCategoryGrappItems = importNacsItems(nacsItemsByType.getOrDefault(NacsItemType.SUB_CATEGORY, emptyList()), this::importNacsItemAsSubItem);
      List<GrappItem> subItemGrappItems = importNacsItems(nacsItemsByType.getOrDefault(NacsItemType.ITEM, emptyList()), this::importNacsItemAsSubItem);
      return Stream.of(categoryGrappItems, subCategoryGrappItems, subItemGrappItems)
                   .flatMap(Collection::stream)
                   .map(GrappItemDtoFactory::createDto)
                   .collect(toList());
   }

   private List<GrappItem> importNacsItems(List<NacsItem> nacsItems, Function<NacsItem, Optional<GrappItem>> nacsGrappItemConverter) {
      return nacsItems.stream()
                      .map(nacsGrappItemConverter)
                      .filter(Optional::isPresent)
                      .map(Optional::get)
                      .collect(toList());
   }

   private Optional<GrappItem> importNacsItemAsGeneralItem(NacsItem nacsItem) {
      GrappItemCode code = nacsIdToGrappItemCode(nacsItem.getId());
      Optional<GrappItem> createdGeneralItem = tryToImportItemAsGeneralItem(code, nacsItem.getName());
      createdGeneralItem.ifPresent(generalItem -> nacsItem.getSubItems().forEach(subItemName -> tryToImportItemAsSubItem(code, generateRandomNacsChildCode(nacsItem.getId()), subItemName)));
      return createdGeneralItem;
   }

   private Optional<GrappItem> importNacsItemAsSubItem(NacsItem nacsItem) {
      GrappItemCode code = nacsIdToGrappItemCode(nacsItem.getId());
      Optional<GrappItem> createdSubItem = tryToImportItemAsSubItem(nacsIdToGrappItemCode(nacsItem.getParentId()), code, nacsItem.getName());
      createdSubItem.ifPresent(subItem -> nacsItem.getSubItems().forEach(subItemName -> tryToImportItemAsSubItem(code, generateRandomNacsChildCode(nacsItem.getId()), subItemName)));
      return createdSubItem;
   }

   private GrappItemCode nacsIdToGrappItemCode(NacsId nacsId) {
      return new GrappItemCode(GrappItemCodeType.NACS, nacsId.toString(GrappItemCodeType.NACS.getValueFormat()));
   }

   private Optional<GrappItem> tryToImportItemAsGeneralItem(GrappItemCode code, String name) {
      try {
         return Optional.of(grappItemImportService.importGeneralItem(code, name));
      }
      catch (Exception e) {
         return Optional.empty();
      }
   }

   private Optional<GrappItem> tryToImportItemAsSubItem(GrappItemCode superCode, GrappItemCode code, String name) {
      try {
         return Optional.of(grappItemImportService.importSubItem(superCode, code, name));
      }
      catch (Exception e) {
         return Optional.empty();
      }
   }

   private GrappItemCode generateRandomNacsChildCode(NacsId parentCode) {
      String randomSuffix = String.format("%02d", (int)(Math.random() * 100));
      return new GrappItemCode(GrappItemCodeType.RANDOM, parentCode.toString(GrappItemCodeType.NACS.getValueFormat()) + randomSuffix);
   }
}
