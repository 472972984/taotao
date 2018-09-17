package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.RegisterService;

/**
 * 数据校验Service
 * 
 * @author 陈汇奇
 *
 */
@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper userMapper;

	/**
	 * 通过调用userMapper查询数据库，是否存在。存在返回false， 不存在返回true
	 */
	@Override
	public TaotaoResult checkData(String param, Integer type) {

		// 创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();

		// 1 ， 2， 3 分别代表查询的字段为：username ， phone ， email
		if (1 == type) {
			criteria.andUsernameEqualTo(param);
		} else if (2 == type) {
			criteria.andPhoneEqualTo(param);
		} else if (3 == type) {
			criteria.andEmailEqualTo(param);
		}

		// 执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		// 结果不存在,数据可用 返回true
		if (list == null || list.isEmpty()) {
			return TaotaoResult.ok(true);
		}

		return TaotaoResult.ok(false);
	}

	/**
	 * 实现用户注册
	 */
	@Override
	public TaotaoResult register(TbUser user) {

		// 校验用户名密码不能为空
		if (StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPassword())) {
			return TaotaoResult.build(400, "用户名或密码不能为空");
		}

		// 校验数据是否重复
		// 校验用户名
		TaotaoResult result = checkData(user.getUsername(), 1);
		if (!(boolean) result.getData()) {
			return TaotaoResult.build(400, "账号已存在");
		}

		// 校验手机号码
		result = checkData(user.getPhone(), 2);
		if (!(boolean) result.getData()) {
			return TaotaoResult.build(400, "手机号已存在");
		}

		// 校验邮箱
		if (user.getEmail() != null) {

			result = checkData(user.getEmail(), 3);
			if (!(boolean) result.getData()) {
				return TaotaoResult.build(400, "邮箱已存在");
			}
		}

		// 允许用户注册
		user.setCreated(new Date());
		user.setUpdated(new Date());

		// 设置密码加密操作
		String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Password);

		// 执行插入操作
		userMapper.insert(user);

		return TaotaoResult.ok();
	}

}
