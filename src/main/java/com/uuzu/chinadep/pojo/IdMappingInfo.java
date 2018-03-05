package com.uuzu.chinadep.pojo;

import com.uuzu.common.pojo.DmpResponse;
import lombok.Data;

@Data
public class IdMappingInfo extends DmpResponse {
    private String mapId;
    private String outIdType;
    private String extraId;
    private String userId;
}
