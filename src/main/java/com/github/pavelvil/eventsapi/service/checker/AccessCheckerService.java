package com.github.pavelvil.eventsapi.service.checker;

import com.github.pavelvil.eventsapi.aop.AccessCheckType;
import com.github.pavelvil.eventsapi.aop.Accessible;
import jakarta.servlet.http.HttpServletRequest;

public interface AccessCheckerService {

    boolean check(HttpServletRequest request, Accessible accessible);

    AccessCheckType getType();

}
