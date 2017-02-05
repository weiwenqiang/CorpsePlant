package com.wwq.cclayer;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGSize;

/**
 * Created by 魏文强 on 2017/1/27.
 */
public class BaseLayer extends CCLayer {
    protected CGSize winSize;

    public BaseLayer() {
        winSize = CCDirector.sharedDirector().getWinSize();
    }
}
