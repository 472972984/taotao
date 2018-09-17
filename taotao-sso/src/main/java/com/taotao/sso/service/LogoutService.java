package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;

public interface LogoutService {

	TaotaoResult logout(String token);
}
