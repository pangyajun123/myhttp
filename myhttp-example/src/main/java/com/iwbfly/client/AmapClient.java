package com.iwbfly.client;

import com.iwbfly.model.Coordinate;
import com.iwbfly.myhttp.annotation.Get;
import com.iwbfly.myhttp.annotation.MyhttpClient;
import com.iwbfly.myhttp.annotation.Query;

import java.util.Map;

/**
 * @auther: pangyajun
 * @create: 2021/12/1 12:41
 **/
@MyhttpClient(url = "${amapUrl}")
public interface AmapClient {

    @Get(url = "service/regeo?longitude=${0}&latitude=${1}")
    Map getLocation(String longitude, String latitude);

    @Get(url = "service/regeo")
    Map getLocationQuery(@Query("longitude") String longitude, @Query("latitude") String latitude);

    @Get(url = "service/regeo")
    Map getLocationQueryBody(@Query Coordinate coordinate );

}
