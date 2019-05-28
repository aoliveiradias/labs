package br.com.aline.labs.facebook;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "facebookClient", url = "https://graph.facebook.com")
public interface FacebookClient {

    @RequestMapping(method = RequestMethod.GET, value = "/{userId}?fields=id,name&access_token={accessToken}")
    UserFacebookResponse getUser(@PathVariable("userId") String userId, @PathVariable("accessToken") String accessToken);

}
