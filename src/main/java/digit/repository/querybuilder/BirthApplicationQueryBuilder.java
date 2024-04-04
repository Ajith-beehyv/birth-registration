package digit.repository.querybuilder;

import digit.web.models.BirthApplicationSearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class BirthApplicationQueryBuilder {

    private static final String BASE_QUERY = "SELECT " +
            "reg.id AS registration_id, reg.tenantid AS registration_tenant_id, " +
            "reg.applicationnumber AS registration_application_number, " +
            "reg.babyfirstname AS baby_first_name, reg.babylastname AS baby_last_name, " +
            "reg.fatherid AS father_id, reg.motherid AS mother_id, " +
            "reg.doctorname AS doctor_name, reg.hospitalmame AS hospital_name, " +
            "reg.placeofbirth AS place_of_birth, reg.timeofbirth AS time_of_birth, " +
            "reg.createdby AS registration_created_by, reg.lastmodifiedby AS registration_last_modified_by, " +
            "reg.createdtime AS registration_created_time, reg.lastmodifiedtime AS registration_last_modified_time, ";

    private static final String BIRTH_ADDRESS_QUERY = "appAddr.id AS app_address_id, appAddr.tenantId AS app_address_tenant_id, " +
            "appAddr.applicationnumber AS app_address_application_number, ";

    private static final String ADDRESS_QUERY = "addr.latitude AS address_latitude, addr.longitude AS address_longitude, " +
            "addr.buildingname AS address_building_name, addr.addressid AS address_id, " +
            "addr.addressnumber AS address_number, addr.addressline1 AS address_line1, " +
            "addr.addressline2 AS address_line2, addr.city AS address_city, " +
            "addr.pincode AS address_pincode, addr.detail AS address_detail, " +
            "addr.createdby AS address_created_by, addr.lastmodifiedby AS address_last_modified_by, " +
            "addr.createdtime AS address_created_time, addr.lastmodifiedtime AS address_last_modified_time ";

    private static final String FROM_TABLES = "FROM eg_bt_registration reg " +
            "LEFT JOIN eg_btr_app_address appAddr ON reg.applicationNumber = appAddr.applicationNumber " +
            "LEFT JOIN eg_bt_address addr ON appAddr.id = addr.addressId";

    private static final String ORDER_BY_CREATED_TIME = " ORDER BY reg.createdTime DESC ";

    public String getBirthApplicationSearchQuery(BirthApplicationSearchCriteria criteria, List<Object> preparedStmtList){
        StringBuilder query = new StringBuilder(BASE_QUERY);
        query.append(BIRTH_ADDRESS_QUERY);
        query.append(ADDRESS_QUERY);
        query.append(FROM_TABLES);

        if(!ObjectUtils.isEmpty(criteria.getTenantId())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" reg.tenantid = ? ");
            preparedStmtList.add(criteria.getTenantId());
        }
        if(!CollectionUtils.isEmpty(criteria.getIds())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" reg.id IN ( ").append(createQuery(criteria.getIds())).append(" ) ");
            addToPreparedStatement(preparedStmtList, criteria.getIds());
        }
        if(!ObjectUtils.isEmpty(criteria.getApplicationNumber())){
            addClauseIfRequired(query, preparedStmtList);
            query.append(" reg.applicationnumber = ? ");
            preparedStmtList.add(criteria.getApplicationNumber());
        }

        // order birth registration applications based on their createdtime in latest first manner
        query.append(ORDER_BY_CREATED_TIME);

        return query.toString();
    }

    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList){
        if(preparedStmtList.isEmpty()){
            query.append(" WHERE ");
        }else{
            query.append(" AND ");
        }
    }

    private String createQuery(List<String> ids) {
        StringBuilder builder = new StringBuilder();
        int length = ids.size();
        for (int i = 0; i < length; i++) {
            builder.append(" ?");
            if (i != length - 1)
                builder.append(",");
        }
        return builder.toString();
    }

    private void addToPreparedStatement(List<Object> preparedStmtList, List<String> ids) {
        ids.forEach(id -> {
            preparedStmtList.add(id);
        });
    }
}
