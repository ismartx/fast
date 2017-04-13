package org.smartx.fast.demo.controller;

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

    public ApiResponse method1(ApiRequest request) {
        return ApiResponse.ok();
    }
}
