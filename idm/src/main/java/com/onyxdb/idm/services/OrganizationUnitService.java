package com.onyxdb.idm.services;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.OrganizationUnitTable;
import com.onyxdb.idm.models.OrganizationUnit;
import com.onyxdb.idm.repositories.OrganizationUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationUnitService {

 private final OrganizationUnitRepository organizationUnitRepository;
 private final OrganizationUnitTable organizationUnitTable = Tables.ORGANIZATION_UNIT_TABLE;

 public Optional<OrganizationUnit> findById(UUID id) {
  return organizationUnitRepository.findById(id);
 }

 public List<OrganizationUnit> findAll() {
  return organizationUnitRepository.findAll();
 }

 public List<OrganizationUnit> findByDomainComponentId(UUID domainComponentId) {
  return organizationUnitRepository.findByDomainComponentId(domainComponentId);
 }

 @Transactional
 public void create(OrganizationUnit organizationUnit) {
  organizationUnitRepository.create(organizationUnit);
 }

 @Transactional
 public void update(OrganizationUnit organizationUnit) {
  organizationUnitRepository.update(organizationUnit);
 }

 @Transactional
 public void delete(UUID id) {
  organizationUnitRepository.delete(id);
 }
}