package com.gtp.tradeapp.rest;

import com.gtp.tradeapp.domain.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/token")
public class TokenController {

    @Autowired
    private ConsumerTokenServices defaultTokenServices;

    @RequestMapping(value = "/revoke", method = RequestMethod.POST)
    @ResponseBody
    public Status revokeToken(@RequestHeader("Authorization") final String authorization) {
        String token = getToken(authorization);
        if (defaultTokenServices.revokeToken(token)) {
            return new Status(1, "Token Removed");
        }
        return new Status(0, "Token Not found");
    }

    private String getToken(String authorization) {
        int lastIndexOf = authorization.lastIndexOf(' ');
        return authorization.substring(lastIndexOf + 1);
    }
}
