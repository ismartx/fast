package org.smartx.fast.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartx.fast.annotation.Privilege;
import org.smartx.fast.bean.ApiRequest;
import org.smartx.fast.bean.ApiResponse;
import org.smartx.fast.controller.ApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author kext
 */
@RestController
@RequestMapping("/demo")
public class DemoController extends ApiController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    public ApiResponse test(ApiRequest request) {
        logger.info(key);
        // get params from request
        String id = request.getId();
        logger.info(id);
        // response value
        return ApiResponse.ok().addValueToData("name", "kext");
    }

    @Privilege
    public ApiResponse testPrivilege(ApiRequest request) {
        return ApiResponse.ok();
    }
}
