package com.wwq.bean;

import com.wwq.base.DefancePlant;
import com.wwq.utils.CommonUtils;

import org.cocos2d.actions.base.CCAction;


public class Nut extends DefancePlant {

	public Nut() {
		super("image/plant/nut/p_3_01.png");
		baseAction();
	}

	@Override
	public void baseAction() {
		CCAction animate = CommonUtils.getAnimate("image/plant/nut/p_3_%02d.png", 11, true);
		this.runAction(animate);
	}

}
