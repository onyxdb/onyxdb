//package com.onyxdb.mdb.controllers.v1;
//
//import java.util.List;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.onyxdb.mdb.core.zones.Zone;
//import com.onyxdb.mdb.core.zones.ZoneConverter;
//import com.onyxdb.mdb.core.zones.ZoneService;
//import com.onyxdb.mdb.generated.openapi.apis.V1ZonesApi;
//import com.onyxdb.mdb.generated.openapi.models.V1CreateZoneRequest;
//import com.onyxdb.mdb.generated.openapi.models.V1ListZonesResponse;
//import com.onyxdb.mdb.generated.openapi.models.V1UpdateZoneRequest;
//import com.onyxdb.mdb.generated.openapi.models.V1ZoneResponse;
//
///**
// * @author foxleren
// */
//@RestController
//public class V1ZoneController implements V1ZonesApi {
//    private final ZoneService zoneService;
//
//    public V1ZoneController(ZoneService zoneService) {
//        this.zoneService = zoneService;
//    }
//
//    @Override
//    public ResponseEntity<V1ListZonesResponse> listZones() {
//        List<Zone> zones = zoneService.list();
//        var response = ZoneConverter.toV1ListZonesResponse(zones);
//        return ResponseEntity.ok().body(response);
//    }
//
//    @Override
//    public ResponseEntity<V1ZoneResponse> getZone(String zoneId) {
//        Zone zone = zoneService.getOrThrow(zoneId);
//        var response = ZoneConverter.toV1ZoneResponse(zone);
//        return ResponseEntity.ok().body(response);
//    }
//
//    @Override
//    public ResponseEntity<Void> createZone(V1CreateZoneRequest v1CreateZoneRequest) {
//        Zone zone = ZoneConverter.fromV1CreateZoneRequest(v1CreateZoneRequest);
//        zoneService.create(zone);
//        return ResponseEntity.ok().build();
//    }
//
//    @Override
//    public ResponseEntity<Void> updateZone(String zoneId, V1UpdateZoneRequest v1UpdateZoneRequest) {
//        Zone zone = ZoneConverter.fromV1UpdateZoneRequest(
//                zoneId,
//                v1UpdateZoneRequest
//        );
//        zoneService.update(zone);
//        return ResponseEntity.ok().build();
//    }
//
//    @Override
//    public ResponseEntity<Void> deleteZone(String zoneId) {
//        zoneService.delete(zoneId);
//        return ResponseEntity.ok().build();
//    }
//}
