package com.wwq.bean;

import com.wwq.base.AttackPlant;
import com.wwq.base.Bullet;
import com.wwq.utils.CommonUtils;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.nodes.CCNode;


public class PeasePlant extends AttackPlant {

	public PeasePlant() {
		super("image/plant/pease/p_2_01.png");
		baseAction();
	}

	@Override
	public Bullet createBullet() {
		if(bullets.size()<1){// ֤��֮ǰû�д����ӵ� 
			final Pease pease=new Pease();
			pease.setPosition(CCNode.ccp(this.getPosition().x+20, this.getPosition().y+40));
			this.getParent().addChild(pease);
			pease.setDieListener(new DieListener() {
				
				@Override
				public void die() {
					 bullets.remove(pease);
				}
			});
			bullets.add(pease);
			
			pease.move();
		}
		return null;
	}

	@Override
	public void baseAction() {
		CCAction animate = CommonUtils.getAnimate("image/plant/pease/p_2_%02d.png", 8, true);
		this.runAction(animate);
	}

}
