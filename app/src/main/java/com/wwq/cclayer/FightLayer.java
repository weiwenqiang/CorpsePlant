package com.wwq.cclayer;

import android.view.MotionEvent;

import com.wwq.bean.ShowPlant;
import com.wwq.bean.ShowZombies;
import com.wwq.utils.CommonUtils;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by 魏文强 on 2017/2/4.
 */
public class FightLayer extends BaseLayer {
    private CCTMXTiledMap map;
    private List<CGPoint> zombilesPoints;
    private CCSprite choose; // 玩家可选植物的容器
    private CCSprite chose; // 玩家已选植物的容器

    private List<ShowPlant> showPlatns; // 展示用的植物的集合
    private List<ShowPlant> selectPlants = new CopyOnWriteArrayList<ShowPlant>();// 已经选中植物的集合
    private boolean isLock;

    public FightLayer() {
        init();
    }

    private void init() {
        loadMap();
        parserMap();
        showZombies();
        moveMap();
    }

    private void loadMap() {
        map = CCTMXTiledMap.tiledMap("image/fight/map_day.tmx");
        map.setAnchorPoint(0.5f, 0.5f);
        CGSize contentSize = map.getContentSize();
        map.setPosition(contentSize.width / 2, contentSize.height / 2);
        this.addChild(map);
    }

    // 移动地图
    private void moveMap() {
        int x = (int) (winSize.width - map.getContentSize().width);
        CCMoveBy moveBy = CCMoveBy.action(3, ccp(x, 0));
        CCSequence sequence = CCSequence
                .actions(CCDelayTime.action(4), moveBy, CCDelayTime.action(2),
                        CCCallFunc.action(this, "loadContainer"));
        map.runAction(sequence);
    }

    private void parserMap() {
        zombilesPoints = CommonUtils.getMapPoints(map, "zombies");
    }

    // 加载展示用的僵尸
    private void showZombies() {
        for (int i = 0; i < zombilesPoints.size(); i++) {
            CGPoint cgPoint = zombilesPoints.get(i);
            ShowZombies showZombies = new ShowZombies();
            showZombies.setPosition(cgPoint);// 给展示用的僵尸设置了位置
            map.addChild(showZombies);// 注意: 把僵尸加载到地图上
        }
    }

    // 加载两个容器
    public void loadContainer() {
        chose = CCSprite.sprite("image/fight/chose/fight_chose.png");
        chose.setAnchorPoint(0, 1);
        chose.setPosition(0, winSize.height);// 设置位置是屏幕的左上角
        this.addChild(chose);

        choose = CCSprite.sprite("image/fight/chose/fight_choose.png");
        choose.setAnchorPoint(0, 0);
        this.addChild(choose);

        loadShowPlant();
    }

    // 加载展示用的植物
    private void loadShowPlant() {
        showPlatns = new ArrayList<ShowPlant>();
        for (int i = 1; i <= 9; i++) {
            ShowPlant plant = new ShowPlant(i); // 创建了展示的植物

            CCSprite bgSprite = plant.getBgSprite();
            bgSprite.setPosition(16 + ((i - 1) % 4) * 54,
                    175 - ((i - 1) / 4) * 59);
            choose.addChild(bgSprite);

            CCSprite showSprite = plant.getShowSprite();// 获取到了展示的精灵
            // 设置坐标
            showSprite.setPosition(16 + ((i - 1) % 4) * 54,
                    175 - ((i - 1) / 4) * 59);
            choose.addChild(showSprite); // 添加到了容器上

            showPlatns.add(plant);
        }
        setIsTouchEnabled(true);
    }

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        // 需要把Android坐标系中的点 转换成Cocos2d坐标系中的点
        CGPoint cgPoint = this.convertTouchToNodeSpace(event);
        CGRect boundingBox = choose.getBoundingBox();
        CGRect choseBox2 = chose.getBoundingBox();

        if (CGRect.containsPoint(choseBox2, cgPoint)) {
            for (ShowPlant plant : selectPlants) {
                CGRect selectPlantBox1 = plant.getShowSprite().getBoundingBox();
                if (CGRect.containsPoint(selectPlantBox1, cgPoint)) {
                    CCMoveTo moveTo = CCMoveTo.action(1, plant.getBgSprite().getPosition());
                    plant.getShowSprite().runAction(moveTo);
                    selectPlants.remove(plant);
                }
            }
        } else if (CGRect.containsPoint(boundingBox, cgPoint)) {
            //有可能选择植物
            if (selectPlants.size() < 5 && !isLock) {
                for (ShowPlant plant : showPlatns) {
                    CGRect boundingBox1 = plant.getShowSprite().getBoundingBox();
                    if (CGRect.containsPoint(boundingBox1, cgPoint)) {
                        System.out.println("我被选中了");
                        isLock = true;
                        CCMoveTo moveTo = CCMoveTo.action(0.5f, ccp(75 + selectPlants.size() * 53, 255));
                        CCSequence ccSequence = CCSequence.actions(moveTo, CCCallFunc.action(this, "unlock"));
                        plant.getShowSprite().runAction(ccSequence);
                        selectPlants.add(plant);
                    }
                }
            }
        }
        return super.ccTouchesBegan(event);
    }

    public void unlock() {
        isLock = false;
    }

}
