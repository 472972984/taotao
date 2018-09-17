package com.taotao.rest.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface CartService {

	TaotaoResult getCartList(TbUser user);
	TaotaoResult setCartList(String cartListJson,TbUser user);
}
